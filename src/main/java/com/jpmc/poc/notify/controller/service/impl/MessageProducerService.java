package com.jpmc.poc.notify.controller.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.jpmc.poc.notify.connectors.MessageBusProducer;
import com.jpmc.poc.notify.domain.Notify;
import com.jpmc.poc.notify.domain.Response;

@Service
public class MessageProducerService {
	private static final Logger logger = Logger.getLogger(MessageProducerService.class);

	@Autowired
	Gson gson;

	@Autowired
	Notify notify;

	public Response run(String msg) {
		Response resp = new Response();
		notify = gson.fromJson(msg, Notify.class);
		if (notify.getId() == null || notify.getCategory() == null || notify.getArg() == null){
			resp.setErrNErr(false);
			resp.setMessage("Invalid Payload");
			return resp;
		}
		else {
			notify.setTimestamp(Long.toString(System.currentTimeMillis()));
			MessageBusProducer conn = new MessageBusProducer();
			conn.sendMessage(gson.toJson(notify, Notify.class), "public");
			resp.setErrNErr(true);
			resp.setMessage("Message Routed");
			return resp;
		}
	}
}
