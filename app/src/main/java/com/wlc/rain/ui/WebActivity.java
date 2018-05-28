package com.rainbowloveapp.app.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.rainbowloveapp.app.R;
import com.rainbowloveapp.app.network.ApiClient;
import com.rainbowloveapp.app.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 *  on 10/5/17.
 */

public class WebActivity extends AppCompatActivity {


    @BindView(R.id.tv_heading)
    TextView tvheading;
    @BindView(R.id.webview)
    WebView webview;

    ProgressDialog progressDialog;

    String url;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_activity);
        ButterKnife.bind(this);

        if(getIntent().getExtras().containsKey(Utils.WebActivity_Key)){
            getTitleURL(getIntent().getExtras().getString(Utils.WebActivity_Key));
        }

        tvheading.setText(title);

        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new MyWebViewClient());
        webview.loadUrl(url);

        progressDialog = new ProgressDialog(this);
        Utils.showProgressDialog(progressDialog, getString(R.string.loading));

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Utils.dismissProgressDialog(progressDialog);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @OnClick(R.id.header_frame_left_1)
    public void onViewClicked() {
        onBackPressed();
    }

    public void getTitleURL(String value){

        if(value.equalsIgnoreCase(Utils.PrivacyPolicy)){
            title = getString(R.string.privacy_policy);
            url = ApiClient.Privacy_Policy;
        }else if(value.equalsIgnoreCase(Utils.DesignTips)){
            title = getString(R.string.design_tips);
            url = ApiClient.Design_Tips;
        }else if(value.equalsIgnoreCase(Utils.FAQ)){
            title = getString(R.string.faq);
            url = ApiClient.FAQ;
        }else if(value.equalsIgnoreCase(Utils.EditOfWeek)){
            title = getString(R.string.edit_of_the_week);
            url = ApiClient.EDIT_OF_THE_WEEK;
        }else if(value.equalsIgnoreCase(Utils.FollowInsta)){
            title = getString(R.string.follow_on_instagram);
            url = ApiClient.Follow_Our_Instagram;
        }else if(value.equalsIgnoreCase(Utils.TermService)){
            title = getString(R.string.terms_of_service);
            url = ApiClient.Terms_of_Service;
        }else if(value.equalsIgnoreCase(Utils.FilterTips)){
            title = getString(R.string.filters_tips);
            url = ApiClient.Filters_Tips;
        }else if(value.equalsIgnoreCase(Utils.HomeFilterTips)){
            title = getString(R.string.filter_tips);
            url = ApiClient.HomeFilters_Tips;
        }else if(value.equalsIgnoreCase(Utils.GetTips)){
            title = getString(R.string.get_tips);
            url = ApiClient.Get_Tips;
        }else{
            title = "";
            url = ApiClient.Site;
        }
    }
}
