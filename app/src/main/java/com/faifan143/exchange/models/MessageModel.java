package com.faifan143.exchange.models;

// Message.java
public class MessageModel {
    private String messageBody; // message body
    private String receiverEmail; // data of the user that sent this message
    private String receiverNumber;
    private String receiverName;
    private String sendTime;
    private String key;

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getReceiverNumber() {
        return receiverNumber;
    }

    public void setReceiverNumber(String receiverNumber) {
        this.receiverNumber = receiverNumber;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public MessageModel(String messageBody, String receiverEmail, String receiverNumber, String receiverName, String sendTime) {
        this.messageBody = messageBody;
        this.receiverEmail = receiverEmail;
        this.receiverNumber = receiverNumber;
        this.receiverName = receiverName;
        this.sendTime = sendTime;
    }

    public MessageModel() {
    }
}