package org.wang.chuangen.webvs.model;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;

import org.wang.chuangen.webvs.util.Tool;

/**
 * 资源实体类
 * @author 王传根
 * @date 2016-8-19 上午10:49:56
 */
public class Resource {
	
	public static final String DIRECTORY="directory";
	public static final String CODE="code";
	public static final String FILE="file";

	private int id;
	private int parentId;
	private String url;
	private String urlType;
	private String fileName;
	private String label;
	private int versionNumber;
	private long updateTime;
	
	private List<Resource> children;
	
	private File file;
	private boolean newVersion = false;
	private boolean checkVersion = false;
	
	public Resource(){}
	
	public Resource(int parentId, String url, String urlType, String fileName, String label, int versionNumber, long updateTime) {
		this.parentId = parentId;
		this.url = url;
		this.urlType = urlType;
		this.fileName = fileName;
		this.label = label;
		this.versionNumber = versionNumber;
		this.updateTime = updateTime;
		this.checkVersion = true;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrlType() {
		return urlType;
	}

	public void setUrlType(String urlType) {
		this.urlType = urlType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(int versionNumber) {
		this.versionNumber = versionNumber;
	}
	
	public void refreshVersionNumber(){
		this.newVersion = true;
		this.versionNumber++;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	
	public String getUpdateTimeText(){
		return Tool.getDateTime(new Timestamp(updateTime));
	}

	public List<Resource> getChildren() {
		return children;
	}

	public void setChildren(List<Resource> children) {
		this.children = children;
	}

	public void newFile(String sourceCode){
		String urlText = Tool.urlDecode(url);
		file = new File(sourceCode + urlText);
	}
	
	public boolean fileExists(){
		if(file == null)return false;
		return file.exists();
	}
	
	public File getFile(){
		return file;
	}
	
	public boolean isNewVersion() {
		return newVersion;
	}

	public boolean isCheckVersionOK() {
		return checkVersion;
	}

	public void checkVersionOK() {
		this.checkVersion = true;
	}
	
	public String getState() {
		if(Resource.DIRECTORY.equals(urlType))return "closed";
		return null;
	}
}
