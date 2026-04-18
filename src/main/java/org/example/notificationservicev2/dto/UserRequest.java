package org.example.notificationservicev2.dto;


public class UserRequest {

    private String recipient;     // email / phone / device token
    private String message;
    private String channel;       // EMAIL / SMS / PUSH

    // getters & setters (or Lombok)

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String toString() {
        return this.message + " " + this.channel + " " + ":name:" + this.recipient;
    }
}
