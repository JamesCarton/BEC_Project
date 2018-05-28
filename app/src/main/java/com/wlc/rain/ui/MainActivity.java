package com.rainbowloveapp.app.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.rainbowloveapp.app.MyApplication;
import com.rainbowloveapp.app.R;
import com.rainbowloveapp.app.adapter.ArtCategoryListAdapter;
import com.rainbowloveapp.app.adapter.ArtImageListAdapter;
import com.rainbowloveapp.app.adapter.ColorListAdapter;
import com.rainbowloveapp.app.adapter.FilterListAdapter;
import com.rainbowloveapp.app.adapter.FontListAdapter;
import com.rainbowloveapp.app.database.DatabaseHandler;
import com.rainbowloveapp.app.filter.ColorFilterGenerator;
import com.rainbowloveapp.app.model.ArtImageLocalModel;
import com.rainbowloveapp.app.model.ArtInAppLocalModel;
import com.rainbowloveapp.app.model.ArtLocalModel;
import com.rainbowloveapp.app.model.EditImageModel;
import com.rainbowloveapp.app.model.FilterModel;
import com.rainbowloveapp.app.model.RecentLocalModel;
import com.rainbowloveapp.app.model.TextArtEditorModel;
import com.rainbowloveapp.app.network.ApiClient;
import com.rainbowloveapp.app.network.ApiInterface;
import com.rainbowloveapp.app.network.DownloadImage;
import com.rainbowloveapp.app.utils.AnalyticsHelper;
import com.rainbowloveapp.app.utils.MyTouchListener;
import com.rainbowloveapp.app.utils.Utils;
import com.localytics.android.Localytics;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, RadioGroup.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {

    private static final int CAMERA_REQUEST = 100;

    private static final int CAMERA_REQUEST_ADD_PHOTO = 101;

    private static final int ALBUM_ADD_PHOTO = 201;


    @BindView(R.id.main_tool_layout)
    Toolbar mainToolLayout;
    @BindView(R.id.img_quick_tips)
    ImageView imgQuickTips;
    @BindView(R.id.linear_filter_save_exit)
    LinearLayout linearFilterSaveExit;
    @BindView(R.id.img_main_activity)
    ImageView imgMainActivity;
    @BindView(R.id.img_app_logo)
    ImageView img_app_logo;
    @BindView(R.id.linear_add_edit_main)
    LinearLayout linearAddEditMain;
    @BindView(R.id.img_grid_on_image)
    ImageView imgGridOnImage;
    @BindView(R.id.recycler_filter)
    RecyclerView recyclerFilter;
    @BindView(R.id.linear_recycler_filter)
    RelativeLayout linearRecyclerFilter;
    @BindView(R.id.img_lock_on_image)
    public ImageView img_lock_on_image;


    public static FrameLayout frame_middle;
    public static PercentRelativeLayout percent_relative_save;

    // Text Editor ID's
    @BindView(R.id.linear_text_editor)
    LinearLayout linearTextEditor;
    @BindView(R.id.recycler_font)
    RecyclerView recyclerFont;
    @BindView(R.id.linear_recycler_font)
    LinearLayout linearRecyclerFont;
    @BindView(R.id.seekbar_text_size)
    SeekBar seekbarTextSize;
    @BindView(R.id.linear_text_size)
    LinearLayout linearTextSize;
    @BindView(R.id.img_font_text_editor)
    ImageView imgFontTextEditor;
    @BindView(R.id.img_size_text_editor)
    ImageView imgSizeTextEditor;
    @BindView(R.id.linear_text_aling)
    LinearLayout linearTextAling;
    @BindView(R.id.img_aling_text_editor)
    ImageView imgAlingTextEditor;
    @BindView(R.id.img_left_aling)
    ImageView imgLeftAling;
    @BindView(R.id.img_center_aling)
    ImageView imgCenterAling;
    @BindView(R.id.img_right_aling)
    ImageView imgRightAling;
    @BindView(R.id.img_vertical_aling)
    ImageView imgVerticalAling;
    @BindView(R.id.seekbar_space_aling)
    SeekBar seekbarSpaceAling;
    @BindView(R.id.img_c_space_aling)
    ImageView imgCSpaceAling;
    @BindView(R.id.img_l_space_aling)
    ImageView imgLSpaceAling;
    @BindView(R.id.seekbar_text_trans)
    SeekBar seekbarTextTrans;
    @BindView(R.id.linear_text_trans)
    LinearLayout linearTextTrans;
    @BindView(R.id.img_trans_text_editor)
    ImageView imgTransTextEditor;
    @BindView(R.id.img_type_text_editor)
    ImageView imgTypeTextEditor;
    @BindView(R.id.img_color_text_editor)
    ImageView imgColorTextEditor;
    @BindView(R.id.img_rotate_text_editor)
    ImageView imgRotateTextEditor;
    @BindView(R.id.img_layer_text_editor)
    ImageView imgLayerTextEditor;
    @BindView(R.id.seekbar_manual_rotate)
    SeekBar seekbarManualRotate;
    @BindView(R.id.img_rotete_main)
    ImageView imgRoteteMain;
    @BindView(R.id.linear_text_rotate)
    LinearLayout linearTextRotate;
    @BindView(R.id.linear_text_layer)
    LinearLayout linearTextLayer;
    @BindView(R.id.recycler_text_color)
    RecyclerView recyclerTextColor;
    @BindView(R.id.linear_recycler_text_color)
    LinearLayout linearRecyclerTextColor;
    @BindView(R.id.recycler_highlight_color)
    RecyclerView recyclerHighlightColor;
    @BindView(R.id.radiogroup_color)
    RadioGroup radiogroupColor;
    @BindView(R.id.radio_text_color)
    RadioButton radioTextColor;
    @BindView(R.id.radio_highlight_color)
    RadioButton radioHighlightColor;
    @BindView(R.id.scrollview)
    ScrollView scrollview;
    @BindView(R.id.recycler_art_categories)
    RecyclerView recyclerArtCategories;
    @BindView(R.id.relative_art_category)
    RelativeLayout relativeArtCategory;
    @BindView(R.id.recycler_art_image)
    RecyclerView recyclerArtImage;
    @BindView(R.id.relative_art_image)
    RelativeLayout relativeArtImage;
    @BindView(R.id.relative_recycler_art)
    RelativeLayout relativeRecyclerArt;
//    @BindView(R.id.frame_container)
//    FrameLayout frame_container;
    @BindView(R.id.edit_image_tool_layout)
    Toolbar editImageToolLayout;
    @BindView(R.id.text_editor_tool_layout)
    Toolbar textEditorToolLayout;

    @BindView(R.id.seekbar_edit_image)
    SeekBar seekbarEditImage;
    @BindView(R.id.img_brightness_edit_image)
    ImageView imgBrightnessEditImage;
    @BindView(R.id.img_contrasst_edit_image)
    ImageView imgContrasstEditImage;
    @BindView(R.id.img_saturation_edit_image)
    ImageView imgSaturationEditImage;
    @BindView(R.id.img_bw_edit_image)
    ImageView imgBwEditImage;
    @BindView(R.id.img_rotate_edit_image)
    ImageView imgRotateEditImage;
    @BindView(R.id.img_change_bg_edit_image)
    ImageView imgChangeBgEditImage;
    @BindView(R.id.linear_edit_image)
    LinearLayout linearEditImage;
    @BindView(R.id.linear_main_filter)
    LinearLayout linearMainFilter;
    @BindView(R.id.linear_edit_filter)
    LinearLayout linearEditFilter;
    @BindView(R.id.linear_filter_intensity)
    LinearLayout linearFilterIntensity;
    @BindView(R.id.seekbar_filter_intensity)
    SeekBar seekbarFilterIntensity;


    List<FilterModel> filterList = new ArrayList<>();

    FilterListAdapter filterListAdapter;
   // public static List<FilterModel> filterListTemp = new ArrayList<>();
    //public static List<FilterModel> filterListMain = new ArrayList<>();

    //public static List<Integer> savedFilterIndexList;
    public static boolean isTempSelectFilter;
    public static int lastSelectFilterPosition = 0;

    String[] colorsArray;

    String[] filterInappIdArray;

    public int currentFontPosition = -1;

    FontListAdapter fontListAdapter;

    Gson gson;

    DatabaseHandler databaseHandler;

    ArtCategoryListAdapter artCategoryListAdapter;
    ArtImageListAdapter artImageListAdapter;

    EditImageModel editImageModel_main;

    EditImageModel editImageModel_tmp;

    String img_path = "";

    public static boolean From_BG_Change;

    List<TextArtEditorModel> textArtEditorList = new ArrayList<>();

    String lastSelectTagIdTextArt = "";

    FrameLayout frame_container;

    public static int screenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        databaseHandler = new DatabaseHandler(this);

        From_BG_Change = false;

        colorsArray = getResources().getStringArray(R.array.color_list_array);
        filterInappIdArray = getResources().getStringArray(R.array.filter_inapp_id_array);

        screenWidth = Utils.getScreenWidth(this);

        frame_middle = (FrameLayout) findViewById(R.id.frame_middle);
        percent_relative_save = (PercentRelativeLayout) findViewById(R.id.percent_relative_save);

        percent_relative_save.setPadding(0, Utils.getSizefromDp(this, 1), 0, Utils.getSizefromDp(this, 1));

        frame_container = (FrameLayout) findViewById(R.id.frame_container);

        //int image_id = Utils.getDrawableResIDFromName(this, "rainbow_filter_2.png");

        //Bitmap filterBitmap = BitmapFactory.decodeResource(getResources(), image_id);

        // -------------------------------
        Utils.ImageCopy = Utils.ImageMain.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap result = Utils.ImageMain.copy(Bitmap.Config.ARGB_8888, true);

//        Paint p = new Paint();
//        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.OVERLAY));//
//        p.setShader(new BitmapShader(Bitmap.createScaledBitmap(filterBitmap, Utils.ImageMain.getWidth(), Utils.ImageMain.getHeight(), false), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
//
//        Canvas c = new Canvas();
//        c.setBitmap(result);
//        c.drawBitmap(Utils.ImageMain, 0, 0, null);
//        c.drawRect(0, 0, Utils.ImageMain.getWidth(), Utils.ImageMain.getHeight(), p);
        // -------------------------------

        imgMainActivity.setImageBitmap(result);

        ViewTreeObserver viewTreeObserver = percent_relative_save.getViewTreeObserver();
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                percent_relative_save.getViewTreeObserver().removeOnPreDrawListener(this);
                //Log.v("percent...","...width..." + percent_relative_save.getMeasuredWidth() + "...height.." + percent_relative_save.getMeasuredHeight());
                FrameLayout.LayoutParams imgGridParam = new FrameLayout.LayoutParams(percent_relative_save.getMeasuredWidth(), percent_relative_save.getMeasuredHeight());
                imgGridOnImage.setLayoutParams(imgGridParam);
                return true;
            }
        });



        recyclerFilter.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        recyclerFilter.setHasFixedSize(true);
        recyclerFilter.getItemAnimator().setChangeDuration(0);

        //savedFilterIndexList = new ArrayList<>();

        getFilterList();

        Utils.setFilterToPref(this, filterList);

        filterListAdapter = new FilterListAdapter(this, filterList, filterInappIdArray);
        recyclerFilter.setAdapter(filterListAdapter);
        /*RecyclerView.ItemAnimator animator = recyclerFilter.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }*/

        //filterListMain.clear();
        /*for(FilterModel filterModel : filterList){
            filterListMain.add(new FilterModel(filterModel));
        }*/
        /*for(int i = 0; i < filterList.size(); i++){
            FilterModel filterModel = filterList.get(i);
            filterListMain.add(new FilterModel(filterModel));
        }
        Log.v("filters...","create..main..size.." + filterListMain.size());*/

        recyclerFont.setLayoutManager(new LinearLayoutManager(this));
        fontListAdapter = new FontListAdapter(this);
        recyclerFont.setAdapter(fontListAdapter);


        recyclerTextColor.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        recyclerTextColor.setAdapter(new ColorListAdapter(this, colorsArray, true));

        recyclerHighlightColor.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        recyclerHighlightColor.setAdapter(new ColorListAdapter(this, colorsArray, false));

        recyclerArtCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        recyclerArtImage.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));

        recyclerArtCategories.setHasFixedSize(true);
        recyclerArtCategories.setItemViewCacheSize(20);
        recyclerArtCategories.setDrawingCacheEnabled(true);

        recyclerArtImage.setHasFixedSize(true);
        recyclerArtImage.setItemViewCacheSize(20);
        recyclerArtImage.setDrawingCacheEnabled(true);

        radiogroupColor.setOnCheckedChangeListener(this);

        imgTypeTextEditor.setOnTouchListener(this);
        imgFontTextEditor.setOnTouchListener(this);
        imgSizeTextEditor.setOnTouchListener(this);
        imgAlingTextEditor.setOnTouchListener(this);
        imgTransTextEditor.setOnTouchListener(this);
        imgColorTextEditor.setOnTouchListener(this);
        imgRotateTextEditor.setOnTouchListener(this);
        imgLayerTextEditor.setOnTouchListener(this);

        imgLeftAling.setOnTouchListener(this);
        imgCenterAling.setOnTouchListener(this);
        imgRightAling.setOnTouchListener(this);
        imgVerticalAling.setOnTouchListener(this);
        imgCSpaceAling.setOnTouchListener(this);
        imgLSpaceAling.setOnTouchListener(this);

        imgBrightnessEditImage.setOnTouchListener(this);
        imgContrasstEditImage.setOnTouchListener(this);
        imgSaturationEditImage.setOnTouchListener(this);

        imgCSpaceAling.setSelected(true);
        imgCenterAling.setSelected(true);


        seekbarTextSize.setOnSeekBarChangeListener(this);
        seekbarTextTrans.setOnSeekBarChangeListener(this);
        seekbarManualRotate.setOnSeekBarChangeListener(this);
        seekbarSpaceAling.setOnSeekBarChangeListener(this);

        scrollview.setEnabled(false);

        getArtCatogries();

        editImageModel_main = new EditImageModel(Utils.ColorFilterBaseValue, Utils.ColorFilterBaseValue, Utils.ColorFilterBaseValue, false, 0); // Initialize value

        editImageModel_tmp = new EditImageModel(Utils.ColorFilterBaseValue, Utils.ColorFilterBaseValue, Utils.ColorFilterBaseValue, false, 0);


        seekbarEditImage.setOnSeekBarChangeListener(this);
        seekbarFilterIntensity.setOnSeekBarChangeListener(this);

        //Log.v("text..size..","...MainActivty.." + ((TextView)findViewById(R.id.header_tv_left_1)).getTextSize());
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();

        MyApplication.getFirebaseAnalytics().setCurrentScreen(this, AnalyticsHelper.Screen_Image_Editer, null);

        //onViewClicked(findViewById(R.id.tv_add_art));
        //onViewClicked(findViewById(R.id.tv_close_art_category));

        //onViewClicked((findViewById(R.id.header_text_editor_frame_left_cancel)));

        findViewById(R.id.img_quick_tips).requestFocus();

        filterListAdapter.notifyDataSetChanged();

