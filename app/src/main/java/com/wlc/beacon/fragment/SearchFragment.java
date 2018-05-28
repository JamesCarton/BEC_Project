package com.wlc.beacon.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.warkiz.widget.IndicatorSeekBar;
import com.wlc.beacon.R;
import com.wlc.beacon.activity.SearchActivity;
import com.wlc.beacon.adapter.PopularCampaignAdapter;
import com.wlc.beacon.model.PopularListModel;
import com.wlc.beacon.model.SearchModel;
import com.wlc.beacon.model.UserLoginModel;
import com.wlc.beacon.network.ApiClient;
import com.wlc.beacon.network.ApiInterface;
import com.wlc.beacon.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URLEncoder;
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
import okhttp3.ResponseBody;
import retrofit2.Response;



public class SearchFragment extends Fragment {

    @BindView(R.id.header_text_left_2)
    TextView headerTextLeft2;
    Unbinder unbinder;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.tv_nearby_store)
    TextView tvNearbyStore;
    @BindView(R.id.seekbar_search)
    IndicatorSeekBar seekbarSearch;

    int stateDefaultSelected = 0;
    int cityDefaultSelected = 0;

    RecyclerView recyclerPopularSearch;

    List<PopularListModel> popularList;

    PopularCampaignAdapter popularCampaignAdapter;

    List<String> stateList;
    List<String> stateIDList;
    List<String> cityList;

    public static List<SearchModel.Root> searchRootList;

    int distanceKM = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        unbinder = ButterKnife.bind(this, view);

        headerTextLeft2.setText(R.string.search);

        popularList = new ArrayList<>();
        recyclerPopularSearch = view.findViewById(R.id.recycler_popular_search);
        recyclerPopularSearch.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        popularCampaignAdapter = new PopularCampaignAdapter(getActivity(), popularList, SearchFragment.this);
        recyclerPopularSearch.setAdapter(popularCampaignAdapter);

        seekbarSearch.setOnSeekChangeListener(new IndicatorSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(IndicatorSeekBar seekBar, int progress, float progressFloat, boolean fromUserTouch) {
                String str = getString(R.string.nearby_store) + " " + progress + "km " + getString(R.string.radius);
                tvNearbyStore.setText(str);
                distanceKM = progress;
            }

            @Override
            public void onSectionChanged(IndicatorSeekBar seekBar, int thumbPosOnTick, String textBelowTick, boolean fromUserTouch) {

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar, int thumbPosOnTick) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }
        });

        stateList = new ArrayList<>();
        stateIDList = new ArrayList<>();
        cityList = new ArrayList<>();

        searchRootList = new ArrayList<>();

        /*ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.country));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerCountry.setAdapter(dataAdapter);*/

        getPopularCampaign();
        getStateList();


        return view;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_state, R.id.tv_city, R.id.card_search_city_state, R.id.card_distance_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_state:
                if (!stateList.isEmpty()) {
                    openStateDialog();
                }
                break;
            case R.id.tv_city:
                if (!cityList.isEmpty()) {
                    openCityDialog();
                }
                break;
            case R.id.card_search_city_state:
                if(tvState.getText().toString().isEmpty()){
                    Utils.showDialog(getActivity(), getString(R.string.pls_select_state));
                }else if(tvCity.getText().toString().isEmpty()){
                    Utils.showDialog(getActivity(), getString(R.string.pls_select_city));
                }else{
                    filterCityState(tvState.getText().toString(), tvCity.getText().toString());
                }
                break;
            case R.id.card_distance_search:
                filterDistanceWise(distanceKM);
                break;
        }
    }

    public void openStateDialog() {

        new MaterialDialog.Builder(getActivity())
                .title(R.string.select_state)
                .items(stateList)
                .itemsCallbackSingleChoice(stateDefaultSelected, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        stateDefaultSelected = which;
                        tvState.setText(stateList.get(which));
                        tvCity.setText("");

                        cityDefaultSelected = 0;
                        getCityList(stateIDList.get(which));

                        /**
                         * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                         * returning false here won't allow the newly selected radio button to actually be selected.
                         **/
                        return true;
                    }
                })
                //.widgetColor(getResources().getColor(R.color.blue_app))
                .positiveText(R.string.choose)
                .show();

    }

    public void openCityDialog() {

        new MaterialDialog.Builder(getActivity())
                .title(R.string.select_city)
                .items(cityList)
                .itemsCallbackSingleChoice(cityDefaultSelected, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        cityDefaultSelected = which;
                        tvCity.setText(cityList.get(which));
                        /**
                         * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                         * returning false here won't allow the newly selected radio button to actually be selected.
                         **/
                        return true;
                    }
                })
                //.widgetColor(getResources().getColor(R.color.blue_app))
                .positiveText(R.string.choose)
                .show();

    }

    public void getPopularCampaign() {

        UserLoginModel userLoginModel = new Gson().fromJson(Utils.getStringPreference(getActivity(), Utils.UserDetail_Key, ""), UserLoginModel.class);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        apiInterface.popularStore(userLoginModel.getRoot().get(0).getUserId(), ApiClient.PopularLocation, Utils.getStringPreference(getActivity(), Utils.AccessToken_Key, ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {

                        if (responseBodyResponse.isSuccessful()) {

                            popularList.clear();

                            try {
                                Gson gson = new Gson();
                                String str = responseBodyResponse.body().string();

                                JSONObject jsonObject = new JSONObject(str);
                                if (jsonObject.getString(ApiClient.STATUS).equalsIgnoreCase(ApiClient.SUCCESS)) {

                                    JSONArray jArray = jsonObject.getJSONArray(ApiClient.ROOT);

                                    Type listType = new TypeToken<List<PopularListModel>>() {
                                    }.getType();
                                    List<PopularListModel> tmpPopularList = gson.fromJson(jArray.toString(), listType);

                                    popularList.addAll(tmpPopularList);

                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        popularCampaignAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void getStateList() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        Utils.showProgressDialog(progressDialog, getString(R.string.loading));

        UserLoginModel userLoginModel = new Gson().fromJson(Utils.getStringPreference(getActivity(), Utils.UserDetail_Key, ""), UserLoginModel.class);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        apiInterface.stateList(userLoginModel.getRoot().get(0).getUserId(), Utils.getStringPreference(getActivity(), Utils.AccessToken_Key, ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {

                        if (responseBodyResponse.isSuccessful()) {

                            stateList.clear();
                            stateIDList.clear();

                            try {
                                Gson gson = new Gson();
                                String str = responseBodyResponse.body().string();

                                JSONObject jsonObject = new JSONObject(str);
                                if (jsonObject.getString(ApiClient.STATUS).equalsIgnoreCase(ApiClient.SUCCESS)) {
                                    JSONArray jArray = jsonObject.getJSONArray(ApiClient.ROOT);

                                    for (int i = 0; i < jArray.length(); i++) {
                                        JSONObject jObj = jArray.getJSONObject(i);
                                        stateList.add(jObj.getString(ApiClient.STATENAME));
                                        stateIDList.add(jObj.getString(ApiClient.STATEID));
                                    }

                                }


                            } catch (Exception e) {
                                e.printStackTrace();
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

    }

    public void getCityList(String stateID) {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        Utils.showProgressDialog(progressDialog, getString(R.string.loading));

        UserLoginModel userLoginModel = new Gson().fromJson(Utils.getStringPreference(getActivity(), Utils.UserDetail_Key, ""), UserLoginModel.class);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        apiInterface.cityList(userLoginModel.getRoot().get(0).getUserId(), stateID, Utils.getStringPreference(getActivity(), Utils.AccessToken_Key, ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {

                        if (responseBodyResponse.isSuccessful()) {

                            cityList.clear();

                            try {
                                String str = responseBodyResponse.body().string();

                                JSONObject jsonObject = new JSONObject(str);
                                if (jsonObject.getString(ApiClient.STATUS).equalsIgnoreCase(ApiClient.SUCCESS)) {
                                    JSONArray jArray = jsonObject.getJSONArray(ApiClient.ROOT);

                                    for (int i = 0; i < jArray.length(); i++) {
                                        JSONObject jObj = jArray.getJSONObject(i);
                                        cityList.add(jObj.getString(ApiClient.CITYNAME));
                                    }

                                }else{
                                    Utils.showDialog(getActivity(), jsonObject.getString(ApiClient.MESSAGE));
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.dismissProgressDialog(progressDialog);
                    }

                    @Override
                    public void onComplete() {
                        Utils.dismissProgressDialog(progressDialog);
                    }
                });

    }


    public void filterCityState(String state, String city) {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        Utils.showProgressDialog(progressDialog, getString(R.string.loading));

        UserLoginModel userLoginModel = new Gson().fromJson(Utils.getStringPreference(getActivity(), Utils.UserDetail_Key, ""), UserLoginModel.class);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        try {

            apiInterface.filterCityState(userLoginModel.getRoot().get(0).getUserId(), URLEncoder.encode(state, "UTF-8"), URLEncoder.encode(city, "UTF-8"), URLEncoder.encode(Utils.CityState, "UTF-8"), Utils.getStringPreference(getActivity(), Utils.AccessToken_Key, ""))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<SearchModel>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Response<SearchModel> responseBodyResponse) {

                            if (responseBodyResponse.isSuccessful()) {

                                searchRootList.clear();

                                if (responseBodyResponse.body().getStatus().equalsIgnoreCase(ApiClient.SUCCESS)) {
                                    searchRootList.addAll(responseBodyResponse.body().getRoot());

                                    getActivity().startActivity(new Intent(getActivity(), SearchActivity.class));
                                }else {
                                    Utils.showDialog(getActivity(), responseBodyResponse.body().getMessage());
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

    public void filterLocationWise(String stateName) {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        Utils.showProgressDialog(progressDialog, getString(R.string.loading));

        UserLoginModel userLoginModel = new Gson().fromJson(Utils.getStringPreference(getActivity(), Utils.UserDetail_Key, ""), UserLoginModel.class);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        try {

            apiInterface.filterLocationWise(userLoginModel.getRoot().get(0).getUserId(), URLEncoder.encode(stateName, "UTF-8"), URLEncoder.encode(Utils.LocationWiseStore, "UTF-8"), Utils.getStringPreference(getActivity(), Utils.AccessToken_Key, ""))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<SearchModel>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Response<SearchModel> responseBodyResponse) {

                            if (responseBodyResponse.isSuccessful()) {

                                searchRootList.clear();

                                if (responseBodyResponse.body().getStatus().equalsIgnoreCase(ApiClient.SUCCESS)) {
                                    searchRootList.addAll(responseBodyResponse.body().getRoot());

                                    getActivity().startActivity(new Intent(getActivity(), SearchActivity.class));
                                }else {
                                    Utils.showDialog(getActivity(), responseBodyResponse.body().getMessage());
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

    public void filterDistanceWise(int distance) {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        Utils.showProgressDialog(progressDialog, getString(R.string.loading));

        UserLoginModel userLoginModel = new Gson().fromJson(Utils.getStringPreference(getActivity(), Utils.UserDetail_Key, ""), UserLoginModel.class);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        try {

            apiInterface.filterDistanceWise(userLoginModel.getRoot().get(0).getUserId(), URLEncoder.encode(String.valueOf(Utils.DeviceLatitude), "UTF-8"), URLEncoder.encode(String.valueOf(Utils.DeviceLongitude), "UTF-8"), distance, URLEncoder.encode(Utils.DistanceFilter, "UTF-8"), Utils.getStringPreference(getActivity(), Utils.AccessToken_Key, ""))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<SearchModel>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Response<SearchModel> responseBodyResponse) {

                            if (responseBodyResponse.isSuccessful()) {

                                searchRootList.clear();

                                if (responseBodyResponse.body().getStatus().equalsIgnoreCase(ApiClient.SUCCESS)) {
                                    searchRootList.addAll(responseBodyResponse.body().getRoot());

                                    getActivity().startActivity(new Intent(getActivity(), SearchActivity.class));
                                }else {
                                    Utils.showDialog(getActivity(), responseBodyResponse.body().getMessage());
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
