package com.rainbowloveapp.app.ui;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.vending.billing.IInAppBillingService;
import com.rainbowloveapp.app.MyApplication;
import com.rainbowloveapp.app.R;
import com.rainbowloveapp.app.database.DatabaseHandler;
import com.rainbowloveapp.app.model.ArtInAppLocalModel;
import com.rainbowloveapp.app.utils.AnalyticsHelper;
import com.rainbowloveapp.app.utils.Utils;
import com.localytics.android.Localytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *  on 2/8/17.
 */

public class InAppPurchaseActivity extends AppCompatActivity {

    IInAppBillingService mService;

    ServiceConnection mServiceConn;

    /*ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name,IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
        }
    };*/

    // ProductID
    private String productID = "android.test.purchased";
    //private final String productID = "com.katemitchell.loveis.filter.rainbow.1";

    String type_inapp;
    String inAppId;
    String catId_inapp;

    DatabaseHandler databaseHandler;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        databaseHandler = new DatabaseHandler(this);

        if (getIntent().getExtras() != null) {
            type_inapp = getIntent().getExtras().getString(Utils.Type_Inapp_key);
            inAppId = getIntent().getExtras().getString(Utils.InappId_key);
            catId_inapp = getIntent().getExtras().getString(Utils.CatId_Inapp_key);

            //productID = inAppId;
        }

