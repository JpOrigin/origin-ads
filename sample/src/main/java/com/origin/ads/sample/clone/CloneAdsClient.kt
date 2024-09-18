package com.origin.ads.sample.clone

import retrofit2.Retrofit
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

object CloneAdsClient {
    fun getRetrofit(url: String): Retrofit? {
        if (url.isNotEmpty()) {
            val builder = OkHttpClient.Builder()
            builder.addInterceptor { chain ->
                val mResponse = chain.proceed(chain.request())
                getResponseString(mResponse)
                mResponse
            }
            builder.connectTimeout(5, TimeUnit.MINUTES).writeTimeout(5, TimeUnit.MINUTES).readTimeout(5, TimeUnit.MINUTES)
            val okHttpClient: OkHttpClient = builder.build()
            return Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build()
        }else{
            return null
        }
    }

    private fun getResponseString(response: Response): String {
        val responseBody = response.body
        val source = responseBody?.source()
        source?.request(Long.MAX_VALUE)
        val buffer = source?.buffer
        var charset = Charset.forName("UTF-8")
        val contentType = responseBody?.contentType()
        if (contentType != null) {
            charset = contentType.charset(Charset.forName("UTF-8")) ?: charset
        }
        return buffer?.clone()?.readString(charset) ?: " "
    }
}