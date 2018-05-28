package com.wlc.beacon.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *  on 12/4/18.
 */


public class FavouriteModel {

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

        @SerializedName("favourite_id")
        @Expose
        private Integer favouriteId;
        @SerializedName("store_id")
        @Expose
        private Integer storeId;
        @SerializedName("favourite_type")
        @Expose
        private String favouriteType;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
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
        @SerializedName("total_campaign")
        @Expose
        private String totalCampaign;

        public Integer getFavouriteId() {
            return favouriteId;
        }

        public void setFavouriteId(Integer favouriteId) {
            this.favouriteId = favouriteId;
        }

        public Integer getStoreId() {
            return storeId;
        }

        public void setStoreId(Integer storeId) {
            this.storeId = storeId;
        }

        public String getFavouriteType() {
            return favouriteType;
        }

        public void setFavouriteType(String favouriteType) {
            this.favouriteType = favouriteType;
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

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
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

        public String getTotalCampaign() {
            return totalCampaign;
        }

        public void setTotalCampaign(String totalCampaign) {
            this.totalCampaign = totalCampaign;
        }

    }

}