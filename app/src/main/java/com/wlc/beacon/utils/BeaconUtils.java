/*
package com.wlc.beacon.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.estimote.coresdk.common.config.EstimoteSDK;
import com.estimote.coresdk.observation.region.RegionUtils;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;
import com.google.gson.Gson;
import com.wlc.beacon.MyApplication;
import com.wlc.beacon.R;
import com.wlc.beacon.activity.MainActivity;
import com.wlc.beacon.activity.SplashScreenActivity;
import com.wlc.beacon.model.UserLoginModel;
import com.wlc.beacon.network.ApiClient;
import com.wlc.beacon.network.ApiInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

*/
/**
 *  on 5/4/18.
 *//*


public class BeaconUtils {

    private BeaconManager beaconManager;
    private BeaconRegion region;

    public BeaconUtils(final Context context) {

        //  To get your AppId and AppToken you need to create new application in Estimote Cloud.
        EstimoteSDK.initialize(context, "beca-90s", "3694418278b2dba38b682236d0671a7d");
        EstimoteSDK.enableDebugLogging(true);

        beaconManager = new BeaconManager(context);
        region = new BeaconRegion("ranged region",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                */
/*beaconManager.startMonitoring(new BeaconRegion(
                        "monitored region",
                        UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
                        18057, 8619));*//*


                beaconManager.startRanging(region);

            }
        });

        beaconManager.setMonitoringListener(new BeaconManager.BeaconMonitoringListener() {
            @Override
            public void onEnteredRegion(BeaconRegion region, List<Beacon> beacons) {
                Log.v("aaa","..onEnteredRegion..");
                if(beacons != null){
                    Log.v("aaa","..onEnteredRegion..IFF.." + beacons.size() + beacons);
                    for(int i = 0; i < beacons.size(); i++){
                        if(Utils.isContainsPreference(context, Utils.UserDetail_Key)) {
                            getCampaignList(context, String.valueOf(beacons.get(i).getMajor()), String.valueOf(beacons.get(i).getMinor()), "entry");
                        }
                    }
                }
            }
            @Override
            public void onExitedRegion(BeaconRegion region) {
                // could add an "exit" notification too if you want (-:
                Log.v("aaa","..onExitRegion..");
            }
        });

        beaconManager.setRangingListener(new BeaconManager.BeaconRangingListener() {
            @Override
            public void onBeaconsDiscovered(BeaconRegion beaconRegion, List<Beacon> beacons) {
                if(beacons != null){
                    for(Beacon bb : beacons){
                        Log.v("aaa","..onBeaconsDiscovered..Major.." + bb.getMajor() + "..RegionUtils.." + RegionUtils.computeAccuracy(bb));
                    }
                }
            }
        });


    }


    public void startMonitoringManual(String uuid, int major, int minor){

        beaconManager.startMonitoring(new BeaconRegion(
                "monitored region",
                UUID.fromString(uuid),
                major, minor));

        Log.v("startMonitoringManual","..." + major + "..." + minor);

    }

    public void disConnectBeacon(){

        if(beaconManager != null){
            beaconManager.disconnect();
        }

    }

    public void getCampaignList(final Context context, String major, String minor, String type){

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        UserLoginModel userLoginModel = new Gson().fromJson(Utils.getStringPreference(context, Utils.UserDetail_Key, ""), UserLoginModel.class);

        apiInterface.campaignList(userLoginModel.getRoot().get(0).getUserId(), major, minor, type, Utils.getStringPreference(context, Utils.AccessToken_Key, ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {

                        try {

                            if(responseBodyResponse.isSuccessful()){

                                String response = responseBodyResponse.body().string();
                                Log.v("response","..campaignList.." + response);
                                JSONObject jsonObject = new JSONObject(response);

                                String status = jsonObject.getString(ApiClient.STATUS);
                                if(status.equalsIgnoreCase(ApiClient.SUCCESS)){
                                    JSONArray jsonArray = jsonObject.getJSONArray(ApiClient.ROOT);
                                    for(int i = 0; i < jsonArray.length(); i++){
                                        JSONObject jObj = jsonArray.getJSONObject(i);
                                        String shortNotification = jObj.getString(ApiClient.SHORT_NOTIFICATION);

                                        showNotification(context, context.getString(R.string.app_name), shortNotification);
                                    }
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

    public void showNotification(Context context, String title, String message) {

        Intent notifyIntent = new Intent(context, SplashScreenActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivities(context, 0,
                new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(new Random().nextInt(), notification);

    }



}
*/
