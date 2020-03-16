package com.maha.uds.Chat;

import java.util.Date;

public class MessageModel {

	private long creationDate;

	private String senderID;

	private String messageType;

	private String message;


	public MessageModel() {
	}

	public MessageModel(long creationDate, String senderID, String messageType, String message) {
		this.creationDate = creationDate;
		this.senderID = senderID;
		this.messageType = messageType;
		this.message = message;
	}

	public long getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(long creationDate) {
		this.creationDate = creationDate;
	}

	public String getSenderID() {
		return senderID;
	}

	public void setSenderID(String senderID) {
		this.senderID = senderID;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "MessageModel{" +
				"creationDate=" + creationDate +
				", senderID='" + senderID + '\'' +
				", messageType='" + messageType + '\'' +
				", message='" + message + '\'' +
				'}';
	}
}