package com.goodiebag.adverPizing.networks.rest;

import com.goodiebag.adverPizing.networks.rest.models.NoticeBoardRespnose;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Kai on 25/02/18.
 */

public interface AdverPizingService {
    //AdverPizingService turns your HTTP API into a Java interface.
    @GET("noticeboards/firstTen")
    Call<List<NoticeBoardRespnose>> listNotices();
}
