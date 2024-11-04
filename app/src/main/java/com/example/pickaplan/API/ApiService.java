package com.example.pickaplan.API;

import com.example.pickaplan.dataClass.phoneData;
import com.example.pickaplan.dataClass.planData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("plans")
    Call<List<planData>> getMobilePlans();

    @GET("phones")
    Call<List<phoneData>> getSmartphones();
}