        mServiceConn = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mService = null;
            }

            @Override
            public void onServiceConnected(ComponentName name,IBinder service) {
                mService = IInAppBillingService.Stub.asInterface(service);
                //buy();
                inAppDialog();
            }
        };


        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            unbindService(mServiceConn);
        }

        if(dialog != null){
            if(dialog.isShowing()){
                dialog.dismiss();
            }
        }
    }

    public void buy(String inAppId_str) {

        ArrayList<String> skuList = new ArrayList<String>();
        //skuList.add(productID);
        skuList.add(inAppId_str);
        Bundle querySkus = new Bundle();
        querySkus.putStringArrayList("ITEM_ID_LIST", skuList);

        Bundle skuDetails = null;
        try {
            Log.v("mService....","...." + (mService == null));
            skuDetails = mService.getSkuDetails(3, getPackageName(), "inapp", querySkus);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        int response = skuDetails.getInt("RESPONSE_CODE");
        //Log.i("response...","....response..." + response);
        if (response == 0) {
            ArrayList<String> responseList = skuDetails.getStringArrayList("DETAILS_LIST");

            for (String thisResponse : responseList) {
                try {
                    JSONObject object = new JSONObject(thisResponse);
                    String sku = object.getString("productId");
                    String price = object.getString("price");

                    //Log.i("response..","....object..." + object.toString());
                    //Log.i("response..","....sku..." + sku);
                    Log.i("response..","....price..." + price);
                    if (sku.equals(inAppId_str)) {
                        Bundle buyIntentBundle = mService.getBuyIntent(3, getPackageName(),
                                sku, "inapp", "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
                        Log.i("response..","....buyIntentBundle..code.." + buyIntentBundle.getInt("RESPONSE_CODE"));
                        if(buyIntentBundle.getInt("RESPONSE_CODE") == 0) // new purchase
                        {   //inAppDialog();
                            PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
                            startIntentSenderForResult(pendingIntent.getIntentSender(),
                                    1001, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
                                    Integer.valueOf(0));
                        }
                        else if(buyIntentBundle.getInt("RESPONSE_CODE") == 7) // already purchase, restore
                        {
                            //consume();
                            handleInAppSuccessResult(sku);
                        }
                    }

                }catch (Exception e) {

                }
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001) {
            int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");
            Log.i("resultCode..","....resultCode..." + resultCode);
            if (resultCode == RESULT_OK) {
                try {
                    JSONObject jo = new JSONObject(purchaseData);
                    String sku = jo.getString("productId");
                    //Log.i("RESULT_OK","You have bought the " + sku + ". Excellent choice, adventurer!");
                    //consume();
                    handleInAppSuccessResult(sku);

//                    FlurryAgent.logEvent(AnalyticsHelper.Event_Purchased_Successfully);
//                    Localytics.tagEvent(AnalyticsHelper.Event_Purchased_Successfully);
//                    MyApplication.getFirebaseAnalytics().logEvent((AnalyticsHelper.Event_Purchased_Successfully).replace(" ","_"), null);
                }
                catch (JSONException e) {
                    Log.i("RESULT_OK","Failed to parse purchase data.");
                    e.printStackTrace();
                }
            }
        }
    }

    public void consume() {

        Bundle ownedItems = null;
        try {
            ownedItems = mService.getPurchases(3, getPackageName(), "inapp", null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        int response = ownedItems.getInt("RESPONSE_CODE");
        if (response == 0)
        {
            ArrayList<String> ownedSkus = ownedItems.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
            ArrayList<String> purchaseDataList = ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
            //ArrayList<String> signatureList = ownedItems.getStringArrayList("INAPP_DATA_SIGNATURE");
            //String continuationToken = ownedItems.getString("INAPP_CONTINUATION_TOKEN");
            for (int i = 0; i < purchaseDataList.size(); ++i) {
                try {
                    String purchaseData = purchaseDataList.get(i);
                    //Log.i("consume..","....purchaseData..." + purchaseData);
                    JSONObject jo = new JSONObject(purchaseData);
                    final String token = jo.getString("purchaseToken");
                    String sku = null;
                    if (ownedSkus != null)
                        sku = ownedSkus.get(i);
                    try {
                        mService.consumePurchase(3, getPackageName(), token);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    public void inAppDialog(){

        View view = getLayoutInflater().inflate(R.layout.inapp_dialog_layout, null);
        dialog = new Dialog(this);
        int width = (int) (Utils.getScreenWidth(this) * 0.8);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(view);
        dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                InAppPurchaseActivity.this.finish();
            }
        });

        final TextView tv_1 = (TextView) view.findViewById(R.id.tv_1);
        final TextView tv_2 = (TextView) view.findViewById(R.id.tv_2);
        TextView tv_3 = (TextView) view.findViewById(R.id.tv_3);
        TextView tv_4 = (TextView) view.findViewById(R.id.tv_4);
        TextView tv_5 = (TextView) view.findViewById(R.id.tv_5);

        tv_3.setText(getString(R.string.upgrade_to_deluxe));
        tv_4.setText(getString(R.string.restore_past_purchase));
        tv_5.setText(getString(R.string.not_now));

        if(type_inapp.equalsIgnoreCase(Utils.Filter)){
            String filterName = "";
            if(inAppId.contains("rainbow")){
                filterName = "rainbow";
            }else if(inAppId.contains("moon")){
                filterName = "moon";
            }else if(inAppId.contains("dreamy")){
                filterName = "dreamy";
            }else if(inAppId.contains("floral")){
                filterName = "earth";
            }else if(inAppId.contains("dusk")){
                filterName = "dusk";
            }

            tv_1.setText("get 20 " + filterName + " filters " + getPriceFromInAppId(inAppId));
            tv_2.setText("get all 80 " + " filters " + getPriceFromInAppId(getString(R.string.filter_unlockall_inapp_id)));

            // set InAppId in Tag
            tv_1.setTag(inAppId);
            tv_2.setTag(getString(R.string.filter_unlockall_inapp_id));

        }else if(type_inapp.equalsIgnoreCase(Utils.Art)){
            tv_1.setText(getString(R.string.get_this_art_category) + getPriceFromInAppId(inAppId));
            tv_2.setText(getString(R.string.get_all_art_categories) + getPriceFromInAppId(getString(R.string.art_unlockall_inapp_id)));

            tv_1.setTag(inAppId);
            tv_2.setTag(getString(R.string.art_unlockall_inapp_id));

        }else if(type_inapp.equalsIgnoreCase(Utils.AddLayer)){
            tv_2.setVisibility(View.GONE);
            tv_2.setTag("");

            tv_1.setText(getString(R.string.get_this_feature) + getPriceFromInAppId(inAppId));

            tv_1.setTag(inAppId);

        }else if(type_inapp.equalsIgnoreCase(Utils.Logo)){
            tv_2.setVisibility(View.GONE);
            tv_3.setVisibility(View.GONE);
            tv_2.setTag("");

            tv_1.setText(getString(R.string.remove_logo) + getPriceFromInAppId(inAppId));

            tv_1.setTag(inAppId);
        }


        tv_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.v("Tag....1..","....." + view.getTag());
                buy(view.getTag().toString());
            }
        });
        tv_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.v("Tag....2..","....." + view.getTag());
                buy(view.getTag().toString());
            }
        });
        tv_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("Tag....3..",".....");
            }
        });
        tv_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.v("Tag....4..","..tv_1..." + tv_1.getTag() + "....tv_2..." + tv_2.getTag());
                //buy(tv_1.getTag().toString());
                restorePurchase(tv_1.getTag().toString(), tv_2.getTag().toString());
            }
        });
        tv_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type_inapp.equalsIgnoreCase(Utils.Logo)){
                    Utils.isLogoInAppNotNow = true;
                    MainActivity.saveShareImage(InAppPurchaseActivity.this);
                }
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    public void handleInAppSuccessResult(String inAppId_purchased){

        if(type_inapp.equalsIgnoreCase(Utils.Filter)){

            Map<String, String> articleParams = new HashMap<String, String>();
            articleParams.put(AnalyticsHelper.Event_Filter_Unlock, inAppId_purchased);

            Localytics.tagEvent(AnalyticsHelper.Event_Filter_Pack, articleParams);
            MyApplication.getFirebaseAnalytics().logEvent((AnalyticsHelper.Event_Filter_Pack).replace(" ","_"), Utils.hashMapToBundle(articleParams));

            if(inAppId_purchased.equalsIgnoreCase(getString(R.string.filter_unlockall_inapp_id))){ // unlock all Filters
                Utils.setStringPreference(this, Utils.UnlockallFilter_key, Utils.Purchased);
            }else if(inAppId_purchased.contains("lite")){
                Utils.setStringPreference(this, Utils.LiteFilterInApp_key, Utils.Purchased);
            }else if(inAppId_purchased.contains("rainbow")){
                Utils.setStringPreference(this, Utils.RainbowFilterInApp_key, Utils.Purchased);
            }else if(inAppId_purchased.contains("moon")){
                Utils.setStringPreference(this, Utils.MoonFilterInApp_key, Utils.Purchased);
            }else if(inAppId_purchased.contains("dreamy")){
                Utils.setStringPreference(this, Utils.DreamyFilterInApp_key, Utils.Purchased);
            }else if(inAppId_purchased.contains("floral")){
                Utils.setStringPreference(this, Utils.FloralFilterInApp_key, Utils.Purchased);
            }else if(inAppId_purchased.contains("dusk")){
                Utils.setStringPreference(this, Utils.DuskFilterInApp_key, Utils.Purchased);
            }
        }else if(type_inapp.equalsIgnoreCase(Utils.Art)){ // for Art
            if(inAppId_purchased.equalsIgnoreCase(getString(R.string.art_unlockall_inapp_id))){ // unlock all Atrs
                Utils.setStringPreference(this, Utils.UnlockallArt_key, Utils.Purchased);
                Localytics.tagEvent(AnalyticsHelper.Event_Photo_Art);
                MyApplication.getFirebaseAnalytics().logEvent((AnalyticsHelper.Event_Photo_Art).replace(" ","_"), null);
            }else{
                ArtInAppLocalModel artInAppLocalModel = new ArtInAppLocalModel();
                artInAppLocalModel.setCatId(catId_inapp);

                databaseHandler.insertArtInAppTable(artInAppLocalModel);

                Map<String, String> articleParams = new HashMap<String, String>();
                articleParams.put(AnalyticsHelper.Event_Art_Unlock, inAppId_purchased);

                Localytics.tagEvent(AnalyticsHelper.Event_Art_Category, articleParams);
                MyApplication.getFirebaseAnalytics().logEvent((AnalyticsHelper.Event_Art_Category).replace(" ","_"), Utils.hashMapToBundle(articleParams));
            }

        }else if(type_inapp.equalsIgnoreCase(Utils.AddLayer)){
            Utils.setStringPreference(this, Utils.AddLayerInApp_key, Utils.Purchased);
        }else if(type_inapp.equalsIgnoreCase(Utils.Logo)){
            Utils.setStringPreference(this, Utils.LogoInApp_key, Utils.Purchased);
            Localytics.tagEvent(AnalyticsHelper.Event_Remove_WaterMark);
            MyApplication.getFirebaseAnalytics().logEvent((AnalyticsHelper.Event_Remove_WaterMark).replace(" ","_"), null);
        }


        try {
            this.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getPriceFromInAppId(String inAppId){

        String price_str = "";

        ArrayList<String> skuList = new ArrayList<String>();
        skuList.add(inAppId);
        Bundle querySkus = new Bundle();
        querySkus.putStringArrayList("ITEM_ID_LIST", skuList);

        Bundle skuDetails = null;
        try {
            Log.v("mService....","...." + (mService == null));
            skuDetails = mService.getSkuDetails(3, getPackageName(), "inapp", querySkus);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        int response = skuDetails.getInt("RESPONSE_CODE");
        //Log.i("response...","....response..." + response);
        if (response == 0) {
            ArrayList<String> responseList = skuDetails.getStringArrayList("DETAILS_LIST");

            for (String thisResponse : responseList) {
                try {
                    JSONObject object = new JSONObject(thisResponse);
                    String sku = object.getString("productId");
                    String price = object.getString("price");

                    //Log.i("response..","....object..." + object.toString());
                    //Log.i("response..","....sku..." + sku);
                    Log.i("response..","....price..." + price);

                    price_str = price;

                    return price_str;

                }catch (Exception e) {

                }
            }
        }

        return price_str;
    }

    public void restorePurchase(String inAppId_1, String inAppId_2){
        buy(inAppId_1);
        buy(inAppId_2);
    }

}
