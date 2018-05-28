package com.wlc.beacon.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wlc.beacon.R;
import com.wlc.beacon.adapter.SearchAdapter;
import com.wlc.beacon.fragment.SearchFragment;
import com.wlc.beacon.model.CampaignModel;
import com.wlc.beacon.model.SearchModel;
import com.wlc.beacon.model.UserLoginModel;
import com.wlc.beacon.network.ApiClient;
import com.wlc.beacon.network.ApiInterface;
import com.wlc.beacon.utils.Utils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;



public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.header_img_left_1)
    ImageView headerImgLeft1;
    @BindView(R.id.header_text_left_2)
    TextView headerTextLeft2;

    RecyclerView recyclerview_search_campaign;

    SearchAdapter searchAdapter;

    public List<CampaignModel.Root> campaignRootList;
    public String campaignTitle = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_popular_activity);
        ButterKnife.bind(this);

        headerTextLeft2.setText(R.string.search);

        headerImgLeft1.setImageResource(R.drawable.left_arrow_ic);
        headerImgLeft1.setColorFilter(ContextCompat.getColor(this, R.color.blue_app));

        recyclerview_search_campaign = findViewById(R.id.recyclerview_search_campaign);
        recyclerview_search_campaign.setLayoutManager(new LinearLayoutManager(this));
        searchAdapter = new SearchAdapter(this, SearchFragment.searchRootList);
        recyclerview_search_campaign.setAdapter(searchAdapter);

        campaignRootList = new ArrayList<>();

    }

    @OnClick(R.id.header_frame_left_1)
    public void onViewClicked() {
        onBackPressed();
    }


    public void getStoreIdWiseCampaign(String storeId, final String title) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        Utils.showProgressDialog(progressDialog, getString(R.string.loading));

        UserLoginModel userLoginModel = new Gson().fromJson(Utils.getStringPreference(this, Utils.UserDetail_Key, ""), UserLoginModel.class);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        try {

            apiInterface.storeIDWiseCampaign(userLoginModel.getRoot().get(0).getUserId(), storeId, Utils.CampaignViewFilter, Utils.getStringPreference(SearchActivity.this, Utils.AccessToken_Key, ""))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<CampaignModel>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Response<CampaignModel> responseBodyResponse) {

                            if (responseBodyResponse.isSuccessful()) {

                                campaignRootList.clear();

                                if (responseBodyResponse.body().getStatus().equalsIgnoreCase(ApiClient.SUCCESS)) {
                                    campaignRootList.addAll(responseBodyResponse.body().getRoot());
                                    String rootList = new Gson().toJson(campaignRootList);
                                    startActivity(new Intent(SearchActivity.this, CampaignActivity.class)
                                            .putExtra(Utils.CampaignRootList_Key, rootList)
                                            .putExtra(Utils.CampaignTitle_Key, title));
                                }else {
                                    Utils.showDialog(SearchActivity.this, responseBodyResponse.body().getMessage());
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
