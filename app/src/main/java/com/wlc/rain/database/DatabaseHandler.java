package com.rainbowloveapp.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MergeCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.rainbowloveapp.app.model.ArtImageLocalModel;
import com.rainbowloveapp.app.model.ArtInAppLocalModel;
import com.rainbowloveapp.app.model.ArtLocalModel;
import com.rainbowloveapp.app.model.CardImageLocalModel;
import com.rainbowloveapp.app.model.CardLocalModel;
import com.rainbowloveapp.app.model.EventLocalModel;
import com.rainbowloveapp.app.model.LaunchLocalModel;
import com.rainbowloveapp.app.model.LoginLocalModel;
import com.rainbowloveapp.app.model.ProfileLocalModel;
import com.rainbowloveapp.app.model.RecentColorLocalModel;
import com.rainbowloveapp.app.model.RecentLocalModel;

import java.util.ArrayList;
import java.util.List;


/**
 *  on 29/11/16.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "db_rainbowlove";


    private static final String ID = "id";

    // ------------------Table 1-----------------------------
    public static final String ART_TABLE = "tbl_Art";

    private static final String CAT_ID = "cat_id";
    private static final String CAT_IMAGE = "cat_image";
    private static final String CAT_INAPP_ID = "cat_inapp_id";
    private static final String MDATE = "mdate";
    private static final String STATUS = "status";
    private static final String IMAGE_URL = "image_url";
    private static final String CAT_POSSION = "cat_possion";
    private static final String CAT_TRY = "cat_try";

    // ------------------Table 2-----------------------------
    public static final String ARTINAPP_TABLE = "tbl_ArtInApp";


    // ------------------Table 3-----------------------------
    public static final String ARTIMAGE_TABLE = "tbl_Artimage";

    private static final String IMAGE_ID = "image_id";


    // ------------------Table 4-----------------------------
    public static final String CARD_TABLE = "tbl_Card";

    private static final String CARD_NAME = "card_name";


    // ------------------Table 5-----------------------------
    public static final String CARDIMAGE_TABLE = "tbl_Cardimage";

    private static final String CARD_IMAGE = "card_image";


    // ------------------Table 6-----------------------------
    public static final String PROFILE_TABLE = "tbl_Profile";

    private static final String USER_ID = "user_id";
    private static final String USER_FIRSTNAME = "user_firstname";
    private static final String USER_LASTNAME = "user_lastname";
    private static final String USER_EMAIL = "user_email";
    private static final String USER_DOB = "user_dob";
    private static final String PURCHASE_ART = "purchase_art";
    private static final String PURCHASE_LOGO = "purchase_logo";
    private static final String PURCHASE_ADDLAYER = "purchase_addlayer";
    private static final String PURCHASE_HD = "purchase_hd";


    // ------------------Table 7-----------------------------
    public static final String RECENT_TABLE = "tbl_Recent";


    // ------------------Table 8-----------------------------
    public static final String RECENTCOLOR_TABLE = "tbl_Recent_Color";

    private static final String COLORCODE = "color_code";


    // ------------------Table 9-----------------------------
    public static final String EVENT_TABLE = "tbl_event";

    private static final String EVENT_ID = "event_id";
    private static final String EVENT_NAME = "event_name";
    private static final String EVENT_DATE = "event_date";
    private static final String EVENT_YEAR = "event_year";
    private static final String EVENT_TIME = "event_time";
    private static final String EVENT_TYPE = "event_type";
    private static final String FB_USERID = "fb_userid";


    // ------------------Table 10-----------------------------
    public static final String LAUNCH_TABLE = "tbl_launch";

    private static final String LAUNCH = "launch";


    // ------------------Table 11-----------------------------
    public static final String LOGIN_TABLE = "tbl_login";

    private static final String LOG_ID = "log_id";
    private static final String USER_NAME = "user_name";
    private static final String USER_PASS = "user_pass";
    private static final String LOG_TYPE = "log_type";



    private static final String CREATE_ART_TABLE = "CREATE TABLE IF NOT EXISTS " + ART_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," +
            CAT_ID + " TEXT,"  + CAT_IMAGE + " TEXT," +  CAT_INAPP_ID + " TEXT," +  MDATE + " TEXT," +  STATUS + " TEXT," +
            IMAGE_URL + " TEXT," + CAT_POSSION + " TEXT," + CAT_TRY + " TEXT)";

    private static final String CREATE_ARTINAPP_TABLE = "CREATE TABLE IF NOT EXISTS " + ARTINAPP_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," +
            CAT_ID + " TEXT)";

    private static final String CREATE_ARTIMAGE_TABLE = "CREATE TABLE IF NOT EXISTS " + ARTIMAGE_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," +
            CAT_ID + " TEXT,"  + CAT_IMAGE + " TEXT," +  STATUS + " TEXT," +  IMAGE_ID + " TEXT," +  IMAGE_URL + " TEXT)";

    private static final String CREATE_CARD_TABLE = "CREATE TABLE IF NOT EXISTS " + CARD_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," +
            CAT_ID + " TEXT,"  + CARD_NAME + " TEXT," +  CAT_POSSION + " TEXT)";

    private static final String CREATE_CARDIMAGE_TABLE = "CREATE TABLE IF NOT EXISTS " + CARDIMAGE_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," +
            CAT_ID + " TEXT,"  + CARD_IMAGE + " TEXT," +  STATUS + " TEXT," +  IMAGE_ID + " TEXT," +  IMAGE_URL + " TEXT)";

    private static final String CREATE_PROFILE_TABLE = "CREATE TABLE IF NOT EXISTS " + PROFILE_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," +
            USER_ID + " TEXT,"  + USER_FIRSTNAME + " TEXT," +  USER_LASTNAME + " TEXT," +  USER_EMAIL + " TEXT," +  USER_DOB + " TEXT," +
            PURCHASE_ART + " TEXT," + PURCHASE_LOGO + " TEXT," + PURCHASE_ADDLAYER + " TEXT," + PURCHASE_HD + " TEXT)";

    private static final String CREATE_RECENT_TABLE = "CREATE TABLE IF NOT EXISTS " + RECENT_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," +
            CAT_ID + " TEXT,"  + CAT_IMAGE + " TEXT," +  STATUS + " TEXT," +  IMAGE_ID + " TEXT)";

    private static final String CREATE_RECENTCOLOR_TABLE = "CREATE TABLE IF NOT EXISTS " + RECENTCOLOR_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," +
            COLORCODE + " TEXT)";

    private static final String CREATE_EVENT_TABLE = "CREATE TABLE IF NOT EXISTS " + EVENT_TABLE + "(" + EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," +
            EVENT_NAME + " TEXT,"  + EVENT_DATE + " TEXT," +  EVENT_YEAR + " TEXT," +  EVENT_TIME + " TEXT," +  EVENT_TYPE + " TEXT," +
            FB_USERID + " TEXT)";

    private static final String CREATE_LAUNCH_TABLE = "CREATE TABLE IF NOT EXISTS " + LAUNCH_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," +
            LAUNCH + " TEXT)";

    private static final String CREATE_LOGIN_TABLE = "CREATE TABLE IF NOT EXISTS " + LOGIN_TABLE + "(" + LOG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," +
            USER_ID + " TEXT,"  + USER_NAME + " TEXT," +  USER_PASS + " TEXT," +  LOG_TYPE + " TEXT)";



    private static final String SELECT_SQL = "SELECT * FROM ";
    private static final String DELETE_ALL_ROW_SQL = "DELETE FROM ";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_ART_TABLE);
        sqLiteDatabase.execSQL(CREATE_ARTINAPP_TABLE);
        sqLiteDatabase.execSQL(CREATE_ARTIMAGE_TABLE);
        sqLiteDatabase.execSQL(CREATE_CARD_TABLE);
        sqLiteDatabase.execSQL(CREATE_CARDIMAGE_TABLE);
        sqLiteDatabase.execSQL(CREATE_PROFILE_TABLE);
        sqLiteDatabase.execSQL(CREATE_RECENT_TABLE);
        sqLiteDatabase.execSQL(CREATE_RECENTCOLOR_TABLE);
        sqLiteDatabase.execSQL(CREATE_EVENT_TABLE);
        sqLiteDatabase.execSQL(CREATE_LAUNCH_TABLE);
        sqLiteDatabase.execSQL(CREATE_LOGIN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertArtTable(ArtLocalModel artLocalModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CAT_ID, artLocalModel.getCatId());
        contentValues.put(CAT_IMAGE, artLocalModel.getCatImage());
        contentValues.put(CAT_INAPP_ID, artLocalModel.getCatInappId());
        contentValues.put(MDATE, artLocalModel.getMdate());
        contentValues.put(STATUS, artLocalModel.getStatus());
        contentValues.put(IMAGE_URL, artLocalModel.getImageUrl());
        contentValues.put(CAT_POSSION, artLocalModel.getCatPossion());
        contentValues.put(CAT_TRY, artLocalModel.getCatTry());

        db.insert(ART_TABLE, null, contentValues);
    }

    public void insertArtInAppTable(ArtInAppLocalModel artInAppLocalModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CAT_ID, artInAppLocalModel.getCatId());

        db.insert(ARTINAPP_TABLE, null, contentValues);
    }

    public void insertArtImageTable(ArtImageLocalModel artImageLocalModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CAT_ID, artImageLocalModel.getCatId());
        contentValues.put(CAT_IMAGE, artImageLocalModel.getCatImage());
        contentValues.put(STATUS, artImageLocalModel.getStatus());
        contentValues.put(IMAGE_ID, artImageLocalModel.getImageId());
        contentValues.put(IMAGE_URL, artImageLocalModel.getImageUrl());

        db.insert(ARTIMAGE_TABLE, null, contentValues);
    }

    public void insertCardTable(CardLocalModel cardLocalModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CAT_ID, cardLocalModel.getCatId());
        contentValues.put(CARD_NAME, cardLocalModel.getCardName());
        contentValues.put(CAT_POSSION, cardLocalModel.getCatPossion());

        db.insert(CARD_TABLE, null, contentValues);
    }

    public void insertCardImageTable(CardImageLocalModel cardImageLocalModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CAT_ID, cardImageLocalModel.getCatId());
        contentValues.put(CARD_IMAGE, cardImageLocalModel.getCardImage());
        contentValues.put(STATUS, cardImageLocalModel.getStatus());
        contentValues.put(IMAGE_ID, cardImageLocalModel.getImageId());
        contentValues.put(IMAGE_URL, cardImageLocalModel.getImageUrl());

        db.insert(CARDIMAGE_TABLE, null, contentValues);
    }

    public void insertProfileTable(ProfileLocalModel profileLocalModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_ID, profileLocalModel.getUserId());
        contentValues.put(USER_FIRSTNAME, profileLocalModel.getUserFirstname());
        contentValues.put(USER_LASTNAME, profileLocalModel.getUserLastname());
        contentValues.put(USER_EMAIL, profileLocalModel.getUserEmail());
        contentValues.put(USER_DOB, profileLocalModel.getUserDob());
        contentValues.put(PURCHASE_ART, profileLocalModel.getPurchaseArt());
        contentValues.put(PURCHASE_LOGO, profileLocalModel.getPurchaseLogo());
        contentValues.put(PURCHASE_ADDLAYER, profileLocalModel.getPurchaseAddlayer());
        contentValues.put(PURCHASE_HD, profileLocalModel.getPurchaseHd());

        db.insert(PROFILE_TABLE, null, contentValues);
    }

    public void insertRecentTable(RecentLocalModel recentLocalModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CAT_ID, recentLocalModel.getCatId());
        contentValues.put(CAT_IMAGE, recentLocalModel.getCatImage());
        contentValues.put(STATUS, recentLocalModel.getStatus());
        contentValues.put(IMAGE_ID, recentLocalModel.getImageId());

        db.insert(RECENT_TABLE, null, contentValues);
    }

    public void insertRecentColorTable(RecentColorLocalModel recentColorLocalModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLORCODE, recentColorLocalModel.getColorCode());

        db.insert(RECENTCOLOR_TABLE, null, contentValues);
    }

    public void insertEventTable(EventLocalModel eventLocalModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(EVENT_NAME, eventLocalModel.getEventName());
        contentValues.put(EVENT_DATE, eventLocalModel.getEventDate());
        contentValues.put(EVENT_YEAR, eventLocalModel.getEventYear());
        contentValues.put(EVENT_TIME, eventLocalModel.getEventTime());
        contentValues.put(EVENT_TYPE, eventLocalModel.getEventType());
        contentValues.put(FB_USERID, eventLocalModel.getFbUserid());

        db.insert(EVENT_TABLE, null, contentValues);
    }

    public void insertLaunchTable(LaunchLocalModel launchLocalModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(LAUNCH, launchLocalModel.getLaunch());

        db.insert(LAUNCH_TABLE, null, contentValues);
    }

    public void insertLoginTable(LoginLocalModel loginLocalModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        //contentValues.put(LOG_ID, loginLocalModel.getLogId());
        contentValues.put(USER_ID, loginLocalModel.getUserId());
        contentValues.put(USER_NAME, loginLocalModel.getUserName());
        contentValues.put(USER_PASS, loginLocalModel.getUserPass());
        contentValues.put(LOG_TYPE, loginLocalModel.getLogType());

        db.insert(LOGIN_TABLE, null, contentValues);
    }


    // update table

    public void updateArtTable(ArtLocalModel artLocalModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CAT_ID, artLocalModel.getCatId());
        contentValues.put(CAT_IMAGE, artLocalModel.getCatImage());
        contentValues.put(CAT_INAPP_ID, artLocalModel.getCatInappId());
        contentValues.put(MDATE, artLocalModel.getMdate());
        contentValues.put(STATUS, artLocalModel.getStatus());
        contentValues.put(IMAGE_URL, artLocalModel.getImageUrl());
        contentValues.put(CAT_POSSION, artLocalModel.getCatPossion());
        contentValues.put(CAT_TRY, artLocalModel.getCatTry());

        db.update(ART_TABLE, contentValues, CAT_ID + "=?", new String[] { artLocalModel.getCatId() });
    }

    public void updateArtInAppTable(ArtInAppLocalModel artInAppLocalModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CAT_ID, artInAppLocalModel.getCatId());

        db.update(ARTINAPP_TABLE, contentValues, CAT_ID + "=?", new String[] { artInAppLocalModel.getCatId() });
    }

    public void updateArtImageTable(ArtImageLocalModel artImageLocalModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CAT_ID, artImageLocalModel.getCatId());
        contentValues.put(CAT_IMAGE, artImageLocalModel.getCatImage());
        contentValues.put(STATUS, artImageLocalModel.getStatus());
        contentValues.put(IMAGE_ID, artImageLocalModel.getImageId());
        contentValues.put(IMAGE_URL, artImageLocalModel.getImageUrl());

        db.update(ARTIMAGE_TABLE, contentValues, CAT_ID + "=?", new String[] { artImageLocalModel.getCatId() });
    }

    public void updateCardTable(CardLocalModel cardLocalModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        //contentValues.put(CAT_ID, cardLocalModel.getCatId());
        contentValues.put(CARD_NAME, cardLocalModel.getCardName());
        contentValues.put(CAT_POSSION, cardLocalModel.getCatPossion());

        db.update(CARD_TABLE, contentValues, CAT_ID + "=?", new String[] { cardLocalModel.getCatId() });
    }

    public void updateCardImageTable(CardImageLocalModel cardImageLocalModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CAT_ID, cardImageLocalModel.getCatId());
        contentValues.put(CARD_IMAGE, cardImageLocalModel.getCardImage());
        contentValues.put(STATUS, cardImageLocalModel.getStatus());
        contentValues.put(IMAGE_ID, cardImageLocalModel.getImageId());
        contentValues.put(IMAGE_URL, cardImageLocalModel.getImageUrl());


        db.update(CARDIMAGE_TABLE, contentValues, CAT_ID + "=?", new String[] { cardImageLocalModel.getCatId() });
    }

    public void updateCardImageTableOnImageId(String imageID, String status, String card_image)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(STATUS, status);
        contentValues.put(CARD_IMAGE, card_image);

        db.update(CARDIMAGE_TABLE, contentValues, IMAGE_ID + "=?", new String[] { imageID });
    }

    public void updateArtTablePossion(String catID, String possion)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CAT_POSSION, possion);

        db.update(ART_TABLE, contentValues, CAT_ID + "=?", new String[] { catID });
    }

    public void updateArtTableOnCatID(String catID, String status, String cat_image)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(STATUS, status);
        contentValues.put(CAT_IMAGE, cat_image);

        //Log.v("updateArtTableOnCatID.", ".catID.." + catID);

        db.update(ART_TABLE, contentValues, CAT_ID + "=?", new String[] { catID });
    }

    public void updateArtTableTry(String catID, String try_str)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CAT_TRY, try_str);

        db.update(ART_TABLE, contentValues, CAT_ID + "=?", new String[] { catID });
    }

    public void updateArtImageTableOnImageID(String imageID, String status, String cat_image)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(STATUS, status);
        contentValues.put(CAT_IMAGE, cat_image);

        db.update(ARTIMAGE_TABLE, contentValues, IMAGE_ID + "=?", new String[] { imageID });
    }

    public void updateProfileTable(ProfileLocalModel profileLocalModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_ID, profileLocalModel.getUserId());
        contentValues.put(USER_FIRSTNAME, profileLocalModel.getUserFirstname());
        contentValues.put(USER_LASTNAME, profileLocalModel.getUserLastname());
        contentValues.put(USER_EMAIL, profileLocalModel.getUserEmail());
        contentValues.put(USER_DOB, profileLocalModel.getUserDob());
        contentValues.put(PURCHASE_ART, profileLocalModel.getPurchaseArt());
        contentValues.put(PURCHASE_LOGO, profileLocalModel.getPurchaseLogo());
        contentValues.put(PURCHASE_ADDLAYER, profileLocalModel.getPurchaseAddlayer());
        contentValues.put(PURCHASE_HD, profileLocalModel.getPurchaseHd());


        db.update(PROFILE_TABLE, contentValues, USER_ID + "=?", new String[] { profileLocalModel.getUserId() });
    }

    public void updateRecentTable(RecentLocalModel recentLocalModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CAT_ID, recentLocalModel.getCatId());
        contentValues.put(CAT_IMAGE, recentLocalModel.getCatImage());
        contentValues.put(STATUS, recentLocalModel.getStatus());
        contentValues.put(IMAGE_ID, recentLocalModel.getImageId());


        db.update(RECENT_TABLE, contentValues, CAT_ID + "=?", new String[] { recentLocalModel.getCatId() });
    }

    public void updateRecentColorTable(RecentColorLocalModel recentColorLocalModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLORCODE, recentColorLocalModel.getColorCode());

        db.update(RECENTCOLOR_TABLE, contentValues, COLORCODE + "=?", new String[] { recentColorLocalModel.getColorCode() });
    }

    public void updateEventTable(EventLocalModel eventLocalModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(EVENT_NAME, eventLocalModel.getEventName());
        contentValues.put(EVENT_DATE, eventLocalModel.getEventDate());
        contentValues.put(EVENT_YEAR, eventLocalModel.getEventYear());
        contentValues.put(EVENT_TIME, eventLocalModel.getEventTime());
        contentValues.put(EVENT_TYPE, eventLocalModel.getEventType());
        contentValues.put(FB_USERID, eventLocalModel.getFbUserid());

        db.update(EVENT_TABLE, contentValues, EVENT_ID + "=?", new String[] { eventLocalModel.getEventId() });
    }

    public void updateLaunchTable(LaunchLocalModel launchLocalModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(LAUNCH, launchLocalModel.getLaunch());

        db.update(LAUNCH_TABLE, contentValues, LAUNCH + "=?", new String[] { launchLocalModel.getLaunch() });
    }

    public void updateLoginTable(LoginLocalModel loginLocalModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_ID, loginLocalModel.getUserId());
        contentValues.put(USER_NAME, loginLocalModel.getUserName());
        contentValues.put(USER_PASS, loginLocalModel.getUserPass());
        contentValues.put(LOG_TYPE, loginLocalModel.getLogType());

        db.update(LOGIN_TABLE, contentValues, LOG_ID + "=?", new String[] { loginLocalModel.getLogId() });
    }

    public Cursor getAllDataFromTable(String tableName) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        return  sqLiteDatabase.rawQuery(SELECT_SQL + tableName, null);
    }

    public Cursor getCardImage(String catID) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        return  sqLiteDatabase.rawQuery(SELECT_SQL + CARDIMAGE_TABLE + " where " + CAT_ID + " = ?", new String[] { catID });
    }

    public Cursor getCardImageStatus() {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        return  sqLiteDatabase.rawQuery(SELECT_SQL + CARDIMAGE_TABLE + " where " + STATUS + " = ?", new String[] { "0" });
    }

    public Cursor getArtCategoriesImageStatus() {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        return  sqLiteDatabase.rawQuery(SELECT_SQL + ART_TABLE + " where " + STATUS + " = ?", new String[] { "0" });
    }

    public Cursor getArtTryImageFromCatId(String cat_id) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        return  sqLiteDatabase.rawQuery("SELECT " + CAT_TRY + " FROM " + ART_TABLE + " where " + CAT_ID + " = ?", new String[] { cat_id });
    }

    public Cursor getArtInAppIdFromCatId(String cat_id) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        return  sqLiteDatabase.rawQuery("SELECT " + CAT_INAPP_ID + " FROM " + ART_TABLE + " where " + CAT_ID + " = ?", new String[] { cat_id });
    }

    public Cursor getArtImageStatus() {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        return  sqLiteDatabase.rawQuery(SELECT_SQL + ARTIMAGE_TABLE + " where " + STATUS + " = ?", new String[] { "0" });
    }

    public Cursor getArtImageOnCatID(String cat_id) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        List<Cursor> cursorList = new ArrayList<>();

        Cursor cursorImageID = sqLiteDatabase.rawQuery("SELECT " + IMAGE_ID + " FROM " + ARTIMAGE_TABLE + " WHERE " + CAT_ID + " = ?", new String[] { cat_id });

        try {
            while (cursorImageID.moveToNext()) {
                String imageID = cursorImageID.getString(0);
                Cursor cursor_single = sqLiteDatabase.rawQuery(SELECT_SQL + ARTIMAGE_TABLE + " where " + IMAGE_ID + " = ?", new String[] { imageID });
                cursorList.add(cursor_single);
                //cursor_single.close();
                //Log.v("cursor_single...","..." + cursorList.size());
            }
        } finally {
            cursorImageID.close();
        }

        if(cursorList.size() > 0) {
            return new MergeCursor(cursorList.toArray(new Cursor[cursorList.size()])); // merge cursor
        }else{
            return cursorImageID;
        }

        //return  sqLiteDatabase.rawQuery(SELECT_SQL + ARTIMAGE_TABLE + " where " + CAT_ID + " = ?", new String[] { cat_id });
    }


    public void deleteFromCardTable(String catId)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(CARD_TABLE, CAT_ID + "=?" , new String[] { catId });
    }

    public void deleteFromCardImageTable(String catId)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(CARDIMAGE_TABLE, CAT_ID + "=?" , new String[] { catId });
    }

    public void deleteFromCardImageTableOnImageId(String imageId)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(CARDIMAGE_TABLE, IMAGE_ID + "=?" , new String[] { imageId });
    }

    public void deleteFromArtTable(String catId)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(ART_TABLE, CAT_ID + "=?" , new String[] { catId });
    }

    public void deleteFromArtImageTable(String catId)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(ARTIMAGE_TABLE, CAT_ID + "=?" , new String[] { catId });
    }

    public void deleteFromArtImageTableOnImageID(String imageId)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(ARTIMAGE_TABLE, IMAGE_ID + "=?" , new String[] { imageId });
    }

    public void deleteFromRecentTable(String imageId)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(RECENT_TABLE, IMAGE_ID + "=?" , new String[] { imageId });
    }

    /*public Cursor getAllTeamData() {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        return  sqLiteDatabase.rawQuery(SELECT_SQL + TEAM_TABLE + " where " + TEAM_DELETE_STATUS + " = ?", new String[] { "0" });
    }

    public Cursor getPlayerFromID(String playerID) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        return  sqLiteDatabase.rawQuery(SELECT_SQL + PLAYER_TABLE + " where " + PLAYER_ID + " = ?", new String[] { playerID });
    }

    public Cursor getPlayerFromTeamID(String team_id) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        return  sqLiteDatabase.rawQuery(SELECT_SQL + PLAYER_TABLE + " where " + PLAYER_TEAM_ID + " = ?", new String[] { team_id });
    }

    public Cursor getTeamFromID(String teamID) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        return  sqLiteDatabase.rawQuery(SELECT_SQL + TEAM_TABLE + " where " + TEAM_ID + " = ?", new String[] { teamID });
    }

    public Cursor checkTeamSID(String teamServerID) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        return  sqLiteDatabase.rawQuery(SELECT_SQL + TEAM_TABLE + " where " + TEAM_SERVER_ID + " = ?", new String[] { teamServerID });
    }

    public Cursor checkPlayerSID(String playerServerID) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        return  sqLiteDatabase.rawQuery(SELECT_SQL + PLAYER_TABLE + " where " + PLAYER_SERVER_ID + " = ?", new String[] { playerServerID });
    }

    public Cursor getSyncTeamData(String userId) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        return  sqLiteDatabase.rawQuery(SELECT_SQL + TEAM_TABLE + " where " + TEAM_SERVER_ID + " != ? and " + TEAM_USER_ID + "=?", new String[] { "0", userId });
    }

    public Cursor getTeamForSync(String userId) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        return  sqLiteDatabase.rawQuery(SELECT_SQL + TEAM_TABLE + " where " + TEAM_SYNC_STATUS + " = ? and " + TEAM_USER_ID + "=?", new String[] { "0", userId });
    }

    public Cursor getPlayerForSync(String teamId) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        return  sqLiteDatabase.rawQuery(SELECT_SQL + PLAYER_TABLE + " where " + PLAYER_SYNC_STATUS + " = ? and " + PLAYER_TEAM_ID + "=?", new String[] { "0", teamId });
    }



    *//*public Cursor getContributionFromGoalID(String goalId) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        return  sqLiteDatabase.rawQuery(SELECT_SQL + CONTRIBUTION_TABLE + " where " + GOLA_ID_CONTRIBUTION + " = ?", new String[] { goalId });
    }*//*

    public void updatePlayerTable(String playerId, PlayerLocalModel playerLocalModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(PLAYER_ID, playerLocalModel.getPlayerId());
        contentValues.put(PLAYER_SERVER_ID, playerLocalModel.getPlayerServerId());
        contentValues.put(PLAYER_TEAM_ID, playerLocalModel.getPlayerTeamId());
        contentValues.put(PLAYER_NAME, playerLocalModel.getPlayerName());
        contentValues.put(PLAYER_STATUS, playerLocalModel.getPlayerStatus());
        contentValues.put(PLAYER_TSHIRT, playerLocalModel.getPlayerTshirt());
        contentValues.put(PALYER_POSITION, playerLocalModel.getPlayerPosition());
        contentValues.put(PLAYER_TYPE, playerLocalModel.getPlayerType());
        contentValues.put(PLAYER_INVITE, playerLocalModel.getPlayerInvite());
        contentValues.put(PLAYER_INVITE_TYPE, playerLocalModel.getPlayerInviteType());
        contentValues.put(PLAYER_SELECTION, playerLocalModel.getPlayerSelection());
        contentValues.put(PLAYER_SYNC_STATUS, playerLocalModel.getPlayerSyncStatus());

        db.update(PLAYER_TABLE, contentValues, PLAYER_ID + "=?", new String[] { playerId });
    }

    public void updateTeamTable(String teamID, TeamLocalModel teamLocalModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TEAM_ID, teamLocalModel.getTeamId());
        contentValues.put(TEAM_SERVER_ID, teamLocalModel.getTeamServerId());
        contentValues.put(TEAM_USER_ID, teamLocalModel.getTeamUserId());
        contentValues.put(TEAM_NAME, teamLocalModel.getTeamName());
        contentValues.put(TEAM_FORMAT, teamLocalModel.getTeamFormat());
        contentValues.put(TEAM_PLAYERS, teamLocalModel.getTeamPlayers());
        contentValues.put(TEAM_GOALKEEPER, teamLocalModel.getTeamGoalkeeper());
        contentValues.put(TEAM_SYNC_STATUS, teamLocalModel.getTeamSyncStatus());
        contentValues.put(TEAM_DELETE_STATUS, teamLocalModel.getTeamDeleteStatus());

        db.update(TEAM_TABLE, contentValues, TEAM_ID + "=?", new String[] { teamID });
    }

    public void assignLocalTeamToUser(String teamUserID)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TEAM_USER_ID, teamUserID);

        db.update(TEAM_TABLE, contentValues, TEAM_USER_ID + "=?", new String[] { "0" });
    }

    public void updateTeamTableOnSID(String teamServerID, TeamLocalModel teamLocalModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TEAM_ID, teamLocalModel.getTeamId());
        contentValues.put(TEAM_SERVER_ID, teamLocalModel.getTeamServerId());
        contentValues.put(TEAM_USER_ID, teamLocalModel.getTeamUserId());
        contentValues.put(TEAM_NAME, teamLocalModel.getTeamName());
        contentValues.put(TEAM_FORMAT, teamLocalModel.getTeamFormat());
        contentValues.put(TEAM_PLAYERS, teamLocalModel.getTeamPlayers());
        contentValues.put(TEAM_GOALKEEPER, teamLocalModel.getTeamGoalkeeper());
        contentValues.put(TEAM_SYNC_STATUS, teamLocalModel.getTeamSyncStatus());
        contentValues.put(TEAM_DELETE_STATUS, teamLocalModel.getTeamDeleteStatus());

        db.update(TEAM_TABLE, contentValues, TEAM_SERVER_ID + "=?", new String[] { teamServerID });
    }

    public void updatePlayerTableOnSID(String playerServerId, PlayerLocalModel playerLocalModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(PLAYER_ID, playerLocalModel.getPlayerId());
        contentValues.put(PLAYER_SERVER_ID, playerLocalModel.getPlayerServerId());
        contentValues.put(PLAYER_TEAM_ID, playerLocalModel.getPlayerTeamId());
        contentValues.put(PLAYER_NAME, playerLocalModel.getPlayerName());
        contentValues.put(PLAYER_STATUS, playerLocalModel.getPlayerStatus());
        contentValues.put(PLAYER_TSHIRT, playerLocalModel.getPlayerTshirt());
        contentValues.put(PALYER_POSITION, playerLocalModel.getPlayerPosition());
        contentValues.put(PLAYER_TYPE, playerLocalModel.getPlayerType());
        contentValues.put(PLAYER_INVITE, playerLocalModel.getPlayerInvite());
        contentValues.put(PLAYER_INVITE_TYPE, playerLocalModel.getPlayerInviteType());
        contentValues.put(PLAYER_SELECTION, playerLocalModel.getPlayerSelection());
        contentValues.put(PLAYER_SYNC_STATUS, playerLocalModel.getPlayerSyncStatus());

        db.update(PLAYER_TABLE, contentValues, PLAYER_SERVER_ID + "=?", new String[] { playerServerId });
    }

    public void updateTeamIdSync(String teamID, String teamServerID)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TEAM_ID, teamServerID);
        contentValues.put(TEAM_SERVER_ID, teamServerID);
        contentValues.put(TEAM_SYNC_STATUS, "1");

        db.update(TEAM_TABLE, contentValues, TEAM_ID + "=?", new String[] { teamID });
    }

    public void updateTeamIdSync(String teamID)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TEAM_SYNC_STATUS, "1");

        db.update(TEAM_TABLE, contentValues, TEAM_ID + "=?", new String[] { teamID });
    }

    public void updateTeamSyncOff(String teamID)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TEAM_SYNC_STATUS, "0");

        db.update(TEAM_TABLE, contentValues, TEAM_ID + "=?", new String[] { teamID });
    }

    public void updatePlayerSync(String teamID, String teamServerID)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(PLAYER_TEAM_ID, teamServerID);
        contentValues.put(PLAYER_SYNC_STATUS, "1");

        db.update(PLAYER_TABLE, contentValues, PLAYER_TEAM_ID + "=?", new String[] { teamID });
    }

    public void updatePlayerSIDSync(String playerID, String playerServerID)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(PLAYER_SERVER_ID, playerServerID);
        contentValues.put(PLAYER_SYNC_STATUS, "1");

        db.update(PLAYER_TABLE, contentValues, PLAYER_ID + "=?", new String[] { playerID });
    }

    public void updatePlayerSync(String playerID)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(PLAYER_SYNC_STATUS, "1");

        db.update(PLAYER_TABLE, contentValues, PLAYER_TEAM_ID + "=?", new String[] { playerID });
    }

    public void updatePlayerPIDSync(String playerID)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(PLAYER_SYNC_STATUS, "1");

        db.update(PLAYER_TABLE, contentValues, PLAYER_ID + "=?", new String[] { playerID });
    }

    public void updateTshirtAllPlayer(String teamID, String tShirt)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(PLAYER_TSHIRT, tShirt);
        contentValues.put(PLAYER_SYNC_STATUS, "0");

        db.update(PLAYER_TABLE, contentValues, PLAYER_TEAM_ID + "=?", new String[] { teamID });
    }

    public void updateTshirtGoalKeeper(String teamID, String tShirt)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(PLAYER_TSHIRT, tShirt);
        contentValues.put(PLAYER_SYNC_STATUS, "0");

        db.update(PLAYER_TABLE, contentValues, PLAYER_TEAM_ID + "=? and " + PLAYER_TYPE + "=?", new String[] { teamID, "gk" });
    }

    public void updatePlayerPosition(String playerID, String position)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(PALYER_POSITION, position);
        contentValues.put(PLAYER_SYNC_STATUS, "0");

        db.update(PLAYER_TABLE, contentValues, PLAYER_ID + "=?", new String[] { playerID });
    }

    public void updatePlayerRemoveInvite(String playerID)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(PLAYER_NAME, "");
        contentValues.put(PLAYER_INVITE, "");
        contentValues.put(PLAYER_INVITE_TYPE, "");

        db.update(PLAYER_TABLE, contentValues, PLAYER_ID + "=?", new String[] { playerID });
    }



   *//* public void updateContAmountGoalTable(String goalID)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        // UPDATE  tbl_goal set contributed_amount = (SELECT sum(amount) FROM  tbl_contribution where goal_id=1) where id = 1

        String query = SELECT_SQL + CONTRIBUTION_TABLE + " where " + GOLA_ID_CONTRIBUTION + " = " + goalID;

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if(cursor.getCount() <= 0){

            sqLiteDatabase.execSQL("UPDATE " + GOAL_TABLE + " set " + CONTRIBUTED_AMOUNT_GOAL + " = " + 0.00 + " where " + ID + " = " + goalID);

        }else {

            sqLiteDatabase.execSQL("UPDATE " + GOAL_TABLE + " set " + CONTRIBUTED_AMOUNT_GOAL + " = (SELECT sum(" + AMOUNT_CONTRIBUTION + ") FROM " + CONTRIBUTION_TABLE
                    + " where " + GOLA_ID_CONTRIBUTION + " = " + goalID + ") where " + ID + " = " + goalID);
        }
        cursor.close();


    }*//*


    public void deletePlayerFromTeamId(String playerTeamId)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(PLAYER_TABLE, PLAYER_TEAM_ID + "=?" , new String[] { playerTeamId });
    }

    public void deleteTeamId(String teamId)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TEAM_TABLE, TEAM_ID + "=?" , new String[] { teamId });
    }

    public void setDeleteFlagTeamId(String teamId)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TEAM_DELETE_STATUS, "1");

        db.update(TEAM_TABLE, contentValues, TEAM_ID + "=?" , new String[] { teamId });
    }

    *//*public void deleteContributionFromGoalId(String goalId)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(CONTRIBUTION_TABLE, GOLA_ID_CONTRIBUTION + "=?" , new String[] { goalId });
    }*//*

    public void deleteTable(String tableName)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " + tableName);
    }

    public int getCurrentHighestIDPlayer() {

        SQLiteDatabase db = this.getWritableDatabase();

        final String MY_QUERY = "SELECT MAX(" + PLAYER_ID +") FROM " + PLAYER_TABLE;
        Cursor cur = db.rawQuery(MY_QUERY, null);
        cur.moveToFirst();
        int ID = cur.getInt(0);
        cur.close();
        return ID;
    }

    public int getCurrentHighestIDTeam() {

        SQLiteDatabase db = this.getWritableDatabase();

        final String MY_QUERY = "SELECT MAX(" + ID + ") FROM " + TEAM_TABLE;
        Cursor cur = db.rawQuery(MY_QUERY, null);
        cur.moveToFirst();
        int ID = cur.getInt(0);
        cur.close();
        return ID;
    }

*/
}
