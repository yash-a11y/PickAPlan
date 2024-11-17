package com.example.pickaplan.API;

import com.example.pickaplan.dataClass.phoneData;
import com.example.pickaplan.dataClass.planData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("fidoplans")
    Call<List<planData>> getFidoPlans();

    @GET("rogersplans")
    Call<List<planData>> getrogersPlans();


    @GET("ranking")
    Call<List<String>> getRanking(@Query("keyword") String key);

    @GET("virginplans")
    Call<List<planData>> getVirginPlans();

    @GET("telusplans")
    Call<List<planData>> getTelusPlan();
}
