package com.rainbowloveapp.app.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rainbowloveapp.app.MyApplication;
import com.rainbowloveapp.app.R;
import com.rainbowloveapp.app.model.LoginLocalModel;
import com.rainbowloveapp.app.model.ProfileLocalModel;
import com.rainbowloveapp.app.network.ApiClient;
import com.rainbowloveapp.app.network.ApiInterface;
import com.rainbowloveapp.app.network.DownloadImage;
import com.rainbowloveapp.app.utils.AnalyticsHelper;
import com.rainbowloveapp.app.utils.Utils;
import com.localytics.android.Localytics;
import com.yalantis.ucrop.UCrop;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 *  on 24/7/17.
 */

public class SaveShareActivity extends AppCompatActivity {

    private static final int REQ_SELECT_PHOTO = 1;
    private static final int REQ_START_SHARE = 2;

    private static final int REQ_FOR_CROP = 101;

    private final String SAVE_TO_PHONE = "save_to_phone";
    private final String EMAIL = "email";
    private final String TEXT = "text";
    private final String FACEBOOK = "facebook";
    private final String TWITTER = "twitter";
    private final String INSTAGRAM = "instagram";
    private final String GOOGLE_PLUS = "google_plus";
    private final String UPLOAD_GALLERY = "upload_gallery";


    String imageUri;

    //CallbackManager callbackManager;
    //ShareDialog shareDialog;

    String clickedButton;

    BottomSheetDialog bottomSheetDialog;

    String deviceGmail = "";
    String deviceIMEI = "";
    String deviceInfoStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        //callbackManager = CallbackManager.Factory.create();
        //shareDialog = new ShareDialog(this);

        if (getIntent().getExtras() != null) {
            imageUri = getIntent().getExtras().getString(Utils.ImageUri_key);
        }

        openBottomSheetDialog();

        if(MainActivity.percent_relative_save != null) {
            MainActivity.percent_relative_save.setPadding(0, Utils.getSizefromDp(this, 1), 0, Utils.getSizefromDp(this, 1));
        }

        //shareToFacebook();

