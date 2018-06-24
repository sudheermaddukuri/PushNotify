package com.ca.notify.appconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationLoader {
	
	@Bean(name="config",initMethod="init")
	public ConfigurationBean config(){
		return new ConfigurationBean();
	}
}
