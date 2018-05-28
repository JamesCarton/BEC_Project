package com.wlc.beacon.network;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;



public class ApiClient {

    public static final String BASE_URL = "https://beacon-195311.appspot.com/";
    //public static final String BASE_URL = "http://api.themoviedb.org/3/";

    private static Retrofit retrofit = null;

    public static final String STATUS       = "status";
    public static final String MESSAGE      = "message";
    public static final String SUCCESS      = "success";
    public static final String ROOT         = "Root";
    public static final String RECORD       = "Record";
    public static final String USER_ID      = "user_id";
    public static final String NAME         = "name";
    public static final String EMAIL        = "email";
    public static final String SHORT_NOTIFICATION   = "short_notification";
    public static final String STATENAME    = "state_name";
    public static final String STATEID      = "state_id";
    public static final String CITYNAME     = "city_name";
    public static final String USER_AVATAR  = "user_avatar";

    public static final String PopularLocation = "popular_location";


    public static Retrofit getClient() {
        if (retrofit==null) {

           /* HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();*/

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    //.client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }


}
