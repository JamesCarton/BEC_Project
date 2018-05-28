package com.wlc.beacon.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.eftimoff.viewpagertransformers.CubeOutTransformer;
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.Requirement;
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory;
import com.estimote.proximity_sdk.proximity.ProximityZone;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.wlc.beacon.MyApplication;
import com.wlc.beacon.R;
import com.wlc.beacon.adapter.CardSwipeAdapter;
import com.wlc.beacon.adapter.ViewPagerAdapter;
import com.wlc.beacon.fragment.SearchFragment;
import com.wlc.beacon.model.BeaconListModel;
import com.wlc.beacon.model.UserLoginModel;
import com.wlc.beacon.network.ApiClient;
import com.wlc.beacon.network.ApiInterface;
import com.wlc.beacon.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import okhttp3.ResponseBody;
import permissions.dispatcher.NeedsPermission;
import retrofit2.Response;

import static com.wlc.beacon.utils.BeaconProximityUtils.observationHandler;



public class MainActivity extends BaseActivity {

    View tabOne;
    View tabTwo;
    View tabThree;
    View tabForth;


    @BindView(R.id.viewpager_main)
    ViewPager viewpagerMain;
    @BindView(R.id.tab_main)
    TabLayout tabMain;

    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        setupViewPager();

        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("TAG", "Refreshed token: " + token);

