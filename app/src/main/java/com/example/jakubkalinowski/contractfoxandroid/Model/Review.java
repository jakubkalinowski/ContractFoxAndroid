package com.example.jakubkalinowski.contractfoxandroid.Model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Map;

/**
 * Created by Ladimer on 9/26/2016.
 */

@IgnoreExtraProperties

public class Review {


    //this data is autogenerated by firebase
    private String mReviewerHomeownerId;
    private String mContractorId;
    private Map<String, String> mFirebaseServerTimeStamp;
    //this data is taken from string
    private String mTitle;
    private String mDescription;
    private Double mStars;

    public Review(String homeownerId, String contractorId,
                  Map<String, String> firebaseServerTimeStamp, String title,
                  String description, Double stars) {
        mReviewerHomeownerId = homeownerId;
        mContractorId = contractorId;
        mFirebaseServerTimeStamp = firebaseServerTimeStamp;
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

    public Map<String, String> getFirebaseServerTimeStamp() {
        return mFirebaseServerTimeStamp;
    }

    public void setFirebaseServerTimeStamp(Map<String, String> firebaseServerTimeStamp) {
        mFirebaseServerTimeStamp = firebaseServerTimeStamp;
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





}
