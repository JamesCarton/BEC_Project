package com.rainbowloveapp.app.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rainbowloveapp.app.MyApplication;
import com.rainbowloveapp.app.R;
import com.rainbowloveapp.app.utils.AnalyticsHelper;
import com.rainbowloveapp.app.utils.LetterSpacingTextView;
import com.rainbowloveapp.app.utils.Utils;
import com.localytics.android.Localytics;
import com.soundcloud.android.crop.Crop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 *  on 10/5/17.
 */

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CAMERA_REQUEST = 100;

    private ImageView img_home;
    private LetterSpacingTextView tv_our_cards;
    private LetterSpacingTextView tv_your_photos;

    String img_path = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        setUpViews();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onClick(View view) {
        if(view == tv_our_cards){
            startActivity(new Intent(this, CategoriesActivity.class));
        }else if(view == tv_your_photos){
            selectImage();
        }else if(view == findViewById(R.id.tv_home_filter_tips)){
            startActivity(new Intent(this, WebActivity.class).putExtra(Utils.WebActivity_Key, Utils.DesignTips));
        }else if(view == findViewById(R.id.tv_app_support_home)){
            appSupportDialog();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getFirebaseAnalytics().setCurrentScreen(this, AnalyticsHelper.Screen_Home, null);
    }

    private void setUpViews() {
        img_home = (ImageView)findViewById( R.id.img_home );
        tv_our_cards = (LetterSpacingTextView)findViewById( R.id.tv_our_cards );
        tv_your_photos = (LetterSpacingTextView)findViewById( R.id.tv_your_photos );

        //Log.v("text..size..","..." + tv_our_cards.getTextSize());

        tv_our_cards.setOnClickListener(this);
        tv_your_photos.setOnClickListener(this);

        findViewById(R.id.tv_home_filter_tips).setOnClickListener(this);
        findViewById(R.id.tv_app_support_home).setOnClickListener(this);


        //Glide.with(this).load(R.drawable.bg_register).fitCenter().into(img_home);


        Utils.loadImageGlide(this, R.drawable.bg_home, img_home);

    }


    public void selectImage() {

        final CharSequence[] options = { getString(R.string.album), getString(R.string.camera), getString(R.string.cancel) };


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getString(R.string.select_photo));

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals(getString(R.string.camera))) {

                    img_path = android.os.Environment.getExternalStorageDirectory() + "/img_temp.jpg";
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "img_temp.jpg");
                    //Uri photoURI = FileProvider.getUriForFile(this, "com.example.android.fileprovider", photoFile);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, CAMERA_REQUEST);

                } else if (options[item].equals(getString(R.string.album))) {
                    Crop.pickImage(HomeActivity.this);
                    //Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    //startActivityForResult(intent, RESULT_LOAD_IMAGE);
                } else if (options[item].equals(getString(R.string.cancel))) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.i("requestCode..","..requestCode.."+requestCode);
//        Log.i("resultCode..","..resultCode.."+resultCode);
//        Log.i("data..","..data.."+ (data!=null) );
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(data.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            beginCrop(Uri.fromFile(new File(img_path)));
        }

    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            try {
                Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(Crop.getOutput(result)));

                // -----------
//                Bitmap imageWithBG = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),bitmap.getConfig());  // Create another image the same size
//                imageWithBG.eraseColor(Color.WHITE);  // set its background to white, or whatever color you want
//                Canvas canvas = new Canvas(imageWithBG);  // create a canvas to draw on the new image
//                canvas.drawBitmap(bitmap, 0f, 0f, null); // draw old image on the background
//                bitmap.recycle();  // clear out old image
                // -----------

                Utils.isCardSelected = false;

                Utils.ImageMain = bitmap;
                startActivity(new Intent(this, MainActivity.class));

                Localytics.tagEvent(AnalyticsHelper.Event_Photo_Selected);
                MyApplication.getFirebaseAnalytics().logEvent((AnalyticsHelper.Event_Photo_Selected).replace(" ","_"), null);

                //img_profilepic_signup.setImageBitmap(Utils.getRoundedCornerBitmap(bitmap, 10));
                //img_profilepic_signup.setScaleType(ImageView.ScaleType.FIT_XY);
                //Log.i("bitmap size...","..before compress.." + BitmapCompat.getAllocationByteCount(bitmap));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (resultCode == Crop.RESULT_ERROR) {
            //Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void appSupportDialog(){

        View view = getLayoutInflater().inflate(R.layout.app_support_dialog_layout, null);
        final Dialog dialog_app_support = new Dialog(this);
        int width = (int) (Utils.getScreenWidth(this) * 0.8);
        dialog_app_support.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_app_support.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog_app_support.setContentView(view);
        dialog_app_support.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog_app_support.setCancelable(true);

        TextView tv_turn_on_wifi = (TextView) view.findViewById(R.id.tv_turn_on_wifi);
        TextView tv_get_tips = (TextView) view.findViewById(R.id.tv_get_tips);
        TextView tv_check_for_update_support = (TextView) view.findViewById(R.id.tv_check_for_update_support);
        TextView tv_contact_us = (TextView) view.findViewById(R.id.tv_contact_us);
        TextView tv_connect_wifi = (TextView) view.findViewById(R.id.tv_connect_wifi);
        TextView tv_return_app = (TextView) view.findViewById(R.id.tv_return_app);


        Typeface custFont = Typeface.createFromAsset(getAssets(), "fonts/"+"Quicksand_Book.otf");

        tv_turn_on_wifi.setTypeface(custFont);
        tv_get_tips.setTypeface(custFont);
        tv_check_for_update_support.setTypeface(custFont);
        tv_contact_us.setTypeface(custFont);
        tv_connect_wifi.setTypeface(custFont);
        tv_return_app.setTypeface(custFont);

        tv_get_tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_app_support.dismiss();
                startActivity(new Intent(HomeActivity.this, WebActivity.class).putExtra(Utils.WebActivity_Key, Utils.GetTips));
            }
        });
        tv_check_for_update_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_app_support.dismiss();
                Utils.openAppOnPlayStore(HomeActivity.this);
            }
        });
        tv_contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_app_support.dismiss();
                composeEmail(getResources().getStringArray(R.array.hello_rainbowlove_email), "App Support (Rainbow Love Android)", "");
            }
        });
        tv_connect_wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_app_support.dismiss();
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
        tv_return_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_app_support.dismiss();
            }
        });

        dialog_app_support.show();
    }

    public void composeEmail(String[] addresses, String subject, String content) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        //intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(content));
        /*intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(new StringBuilder()
                .append("<p><b>Some Content</b></p>")
                .append("<img src='http://luvisallaroundme.com/admin/images/techsupport-logo.png'/>")
                .toString()));*/

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, getString(R.string.no_suitable_app_found), Toast.LENGTH_LONG).show();
        }
    }

}
