package com.wlc.beacon.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.wlc.beacon.R;
import com.wlc.beacon.adapter.CardSwipeAdapter;
import com.wlc.beacon.fragment.CampaignFragment;
import com.wlc.beacon.model.CampaignModel;
import com.wlc.beacon.model.UserLoginModel;
import com.wlc.beacon.network.ApiClient;
import com.wlc.beacon.network.ApiInterface;
import com.wlc.beacon.utils.Utils;

import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;



public class CampaignDetailActivity extends AppCompatActivity {

    @BindView(R.id.tv_title_camp_detail)
    TextView tvTitleCampDetail;
    @BindView(R.id.img_store_camp_detail)
    ImageView imgStoreCampDetail;
    @BindView(R.id.tv_address_camp_detail)
    TextView tvAddressCampDetail;
    @BindView(R.id.tv_title1_camp_detail)
    TextView tvTitle1CampDetail;
    @BindView(R.id.tv_desc_camp_detail)
    TextView tvDescCampDetail;
    @BindView(R.id.tv_price_camp_detail)
    TextView tvPriceCampDetail;
    @BindView(R.id.img_barcocde_camp_detail)
    ImageView imgBarcocdeCampDetail;
    @BindView(R.id.tv_barcode_camp_detail)
    TextView tvBarcodeCampDetail;
    @BindView(R.id.cardstack_camp_details)
    HorizontalInfiniteCycleViewPager cardStackCampDetails;
    @BindView(R.id.img_star_camp_detail)
    ImageView imgStarCampDetail;

    CampaignModel.Root root;

    boolean isFavourite;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campaign_details_layout);
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey(Utils.CampaignRoot_Key)) {
                String str = getIntent().getExtras().getString(Utils.CampaignRoot_Key);

                root = new Gson().fromJson(str, CampaignModel.Root.class);

                initView();
            }
        }


    }

    @OnClick({R.id.img_back_camp_detail, R.id.img_delete_camp_detail, R.id.img_map_camp_detail, R.id.img_star_camp_detail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back_camp_detail:
                onBackPressed();
                break;
            case R.id.img_delete_camp_detail:
                if (root != null) {
                    deletePing();
                }
                break;
            case R.id.img_map_camp_detail:
                if (root != null) {
                    // Display a label at the location of Google's Sydney office
                    //Uri gmmIntentUri = Uri.parse("geo:0,0?q=-33.8666,151.1957(Google+Sydney)");

                    Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + root.getStoreLatitude() + "," + root.getStoreLongitude() + "(" + Uri.encode(root.getStoreName()) + ")");
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }
                }
                break;
            case R.id.img_star_camp_detail:
                if (root != null){
                    addToFavourite();
                }
                break;
        }
    }

    public void initView() {

        if (root != null) {

            tvTitleCampDetail.setText(root.getCampaignName());
            String address = root.getAddress1() + ", " + root.getAddress2() + ", " + root.getStoreCity() + ", " +
                    root.getStoreState() + ", " + root.getStoreCountry();
            tvAddressCampDetail.setText(address);
            Utils.loadImageGlide(this, root.getStoreImage(), imgStoreCampDetail);
            tvTitle1CampDetail.setText(root.getCampaignName());
            tvDescCampDetail.setText(root.getShortNotification());
            tvPriceCampDetail.setText("Price : " + root.getPrice());
            tvBarcodeCampDetail.setText(root.getBarcode());

            if(root.getFavouriteCheck().equalsIgnoreCase("yes")){
                imgStarCampDetail.setImageResource(R.drawable.star_fill_ic);
                isFavourite = true;
            }else {
                imgStarCampDetail.setImageResource(R.drawable.star_blue_ic);
                isFavourite = false;
            }

            Log.v("address", "..." + root.getAddress1() + "..." + root.getAddress2());

            try {
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.encodeBitmap(root.getBarcode(), BarcodeFormat.CODE_128, 1000, 250);
                //ImageView imageViewQrCode = (ImageView) findViewById(R.id.qrCode);
                imgBarcocdeCampDetail.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (root.getContentType().equalsIgnoreCase("text")) {
                cardStackCampDetails.setVisibility(View.GONE);
            } else {
                cardStackCampDetails.setVisibility(View.VISIBLE);
                CardSwipeAdapter cardSwipeAdapter = new CardSwipeAdapter(this, root.getContentType(), Arrays.asList(root.getContentMedia().split("\\s*,\\s*")));
                cardStackCampDetails.setAdapter(cardSwipeAdapter);
            }


            readPing();

        }


    }

    public void readPing() {

//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        Utils.showProgressDialog(progressDialog, getString(R.string.loading));

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        UserLoginModel userLoginModel = new Gson().fromJson(Utils.getStringPreference(this, Utils.UserDetail_Key, ""), UserLoginModel.class);

        apiInterface.readPing(userLoginModel.getRoot().get(0).getUserId(), String.valueOf(root.getPingId()), Utils.getStringPreference(this, Utils.AccessToken_Key, ""))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {
                        try {
                            if (responseBodyResponse.isSuccessful()) {
                                String response = responseBodyResponse.body().string();
                                Log.v("response", "..readPing.." + response);
                                JSONObject jsonObject = new JSONObject(response);

                                String status = jsonObject.getString(ApiClient.STATUS);
                                if (status.equalsIgnoreCase(ApiClient.SUCCESS)) {
                                    // onBackPressed();
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        //Utils.dismissProgressDialog(progressDialog);
                    }
                });


    }


    public void deletePing() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        Utils.showProgressDialog(progressDialog, getString(R.string.loading));

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        UserLoginModel userLoginModel = new Gson().fromJson(Utils.getStringPreference(this, Utils.UserDetail_Key, ""), UserLoginModel.class);

        apiInterface.deletePing(userLoginModel.getRoot().get(0).getUserId(), String.valueOf(root.getPingId()), Utils.getStringPreference(this, Utils.AccessToken_Key, ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {
                        try {
                            if (responseBodyResponse.isSuccessful()) {
                                String response = responseBodyResponse.body().string();
                                Log.v("response", "..deletePing.." + response);
                                JSONObject jsonObject = new JSONObject(response);

                                String status = jsonObject.getString(ApiClient.STATUS);
                                if (status.equalsIgnoreCase(ApiClient.SUCCESS)) {
                                    onBackPressed();
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
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

    public void addToFavourite() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        Utils.showProgressDialog(progressDialog, getString(R.string.loading));

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        UserLoginModel userLoginModel = new Gson().fromJson(Utils.getStringPreference(this, Utils.UserDetail_Key, ""), UserLoginModel.class);

        apiInterface.addFavourite(userLoginModel.getRoot().get(0).getUserId(), String.valueOf(root.getStoreId()), Utils.getStringPreference(this, Utils.AccessToken_Key, ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {
                        try {
                            if (responseBodyResponse.isSuccessful()) {
                                String response = responseBodyResponse.body().string();
                                Log.v("response", "..readPing.." + response);
                                JSONObject jsonObject = new JSONObject(response);

                                String status = jsonObject.getString(ApiClient.STATUS);
                                if (status.equalsIgnoreCase(ApiClient.SUCCESS)) {

                                    isFavourite = !isFavourite;

                                    if(isFavourite){
                                        imgStarCampDetail.setImageResource(R.drawable.star_fill_ic);
                                    }else {
                                        imgStarCampDetail.setImageResource(R.drawable.star_blue_ic);
                                    }

                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
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


}
