package com.example.jakubkalinowski.contractfoxandroid.Model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by MD on 11/16/2016.
 */
@IgnoreExtraProperties
public class ChatSession {


    public String firebasePushId;
    public Map<String, Boolean> usersInChat = new HashMap<String, Boolean>();
    public String projectTitle;
    public String lastMessageRecieved;
    public Map<String, Object> createdAtFirebaseTimestamp;
    public Map<String, Object> lastMessageRecievedFirebaseTimestamp;



    public ChatSession() {
    }

    public ChatSession(Map<String, Object> createdAtFirebaseTimestamp, String firebasePushId,
                       Map<String, Boolean> usersInChat, String projectTitle,
                       String lastMessageRecieved, Map<String, Object>
                               lastMessageRecievedFirebaseTimestamp) {
        this.createdAtFirebaseTimestamp = createdAtFirebaseTimestamp;
        this.firebasePushId = firebasePushId;
        this.usersInChat = usersInChat;
        this.projectTitle = projectTitle;
        this.lastMessageRecieved = lastMessageRecieved;
        this.lastMessageRecievedFirebaseTimestamp = lastMessageRecievedFirebaseTimestamp;
    }




    public String getFirebasePushId() {
        return firebasePushId;
    }

    public void setFirebasePushId(String firebasePushId) {
        this.firebasePushId = firebasePushId;
    }

    public Map<String, Boolean> getUsersInChat() {
        return usersInChat;
    }

    public void setUsersInChat(Map<String, Boolean> usersInChat) {
        this.usersInChat = usersInChat;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getLastMessageRecieved() {
        return lastMessageRecieved;
    }

    public void setLastMessageRecieved(String lastMessageRecieved) {
        this.lastMessageRecieved = lastMessageRecieved;
    }

    public Map<String, Object> getCreatedAtFirebaseTimestamp() {
        return createdAtFirebaseTimestamp;
    }

    public Map<String, Object> getLastMessageRecievedFirebaseTimestamp() {
        return lastMessageRecievedFirebaseTimestamp;
    }



    @Exclude
    public long getCreatedAtFirebaseTimestampLong() {
        return (long)createdAtFirebaseTimestamp.get("date");
    }


    @Exclude
    public long getLastMessageRecievedTimestampLong() {
        return (long)lastMessageRecievedFirebaseTimestamp.get("date");
    }

    @Exclude
    public String getLastMessageRecievedTimestampTimeFormattedString(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        c.setTimeInMillis((long) lastMessageRecievedFirebaseTimestamp.get("date"));
        return sdf.format(c.getTime());
    }

    @Exclude
    public String getLastMessageRecievedTimestampDateFormattedString(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        c.setTimeInMillis((long) lastMessageRecievedFirebaseTimestamp.get("date"));
        return sdf.format(c.getTime());
    }


    @Exclude
    public String getRecipientNameForCurrentUser() {
        return recipientNameForCurrentUser;
    }

    @Exclude
    public void setRecipientNameForCurrentUser(String recipientNameForCurrentUser) {
        this.recipientNameForCurrentUser = recipientNameForCurrentUser;
    }

    @Exclude
    public String recipientNameForCurrentUser;

    @Exclude
    public String recipientUserId;

    @Exclude
    public long createdAtFirebaseTimestampLong;

    @Exclude
    public long lastMessageRecievedTimestampLong;


    @Exclude
    public void setRecipientUserId(String recipientUserId) {
        this.recipientUserId = recipientUserId;
    }

    @Exclude
    public String getRecipientUserId(){
        return this.recipientUserId;
    }


}







