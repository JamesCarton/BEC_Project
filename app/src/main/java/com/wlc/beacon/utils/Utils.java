package com.wlc.beacon.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ScaleXSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wlc.beacon.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;


/**
 *  on 23/03/18.
 */

public class Utils {

    public static final String WebActivity_Key     = "web_activity";
    public static final String CardID_Key          = "card_id";
    public static final String CardCategory_Key    = "CardCategory";

    public static final String PlayerInvite_Key   = "player_invite_key";

    public static final String NotifMSG_Key     = "notif_msg";


    public static final String PrivacyPolicy        = "PrivacyPolicy";
    public static final String DesignTips           = "DesignTips";
    public static final String FAQ                  = "FAQ";
    public static final String EditOfWeek           = "EditOfWeek";
    public static final String FollowInsta          = "FollowInsta Pitch";
    public static final String TermService          = "TermService";
    public static final String FilterTips           = "FilterTips";
    public static final String HomeFilterTips       = "HomeFilterTips";
    public static final String GetTips              = "GetTips";

    public static final String DeviceType           = "Android";

    // Invite from
    public static final String Facebook     = "Facebook";
    public static final String Google       = "Google";
    public static final String Twitter      = "Twitter";
    public static final String Phone        = "Phone";

    public static final String SocialTypeFacebook   = "facebook";
    public static final String SocialTypeGmail      = "gmail";

    public static final String CityState            = "city_state";
    public static final String LocationWiseStore    = "location_wise_store";
    public static final String DistanceFilter       = "distance_filter";
    public static final String CampaignViewFilter   = "filter";
    public static final String CampaignViewFavourite= "favourite";



    public static final String CustomBroadcastAction = "custom_broadcast_action";

    // Preference Key
    public static final String IsFirstTime_key     = "is_first_time";

    public static final String UserDetail_Key      = "userdetail";
    public static final String AccessToken_Key     = "access_token";

    public static final String BeaconList_Key      = "beacon_list";

    public static final String SelectedLang_Key = "language";

    public static final String CampaignPosition_Key = "campaign_position";

    public static final String CampaignRoot_Key     = "campaign_root";

    public static final String CampaignRootList_Key = "campaign_root_list";
    public static final String CampaignTitle_Key    = "campaign_title";

    public static int DeviceWidth;
    public static int DeviceHeight;

    public static double DeviceLatitude     = 0;
    public static double DeviceLongitude    = 0;

    public static int getScreenWidth(Activity activity) {

        Display display = activity.getWindowManager().getDefaultDisplay();

        if (Build.VERSION.SDK_INT >= 13) {
            Point size = new Point();
            display.getSize(size);

            return size.x;
        } else {
            return display.getWidth();
        }
    }

    public static int getScreenHeight(Activity activity) {

        Display display = activity.getWindowManager().getDefaultDisplay();

        if (Build.VERSION.SDK_INT >= 13) {
            Point size = new Point();
            display.getSize(size);

            return size.y;
        } else {
            return display.getHeight();
        }
    }

