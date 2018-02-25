package com.goodiebag.adverPizing.networks.rest;

import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * Created by Kai on 25/02/18.
 */

public class AdverPizingRetroServer {
    private static final String URL_PREFIX = "http://";
    private static final String PORT = ":3000/";
    private static Retrofit retrofit = null;

    // The RetroServer class generates an implementation of the AdverPizingService interface.
    public static synchronized Retrofit getRetroServer(String ip) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL_PREFIX + ip + PORT)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
