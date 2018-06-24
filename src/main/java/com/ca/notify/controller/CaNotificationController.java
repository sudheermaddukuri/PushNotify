package com.ca.notify.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.config.WebSocketMessageBrokerStats;

import com.ca.notify.controller.service.impl.MessageProducerService;
import com.ca.notify.domain.Notify;
import com.ca.notify.domain.Response;
import com.ca.notify.security.TokenValidationClient;

/**
 * 
 * Main notification controller with the defined endpoints
 *
 */

@Controller
public class CaNotificationController {
	private static final Logger logger = Logger.getLogger(CaNotificationController.class);

	@Autowired
	MessageProducerService messageProducer;

	@Autowired
	SimpMessagingTemplate simpleMessaging;

	@Autowired
	WebSocketMessageBrokerStats wsBrokerStats;

	@MessageMapping("/capns")
	public void sockjs(@Payload Notify nf) throws Exception {
		logger.info("Received request over http on the endpoint capns");
		logger.debug("Received message over the endpoint capns:" + "Payload:" + nf.toString());
		Thread.sleep(3000);
		if (!nf.getId().equals("") && !nf.getCategory().toString().equals(""))
			this.simpleMessaging.convertAndSend("/topic/public/" + nf.getCategory().toString() + "/" + nf.getId(), nf);
		else
			this.simpleMessaging.convertAndSend("/topic/public", nf);
	}

	@RequestMapping(value = "/capnw", method = RequestMethod.POST)
	@ResponseBody
	public String sockw(@RequestBody String arg, @RequestHeader HttpHeaders headers) throws Exception {
		logger.info("Received request over http on the endpoint capnw");
		logger.debug("Received message over the endpoint capnw:" + arg);
		Thread.sleep(1000);
		Response resp = TokenValidationClient.authenticate(headers);
		if (resp.getErrNErr()) {
			Response result = messageProducer.run(arg);
			if (!result.getErrNErr())
				return result.getMessage();
			else
				return result.getMessage();
		} else {
			return Integer.toString(HttpServletResponse.SC_FORBIDDEN) + ":" + resp.getMessage();
		}
	}

	@RequestMapping("/stats")
	public @ResponseBody WebSocketMessageBrokerStats showStats() {
		return wsBrokerStats;
	}
}
