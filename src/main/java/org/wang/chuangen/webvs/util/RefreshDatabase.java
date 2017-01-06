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
 * 刷新全部数据
 * @author 王传根
 * @date 2016-8-19 下午8:03:53
 */
public class RefreshDatabase implements Runnable{
	
	public static final int MIN_REFRESH_TIME = 1000 * 60 * 1;
	private static long refreshTime;
	
	private String sourceCode;
	private String publishPath;
	private String ignoreFile;
	private String startWith="[[";
	private String endWith="]]";
	
	private ResourceService resourceService;
	
	private boolean isIgnore;
	
	public RefreshDatabase(ResourceService resourceService){
		this.sourceCode = WebContextListener.getSourceCode();
		this.publishPath = WebContextListener.getPublishPath();
		this.ignoreFile = WebContextListener.getIgnoreFile();
		this.startWith = WebContextListener.getStartWith();
		this.endWith = WebContextListener.getEndWith();
		this.resourceService = resourceService;
		this.isIgnore = StringUtils.isNotBlank(ignoreFile);
	}

	public RefreshDatabase(String sourceCode, String publishPath, String ignoreFile, String startWith, String endWith, ResourceService resourceService) {
		this.sourceCode = sourceCode;
		this.publishPath = publishPath;
		this.ignoreFile = ignoreFile;
		this.startWith = startWith;
		this.endWith = endWith;
		this.resourceService = resourceService;
		this.isIgnore = StringUtils.isNotBlank(ignoreFile);
	}
	
	/**
	 * 刷新时间
	 * @return
	 * @author 王传根
	 * @date 2016-8-20 下午1:58:06
	 */
	public static long getRefreshTime(){
		return refreshTime;
	}

	@Override
	public void run() {
		refreshTime = System.currentTimeMillis();
		if(StringUtils.isBlank(sourceCode))sourceCode = "E:/web/PHP/www";
		if(StringUtils.isBlank(publishPath))publishPath = "E:/web/PHP/aa";
		//查询所有源数据
		List<Resource> resources = resourceService.findAllResource();
		Map<String, Resource> urlMap = new HashMap<String, Resource>();
		Map<String, Resource> labelMap = new HashMap<String, Resource>();
		//验证文件是否存在,文件不存在删除无效数据
		List<Integer> invalidResourceIds = new ArrayList<Integer>();
		for (Resource resource : resources) {
			resource.newFile(sourceCode);
			if(resource.fileExists()){
				urlMap.put(resource.getUrl(), resource);
				labelMap.put(resource.getLabel(), resource);
			}else{
				invalidResourceIds.add(resource.getId());
			}
		}
		if(invalidResourceIds.size() > 0)resourceService.deleteInvalidResource(invalidResourceIds);
		//刷新文件
		File sourceCodeFile = new File(sourceCode);
		String[] sourceCodeFiles = sourceCodeFile.list();
		List<Resource> changeResources = new ArrayList<Resource>();
		List<Resource> newResources = new ArrayList<Resource>();
		Map<Integer, List<ResourceRely>> relyIdMap = new HashMap<Integer, List<ResourceRely>>();
		refreshFiles(0, "", sourceCodeFiles, urlMap, labelMap, changeResources, newResources, relyIdMap);
		if(changeResources.size() > 0)resourceService.updateResourceVersions(changeResources);
		if(newResources.size() > 0)resourceService.addResources(newResources);
		if(relyIdMap.size() > 0){
			resourceService.deleteResourceRelyByIds(new ArrayList<Integer>(relyIdMap.keySet()));
			List<ResourceRely> resourceRelies = new ArrayList<ResourceRely>();
			for (Entry<Integer, List<ResourceRely>> resourceRelyEntry : relyIdMap.entrySet()) {
				List<ResourceRely> relyIds = resourceRelyEntry.getValue();
				if(relyIds != null && relyIds.size() > 0)resourceRelies.addAll(relyIds);
			}
			if(resourceRelies.size() > 0)resourceService.addResourceRelies(resourceRelies);
		}
	}

