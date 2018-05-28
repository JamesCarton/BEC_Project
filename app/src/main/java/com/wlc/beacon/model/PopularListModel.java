package com.wlc.beacon.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *  on 11/4/18.
 */

public class PopularListModel {


    @SerializedName("total_campaign")
    @Expose
    private String totalCampaign;
    @SerializedName("store_state")
    @Expose
    private String storeState;

    public String getTotalCampaign() {
        return totalCampaign;
    }

    public void setTotalCampaign(String totalCampaign) {
        this.totalCampaign = totalCampaign;
    }

    public String getStoreState() {
        return storeState;
    }

    public void setStoreState(String storeState) {
        this.storeState = storeState;
    }


}
