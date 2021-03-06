package com.jpmc.poc.notify;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware {

	private static ApplicationContext context;

	public ApplicationContext getApplicationContext() {
		return context;
	}

	
	public void setApplicationContext(ApplicationContext ac) throws BeansException {
		context = ac;
	}

}
