package com.wlc.beacon.adapter;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wlc.beacon.R;
import com.wlc.beacon.activity.SearchActivity;
import com.wlc.beacon.model.FavouriteModel;
import com.wlc.beacon.model.SearchModel;
import com.wlc.beacon.utils.Utils;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 *  on 30/3/18.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.CustomViewHolder> {

    List<SearchModel.Root> searchRootList;
    Context context;

    public SearchAdapter(Context context, List<SearchModel.Root> searchRootList) {
        this.context = context;
        this.searchRootList = searchRootList;
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_campaign, parent, false);

        CustomViewHolder customViewHolder = new CustomViewHolder(view);

        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, final int position) {


        holder.tvTitleCampaignItem.setText(searchRootList.get(position).getStoreName());

        String totalCampiagn = searchRootList.get(position).getTotalCampaign() + " " + context.getString(R.string.campaigns);
        holder.tvCampaignNumberItem.setText(totalCampiagn);

        String address = searchRootList.get(position).getAddress1() + "," + searchRootList.get(position).getAddress2() + "," +
                searchRootList.get(position).getStoreCity() + "," + searchRootList.get(position).getStoreState() + "," +
                searchRootList.get(position).getStoreCountry();
        holder.tvDescCampaignItem.setText(address);

        Utils.loadImageGlide(context, searchRootList.get(position).getStoreImage(), holder.imgCampaignItem);

        holder.linearMainItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = searchRootList.get(position).getStoreName();
                ((SearchActivity)context).getStoreIdWiseCampaign(String.valueOf(searchRootList.get(position).getStoreId()), title);
            }
        });

        holder.tvDistance.setText("");
        float [] results = new float[5];
        if(!searchRootList.get(position).getStoreLatitude().isEmpty() && !searchRootList.get(position).getStoreLongitude().isEmpty()){
            double storeLat = Double.parseDouble(searchRootList.get(position).getStoreLatitude());
            double storeLong = Double.parseDouble(searchRootList.get(position).getStoreLongitude());

            Location.distanceBetween(Utils.DeviceLatitude, Utils.DeviceLongitude, storeLat, storeLong, results);

            String dist = String.format(Locale.US, "%.02f", (results[0] / 1000)) + "km";
            holder.tvDistance.setText(dist);
        }

    }

    @Override
    public int getItemCount() {
        return searchRootList.size();
    }


    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_campaign_item)
        ImageView imgCampaignItem;
        @BindView(R.id.tv_title_campaign_item)
        TextView tvTitleCampaignItem;
        @BindView(R.id.tv_campaign_number_item)
        TextView tvCampaignNumberItem;
        @BindView(R.id.tv_desc_campaign_item)
        AppCompatTextView tvDescCampaignItem;
        @BindView(R.id.tv_distance)
        TextView tvDistance;
        @BindView(R.id.linear_main_item)
        LinearLayout linearMainItem;

        CustomViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