    public static boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!&^%$#@()/])(?=\\S+$).{7,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    public static void showDialog(Context context,String msg)
    {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        builder.title(context.getString(R.string.app_name));
        builder.titleGravity(GravityEnum.CENTER);
        builder.content(msg);
        builder.cancelable(false);
        builder.positiveText(context.getString(R.string.ok));
        builder.positiveColor(Color.parseColor("#303F9F"));
        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog dialog, DialogAction which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public static void showTitleDialog(Context context, String title, String msg)
    {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        builder.title(title);
        builder.titleGravity(GravityEnum.CENTER);
        builder.content(msg);
        builder.cancelable(false);
        builder.positiveText(context.getString(R.string.ok));
        builder.positiveColor(Color.parseColor("#303F9F"));
        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog dialog, DialogAction which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }




    public static boolean isConnectingToInternet(Context cont){

        ConnectivityManager cm = (ConnectivityManager) cont.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static String convertDateToStringUS(Date date, String dateFormat)
    {
        SimpleDateFormat dateformat = new SimpleDateFormat(dateFormat, Locale.US);

        String datetime = null;
        try {
            //Date date = new Date();
            datetime = dateformat.format(date);
            System.out.println("Current Date Time : " + datetime);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return datetime;
    }

    public static Date convertStringToDateUS(String date_str, String dateFormat)
    {
        SimpleDateFormat dateformat = new SimpleDateFormat(dateFormat, Locale.US);

        Date date = null;
        try {
            date = dateformat.parse(date_str);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return date;
    }

    public static String convertDateToStringDefault(Date date, String dateFormat)
    {
        SimpleDateFormat dateformat = new SimpleDateFormat(dateFormat, Locale.getDefault());

        String datetime = null;
        try {
            //Date date = new Date();
            datetime = dateformat.format(date);
            System.out.println("Current Date Time : " + datetime);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return datetime;
    }

    public static Date convertStringToDateDefault(String date_str, String dateFormat)
    {
        SimpleDateFormat dateformat = new SimpleDateFormat(dateFormat, Locale.getDefault());

        Date date = null;
        try {
            date = dateformat.parse(date_str);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return date;
    }

    public static void hideKeyBoardFromWindow(Context context, View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive())
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String ordinal(int i) {
        String[] sufixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return String.format(Locale.US, "%02d", i) + "th";
            default:
                return String.format(Locale.US, "%02d", i) + sufixes[i % 10];

        }
    }

    public static JSONArray cur2JsonArray(Cursor cursor) {

        JSONArray resultSet = new JSONArray();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();
            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {
                    try {
                        rowObject.put(cursor.getColumnName(i),
                                cursor.getString(i));
                    } catch (Exception e) {
                        Log.d("TAG", e.getMessage());
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }

        cursor.close();
        return resultSet;

    }

    public static Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        //bitmap.recycle();

        return output;
    }

    public static Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap( v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
        v.draw(c);
        return b;
    }

    public static String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,60, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public static String BitMapToStringPNG(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,60, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public static Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }


    public static String formattedNumber(float number) {
        return String.format(Locale.US, "%,.2f", number);
    }


    public static List<String> checkPermissionList(Context context)
    {
        List<String> permissions = new ArrayList<String>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            //int hasLocationPermission = context.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);
            int hasWriteExternalPermission = context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);


            /*if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            }*/

            if (hasWriteExternalPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            return permissions;
        }
        else
        {
            return permissions;
        }
    }

    public static void OpenSettingPermissionDialog(final Context context)
    {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        builder.cancelable(false);
        builder.content("You need to grant all permission to access feature of this app. \n\nGo to Setting > Permissions & allow all permission.");
        builder.contentColor(Color.BLACK);
        builder.positiveText("Setting");
        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", context.getPackageName(), null));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
        builder.show();

    }


    public static String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("country_code.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static void setStringPreference(Context context, String key,String value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public static String getStringPreference(Context context, String key, String defValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        return preferences.getString(key, defValue);
    }

    // Remove Key Preference
    public static void removePreference(Context context, String key){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.apply();
    }

    // Check Contains Preference
    public static boolean isContainsPreference(Context context, String key){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        return preferences.contains(key);
    }

    public static int getSizefromDp(Context context,float dps_size) {

        final float scale = context.getResources().getDisplayMetrics().density;
        int size = (int) (dps_size * scale + 0.5f);

        return size;
    }

    public static void loadImageGlide(Context context, String imgURL, ImageView imageView){
        Glide.with(context)
            .load(imgURL)
            //.diskCacheStrategy(DiskCacheStrategy.NONE) // while getting image from URL Cache None
            //.skipMemoryCache(true)
            .into(imageView);
    }

    public static void loadImageGlide(Context context, int imgID, ImageView imageView){
        Glide.with(context)
                .load(imgID)
                //.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                //.skipMemoryCache(true)
                .into(imageView);
    }

   /* public static void loadImagePlaceholderGlide(Context context, int img, ImageView imageView){
        Glide.with(context)
                .load(img)
                .placeholder(R.drawable.placeholder_img)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                //.skipMemoryCache(true)
                .into(imageView);
    }

    public static void loadImagePlaceholderGlide(Context context, String img, ImageView imageView){
        Glide.with(context)
                .load(img)
                .placeholder(R.drawable.placeholder_img)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                //.diskCacheStrategy(DiskCacheStrategy.NONE) // while getting image from URL Cache None
                //.skipMemoryCache(true)
                .into(imageView);
    }

    public static void loadImagePlaceholderGlide(Context context, byte[] img, ImageView imageView){
        Glide.with(context)
                .load(img)
                .placeholder(R.drawable.placeholder_img)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                //.skipMemoryCache(true)
                .into(imageView);
    }*/

   /* public static void loadImage(Context context, String imgUrl, final ImageView imageView) {

        Target t = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {


                imageView.setImageBitmap(bitmap);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                Log.i("userImage Link...", "....onBitmapLoaded");
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.i("userImage Link...", "....onBitmapFailed");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.i("userImage Link...", "....onPrepareLoad");
            }
        };

        //Picasso.with(context).load(imgUrl).resize(120, 120).into(t);
        Picasso.with(context).load(imgUrl).placeholder(R.drawable.placeholder_img).into(t);

        imageView.setTag(t);

    }*/


    /*public static void loadImageWithId(Context context, int imgid, final ImageView imageView) {

        Target t = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                //bitmap = Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*0.3), (int)(bitmap.getHeight()*0.3), true);
                //bitmap = Bitmap.createScaledBitmap(bitmap,(int)(imageView.getWidth()), (int)(imageView.getHeight()), true);

                imageView.setBackgroundResource(R.drawable.black_round_border);
                imageView.setImageBitmap(getCircleBitmap(bitmap));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setPadding(1,1,1,1);
                Log.i("userImage Link...", "....onBitmapLoaded");
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.i("userImage Link...", "....onBitmapFailed");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.i("userImage Link...", "....onPrepareLoad");
            }
        };

        //Picasso.with(context).load(imgUrl).resize(120, 120).into(t);
        Picasso.with(context).load(imgid).into(t);

        imageView.setTag(t);

    }*/

   /* public static void loadImageWhiteBorder(Context context, String imgUrl, final ImageView imageView) {

        Target t = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                //bitmap = Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*0.3), (int)(bitmap.getHeight()*0.3), true);
                //bitmap = Bitmap.createScaledBitmap(bitmap,(int)(imageView.getWidth()), (int)(imageView.getHeight()), true);

                imageView.setBackgroundResource(R.drawable.white_round_border);
                imageView.setImageBitmap(getCircleBitmap(bitmap));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setPadding(5,5,5,5);
                Log.i("userImage Link...", "....onBitmapLoaded");
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.i("userImage Link...", "....onBitmapFailed");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.i("userImage Link...", "....onPrepareLoad");
            }
        };

        //Picasso.with(context).load(imgUrl).resize(120, 120).into(t);
        Picasso.with(context).load(imgUrl).into(t);

        imageView.setTag(t);

    }
*/
    /*public static void loadImage_withoutCircle(final Context context, String imgUrl, final ImageView imageView, final ImageView.ScaleType scaleType) {

        Target t = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                //bitmap = Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*0.3), (int)(bitmap.getHeight()*0.3), true);
                //bitmap = Bitmap.createScaledBitmap(bitmap,(int)(imageView.getWidth()), (int)(imageView.getHeight()), true);

                imageView.setImageBitmap(bitmap);
                imageView.setScaleType(scaleType);
                Log.i("userImage Link...", "....onBitmapLoaded");
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.i("userImage Link...", "....onBitmapFailed");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.i("userImage Link...", "....onPrepareLoad");
            }
        };

        int width = getSizefromDp(context, 100);
        //Picasso.with(context).load(imgUrl).resize(width, width).into(t);
        Picasso.with(context).load(imgUrl).into(t);

        imageView.setTag(t);

    }

    public static void loadImageUrlWithBlurry(final Context context, String imgUrl, final ImageView imageView) {

        Target t = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                //bitmap = Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*0.3), (int)(bitmap.getHeight()*0.3), true);
                //bitmap = Bitmap.createScaledBitmap(bitmap,(int)(imageView.getWidth()), (int)(imageView.getHeight()), true);

                //imageView.setImageBitmap(bitmap);
                Blurry.with(context).sampling(10).from(bitmap).into(imageView);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                Log.i("userImage Link...", "....onBitmapLoaded");
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.i("userImage Link...", "....onBitmapFailed");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.i("userImage Link...", "....onPrepareLoad");
            }
        };

        int width = getSizefromDp(context, 100);
        //Picasso.with(context).load(imgUrl).resize(width, width).into(t);
        Picasso.with(context).load(imgUrl).into(t);

        imageView.setTag(t);

    }
*/


    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixel) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixel;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static Date utcTolocalTimeZone(String date_str) {

        //String date = "Thursday, May 05, 2016 08:00 AM";

        final SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        mFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));	// entered timezone
        Date dateet = null;
        try {
            dateet = mFormatter.parse(date_str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mFormatter.setTimeZone(TimeZone.getDefault());		// result timezone
        String formattedDate = mFormatter.format(dateet);


        try {
            dateet = mFormatter.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //Log.d("finddate", "" + date_str + "  " + formattedDate);

        return dateet;

    }

    /*public static Bitmap loadBitmapFromViewBGWhite(View v,Context context) {
        Bitmap b = Bitmap.createBitmap( v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.drawColor(ContextCompat.getColor(context,R.color.gray_back));
        v.layout(0, 0, v.getLayoutParams().width , v.getLayoutParams().height);
        v.draw(c);
        return b;
    }*/

    public static Bitmap loadBitmapFromViewBGWhite_Custom(View v,Context context, int height) {
        Bitmap b = Bitmap.createBitmap( v.getWidth(), height , Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.drawColor(ContextCompat.getColor(context, android.R.color.white));
        v.layout(v.getLeft(), v.getTop(), v.getWidth(), v.getHeight());
        v.draw(c);
        return b;
    }


    public static void showProgressDialog(ProgressDialog mProgressDialog, String message) {
        try {

            if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                mProgressDialog.setMessage(message);
                //mProgressDialog.setCancelable(isCancelable);
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dismissProgressDialog(ProgressDialog mProgressDialog) {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] getByteArrayFromImageview(Context context, ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] getByteArrayFromBitmap(Context context, Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static int getDrawableResIDFromName(Context context, String imgName){

        // also remove ".png" / ".jpg" from file
        int resourceID = context.getResources().getIdentifier(imgName.split("\\.")[0], "drawable", context.getPackageName());

        return resourceID;
    }

    public static Spannable applyKerning(CharSequence src, float kerning)
    {
        if (src == null) return null;
        final int srcLength = src.length();
        if (srcLength < 2) return src instanceof Spannable
                ? (Spannable)src
                : new SpannableString(src);

        final String nonBreakingSpace = "\u00A0";
        final SpannableStringBuilder builder = src instanceof SpannableStringBuilder
                ? (SpannableStringBuilder)src
                : new SpannableStringBuilder(src);
        for (int i = src.length() - 1; i >= 1; i--)
        {
            builder.insert(i, nonBreakingSpace);
            builder.setSpan(new ScaleXSpan(kerning), i, i + 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return builder;
    }


    public static Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, 0, 0, null);
        return bmOverlay;
    }

    public static Bitmap mergeMultiple(Bitmap[] parts){

        Bitmap result = Bitmap.createBitmap(parts[0].getWidth(), parts[0].getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        for (int i = 0; i < parts.length; i++) {
            canvas.drawBitmap(parts[i], parts[i].getWidth() * (i % 2), parts[i].getHeight() * (i / 2), paint);
        }
        return result;
    }


    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap createBlackAndWhite(Bitmap src) {
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                int gray = (int) (0.2989 * R + 0.5870 * G + 0.1140 * B);
                // use 128 as threshold, above -> white, below -> black
                if (gray > 128) {
                    gray = 255;
                }
                else{
                    gray = 0;
                }
                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, gray, gray, gray));
            }
        }
        return bmOut;
    }







    public static void setBitmapAsTextColor(final EditText txt, final Bitmap bitmap) {
        final Shader.TileMode tile_mode = Shader.TileMode.REPEAT;
        final int height = txt.getHeight();
        final int width = txt.getWidth();
        final Bitmap temp = Bitmap.createScaledBitmap(bitmap, width, height, true);
        final BitmapShader bitmapShader = new BitmapShader(temp, tile_mode, tile_mode);
        txt.getPaint().setShader(bitmapShader);
    }

    public static Rect getTextHeightWidth(EditText editText){

        Paint paint = new Paint();
        Rect bounds = new Rect();

        paint.setTypeface(editText.getTypeface());// your preference here
        paint.setTextSize(editText.getTextSize());// have this the same as your text size

        int tmpLength = 0;

        for(String txt : editText.getText().toString().split("\\n")){
            if(txt.length() > tmpLength){
                tmpLength = txt.length();
                paint.getTextBounds(txt, 0, txt.length(), bounds);
            }
        }

        //paint.getTextBounds(editText.getText().toString(), 0, editText.getText().length(), bounds);

        return bounds;
    }

    public static boolean isPackageInstalled(String packagename, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static Bundle hashMapToBundle(Map<String, String> articleParams) {

        Bundle output = new Bundle();

        for (String key : articleParams.keySet()) {
            output.putString(key, articleParams.get(key));
        }

        return output;

    }

    public static void openAppOnPlayStore(Context context){
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName())));
        } catch (Exception e) {
            openWebPage(context, "http://play.google.com/store/apps/details?id=" + context.getPackageName());
        }
    }

    public static void openWebPage(Context context, String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, context.getString(R.string.no_suitable_app_found), Toast.LENGTH_LONG).show();
        }
    }

