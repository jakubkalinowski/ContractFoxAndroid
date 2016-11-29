package com.example.jakubkalinowski.contractfoxandroid;

import com.google.firebase.database.IgnoreExtraProperties;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Contractor extends Member {
    private String briefDescription;
    private String companyName;
    private String businessWebsiteURL;
    public Map<String, Boolean> skillSet = new HashMap<>();


    public Contractor() {}

    public Contractor(String firstname, String lastname, String email, String phoneNo,
                      Boolean contractorOption, Address mAddress,
                      String briefDescription, Map<String, Boolean> skillSet,
                      String businessWebsiteURL) {
        super(firstname, lastname, email, phoneNo, contractorOption, mAddress);
        this.briefDescription = briefDescription;
        this.skillSet = skillSet;
        this.businessWebsiteURL = businessWebsiteURL;
    }

    public String getBriefDescription() {
        return briefDescription;
    }

    public void setBriefDescription(String briefDescription) {
        this.briefDescription = briefDescription;
    }

    //implementing abstract methods

    @Override
    public Boolean getContractorOption() {
        return contractorOption;
    }

    @Override
    public void setContractorOption(Boolean contractorOption) {
        this.contractorOption = contractorOption;
    }

    @Override
    public String getEmailAddress() {
        return emailAddress;
    }

    @Override
    public String getPhoneNo() {
        return phoneNo;
    }

    @Override
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    @Override
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public void setAddress(Address address) {
        mAddress = address;
    }

    @Override
    public Address getAddress() {
        return mAddress;
    }


    public  Map<String, Boolean> getSkillSet() {
        return skillSet;
    }

    public void setSkillSet(Map<String, Boolean> skillSet) {
        this.skillSet = skillSet;
    }

    public void addSkill(String skill) {
        skillSet.put(skill, true);
    }

    public String getBusinessWebsiteURL() {
        return businessWebsiteURL;
    }

    public void setBusinessWebsiteURL(String businessWebsiteURL) {
        this.businessWebsiteURL = businessWebsiteURL;
    }


    public String getCompanyName() {
        return this.companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }


}