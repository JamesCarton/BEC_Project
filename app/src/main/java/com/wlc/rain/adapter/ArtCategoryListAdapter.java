package com.rainbowloveapp.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.localytics.android.Localytics;
import com.rainbowloveapp.app.MyApplication;
import com.rainbowloveapp.app.R;
import com.rainbowloveapp.app.model.ArtLocalModel;
import com.rainbowloveapp.app.ui.MainActivity;
import com.rainbowloveapp.app.utils.AnalyticsHelper;
import com.rainbowloveapp.app.utils.Utils;

import java.util.List;

/**
 *  on 5/8/16.
 */
public class ArtCategoryListAdapter extends RecyclerView.Adapter<ArtCategoryListAdapter.CustomViewHolder> {

    private List<ArtLocalModel> artCategoryImageList;
    private Context mContext;


    public ArtCategoryListAdapter(Context mContext, List<ArtLocalModel> artCategoryImageList) {

        this.mContext               = mContext;
        this.artCategoryImageList   = artCategoryImageList;

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_art_category_layout, parent, false);

        CustomViewHolder viewHolder = new CustomViewHolder(itemView);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {

        final ArtLocalModel model = artCategoryImageList.get(position);

        if(model.getCatId().equalsIgnoreCase(Utils.Recent)){    // for Recent Item
            Utils.loadImagePlaceholderGlide(mContext, R.drawable.recently, holder.img_art_category);
        }else{
            if(model.getStatus().equalsIgnoreCase("0")){
                if(Utils.isConnectingToInternet(mContext))
                {
                    Utils.loadImagePlaceholderGlide(mContext, model.getImageUrl(), holder.img_art_category);
                    //Log.i("onBindViewHolder...","...IF..." + model.getStatus());
                }
            }else{
                //Log.i("onBindViewHolder...","...Else..." + model.getStatus());
                byte [] encodeByte= Base64.decode(model.getCatImage(),Base64.DEFAULT);
                Utils.loadImagePlaceholderGlide(mContext, encodeByte, holder.img_art_category);
            }
        }

        holder.img_art_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(model.getCatId().equalsIgnoreCase(Utils.Recent)) {    // for Recent Item
                    ((MainActivity) mContext).setUpRecentArtImageAdapter();
                }else {
                    ((MainActivity) mContext).getArtImage(model.getCatId());
                }
                Log.v("onClick..", "..img_art_category.." + model.getCatId());

                if(model.getCatInappId() != null && !model.getCatInappId().isEmpty()){
                    Localytics.tagEvent(model.getCatInappId());
                    MyApplication.getFirebaseAnalytics().logEvent((model.getCatInappId()).replace(".","_"), null);
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return artCategoryImageList.size();
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView img_art_category;


        public CustomViewHolder(View view) {
            super(view);
            this.img_art_category     = (ImageView) view.findViewById(R.id.img_art_category);
        }
    }


}
