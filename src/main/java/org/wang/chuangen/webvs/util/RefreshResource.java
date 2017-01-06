package org.wang.chuangen.webvs.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.wang.chuangen.webvs.listener.WebContextListener;
import org.wang.chuangen.webvs.model.Resource;
import org.wang.chuangen.webvs.service.ResourceService;
import org.wang.chuangen.webvs.vo.ResourceRely;

/**
 * 刷新资源
 * @author 王传根
 * @date 2016-8-20 下午3:34:41
 */
public class RefreshResource implements Runnable{
	
	private String sourceCode;
	private String publishPath;
	private String ignoreFile;
	private String startWith="[[";
	private String endWith="]]";
	
	private ResourceService resourceService;
	
	private boolean isIgnore;
	private int id;
	
	public RefreshResource(ResourceService resourceService, int id){
		this.sourceCode = WebContextListener.getSourceCode();
		this.publishPath = WebContextListener.getPublishPath();
		this.ignoreFile = WebContextListener.getIgnoreFile();
		this.startWith = WebContextListener.getStartWith();
		this.endWith = WebContextListener.getEndWith();
		this.resourceService = resourceService;
		this.isIgnore = StringUtils.isNotBlank(ignoreFile);
		this.id = id;
	}
	
	public RefreshResource(String sourceCode, String publishPath, String ignoreFile, String startWith, String endWith, ResourceService resourceService, int id) {
		this.sourceCode = sourceCode;
		this.publishPath = publishPath;
		this.ignoreFile = ignoreFile;
		this.startWith = startWith;
		this.endWith = endWith;
		this.resourceService = resourceService;
		this.isIgnore = StringUtils.isNotBlank(ignoreFile);
		this.id = id;
	}

	@Override
	public void run() {
		//查询指定源数据
		Resource resource=resourceService.findResource(id);
		if(resource == null)return;
		List<Integer> invalidIds = new ArrayList<Integer>();
		Map<String, Resource> changeMap = new HashMap<String, Resource>();
		List<Resource> newResources = new ArrayList<Resource>();
		String url = resource.getUrl();
		String urlText = Tool.urlDecode(url);
		File file = new File(sourceCode + urlText);
		if(file.exists()){
			long updateTime = file.lastModified();
			if((updateTime - 1000) > resource.getUpdateTime()){
				resource.setUpdateTime(updateTime);
				changeMap.put(resource.getUrl(), resource);
				if(Resource.DIRECTORY.equals(resource.getUrlType())){
					refreshDirectory(file, resource, urlText, false, invalidIds, changeMap, newResources);
				}
			}
		}else{
			if(Resource.DIRECTORY.equals(resource.getUrlType())){
				List<Resource> sonResources = resourceService.findSonResource(resource.getId());
				resource.setChildren(sonResources);
				disposeInvalidDirectory(resource, invalidIds);
			}else{
				invalidIds.add(resource.getId());
			}
		}
		if(invalidIds.size() > 0)resourceService.deleteInvalidResource(invalidIds);
		Map<Integer, List<ResourceRely>> relyIdMap = new HashMap<Integer, List<ResourceRely>>();
		if(changeMap.size() > 0){
			List<Integer> relyIds = new ArrayList<Integer>();
			List<Resource> changeResources = new ArrayList<Resource>(changeMap.values());
			for (Resource changeResource : changeResources) {
				relyIds.add(changeResource.getId());
			}
			for (int i = changeResources.size() - 1; i > -1; i--) {
				Resource changeResource = changeResources.get(i);
				changeResource.refreshVersionNumber();
				List<Resource> tempResources = new ArrayList<Resource>();
				resourceService.findRelyResource(changeResource.getId(), tempResources, relyIds);
				if(tempResources != null && tempResources.size() > 0){
					for (Resource tempResource : tempResources) {
						tempResource.refreshVersionNumber();
						changeMap.put(tempResource.getLabel(), tempResource);
						changeResources.add(tempResource);
					}
				}
			}
			resourceService.updateResourceVersions(changeResources);
			for (Resource changeResource : changeResources) {
				String urlType = changeResource.getUrlType();
				if(Resource.FILE.equals(urlType)){
					urlText = Tool.urlDecode(changeResource.getUrl());
					Tool.fileCopy(sourceCode + urlText, publishPath + urlText);
				}else if(Resource.CODE.equals(urlType)){
					urlText = Tool.urlDecode(changeResource.getUrl());
					copyCode(changeResource, sourceCode + urlText, publishPath + urlText, changeMap, relyIdMap);
				}
			}
		}
		if(relyIdMap.size() > 0)resourceService.deleteResourceRelyByIds(new ArrayList<Integer>(relyIdMap.keySet()));
		if(newResources.size() > 0){
			resourceService.addResources(newResources);
			for (Resource newResource : newResources) {
				String urlType = newResource.getUrlType();
				if(Resource.FILE.equals(urlType)){
					urlText = Tool.urlDecode(newResource.getUrl());
					Tool.fileCopy(sourceCode + urlText, publishPath + urlText);
				}else if(Resource.CODE.equals(urlType)){
					urlText = Tool.urlDecode(newResource.getUrl());
					copyCode(newResource, sourceCode + urlText, publishPath + urlText, changeMap, relyIdMap);
				}
			}
		}
		if(relyIdMap.size() > 0){
			List<ResourceRely> resourceRelies = new ArrayList<ResourceRely>();
			for (Entry<Integer, List<ResourceRely>> resourceRelyEntry : relyIdMap.entrySet()) {
				List<ResourceRely> relyIds = resourceRelyEntry.getValue();
				if(relyIds != null && relyIds.size() > 0)resourceRelies.addAll(relyIds);
			}
			if(resourceRelies.size() > 0)resourceService.addResourceRelies(resourceRelies);
		}
	}

