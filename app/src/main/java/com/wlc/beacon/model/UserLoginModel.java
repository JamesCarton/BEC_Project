package com.wlc.beacon.model;

/**
 *  on 23/3/18.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class UserLoginModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("Root")
    @Expose
    private List<Root> root = null;
    @SerializedName("message")
    @Expose
    private String message;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Root> getRoot() {
        return root;
    }

    public void setRoot(List<Root> root) {
        this.root = root;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public class Root {

        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("user_role")
        @Expose
        private String userRole;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("user_avatar")
        @Expose
        private String userAvatar;
        @SerializedName("fb_id")
        @Expose
        private String fbId;
        @SerializedName("gmail_id")
        @Expose
        private String gmailId;
        @SerializedName("user_status")
        @Expose
        private String userStatus;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("birthdate")
        @Expose
        private String birthdate;
        @SerializedName("gender_preference")
        @Expose
        private String genderPreference;
        @SerializedName("interest_preference")
        @Expose
        private String interestPreference;
        @SerializedName("age_preference")
        @Expose
        private String agePreference;
        @SerializedName("beacon_restrication")
        @Expose
        private String beaconRestrication;
        @SerializedName("x-access-token")
        @Expose
        private String xAccessToken;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUserRole() {
            return userRole;
        }

        public void setUserRole(String userRole) {
            this.userRole = userRole;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getUserAvatar() {
            return userAvatar;
        }

        public void setUserAvatar(String userAvatar) {
            this.userAvatar = userAvatar;
        }

        public String getFbId() {
            return fbId;
        }

        public void setFbId(String fbId) {
            this.fbId = fbId;
        }

        public String getGmailId() {
            return gmailId;
        }

        public void setGmailId(String gmailId) {
            this.gmailId = gmailId;
        }

        public String getUserStatus() {
            return userStatus;
        }

        public void setUserStatus(String userStatus) {
            this.userStatus = userStatus;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getBirthdate() {
            return birthdate;
        }

        public void setBirthdate(String birthdate) {
            this.birthdate = birthdate;
        }

        public String getGenderPreference() {
            return genderPreference;
        }

        public void setGenderPreference(String genderPreference) {
            this.genderPreference = genderPreference;
        }

        public String getInterestPreference() {
            return interestPreference;
        }

        public void setInterestPreference(String interestPreference) {
            this.interestPreference = interestPreference;
        }

        public String getAgePreference() {
            return agePreference;
        }

        public void setAgePreference(String agePreference) {
            this.agePreference = agePreference;
        }

        public String getBeaconRestrication() {
            return beaconRestrication;
        }

        public void setBeaconRestrication(String beaconRestrication) {
            this.beaconRestrication = beaconRestrication;
        }

        public String getXAccessToken() {
            return xAccessToken;
        }

        public void setXAccessToken(String xAccessToken) {
            this.xAccessToken = xAccessToken;
        }

    }


}
