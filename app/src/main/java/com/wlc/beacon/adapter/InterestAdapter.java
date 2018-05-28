package com.wlc.beacon.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wlc.beacon.R;
import com.wlc.beacon.activity.CampaignDetailActivity;
import com.wlc.beacon.model.CampaignModel;
import com.wlc.beacon.model.InterestModel;
import com.wlc.beacon.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



public class InterestAdapter extends RecyclerView.Adapter<InterestAdapter.CustomViewHolder> {

    List<InterestModel.Root> interestRootList;
    Context context;

    public InterestAdapter(Context context, List<InterestModel.Root> interestRootList) {
        this.context = context;
        this.interestRootList = interestRootList;
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_interest_layout, parent, false);

        CustomViewHolder customViewHolder = new CustomViewHolder(view);

        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, final int position) {


        holder.checkPreference.setText(interestRootList.get(position).getCategoryName());

        holder.checkPreference.setTypeface(ResourcesCompat.getFont(context, R.font.raleway_regular));

        holder.checkPreference.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                interestRootList.get(position).setChecked(isChecked);
            }
        });

        if(interestRootList.get(position).isChecked()){
            holder.checkPreference.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return interestRootList.size();
    }



    public static class CustomViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.check_preference)
        CheckBox checkPreference;

        CustomViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


}
