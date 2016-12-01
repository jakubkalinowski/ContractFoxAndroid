package com.example.jakubkalinowski.contractfoxandroid.Model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by Ladimer on 9/26/2016.
 */

@IgnoreExtraProperties

public class Review {


    //this data is autogenerated by firebase
    private String mReviewerHomeownerId;
    private String mContractorId;
    private Map<String, Object> createdAtFirebaseTimestamp;
    //this data is taken from string
    private String mTitle;
    private String mDescription;
    private Double mStars;



    public Review(){}



    public Review(String homeownerId, String contractorId,
                  Map<String, Object> firebaseTimestamp, String title,
                  String description, Double stars) {
        mReviewerHomeownerId = homeownerId;
        mContractorId = contractorId;
        createdAtFirebaseTimestamp = firebaseTimestamp;
        mTitle = title;
        mDescription = description;
        mStars = stars;
    }



    public String getReviewerHomeownerId() {
        return mReviewerHomeownerId;
    }

    public void setReviewerHomeownerId(String reviewerHomeownerId) {
        mReviewerHomeownerId = reviewerHomeownerId;
    }

    public String getContractorId() {
        return mContractorId;
    }

    public void setContractorId(String contractorId) {
        mContractorId = contractorId;
    }

    public Map<String, Object> getCreatedAtFirebaseTimestamp() {
        return createdAtFirebaseTimestamp;
    }

    public void setCreatedAtFirebaseTimestamp(Map<String, Object> createdAtFirebaseTimestamp) {
        this.createdAtFirebaseTimestamp = createdAtFirebaseTimestamp;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public Double getStars() {
        return mStars;
    }

    public void setStars(Double stars) {
        mStars = stars;
    }


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }


    @Exclude
    public long createdAtFirebaseTimestampLong;

    @Exclude
    public long getCreatedAtFirebaseTimestampLong() {
        return (long) createdAtFirebaseTimestamp.get("date");
    }



    @Exclude
    public String getCreatedAtFirebaseTimeStampFormattedString(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        c.setTimeInMillis((long) createdAtFirebaseTimestamp.get("date"));
        return sdf.format(c.getTime());
    }









}
