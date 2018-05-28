package com.wlc.beacon.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wlc.beacon.R;
import com.wlc.beacon.activity.CampaignDetailActivity;
import com.wlc.beacon.fragment.SearchFragment;
import com.wlc.beacon.model.CampaignModel;
import com.wlc.beacon.model.PopularListModel;
import com.wlc.beacon.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *  on 30/3/18.
 */

public class PopularCampaignAdapter extends RecyclerView.Adapter<PopularCampaignAdapter.CustomViewHolder> {

    List<PopularListModel> popularList;
    Context context;
    SearchFragment searchFragment;

    public PopularCampaignAdapter(Context context, List<PopularListModel> popularList, SearchFragment searchFragment) {
        this.context = context;
        this.popularList = popularList;
        this.searchFragment = searchFragment;
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_popular_campaign, parent, false);

        CustomViewHolder customViewHolder = new CustomViewHolder(view);

        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, final int position) {

        final PopularListModel model = popularList.get(position);

        holder.tvNameItem.setText(model.getStoreState());
        holder.tvCampNumberItem.setText(model.getTotalCampaign() + " " + context.getString(R.string.campaigns));

        holder.cardMainItem.setCardBackgroundColor(Color.parseColor(getBGColor(position)));

        holder.cardMainItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFragment.filterLocationWise(model.getStoreState());
            }
        });
    }

    @Override
    public int getItemCount() {
        return popularList.size();
    }


    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name_item)
        TextView tvNameItem;
        @BindView(R.id.tv_camp_number_item)
        TextView tvCampNumberItem;
        @BindView(R.id.card_main_item)
        CardView cardMainItem;

        CustomViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void openPopularDetails(int position) {
        //context.startActivity(new Intent(context, CampaignDetailActivity.class).putExtra(Utils.CampaignPosition_Key, position));
    }


    public String getBGColor(int position){

        String color = "#FFFFFF";

        switch (position % 7) {
            case 0:
                color = "#FFA051";
                break;
            case 1:
                color = "#FCD060";
                break;
            case 2:
                color = "#EB0927";
                break;
            case 3:
                color = "#CE227D";
                break;
            case 4:
                color = "#F22A81";
                break;
            case 5:
                color = "#007EB9";
                break;
            case 6:
                color = "#00AFE0";
                break;
            default:
                color = "#FFFFFF";

        }

        return color;

    }


}
