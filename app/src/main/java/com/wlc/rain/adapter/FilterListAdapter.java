package com.rainbowloveapp.app.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainbowloveapp.app.R;
import com.rainbowloveapp.app.model.FilterModel;
import com.rainbowloveapp.app.ui.MainActivity;
import com.rainbowloveapp.app.utils.Utils;

import java.util.List;

/**
 *  on 5/8/16.
 */
public class FilterListAdapter extends RecyclerView.Adapter<FilterListAdapter.CustomViewHolder> {

    private Context mContext;
    private String[] filter_inapp_id;

    List<FilterModel> filterList;

    int imageSize;

    int pos = 0;

    GestureDetector detector;


    public FilterListAdapter(Context mContext, List<FilterModel> filterList, String[] filter_inapp_id) {

        this.mContext = mContext;

        //filter_inapp_id = mContext.getResources().getStringArray(R.array.filter_inapp_id_array);

        this.filterList = filterList;

        this.filter_inapp_id = filter_inapp_id;

        imageSize = Utils.getSizefromDp(mContext, 100);

        detector = new GestureDetector(mContext, new GestureTap());


    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter_layout, parent, false);

        CustomViewHolder viewHolder = new CustomViewHolder(itemView);

        //Log.i("viewType....","....." + viewType);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {

        final FilterModel model = filterList.get(position);

        Bitmap mainBitmap = Bitmap.createScaledBitmap(Utils.ImageMain, imageSize, imageSize, false);

        Bitmap filterBitmap = Utils.decodeSampledBitmapFromResource(mContext.getResources(), model.getFilterDrawableID(), 100, 100);

        //Bitmap filterBitmap = BitmapFactory.decodeResource(mContext.getResources(), model.getFilterDrawableID());
        //filterBitmap = Bitmap.createScaledBitmap(filterBitmap, imageSize, imageSize, false);

        //new filterAsync(holder).execute(mainBitmap, filterBitmap);

        //Bitmap mainBitmap = bitmaps[0];
        //Bitmap filterBitmap = bitmaps[1];

        // -------------------------------
        Bitmap result = mainBitmap.copy(Bitmap.Config.ARGB_8888, true);

