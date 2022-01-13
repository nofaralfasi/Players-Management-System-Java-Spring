package com.exam.demo.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WSService {

	private final SimpMessagingTemplate messagingTemplate;
	private static final String WS_MESSAGE_TRANSFER_DESTINATION = "/topic/messages";

	@Autowired
	public WSService(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}

	public void sendMessages(List<String> updatedPlayersIds) {
		for (String id : updatedPlayersIds) {
			messagingTemplate.convertAndSend(WS_MESSAGE_TRANSFER_DESTINATION,
					"Updated player #" + id + " at " + new Date().toString());
		}

	}

}