package com.wlc.beacon.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wlc.beacon.R;
import com.wlc.beacon.activity.CampaignDetailActivity;
import com.wlc.beacon.model.CampaignModel;
import com.wlc.beacon.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *  on 30/3/18.
 */

public class MyTagCampaignAdapter extends RecyclerView.Adapter<MyTagCampaignAdapter.CustomViewHolder> {

    List<CampaignModel.Root> campaignModelRoot;
    Context context;

    public MyTagCampaignAdapter(Context context, List<CampaignModel.Root> campaignModelRoot) {
        this.context = context;
        this.campaignModelRoot = campaignModelRoot;
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mytagcampaign, parent, false);

        CustomViewHolder customViewHolder = new CustomViewHolder(view);

        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, final int position) {

        String imgURL = campaignModelRoot.get(position).getContentMedia();
        if(!imgURL.isEmpty()){
            imgURL = imgURL.split(",")[0];
            if(imgURL.contains("youtube")){
                imgURL = Utils.getYoutubeThumbnailUrlFromVideoUrl(imgURL);
            }
            Utils.loadImageGlide(context, imgURL, holder.imgCampaignItem);
        }else {
            String storeImg = campaignModelRoot.get(position).getStoreImage();
            if(!storeImg.isEmpty()){
                Utils.loadImageGlide(context, storeImg, holder.imgCampaignItem);
            }
        }

        holder.tvTitleCampaignItem.setText(campaignModelRoot.get(position).getCampaignName());
        holder.tvDescCampaignItem.setText(campaignModelRoot.get(position).getLongNotification());

        holder.linear_main_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String rootStr = gson.toJson(campaignModelRoot.get(position), CampaignModel.Root.class);

                openCampaignDetails(rootStr);
            }
        });
    }

    @Override
    public int getItemCount() {
        return campaignModelRoot.size();
    }



    public static class CustomViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.img_campaign_item)
        ImageView imgCampaignItem;
        @BindView(R.id.tv_title_campaign_item)
        TextView tvTitleCampaignItem;
        @BindView(R.id.tv_desc_campaign_item)
        TextView tvDescCampaignItem;
        @BindView(R.id.linear_main_item)
        LinearLayout linear_main_item;

        CustomViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void openCampaignDetails(String root){
        context.startActivity(new Intent(context, CampaignDetailActivity.class).putExtra(Utils.CampaignRoot_Key, root));
    }
}