        // ------ deviceInfo --------
        getDeviceInfo();
        // --------------------------

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_FOR_CROP && resultCode == RESULT_OK) {
            UCrop.of(Uri.fromFile(new File(imageUri)), Uri.fromFile(new File(getCacheDir(), "tmp_cropped"))).start(this);
        }else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {

            final Uri resultUri = UCrop.getOutput(data);

            saveImageToDownload("", resultUri);

        }

    }

    public void openBottomSheetDialog() {

        View bottomSheetView = getLayoutInflater().inflate(R.layout.share_dialog_layout, null);
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(bottomSheetView);

        bottomSheetView.findViewById(R.id.img_save_to_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("img_save_to_phone...", "..img_save_to_phone...");
                clickedButton = SAVE_TO_PHONE;

                checkImageForCrop();
            }
        });
        bottomSheetView.findViewById(R.id.img_email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedButton = EMAIL;
                checkImageForCrop();
            }
        });
        bottomSheetView.findViewById(R.id.img_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedButton = TEXT;
                checkImageForCrop();
            }
        });
        bottomSheetView.findViewById(R.id.img_facebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedButton = FACEBOOK;
                checkImageForCrop();
            }
        });
        bottomSheetView.findViewById(R.id.img_twitter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedButton = TWITTER;
                checkImageForCrop();
            }
        });
        bottomSheetView.findViewById(R.id.tv_save_to_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedButton = SAVE_TO_PHONE;
                checkImageForCrop();
            }
        });
        bottomSheetView.findViewById(R.id.tv_email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedButton = EMAIL;
                checkImageForCrop();
            }
        });
        bottomSheetView.findViewById(R.id.tv_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedButton = TEXT;
                checkImageForCrop();
            }
        });
        bottomSheetView.findViewById(R.id.tv_facebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedButton = FACEBOOK;
                checkImageForCrop();
            }
        });
        bottomSheetView.findViewById(R.id.tv_twitter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedButton = TWITTER;
                checkImageForCrop();
            }
        });
        bottomSheetView.findViewById(R.id.img_insta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedButton = INSTAGRAM;
                checkImageForCrop();
            }
        });
        bottomSheetView.findViewById(R.id.img_gplus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedButton = GOOGLE_PLUS;

                /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(imageUri)));
                startActivityForResult(intent, REQ_FOR_CROP);*/

                checkImageForCrop();
            }
        });
        bottomSheetView.findViewById(R.id.tv_insta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedButton = INSTAGRAM;
                checkImageForCrop();
            }
        });
        bottomSheetView.findViewById(R.id.tv_gplus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedButton = GOOGLE_PLUS;
                checkImageForCrop();
            }
        });
        bottomSheetView.findViewById(R.id.relative_upload_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedButton = UPLOAD_GALLERY;
                checkImageForCrop();
            }
        });


        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        bottomSheetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Log.v("onCancel...", "..onCancel...");
                Utils.isLogoInAppNotNow = false;
                dialogInterface.dismiss();
                SaveShareActivity.this.finish();
            }
        });
        bottomSheetDialog.show();

    }

    public void shareToFacebook(File file) {

        if(Utils.isPackageInstalled("com.facebook.katana", SaveShareActivity.this)) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setPackage("com.facebook.katana");
            //intent.putExtra(Intent.EXTRA_TEXT, "#RainbowLoveApp");
            //intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/jpg");
            startActivity(intent);
        }else{
            Toast.makeText(SaveShareActivity.this, "Application not found", Toast.LENGTH_LONG).show();
        }

        /*ShareDialog shareDialog = new ShareDialog(this);
        CallbackManager callbackManager = CallbackManager.Factory.create();
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


        SharePhoto photo = new SharePhoto.Builder().setBitmap(bitmap).build();
        SharePhotoContent linkContent = new SharePhotoContent.Builder()
                .addPhoto(photo)
                //.setContentUrl(Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName()))
                .build();
        if (ShareDialog.canShow(SharePhotoContent.class)) {
            shareDialog.show(linkContent);
        }*/

    }

    public void shareToGoolgePlus(File filePath){

        if(Utils.isPackageInstalled("com.google.android.apps.plus", SaveShareActivity.this)){
            Intent shareIntent = ShareCompat.IntentBuilder
                    .from(SaveShareActivity.this)
                    .setText("#RainbowLoveApp #RainbowLoveGreetings #RainbowApp")
                    .setType("image/jpeg").setStream(Uri.fromFile(filePath)).getIntent()
                    .setPackage("com.google.android.apps.plus");
            startActivityForResult(shareIntent, REQ_SELECT_PHOTO);
        }else{
            Toast.makeText(SaveShareActivity.this, "Application not found", Toast.LENGTH_LONG).show();
        }

    }

    public void shareToText(File file){
        if(Utils.isPackageInstalled("com.android.mms", SaveShareActivity.this)) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            //intent.putExtra(Intent.EXTRA_TEXT, "Test message");
            //intent.setClassName("com.android.mms", "com.android.mms.ui.ComposeMessageActivity");
            intent.setPackage("com.android.mms");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/jpg");
            startActivity(intent);
        }else{
            Toast.makeText(SaveShareActivity.this, "Application not found", Toast.LENGTH_LONG).show();
        }
    }

    public void shareToTwitter(File file){
        if(Utils.isPackageInstalled("com.twitter.android", SaveShareActivity.this)) {

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setPackage("com.twitter.android");
            intent.putExtra(Intent.EXTRA_TEXT, "#RainbowLoveApp");
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/jpg");
            startActivity(intent);

           /* String appName = "twitter";
            final PackageManager pm = getPackageManager();
            final List<?> activityList = pm.queryIntentActivities(intent, 0);
            int len = activityList.size();
            Log.d("Tag","Length: "+len);
            for (int i = 0; i < len; i++)
            {
                final ResolveInfo app = (ResolveInfo) activityList.get(i);
                Log.v("Apps on share list: ", app.activityInfo.name);
                if ((app.activityInfo.name.contains(appName)))
                {
                    Log.d("Tag","Found package: "+app.activityInfo.name);
                    final ActivityInfo activity = app.activityInfo;
                    final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                    //final ComponentName name = new ComponentName(activity.applicationInfo.packageName, "com.twitter.android.composer.ComposerActivity");

                    intent.setComponent(name);
                }
            }*/


            //startActivity(Intent.createChooser(intent, "Share image using"));

        }else{
            Toast.makeText(SaveShareActivity.this, "Application not found", Toast.LENGTH_LONG).show();
        }
    }

    public void composeEmail(File file) {
        String content = "I made this just for you! \n\n\nMade with Rainbow Love App  ";
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        //intent.setType("text/plain");
        //intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, "A Rainbow Love Gram For You \uD83C\uDF08❤️");
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, getString(R.string.no_suitable_app_found), Toast.LENGTH_LONG).show();
        }
    }

    public void shareToInsragram(File file){
        if(Utils.isPackageInstalled("com.instagram.android", SaveShareActivity.this)) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setPackage("com.instagram.android");
            intent.putExtra(Intent.EXTRA_TEXT, "#RainbowLoveApp #RainbowLoveGram #RainbowLove @luvisallaroundme");
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/jpg");
            startActivity(intent);
        }else{
            Toast.makeText(SaveShareActivity.this, "Application not found", Toast.LENGTH_LONG).show();
        }
    }

    public void uploadImageToGallery(File file, Bitmap bitmap){

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), Utils.getByteArrayFromBitmap(this, bitmap));

        MultipartBody.Part body = MultipartBody.Part.createFormData("photoimg", "rainbowlove.jpg", reqFile);  //key,file name,file

        final ProgressDialog progressDialog = new ProgressDialog(this);
        Utils.showProgressDialog(progressDialog, getString(R.string.loading));

        Call<ResponseBody> call = apiService.uploadImageGalleryApi(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        String resultStr = response.body().string();
                        JSONObject jsonObject = new JSONObject(resultStr);

                        if(jsonObject.getString(ApiClient.STATUS).equalsIgnoreCase(ApiClient.SUCCESS)){
                            Toast.makeText(SaveShareActivity.this, getString(R.string.upload_image_successfully), Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(SaveShareActivity.this, getString(R.string.pls_try_again), Toast.LENGTH_LONG).show();
                        }

                        Log.v("uploadImageToGallery...", "..jsonObject.." + jsonObject);

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Utils.dismissProgressDialog(progressDialog);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utils.dismissProgressDialog(progressDialog);
            }
        });
    }



    public void savedImageDialog(){

        View view = getLayoutInflater().inflate(R.layout.image_saved_dialog_layout, null);
        final Dialog dialog_saved_image = new Dialog(this);
        int width = (int) (Utils.getScreenWidth(this) * 0.8);
        dialog_saved_image.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_saved_image.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog_saved_image.setContentView(view);
        dialog_saved_image.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog_saved_image.setCancelable(false);

        TextView tv_2 = (TextView) view.findViewById(R.id.tv_2);
        TextView tv_3 = (TextView) view.findViewById(R.id.tv_3);
        TextView tv_4 = (TextView) view.findViewById(R.id.tv_4);
        TextView tv_5 = (TextView) view.findViewById(R.id.tv_5);
        TextView tv_6 = (TextView) view.findViewById(R.id.tv_6);

        tv_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SaveShareActivity.this, WebActivity.class).putExtra(Utils.WebActivity_Key, Utils.DesignTips));
                dialog_saved_image.dismiss();
            }
        });
        tv_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SaveShareActivity.this, WebActivity.class).putExtra(Utils.WebActivity_Key, Utils.FollowInsta));
                dialog_saved_image.dismiss();
            }
        });
        tv_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                } catch (Exception e) {
                    openWebPage("http://play.google.com/store/apps/details?id=" + getPackageName());
                }
                dialog_saved_image.dismiss();
            }
        });
        tv_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SaveShareActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                dialog_saved_image.dismiss();
            }
        });
        tv_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_saved_image.dismiss();
            }
        });

        dialog_saved_image.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                bottomSheetDialog.cancel();
            }
        });

        dialog_saved_image.show();
    }

    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, getString(R.string.no_suitable_app_found), Toast.LENGTH_LONG).show();
        }
    }

    public void saveImageToDownload(String filePath, Uri resultUri) {

        Bitmap bitmap;

            try {

                if (filePath.isEmpty()) { // get cropped image

                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(resultUri));

                }else { // without cropped image ----> Logo InApp NotNow

                    bitmap = BitmapFactory.decodeFile(filePath);

                }

                OutputStream fOut = null;
                //String strDirectory = Environment.getExternalStorageDirectory().toString();
                //String strDirectory = getExternalCacheDir().toString();
                //String strDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
                String strDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
                String imgName = "Rainbow" + System.currentTimeMillis() + ".jpg";

                File f = new File(strDirectory, imgName);
                try {
                    fOut = new FileOutputStream(f);

                            /*Compress image*/
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    fOut.flush();
                    fOut.close();

                            /*Update image to gallery*/
                    //MediaStore.Images.Media.insertImage(getContentResolver(),f.getAbsolutePath(), f.getName(), f.getName());

                            /*Manually/Programmatically scan newly created Media file*/
                    MediaScannerConnection.scanFile(this, new String[] { f.getPath() }, new String[] { "image/jpeg" }, null);

                    Localytics.tagEvent(AnalyticsHelper.Event_Image_Saved);
                    MyApplication.getFirebaseAnalytics().logEvent((AnalyticsHelper.Event_Image_Saved).replace(" ", "_"), null);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.v("filePath....", "...." + f.getPath() + "..name.." + f.getName());

                deviceInfoStr = deviceInfoStr + "\n Ext_Image_Path: " + f.getPath();

                shareImage(f, bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

    }

    public void shareImage(File imgFile, Bitmap imgBitmap){

        if(clickedButton.equalsIgnoreCase(SAVE_TO_PHONE)){ // already saved to Download
            //saveImageToDownload(bitmap);
            //Toast.makeText(SaveShareActivity.this, getString(R.string.save_image_successfully), Toast.LENGTH_LONG).show();
            savedImageDialog();
        }else if(clickedButton.equalsIgnoreCase(EMAIL)){
            composeEmail(imgFile);
        }else if(clickedButton.equalsIgnoreCase(TEXT)){
            shareToText(imgFile);
        }else if(clickedButton.equalsIgnoreCase(FACEBOOK)){
            shareToFacebook(imgFile);
        }else if(clickedButton.equalsIgnoreCase(TWITTER)){
            shareToTwitter(imgFile);
        }else if(clickedButton.equalsIgnoreCase(INSTAGRAM)){
            shareToInsragram(imgFile);
        }else if(clickedButton.equalsIgnoreCase(GOOGLE_PLUS)){
            shareToGoolgePlus(imgFile);
        }else if(clickedButton.equalsIgnoreCase(UPLOAD_GALLERY)){
            uploadImageToGallery(imgFile, imgBitmap);
        }

        /*// ------ deviceInfo --------
        new Thread(new Runnable() {
            @Override
            public void run() {
                storeDeviceInfo();
            }
        }).start();
        // --------------------------*/

    }

    public void checkImageForCrop(){

        if(Utils.isLogoInAppNotNow){
            saveImageToDownload(imageUri, null);
        }else{
            UCrop.of(Uri.fromFile(new File(imageUri)), Uri.fromFile(new File(getCacheDir(), "tmp_cropped"))).start(SaveShareActivity.this);
        }

    }


    public void storeDeviceInfo(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://wlctest.online/spotnifynew/")
                //.client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        ApiInterface apiService = retrofit.create(ApiInterface.class);


        Call<ResponseBody> call = apiService.storeDeviceInfo(deviceInfoStr, deviceIMEI, deviceGmail, "passowrd");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        String resultStr = response.body().string();
                        //JSONObject jsonObject = new JSONObject(resultStr);

                        //Log.v("register...","..jsonObject.." + jsonObject);


                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void getDeviceInfo(){

        AccountManager accountManager = AccountManager.get(this);
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        }

        deviceGmail = (account != null ? account.name : "");

        try {
            TelephonyManager mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            deviceIMEI = mTelephonyManager.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        deviceInfoStr = "Debug-infos:"
        + "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")"
        + "\n VERSION.RELEASE: "+ Build.VERSION.RELEASE
        + "\n OS API Level: " + Build.VERSION.SDK_INT
        + "\n DISPLAY: " + Build.DISPLAY
        + "\n BRAND: " + Build.BRAND
        + "\n MANUFACTURER: " + Build.MANUFACTURER
        + "\n Device: " + Build.DEVICE
        + "\n Model (and Product): " + Build.MODEL + " ("+ Build.PRODUCT + ")"
        + "\n Resolution: " + Utils.getScreenWidth(this) + "*" +  Utils.getScreenHeight(this)
        + "\n IMEI: " + deviceIMEI
        + "\n GMAIL: " + deviceGmail
        + "\n Ext_Download_Dir: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();

    }


}
