package com.example.covid_19;

import com.example.covid_19.bean.PatientData;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DataService {
    @GET("pakistan")
    Call<PatientData> getAllPatients();
}
