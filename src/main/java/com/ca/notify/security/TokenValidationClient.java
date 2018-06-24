package com.ca.notify.security;

import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;

import com.ca.notify.appconfig.ConfigurationBean;
import com.ca.notify.domain.Response;
import com.ca.notify.util.StringConstants;

public class TokenValidationClient {

	private static final Logger logger = Logger.getLogger(TokenValidationClient.class);

	public static Response authenticate(HttpHeaders headers) {
		if (headers.get("ca-push-notify-token") != null) {
			String token = headers.get("ca-push-notify-token").get(0);
			//if (headers.get("ca-push-notify-token").get(0).equals(DummyTokens.TOKEN)) {
			if (verifyToken(token)) {
				Response resp = new Response();
				resp.setErrNErr(true);
				resp.setMessage("Message Routed");
				return resp;
			} else {
				Response resp = new Response();
				resp.setErrNErr(false);
				resp.setMessage("Access Denied");
				return resp;
			}
		} else {
			logger.error("Authentication header not present in the request");
			Response resp = new Response();
			resp.setErrNErr(false);
			resp.setMessage("Access Denied");
			return resp;
		}
	}
	
	public static boolean verifyToken(String token) {
		String key = ConfigurationBean.config.get(StringConstants.KEY);
		String headerToken = Crypto.decrypt(key, StringConstants.INIT_VECTOR, token);
		String originalToken = ConfigurationBean.config.get(StringConstants.TOKEN);
		
		if(headerToken != null && headerToken.equals(originalToken))
			return true;
		else
			return false;
		
	}
}
