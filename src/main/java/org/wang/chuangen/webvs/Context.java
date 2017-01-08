package org.wang.chuangen.webvs;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Context{
	
	private static final String CONFIG_FILE="/config/applicationContext.xml";

	private static Context instance;
	
	private ApplicationContext applicationContext;
	
	private Context(){}
	
	public static synchronized Context getInstance(){
		if(instance==null)instance=new Context();
		return instance;
	}
	
	public void setApplicationContext(ApplicationContext applicationContext)throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	public ApplicationContext getApplicationContext(){
		if(applicationContext==null)applicationContext=new ClassPathXmlApplicationContext(CONFIG_FILE);
		return applicationContext;
	}
	
	public static Object getBean(String beanName){
		ApplicationContext context=getInstance().getApplicationContext();
		return context.getBean(beanName);
	}
	
	public static <T> T getBean(String name, Class<T> requiredType){
		ApplicationContext context=getInstance().getApplicationContext();
		return context.getBean(name, requiredType);
	}
	
	public static <T> T getBean(Class<T> requiredType){
		ApplicationContext context=getInstance().getApplicationContext();
		return context.getBean(requiredType);
	}
}
