package com.wlc.beacon.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *  on 28/3/18.
 */


public class CampaignModel {

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

        @SerializedName("ping_id")
        @Expose
        private Integer pingId;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("open_status")
        @Expose
        private String openStatus;
        @SerializedName("click_status")
        @Expose
        private String clickStatus;
        @SerializedName("rules_id")
        @Expose
        private Integer rulesId;
        @SerializedName("campaign_id")
        @Expose
        private Integer campaignId;
        @SerializedName("campaign_name")
        @Expose
        private String campaignName;
        @SerializedName("start_date")
        @Expose
        private String startDate;
        @SerializedName("end_date")
        @Expose
        private String endDate;
        @SerializedName("start_time")
        @Expose
        private String startTime;
        @SerializedName("end_time")
        @Expose
        private String endTime;
        @SerializedName("schedule_days")
        @Expose
        private String scheduleDays;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("min_age")
        @Expose
        private String minAge;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("max_age")
        @Expose
        private String maxAge;
        @SerializedName("beacon_id")
        @Expose
        private Integer beaconId;
        @SerializedName("store_id")
        @Expose
        private Integer storeId;
        @SerializedName("category_id")
        @Expose
        private String categoryId;
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
        @SerializedName("beacon_identifier")
        @Expose
        private String beaconIdentifier;
        @SerializedName("content_id")
        @Expose
        private Integer contentId;
        @SerializedName("content_type")
        @Expose
        private String contentType;
        @SerializedName("short_notification")
        @Expose
        private String shortNotification;
        @SerializedName("long_notification")
        @Expose
        private String longNotification;
        @SerializedName("barcode")
        @Expose
        private String barcode;
        @SerializedName("content_price")
        @Expose
        private String contentPrice;
        @SerializedName("content_name")
        @Expose
        private String contentName;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("content_media")
        @Expose
        private String contentMedia;
        @SerializedName("url_button_name")
        @Expose
        private String urlButtonName;
        @SerializedName("content_url")
        @Expose
        private String contentUrl;
        @SerializedName("company_name")
        @Expose
        private String companyName;
        @SerializedName("store_name")
        @Expose
        private String storeName;
        @SerializedName("shop_type")
        @Expose
        private String shopType;
        @SerializedName("manager_name")
        @Expose
        private String managerName;
        @SerializedName("manager_email")
        @Expose
        private String managerEmail;
        @SerializedName("manager_phone")
        @Expose
        private String managerPhone;
        @SerializedName("store_country")
        @Expose
        private String storeCountry;
        @SerializedName("store_state")
        @Expose
        private String storeState;
        @SerializedName("store_city")
        @Expose
        private String storeCity;
        @SerializedName("address_1")
        @Expose
        private String address1;
        @SerializedName("store_latitude")
        @Expose
        private String storeLatitude;
        @SerializedName("store_longitude")
        @Expose
        private String storeLongitude;
        @SerializedName("store_starttime")
        @Expose
        private String storeStarttime;
        @SerializedName("store_endtime")
        @Expose
        private String storeEndtime;
        @SerializedName("address_2")
        @Expose
        private String address2;
        @SerializedName("store_image")
        @Expose
        private String storeImage;
        @SerializedName("store_beacon")
        @Expose
        private String storeBeacon;
        @SerializedName("favourite_check")
        @Expose
        private String favouriteCheck;

        public Integer getPingId() {
            return pingId;
        }

