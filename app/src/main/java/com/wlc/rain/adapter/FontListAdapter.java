package com.rainbowloveapp.app.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rainbowloveapp.app.R;
import com.rainbowloveapp.app.ui.MainActivity;

/**
 *  on 5/8/16.
 */
public class FontListAdapter extends RecyclerView.Adapter<FontListAdapter.CustomViewHolder> {

    private String[] fontList;
    private Context mContext;


    public FontListAdapter(Context mContext) {

        this.mContext   = mContext;
        this.fontList   = mContext.getResources().getStringArray(R.array.font_array);

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_font, parent, false);

        CustomViewHolder viewHolder = new CustomViewHolder(itemView);

        //Log.i("viewType....","....." + viewType);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {

        String fontName = fontList[position];

        final Typeface custFont = Typeface.createFromAsset(mContext.getAssets(), "fonts/"+fontName);


        holder.tv_item_font.setText(fontName.split("\\.")[0]);
        holder.tv_item_font.setTypeface(custFont);

        holder.tv_item_font.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) mContext).setCustomFont(custFont, position);
                ((MainActivity)mContext).currentFontPosition = position;
                notifyDataSetChanged();
            }
        });

        if(((MainActivity)mContext).currentFontPosition == position){
            holder.linear_item_font.setBackgroundResource(R.color.font_select_bg);
        }else{
            holder.linear_item_font.setBackgroundResource(android.R.color.transparent);
        }

    }

    @Override
    public int getItemCount() {
        return fontList.length;
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected LinearLayout linear_item_font;
        protected TextView tv_item_font;


        public CustomViewHolder(View view) {
            super(view);
            this.linear_item_font = (LinearLayout) view.findViewById(R.id.linear_item_font);
            this.tv_item_font     = (TextView) view.findViewById(R.id.tv_item_font);
        }
    }


}
