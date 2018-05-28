package com.rainbowloveapp.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rainbowloveapp.app.R;
import com.rainbowloveapp.app.model.CardLocalModel;
import com.rainbowloveapp.app.ui.GridViewActivity;
import com.rainbowloveapp.app.utils.LetterSpacingTextView;
import com.rainbowloveapp.app.utils.Utils;

import java.util.List;

/**
 *  on 5/8/16.
 */
public class CardCategoriesListAdapter extends RecyclerView.Adapter<CardCategoriesListAdapter.CustomViewHolder> {

    private List<CardLocalModel> cardList;
    private Context mContext;


    public CardCategoriesListAdapter(Context mContext, List<CardLocalModel> cardList) {

        this.mContext   = mContext;
        this.cardList   = cardList;

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_categories, parent, false);

        CustomViewHolder viewHolder = new CustomViewHolder(itemView);

        //Log.i("viewType....","....." + viewType);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {

        final CardLocalModel model = cardList.get(position);

        holder.tv_item_card_categories.setText(model.getCardName());


        holder.tv_item_card_categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, GridViewActivity.class);
                intent.putExtra(Utils.CardCategory_Key, model.getCardName());
                intent.putExtra(Utils.CardID_Key, model.getCatId());
                mContext.startActivity(intent);
            }
        });


        //Log.i("onBindViewHolder...","...onBindViewHolder...");

    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected LetterSpacingTextView tv_item_card_categories;


        public CustomViewHolder(View view) {
            super(view);
            this.tv_item_card_categories     = (LetterSpacingTextView) view.findViewById(R.id.tv_item_card_categories);
        }
    }


}