        public void setPingId(Integer pingId) {
            this.pingId = pingId;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getOpenStatus() {
            return openStatus;
        }

        public void setOpenStatus(String openStatus) {
            this.openStatus = openStatus;
        }

        public String getClickStatus() {
            return clickStatus;
        }

        public void setClickStatus(String clickStatus) {
            this.clickStatus = clickStatus;
        }

        public Integer getRulesId() {
            return rulesId;
        }

        public void setRulesId(Integer rulesId) {
            this.rulesId = rulesId;
        }

        public Integer getCampaignId() {
            return campaignId;
        }

        public void setCampaignId(Integer campaignId) {
            this.campaignId = campaignId;
        }

        public String getCampaignName() {
            return campaignName;
        }

        public void setCampaignName(String campaignName) {
            this.campaignName = campaignName;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getScheduleDays() {
            return scheduleDays;
        }

        public void setScheduleDays(String scheduleDays) {
            this.scheduleDays = scheduleDays;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getMinAge() {
            return minAge;
        }

        public void setMinAge(String minAge) {
            this.minAge = minAge;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getMaxAge() {
            return maxAge;
        }

        public void setMaxAge(String maxAge) {
            this.maxAge = maxAge;
        }

        public Integer getBeaconId() {
            return beaconId;
        }

        public void setBeaconId(Integer beaconId) {
            this.beaconId = beaconId;
        }

        public Integer getStoreId() {
            return storeId;
        }

        public void setStoreId(Integer storeId) {
            this.storeId = storeId;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
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

        public String getBeaconIdentifier() {
            return beaconIdentifier;
        }

        public void setBeaconIdentifier(String beaconIdentifier) {
            this.beaconIdentifier = beaconIdentifier;
        }

        public Integer getContentId() {
            return contentId;
        }

        public void setContentId(Integer contentId) {
            this.contentId = contentId;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public String getShortNotification() {
            return shortNotification;
        }

        public void setShortNotification(String shortNotification) {
            this.shortNotification = shortNotification;
        }

        public String getLongNotification() {
            return longNotification;
        }

        public void setLongNotification(String longNotification) {
            this.longNotification = longNotification;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public String getContentPrice() {
            return contentPrice;
        }

        public void setContentPrice(String contentPrice) {
            this.contentPrice = contentPrice;
        }

        public String getContentName() {
            return contentName;
        }

        public void setContentName(String contentName) {
            this.contentName = contentName;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getContentMedia() {
            return contentMedia;
        }

        public void setContentMedia(String contentMedia) {
            this.contentMedia = contentMedia;
        }

        public String getUrlButtonName() {
            return urlButtonName;
        }

        public void setUrlButtonName(String urlButtonName) {
            this.urlButtonName = urlButtonName;
        }

        public String getContentUrl() {
            return contentUrl;
        }

        public void setContentUrl(String contentUrl) {
            this.contentUrl = contentUrl;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public String getShopType() {
            return shopType;
        }

        public void setShopType(String shopType) {
            this.shopType = shopType;
        }

        public String getManagerName() {
            return managerName;
        }

        public void setManagerName(String managerName) {
            this.managerName = managerName;
        }

        public String getManagerEmail() {
            return managerEmail;
        }

        public void setManagerEmail(String managerEmail) {
            this.managerEmail = managerEmail;
        }

        public String getManagerPhone() {
            return managerPhone;
        }

        public void setManagerPhone(String managerPhone) {
            this.managerPhone = managerPhone;
        }

        public String getStoreCountry() {
            return storeCountry;
        }

        public void setStoreCountry(String storeCountry) {
            this.storeCountry = storeCountry;
        }

        public String getStoreState() {
            return storeState;
        }

        public void setStoreState(String storeState) {
            this.storeState = storeState;
        }

        public String getStoreCity() {
            return storeCity;
        }

        public void setStoreCity(String storeCity) {
            this.storeCity = storeCity;
        }

        public String getAddress1() {
            return address1;
        }

        public void setAddress1(String address1) {
            this.address1 = address1;
        }

        public String getStoreLatitude() {
            return storeLatitude;
        }

        public void setStoreLatitude(String storeLatitude) {
            this.storeLatitude = storeLatitude;
        }

        public String getStoreLongitude() {
            return storeLongitude;
        }

        public void setStoreLongitude(String storeLongitude) {
            this.storeLongitude = storeLongitude;
        }

        public String getStoreStarttime() {
            return storeStarttime;
        }

        public void setStoreStarttime(String storeStarttime) {
            this.storeStarttime = storeStarttime;
        }

        public String getStoreEndtime() {
            return storeEndtime;
        }

        public void setStoreEndtime(String storeEndtime) {
            this.storeEndtime = storeEndtime;
        }

        public String getAddress2() {
            return address2;
        }

        public void setAddress2(String address2) {
            this.address2 = address2;
        }

        public String getStoreImage() {
            return storeImage;
        }

        public void setStoreImage(String storeImage) {
            this.storeImage = storeImage;
        }

        public String getStoreBeacon() {
            return storeBeacon;
        }

        public void setStoreBeacon(String storeBeacon) {
            this.storeBeacon = storeBeacon;
        }

        public String getFavouriteCheck() {
            return favouriteCheck;
        }

        public void setFavouriteCheck(String favouriteCheck) {
            this.favouriteCheck = favouriteCheck;
        }

    }

}