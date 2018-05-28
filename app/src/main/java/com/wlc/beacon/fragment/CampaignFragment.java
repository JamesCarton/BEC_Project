package com.wlc.beacon.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.wlc.beacon.MyApplication;
import com.wlc.beacon.R;
import com.wlc.beacon.activity.CampaignDetailActivity;
import com.wlc.beacon.activity.MainActivity;
import com.wlc.beacon.adapter.MyTagCampaignAdapter;
import com.wlc.beacon.adapter.MyTagCampaignGridAdapter;
import com.wlc.beacon.model.CampaignModel;
import com.wlc.beacon.model.UserLoginModel;
import com.wlc.beacon.network.ApiClient;
import com.wlc.beacon.network.ApiInterface;
import com.wlc.beacon.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 *  on 16/3/18.
 */

public class CampaignFragment extends Fragment {

    Unbinder unbinder;

    boolean isGridView;


    @BindView(R.id.header_text_left_2)
    TextView headerTextLeft2;
    @BindView(R.id.header_img_right_1)
    ImageView headerImgRight1;
    @BindView(R.id.searchview)
    SearchView searchview;
    @BindView(R.id.linear_search_icon_text)
    LinearLayout linearSearchIconText;

    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerViewContainer;
    @BindView(R.id.shimmer_view_container_2)
    ShimmerFrameLayout shimmerViewContainer_2;
    @BindView(R.id.linear_shimmer_layout)
    LinearLayout linearShimmerLayout;

    //@BindView(R.id.recyclerview_mytag_campaign)
    RecyclerView recyclerviewMytagCampaign;
    //@BindView(R.id.recyclerview_mytag_campaign_grid)
    RecyclerView recyclerviewMytagCampaignGrid;

    MyTagCampaignAdapter myTagCampaignAdapter;
    MyTagCampaignGridAdapter myTagCampaignGridAdapter;

    boolean isViewShown;

    List<CampaignModel.Root> campaignModelRoot;
    List<CampaignModel.Root> tmp_campaignModelRoot;

