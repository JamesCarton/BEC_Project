package com.wlc.beacon.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.estimote.proximity_sdk.proximity.EstimoteCloudCredentials;
import com.estimote.proximity_sdk.proximity.ProximityAttachment;
import com.estimote.proximity_sdk.proximity.ProximityObserver;
import com.estimote.proximity_sdk.proximity.ProximityObserverBuilder;
import com.estimote.proximity_sdk.proximity.ProximityZone;
import com.google.gson.Gson;
import com.wlc.beacon.R;
import com.wlc.beacon.activity.SplashScreenActivity;
import com.wlc.beacon.model.UserLoginModel;
import com.wlc.beacon.network.ApiClient;
import com.wlc.beacon.network.ApiInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 *  on 5/4/18.
 */

public class BeaconProximityUtils {

    ProximityObserver proximityObserver;

    public static ProximityObserver.Handler observationHandler;

    public BeaconProximityUtils(final Context context) {

        EstimoteCloudCredentials cloudCredentials = new EstimoteCloudCredentials( "beaczone-kes", "dd6ee95dab6e039cf6bf194ac69fef87");
        proximityObserver = new ProximityObserverBuilder(context.getApplicationContext(), cloudCredentials)
                .withBalancedPowerMode()
                .withOnErrorAction(new Function1<Throwable, Unit>() {
                    @Override
                    public Unit invoke(Throwable throwable) {
                        return null;
                    }
                })
                .build();

    }

    public ProximityZone addProximityZone(final Context context, final String key, final String value, String distance){

        Log.v("addProximityZone", "key.." + key);

        double custRange = 0;
        try {
            custRange = Double.parseDouble(distance);
        }catch (Exception e){
            e.printStackTrace();
        }

        ProximityZone venueZone =
                proximityObserver.zoneBuilder()
                        .forAttachmentKeyAndValue(key, value)
                        //.inFarRange()
                        .inCustomRange(custRange)
                        .withOnEnterAction(new Function1<ProximityAttachment, Unit>() {
                            @Override public Unit invoke(ProximityAttachment proximityAttachment) {
                                /* Do something here */
                                Log.v("withOnEnterAction", "venueZone.." + key);
                                if(key.equalsIgnoreCase("entry")){
                                    getCampaignList(context, value.split("-")[0], value.split("-")[1], key);
                                }
                                return null;
                            }
                        })
                        .withOnExitAction(new Function1<ProximityAttachment, Unit>() {
                            @Override
                            public Unit invoke(ProximityAttachment proximityAttachment) {
                                /* Do something here */
                                Log.v("withOnExitAction", "venueZone.." + key);
                                if(!key.equalsIgnoreCase("entry")){
                                    getCampaignList(context, value.split("-")[0], value.split("-")[1], key);
                                }
                                return null;
                            }
                        })
                        .withOnChangeAction(new Function1<List<? extends ProximityAttachment>, Unit>() {
                            @Override
                            public Unit invoke(List<? extends ProximityAttachment> proximityAttachments) {
                                /* Do something here */
                                Log.v("withOnChangeAction", "venueZone.." + key);
                                return null;
                            }
                        })
                        .create();//27-04


        return venueZone;

        /*proximityObserver
                .addProximityZones(venueZone)
                .start();*/

    }

    public void startZone(List<ProximityZone> zoneList){
        if(observationHandler != null){
            observationHandler.stop();
        }

        observationHandler = proximityObserver
                .addProximityZones(zoneList)
                .start();

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

        // send broadcast
        Intent i = new Intent(Utils.CustomBroadcastAction);
        context.sendBroadcast(i);

    }



}
