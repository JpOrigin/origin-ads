package com.origin.ads.sample.clone

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface CloneAdsInterface {
    @FormUrlEncoded
    @POST("index.php")
    fun getCloneAdsData(@Field("account_name") phone: String): Call<CloneAdsData>
}