//        if(Utils.getStringPreference(this, Utils.LogoInApp_key, "").equalsIgnoreCase(Utils.Purchased)) {
        if(Utils.isCardSelected) {
            img_app_logo.setVisibility(View.GONE);
        }else {
            img_app_logo.setVisibility(View.VISIBLE);
            setWaterMarkBasedOnBgColor();
        }

        Log.v("result....","...onResume" + From_BG_Change + "...");
        if(From_BG_Change){
            From_BG_Change = false;//

            changeBGAllowDialog(Utils.ImageMain, true);

        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        changeBGPhoto(true);
    }

    @OnClick({R.id.header_frame_right_home_bg, R.id.header_frame_right_setting, R.id.header_frame_right_grid, R.id.img_quick_tips, R.id.tv_add_filters, R.id.tv_add_art,
            R.id.tv_add_text, R.id.tv_add_photo, R.id.tv_edit_image, R.id.tv_save, R.id.tv_filter_exit, R.id.tv_filter_edit, R.id.tv_filter_save,
            R.id.tv_addart_text_editor, R.id.img_type_text_editor, R.id.img_font_text_editor, R.id.img_aling_text_editor, R.id.img_color_text_editor,
            R.id.img_size_text_editor, R.id.img_rotate_text_editor, R.id.img_trans_text_editor, R.id.img_layer_text_editor, R.id.img_left_aling,
            R.id.img_center_aling, R.id.img_right_aling, R.id.img_vertical_aling, R.id.img_c_space_aling, R.id.img_l_space_aling, R.id.img_lr_layer,
            R.id.img_flip_layer, R.id.img_ud_layer, R.id.img_fwd_layer, R.id.img_back_layer, R.id.img_copy_layer, R.id.img_delete_layer, R.id.img_main_activity,
            R.id.tv_close_art_category, R.id.tv_back_art_image, R.id.header_edit_image_frame_left_cancel, R.id.header_edit_image_frame_right_save, R.id.img_brightness_edit_image, R.id.img_contrasst_edit_image,
            R.id.img_saturation_edit_image, R.id.img_bw_edit_image, R.id.img_rotate_edit_image, R.id.img_change_bg_edit_image, R.id.header_text_editor_frame_left_cancel,
            R.id.header_text_editor_frame_right_save, R.id.tv_filter_remove_all, R.id.tv_filter_edit_cancel, R.id.tv_filter_edit_save_all,
            R.id.tv_filter_edit_remove_all, R.id.tv_filter_save_share, R.id.img_lr_thine_filter, R.id.img_rotate_thine_filter, R.id.img_rotete_main, R.id.img_filter_recycler_arrowleft, R.id.img_filter_recycler_arrowright})
    public void onViewClicked(View view) { // R.id.tv_filter_edit_apply
        switch (view.getId()) {
            case R.id.header_frame_right_home_bg:
                changeBGPhoto(true);
                break;
            case R.id.header_frame_right_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.header_frame_right_grid:
                if (imgGridOnImage.getVisibility() == View.VISIBLE) {
                    imgGridOnImage.setVisibility(View.GONE);
                    Log.v("grid...", "..Iff.");
                } else {
                    imgGridOnImage.setVisibility(View.VISIBLE);
                    Log.v("grid...", "..else.");
                }
                break;
            case R.id.img_quick_tips:
                startActivity(new Intent(this, QuickTipsActivity.class));
                break;
            case R.id.tv_add_filters:
                hideAllHeader();
                linearFilterSaveExit.setVisibility(View.VISIBLE);


                filterApplyRemoveText();


                lastSelectFilterPosition = 0;
                for(int i = filterList.size()-1 ; i >= 0; i--){
                    FilterModel model = filterList.get(i);
                    if(model.isSaved()){
                        lastSelectFilterPosition = i;
                        //Log.v("remove..","...lastSelectInd.." + i);
                        break;
                    }
                }

                if(lastSelectFilterPosition == 0){
                    filterList.get(lastSelectFilterPosition).setSelected(true);
                    MainActivity.isTempSelectFilter = true;
                }

                filterListAdapter.notifyDataSetChanged();

                linearRecyclerFilter.setVisibility(View.VISIBLE);

                Localytics.tagEvent(AnalyticsHelper.Event_Filter_Previewed);
                MyApplication.getFirebaseAnalytics().logEvent((AnalyticsHelper.Event_Filter_Previewed).replace(" ","_"), null);

                break;
            case R.id.tv_add_art:
                relativeArtCategory.setVisibility(View.VISIBLE);
                relativeArtImage.setVisibility(View.GONE);
                relativeRecyclerArt.setVisibility(View.VISIBLE);

                linearTextEditor.setVisibility(View.GONE);

                setUpArtCategoriesAdapter();
                break;
            case R.id.tv_add_text:

                Localytics.tagEvent(AnalyticsHelper.Event_Text_Added);
                MyApplication.getFirebaseAnalytics().logEvent((AnalyticsHelper.Event_Text_Added).replace(" ","_"), null);

                hideAllHeader();
                textEditorToolLayout.setVisibility(View.VISIBLE);
                imgQuickTips.setVisibility(View.VISIBLE);

                relativeRecyclerArt.setVisibility(View.GONE);

                linearTextEditor.setVisibility(View.VISIBLE);


                linearAddEditMain.setVisibility(View.GONE);
                //hideAllChildTextEditor();
                //InputMethodManager imm_add_text = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm_add_text.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                addEdittext();

                onViewClicked(findViewById(R.id.img_type_text_editor));


                break;
            case R.id.tv_add_photo:

                if(Utils.getStringPreference(this, Utils.AddLayerInApp_key, "").equalsIgnoreCase(Utils.Purchased)){
                    AddPhoto();
                }else {
                    Intent intent = new Intent(this, InAppPurchaseActivity.class);
                    intent.putExtra(Utils.Type_Inapp_key, Utils.AddLayer);
                    intent.putExtra(Utils.InappId_key, getString(R.string.add_layer_inapp_id));
                    intent.putExtra(Utils.CatId_Inapp_key, "");
                    startActivity(intent);
                }

                break;
            case R.id.tv_edit_image:
                hideAllHeader();
                imgQuickTips.setVisibility(View.VISIBLE);
                editImageToolLayout.setVisibility(View.VISIBLE);
                linearEditImage.setVisibility(View.VISIBLE);
                linearFilterIntensity.setVisibility(View.GONE);

                setEditImageView();

                break;
            case R.id.tv_save:

                imgGridOnImage.setVisibility(View.GONE);
                img_lock_on_image.setVisibility(View.GONE);

                if(Utils.isCardSelected){ // No Logo for Our Card
                    img_app_logo.setVisibility(View.GONE);
                    saveShareImage(this);
                }else{
                    if(Utils.getStringPreference(this, Utils.LogoInApp_key, "").equalsIgnoreCase(Utils.Purchased)) {

                        img_app_logo.setVisibility(View.GONE);
                        saveShareImage(this);

                    }else{
                        Intent intent = new Intent(this, InAppPurchaseActivity.class);
                        intent.putExtra(Utils.Type_Inapp_key, Utils.Logo);
                        intent.putExtra(Utils.InappId_key, getString(R.string.logo_inapp_id));
                        intent.putExtra(Utils.CatId_Inapp_key, "");
                        startActivity(intent);
                    }
                }


                break;
            case R.id.tv_filter_exit:

                if(MainActivity.isTempSelectFilter) {
                    /*if (MainActivity.savedFilterIndexList.size() > 0) {
                        MainActivity.savedFilterIndexList.remove(MainActivity.savedFilterIndexList.size() - 1);
                    }*/
                    MainActivity.isTempSelectFilter = false;
                    filterList.get(lastSelectFilterPosition).setSelected(false);
                }
                img_lock_on_image.setVisibility(View.GONE);

                setMainFilterToEditor();

                filterListAdapter.notifyDataSetChanged();

                hideAllHeader();
                hideAllFooter();
                mainToolLayout.setVisibility(View.VISIBLE);
                imgQuickTips.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_filter_edit:
                if(lastSelectFilterPosition != 0) {

                    Log.v("tv_filter_edit_..","...lastSelectFilterPosition.." + lastSelectFilterPosition);
                    linearEditFilter.setVisibility(View.VISIBLE);
                    linearMainFilter.setVisibility(View.GONE);

                    linearEditImage.setVisibility(View.VISIBLE);
                    linearFilterIntensity.setVisibility(View.VISIBLE);

                    findViewById(R.id.tv_filter_edit_cancel).setVisibility(View.VISIBLE);

                    setEditImageView();
                    setFilterIntensityView();

                    filterApplyRemoveText();

                    Localytics.tagEvent(AnalyticsHelper.Event_Filter_Edited);
                    MyApplication.getFirebaseAnalytics().logEvent((AnalyticsHelper.Event_Filter_Edited).replace(" ","_"), null);
                }

                break;
            case R.id.tv_filter_save:
                //Log.v("tv_filter_edit_apply...","...click..");
                // check for InApp
                if(!isFilterPurchased(filterInappIdArray[lastSelectFilterPosition], img_lock_on_image).isEmpty()){ // not Purchase
                    String tmpInAppId = isFilterPurchased(filterInappIdArray[lastSelectFilterPosition], img_lock_on_image);

                    Intent intent = new Intent(this, InAppPurchaseActivity.class);
                    intent.putExtra(Utils.Type_Inapp_key, Utils.Filter);
                    intent.putExtra(Utils.InappId_key, tmpInAppId);
                    intent.putExtra(Utils.CatId_Inapp_key, "");
                    startActivity(intent);

                }else { // already Purchase
                    if (filterList.get(lastSelectFilterPosition).isSaved()) {
                        filterList.get(lastSelectFilterPosition).setSaved(false);
                        ((TextView) findViewById(R.id.tv_filter_save)).setText(getString(R.string.save_filter));
                        setTempFilterToEditor();
                    } else {
                        filterList.get(lastSelectFilterPosition).setSaved(true);
                        ((TextView) findViewById(R.id.tv_filter_save)).setText(getString(R.string.remove));
                    }
                    isTempSelectFilter = false;

                    filterApplyRemoveText();

                    Utils.setFilterToPref(this, filterList);
                }
                break;
            case R.id.tv_addart_text_editor:
                //Log.v("click...","...tv_addart_text_editor" );
                onViewClicked((findViewById(R.id.header_text_editor_frame_left_cancel)));
                onViewClicked(findViewById(R.id.tv_add_art));
                break;
            case R.id.img_type_text_editor:
                linearAddEditMain.setVisibility(View.GONE);
                imgTypeTextEditor.clearFocus();
                TextArtEditorModel model_tmp_text = getTextArtEditorFromTag(lastSelectTagIdTextArt);
                if(model_tmp_text.getTag().contains("e")){
                    hideAllChildTextEditor();

                    final EditText editText = (EditText) frame_container.findViewWithTag(model_tmp_text.getTag());
                    editText.setSelection(editText.getText().length());
                    //Log.v("text...",".." + editText.getText());
                    editText.requestFocus();
                    editText.postDelayed(new Runnable() {
                        public void run() {
                            //Log.v("text...","..in...Runnable...");
                            editText.requestFocus();
                            editText.setOnTouchListener(null);
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                        }
                    }, 500);


                }else{
                    imgTypeTextEditor.setPressed(false);
                }

                break;
            case R.id.img_font_text_editor:
                Log.v("img_font_text_editor...", "..click..select.." + imgFontTextEditor.isSelected() + "..pressed.." + imgFontTextEditor.isPressed() + "..focused.." + imgFontTextEditor.isFocused() + "..activated.." + imgFontTextEditor.isActivated());
                TextArtEditorModel model_tmp = getTextArtEditorFromTag(lastSelectTagIdTextArt);
                if(model_tmp.getTag().contains("e")) {
                    currentFontPosition = model_tmp.getFontPosition();
                    //Log.v("fontposition","...." + currentFontPosition);
                    fontListAdapter.notifyDataSetChanged();
                    hideAllChildTextEditor();
                    linearRecyclerFont.setVisibility(View.VISIBLE);
                }else{
                    imgFontTextEditor.setPressed(false);
                }

                break;
            case R.id.img_aling_text_editor:
                TextArtEditorModel model_tmp_for_align = getTextArtEditorFromTag(lastSelectTagIdTextArt);
                if(model_tmp_for_align.getTag().contains("e")) {
                    hideAllChildTextEditor();
                    linearTextAling.setVisibility(View.VISIBLE);
                    imgAlingTextEditor.setSelected(true);
                }else {
                    imgAlingTextEditor.setPressed(false);
                }
                break;
            case R.id.img_color_text_editor:
                TextArtEditorModel model_tmp_for_color = getTextArtEditorFromTag(lastSelectTagIdTextArt);
                if(model_tmp_for_color.getTag().contains("e")) {
                    hideAllChildTextEditor();
                    linearRecyclerTextColor.setVisibility(View.VISIBLE);
                }else{
                    imgColorTextEditor.setPressed(false);
                }
                break;
            case R.id.img_size_text_editor:
                hideAllChildTextEditor();
                linearTextSize.setVisibility(View.VISIBLE);
                //Log.v("img_size_text_editor...", "..click.." + getWindow().getCurrentFocus());
                break;
            case R.id.img_rotate_text_editor:
                hideAllChildTextEditor();
                linearTextRotate.setVisibility(View.VISIBLE);
                break;
            case R.id.img_trans_text_editor:
                hideAllChildTextEditor();
                linearTextTrans.setVisibility(View.VISIBLE);
                break;
            case R.id.img_layer_text_editor:
                hideAllChildTextEditor();
                linearTextLayer.setVisibility(View.VISIBLE);
                break;
            case R.id.img_left_aling:
                imgLeftAling.setSelected(true);
                imgCenterAling.setSelected(false);
                imgRightAling.setSelected(false);

                TextArtEditorModel model_tmp_left = getTextArtEditorFromTag(lastSelectTagIdTextArt);
                model_tmp_left.setAlign("l");
                setViewTextArt(model_tmp_left);
                break;
            case R.id.img_center_aling:
                imgLeftAling.setSelected(false);
                imgCenterAling.setSelected(true);
                imgRightAling.setSelected(false);

                TextArtEditorModel model_tmp_center = getTextArtEditorFromTag(lastSelectTagIdTextArt);
                model_tmp_center.setAlign("c");
                setViewTextArt(model_tmp_center);
                break;
            case R.id.img_right_aling:
                imgLeftAling.setSelected(false);
                imgCenterAling.setSelected(false);
                imgRightAling.setSelected(true);

                TextArtEditorModel model_tmp_right = getTextArtEditorFromTag(lastSelectTagIdTextArt);
                model_tmp_right.setAlign("r");
                setViewTextArt(model_tmp_right);
                break;
            case R.id.img_vertical_aling:
                if (imgVerticalAling.isSelected()) {
                    imgVerticalAling.setSelected(false);
                } else {
                    imgVerticalAling.setSelected(true);
                }

                TextArtEditorModel model_tmp_vertical_aling = getTextArtEditorFromTag(lastSelectTagIdTextArt);
                model_tmp_vertical_aling.setVerticalAlign(imgVerticalAling.isSelected());

                setViewTextArt(model_tmp_vertical_aling);
                break;
            case R.id.img_c_space_aling:
                imgCSpaceAling.setSelected(true);
                imgLSpaceAling.setSelected(false);

                TextArtEditorModel model_c_spcae = getTextArtEditorFromTag(lastSelectTagIdTextArt);
                seekbarSpaceAling.setProgress(model_c_spcae.getHorizontalSpace());
                break;
            case R.id.img_l_space_aling:
                imgCSpaceAling.setSelected(false);
                imgLSpaceAling.setSelected(true);

                TextArtEditorModel model_l_space = getTextArtEditorFromTag(lastSelectTagIdTextArt);
                seekbarSpaceAling.setProgress(model_l_space.getVerticalSpace());
                break;
            case R.id.img_lr_layer:
                //Log.v("click...","..img_lr_layer.." );
                TextArtEditorModel model_tmp_layer = getTextArtEditorFromTag(lastSelectTagIdTextArt);
                if(model_tmp_layer.isLR()){
                    model_tmp_layer.setLR(false);
                }else {
                    model_tmp_layer.setLR(true);
                }
                setViewTextArt(model_tmp_layer);
                break;
            case R.id.img_flip_layer:
                break;
            case R.id.img_ud_layer:
                //Log.v("click...","..img_ud_layer.." );
                TextArtEditorModel model_tmp_ud = getTextArtEditorFromTag(lastSelectTagIdTextArt);
                if(model_tmp_ud.isUD()){
                    model_tmp_ud.setUD(false);
                }else {
                    model_tmp_ud.setUD(true);
                }
                setViewTextArt(model_tmp_ud);
                break;
            case R.id.img_fwd_layer:
                //Log.v("click...","..img_fwd_layer.." );

                TextArtEditorModel model_tmp_fwd = getTextArtEditorFromTag(lastSelectTagIdTextArt);
                View view_tmp = frame_container.findViewWithTag(model_tmp_fwd.getTag());
                int index = frame_container.indexOfChild(view_tmp);
                frame_container.removeView(view_tmp);
                //Log.v("click...","..index.." + index + "...chileCount.." + frame_container.getChildCount());

                if(index == frame_container.getChildCount()){ // check if view is in top position
                    frame_container.addView(view_tmp, index);
                }else{
                    frame_container.addView(view_tmp, index + 1);
                }

                break;
            case R.id.img_back_layer:
                //Log.v("click...","..img_back_layer.." );

                TextArtEditorModel model_tmp_back = getTextArtEditorFromTag(lastSelectTagIdTextArt);
                View view_tmp_back = frame_container.findViewWithTag(model_tmp_back.getTag());
                int index_back = frame_container.indexOfChild(view_tmp_back);
                frame_container.removeView(view_tmp_back);
                //Log.v("click...","..index.." + index_back + "...chileCount.." + frame_container.getChildCount());

                if(index_back == 0){
                    frame_container.addView(view_tmp_back, 0);
                }else{
                    frame_container.addView(view_tmp_back, index_back - 1);
                }

                break;
            case R.id.img_copy_layer:
                TextArtEditorModel model_tmp_copy = getTextArtEditorFromTag(lastSelectTagIdTextArt);
                View view_tmp_copy = frame_container.findViewWithTag(model_tmp_copy.getTag());

                gson = new Gson();
                String textArt_str = gson.toJson(model_tmp_copy);
                TextArtEditorModel model_new = gson.fromJson(textArt_str, TextArtEditorModel.class);

                //FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view_tmp_copy.getLayoutParams();
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(view_tmp_copy.getLayoutParams());
                //layoutParams.topMargin = view_tmp_copy.getTop() + 20;
                //layoutParams.leftMargin = view_tmp_copy.getLeft() + 20;

                if(model_new.getTag().contains("i")){
                   // ImageView old_imageview = (ImageView) view_tmp_copy;

                    /*old_imageview.setId(new Random().nextInt(1000));

                    String tag_str = "i" + new Random().nextInt(1000);
                    old_imageview.setTag(tag_str);*/


                    final ImageView imageView = new ImageView(this);

                    final String tag_str = "i" + new Random().nextInt(1000);
                    imageView.setTag(tag_str);
                    model_new.setTag(tag_str);

                    imageView.setLayoutParams(layoutParams);
                    imageView.setAdjustViewBounds(true);

                    imageView.setX(view_tmp_copy.getX() + 30);
                    imageView.setY(view_tmp_copy.getY() + 30);

                    //Bitmap image_bitmap = ((BitmapDrawable) old_imageview.getDrawable()).getBitmap();
                    //GlideBitmapDrawable image_bitmap = (GlideBitmapDrawable)  old_imageview.getDrawable();

                    BitmapDrawable drawable = (BitmapDrawable) ((ImageView) view_tmp_copy).getDrawable();
                    Bitmap bitmap = drawable.getBitmap();

                    imageView.setImageBitmap(bitmap);

                    imageView.setOnTouchListener(new MyTouchListener());
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Log.v("tag....","....imageView...click..tag.." + tag_str);
                            viewTextArtClick(view, tag_str);

                        }
                    });

                    imageView.setImageAlpha(model_new.getTransparency());
                    imageView.setRotation(model_new.getRotate());

                    if(model_new.isLR()){
                        imageView.setScaleX(-1);
                    }else{
                        imageView.setScaleX(1);
                    }

                    if(model_new.isUD()){
                        imageView.setScaleY(-1);
                    }else{
                        imageView.setScaleY(1);
                    }

                    frame_container.addView(imageView);
                    textArtEditorList.add(model_new);

                }else if(model_new.getTag().contains("e")){

                    final EditText editText = new EditText(this);

                    final String tag_str = "e" + new Random().nextInt(1000);
                    editText.setTag(tag_str);
                    model_new.setTag(tag_str);

                    editText.setBackgroundResource(android.R.color.transparent);
                    //editText.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL | InputType.TYPE_TEXT_VARIATION_FILTER | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                    editText.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                    editText.setFocusable(true);
                    editText.setFocusableInTouchMode(true);
                    editText.clearComposingText();


                    editText.setLayoutParams(layoutParams);

                    editText.setX(view_tmp_copy.getX() + 30);
                    editText.setY(view_tmp_copy.getY() + 30);

                    editText.setText(((EditText) view_tmp_copy).getText());
                    editText.setTextSize(model_new.getSize());

                    //editText.setPadding(Utils.getSizefromDp(this, 10),0,Utils.getSizefromDp(this, 10),0);
                    //editText.setGravity(Gravity.CENTER);

                    if(model_new.getFontTypeface() != null) {
                        editText.setTypeface(model_new.getFontTypeface());
                    }

                    float alphaValue = (float) model_new.getTransparency() / 255;
                    editText.setAlpha(alphaValue);
                    editText.setRotation(model_new.getRotate());

                    editText.setOnTouchListener(new MyTouchListener());
                    editText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Log.v("tag....","....editText...click..tag.." + tag_str);
                            viewTextArtClick(view, tag_str);

                        }
                    });

                    editText.setAlpha(model_new.getTransparency());
                    editText.setRotation(model_new.getRotate());

                    if(model_new.isLR()){
                        editText.setScaleX(-1);
                    }else{
                        editText.setScaleX(1);
                    }

                    if(model_new.isUD()){
                        editText.setScaleY(-1);
                    }else{
                        editText.setScaleY(1);
                    }

                    frame_container.addView(editText);
                    textArtEditorList.add(model_new);
                }


                lastSelectTagIdTextArt = model_new.getTag();


                // set temp TextArtModel
                Utils.setTmpTextArtModelToPref(this, getTextArtEditorFromTag(lastSelectTagIdTextArt));

                break;
            case R.id.img_delete_layer:
                TextArtEditorModel model_tmp_delete = getTextArtEditorFromTag(lastSelectTagIdTextArt);
                View view_tmp_delete = frame_container.findViewWithTag(model_tmp_delete.getTag());

                frame_container.removeView(view_tmp_delete);
                removeTextArtEditorListFromTag(lastSelectTagIdTextArt);

                if(textArtEditorList.size() > 0) {
                    lastSelectTagIdTextArt = textArtEditorList.get(textArtEditorList.size() - 1).getTag();
                }else{ // no item so called cancel
                    onViewClicked((findViewById(R.id.header_text_editor_frame_left_cancel)));
                }

                break;
            case R.id.img_main_activity:
                //Log.v("V...", "....Click..img_main_activity.");
                break;
            case R.id.tv_close_art_category:
                relativeRecyclerArt.setVisibility(View.GONE);
                break;
            case R.id.tv_back_art_image:
                relativeArtImage.setVisibility(View.GONE);
                relativeArtCategory.setVisibility(View.VISIBLE);
                break;
            case R.id.header_edit_image_frame_left_cancel:
                hideAllHeader();
                mainToolLayout.setVisibility(View.VISIBLE);
                imgQuickTips.setVisibility(View.VISIBLE);
                linearEditImage.setVisibility(View.GONE);
                //setEditImageValuePreviousState();

                setMainFilterToEditor();

                break;
            case R.id.header_edit_image_frame_right_save:

                hideAllHeader();
                mainToolLayout.setVisibility(View.VISIBLE);
                imgQuickTips.setVisibility(View.VISIBLE);
                linearEditImage.setVisibility(View.GONE);

                gson= new Gson();
                String tmp_save_str = gson.toJson(editImageModel_tmp);
                editImageModel_main = gson.fromJson(tmp_save_str,EditImageModel.class);

                setMainFilterToEditor();

                break;
            case R.id.img_brightness_edit_image:
                //Log.v("brightness...", "....main..." + editImageModel_main.getBrightnessBGImage());
                seekbarEditImage.setProgress(editImageModel_tmp.getBrightnessBGImage());
                break;
            case R.id.img_contrasst_edit_image:
                //Log.v("contrasst...", "..click.." + contrastBGImage);
                seekbarEditImage.setProgress(editImageModel_tmp.getContrastBGImage());
                break;
            case R.id.img_saturation_edit_image:
                //Log.v("saturation...", "..click.." + saturationBGImage);
                seekbarEditImage.setProgress(editImageModel_tmp.getSaturationBGImage());
                break;
            case R.id.img_bw_edit_image:
                //Log.v("bw...", "..click..");

                if (editImageModel_tmp.isBlackAndWhite()) {
                    imgMainActivity.setColorFilter(ColorFilterGenerator.adjustColor((editImageModel_tmp.getBrightnessBGImage() - Utils.ColorFilterBaseValue),
                            (editImageModel_tmp.getContrastBGImage() - Utils.ColorFilterBaseValue), 0, 0));
                    editImageModel_tmp.setBlackAndWhite(false);
                } else {
                    //matrix.setSaturation(0); // black & white

                    //Utils.createBlackAndWhite(((BitmapDrawable)imgMainActivity.getDrawable()).getBitmap());

                    imgMainActivity.setColorFilter(ColorFilterGenerator.adjustColor((editImageModel_tmp.getBrightnessBGImage() - Utils.ColorFilterBaseValue),
                            (editImageModel_tmp.getContrastBGImage() - Utils.ColorFilterBaseValue), -Utils.ColorFilterBaseValue, 0));
                    editImageModel_tmp.setBlackAndWhite(true);
                }

                setTempFilterToEditor();

                /*ColorMatrix matrix = new ColorMatrix();

                if (editImageModel_tmp.isBlackAndWhite()) {
                    matrix.setSaturation(1);
                    editImageModel_tmp.setBlackAndWhite(false);
                } else {
                    matrix.setSaturation(0); // black & white
                    editImageModel_tmp.setBlackAndWhite(true);
                }

                imgMainActivity.setColorFilter(new ColorMatrixColorFilter(matrix));*/

                //imgMainActivity.setColorFilter(new ColorMatrixColorFilter(mat));

                //Utils.createBlackAndWhite(imgMainActivity.getDrawingCache());

                break;
            case R.id.img_rotate_edit_image:
                //Log.v("rotate...", "..click..");
                //imgMainActivity.setRotation(editImageModel_tmp.getRotate() + 90);
                editImageModel_tmp.setRotate(editImageModel_tmp.getRotate() + 90);
                setTempFilterToEditor();
                break;
            case R.id.img_change_bg_edit_image:
                changeBGPhoto(false);
                break;
            case R.id.header_text_editor_frame_left_cancel:
                linearTextEditor.setVisibility(View.GONE);
                linearAddEditMain.setVisibility(View.VISIBLE);
                hideAllChildTextEditor();
                hideAllHeader();
                mainToolLayout.setVisibility(View.VISIBLE);
                imgQuickTips.setVisibility(View.VISIBLE);

                if(textArtEditorList.size() > 0) {

                    TextArtEditorModel model_tmp_cancel = Utils.getTmpTextArtModelFromPref(this);

                    for (int i = 0; i < textArtEditorList.size(); i++) {
                        if (textArtEditorList.get(i).getTag().equalsIgnoreCase(model_tmp_cancel.getTag())) {
                            textArtEditorList.set(i, model_tmp_cancel);
                            break;
                        }
                    }
                    setViewTextArt(model_tmp_cancel);

                    //Log.v("cancel...tag..","..model_cancel.." + model_tmp_cancel.getTag() + "..highlight.." + model_tmp_cancel.getHighlightColor());
                    //Log.v("cancel...tag..","..list..model.." + textArtEditorList.get(0).getTag() + "..highlight.." + textArtEditorList.get(0).getHighlightColor());
                }

                imgQuickTips.requestFocus();


                break;
            case R.id.header_text_editor_frame_right_save:
                linearTextEditor.setVisibility(View.GONE);
                linearAddEditMain.setVisibility(View.VISIBLE);
                hideAllChildTextEditor();
                hideAllHeader();
                mainToolLayout.setVisibility(View.VISIBLE);
                imgQuickTips.setVisibility(View.VISIBLE);
                imgQuickTips.requestFocus();
                break;
            case R.id.img_lr_thine_filter:

                FilterModel filterModel_lr = filterList.get(lastSelectFilterPosition);

                if(filterModel_lr.isLR()){
                    filterModel_lr.setLR(false);
                }else{
                    filterModel_lr.setLR(true);
                }

                setTempFilterToEditor();

                /*if(MainActivity.savedFilterIndexList.size() > 0) {
                    int lastSavedIndex = MainActivity.savedFilterIndexList.get(MainActivity.savedFilterIndexList.size() - 1);


                    FilterModel filterModel = Utils.getFilterFromPref(this).get(lastSavedIndex);
                    FilterModel filterModelTemp = filterListTemp.get(lastSavedIndex);

                    if(filterModel.getIsLR()){
                        filterModelTemp.setIsLR(false);
                    }else {
                        filterModelTemp.setIsLR(true);
                    }
                    setTempFilterToEditor();

                }*/
                break;
            case R.id.img_rotate_thine_filter:

                FilterModel filterModel_rotate = filterList.get(lastSelectFilterPosition);

                //Log.v("rotate...","..before.." + filterModel_rotate.getRotate());

                filterModel_rotate.setRotate(filterModel_rotate.getRotate() + 90);

                //Log.v("rotate...","..after.." + filterModel_rotate.getRotate());

                setTempFilterToEditor();

                /*if(MainActivity.savedFilterIndexList.size() > 0) {
                    int lastSavedIndex = MainActivity.savedFilterIndexList.get(MainActivity.savedFilterIndexList.size() - 1);


                    FilterModel filterModel = Utils.getFilterFromPref(this).get(lastSavedIndex);
                    FilterModel filterModelTemp = filterListTemp.get(lastSavedIndex);

                    Log.v("rotate...","..before.." + filterModel.getRotate() + "...lastSavedIndex..." + lastSavedIndex);

                    filterModelTemp.setRotate(filterModel.getRotate() + 90);

                    Log.v("rotate...","..after.." + filterModel.getRotate() + "...lastSavedIndex..." + lastSavedIndex);

                    setTempFilterToEditor();
                }*/
                break;
            case R.id.tv_filter_remove_all:
                //linearEditFilter.setVisibility(View.GONE);
                //linearMainFilter.setVisibility(View.VISIBLE);
                //linearEditImage.setVisibility(View.GONE);

                removeAllFiltersWarningDialog();

                break;
            /*case R.id.tv_filter_edit_apply:

                if(filterList.get(lastSelectFilterPosition).isSaved()){
                    filterList.get(lastSelectFilterPosition).setSaved(false);
                    ((TextView)findViewById(R.id.tv_filter_edit_apply)).setText(getString(R.string.apply));
                    lastSelectFilterPosition = 0;
                    for(int i = filterList.size()-1 ; i >= 0; i--){
                        FilterModel model = filterList.get(i);
                        if(model.isSaved()){
                            lastSelectFilterPosition = i;
                            Log.v("remove..","...lastSelectInd.." + i);
                            break;
                        }
                    }
                    filterListAdapter.notifyDataSetChanged();
                    setTempFilterToEditor();
                }else{
                    filterList.get(lastSelectFilterPosition).setSaved(true);
                    ((TextView)findViewById(R.id.tv_filter_edit_apply)).setText(getString(R.string.remove));
                }
                isTempSelectFilter = false;

                filterApplyRemoveText();

                Utils.setFilterToPref(this, filterList);

                *//*filterListMain.clear();
                for (FilterModel model : filterList){
                    filterListMain.add(new FilterModel(model));
                }*//*


                break;*/
            case R.id.tv_filter_edit_cancel:

                linearEditFilter.setVisibility(View.GONE);
                linearMainFilter.setVisibility(View.VISIBLE);
                linearEditImage.setVisibility(View.GONE);

                findViewById(R.id.tv_filter_edit_cancel).setVisibility(View.GONE);

                //Log.v("filters...","..main..size.." + filterListMain.size() + "...position.." + lastSelectFilterPosition);

                FilterModel filterModel = Utils.getFilterFromPref(this).get(lastSelectFilterPosition);

                //Log.v("filters...","..cancel..rotate.." + filterModel.getRotate() + "...LR..." + filterModel.isLR());

                FilterModel tmpModel = filterList.get(lastSelectFilterPosition);

                tmpModel.setLR(filterModel.isLR());
                tmpModel.setRotate(filterModel.getRotate());
                tmpModel.setAlpha(filterModel.getAlpha());

                gson= new Gson();
                String str_imgModel = gson.toJson(editImageModel_main);
                editImageModel_tmp = gson.fromJson(str_imgModel,EditImageModel.class);

                setTempFilterToEditor();

               /* if(MainActivity.savedFilterIndexList.size() > 0) {
                    int lastSavedIndex = MainActivity.savedFilterIndexList.get(MainActivity.savedFilterIndexList.size() - 1);

                    FilterModel filterModel = Utils.getFilterFromPref(this).get(lastSavedIndex);
                    FilterModel filterModelTemp = filterListTemp.get(lastSavedIndex);

                    Log.v("rotate...","...." + filterModel.getRotate());

                    filterModelTemp.setIsLR(filterModel.getIsLR());
                    filterModelTemp.setRotate(filterModel.getRotate());
                    filterModelTemp.setAlpha(filterModel.getAlpha());

                    setTempFilterToEditor();
                }*/
                break;
            case R.id.tv_filter_edit_save_all:

                // check for InApp
                if(!isFilterPurchased(filterInappIdArray[lastSelectFilterPosition], img_lock_on_image).isEmpty()){ // not Purchase
                    String tmpInAppId = isFilterPurchased(filterInappIdArray[lastSelectFilterPosition], img_lock_on_image);

                    Intent intent = new Intent(this, InAppPurchaseActivity.class);
                    intent.putExtra(Utils.Type_Inapp_key, Utils.Filter);
                    intent.putExtra(Utils.InappId_key, tmpInAppId);
                    intent.putExtra(Utils.CatId_Inapp_key, "");
                    startActivity(intent);

                }else { // already Purchase

                    // save filter.................
                    if (filterList.get(lastSelectFilterPosition).isSaved()) {
                        filterList.get(lastSelectFilterPosition).setSaved(false);
                        //((TextView)findViewById(R.id.tv_filter_edit_apply)).setText(getString(R.string.apply));
                        lastSelectFilterPosition = 0;
                        for (int i = filterList.size() - 1; i >= 0; i--) {
                            FilterModel model_tmp_all = filterList.get(i);
                            if (model_tmp_all.isSaved()) {
                                lastSelectFilterPosition = i;
                                Log.v("remove..", "...lastSelectInd.." + i);
                                break;
                            }
                        }
                        filterListAdapter.notifyDataSetChanged();
                        setTempFilterToEditor();
                    } else {
                        filterList.get(lastSelectFilterPosition).setSaved(true);
                        //((TextView)findViewById(R.id.tv_filter_edit_apply)).setText(getString(R.string.remove));
                    }
                    isTempSelectFilter = false;

                    filterApplyRemoveText();

                    Utils.setFilterToPref(this, filterList);


                    // save BG Image.................
                    gson = new Gson();
                    String str = gson.toJson(editImageModel_tmp);
                    editImageModel_main = gson.fromJson(str, EditImageModel.class);

                    setMainFilterToEditor();

                    // change UI
                    linearEditFilter.setVisibility(View.GONE);
                    linearMainFilter.setVisibility(View.VISIBLE);
                    linearEditImage.setVisibility(View.GONE);
                    findViewById(R.id.tv_filter_edit_cancel).setVisibility(View.GONE);

                }

                break;
            case R.id.tv_filter_edit_remove_all:
                removeAllFiltersWarningDialog();
                break;
            case R.id.tv_filter_save_share:

                final ProgressDialog progressDialog = new ProgressDialog(this);
                Utils.showProgressDialog(progressDialog, getString(R.string.loading));

                onViewClicked(findViewById(R.id.tv_filter_exit));
                onViewClicked(findViewById(R.id.tv_add_filters));

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        onViewClicked(findViewById(R.id.tv_save));
                        Log.v("save_share...","..handler..");

                        Utils.dismissProgressDialog(progressDialog);
                    }
                }, 200);

                break;
            case R.id.img_rotete_main:
                TextArtEditorModel model = getTextArtEditorFromTag(lastSelectTagIdTextArt);

                if(model.getRotate() >= 0 && model.getRotate() < 90){
                    model.setRotate(90);
                }else if(model.getRotate() >= 90 && model.getRotate() < 179){
                    model.setRotate(180);
                }else if(model.getRotate() >= 180 && model.getRotate() < 269){
                    model.setRotate(270);
                }else if(model.getRotate() >= 270){
                    model.setRotate(0);
                }

                seekbarManualRotate.setProgress(model.getRotate());
                setViewTextArt(model);

                break;
            case R.id.img_filter_recycler_arrowleft:
                int lastVisibleItemPosition_left = ((LinearLayoutManager) recyclerFilter.getLayoutManager()).findLastVisibleItemPosition();
                if(lastVisibleItemPosition_left >= 4 && lastVisibleItemPosition_left <= 51){
                    recyclerFilter.scrollToPosition(0);
                }else if(lastVisibleItemPosition_left >= 52 && lastVisibleItemPosition_left <= 71){
                    recyclerFilter.scrollToPosition(0);
                }else if(lastVisibleItemPosition_left >= 72 && lastVisibleItemPosition_left <= 91){
                    recyclerFilter.scrollToPosition(52);
                }else if(lastVisibleItemPosition_left >= 92 && lastVisibleItemPosition_left <= 111){
                    recyclerFilter.scrollToPosition(72);
                }else if(lastVisibleItemPosition_left >= 112 && lastVisibleItemPosition_left <= 131){
                    recyclerFilter.scrollToPosition(92);
                }

                break;
            case R.id.img_filter_recycler_arrowright:
                int lastVisibleItemPosition_right = ((LinearLayoutManager) recyclerFilter.getLayoutManager()).findLastVisibleItemPosition();
                if(lastVisibleItemPosition_right >= 0 && lastVisibleItemPosition_right <= 51){
                    recyclerFilter.scrollToPosition(52);
                }else if(lastVisibleItemPosition_right >= 52 && lastVisibleItemPosition_right <= 71){
                    recyclerFilter.scrollToPosition(72);
                }else if(lastVisibleItemPosition_right >= 72 && lastVisibleItemPosition_right <= 91){
                    recyclerFilter.scrollToPosition(92);
                }else if(lastVisibleItemPosition_right >= 92 && lastVisibleItemPosition_right <= 111){
                    recyclerFilter.scrollToPosition(112);
                }

                break;
        }
    }

    public List<FilterModel> getFilterList() {

        int temp = 0;

        filterList.clear();

        String[] filters = getResources().getStringArray(R.array.filter_image_array);
        //Log.v("filters...","..size.." + filters.length);

        for (int i = 0; i < filters.length; i++) {
            FilterModel model = new FilterModel();
            model.setId(String.valueOf(i));
            model.setFilterDrawableID(Utils.getDrawableResIDFromName(this, filters[i]));
            model.setSaved(false);
            model.setSelected(false);
            model.setAlpha(255);
            model.setLR(false);
            model.setRotate(0);
                                                            // Total = 132
            if (i == 0) {
                model.setName("Original");                  // 0
            } else {
                if (filters[i].contains("eclipse")) {       //
                    model.setName(i + " Eclipse");
                } else if (filters[i].contains("lite")) {   // 1
                    temp = (i == 1) ? 1 : ++temp;
                    model.setName(temp + " Lite Base");
                } else if (filters[i].contains("rainbow")) {// 2-51
                    temp = (i == 2) ? 1 : ++temp;
                    model.setName(temp + " Rainbow");
                } else if (filters[i].contains("moon")) {   // 52-71
                    temp = (i == 52) ? 1 : ++temp;
                    model.setName(temp + " Moon");
                } else if (filters[i].contains("dreamy")) { // 72-91
                    temp = (i == 72) ? 1 : ++temp;
                    model.setName(temp + " Dreamy");
                } else if (filters[i].contains("floral")) { // 92-111
                    temp = (i == 92) ? 1 : ++temp;
                    model.setName(temp + " Earth");
                } else if (filters[i].contains("dusk")) {   // 112-131
                    temp = (i == 112) ? 1 : ++temp;
                    model.setName(temp + " Dusk");
                }
            }

            filterList.add(model);
        }

        //Log.v("filters...",".last..size.." + filterList.size());

        return filterList;

    }

    public void clearAllFilterOriginal(){
        getFilterList();
        lastSelectFilterPosition = 0;
        filterList.get(lastSelectFilterPosition).setSelected(true);
        Utils.setFilterToPref(this, filterList);
        filterListAdapter.notifyDataSetChanged();
        setTempFilterToEditor();
    }

    public void hideAllHeader() {
        mainToolLayout.setVisibility(View.GONE);
        editImageToolLayout.setVisibility(View.GONE);
        textEditorToolLayout.setVisibility(View.GONE);
        imgQuickTips.setVisibility(View.GONE);
        linearFilterSaveExit.setVisibility(View.GONE);
    }

    public void hideAllFooter() {
        linearRecyclerFilter.setVisibility(View.GONE);
    }

    public void hideAllChildTextEditor() {
        linearRecyclerFont.setVisibility(View.GONE);
        linearTextSize.setVisibility(View.GONE);
        linearTextAling.setVisibility(View.GONE);
        linearTextTrans.setVisibility(View.GONE);
        linearTextRotate.setVisibility(View.GONE);
        linearTextLayer.setVisibility(View.GONE);
        linearRecyclerTextColor.setVisibility(View.GONE);

        Utils.hideKeyBoardFromWindow(this, imgTypeTextEditor);

        // -------- set Edittext touchListner --------------
        for(TextArtEditorModel textArtEditorModel : textArtEditorList){
            if(textArtEditorModel.getTag().contains("e")) {
                EditText editText = (EditText) frame_container.findViewWithTag(textArtEditorModel.getTag());
                editText.setOnTouchListener(new MyTouchListener());
            }
        }
        // ----------------------------------

        imgAlingTextEditor.setSelected(false);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view == imgFontTextEditor) {
            if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                view.performClick();
                 //return true;
            }
        } else if (view == imgSizeTextEditor) {
            if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                view.performClick();
            }
        } else if (view == imgAlingTextEditor) {
            if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                view.performClick();
            }
        } else if (view == imgLeftAling) {
            if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                view.performClick();
            }
        } else if (view == imgCenterAling) {
            if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                view.performClick();
            }
        } else if (view == imgRightAling) {
            if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                view.performClick();
            }
        } else if (view == imgVerticalAling) {
            if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                view.performClick();
                return true;
            }
        } else if (view == imgCSpaceAling) {
            if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                view.performClick();
            }
        } else if (view == imgLSpaceAling) {
            if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                view.performClick();
            }
        } else if (view == imgTransTextEditor) {
            if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                view.performClick();
            }
        } else if (view == imgTypeTextEditor) {
            if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                view.performClick();
            }
        } else if (view == imgColorTextEditor) {
            if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                view.performClick();
            }
        } else if (view == imgRotateTextEditor) {
            if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                view.performClick();
            }
        } else if (view == imgLayerTextEditor) {
            if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                view.performClick();
            }
        } else if (view == imgBrightnessEditImage) {
            if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                view.performClick();
            }
        } else if (view == imgContrasstEditImage) {
            if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                view.performClick();
            }
        } else if (view == imgSaturationEditImage) {
            if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                view.performClick();
            }
        }

        return false;
    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        if (checkedId == radioTextColor.getId()) {
            recyclerTextColor.setVisibility(View.VISIBLE);
            recyclerHighlightColor.setVisibility(View.GONE);
        } else {
            recyclerTextColor.setVisibility(View.GONE);
            recyclerHighlightColor.setVisibility(View.VISIBLE);
        }

        //Log.v("getCheckedRadio","....." + radiogroupColor.getCheckedRadioButtonId());
    }


    public void getArtCatogries() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> call = apiService.getArtCategory();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String resultStr = response.body().string();
                        JSONObject jsonObject = new JSONObject(resultStr);

                        if (jsonObject.getString(ApiClient.STATUS).equalsIgnoreCase(ApiClient.SUCCESS)) {

                            gson = new Gson();
                            String jsonOutput = Utils.cur2JsonArray(databaseHandler.getAllDataFromTable(DatabaseHandler.ART_TABLE)).toString();
                            Type listType = new TypeToken<List<ArtLocalModel>>() {
                            }.getType();
                            List<ArtLocalModel> lists = gson.fromJson(jsonOutput, listType);

                            List<String> catIDList = new ArrayList();
                            List<String> catIDmDateList = new ArrayList();

                            for (ArtLocalModel model : lists) {
                                catIDList.add(model.getCatId());
                                catIDmDateList.add(model.getCatId() + "," + model.getMdate());

                                //Log.v("local.", "..catIDList.." + model.getCatId() + "...mDateList..." + model.getMdate());
                            }

                            JSONArray jsonArray = jsonObject.getJSONArray(ApiClient.ROOT);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jObj = jsonArray.getJSONObject(i);
                                jObj = jObj.getJSONObject(ApiClient.RECORD);
                                //Log.v("cat..id.", "..i.." + i + "..." + jObj.getString(ApiClient.CAT_ID));

                                String catID = jObj.getString(ApiClient.CAT_ID);
                                String mDate = jObj.getString(ApiClient.MDATE);
                                String bIndentifier = jObj.getString(ApiClient.Bundle_Indentifier);
                                String image = jObj.getString(ApiClient.IMAGE);
                                String possion = String.valueOf(i);

                                ArtLocalModel artLocalModel = new ArtLocalModel();
                                artLocalModel.setCatId(catID);
                                artLocalModel.setCatImage(image);
                                artLocalModel.setMdate(mDate);
                                artLocalModel.setCatInappId(bIndentifier);
                                artLocalModel.setStatus("0");
                                artLocalModel.setImageUrl(image);
                                artLocalModel.setCatPossion(possion);
                                artLocalModel.setCatTry("");

                                if (catIDList.contains(catID)) {

                                    for (String catDate : catIDmDateList) {
                                        String catID_str = catDate.split(",")[0];

                                        if (catID_str.equalsIgnoreCase(catID)) {
                                            String date_str = catDate.split(",")[1];

                                            if (!date_str.equalsIgnoreCase(mDate)) {
                                                databaseHandler.updateArtTable(artLocalModel);    // update query
                                                //Log.v("contains..IFF..", "...update query.." + date_str + "....." + mDate);
                                            } else {
                                                databaseHandler.updateArtTablePossion(catID, possion);    // update possion query
                                                //Log.v("contains..IFF..", ".update possion..");
                                            }
                                        }
                                    }
                                    //Log.v("indexOf....", ".." + catIDList.indexOf(catID) + "...catID.." + catID);


                                } else {

                                    databaseHandler.insertArtTable(artLocalModel);    // insert query

                                    //Log.v("contains..Else..", ".insert..");
                                }

                                catIDList.remove(catID);
                            }

                            if (!catIDList.isEmpty()) {
                                for (int i = 0; i < catIDList.size(); i++) {
                                    //Log.v("delete..catID..", "..." + catIDList.get(i));
                                    databaseHandler.deleteFromArtTable(catIDList.get(i));
                                    databaseHandler.deleteFromArtImageTable(catIDList.get(i));
                                }
                            }

                            setUpArtCategoriesAdapter();

                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("onFailure..", "..onFailure.." + t.getMessage());

                setUpArtCategoriesAdapter();
            }
        });

    }


    public void setUpArtCategoriesAdapter() {

        gson = new Gson();
        String jsonOutput = Utils.cur2JsonArray(databaseHandler.getAllDataFromTable(DatabaseHandler.ART_TABLE)).toString();
        Type listType = new TypeToken<List<ArtLocalModel>>() {
        }.getType();
        List<ArtLocalModel> lists = gson.fromJson(jsonOutput, listType);

        Collections.sort(lists, new Comparator<ArtLocalModel>() {
            @Override
            public int compare(ArtLocalModel artLocalModel, ArtLocalModel t1) {
                //Log.v("sort...","..." + artLocalModel.getCatPossion() + "....." + t1.getCatPossion());
                Integer aa = Integer.parseInt(artLocalModel.getCatPossion());
                Integer bb = Integer.parseInt(t1.getCatPossion());
                return aa.compareTo(bb);
            }
        });

        // ........ add Recent to Art Category if any ...............
        gson = new Gson();
        String jsonOutputRecent = Utils.cur2JsonArray(databaseHandler.getAllDataFromTable(DatabaseHandler.RECENT_TABLE)).toString();
        Type listTypeRecent = new TypeToken<List<RecentLocalModel>>() {
        }.getType();
        List<RecentLocalModel> listsRecent = gson.fromJson(jsonOutputRecent, listTypeRecent);

        if(listsRecent.size() > 0){
            ArtLocalModel artLocalModel = new ArtLocalModel();

            artLocalModel.setCatId(Utils.Recent);
            artLocalModel.setCatImage("");
            artLocalModel.setMdate("");
            artLocalModel.setCatInappId("");
            artLocalModel.setStatus("");
            artLocalModel.setImageUrl("");
            artLocalModel.setCatPossion("");
            artLocalModel.setCatTry("");

            lists.add(0, artLocalModel);
        }

        // .....................................................

        artCategoryListAdapter = new ArtCategoryListAdapter(this, lists);
        recyclerArtCategories.setAdapter(artCategoryListAdapter);


        //DownloadImage.artCategoriesImage(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                DownloadImage.artCategoriesImage(getApplicationContext());
            }
        }).start();
    }


    public void getArtImage(final String catID) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        Utils.showProgressDialog(progressDialog, getString(R.string.loading));

        relativeArtCategory.setVisibility(View.GONE);
        relativeArtImage.setVisibility(View.VISIBLE);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> call = apiService.getArtImages(catID);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String resultStr = response.body().string();
                        JSONObject jsonObject = new JSONObject(resultStr);
                        //Log.v("jsonObject..", "..jsonObject.." + jsonObject);

                        if (jsonObject.getString(ApiClient.STATUS).equalsIgnoreCase(ApiClient.SUCCESS)) {

//                            gson = new Gson();
//                            String jsonOutput = Utils.cur2JsonArray(databaseHandler.getArtImageOnCatID(catID)).toString();
//                            Type listType = new TypeToken<List<ArtImageLocalModel>>() {}.getType();
//                            List<ArtImageLocalModel> lists = gson.fromJson(jsonOutput, listType);

                            List<ArtImageLocalModel> lists = new ArrayList<>();

                            JSONArray tmpjsonArray = Utils.cur2JsonArray(databaseHandler.getArtImageOnCatID(catID));

                            //Log.v("jsonArray..", "...." + tmpjsonArray.length());

                            for (int i = 0; i < tmpjsonArray.length(); i++) {
                                try {
                                    JSONObject tmpObj = tmpjsonArray.getJSONObject(i);

                                    ArtImageLocalModel artImageLocalModel = new ArtImageLocalModel();
                                    artImageLocalModel.setId(tmpObj.getString("id"));
                                    artImageLocalModel.setCatId(tmpObj.getString("cat_id"));
                                    artImageLocalModel.setCatImage(tmpObj.getString("cat_image"));
                                    artImageLocalModel.setStatus(tmpObj.getString("status"));
                                    artImageLocalModel.setImageId(tmpObj.getString("image_id"));
                                    artImageLocalModel.setImageUrl(tmpObj.getString("image_url"));

                                    lists.add(artImageLocalModel);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            List<String> imageIDList = new ArrayList();

                            for (ArtImageLocalModel model : lists) {
                                imageIDList.add(model.getImageId());
                            }

                            JSONArray jsonArray = jsonObject.getJSONArray(ApiClient.ROOT);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jObj = jsonArray.getJSONObject(i);
                                jObj = jObj.getJSONObject(ApiClient.RECORD);

                                String imageURL = jObj.getString(ApiClient.IMAGE);
                                String imageID = jObj.getString(ApiClient.IMAGE_ID);


                                if (imageIDList.contains(imageID)) {

                                    imageIDList.remove(imageID);

                                    //Log.v("jsonObject..", "..remove.." + imageID);

                                } else {

                                    ArtImageLocalModel artImageLocalModel = new ArtImageLocalModel();
                                    artImageLocalModel.setCatId(jObj.getString(ApiClient.CAT_ID));
                                    artImageLocalModel.setCatImage(imageURL);
                                    artImageLocalModel.setStatus("0");
                                    artImageLocalModel.setImageId(imageID);
                                    artImageLocalModel.setImageUrl(imageURL);


                                    databaseHandler.insertArtImageTable(artImageLocalModel);    // insert query

                                    //Log.v("jsonObject..", "..insert.." + imageID);

                                }

                            }

                            if (!imageIDList.isEmpty()) {
                                for (int i = 0; i < imageIDList.size(); i++) {
                                    databaseHandler.deleteFromArtImageTableOnImageID(imageIDList.get(i));
                                }
                            }


                            setUpArtImageAdapter(catID);

                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Utils.dismissProgressDialog(progressDialog);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("onFailure..", "..onFailure.." + t.getMessage());

                setUpArtImageAdapter(catID);

                Utils.dismissProgressDialog(progressDialog);

            }
        });

    }

    public void setUpArtImageAdapter(String catID) {

        List<ArtImageLocalModel> lists = new ArrayList<>();

        JSONArray jsonArray = Utils.cur2JsonArray(databaseHandler.getArtImageOnCatID(catID));

        //Log.v("jsonArray..", "...." + jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                ArtImageLocalModel artImageLocalModel = new ArtImageLocalModel();
                artImageLocalModel.setId(jsonObject.getString("id"));
                artImageLocalModel.setCatId(jsonObject.getString("cat_id"));
                artImageLocalModel.setCatImage(jsonObject.getString("cat_image"));
                artImageLocalModel.setStatus(jsonObject.getString("status"));
                artImageLocalModel.setImageId(jsonObject.getString("image_id"));
                artImageLocalModel.setImageUrl(jsonObject.getString("image_url"));

                lists.add(artImageLocalModel);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        artImageListAdapter = new ArtImageListAdapter(this, lists);
        recyclerArtImage.setAdapter(artImageListAdapter);

        //DownloadImage.artImage(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                DownloadImage.artImage(getApplicationContext());
            }
        }).start();
    }

    public void setUpRecentArtImageAdapter(){

        relativeArtCategory.setVisibility(View.GONE);
        relativeArtImage.setVisibility(View.VISIBLE);

        gson = new Gson();
        String jsonOutput = Utils.cur2JsonArray(databaseHandler.getAllDataFromTable(DatabaseHandler.RECENT_TABLE)).toString();
        Type listType = new TypeToken<List<RecentLocalModel>>() {
        }.getType();
        List<RecentLocalModel> lists = gson.fromJson(jsonOutput, listType);


        List<ArtImageLocalModel> recentArtImagelists = new ArrayList<>();

        for(RecentLocalModel model : lists){

            ArtImageLocalModel artImageLocalModel = new ArtImageLocalModel();
            artImageLocalModel.setId(model.getId());
            artImageLocalModel.setCatId(model.getCatId());
            artImageLocalModel.setImageId(model.getImageId());
            artImageLocalModel.setStatus(model.getStatus());
            artImageLocalModel.setCatImage(model.getCatImage());
            artImageLocalModel.setImageUrl(model.getCatImage());

            recentArtImagelists.add(artImageLocalModel);
        }

        Collections.reverse(recentArtImagelists);

        artImageListAdapter = new ArtImageListAdapter(this, recentArtImagelists);
        recyclerArtImage.setAdapter(artImageListAdapter);

    }

    public void addImageview(Bitmap bm, String imgURL, byte[] encodeByte) {

        int imageSize = Utils.getSizefromDp(this, 200);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(imageSize, ViewGroup.LayoutParams.WRAP_CONTENT);
        //layoutParams.leftMargin = (screenWidth / 2) - (imageSize / 2);
        layoutParams.topMargin = 20;
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;

        final ImageView imageView = new ImageView(this);
        final String tag_str = "i" + new Random().nextInt(1000);
        imageView.setTag(tag_str);
        //Log.v("tag....","....tag_str..." + tag_str);
        //Log.v("tag....","....imageView..." + imageView.getTag());
        imageView.setLayoutParams(layoutParams);
        imageView.setAdjustViewBounds(true);

        if(bm != null){
            imageView.setImageBitmap(bm);
        }else if (imgURL.isEmpty()) {
            //Utils.loadImagePlaceholderGlide(this, encodeByte, imageView);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            imageView.setImageBitmap(bitmap);
        } else {
            //Utils.loadImagePlaceholderGlide(this, imgURL, imageView);

            // ---------- Picasso, Tag overwrite issue ------------
            //Utils.loadImage(this, imgURL, imageView);

            Picasso.with(this).load(imgURL).placeholder(R.drawable.placeholder_img).into(imageView);
        }


        imageView.setOnTouchListener(new MyTouchListener());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("tag....","....imageView...click..tag.." + tag_str);
                viewTextArtClick(view, tag_str);

            }
        });

        frame_container.addView(imageView);

        textArtEditorList.add(getDeafultTextArtEditor(tag_str));

    }

    public void addEdittext() {

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = 20;
        //layoutParams.leftMargin = (screenWidth / 2) - 100;
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;

        final EditText editText = new EditText(this);
        final String tag_str = "e" + new Random().nextInt(1000);
        editText.setTag(tag_str);
        editText.setLayoutParams(layoutParams);
        editText.setTextSize(20);
        editText.setBackgroundResource(android.R.color.transparent);
        //editText.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL | InputType.TYPE_TEXT_VARIATION_FILTER | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        editText.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        //editText.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.clearComposingText();
        editText.setPadding(Utils.getSizefromDp(this, 10),0,Utils.getSizefromDp(this, 10),0);
        editText.setGravity(Gravity.CENTER);

        editText.setOnTouchListener(new MyTouchListener());
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("tag....","....editText...click..tag.." + tag_str);
                viewTextArtClick(view, tag_str);

            }
        });

        frame_container.addView(editText);

        textArtEditorList.add(getDeafultTextArtEditor(editText.getTag().toString()));

        lastSelectTagIdTextArt = tag_str;


        Utils.setTmpTextArtModelToPref(this, getTextArtEditorFromTag(lastSelectTagIdTextArt));

    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if(seekBar == seekbarTextSize) {
            Log.v("progress...", "..seekbarTextSize.." + seekBar.getProgress());
            TextArtEditorModel model = getTextArtEditorFromTag(lastSelectTagIdTextArt);
            model.setSize(seekBar.getProgress());
            setViewTextArt(model);
        } else if(seekBar == seekbarTextTrans){
            Log.v("progress...","..seekbarTextTrans.." + seekBar.getProgress());
            TextArtEditorModel model = getTextArtEditorFromTag(lastSelectTagIdTextArt);
            model.setTransparency(seekBar.getProgress());
            setViewTextArt(model);
        }else if(seekBar == seekbarManualRotate){
            Log.v("progress...","..seekbarManualRotate.." + seekBar.getProgress());
            TextArtEditorModel model = getTextArtEditorFromTag(lastSelectTagIdTextArt);
            model.setRotate(seekBar.getProgress());
            setViewTextArt(model);
        }else if(seekBar == seekbarSpaceAling){
            TextArtEditorModel model = getTextArtEditorFromTag(lastSelectTagIdTextArt);
            if(imgCSpaceAling.isSelected()){
                model.setHorizontalSpace(seekbarSpaceAling.getProgress());
            }else{
                model.setVerticalSpace(seekbarSpaceAling.getProgress());
            }
            setViewTextArt(model);
        }

        /*if (seekBar == seekbarEditImage) {
            if (fromUser) {
                Log.v("seek...", "..." + progress);

                if (imgBrightnessEditImage.hasFocus()) {
                    editImageModel_tmp.setBrightnessBGImage(progress);
                } else if (imgContrasstEditImage.hasFocus()) {
                    editImageModel_tmp.setContrastBGImage(progress);
                } else {
                    editImageModel_tmp.setSaturationBGImage(progress);
                }

                imgMainActivity.setColorFilter(ColorFilterGenerator.adjustColor((editImageModel_tmp.getBrightnessBGImage() - 128),
                        (editImageModel_tmp.getContrastBGImage() - 128), (editImageModel_tmp.getSaturationBGImage() - 128), 0));


                if(editImageModel_tmp.isBlackAndWhite()){
                    imgMainActivity.setColorFilter(ColorFilterGenerator.adjustColor((editImageModel_tmp.getBrightnessBGImage() - 128),
                            (editImageModel_tmp.getContrastBGImage() - 128), -128, 0));
                }else{
                    imgMainActivity.setColorFilter(ColorFilterGenerator.adjustColor((editImageModel_tmp.getBrightnessBGImage() - 128),
                            (editImageModel_tmp.getContrastBGImage() - 128), (editImageModel_tmp.getSaturationBGImage() - 128), 0));
                }

                setTempFilterToEditor();
            }

        }*/
        /*else if(seekBar == seekbarFilterIntensity){
            if(fromUser){
                for(FilterModel filterModel : filterList){
                    if(filterModel.isSelected()){
                        filterModel.setAlpha(progress);

                        setFilterToEditorLoop();
                    }
                }
            }
        }*/
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if(seekBar == seekbarFilterIntensity){


            FilterModel filterModel_intensity = filterList.get(lastSelectFilterPosition);

            filterModel_intensity.setAlpha(seekBar.getProgress());

            setTempFilterToEditor();

            /*if(MainActivity.savedFilterIndexList.size() > 0) {
                int lastSavedIndex = MainActivity.savedFilterIndexList.get(MainActivity.savedFilterIndexList.size() - 1);

                //FilterModel filterModel = filterList.get(lastSavedIndex);
                FilterModel filterModelTemp = filterListTemp.get(lastSavedIndex);

                filterModelTemp.setAlpha(seekBar.getProgress());

                setTempFilterToEditor();
            }*/

        } else if (seekBar == seekbarEditImage){
            if (imgBrightnessEditImage.hasFocus()) {
                editImageModel_tmp.setBrightnessBGImage(seekBar.getProgress());
            } else if (imgContrasstEditImage.hasFocus()) {
                editImageModel_tmp.setContrastBGImage(seekBar.getProgress());
            } else {
                editImageModel_tmp.setSaturationBGImage(seekBar.getProgress());
            }

            /*imgMainActivity.setColorFilter(ColorFilterGenerator.adjustColor((editImageModel_tmp.getBrightnessBGImage() - 128),
                    (editImageModel_tmp.getContrastBGImage() - 128), (editImageModel_tmp.getSaturationBGImage() - 128), 0));


            if(editImageModel_tmp.isBlackAndWhite()){
                imgMainActivity.setColorFilter(ColorFilterGenerator.adjustColor((editImageModel_tmp.getBrightnessBGImage() - 128),
                        (editImageModel_tmp.getContrastBGImage() - 128), -128, 0));
            }else{
                imgMainActivity.setColorFilter(ColorFilterGenerator.adjustColor((editImageModel_tmp.getBrightnessBGImage() - 128),
                        (editImageModel_tmp.getContrastBGImage() - 128), (editImageModel_tmp.getSaturationBGImage() - 128), 0));
            }*/

            setTempFilterToEditor();
        }
