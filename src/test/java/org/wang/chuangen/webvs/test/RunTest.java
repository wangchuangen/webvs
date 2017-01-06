package org.wang.chuangen.webvs.test;

import org.wang.chuangen.webvs.service.ResourceService;
import org.wang.chuangen.webvs.util.RefreshDatabase;
import org.wang.chuangen.webvs.util.Tool;
import org.wang.chuangen.webvs.Context;

public class RunTest {

	public static void main(String[] args) {
		System.out.println("测试开始...");
		ResourceService resourceService=(ResourceService)Context.getBean("resourceService");
		Tool.addCodeType("html");
		Tool.addCodeType("htm");
		int s=resourceService.isExistTable("RESOURCE");
		if(s == 0){
			resourceService.createResourceTable();
			resourceService.createUrlIndex();
		}
		s=resourceService.isExistTable("RESOURCE_RELY");
		if(s == 0){
			resourceService.createResourceRelyTable();
		}
		new RefreshDatabase(resourceService).run();
		System.exit(0);
	}
}
