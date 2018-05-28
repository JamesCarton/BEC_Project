package com.rainbowloveapp.app.utils;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.GlideModule;

/**
 *  on 12/5/17.
 */


public class CustomGlideModule implements GlideModule {
    @Override public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        //Log.v("CustomGlideModule..","..CustomGlideModule..");
    }

    @Override public void registerComponents(Context context, Glide glide) {
        // nothing to do here
    }
}