    /*
    pixelSpacing tells how many pixels to skip each pixel.
    If pixelSpacing > 1: the average color is an estimate, but higher values mean better performance
    If pixelSpacing == 1: the average color will be the real average
    If pixelSpacing < 1: the method will most likely crash (don't use values below 1)
    */
    public static int calculateAverageColor(Bitmap bitmap, int pixelSpacing) {
        int R = 0; int G = 0; int B = 0;
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int n = 0;
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < pixels.length; i += pixelSpacing) {
            int color = pixels[i];
            R += Color.red(color);
            G += Color.green(color);
            B += Color.blue(color);
            n++;
        }
        return Color.rgb(R / n, G / n, B / n);
    }

    public static boolean isColorDark(int color){
        double darkness = 1-(0.299*Color.red(color) + 0.587*Color.green(color) + 0.114*Color.blue(color))/255;
        if(darkness<0.5){
            return false; // It's a light color
        }else{
            return true; // It's a dark color
        }
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static String getRealPathFromUri(Context context, final Uri uri) {
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String selection,
                                 String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String getYoutubeThumbnailUrlFromVideoUrl(String videoUrl) {
        String imgUrl = "http://img.youtube.com/vi/"+getYoutubeVideoIdFromUrl(videoUrl) + "/0.jpg";
        return imgUrl;
    }

    public static String getYoutubeVideoIdFromUrl(String inUrl) {
        if (inUrl.toLowerCase().contains("youtu.be")) {
            return inUrl.substring(inUrl.lastIndexOf("/") + 1);
        }
        String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(inUrl);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

}