        Paint p = new Paint();
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.OVERLAY));//
        p.setShader(new BitmapShader(Bitmap.createScaledBitmap(filterBitmap, imageSize, imageSize, false), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

        Canvas c = new Canvas();
        c.setBitmap(result);
        c.drawBitmap(mainBitmap, 0, 0, null);
        c.drawRect(0, 0, mainBitmap.getWidth(), mainBitmap.getHeight(), p);
        // -------------------------------

        holder.img_bg_filter.setImageBitmap(result);


        holder.tv_name_filter.setText(model.getName());

        if(model.isSaved()){
            holder.tv_name_filter.setBackgroundColor(Color.BLACK);
            holder.tv_name_filter.setTextColor(Color.WHITE);
        }else if(model.isSelected()){
            if(MainActivity.isTempSelectFilter){
                holder.tv_name_filter.setBackgroundColor(Color.BLACK);
                holder.tv_name_filter.setTextColor(Color.WHITE);
            }else{
                holder.tv_name_filter.setBackgroundColor(Color.WHITE);
                holder.tv_name_filter.setTextColor(Color.BLACK);
            }
        }else {
            holder.tv_name_filter.setBackgroundColor(Color.WHITE);
            holder.tv_name_filter.setTextColor(Color.BLACK);
        }

        // check for lock image
        ((MainActivity)mContext).isFilterPurchased(filter_inapp_id[position], holder.img_lock_filter);


        /*holder.img_bg_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/

        holder.img_bg_filter.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pos = position;
                detector.onTouchEvent(event);
                return true;
            }
        });


        //Log.i("filter......","..position..." + position + "...name..." + filterList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView img_bg_main_filter;
        protected ImageView img_bg_filter;
        protected TextView tv_name_filter;
        protected ImageView img_lock_filter;


        public CustomViewHolder(View view) {
            super(view);
            this.img_bg_main_filter     = (ImageView) view.findViewById(R.id.img_bg_main_filter);
            this.img_bg_filter     = (ImageView) view.findViewById(R.id.img_bg_filter);
            this.tv_name_filter     = (TextView) view.findViewById(R.id.tv_name_filter);
            this.img_lock_filter         = (ImageView) view.findViewById(R.id.img_lock_filter);
        }
    }



    public class filterAsync extends AsyncTask<Bitmap, Void, Bitmap> {

        CustomViewHolder viewHolder;

        public filterAsync(CustomViewHolder viewHolder){
            this.viewHolder = viewHolder;
        }
        @Override
        protected Bitmap doInBackground(Bitmap... bitmaps) {

            Bitmap mainBitmap = bitmaps[0];
            Bitmap filterBitmap = bitmaps[1];

            // -------------------------------
            Bitmap result = mainBitmap.copy(Bitmap.Config.ARGB_8888, true);

            Paint p = new Paint();
            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.OVERLAY));//
            p.setShader(new BitmapShader(Bitmap.createScaledBitmap(filterBitmap, imageSize, imageSize, false), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

            Canvas c = new Canvas();
            c.setBitmap(result);
            c.drawBitmap(mainBitmap, 0, 0, null);
            c.drawRect(0, 0, mainBitmap.getWidth(), mainBitmap.getHeight(), p);
            // -------------------------------

//            Log.v("filterBitmap..","...width..." + filterBitmap.getWidth() + "...height.." + filterBitmap.getHeight());
//            Log.v("ImageMain..","...width..." + mainBitmap.getWidth() + "...height.." + mainBitmap.getHeight());
//            Log.v("result..","...width..." + result.getWidth() + "...height.." + result.getHeight());


            /*if (filterBitmap != null && !filterBitmap.isRecycled()) {
                filterBitmap.recycle();
                filterBitmap = null;
            }
            if (mainBitmap != null && !mainBitmap.isRecycled()) {
                mainBitmap.recycle();
                mainBitmap = null;
            }*/

            /*ByteArrayOutputStream baos=new  ByteArrayOutputStream();
            result.compress(Bitmap.CompressFormat.JPEG,50, baos);
            byte [] b=baos.toByteArray();
            String temp= Base64.encodeToString(b, Base64.DEFAULT);*/


            return result;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            viewHolder.img_bg_filter.setImageBitmap(bitmap);
            //Picasso.with(mContext).load(bitmap_str).into(viewHolder.img_bg_filter);
            //Glide.with(mContext).load(bitmap_str).asBitmap().into(viewHolder.img_bg_filter);
            //Utils.loadImageGlide(mContext, bitmap_str, viewHolder.img_bg_filter);

        }
    }

    public void applyFilter(){

        FilterModel model = filterList.get(pos);

        // add lock icon on Main Screen if not purchased
        ((MainActivity)mContext).isFilterPurchased(filter_inapp_id[pos], ((MainActivity)mContext).img_lock_on_image);

        // ------- check inApp on filter click --------- changed ---
                /*if(!((MainActivity)mContext).isFilterPurchased(filter_inapp_id[position], holder.img_lock_filter).isEmpty()){
                    String tmpInAppId = ((MainActivity)mContext).isFilterPurchased(filter_inapp_id[position], holder.img_lock_filter);

                    Intent intent = new Intent(mContext, InAppPurchaseActivity.class);
                    intent.putExtra(Utils.Type_Inapp_key, Utils.Filter);
                    intent.putExtra(Utils.InappId_key, tmpInAppId);
                    intent.putExtra(Utils.CatId_Inapp_key, "");
                    mContext.startActivity(intent);

                    return;
                }*/
        // ---------------------------------------------


                /*Log.i("savedFilterIndexList...", "..length..before.." + MainActivity.savedFilterIndexList.size() + "..." + MainActivity.savedFilterIndexList);
                if (MainActivity.savedFilterIndexList.contains(position)) {
                    MainActivity.savedFilterIndexList.remove(Integer.valueOf(position));

                    MainActivity.filterListTemp.remove(position);
                } else if(MainActivity.isTempSelectFilter){
                    //MainActivity.isTempSelectFilter = true;
                    if (MainActivity.savedFilterIndexList.size() > 0) {
                        MainActivity.savedFilterIndexList.remove(MainActivity.savedFilterIndexList.size() - 1); // remove last index
                    }
                }else{
                    MainActivity.isTempSelectFilter = true;
                }


                MainActivity.savedFilterIndexList.add(position);

                Log.i("savedFilterIndexList...","..length...after.." + MainActivity.savedFilterIndexList.size() + "..." + MainActivity.savedFilterIndexList);*/

        // ----------------------------------------------------------------

                /*if(filterList.get(MainActivity.lastSelectFilterPosition).isSelected()){
                    filterList.get(MainActivity.lastSelectFilterPosition).setSelected(false);
                }*/

        if(MainActivity.lastSelectFilterPosition != pos) {
            if (filterList.get(MainActivity.lastSelectFilterPosition).isSelected()) {
                filterList.get(MainActivity.lastSelectFilterPosition).setSelected(false);
            }
        }

        if(pos == 0){
            //((MainActivity)mContext).clearAllFilterOriginal();

            // Open warning dialog
            ((MainActivity)mContext).removeAllFiltersWarningDialog();
        }else{

                    /*if(!model.isSaved()){
                        MainActivity.isTempSelectFilter = true;
                        model.setSelected(true);
                    }*/
            if(!model.isSaved()){
                if(model.isSelected()){
                    model.setSelected(false);
                }else{
                    MainActivity.isTempSelectFilter = true;
                    model.setSelected(true);
                    MainActivity.lastSelectFilterPosition = pos;
                }
            }else{
                MainActivity.lastSelectFilterPosition = pos;
            }

                    /*MainActivity.lastSelectFilterPosition = position;*/

            notifyDataSetChanged();

            ((MainActivity)mContext).filterApplyRemoveText();
            ((MainActivity)mContext).setTempFilterToEditor();
        }


    }

    class GestureTap extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            //Log.i("onDoubleTap :", "" + e.getAction());
            applyFilter();
            ((MainActivity)mContext).onViewClicked(((MainActivity) mContext).findViewById(R.id.tv_filter_edit));
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            //Log.i("onSingleTap :", "" + e.getAction());
            applyFilter();
            return true;
        }
    }

}