	/**
	 * 刷新文件
	 * @param sourceFiles
	 * @param urlMap
	 * @param labelMap
	 * @param updateMap
	 * @param newMap
	 * @author 王传根
	 * @date 2016-8-19 下午8:26:05
	 */
	public void refreshFiles(int parentId, String parentUrlText, String[] sourceFiles, Map<String, Resource> urlMap, Map<String, Resource> labelMap, List<Resource> changeResources, List<Resource> newResources, Map<Integer, List<ResourceRely>> relyIdMap){
		for (String fileName : sourceFiles) {
			if(isIgnore){
				if(fileName.matches(ignoreFile))continue;
			}
			String urlText = parentUrlText + File.separator + fileName;
			String url = Tool.urlEncode(urlText);
			Resource resource = urlMap.get(url);
			File file = (resource != null)?resource.getFile():new File(sourceCode + File.separator + urlText);
			//获取文件修改时间
			long updateTime = file.lastModified();
			if(file.isDirectory()){
				if(resource == null){
					resource = new Resource(parentId, url, Resource.DIRECTORY, fileName, "", 0, updateTime);
					resourceService.addResource(resource);
				}else{
					if((updateTime - 1000) > resource.getUpdateTime()){
						resource.setUpdateTime(updateTime);
						changeResources.add(resource);
					}
				}
				//递归刷新子目录
				String[] sourceCodeFiles = file.list();
				refreshFiles(resource.getId(), urlText, sourceCodeFiles, urlMap, labelMap, changeResources, newResources, relyIdMap);
			}else{
				//检查文件版本
				if(resource == null){
					String urlType = Tool.isCodeFile(fileName)?Resource.CODE:Resource.FILE;
					resource = new Resource(parentId, url, urlType, fileName, Tool.createTimeLabel(), 0, updateTime);
					checkVersion(file, resource, urlText, updateTime, true, labelMap, changeResources, relyIdMap);
					newResources.add(resource);
				}else{
					checkVersion(file, resource, urlText, updateTime, false, labelMap, changeResources, relyIdMap);
					if(resource.isNewVersion()){
						changeResources.add(resource);
					}
				}
			}
		}
	}
	
	/**
	 * 检查文件版本
	 * @param sourceFile
	 * @param resource
	 * @param labelMap
	 * @param changeMap
	 * @param isNewFile
	 * @return
	 * @author 王传根
	 * @date 2016-8-20 上午8:24:11
	 */
	public void checkVersion(File sourceFile, Resource resource, String urlText, long updateTime, boolean isNewFile, Map<String, Resource> labelMap, List<Resource> changeResources, Map<Integer, List<ResourceRely>> relyIdMap){
		if(resource.isCheckVersionOK())return;
		String fileName = sourceFile.getName();
		String destPath = publishPath + urlText;
		if(Tool.isCodeFile(fileName)){
			//代码类文件 读取源文件
			File destFile = new File(destPath);
			destFile.getParentFile().mkdirs();
			boolean newVersion = false;
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
								Resource labelResource = labelMap.get(label);
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
									File file = labelResource.getFile();
									String fileUrl = labelResource.getUrl();
									String fileUrlText = Tool.urlDecode(fileUrl);
									checkVersion(file, labelResource, fileUrlText, file.lastModified(), false, labelMap, changeResources, relyIdMap);
									version = labelResource.getVersionNumber();
									if(labelResource.isNewVersion()){
										long labelUpdateTime = labelResource.getUpdateTime();
										if(labelUpdateTime > updateTime)updateTime = labelResource.getUpdateTime();
										changeResources.add(labelResource);
										newVersion = true;
									}
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
			if(!newVersion){
				newVersion = ((updateTime - 1000) > resource.getUpdateTime());
			}
			if(newVersion){
				resource.refreshVersionNumber();
				resource.setUpdateTime(updateTime);
			}
		}else{
			//非代码类文件 检查修改时间
			if((updateTime - 1000) > resource.getUpdateTime()){
				resource.refreshVersionNumber();
				resource.setUpdateTime(updateTime);
			}
			Tool.fileCopy(sourceFile, destPath);
		}
		resource.checkVersionOK();
	}
}
