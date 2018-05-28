package com.rainbowloveapp.app.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *  on 11/5/17.
 */

public class ApiClient {

    public static final String BASE_URL = "http://www.luvisallaroundme.com/webservice/";
    //public static final String BASE_URL = "http://api.themoviedb.org/3/";

    private static Retrofit retrofit = null;

    public static final String Filters_Tips = "https://rainbowloveappblog.com/2017/03/13/our-best-photo-filters-for-instagram/";
    public static final String Add_Your_Logo = "https://rainbowloveappblog.com/2016/11/29/add-your-logo-to-photos/";
    public static final String Rainbow_Love_Filters = "https://rainbowloveappblog.com/2016/11/29/rainbow-love-filters/";
    public static final String Design_Tips = "https://rainbowloveappblog.com/blog-posts/";
    public static final String FAQ = "http://www.rainbowloveapp.com/faq-.html";
    public static final String Follow_Our_Instagram = "https://www.instagram.com/rainbowlovegreetings/";
    public static final String Privacy_Policy = "http://www.rainbowloveapp.com/privacy.html";
    public static final String Terms_of_Service = "http://www.rainbowloveapp.com/terms.html";
    public static final String EDIT_OF_THE_WEEK = "https://rainbowloveappblog.com/edit-of-the-week/";
    public static final String Gallery = "http://luvisallaroundme.com/admin/gallery_view.php";
    public static final String UploadToGallery = "http://www.luvisallaroundme.com/webservice/gallrey_web.php?";
    public static final String HomeFilters_Tips = "https://rainbowloveappblog.com/rainbow-filter-editing-tips/";
    public static final String Get_Tips = "https://rainbowloveappblog.com";

    public static final String Site = "http://loveisallaroundme.com";


    public static final String EmailContent = "<html><body>" +
            "<a href=https://itunes.apple.com/us/app/rainbowlove-greetings/id922406189?mt=8><img src='http://luvisallaroundme.com/admin/images/techsupport-logo.png'></a>" +
            "</body></html>";

    /*public static final String EmailContent = "<img src=\"http://luvisallaroundme.com/admin/images/techsupport-logo.png\">";//MHA112133347*/

    public static final String STATUS       = "status";
    public static final String MESSAGE      = "message";
    public static final String SUCCESS      = "success";
    public static final String ROOT         = "Root";
    public static final String RECORD       = "Record";
    public static final String CAT_ID       = "cat_id";
    public static final String CATE_NAME    = "cate_name";
    public static final String IMAGE_ID     = "image_id";
    public static final String IMAGE        = "image";
    public static final String MDATE        = "mdate";
    public static final String Bundle_Indentifier = "bundle_indentifier";
    public static final String USER_ID      = "user_id";
    public static final String NAME         = "name";
    public static final String EMAIL        = "email";
    public static final String BIRTHDATE    = "birthdate";


    public static Retrofit getClient() {
        if (retrofit==null) {

           /* HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();*/

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    //.client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    /*public static Retrofit tempClient() {
        Retrofit temp_retrofit = null;
        if (temp_retrofit==null) {

           *//* HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();*//*

            temp_retrofit = new Retrofit.Builder()
                    .baseUrl("http://wlctest.online/balanced/")
                    //.client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return temp_retrofit;
    }*/

}