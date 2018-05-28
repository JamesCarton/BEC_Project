package com.wlc.beacon.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wlc.beacon.R;
import com.wlc.beacon.adapter.FavouriteAdapter;
import com.wlc.beacon.model.CampaignModel;
import com.wlc.beacon.model.FavouriteModel;
import com.wlc.beacon.model.InterestModel;
import com.wlc.beacon.model.UserLoginModel;
import com.wlc.beacon.network.ApiClient;
import com.wlc.beacon.network.ApiInterface;
import com.wlc.beacon.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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



public class FavouriteFragment extends Fragment {

    @BindView(R.id.header_text_left_2)
    TextView headerTextLeft2;
    Unbinder unbinder;
    @BindView(R.id.header_img_right_1)
    ImageView headerImgRight1;
    @BindView(R.id.searchview)
    SearchView searchview;
    @BindView(R.id.linear_search_icon_text)
    LinearLayout linearSearchIconText;

    RecyclerView recyclerview_favourite_campaign;
    FavouriteAdapter favouriteAdapter;
    List<FavouriteModel.Root> favouriteRootList;
    List<FavouriteModel.Root> tmp_favouriteRootList;

    boolean isAscSort;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        unbinder = ButterKnife.bind(this, view);

        headerTextLeft2.setText(R.string.favourite);
        headerImgRight1.setImageResource(R.drawable.sort_asce_ic);

        recyclerview_favourite_campaign = view.findViewById(R.id.recyclerview_favourite_campaign);
        favouriteRootList = new ArrayList<>();
        tmp_favouriteRootList = new ArrayList<>();
        favouriteAdapter = new FavouriteAdapter(getActivity(), favouriteRootList);
        recyclerview_favourite_campaign.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview_favourite_campaign.setAdapter(favouriteAdapter);

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

        Log.d("favourite", "..onCreateView..");


        return view;

    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d("favourite", "..onResume..");

        getFavouriteList();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

        Log.d("favourite", "..onDestroyView..");
    }

    @OnClick({R.id.header_frame_right_1,R.id.searchview})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_frame_right_1:
                sortList();
                break;
            case R.id.searchview:
                searchview.setIconified(false);
                linearSearchIconText.setVisibility(View.GONE);
                break;
        }
    }

    public void callSearch(String charText) {

        charText = charText.toLowerCase();
        favouriteRootList.clear();

        if(charText.length() == 0){
            favouriteRootList.addAll(tmp_favouriteRootList);
            Log.i("charText","...charText..IF.." + charText);
        }else{
            for(FavouriteModel.Root root : tmp_favouriteRootList){
                if(root.getStoreName().toLowerCase().contains(charText)){
                    favouriteRootList.add(root);
                    Log.i("charText","...charText..ELSE.." + charText);
                }
            }
        }
        Log.i("charText","...charText.." + charText + "..size..." + tmp_favouriteRootList.size()) ;
        favouriteAdapter.notifyDataSetChanged();

    }

    public void getFavouriteList() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        Utils.showProgressDialog(progressDialog, getString(R.string.loading));

        UserLoginModel userLoginModel = new Gson().fromJson(Utils.getStringPreference(getActivity(), Utils.UserDetail_Key, ""), UserLoginModel.class);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        apiInterface.favouriteList(userLoginModel.getRoot().get(0).getUserId(), Utils.getStringPreference(getActivity(), Utils.AccessToken_Key, ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<FavouriteModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<FavouriteModel> favouriteModelResponse) {
                        Log.d("favourite", "..service call..");
                        favouriteRootList.clear();
                        tmp_favouriteRootList.clear();

                        if(favouriteModelResponse.isSuccessful()){
                            if(favouriteModelResponse.body().getStatus().equalsIgnoreCase(ApiClient.SUCCESS)){
                                favouriteRootList.addAll(favouriteModelResponse.body().getRoot());
                                tmp_favouriteRootList.addAll(favouriteModelResponse.body().getRoot());
                            }else {
                                if(getUserVisibleHint()){
                                    Utils.showDialog(getActivity(), favouriteModelResponse.body().getMessage());
                                }
                            }
                        }

                        favouriteAdapter.notifyDataSetChanged();

                        if(getUserVisibleHint()){
                            Utils.hideKeyboardFrom(getActivity(), linearSearchIconText);
                            linearSearchIconText.setVisibility(View.VISIBLE);
                            searchview.onActionViewCollapsed();
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

    public void sortList(){

        isAscSort = !isAscSort;

        if(isAscSort){
            Collections.sort(favouriteRootList, new Comparator<FavouriteModel.Root>() {
                @Override
                public int compare(FavouriteModel.Root o1, FavouriteModel.Root o2) {
                    return o1.getStoreName().compareToIgnoreCase(o2.getStoreName());
                }
            });
            headerImgRight1.setImageResource(R.drawable.sort_desc_ic);
        }else{
            Collections.sort(favouriteRootList, new Comparator<FavouriteModel.Root>() {
                @Override
                public int compare(FavouriteModel.Root o1, FavouriteModel.Root o2) {
                    return o2.getStoreName().compareToIgnoreCase(o1.getStoreName());
                }
            });
            headerImgRight1.setImageResource(R.drawable.sort_asce_ic);
        }

        favouriteAdapter.notifyDataSetChanged();

    }

}
