package com.wlc.beacon.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.wlc.beacon.MyApplication;
import com.wlc.beacon.utils.Utils;



public class MyService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        //MyApplication.getBeaconUtils().disConnectBeacon();

        super.onTaskRemoved(rootIntent);
    }
}
