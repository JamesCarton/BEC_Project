package com.rainbowloveapp.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rainbowloveapp.app.MyApplication;
import com.rainbowloveapp.app.R;
import com.rainbowloveapp.app.model.ArtImageLocalModel;
import com.rainbowloveapp.app.ui.InAppPurchaseActivity;
import com.rainbowloveapp.app.ui.MainActivity;
import com.rainbowloveapp.app.utils.AnalyticsHelper;
import com.rainbowloveapp.app.utils.Utils;
import com.localytics.android.Localytics;

import java.util.List;

/**
 *  on 5/8/16.
 */
public class ArtImageListAdapter extends RecyclerView.Adapter<ArtImageListAdapter.CustomViewHolder> {

    private List<ArtImageLocalModel> artImageList;
    private Context mContext;


    public ArtImageListAdapter(Context mContext, List<ArtImageLocalModel> artImageList) {

        this.mContext       = mContext;
        this.artImageList   = artImageList;

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_art_image_layout, parent, false);

        CustomViewHolder viewHolder = new CustomViewHolder(itemView);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {

        final ArtImageLocalModel model = artImageList.get(position);

        if(model.getStatus().equalsIgnoreCase("0")){
            if(Utils.isConnectingToInternet(mContext))
            {
                Utils.loadImagePlaceholderGlide(mContext, model.getImageUrl(), holder.img_art_image);
                //Log.i("onBindViewHolder...","...IF..." + model.getStatus());
            }
        }else{
            //Log.i("onBindViewHolder...","...Else..." + model.getStatus());
            byte [] encodeByte= Base64.decode(model.getCatImage(),Base64.DEFAULT);
            Utils.loadImagePlaceholderGlide(mContext, encodeByte, holder.img_art_image);
        }

        holder.img_art_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!Utils.getStringPreference(mContext, Utils.UnlockallArt_key, "").equalsIgnoreCase(Utils.Purchased)) {

                    List<String> catIdPurchased = ((MainActivity) mContext).getCatIdFromInAppTable();

                    if (catIdPurchased.contains(model.getCatId())) {  // already purchased

                        if (model.getStatus().equalsIgnoreCase("0")) {
                            ((MainActivity) mContext).addImageview(null, model.getImageUrl(), new byte[0]);
                        } else {
                            byte[] encodeByte = Base64.decode(model.getCatImage(), Base64.DEFAULT);
                            ((MainActivity) mContext).addImageview(null, "", encodeByte);
                        }

                        ((MainActivity) mContext).addArtImageToRecent(model);

                        Localytics.tagEvent(AnalyticsHelper.Event_Art_Added);
                        MyApplication.getFirebaseAnalytics().logEvent((AnalyticsHelper.Event_Art_Added).replace(" ","_"), null);

                        if(Utils.getStringPreference(mContext, Utils.Art_QuickTips_DontShow_key, "false").equalsIgnoreCase("false")){
                            ((MainActivity) mContext).artQuickTipsDialog();
                        }

                    } else {

                        // ------------------- Remove free trial concept --------------------

                        /*if (((MainActivity) mContext).getTryFromCatID(model.getCatId()).isEmpty() ||
                                ((MainActivity) mContext).getTryFromCatID(model.getCatId()).equalsIgnoreCase("1")) { // max 2 try allow.....default empty

                            if (model.getStatus().equalsIgnoreCase("0")) {
                                ((MainActivity) mContext).addImageview(null, model.getImageUrl(), new byte[0]);
                            } else {
                                byte[] encodeByte = Base64.decode(model.getCatImage(), Base64.DEFAULT);
                                ((MainActivity) mContext).addImageview(null, "", encodeByte);
                            }

                            ((MainActivity) mContext).addArtImageToRecent(model);

                            // update try value
                            if (((MainActivity) mContext).getTryFromCatID(model.getCatId()).isEmpty()) {
                                ((MainActivity) mContext).updateTryArtImage(model.getCatId(), "1");
                            } else {
                                ((MainActivity) mContext).updateTryArtImage(model.getCatId(), "2");
                            }

                            Localytics.tagEvent(AnalyticsHelper.Event_Art_Added);
                            MyApplication.getFirebaseAnalytics().logEvent((AnalyticsHelper.Event_Art_Added).replace(" ","_"), null);

                            if(Utils.getStringPreference(mContext, Utils.Art_QuickTips_DontShow_key, "false").equalsIgnoreCase("false")){
                                ((MainActivity) mContext).artQuickTipsDialog();
                            }

                        } else
                            */

                        // ------------------------------

                        { // go for inAppPurchase

                            Intent intent = new Intent(mContext, InAppPurchaseActivity.class);
                            intent.putExtra(Utils.Type_Inapp_key, Utils.Art);
                            intent.putExtra(Utils.InappId_key, ((MainActivity) mContext).getInAppIdFromCatID(model.getCatId()));
                            intent.putExtra(Utils.CatId_Inapp_key, model.getCatId());
                            mContext.startActivity(intent);

                        }

                    }

                }else{ // already purchase all Atrs

                    if (model.getStatus().equalsIgnoreCase("0")) {
                        ((MainActivity) mContext).addImageview(null, model.getImageUrl(), new byte[0]);
                    } else {
                        byte[] encodeByte = Base64.decode(model.getCatImage(), Base64.DEFAULT);
                        ((MainActivity) mContext).addImageview(null, "", encodeByte);
                    }

                    ((MainActivity) mContext).addArtImageToRecent(model);

                    Localytics.tagEvent(AnalyticsHelper.Event_Art_Added);
                    MyApplication.getFirebaseAnalytics().logEvent((AnalyticsHelper.Event_Art_Added).replace(" ","_"), null);

                    if(Utils.getStringPreference(mContext, Utils.Art_QuickTips_DontShow_key, "false").equalsIgnoreCase("false")){
                        ((MainActivity) mContext).artQuickTipsDialog();
                    }

                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return artImageList.size();
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView img_art_image;


        public CustomViewHolder(View view) {
            super(view);
            this.img_art_image     = (ImageView) view.findViewById(R.id.img_art_image);
        }
    }


}
