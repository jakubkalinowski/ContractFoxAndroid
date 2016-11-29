package com.example.jakubkalinowski.contractfoxandroid.Model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by MD on 11/16/2016.
 */

@IgnoreExtraProperties

public class ChatMessage {

    public ChatMessage(){}


    public ChatMessage(String senderUserId, String recieverUserId,
                       Map<String, Object> chatMessageServerTimeStamp,
                       String base64ImageString, String chatMessagePushID,
                       String referencedChatSessionPushId,
                       String messageText) {
        this.senderUserId = senderUserId;
        this.recieverUserId = recieverUserId;
        this.chatMessageServerTimeStamp = chatMessageServerTimeStamp;
        this.base64ImageString = base64ImageString;
        this.chatMessagePushID = chatMessagePushID;
        this.referencedChatSessionPushId = referencedChatSessionPushId;
        this.messageText = messageText;
    }

    public String senderUserId;
    public String recieverUserId;
    public Map<String, Object> chatMessageServerTimeStamp;
    public String base64ImageString;
    public String chatMessagePushID;
    public String referencedChatSessionPushId;
    public String messageText;

//    public Map<String, Object> createdAtFirebaseTimestamp;
//    public Map<String, Object> lastMessageRecievedFirebaseTimestamp;

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }

    public String getRecieverUserId() {
        return recieverUserId;
    }

    public void setRecieverUserId(String recieverUserId) {
        this.recieverUserId = recieverUserId;
    }

    public Map<String, Object> getChatMessageServerTimeStamp() {
        return chatMessageServerTimeStamp;
    }

    public void setChatMessageServerTimeStamp
            (Map<String, Object> chatMessageServerTimeStamp) {
        this.chatMessageServerTimeStamp = chatMessageServerTimeStamp;
    }

    public String getBase64ImageString() {
        return base64ImageString;
    }

    public void setBase64ImageString(String base64ImageString) {
        this.base64ImageString = base64ImageString;
    }

    public String getChatMessagePushID() {
        return chatMessagePushID;
    }

    public void setChatMessagePushID(String chatMessagePushID) {
        this.chatMessagePushID = chatMessagePushID;
    }

    public String getReferencedChatSessionPushId() {
        return referencedChatSessionPushId;
    }

    public void setReferencedChatSessionPushId(String referencedChatSessionPushId) {
        this.referencedChatSessionPushId = referencedChatSessionPushId;
    }

    @Exclude
    public long getChatMessageServerTimestampLong(){
        return (long)chatMessageServerTimeStamp.get("date");
    }

    @Exclude
    public String getChatMessageTimestampTimeFormattedString(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        c.setTimeInMillis((long) chatMessageServerTimeStamp.get("date"));
        return sdf.format(c.getTime());
    }

    @Exclude
    public String getChatMessageFormattedTimestampDateFormattedString(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        c.setTimeInMillis((long) chatMessageServerTimeStamp.get("date"));
        return sdf.format(c.getTime());
    }


}
