package org.wang.chuangen.webvs.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Tool {

	private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static Set<String> codeTypes = new HashSet<String>();
	
	/**
	 * 格式化指定日期
	 * @param date
	 * @return
	 */
	public static String getDateTime(Date date){
		if(date==null)return "";
		return dateTimeFormat.format(date);
	}
	
	/**
	 * 格式化指定时间
	 * @param time
	 * @return 日期时间
	 */
	public static String getDateTime(Timestamp time){
		if(time==null)return "";
		return dateTimeFormat.format(time);
	}
	
	/**
	 * URL编码
	 * @param urlText
	 * @return
	 */
	public static String urlEncode(String urlText){
		try {
			return URLEncoder.encode(urlText,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return urlText;
	}
	
	/**
	 * URL解码
	 * @param urlText
	 * @return
	 */
	public static String urlDecode(String urlText){
		try {
			return URLDecoder.decode(urlText,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return urlText;
	}
	
	/**
	 * 根据时间生成标签
	 * @return
	 * @author 王传根
	 * @date 2016-8-19 下午7:34:52
	 */
	public static synchronized String createTimeLabel(){
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		StringBuilder text=new StringBuilder("_");
		long time = System.currentTimeMillis();
		while(time > 0){
			char x = (char)((int)(time%26) + 'A');
			text.append(x);
			time = time / 26;
		}
		return text.toString();
	}
	
	/**
	 * 添加codeType
	 * @param fileType
	 * @author 王传根
	 * @date 2016-8-19 下午10:16:45
	 */
	public static void addCodeType(String fileType){
		if(fileType == null)return;
		fileType = fileType.trim().toLowerCase();
		if(fileType.length() == 0)return;
		if(fileType.charAt(0) == '.'){
			fileType = fileType.substring(1);
		}
		codeTypes.add(fileType);
	}
	
	/**
	 * 判断codeType
	 * @param fileName
	 * @return
	 * @author 王传根
	 * @date 2016-8-19 下午10:17:56
	 */
	public static boolean isCodeFile(String fileName){
		if(fileName == null)return false;
		int s = fileName.lastIndexOf('.');
		if(s < 0)return false;
		String fileType = fileName.substring(s + 1).trim().toLowerCase();
		return codeTypes.contains(fileType);
	}
	
	/**
	 * 清空codeType
	 * @author 王传根
	 * @date 2016-8-19 下午10:18:57
	 */
	public static void clearCodeType(){
		codeTypes.clear();
	}
	
	/**
	 * 生成JSON
	 * @param data
	 * @return
	 */
	public static String toJSON(Object data){
		return JSON.toJSONString(data,SerializerFeature.DisableCircularReferenceDetect);
	}
	
	/**
	 * 文件拷贝
	 * @param sourceFile
	 * @param destPath
	 * @author 王传根
	 * @date 2016-8-20 上午7:25:02
	 */
	public static void fileCopy(String sourceFile, String destPath){
		File destFile = new File(destPath);
		destFile.getParentFile().mkdirs();
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(sourceFile));
			bos = new BufferedOutputStream(new FileOutputStream(destFile));
			byte[] temp = new byte[1024];
			int length = 0;
			while((length = bis.read(temp)) > 0){
				bos.write(temp, 0, length);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(bos != null){
					bos.flush();
					bos.close();
				}
				if(bis != null){
					bis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 文件拷贝
	 * @param sourceFile
	 * @param destPath
	 * @author 王传根
	 * @date 2016-8-20 上午7:25:02
	 */
	public static void fileCopy(File sourceFile, String destPath){
		File destFile = new File(destPath);
		destFile.getParentFile().mkdirs();
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(sourceFile));
			bos = new BufferedOutputStream(new FileOutputStream(destFile));
			byte[] temp = new byte[1024];
			int length = 0;
			while((length = bis.read(temp)) > 0){
				bos.write(temp, 0, length);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(bos != null){
					bos.flush();
					bos.close();
				}
				if(bis != null){
					bis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