        if(token != null){
            registerToken(token);
        }


    }

    @Override
    public void onBackPressed() {
        if(tabMain != null && tabMain.getSelectedTabPosition() != 0){
            viewpagerMain.setCurrentItem(0);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //SystemRequirementsChecker.checkWithDefaultDialogs(this);

        checkBeaconPermission();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                // Logic to handle location object

                                Utils.DeviceLatitude = location.getLatitude();
                                Utils.DeviceLongitude = location.getLongitude();

                                Log.v("location...", "..." + Utils.DeviceLatitude + "..." + Utils.DeviceLongitude);
                            }
                        }
                    });
        }
    }

    public void createTabIcons() {

        tabOne = LayoutInflater.from(this).inflate(R.layout.custom_tab_layout, tabMain, false);
        ((ImageView)tabOne.findViewById(R.id.img_tab_icon)).setImageResource(R.drawable.wifi_ic);
        tabMain.addTab(tabMain.newTab().setCustomView(tabOne));

        tabTwo = LayoutInflater.from(this).inflate(R.layout.custom_tab_layout, tabMain, false);
        ((ImageView)tabTwo.findViewById(R.id.img_tab_icon)).setImageResource(R.drawable.search_ic);
        tabMain.addTab(tabMain.newTab().setCustomView(tabTwo));

        tabThree = LayoutInflater.from(this).inflate(R.layout.custom_tab_layout, tabMain, false);
        ((ImageView)tabThree.findViewById(R.id.img_tab_icon)).setImageResource(R.drawable.star_ic);
        tabMain.addTab(tabMain.newTab().setCustomView(tabThree));

        tabForth = LayoutInflater.from(this).inflate(R.layout.custom_tab_layout, tabMain, false);
        ((ImageView)tabForth.findViewById(R.id.img_tab_icon)).setImageResource(R.drawable.user_ic);
        tabMain.addTab(tabMain.newTab().setCustomView(tabForth));

    }

    public void setupViewPager(){

        viewpagerMain.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));

        viewpagerMain.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabMain));

        //viewpagerMain.setOffscreenPageLimit(new ViewPagerAdapter(getSupportFragmentManager()).getCount()-1);

        //viewpagerMain.setPageTransformer(true, new CubeOutTransformer());

        viewpagerMain.setPageTransformer(true, new CubeOutTransformer());
        //viewpagerMain.setPageTransformer(true, new DepthPageTransformer());
        //viewpagerMain.setPageTransformer(true, new ZoomOutSlideTransformer());

        tabMain.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewpagerMain.setCurrentItem(tab.getPosition());

                ((ImageView)tab.getCustomView().findViewById(R.id.img_tab_icon)).setColorFilter(getResources().getColor(R.color.blue_app));

                Utils.hideKeyboardFrom(MainActivity.this, findViewById(R.id.img_tab_icon));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((ImageView)tab.getCustomView().findViewById(R.id.img_tab_icon)).setColorFilter(getResources().getColor(R.color.gray_ic));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        createTabIcons();

    }

    public void getBeaconList(){

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        UserLoginModel userLoginModel = new Gson().fromJson(Utils.getStringPreference(this, Utils.UserDetail_Key, ""), UserLoginModel.class);

        apiInterface.beaconList(userLoginModel.getRoot().get(0).getUserId(), Utils.getStringPreference(this, Utils.AccessToken_Key, ""))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<Response<BeaconListModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<BeaconListModel> beaconListModelResponse) {

                        try {

                            if(beaconListModelResponse.isSuccessful()){
                                if(beaconListModelResponse.body().getStatus().equalsIgnoreCase(ApiClient.SUCCESS)){
                                    Gson gson = new Gson();
                                    String str = gson.toJson(beaconListModelResponse.body(), BeaconListModel.class);
                                    Log.v("BeaconList","..." + str);
                                    Utils.setStringPreference(MainActivity.this, Utils.BeaconList_Key, str);

                                    List<ProximityZone> zoneList = new ArrayList<>();

                                    String beacon_value = beaconListModelResponse.body().getRoot().get(0).getBeaconMajor() + "-" + beaconListModelResponse.body().getRoot().get(0).getBeaconMinor();

                                    for(BeaconListModel.BeaconRule beaconRule : beaconListModelResponse.body().getRoot().get(0).getBeaconRules()){
                                        if(!beaconRule.getBeaconDistance().equalsIgnoreCase("0")) {
                                            zoneList.add(MyApplication.getBeaconUtils().addProximityZone(MainActivity.this, beaconRule.getBeaconKey(), beacon_value, beaconRule.getBeaconDistance()));
                                        }
                                    }

                                    MyApplication.getBeaconUtils().startZone(zoneList);

                                    /*List<String> keyList = new ArrayList<>();
                                    List<String> valueList = new ArrayList<>();

                                    keyList.add("miter5");
                                    keyList.add("entry");
                                    keyList.add("exit");
                                    keyList.add("miter1");

                                    valueList.add("18057-8619");
                                    valueList.add("18057-8619");
                                    valueList.add("18057-8619");
                                    valueList.add("18057-8619");

                                    List<ProximityZone> zoneList = new ArrayList<>();

                                    for(int i = 0; i < keyList.size(); i++){
                                        zoneList.add(MyApplication.getBeaconUtils().addProximityZone(keyList.get(i), valueList.get(i)));
                                    }*/



                                    // start Monitoring
                                    /*for(BeaconListModel.Root root : beaconListModelResponse.body().getRoot()){
                                        MyApplication.getBeaconUtils().startMonitoringManual(root.getBeaconUuid(), Integer.parseInt(root.getBeaconMajor()), Integer.parseInt(root.getBeaconMinor()));
                                    }*/
                                }
                            }

                        }catch (Exception e){
                            e.printStackTrace();
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
    protected void onDestroy() {
        observationHandler.stop();
        super.onDestroy();
    }

    public void registerToken(String token){

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        try {
            apiInterface.registerNotificationToken(URLEncoder.encode(token, "UTF-8"), "Android")
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(new Observer<Response<ResponseBody>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Response<ResponseBody> responseBodyResponse) {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void checkBeaconPermission(){

        RequirementsWizardFactory.createEstimoteRequirementsWizard().fulfillRequirements(
                this,
                new Function0<Unit>() {
                    @Override
                    public Unit invoke() {
                        //proximityObserver.addProximityZone(venueZone).start();
                        getBeaconList();
                        return null;
                    }
                },

                new Function1<List<? extends Requirement>, Unit>() {
                    @Override
                    public Unit invoke(List<? extends Requirement> requirements) {
          /* scanning won't work, handle this case in your app */
                        return null;
                    }
                },

                new Function1<Throwable, Unit>() {
                    @Override
                    public Unit invoke(Throwable throwable) {
          /* Oops, some error occurred, handle it here! */
                        return null;
                    }

                });

    }

}
