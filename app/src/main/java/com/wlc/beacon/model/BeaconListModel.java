package com.wlc.beacon.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *  on 6/4/18.
 */



public class BeaconListModel {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("Root")
    @Expose
    private List<Root> root = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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



    public class Root {

        @SerializedName("beacon_id")
        @Expose
        private Integer beaconId;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("beacon_name")
        @Expose
        private String beaconName;
        @SerializedName("beacon_major")
        @Expose
        private String beaconMajor;
        @SerializedName("beacon_minor")
        @Expose
        private String beaconMinor;
        @SerializedName("beacon_uuid")
        @Expose
        private String beaconUuid;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("beacon_identifier")
        @Expose
        private String beaconIdentifier;
        @SerializedName("beacon_rules")
        @Expose
        private List<BeaconRule> beaconRules = null;

        public Integer getBeaconId() {
            return beaconId;
        }

        public void setBeaconId(Integer beaconId) {
            this.beaconId = beaconId;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getBeaconName() {
            return beaconName;
        }

        public void setBeaconName(String beaconName) {
            this.beaconName = beaconName;
        }

        public String getBeaconMajor() {
            return beaconMajor;
        }

        public void setBeaconMajor(String beaconMajor) {
            this.beaconMajor = beaconMajor;
        }

        public String getBeaconMinor() {
            return beaconMinor;
        }

        public void setBeaconMinor(String beaconMinor) {
            this.beaconMinor = beaconMinor;
        }

        public String getBeaconUuid() {
            return beaconUuid;
        }

        public void setBeaconUuid(String beaconUuid) {
            this.beaconUuid = beaconUuid;
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

        public String getBeaconIdentifier() {
            return beaconIdentifier;
        }

        public void setBeaconIdentifier(String beaconIdentifier) {
            this.beaconIdentifier = beaconIdentifier;
        }

        public List<BeaconRule> getBeaconRules() {
            return beaconRules;
        }

        public void setBeaconRules(List<BeaconRule> beaconRules) {
            this.beaconRules = beaconRules;
        }

    }

    public class BeaconRule {

        @SerializedName("beacon_key")
        @Expose
        private String beaconKey;
        @SerializedName("beacon_distance")
        @Expose
        private String beaconDistance;
        @SerializedName("beacon_rules_label")
        @Expose
        private String beaconRulesLabel;

        public String getBeaconKey() {
            return beaconKey;
        }

        public void setBeaconKey(String beaconKey) {
            this.beaconKey = beaconKey;
        }

        public String getBeaconDistance() {
            return beaconDistance;
        }

        public void setBeaconDistance(String beaconDistance) {
            this.beaconDistance = beaconDistance;
        }

        public String getBeaconRulesLabel() {
            return beaconRulesLabel;
        }

        public void setBeaconRulesLabel(String beaconRulesLabel) {
            this.beaconRulesLabel = beaconRulesLabel;
        }

    }

}