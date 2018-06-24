package com.ca.notify.appconfig;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.ca.notify.security.Crypto;
import com.ca.notify.util.StringConstants;

public class ConfigurationBean {

	private static final Logger logger = Logger.getLogger(ConfigurationBean.class);

	@Value("${sEndPoint}")
	private String endPoint;

	@Value("${origins}")
	private String origins;

	@Value("#{'${server.ssl.key-store:\"\"}'}")
	private String keystore;

	@Value("#{'${server.ssl.key-store-password:\"\"}'}")
	private String keystorePwd;

	@Value("#{'${server.ssl.trust-store:\"\"}'}")
	private String truststore;

	@Value("#{'${server.ssl.trust-store-password:\"\"}'}")
	private String truststorePwd;
	
	@Value("#{'${concurrentRouter:\"\"}'}")
	private String concurrentConsumers;
	
	@Value("#{'${subEPTimeout:\"\"}'}")
	private String sockJsEpTimeout;
	
	@Value(("#{'${routeWithinVm:true}'}"))
	private String routeWithinVm;
	
	@Value("#{'${token:\"\"}'}")
	private String token;
	
	@Value("#{'${key:\"\"}'}")
	private String key;
	
	public static Map<String, String> config = new HashMap<String, String>();

	public void init() {
		logger.info("Loading configurations");
		if (endPoint != null)
			config.put(StringConstants.SOCK_JS_ENDPOINT, endPoint);
		else
			config.put(StringConstants.SOCK_JS_ENDPOINT, "http://localhost:8080/capush/capns");

		if (origins != null)
			config.put(StringConstants.ORIGIN, origins);
		else
			config.put(StringConstants.ORIGIN, "*");
		
		config.put(StringConstants.KEYSTORE, keystore);

		config.put(StringConstants.KEYSTORE_PWD, keystorePwd);

		config.put(StringConstants.TRUSTSTORE, truststore);

		config.put(StringConstants.TRUSTSTORE_PWD, truststorePwd);
		
		config.put(StringConstants.CONCURR_ROUTER, concurrentConsumers);
		
		config.put(StringConstants.SOCKJS_TIMEOUT, sockJsEpTimeout);
		
		config.put(StringConstants.ROUTE_WITHIN_VM, routeWithinVm);
		
		key = Crypto.decode(key);
		config.put(StringConstants.KEY, key);
		
		token = Crypto.decrypt(key, StringConstants.INIT_VECTOR, token);
		config.put(StringConstants.TOKEN, token);
	}
}
