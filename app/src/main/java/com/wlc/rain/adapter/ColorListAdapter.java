package com.rainbowloveapp.app.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rainbowloveapp.app.R;
import com.rainbowloveapp.app.ui.MainActivity;
import com.rainbowloveapp.app.utils.Utils;

/**
 *  on 5/8/16.
 */
public class ColorListAdapter extends RecyclerView.Adapter<ColorListAdapter.CustomViewHolder> {

    private Context mContext;
    private String[] colors;
    boolean isTextColor;

    public ColorListAdapter(Context mContext, String[] colors, boolean isTextColor) {

        this.mContext   = mContext;

        this.colors    = colors;

        this.isTextColor = isTextColor;

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_color_layout, parent, false);

        CustomViewHolder viewHolder = new CustomViewHolder(itemView);

        //Log.i("viewType....","....." + viewType);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {

        int image_id = Utils.getDrawableResIDFromName(mContext, colors[position]);

       //Bitmap result = Utils.decodeSampledBitmapFromResource(mContext.getResources(), image_id, 63,70); // 63,70

       // Log.v("Bitmap...adapter","..getWidth.." + result.getWidth() + "...height.." + result.getHeight());
        Glide.with(mContext)
                .load(image_id)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.img_color);

        //holder.img_color.setImageBitmap(result);

        if(isTextColor){
            if(position == 0){
                holder.img_color.setVisibility(View.GONE);
            }else{
                holder.img_color.setVisibility(View.VISIBLE);
            }
        }else{
            holder.img_color.setVisibility(View.VISIBLE);
        }


        holder.img_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) mContext).setTextHighlightColor(String.valueOf(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return colors.length;
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView img_color;


        public CustomViewHolder(View view) {
            super(view);
            this.img_color     = (ImageView) view.findViewById(R.id.img_color);
        }
    }

}