	/**
	 * 刷新目录
	 * @param resource
	 * @author 王传根
	 * @date 2016-8-20 下午4:02:17
	 */
	public void refreshDirectory(File resourceFile, Resource resource, String urlText, boolean isNewFile, List<Integer> invalidIds, Map<String, Resource> changeMap, List<Resource> newResources){
		Map<String, Resource> urlMap = new HashMap<String, Resource>();
		if(!isNewFile){
			List<Resource> sonResourceList = resourceService.findSonResourceList(resource.getId());
			for (Resource sonResource : sonResourceList) {
				String sonUrlText = urlText + File.separator + sonResource.getFileName();
				String sonUrl = resource.getUrl();
				File file = new File(sourceCode + sonUrlText);
				if(file.exists()){
					urlMap.put(sonUrl, sonResource);
					long updateTime = file.lastModified();
					if((updateTime - 1000) > sonResource.getUpdateTime()){
						sonResource.setUpdateTime(updateTime);
						sonResource.refreshVersionNumber();
						changeMap.put(sonResource.getLabel(), sonResource);
						if(Resource.DIRECTORY.equals(sonResource.getUrlType())){
							refreshDirectory(file, sonResource, sonUrlText, false, invalidIds, changeMap, newResources);
						}
					}
				}else{
					if(Resource.DIRECTORY.equals(sonResource.getUrlType())){
						List<Resource> sonResources = resourceService.findSonResource(resource.getId());
						resource.setChildren(sonResources);
						disposeInvalidDirectory(sonResource, invalidIds);
					}else{
						invalidIds.add(sonResource.getId());
					}
				}
			}
		}
		String[] fileNames = resourceFile.list();
		for (String fileName : fileNames) {
			if(isIgnore){
				if(fileName.matches(ignoreFile))continue;
			}
			String sonUrlText = urlText + File.separator + fileName;
			String sonUrl = Tool.urlEncode(sonUrlText);
			if(urlMap.size() == 0 || !urlMap.containsKey(sonUrl)){
				File file = new File(sourceCode + sonUrlText);
				long updateTime = file.lastModified();
				if(file.isDirectory()){
					Resource sonResource = new Resource(resource.getId(), sonUrl, Resource.DIRECTORY, fileName, "", 0, updateTime);
					resourceService.addResource(resource);
					refreshDirectory(file, sonResource, sonUrlText, true, invalidIds, changeMap, newResources);
				}else{
					String urlType = Tool.isCodeFile(fileName)?Resource.CODE:Resource.FILE;
					Resource sonResource = new Resource(resource.getId(), sonUrl, urlType, fileName, Tool.createTimeLabel(), 0, updateTime);
					newResources.add(sonResource);
				}
			}
		}
	}
	
	/**
	 * 处理无效目录
	 * @param resource
	 * @param invalidIds
	 * @author 王传根
	 * @date 2016-8-20 下午4:28:42
	 */
	public void disposeInvalidDirectory(Resource resource, List<Integer> invalidIds){
		invalidIds.add(resource.getId());
		List<Resource> sonResources = resource.getChildren();
		if(sonResources != null && sonResources.size() > 0){
			for (Resource sonResource : sonResources) {
				if(Resource.DIRECTORY.equals(sonResource.getUrlType())){
					disposeInvalidDirectory(sonResource, invalidIds);
				}else{
					invalidIds.add(sonResource.getId());
				}
			}
		}
	}
	
	/**
	 * 拷贝code
	 * @param resource
	 * @param sourceFile
	 * @param destPath
	 * @param changeMap
	 * @author 王传根
	 * @date 2016-8-20 下午8:01:12
	 */
	public void copyCode(Resource resource, String sourceFile, String destPath, Map<String, Resource> changeMap, Map<Integer, List<ResourceRely>> relyIdMap){
		File destFile = new File(destPath);
		destFile.getParentFile().mkdirs();
		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			br = new BufferedReader(new FileReader(sourceFile));
			bw = new BufferedWriter(new FileWriter(destFile));
			String tempText = null;
			while ((tempText = br.readLine()) != null) {
				int begin = tempText.length();
				int end = 0;
				do {
					begin = tempText.lastIndexOf(startWith, begin);
					if(begin > 0){
						end = tempText.indexOf(endWith, begin);
						if(end > 0){
							String label = tempText.substring(begin + startWith.length(), end);
							Resource labelResource = changeMap.get(label);
							if(labelResource == null){
								labelResource = resourceService.findLabelVersion(label);
							}
							int version = 0;
							if(labelResource != null){
								int id = resource.getId();
								int relyId = labelResource.getId();
								List<ResourceRely> relyIds = relyIdMap.get(id);
								if(relyIds == null){
									relyIds = new ArrayList<ResourceRely>();
									relyIdMap.put(id, relyIds);
								}
								relyIds.add(new ResourceRely(id, relyId));
								version = labelResource.getVersionNumber();
							}
							tempText = tempText.replace(startWith + label + endWith, "" + version);
						}
					}
				} while (begin > 0);
				bw.write(tempText);
				bw.newLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				if(bw != null){
					bw.flush();
					bw.close();
				}
				if(br != null){
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