    MyReceiver myReceiver;
    IntentFilter intentFilter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_campaign, container, false);
        unbinder = ButterKnife.bind(this, view);

        headerTextLeft2.setText(R.string.campaigns);
        headerImgRight1.setImageResource(R.drawable.grid_ic);

        shimmerViewContainer.startShimmerAnimation();
        shimmerViewContainer_2.startShimmerAnimation();

        recyclerviewMytagCampaign = view.findViewById(R.id.recyclerview_mytag_campaign);
        recyclerviewMytagCampaign.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerviewMytagCampaignGrid = view.findViewById(R.id.recyclerview_mytag_campaign_grid);
        recyclerviewMytagCampaignGrid.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerviewMytagCampaignGrid.setVisibility(View.GONE);

        campaignModelRoot = new ArrayList<>();
        tmp_campaignModelRoot = new ArrayList<>();

        myTagCampaignAdapter = new MyTagCampaignAdapter(getActivity(), campaignModelRoot);
        recyclerviewMytagCampaign.setAdapter(myTagCampaignAdapter);

        myTagCampaignGridAdapter = new MyTagCampaignGridAdapter(getActivity(), campaignModelRoot);
        recyclerviewMytagCampaignGrid.setAdapter(myTagCampaignGridAdapter);

        searchview.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                linearSearchIconText.setVisibility(View.VISIBLE);
                searchview.onActionViewCollapsed();
                return false;
            }
        });


        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                callSearch(newText);
                return true;
            }
        });

        myReceiver = new MyReceiver();
        intentFilter = new IntentFilter(Utils.CustomBroadcastAction);


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!isViewShown) {       // when tab clicked
            getMyTagCampaign();
        }

        getActivity().registerReceiver(myReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();

        getActivity().unregisterReceiver(myReceiver);
    }

    @OnClick({R.id.header_frame_right_1, R.id.searchview})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_frame_right_1:

                if (isGridView) {
                    headerImgRight1.setImageResource(R.drawable.grid_ic);
                    isGridView = false;

                    recyclerviewMytagCampaign.setVisibility(View.VISIBLE);
                    recyclerviewMytagCampaignGrid.setVisibility(View.GONE);

                } else {
                    headerImgRight1.setImageResource(R.drawable.list_ic);
                    isGridView = true;

                    recyclerviewMytagCampaign.setVisibility(View.GONE);
                    recyclerviewMytagCampaignGrid.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.searchview:
                searchview.setIconified(false);
                linearSearchIconText.setVisibility(View.GONE);
                break;
        }
    }

    public void callSearch(String charText) {

        charText = charText.toLowerCase();
        campaignModelRoot.clear();

        if(charText.length() == 0){
            campaignModelRoot.addAll(tmp_campaignModelRoot);
            Log.i("charText","...charText..IF.." + charText);
        }else{
            for(CampaignModel.Root root : tmp_campaignModelRoot){
                if(root.getCampaignName().toLowerCase().contains(charText)){
                    campaignModelRoot.add(root);
                    Log.i("charText","...charText..ELSE.." + charText);
                }
            }
        }
        Log.i("charText","...charText.." + charText + "..size..." + tmp_campaignModelRoot.size()) ;
        myTagCampaignAdapter.notifyDataSetChanged();
        myTagCampaignGridAdapter.notifyDataSetChanged();

    }

    public void getMyTagCampaign() {

        UserLoginModel userLoginModel = new Gson().fromJson(Utils.getStringPreference(getActivity(), Utils.UserDetail_Key, ""), UserLoginModel.class);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        apiInterface.myTagCampaign(userLoginModel.getRoot().get(0).getUserId(), Utils.getStringPreference(getActivity(), Utils.AccessToken_Key, ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<CampaignModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<CampaignModel> campaignModelResponse) {

                        Log.v("response..", "..code.." + campaignModelResponse.code() + campaignModelResponse.isSuccessful());

                        if (campaignModelResponse.isSuccessful()) {
                            Log.v("response..", ".." + campaignModelResponse.body().getMessage());

                            campaignModelRoot.clear();
                            tmp_campaignModelRoot.clear();

                            if (campaignModelResponse.body().getStatus().equalsIgnoreCase(ApiClient.SUCCESS)) {

                                campaignModelRoot.addAll(campaignModelResponse.body().getRoot());

                                tmp_campaignModelRoot.addAll(campaignModelResponse.body().getRoot());

                                /*if (getUserVisibleHint()) {
                                    linearShimmerLayout.setVisibility(View.GONE);
                                    shimmerViewContainer.stopShimmerAnimation();
                                    shimmerViewContainer_2.stopShimmerAnimation();
                                }*/

                            }else{
                                if (getUserVisibleHint()) {
                                    Utils.showDialog(getActivity(), campaignModelResponse.body().getMessage());
                                }
                            }

                            myTagCampaignAdapter.notifyDataSetChanged();
                            myTagCampaignGridAdapter.notifyDataSetChanged();

                            /*campaignModel = new Gson().fromJson("{\n" +
                                    "    \"status\": \"success\",\n" +
                                    "    \"Root\": [\n" +
                                    "        {\n" +
                                    "            \"ping_id\": 166,\n" +
                                    "            \"created_at\": \"2018-04-03T16:12:02.000Z\",\n" +
                                    "            \"open_status\": \"read\",\n" +
                                    "            \"click_status\": \"unclick\",\n" +
                                    "            \"rules_id\": 20,\n" +
                                    "            \"campaign_id\": 20,\n" +
                                    "            \"campaign_name\": \"Test Campaign 04-04\",\n" +
                                    "            \"start_date\": \"04/04/2018\",\n" +
                                    "            \"end_date\": \"04/19/2018\",\n" +
                                    "            \"start_time\": \"06:00\",\n" +
                                    "            \"end_time\": \"23:00\",\n" +
                                    "            \"schedule_days\": \"all_day\",\n" +
                                    "            \"gender\": \"any\",\n" +
                                    "            \"min_age\": \"\",\n" +
                                    "            \"user_id\": 2,\n" +
                                    "            \"updated_at\": \"2018-04-03T16:12:02.000Z\",\n" +
                                    "            \"max_age\": \"\",\n" +
                                    "            \"beacon_id\": 32,\n" +
                                    "            \"store_id\": 10,\n" +
                                    "            \"category_id\": 6,\n" +
                                    "            \"beacon_name\": \" lemon\",\n" +
                                    "            \"beacon_major\": \"31064\",\n" +
                                    "            \"beacon_minor\": \"33644\",\n" +
                                    "            \"beacon_uuid\": \"B9407F30-F5F8-466E-AFF9-25556B57FE6D\",\n" +
                                    "            \"beacon_identifier\": \"4f53483e6a6e22d6e447049712aff214\",\n" +
                                    "            \"content_id\": 26,\n" +
                                    "            \"content_type\": \"image\",\n" +
                                    "            \"short_notification\": \"Testing Entry Notification fpr Test\",\n" +
                                    "            \"long_notification\": \"Testing Descripation for My new campaign \",\n" +
                                    "            \"barcode\": \"789456456454\",\n" +
                                    "            \"content_price\": \"\",\n" +
                                    "            \"content_name\": \"Testing Content New 04\",\n" +
                                    "            \"price\": \"500\",\n" +
                                    "            \"content_media\": \"https://storage.googleapis.com/staging.beacon-195311.appspot.com/uploads/content/IMG-1522828636570.jpg,https://storage.googleapis.com/staging.beacon-195311.appspot.com/uploads/content/IMG-1522828637157.jpg,https://storage.googleapis.com/staging.beacon-195311.appspot.com/uploads/content/IMG-1522828637228.jpg,https://storage.googleapis.com/staging.beacon-195311.appspot.com/uploads/content/IMG-1522828637283.jpg\",\n" +
                                    "            \"url_button_name\": \"Google\",\n" +
                                    "            \"content_url\": \"https://www.google.co.in/\",\n" +
                                    "            \"company_name\": \"Croma\",\n" +
                                    "            \"store_name\": \"Croma\",\n" +
                                    "            \"shop_type\": \"\",\n" +
                                    "            \"manager_name\": \"Ketan\",\n" +
                                    "            \"manager_email\": \"Ketan@gmail.com\",\n" +
                                    "            \"manager_phone\": \"7894561230\",\n" +
                                    "            \"store_country\": \"Chile\",\n" +
                                    "            \"store_state\": \"Coquimbo\",\n" +
                                    "            \"store_city\": \"La\",\n" +
                                    "            \"address_1\": \"\",\n" +
                                    "            \"store_latitude\": \"23.026976\",\n" +
                                    "            \"store_longitude\": \"72.507762\",\n" +
                                    "            \"store_starttime\": \"4:45 PM\",\n" +
                                    "            \"store_endtime\": \"4:45 PM\",\n" +
                                    "            \"address_2\": \"\",\n" +
                                    "            \"store_image\": \"https://storage.googleapis.com/staging.beacon-195311.appspot.com/uploads/store/1521890322285.jpg\",\n" +
                                    "            \"store_beacon\": \"32,30,35\"\n" +
                                    "        }\n" +
                                    "    ],\n" +
                                    "    \"message\": \"Campaign list successfully.\"\n" +
                                    "}", CampaignModel.class);*/

                            /*myTagCampaignAdapter = new MyTagCampaignAdapter(getActivity(), campaignModel);
                            recyclerviewMytagCampaign.setAdapter(myTagCampaignAdapter);*/

                            if (getUserVisibleHint()) {
                                linearShimmerLayout.setVisibility(View.GONE);
                                shimmerViewContainer.stopShimmerAnimation();
                                shimmerViewContainer_2.stopShimmerAnimation();

                                Utils.hideKeyboardFrom(getActivity(), linearShimmerLayout);
                                linearSearchIconText.setVisibility(View.VISIBLE);
                                searchview.onActionViewCollapsed();
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("call_achieved", "..uservisible." + isVisibleToUser);

        if (getView() != null) {    /// when swipe view
            isViewShown = true;

            getMyTagCampaign();
        } else {
            isViewShown = false;
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        isViewShown = false;
    }

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            onResume();
        }
    }

}





