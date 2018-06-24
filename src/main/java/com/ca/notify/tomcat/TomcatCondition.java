//package com.ca.notify.tomcat;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Condition;
//import org.springframework.context.annotation.ConditionContext;
//import org.springframework.core.type.AnnotatedTypeMetadata;
//
//public class TomcatCondition implements Condition{
//
//	@Value("#{'${server.ssl.key-store:\"\"}'}")
//	private String keyStore;
//	
//	@Override
//	public boolean matches(ConditionContext arg0, AnnotatedTypeMetadata arg1) {
//		if(keyStore==null)
//			return false;
//		else return true;
//	}
//	
//}
