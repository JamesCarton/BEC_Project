package com.wlc.beacon.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wlc.beacon.R;
import com.wlc.beacon.activity.CampaignActivity;
import com.wlc.beacon.activity.SearchActivity;
import com.wlc.beacon.model.CampaignModel;
import com.wlc.beacon.model.FavouriteModel;
import com.wlc.beacon.model.UserLoginModel;
import com.wlc.beacon.network.ApiClient;
import com.wlc.beacon.network.ApiInterface;
import com.wlc.beacon.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;



public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.CustomViewHolder> {

    List<FavouriteModel.Root> favouriteRootList;
    Context context;

    public FavouriteAdapter(Context context, List<FavouriteModel.Root> favouriteRootList) {
        this.context = context;
        this.favouriteRootList = favouriteRootList;
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favourite_campaign, parent, false);

        CustomViewHolder customViewHolder = new CustomViewHolder(view);

        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, final int position) {


        holder.tvTitleCampaignItem.setText(favouriteRootList.get(position).getStoreName());

        String totalCampiagn = favouriteRootList.get(position).getTotalCampaign() + " " + context.getString(R.string.campaigns);
        holder.tvCampaignNumberItem.setText(totalCampiagn);

        String address = favouriteRootList.get(position).getAddress1() + ", " + favouriteRootList.get(position).getAddress2() + ", " +
                favouriteRootList.get(position).getStoreCity() + ", " + favouriteRootList.get(position).getStoreState() + ", " +
                favouriteRootList.get(position).getStoreCountry();
        holder.tvDescCampaignItem.setText(address);

        Utils.loadImageGlide(context, favouriteRootList.get(position).getStoreImage(), holder.imgCampaignItem);

        holder.tvDistance.setText("");
        float [] results = new float[5];
        if(!favouriteRootList.get(position).getStoreLatitude().isEmpty() && !favouriteRootList.get(position).getStoreLongitude().isEmpty()){
            double storeLat = Double.parseDouble(favouriteRootList.get(position).getStoreLatitude());
            double storeLong = Double.parseDouble(favouriteRootList.get(position).getStoreLongitude());

            Location.distanceBetween(Utils.DeviceLatitude, Utils.DeviceLongitude, storeLat, storeLong, results);

            String dist = String.format(Locale.US, "%.02f", (results[0] / 1000)) + "km";
            holder.tvDistance.setText(dist);
        }

        holder.linear_favourite_main_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStoreIdWiseCampaign(String.valueOf(favouriteRootList.get(position).getStoreId()), favouriteRootList.get(position).getStoreName());
            }
        });


    }

    @Override
    public int getItemCount() {
        return favouriteRootList.size();
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
        AppCompatTextView tvDistance;
        @BindView(R.id.linear_favourite_main_item)
        LinearLayout linear_favourite_main_item;

        CustomViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void getStoreIdWiseCampaign(String storeId, final String title) {

        final ProgressDialog progressDialog = new ProgressDialog(context);
        Utils.showProgressDialog(progressDialog, context.getString(R.string.loading));

        UserLoginModel userLoginModel = new Gson().fromJson(Utils.getStringPreference(context, Utils.UserDetail_Key, ""), UserLoginModel.class);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        try {

            apiInterface.storeIDWiseCampaign(userLoginModel.getRoot().get(0).getUserId(), storeId, Utils.CampaignViewFavourite, Utils.getStringPreference(context, Utils.AccessToken_Key, ""))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<CampaignModel>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Response<CampaignModel> responseBodyResponse) {

                            if (responseBodyResponse.isSuccessful()) {

                                List<CampaignModel.Root> campaignRootList = new ArrayList<>();

                                if (responseBodyResponse.body().getStatus().equalsIgnoreCase(ApiClient.SUCCESS)) {
                                    campaignRootList.addAll(responseBodyResponse.body().getRoot());
                                    String rootList = new Gson().toJson(campaignRootList);
                                    context.startActivity(new Intent(context, CampaignActivity.class)
                                            .putExtra(Utils.CampaignRootList_Key, rootList)
                                            .putExtra(Utils.CampaignTitle_Key, title));
                                }else {
                                    Utils.showDialog(context, responseBodyResponse.body().getMessage());
                                }

                            }


                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            Utils.dismissProgressDialog(progressDialog);
                        }

                        @Override
                        public void onComplete() {
                            Utils.dismissProgressDialog(progressDialog);
                        }
                    });

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
