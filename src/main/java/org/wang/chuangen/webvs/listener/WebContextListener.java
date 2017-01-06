package org.wang.chuangen.webvs.listener;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.lang3.StringUtils;
import org.wang.chuangen.webvs.util.Tool;

public class WebContextListener implements ServletContextListener{
	
	public static ServletContext context;
	private static String admin;
	private static String sourceCode;
	private static String publishPath;
	private static String ignoreFile = "\\.\\S*";
	private static String startWith = "[[";
	private static String endWith = "]]";
	
	public static ServletContext getContext() {
		return context;
	}
	
	public static String getAdmin(){
		return admin;
	}

	public static String getSourceCode() {
		return sourceCode;
	}

	public static String getPublishPath() {
		return publishPath;
	}

	public static String getIgnoreFile() {
		return ignoreFile;
	}

	public static String getStartWith() {
		return startWith;
	}

	public static String getEndWith() {
		return endWith;
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		context = event.getServletContext();
		admin = context.getInitParameter("admin");
		sourceCode = context.getInitParameter("sourceCode");
		publishPath = context.getInitParameter("publishPath");
		String ignoreFile = context.getInitParameter("ignoreFile");
		if(StringUtils.isNotBlank(ignoreFile)){
			WebContextListener.ignoreFile = ignoreFile;
		}
		String startWith = context.getInitParameter("startWith");
		if(StringUtils.isNotBlank(startWith)){
			WebContextListener.startWith = startWith;
		}
		String endWith = context.getInitParameter("endWith");
		if(StringUtils.isNotBlank(endWith)){
			WebContextListener.endWith = endWith;
		}
		File sourceCodeFile = new File(sourceCode);
		if(!sourceCodeFile.exists()){
			throw new RuntimeException("源sourceCode目录不存在:"+sourceCode);
		}
		File publishPathFile = new File(publishPath);
		if(!publishPathFile.exists()){
			throw new RuntimeException("publishPath不存在:"+publishPath);
		}
		String codeFileTypes = context.getInitParameter("codeFileTypes");
		if(StringUtils.isNotBlank(codeFileTypes)){
			String[] types = codeFileTypes.split(",");
			for (String fileType : types) {
				Tool.addCodeType(fileType);
			}
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		context = null;
	}
}