//        else if(seekBar == seekbarTextSize){
//            Log.v("progress...","..seekbarTextSize.." + seekBar.getProgress());
//            TextArtEditorModel model = getTextArtEditorFromTag(lastSelectTagIdTextArt);
//            model.setSize(seekBar.getProgress());
//            setViewTextArt(model);
//        }
//        else if(seekBar == seekbarTextTrans){
//            Log.v("progress...","..seekbarTextTrans.." + seekBar.getProgress());
//            TextArtEditorModel model = getTextArtEditorFromTag(lastSelectTagIdTextArt);
//            model.setTransparency(seekBar.getProgress());
//            setViewTextArt(model);
//        }
//        else if(seekBar == seekbarManualRotate){
//            Log.v("progress...","..seekbarManualRotate.." + seekBar.getProgress());
//            TextArtEditorModel model = getTextArtEditorFromTag(lastSelectTagIdTextArt);
//            model.setRotate(seekBar.getProgress());
//            setViewTextArt(model);
//        }
//        else if(seekBar == seekbarSpaceAling){
//            TextArtEditorModel model = getTextArtEditorFromTag(lastSelectTagIdTextArt);
//            if(imgCSpaceAling.isSelected()){
//                model.setHorizontalSpace(seekbarSpaceAling.getProgress());
//            }else{
//                model.setVerticalSpace(seekbarSpaceAling.getProgress());
//            }
//            setViewTextArt(model);
//        }
    }

    public void setFilterIntensityView(){

        FilterModel filterModel_inten = filterList.get(lastSelectFilterPosition);

        seekbarFilterIntensity.setProgress(filterModel_inten.getAlpha());
    }

    public void setEditImageValuePreviousState(){

        imgMainActivity.setColorFilter(ColorFilterGenerator.adjustColor((editImageModel_main.getBrightnessBGImage() - Utils.ColorFilterBaseValue),
                (editImageModel_main.getContrastBGImage() - Utils.ColorFilterBaseValue), (editImageModel_main.getSaturationBGImage() - Utils.ColorFilterBaseValue), 0));


        ColorMatrix matrix = new ColorMatrix();

        /*if (editImageModel_main.isBlackAndWhite()) {
            matrix.setSaturation(0);
        } else {
            matrix.setSaturation(1);
        }*/

        if(editImageModel_main.isBlackAndWhite()){
            imgMainActivity.setColorFilter(ColorFilterGenerator.adjustColor((editImageModel_main.getBrightnessBGImage() - Utils.ColorFilterBaseValue),
                    (editImageModel_main.getContrastBGImage() - Utils.ColorFilterBaseValue), -Utils.ColorFilterBaseValue, 0));
        }else{
            imgMainActivity.setColorFilter(ColorFilterGenerator.adjustColor((editImageModel_main.getBrightnessBGImage() - Utils.ColorFilterBaseValue),
                    (editImageModel_main.getContrastBGImage() - Utils.ColorFilterBaseValue), (editImageModel_main.getSaturationBGImage() - Utils.ColorFilterBaseValue), 0));
        }


        imgMainActivity.setColorFilter(new ColorMatrixColorFilter(matrix));


        imgMainActivity.setRotation(editImageModel_main.getRotate());
    }

    public void setEditImageValueTemp(){

        imgMainActivity.setImageBitmap(Utils.ImageCopy);

        imgMainActivity.setColorFilter(ColorFilterGenerator.adjustColor((editImageModel_tmp.getBrightnessBGImage() - Utils.ColorFilterBaseValue),
                (editImageModel_tmp.getContrastBGImage() - Utils.ColorFilterBaseValue), (editImageModel_tmp.getSaturationBGImage() - Utils.ColorFilterBaseValue), 0));


        //ColorMatrix matrix = new ColorMatrix();

        /*if (editImageModel_main.isBlackAndWhite()) {
            matrix.setSaturation(0);
        } else {
            matrix.setSaturation(1);
        }*/

        if(editImageModel_tmp.isBlackAndWhite()){
            imgMainActivity.setColorFilter(ColorFilterGenerator.adjustColor((editImageModel_tmp.getBrightnessBGImage() - Utils.ColorFilterBaseValue),
                    (editImageModel_tmp.getContrastBGImage() - Utils.ColorFilterBaseValue), -Utils.ColorFilterBaseValue, 0));
        }else{
            imgMainActivity.setColorFilter(ColorFilterGenerator.adjustColor((editImageModel_tmp.getBrightnessBGImage() - Utils.ColorFilterBaseValue),
                    (editImageModel_tmp.getContrastBGImage() - Utils.ColorFilterBaseValue), (editImageModel_tmp.getSaturationBGImage() - Utils.ColorFilterBaseValue), 0));
        }


        //imgMainActivity.setColorFilter(new ColorMatrixColorFilter(matrix));


        imgMainActivity.setRotation(editImageModel_tmp.getRotate());
    }

    public void setTempFilterToEditor(){

        //imgMainActivity.setBackgroundResource(android.R.color.transparent);
        //imgMainActivity.setImageBitmap(null);

        //setEditImageValueTemp();

        //Log.v("fsdfsd","..dsfasdf...");

        new filterAsyncTemp().execute();

        /*for(FilterModel filterModel : filterList){
            if(filterModel.isSelected() || filterModel.isSaved()){
                Log.v("async...","...filter...inside.."+filterModel.getId() + "...selected.." + filterModel.isSelected() + "...size..." + filterList.size());
                Bitmap filterBitmap = BitmapFactory.decodeResource(getResources(), filterModel.getFilterDrawableID());

            }
        }*/


    }



    public class filterAsyncTemp extends AsyncTask<Bitmap, Void, Bitmap> {

       /* List<FilterModel> filterlistTemp;

        public filterAsyncTemp(List<FilterModel> filterlistTemp){
            this.filterlistTemp = filterlistTemp;
        }*/

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //setEditImageValueTemp();

            /*imgMainActivity.setDrawingCacheEnabled(true);
            imgMainActivity.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            imgMainActivity.layout(0, 0,
                    imgMainActivity.getWidth(), imgMainActivity.getHeight());
            imgMainActivity.buildDrawingCache(true);
            result = Bitmap.createScaledBitmap(imgMainActivity.getDrawingCache(), imgMainActivity.getWidth(), imgMainActivity.getHeight(), false);
            imgMainActivity.setDrawingCacheEnabled(false);*/

        }

        //Bitmap result = ((BitmapDrawable)imgMainActivity.getDrawable()).getBitmap();

        @Override
        protected Bitmap doInBackground(Bitmap... bitmaps) {


            // -------------------------------

            //Bitmap result = Utils.loadBitmapFromView(imgMainActivity);



            Bitmap result = Utils.ImageCopy.copy(Bitmap.Config.ARGB_8888, true);

            Matrix matrix = new Matrix();
            matrix.postRotate(editImageModel_tmp.getRotate());
            result = Bitmap.createBitmap(result, 0, 0, result.getWidth(), result.getHeight(), matrix, true);


            Canvas c = new Canvas();
            Paint p = new Paint();
            //c.drawRect(0, 0, result.getWidth(), result.getHeight(), p);
            //c.setBitmap(result);
            //c.drawBitmap(result, 0, 0, null);


            p.setColorFilter(ColorFilterGenerator.adjustColor((editImageModel_tmp.getBrightnessBGImage() - Utils.ColorFilterBaseValue),
                    (editImageModel_tmp.getContrastBGImage() - Utils.ColorFilterBaseValue), (editImageModel_tmp.getSaturationBGImage() - Utils.ColorFilterBaseValue), 0));


            if(editImageModel_tmp.isBlackAndWhite()){
                p.setColorFilter(ColorFilterGenerator.adjustColor((editImageModel_tmp.getBrightnessBGImage() - Utils.ColorFilterBaseValue),
                        (editImageModel_tmp.getContrastBGImage() - Utils.ColorFilterBaseValue), -Utils.ColorFilterBaseValue, 0));
            }else{
                p.setColorFilter(ColorFilterGenerator.adjustColor((editImageModel_tmp.getBrightnessBGImage() - Utils.ColorFilterBaseValue),
                        (editImageModel_tmp.getContrastBGImage() - Utils.ColorFilterBaseValue), (editImageModel_tmp.getSaturationBGImage() - Utils.ColorFilterBaseValue), 0));
            }

           //c.rotate(90, (result.getWidth() / 2), (result.getHeight() / 2));


           //c.drawBitmap(result, 0, 0, p);


           // c.restore();

//            Matrix matrix = new Matrix();
//            matrix.postRotate(editImageModel_tmp.getRotate());
//            result = Bitmap.createBitmap(result, 0, 0, result.getWidth(), result.getHeight(), matrix, true);

            c.setBitmap(result);
            c.drawBitmap(result, 0, 0, p);


            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.OVERLAY));//


            for(FilterModel filterModel : filterList){
                //Bitmap mainBitmap = Utils.ImageCopy;

                //FilterModel filterModel = filterListTemp.get(index);

                if(filterModel.isSaved() || filterModel.isSelected()) {

                    //Log.v("filterModel...", "..Temp..id.." + filterModel.getId() + "..LR.." + filterModel.isLR());

                    Bitmap filterBitmap = BitmapFactory.decodeResource(getResources(), filterModel.getFilterDrawableID());
                    filterBitmap = Bitmap.createScaledBitmap(filterBitmap, result.getWidth(), result.getHeight(), false);

                    ColorMatrix ma = new ColorMatrix();
                    ma.setSaturation(1);
                    p.setColorFilter(new ColorMatrixColorFilter(ma));

                    //p.setShader(new BitmapShader(Bitmap.createScaledBitmap(filterBitmap, result.getWidth(), result.getHeight(), false), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                    p.setAlpha(filterModel.getAlpha());

                    c.save();

                    if (filterModel.isLR()) {
                        c.scale(-1, 1, (result.getWidth() / 2), result.getHeight() / 2);    // flip
                    } else {
                        c.scale(1, 1, (result.getWidth() / 2), result.getHeight() / 2);    // flip
                    }


                    c.rotate(filterModel.getRotate(), (result.getWidth() / 2), result.getHeight() / 2);    // rotate
                    //c.drawRect(0, 0, result.getWidth(), result.getHeight(), p);
                    c.drawBitmap(filterBitmap, 0, 0, p);


                    c.restore();

                }

            }

            return result;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            //Utils.ImageCopy = bitmap.copy(Bitmap.Config.ARGB_8888, true);
           // setEditImageValueTemp();
            imgMainActivity.setImageBitmap(bitmap);
            imgMainActivity.invalidate();
            Log.v("async...","...onPostExecute..");
        }
    }

    public void setMainFilterToEditor(){

        new filterAsyncMain().execute();

    }

    public class filterAsyncMain extends AsyncTask<Bitmap, Void, Bitmap> {

        List<FilterModel> listMain;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            listMain = new ArrayList<>();
            listMain = Utils.getFilterFromPref(MainActivity.this);
        }

        @Override
        protected Bitmap doInBackground(Bitmap... bitmaps) {


            // -------------------------------
            Bitmap result = Utils.ImageCopy.copy(Bitmap.Config.ARGB_8888, true);

            Matrix matrix = new Matrix();
            matrix.postRotate(editImageModel_main.getRotate());
            result = Bitmap.createBitmap(result, 0, 0, result.getWidth(), result.getHeight(), matrix, true);

            Canvas c = new Canvas();
            Paint p = new Paint();
            //c.setBitmap(result);
            //c.drawBitmap(result, 0, 0, null);

            p.setColorFilter(ColorFilterGenerator.adjustColor((editImageModel_main.getBrightnessBGImage() - Utils.ColorFilterBaseValue),
                    (editImageModel_main.getContrastBGImage() - Utils.ColorFilterBaseValue), (editImageModel_main.getSaturationBGImage() - Utils.ColorFilterBaseValue), 0));


            if(editImageModel_main.isBlackAndWhite()){
                p.setColorFilter(ColorFilterGenerator.adjustColor((editImageModel_main.getBrightnessBGImage() - Utils.ColorFilterBaseValue),
                        (editImageModel_main.getContrastBGImage() - Utils.ColorFilterBaseValue), -Utils.ColorFilterBaseValue, 0));
            }else{
                p.setColorFilter(ColorFilterGenerator.adjustColor((editImageModel_main.getBrightnessBGImage() - Utils.ColorFilterBaseValue),
                        (editImageModel_main.getContrastBGImage() - Utils.ColorFilterBaseValue), (editImageModel_main.getSaturationBGImage() - Utils.ColorFilterBaseValue), 0));
            }

            //c.rotate(90, (result.getWidth() / 2), (result.getHeight() / 2));


            //c.drawBitmap(result, 0, 0, p);


            // c.restore();



            c.setBitmap(result);
            c.drawBitmap(result, 0, 0, p);



            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.OVERLAY));//

            for(FilterModel filterModel : listMain){
                //Bitmap mainBitmap = Utils.ImageCopy;

                //FilterModel filterModel = listMain.get(index);
                if(filterModel.isSaved()) {

                    //Log.v("filterModel...", "...id.." + filterModel.getId());

                    Bitmap filterBitmap = BitmapFactory.decodeResource(getResources(), filterModel.getFilterDrawableID());

                    p.setShader(new BitmapShader(Bitmap.createScaledBitmap(filterBitmap, result.getWidth(), result.getHeight(), false), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                    p.setAlpha(filterModel.getAlpha());

                    if (filterModel.isLR()) {
                        c.scale(-1, 1, (result.getWidth() / 2), result.getHeight() / 2);    // flip
                    } else {
                        c.scale(1, 1, (result.getWidth() / 2), result.getHeight() / 2);    // flip
                    }

                    c.save();
                    c.rotate(filterModel.getRotate(), (result.getWidth() / 2), result.getHeight() / 2);    // rotate
                    c.drawRect(0, 0, result.getWidth(), result.getHeight(), p);
                    c.restore();

                }

            }



            //c.save();
            //c.rotate(90, (mainBitmap.getWidth() / 2), mainBitmap.getHeight() / 2);    // rotate
            //c.scale(-1, 1, (mainBitmap.getWidth() / 2), mainBitmap.getHeight() / 2);    // flip
            //c.drawRect(0, 0, mainBitmap.getWidth(), mainBitmap.getHeight(), p); -----------------------> Main line
            //c.restore();
            // -------------------------------

            /*if (filterBitmap != null && !filterBitmap.isRecycled()) {
                filterBitmap.recycle();
                filterBitmap = null;
            }
            if (mainBitmap != null && !mainBitmap.isRecycled()) {
                mainBitmap.recycle();
                mainBitmap = null;
            }*/

            return result;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            //Utils.ImageCopy = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            //setEditImageValueTemp();
            imgMainActivity.setImageBitmap(bitmap);
            imgMainActivity.invalidate();
            Log.v("async...","...onPostExecute..");
        }
    }

    public void setEditImageView(){

        Gson gson= new Gson();
        String tmp = gson.toJson(editImageModel_main);
        editImageModel_tmp = gson.fromJson(tmp,EditImageModel.class);

        imgBrightnessEditImage.requestFocus();
        seekbarEditImage.setProgress(editImageModel_tmp.getBrightnessBGImage());

    }

    public void filterApplyRemoveText(){
        if(filterList.get(lastSelectFilterPosition).isSaved()){
            ((TextView)findViewById(R.id.tv_filter_save)).setText(getString(R.string.remove));
            //((TextView)findViewById(R.id.tv_filter_edit_apply)).setText(getString(R.string.remove));
        }else{
            ((TextView)findViewById(R.id.tv_filter_save)).setText(getString(R.string.save_filter));
            //((TextView)findViewById(R.id.tv_filter_edit_apply)).setText(getString(R.string.apply));
        }
    }



    public void changeBGPhoto(boolean isFromHome){

        final CharSequence[] options;

        if(isFromHome){
            options = new CharSequence[]{ getString(R.string.our_cards), getString(R.string.your_photos), getString(R.string.start_over_msg), getString(R.string.cancel) };
        }else {
            options = new CharSequence[]{ getString(R.string.our_cards), getString(R.string.your_photos), getString(R.string.cancel) };
        }



        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);

        builder.cancelable(false);
        builder.title(getString(R.string.change_bg_photo));
        builder.content(getString(R.string.pls_select_new_photo_option));

        builder.items(options);
        builder.itemsCallback(new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                if (text.equals(getString(R.string.our_cards))) {

                    From_BG_Change = true;
                    startActivity(new Intent(MainActivity.this, CategoriesActivity.class));

                } else if (text.equals(getString(R.string.your_photos))) {

                    selectImage();

                } else if (text.equals(getString(R.string.start_over_msg))) {

                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    //Runtime.getRuntime().gc();


                } else if (text.equals(getString(R.string.cancel))) {

                    From_BG_Change = false;
                    dialog.dismiss();

                }
            }
        });

        builder.show();
    }

    public void selectImage() {

        final CharSequence[] options = { getString(R.string.album), getString(R.string.camera), getString(R.string.cancel) };


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(false);

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

                    Crop.pickImage(MainActivity.this);
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
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) { // selectImage() ---> Edit Image
            beginCrop(data.getData());
        } else if (requestCode == Crop.REQUEST_CROP) { // selectImage() ---> Edit Image
            handleCrop(resultCode, data);
        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) { // selectImage() ---> Edit Image
            beginCrop(Uri.fromFile(new File(img_path)));
        }
        else if(requestCode == ALBUM_ADD_PHOTO && resultCode == RESULT_OK){ // addPhoto() ---> Add Photo
            UCrop.of(data.getData(), Uri.fromFile(new File(getCacheDir(), "tmp_cropped")))
                 .start(this);
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) { // addPhoto() ---> Add Photo

            final Uri resultUri = UCrop.getOutput(data);

            try {
                Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(resultUri));

                addImageview(bitmap, "", new byte[0]);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } else if (requestCode == CAMERA_REQUEST_ADD_PHOTO && resultCode == RESULT_OK) { // addPhoto() ---> Add Photo
            UCrop.of(Uri.fromFile(new File(img_path)), Uri.fromFile(new File(getCacheDir(), "tmp_cropped")))
                    .start(this);
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

                changeBGAllowDialog(bitmap, false);

                Log.v("result....","...handleCrop");

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


    public void changeBGAllowDialog(final Bitmap bitmap_new, final boolean cardSelect)
    {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
        builder.title(getString(R.string.change_bg_photo));
        builder.titleGravity(GravityEnum.CENTER);
        builder.content(getString(R.string.want_change_bg_photo));
        builder.cancelable(false);
        builder.positiveText(getString(R.string.allow));
        builder.positiveColor(Color.parseColor("#303F9F"));
        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog dialog, DialogAction which) {

                //Utils.isCardSelected = false;
                Utils.isCardSelected = cardSelect;

                Utils.ImageMain = bitmap_new;

                Utils.ImageCopy = Utils.ImageMain.copy(Bitmap.Config.ARGB_8888, true);

                setMainFilterToEditor();

                if(Utils.isCardSelected) {
                    img_app_logo.setVisibility(View.GONE);
                }else {
                    img_app_logo.setVisibility(View.VISIBLE);
                    setWaterMarkBasedOnBgColor();
                }

                dialog.dismiss();
            }
        });
        builder.negativeText(getString(R.string.cancel));
        builder.negativeColor(Color.parseColor("#303F9F"));
        builder.onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog dialog, DialogAction which) {

                Utils.ImageMain = Utils.ImageCopy;

                setMainFilterToEditor();

                dialog.dismiss();
            }
        });
        builder.show();
    }

    public TextArtEditorModel getDeafultTextArtEditor(String tagValue){

        TextArtEditorModel textArtEditorModel = new TextArtEditorModel();
        textArtEditorModel.setTag(tagValue);
        textArtEditorModel.setFontTypeface(null);
        textArtEditorModel.setFontPosition(-1);
        textArtEditorModel.setAlign("c");
        textArtEditorModel.setAlignPrevious("c");
        textArtEditorModel.setVerticalAlign(false);
        textArtEditorModel.setHorizontalSpacing(true);
        textArtEditorModel.setVerticalSpacing(false);
        textArtEditorModel.setHorizontalSpace(20);
        textArtEditorModel.setVerticalSpace(20);
        textArtEditorModel.setPositionY(0);
        textArtEditorModel.setTextColor("");
        textArtEditorModel.setHighlightColor("");
        textArtEditorModel.setSize(20);
        textArtEditorModel.setRotate(0);
        textArtEditorModel.setTransparency(255);
        textArtEditorModel.setLR(false);
        textArtEditorModel.setUD(false);

        return textArtEditorModel;
    }

    public TextArtEditorModel getTextArtEditorFromTag(String tagValue){

        TextArtEditorModel textArtEditorModel = new TextArtEditorModel();

        for(TextArtEditorModel model : textArtEditorList){
            if(model.getTag().equalsIgnoreCase(tagValue)){
                textArtEditorModel = model;
                break;
            }
        }

        return textArtEditorModel;
    }

    public void setTextArtEditorValue(TextArtEditorModel textArtEditorModel){

        if(textArtEditorModel.getAlign().equalsIgnoreCase("l")){
            imgLeftAling.requestFocus();
        } else if(textArtEditorModel.getAlign().equalsIgnoreCase("c")){
            imgCenterAling.requestFocus();
        }else{
            imgRightAling.requestFocus();
        }

        if(textArtEditorModel.isVerticalAlign()){
            imgVerticalAling.setSelected(true);
        }else {
            imgVerticalAling.setSelected(false);
        }

        if(imgCSpaceAling.isSelected()){
            seekbarSpaceAling.setProgress(textArtEditorModel.getHorizontalSpace());
        }else{
            seekbarSpaceAling.setProgress(textArtEditorModel.getVerticalSpace());
        }

        seekbarTextSize.setProgress(textArtEditorModel.getSize());

        seekbarManualRotate.setProgress(textArtEditorModel.getRotate());

        seekbarTextTrans.setProgress(textArtEditorModel.getTransparency());




    }

    public void setViewTextArt(final TextArtEditorModel textArtEditorModel){

        String tag_str = textArtEditorModel.getTag();

        //Log.v("setViewTextArt....","...tag_str..." + tag_str + "...childCount.." + frame_container.getChildCount());
        if(tag_str == null){
            return;
        }

        if(tag_str.contains("i")){
            ImageView imageView = (ImageView) frame_container.findViewWithTag(tag_str);
            //Log.v("setViewTextArt....","...imageView..." + (frame_container.findViewWithTag(tag_str)==null));

            if(imageView == null){
                return;
            }

            int imageSize = Utils.getSizefromDp(this, (textArtEditorModel.getSize()*200) / 20);

            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) imageView.getLayoutParams();
            layoutParams.width = imageSize;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            imageView.setLayoutParams(layoutParams);
            imageView.setAdjustViewBounds(true);

            imageView.setImageAlpha(textArtEditorModel.getTransparency());
            imageView.setRotation(textArtEditorModel.getRotate());

            if(textArtEditorModel.isLR()){
                imageView.setScaleX(-1);
            }else{
                imageView.setScaleX(1);
            }

            if(textArtEditorModel.isUD()){
                imageView.setScaleY(-1);
            }else{
                imageView.setScaleY(1);
            }
        }
        else if(tag_str.contains("e")){
            final EditText editText = (EditText) frame_container.findViewWithTag(tag_str);
            //editText.setBackgroundResource(android.R.color.transparent);
            editText.setTextSize(textArtEditorModel.getSize());

            if(textArtEditorModel.getFontTypeface() != null) {
                editText.setTypeface(textArtEditorModel.getFontTypeface());
            }else{
                editText.setTypeface(Typeface.DEFAULT);
            }

            float alphaValue = (float) textArtEditorModel.getTransparency() / 255;
            editText.setAlpha(alphaValue);
            editText.setRotation(textArtEditorModel.getRotate());

            if(textArtEditorModel.isLR()){
                editText.setScaleX(-1);
            }else{
                editText.setScaleX(1);
            }

            if(textArtEditorModel.isUD()){
                editText.setScaleY(-1);
            }else{
                editText.setScaleY(1);
            }


            // ----- set Alignment of Text -------
            if(textArtEditorModel.getAlign().equalsIgnoreCase("l")){
                /*if(textArtEditorModel.getAlignPrevious().equalsIgnoreCase("c")){
                    editText.setX(editText.getX() - 20);
                }else if(textArtEditorModel.getAlignPrevious().equalsIgnoreCase("r")){
                    editText.setX(editText.getX() - 40);
                }else{
                    editText.setX(editText.getX());
                }*/
                editText.setPadding(0,0,Utils.getSizefromDp(this, 20),0);
                editText.setGravity(Gravity.LEFT);
                textArtEditorModel.setAlignPrevious("l");

            }else if(textArtEditorModel.getAlign().equalsIgnoreCase("c")){
                /*if(textArtEditorModel.getAlignPrevious().equalsIgnoreCase("l")){
                    editText.setX(editText.getX() + 20);
                }else if(textArtEditorModel.getAlignPrevious().equalsIgnoreCase("r")){
                    editText.setX(editText.getX() - 20);
                }else{
                    editText.setX(editText.getX());
                }*/
                editText.setPadding(Utils.getSizefromDp(this, 10),0,Utils.getSizefromDp(this, 10),0);
                editText.setGravity(Gravity.CENTER);
                textArtEditorModel.setAlignPrevious("c");

            }else if(textArtEditorModel.getAlign().equalsIgnoreCase("r")){
               /* if(textArtEditorModel.getAlignPrevious().equalsIgnoreCase("c")){
                    editText.setX(editText.getX() + 20);
                }else if(textArtEditorModel.getAlignPrevious().equalsIgnoreCase("l")){
                    editText.setX(editText.getX() + 40);
                }else{
                    editText.setX(editText.getX());
                }*/
                editText.setPadding(Utils.getSizefromDp(this, 20),0,0,0);
                editText.setGravity(Gravity.RIGHT);
                textArtEditorModel.setAlignPrevious("r");
            }
            // -----------------------------------------------

            Log.v("editText.LayoutParams..","...." + editText.getWidth());

            // -------- Text Vetrical Align -------------
            if(textArtEditorModel.isVerticalAlign()){
                String text = editText.getText().toString();
                text = text.replace("\n",""); // removing line break
                String output = "";
                for(int i = 0; i < text.length(); i++){
                    if(i == 0){
                        output = output + text.charAt(i);
                    }else{
                        output = output + "\n" + text.charAt(i);
                    }
                }
                Log.v("vertical..","...." + output);
                editText.setLineSpacing(1, 1);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    editText.setLetterSpacing(0);
                }
                editText.setText(output);
                FrameLayout.LayoutParams tmp_layoutparam = (FrameLayout.LayoutParams) editText.getLayoutParams();
                tmp_layoutparam.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                tmp_layoutparam.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                Log.v("tmp_layoutparam..","..if.." + tmp_layoutparam.width);
                editText.setLayoutParams(tmp_layoutparam);
            }else {
                String output = editText.getText().toString();
                int total_words = output.length();
                int counter_n = output.split("\\n").length - 1;
                if(((total_words - 1) / 2) == counter_n){ // check if came from Vertical align
                    output = output.replace("\n","");
                }
                //output = output.replace("\n","");
                editText.setText(output);
                FrameLayout.LayoutParams tmp_layoutparam = (FrameLayout.LayoutParams) editText.getLayoutParams();
                tmp_layoutparam.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                tmp_layoutparam.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                Log.v("tmp_layoutparam..","..else.." + tmp_layoutparam.width);
                editText.setLayoutParams(tmp_layoutparam);
            }
            // -------------------------------------------

            if(textArtEditorModel.isVerticalAlign()){
                if(imgCSpaceAling.isSelected()){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        //float spaceValue = ((float) textArtEditorModel.getHorizontalSpace() / 50) - 0.4f;
                        float spaceValue = ((float) textArtEditorModel.getHorizontalSpace() / 60) - 0.33f;
                        editText.setLetterSpacing(spaceValue);
                    }
                }else{
                    float lineSpace = (float) (textArtEditorModel.getVerticalSpace()) - 20.0f;
                    Log.v("lineSpace....","...."+ lineSpace );
                    editText.setLineSpacing(lineSpace, 0.8f);
                }
            }else{
                if(imgCSpaceAling.isSelected()){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        //float spaceValue = ((float) textArtEditorModel.getHorizontalSpace() / 50) - 0.4f;
                        float spaceValue = ((float) textArtEditorModel.getHorizontalSpace() / 60) - 0.33f;
                        editText.setLetterSpacing(spaceValue);
                    }
                }else{
                    float lineSpace = (float) (textArtEditorModel.getVerticalSpace()) - 20.0f;
                    Log.v("lineSpace....","...."+ lineSpace );
                    editText.setLineSpacing(lineSpace, 0.8f);
                    /*int setYValue = textArtEditorModel.getVerticalSpace() - 20;
                    editText.setY(textArtEditorModel.getPositionY() + setYValue);*/
                }
            }



            // ----- set Text Color --------
            if(!textArtEditorModel.getTextColor().isEmpty()){
                int image_id = Utils.getDrawableResIDFromName(this, colorsArray[Integer.parseInt(textArtEditorModel.getTextColor())]);

                Bitmap result = BitmapFactory.decodeResource(getResources(), image_id);

                Utils.setBitmapAsTextColor(editText, result);

            }else{
                editText.setTextColor(Color.BLACK);
            }
            // -------------------------------

            // ----- set Text Highlight Color --------
            if(!textArtEditorModel.getHighlightColor().isEmpty()){
                if(textArtEditorModel.getHighlightColor().equalsIgnoreCase("0")){
                    editText.setBackgroundResource(android.R.color.transparent);
                }else{

                    final int image_id = Utils.getDrawableResIDFromName(this, colorsArray[Integer.parseInt(textArtEditorModel.getHighlightColor())]);

                    Rect bounds = Utils.getTextHeightWidth(editText);

                    int text_height =  bounds.height();
                    int text_width = bounds.width();

                    Bitmap result = BitmapFactory.decodeResource(getResources(), image_id);
                    result = Bitmap.createScaledBitmap(result, text_width, text_height, false);

                    //Log.v("edittext...","..getWidth.." + text_width + "...height.." + text_height + "...position.." + textArtEditorModel.getHighlightColor());
                    //Log.v("Bitmap...","..getWidth.." + result.getWidth() + "...height.." + result.getHeight());

                    editText.setBackground(new BitmapDrawable(getResources(), result));

                    /*final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 100ms


                        }
                    }, 50);
*/


                }
            }else {
                editText.setBackgroundResource(android.R.color.transparent);
            }
            // ------------------------------------

        }
    }

    public void viewTextArtClick(View view, String strTag){
        //Log.v("onclick....", "...strTag..." + strTag);

        /*if(view.getTag().toString().contains("com.katemitchell")){
            return;
        }*/

            /* Text/Art click disable while apply filter or edit image*/
        if((linearFilterSaveExit.getVisibility() == View.VISIBLE) || (linearEditImage.getVisibility() == View.VISIBLE)){
            return;
        }

        lastSelectTagIdTextArt = strTag;


        TextArtEditorModel textArtEditorModel = getTextArtEditorFromTag(lastSelectTagIdTextArt);
        textArtEditorModel.setPositionY(view.getY());

        Utils.setTmpTextArtModelToPref(this, textArtEditorModel);

        setTextArtEditorValue(textArtEditorModel);


        hideAllHeader();
        textEditorToolLayout.setVisibility(View.VISIBLE);
        imgQuickTips.setVisibility(View.VISIBLE);

        relativeRecyclerArt.setVisibility(View.GONE);

        linearTextEditor.setVisibility(View.VISIBLE);
        imgSizeTextEditor.requestFocus();
        imgSizeTextEditor.performClick();

    }

    public void removeTextArtEditorListFromTag(String tagValue){

        for(TextArtEditorModel model : textArtEditorList){
            if(model.getTag().equalsIgnoreCase(tagValue)){
                textArtEditorList.remove(model);
                break;
            }
        }

    }

    public void setCustomFont(Typeface customFont, int position){
        TextArtEditorModel model = getTextArtEditorFromTag(lastSelectTagIdTextArt);
        model.setFontTypeface(customFont);
        model.setFontPosition(position);
        setViewTextArt(model);
    }

    public void setTextHighlightColor(String position){

        TextArtEditorModel model = getTextArtEditorFromTag(lastSelectTagIdTextArt);

        if(radiogroupColor.getCheckedRadioButtonId() == radioTextColor.getId()){
            model.setTextColor(position);
        }else{
            model.setHighlightColor(position);
        }

        setViewTextArt(model);
    }


    public void AddPhoto() {

        final CharSequence[] options = { getString(R.string.album), getString(R.string.camera), getString(R.string.cancel) };


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(false);

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
                    startActivityForResult(intent, CAMERA_REQUEST_ADD_PHOTO);

                } else if (options[item].equals(getString(R.string.album))) {

                    Crop.pickImage(MainActivity.this, ALBUM_ADD_PHOTO);
                    //Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    //startActivityForResult(intent, RESULT_LOAD_IMAGE);
                } else if (options[item].equals(getString(R.string.cancel))) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }

    public void addArtImageToRecent(ArtImageLocalModel artImageLocalModel){

        RecentLocalModel recentLocalModel = new RecentLocalModel();
        recentLocalModel.setCatId(artImageLocalModel.getCatId());
        if(artImageLocalModel.getStatus().equalsIgnoreCase("0")){
            recentLocalModel.setCatImage(artImageLocalModel.getImageUrl());
        }else{
            recentLocalModel.setCatImage(artImageLocalModel.getCatImage());
        }
        recentLocalModel.setStatus(artImageLocalModel.getStatus());
        recentLocalModel.setImageId(artImageLocalModel.getImageId());


        gson = new Gson();
        String jsonOutput = Utils.cur2JsonArray(databaseHandler.getAllDataFromTable(DatabaseHandler.RECENT_TABLE)).toString();
        Type listType = new TypeToken<List<RecentLocalModel>>() {
        }.getType();
        List<RecentLocalModel> lists = gson.fromJson(jsonOutput, listType);

        List<String> recentImageIdList = new ArrayList<>();

        for(RecentLocalModel model : lists){
            recentImageIdList.add(model.getImageId());
        }

        if(recentImageIdList.contains(artImageLocalModel.getImageId())){
            // update or delete
            databaseHandler.deleteFromRecentTable(artImageLocalModel.getImageId());
            databaseHandler.insertRecentTable(recentLocalModel);
        }else{
            databaseHandler.insertRecentTable(recentLocalModel); // insert
        }


    }

    public List<String> getCatIdFromInAppTable(){

        List<String> catIdPurchased = new ArrayList<>();

        gson = new Gson();
        String jsonOutput = Utils.cur2JsonArray(databaseHandler.getAllDataFromTable(DatabaseHandler.ARTINAPP_TABLE)).toString();
        Type listType = new TypeToken<List<ArtInAppLocalModel>>() {}.getType();
        List<ArtInAppLocalModel> lists = gson.fromJson(jsonOutput, listType);

        for (ArtInAppLocalModel artInAppLocalModel : lists){
            catIdPurchased.add(artInAppLocalModel.getCatId());
        }

        return catIdPurchased;
    }

    public String getTryFromCatID(String cat_id){
        String try_str = "";

        gson = new Gson();
        Cursor cursor = databaseHandler.getArtTryImageFromCatId(cat_id);

        while (cursor.moveToNext()) {
            try_str = cursor.getString(0);
        }

        return try_str;
    }

    public void updateTryArtImage(String cat_id, String try_value){
        databaseHandler.updateArtTableTry(cat_id, try_value);
    }

    public String getInAppIdFromCatID(String cat_id){
        String inAppId = "";

        gson = new Gson();
        Cursor cursor = databaseHandler.getArtInAppIdFromCatId(cat_id);

        while (cursor.moveToNext()) {
            inAppId = cursor.getString(0);
        }

        return inAppId;
    }

    public void artQuickTipsDialog(){

        View view = getLayoutInflater().inflate(R.layout.art_quicktips_dialog_layout, null);
        final Dialog dialog_art_quick_tips = new Dialog(this);
        int width = (int) (Utils.getScreenWidth(this) * 0.8);
        dialog_art_quick_tips.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_art_quick_tips.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog_art_quick_tips.setContentView(view);
        dialog_art_quick_tips.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog_art_quick_tips.setCancelable(false);

        Typeface custFont = Typeface.createFromAsset(getAssets(), "fonts/"+"Quicksand_Book.otf");

        TextView tv_2_art_quicktips = (TextView) view.findViewById(R.id.tv_2_art_quicktips);
        TextView tv_5_art_quicktips = (TextView) view.findViewById(R.id.tv_5_art_quicktips);
        TextView tv_6_art_quicktips = (TextView) view.findViewById(R.id.tv_6_art_quicktips);

        tv_2_art_quicktips.setTypeface(custFont);
        tv_5_art_quicktips.setTypeface(custFont);
        tv_6_art_quicktips.setTypeface(custFont);

        tv_5_art_quicktips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.setStringPreference(MainActivity.this, Utils.Art_QuickTips_DontShow_key, "true");
                dialog_art_quick_tips.dismiss();
            }
        });
        tv_6_art_quicktips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_art_quick_tips.dismiss();
            }
        });

        dialog_art_quick_tips.show();
    }

    public void removeAllFiltersWarningDialog(){

        View view = getLayoutInflater().inflate(R.layout.remove_all_filters_dialog_layout, null);
        final Dialog dialog_removeAll_filter = new Dialog(this);
        int width = (int) (Utils.getScreenWidth(this) * 0.8);
        dialog_removeAll_filter.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_removeAll_filter.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog_removeAll_filter.setContentView(view);
        dialog_removeAll_filter.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog_removeAll_filter.setCancelable(false);

        Typeface custFont = Typeface.createFromAsset(getAssets(), "fonts/"+"Quicksand_Book.otf");

        TextView tv_1 = (TextView) view.findViewById(R.id.tv_1);
        TextView tv_3 = (TextView) view.findViewById(R.id.tv_3);
        TextView tv_4 = (TextView) view.findViewById(R.id.tv_4);

        tv_1.setTypeface(custFont);
        tv_3.setTypeface(custFont);
        tv_4.setTypeface(custFont);

        tv_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllFilterOriginal();
                dialog_removeAll_filter.dismiss();
            }
        });
        tv_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_removeAll_filter.dismiss();
            }
        });

        dialog_removeAll_filter.show();
    }

    public static void saveShareImage(final Context context){

        percent_relative_save.setPadding(0, 0, 0, 0);

        frame_middle.post(new Runnable() {
            @Override
            public void run() {

                Bitmap ground_Bitmap = Utils.loadBitmapFromViewBGWhite_Custom(frame_middle, context, screenWidth);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                ground_Bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                //final String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), ground_Bitmap, "test.jpg", null);

                OutputStream fOut = null;
                //String strDirectory = Environment.getExternalStorageDirectory().toString();
                String strDirectory = context.getExternalCacheDir().toString();
                String imgName = "Rainbow" + System.currentTimeMillis() + ".jpg";

                File f = new File(strDirectory, imgName);
                try {
                    fOut = new FileOutputStream(f);

                            /*Compress image*/
                    ground_Bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    fOut.flush();
                    fOut.close();

                            /*Update image to gallery*/
                    //MediaStore.Images.Media.insertImage(getContentResolver(), f.getAbsolutePath(), f.getName(), f.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Log.v("filePath....", "...." + f.getPath());

                Intent intent = new Intent(context, SaveShareActivity.class);
                intent.putExtra(Utils.ImageUri_key, f.getPath());
                context.startActivity(intent);

               // percent_relative_save.setPadding(0, Utils.getSizefromDp(context, 1), 0, Utils.getSizefromDp(context, 1));

                //openBottomSheetDialog();
            }
        });

    }


    public String isFilterPurchased(String filterInappId, ImageView imageView){

        String tmpPurchasedId = "";

        imageView.setVisibility(View.GONE);

        if(!Utils.getStringPreference(this, Utils.UnlockallFilter_key, "").equalsIgnoreCase(Utils.Purchased)){
            if(filterInappId.contains("eclipse")){
                if(!Utils.getStringPreference(this, Utils.EclipseFilterInApp_key, "").equalsIgnoreCase(Utils.Purchased)){
                    imageView.setVisibility(View.VISIBLE);
                    tmpPurchasedId = this.getString(R.string.filter_eclipse_inapp_id);
                    Log.v("tmpPurchasedId...","....." + tmpPurchasedId );
                }
            }else if(filterInappId.contains("lite")){
                if(!Utils.getStringPreference(this, Utils.LiteFilterInApp_key, "").equalsIgnoreCase(Utils.Purchased)){
                    imageView.setVisibility(View.VISIBLE);
                    tmpPurchasedId = this.getString(R.string.filter_lite_inapp_id);
                    Log.v("tmpPurchasedId...","....." + tmpPurchasedId );
                }
            }else if(filterInappId.contains("rainbow")){
                if(!Utils.getStringPreference(this, Utils.RainbowFilterInApp_key, "").equalsIgnoreCase(Utils.Purchased)){
                    imageView.setVisibility(View.VISIBLE);
                    tmpPurchasedId = this.getString(R.string.filter_rainbow_inapp_id);
                    Log.v("tmpPurchasedId...","....." + tmpPurchasedId );
                }
            }else if(filterInappId.contains("moon")){
                if(!Utils.getStringPreference(this, Utils.MoonFilterInApp_key, "").equalsIgnoreCase(Utils.Purchased)){
                    imageView.setVisibility(View.VISIBLE);
                    tmpPurchasedId = this.getString(R.string.filter_moon_inapp_id);
                }
            }else if(filterInappId.contains("dreamy")){
                if(!Utils.getStringPreference(this, Utils.DreamyFilterInApp_key, "").equalsIgnoreCase(Utils.Purchased)){
                    imageView.setVisibility(View.VISIBLE);
                    tmpPurchasedId = this.getString(R.string.filter_dreamy_inapp_id);
                }
            }else if(filterInappId.contains("floral")){
                if(!Utils.getStringPreference(this, Utils.FloralFilterInApp_key, "").equalsIgnoreCase(Utils.Purchased)){
                    imageView.setVisibility(View.VISIBLE);
                    tmpPurchasedId = this.getString(R.string.filter_floral_inapp_id);
                }
            }else if(filterInappId.contains("dusk")){
                if(!Utils.getStringPreference(this, Utils.DuskFilterInApp_key, "").equalsIgnoreCase(Utils.Purchased)){
                    imageView.setVisibility(View.VISIBLE);
                    tmpPurchasedId = this.getString(R.string.filter_dusk_inapp_id);
                }
            }
        }else {
            imageView.setVisibility(View.GONE);
            tmpPurchasedId = "";
        }

        // -------- check Individual filterId -----------------
        /*if(!Utils.getStringPreference(mContext, Utils.UnlockallFilter_key, "").equalsIgnoreCase(Utils.Purchased)){
            if(filterInappId.contains("rainbow")){
                if(Utils.getStringPreference(mContext, Utils.RainbowFilter_key, "").equalsIgnoreCase(Utils.Purchased) ||
                        Utils.getStringPreference(mContext, filterInappId, "").equalsIgnoreCase(Utils.Purchased)){
                    imageView.setVisibility(View.GONE);
                    tmpPurchased = true;
                }
            }else if(filterInappId.contains("moon")){
                if(Utils.getStringPreference(mContext, Utils.MoonFilter_key, "").equalsIgnoreCase(Utils.Purchased) ||
                        Utils.getStringPreference(mContext, filterInappId, "").equalsIgnoreCase(Utils.Purchased)){
                    imageView.setVisibility(View.GONE);
                    tmpPurchased = true;
                }
            }else if(filterInappId.contains("dreamy")){
                if(Utils.getStringPreference(mContext, Utils.DreamyFilter_key, "").equalsIgnoreCase(Utils.Purchased) ||
                        Utils.getStringPreference(mContext, filterInappId, "").equalsIgnoreCase(Utils.Purchased)){
                    imageView.setVisibility(View.GONE);
                    tmpPurchased = true;
                }
            }else if(filterInappId.contains("floral")){
                if(Utils.getStringPreference(mContext, Utils.FloralFilter_key, "").equalsIgnoreCase(Utils.Purchased) ||
                        Utils.getStringPreference(mContext, filterInappId, "").equalsIgnoreCase(Utils.Purchased)){
                    imageView.setVisibility(View.GONE);
                    tmpPurchased = true;
                }
            }else if(filterInappId.contains("dusk")){
                if(Utils.getStringPreference(mContext, Utils.DuskFilter_key, "").equalsIgnoreCase(Utils.Purchased) ||
                        Utils.getStringPreference(mContext, filterInappId, "").equalsIgnoreCase(Utils.Purchased)){
                    imageView.setVisibility(View.GONE);
                    tmpPurchased = true;
                }
            }
        }else {
            imageView.setVisibility(View.GONE);
            tmpPurchased = true;
        }*/

        return tmpPurchasedId;
    }

    public void setWaterMarkBasedOnBgColor(){

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                //Log.v("Utils.ImageCopy","...widht.." + Utils.ImageCopy.getWidth() + "..height.." + Utils.ImageCopy.getHeight() + ".....screenwidth.." + screenWidth);
                //Log.v("img_app_logo","...widht.." + img_app_logo.getWidth() + "..height.." + img_app_logo.getHeight());
                //Log.v("img_app_logo..Intrinsic","...widht.." + img_app_logo.getDrawable().getIntrinsicWidth() + "..height.." + img_app_logo.getDrawable().getIntrinsicHeight());
                //Log.v("imgMainActivity","...widht.." + imgMainActivity.getWidth() + "..height.." + imgMainActivity.getHeight());
                //Log.v("diff","...widht.." + (screenWidth - img_app_logo.getWidth()) + "..height.." + (Utils.ImageCopy.getHeight() - img_app_logo.getHeight()));

                try {

                    float ratio_width = (screenWidth / img_app_logo.getWidth());    // screen ratio
                    float result_width = Utils.ImageCopy.getWidth() - (Utils.ImageCopy.getWidth() / ratio_width);   // bitmap Widht - (bitmap ratio based on screen ratio)

                    float ratio_height = (screenWidth / img_app_logo.getHeight());
                    float result_height = Utils.ImageCopy.getHeight() - (Utils.ImageCopy.getHeight() / ratio_height);


                    //  Bitmap      , widht_start of Bitmap , height_start bitmap,
                    Bitmap getBGBitmap = Bitmap.createBitmap(Utils.ImageCopy, (int) result_width, (int) result_height, (int) (Utils.ImageCopy.getWidth() / ratio_width), (int) (Utils.ImageCopy.getHeight() / ratio_height));

                    //Log.v("averageColor..","..." + Utils.calculateAverageColor(getBGBitmap, 1));
                    //Log.v("isColorDark..","..." + Utils.isColorDark(Utils.calculateAverageColor(getBGBitmap, 1)));

                    if (Utils.isColorDark(Utils.calculateAverageColor(getBGBitmap, 1))) {
                        img_app_logo.setImageResource(R.drawable.rainbowlove_watermark_white);
                    } else {
                        img_app_logo.setImageResource(R.drawable.rainbowlove_watermark_black);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        };

        new Handler().postDelayed(runnable, 500);

    }

}
