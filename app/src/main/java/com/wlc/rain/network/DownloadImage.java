package com.rainbowloveapp.app.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Looper;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rainbowloveapp.app.database.DatabaseHandler;
import com.rainbowloveapp.app.model.ArtImageLocalModel;
import com.rainbowloveapp.app.model.ArtLocalModel;
import com.rainbowloveapp.app.model.CardImageLocalModel;
import com.rainbowloveapp.app.utils.Utils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 *  on 19/5/17.
 */

public class DownloadImage {

    public static void cardCategoriesImage(Context context){

        final DatabaseHandler databaseHandler = new DatabaseHandler(context);
        databaseHandler.getWritableDatabase();

        Gson gson = new Gson();
        String jsonOutput = Utils.cur2JsonArray(databaseHandler.getCardImageStatus()).toString(); // get Status = 0
        Type listType = new TypeToken<List<CardImageLocalModel>>(){}.getType();
        List<CardImageLocalModel> lists = gson.fromJson(jsonOutput, listType);

        for (CardImageLocalModel cardImageLocalModel : lists) {

            final String imageID = cardImageLocalModel.getImageId();

            //Log.v("check thread...OUT.","..on..UI.." + (Looper.myLooper() == Looper.getMainLooper()));

            try {
                Bitmap myBitmap = Glide.with(context.getApplicationContext())
                        .load(cardImageLocalModel.getImageUrl())
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        //.diskCacheStrategy(DiskCacheStrategy.NONE)
                        //.skipMemoryCache(true)
                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
                //Log.v("check thread...IN.","..on..UI.." + (Looper.myLooper() == Looper.getMainLooper()));

                String img_str = Utils.BitMapToString(myBitmap);

                databaseHandler.updateCardImageTableOnImageId(imageID, "1", img_str);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }




            /*Glide.with(context.getApplicationContext())
                    .load(cardImageLocalModel.getImageUrl())
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            Log.v("img URL", ".Bitmap.." + resource);
                            //String img_str = Utils.BitMapToString(resource);


                            Log.v("check thread...OUT.","..on..UI.." + (Looper.myLooper() == Looper.getMainLooper()));

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String img_str = Utils.BitMapToString(resource);
                                    databaseHandler.updateCardImageTableOnImageId(imageID, "1", img_str);

                                    Log.v("check thread...IN.","..on..UI.." + (Looper.myLooper() == Looper.getMainLooper()));
                                }
                            }).start();

                            Log.v("img URL", ".imageID.." + imageID);

                            //databaseHandler.updateCardImageTableOnImageId(imageID, "1", img_str);

                        }
                    });*/

        }

    }


    public static void artCategoriesImage(Context context){

        final DatabaseHandler databaseHandler = new DatabaseHandler(context);
        databaseHandler.getWritableDatabase();

        Gson gson = new Gson();
        String jsonOutput = Utils.cur2JsonArray(databaseHandler.getArtCategoriesImageStatus()).toString(); // get Status = 0
        //Log.v("artCategoriesImage", ".status 0.." + jsonOutput);
        Type listType = new TypeToken<List<ArtLocalModel>>(){}.getType();
        List<ArtLocalModel> lists = gson.fromJson(jsonOutput, listType);

        for (ArtLocalModel artLocalModel : lists) {

            final String catID = artLocalModel.getCatId();

            try {
                Bitmap myBitmap = Glide.with(context.getApplicationContext())
                        .load(artLocalModel.getImageUrl())
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        //.diskCacheStrategy(DiskCacheStrategy.NONE)
                        //.skipMemoryCache(true)
                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
                //Log.v("check thread...IN.","..on..UI.." + (Looper.myLooper() == Looper.getMainLooper()));

                String img_str = Utils.BitMapToStringPNG(myBitmap);

                databaseHandler.updateArtTableOnCatID(catID, "1", img_str);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            /*Glide.with(context.getApplicationContext())
                    .load(artLocalModel.getImageUrl())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            Log.v("img URL", ".Bitmap.." + resource);
                            String img_str = Utils.BitMapToStringPNG(resource);
                            //Log.v("img URL",".String.." + img_str);

                            Log.v("img URL", ".catID.." + catID);

                            databaseHandler.updateArtTableOnCatID(catID, "1", img_str);

                        }
                    });*/

        }

    }

    public static void artImage(Context context){

        final DatabaseHandler databaseHandler = new DatabaseHandler(context);
        databaseHandler.getWritableDatabase();

        Gson gson = new Gson();
        String jsonOutput = Utils.cur2JsonArray(databaseHandler.getArtImageStatus()).toString(); // get Status = 0
        //Log.v("artImage", ".status 0.." + jsonOutput);
        Type listType = new TypeToken<List<ArtImageLocalModel>>(){}.getType();
        List<ArtImageLocalModel> lists = gson.fromJson(jsonOutput, listType);

        for (ArtImageLocalModel artImageLocalModel : lists) {

            final String imageID = artImageLocalModel.getImageId();

            try {
                Bitmap myBitmap = Glide.with(context.getApplicationContext())
                        .load(artImageLocalModel.getImageUrl())
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        //.diskCacheStrategy(DiskCacheStrategy.NONE)
                        //.skipMemoryCache(true)
                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
                //Log.v("check thread...IN.","..on..UI.." + (Looper.myLooper() == Looper.getMainLooper()));

                String img_str = Utils.BitMapToStringPNG(myBitmap);

                databaseHandler.updateArtImageTableOnImageID(imageID, "1", img_str);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            /*Glide.with(context.getApplicationContext())
                    .load(artImageLocalModel.getImageUrl())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            Log.v("img URL", ".Bitmap.." + resource);
                            String img_str = Utils.BitMapToStringPNG(resource);

                            Log.v("img URL", ".imageID.." + imageID);

                            databaseHandler.updateArtImageTableOnImageID(imageID, "1", img_str);

                        }
                    });*/

        }

    }


}
