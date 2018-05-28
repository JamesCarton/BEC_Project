package com.wlc.beacon.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.wlc.beacon.R;
import com.wlc.beacon.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



public class CardSwipeAdapter extends PagerAdapter {

    Activity context;
    String mediaType;
    List<String> imgUrl;


    public CardSwipeAdapter(Activity context, String mediaType, List<String> imgUrl) {
        this.context = context;
        this.mediaType = mediaType;
        this.imgUrl = imgUrl;
    }


    @Override
    public int getCount() {
        return imgUrl.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View convertView, @NonNull Object object) {
        return convertView == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup collection, final int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        final ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.item_swipe_card_layout, collection, false);
        Log.v("url..","..." + mediaType + "..." +  imgUrl.get(position));

        String imageURL = imgUrl.get(position);

        if (imageURL.contains("youtube")) {
            imageURL = Utils.getYoutubeThumbnailUrlFromVideoUrl(imgUrl.get(position));
            layout.findViewById(R.id.item_youtube_ic).setVisibility(View.VISIBLE);
        } else {
            layout.findViewById(R.id.item_youtube_ic).setVisibility(View.GONE);
        }

        Utils.loadImageGlide(context, imageURL, ((ImageView)layout.findViewById(R.id.item_swipe_card_image)));



        ((ImageView)layout.findViewById(R.id.item_swipe_card_image)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imgUrl.get(position).contains("youtube")) {
                    Intent intent = YouTubeStandalonePlayer.createVideoIntent(context, "AIzaSyCDvmPNeKQvELfNQNacTD4VcS48gZPGlRs", Utils.getYoutubeVideoIdFromUrl(imgUrl.get(position)), 0, true, false);
                    context.startActivity(intent);
                }

            }
        });

        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }



}
