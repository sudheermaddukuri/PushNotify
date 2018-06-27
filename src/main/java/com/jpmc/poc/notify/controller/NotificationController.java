package com.jpmc.poc.notify.controller;

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

import com.jpmc.poc.notify.controller.service.impl.MessageProducerService;
import com.jpmc.poc.notify.domain.Notify;
import com.jpmc.poc.notify.domain.Response;
import com.jpmc.poc.notify.security.TokenValidationClient;

/**
 * 
 * Main notification controller with the defined endpoints
 *
 */

@Controller
public class NotificationController {
	private static final Logger logger = Logger.getLogger(NotificationController.class);

	@Autowired
	MessageProducerService messageProducer;

	@Autowired
	SimpMessagingTemplate simpleMessaging;

	@Autowired
	WebSocketMessageBrokerStats wsBrokerStats;

	@MessageMapping("/pns")
	public void sockjs(@Payload Notify nf) throws Exception {
		logger.info("Received request over http on the endpoint pns");
		logger.debug("Received message over the endpoint pns:" + "Payload:" + nf.toString());
		Thread.sleep(3000);
		if (!nf.getId().equals("") && !nf.getCategory().toString().equals(""))
			this.simpleMessaging.convertAndSend("/topic/public/" + nf.getCategory().toString() + "/" + nf.getId(), nf);
		else
			this.simpleMessaging.convertAndSend("/topic/public", nf);
	}

	@RequestMapping(value = "/pnw", method = RequestMethod.POST)
	@ResponseBody
	public String sockw(@RequestBody String arg, @RequestHeader HttpHeaders headers) throws Exception {
		logger.info("Received request over http on the endpoint pnw");
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
	
	@RequestMapping(value="/check", method = RequestMethod.GET)
	@ResponseBody
	public String check() {
		return "CHECKED!!";
	}
}
