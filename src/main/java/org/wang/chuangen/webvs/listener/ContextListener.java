package org.wang.chuangen.webvs.listener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.wang.chuangen.webvs.service.ResourceService;
import org.wang.chuangen.webvs.util.RefreshDatabase;
import org.wang.chuangen.webvs.Context;

public class ContextListener implements ApplicationListener<ContextRefreshedEvent>{
	
	private boolean isExecuteOk = false;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		//isExecuteOk =true;
		if(isExecuteOk)return;
		isExecuteOk = true;
		ApplicationContext applicationContext=event.getApplicationContext();
		Context context=Context.getInstance();
		context.setApplicationContext(applicationContext);
		ResourceService resourceService=(ResourceService)context.getBean("resourceService");
		//resourceService.rebuildDatabase();
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
	}

}
