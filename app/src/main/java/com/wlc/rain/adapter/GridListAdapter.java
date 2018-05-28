package com.rainbowloveapp.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.rainbowloveapp.app.MyApplication;
import com.rainbowloveapp.app.R;
import com.rainbowloveapp.app.model.CardImageLocalModel;
import com.rainbowloveapp.app.ui.GridViewActivity;
import com.rainbowloveapp.app.ui.MainActivity;
import com.rainbowloveapp.app.utils.AnalyticsHelper;
import com.rainbowloveapp.app.utils.Utils;
import com.localytics.android.Localytics;

import java.util.List;

/**
 *  on 5/8/16.
 */
public class GridListAdapter extends RecyclerView.Adapter<GridListAdapter.CustomViewHolder> {

    private List<CardImageLocalModel> cardImageList;
    private Context mContext;


    public GridListAdapter(Context mContext, List<CardImageLocalModel> cardImageList) {

        this.mContext       = mContext;
        this.cardImageList  = cardImageList;

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gridlayout, parent, false);

        CustomViewHolder viewHolder = new CustomViewHolder(itemView);

        //Log.i("viewType....","....." + viewType);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {

        final CardImageLocalModel model = cardImageList.get(position);

        if(model.getStatus().equalsIgnoreCase("0")){
            if(Utils.isConnectingToInternet(mContext))
            {
                Utils.loadImagePlaceholderGlide(mContext, model.getImageUrl(), holder.img_cust_grid);
                //Log.i("onBindViewHolder...","...IF...");
            }
        }else{
            //Log.i("onBindViewHolder...","...Else...");
            byte [] encodeByte= Base64.decode(model.getCardImage(),Base64.DEFAULT);
            Utils.loadImagePlaceholderGlide(mContext, encodeByte, holder.img_cust_grid);
        }


        holder.img_cust_grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Utils.isCardSelected = true;

                Localytics.tagEvent(AnalyticsHelper.Event_Card_Selected);
                MyApplication.getFirebaseAnalytics().logEvent((AnalyticsHelper.Event_Card_Selected).replace(" ","_"), null);

                if(model.getStatus().equalsIgnoreCase("0")){
                    if(Utils.isConnectingToInternet(mContext))
                    {
                        Glide.with(mContext.getApplicationContext())
                                .load(model.getImageUrl())
                                .asBitmap()
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                                //.skipMemoryCache(true)
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                        if(MainActivity.From_BG_Change){
                                            GridViewActivity.From_Grid_BG_Change = true;
                                            Utils.ImageMain = resource;
                                            ((GridViewActivity) mContext).onBackPressed();
                                        }else{
                                            Utils.isCardSelected = true;
                                            Utils.ImageMain = resource;
                                            mContext.startActivity(new Intent(mContext, MainActivity.class));
                                        }

                                    }
                                });
                    }
                }else{
                    byte [] encodeByte= Base64.decode(model.getCardImage(),Base64.DEFAULT);
                    Glide.with(mContext.getApplicationContext())
                            .load(encodeByte)
                            .asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            //.diskCacheStrategy(DiskCacheStrategy.NONE)
                            //.skipMemoryCache(true)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                    if(MainActivity.From_BG_Change){
                                        GridViewActivity.From_Grid_BG_Change = true;
                                        Utils.ImageMain = resource;
                                        ((GridViewActivity) mContext).onBackPressed();
                                    }else{
                                        Utils.isCardSelected = true;
                                        Utils.ImageMain = resource;
                                        mContext.startActivity(new Intent(mContext, MainActivity.class));
                                    }
                                }
                            });
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return cardImageList.size();
        //return temp.length;
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView img_cust_grid;


        public CustomViewHolder(View view) {
            super(view);
            this.img_cust_grid     = (ImageView) view.findViewById(R.id.img_cust_grid);
        }
    }

    String[] temp = {
            "http://luvisallaroundme.com/admin/uploads/1490288272Rainbow-18355.jpg",
            "http://luvisallaroundme.com/admin/uploads/1490288272Rainbow-18356.jpg",
            "http://luvisallaroundme.com/admin/uploads/1490288272Rainbow-18357.jpg",
            "http://luvisallaroundme.com/admin/uploads/1490288272Rainbow-18355.jpg",
            "http://luvisallaroundme.com/admin/uploads/1490288272Rainbow-18356.jpg",
            "http://luvisallaroundme.com/admin/uploads/1490288272Rainbow-18357.jpg",
            "http://luvisallaroundme.com/admin/uploads/1490288272Rainbow-18355.jpg",
            "http://luvisallaroundme.com/admin/uploads/1490288272Rainbow-18356.jpg",
            "http://luvisallaroundme.com/admin/uploads/1490288272Rainbow-18357.jpg",
                    };
}
