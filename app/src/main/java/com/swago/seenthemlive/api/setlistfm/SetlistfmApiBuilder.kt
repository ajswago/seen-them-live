package com.swago.seenthemlive.api.setlistfm

import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.swago.seenthemlive.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object SetlistfmApiBuilder{

    //Creating Auth Interceptor to add api_key query in front of all the requests.
    private val authInterceptor = Interceptor {chain->
        val newUrl = chain.request().url()
            .newBuilder()
            .build()

        val newRequest = chain.request()
            .newBuilder()
            .url(newUrl)
            .addHeader("x-api-key", BuildConfig.SETLISTFM_API_KEY)
//            .headers(Headers.of(
//                mutableMapOf(
//                    "content-type" to "application/json",
//                    "x-api-key" to BuildConfig.SETLISTFM_API_KEY)))
            .addHeader("Accept", "application/json")
            .build()

        newRequest.headers().names().forEach {
            Log.d("REQUEST", "BUILT REQUEST: Name: ${it}, Value ${newRequest.headers().values(it)}")
        }

        chain.proceed(newRequest)
    }

    private val setlistfmClient = OkHttpClient().newBuilder()
        .addInterceptor(authInterceptor)
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()



    fun retrofit() : Retrofit = Retrofit.Builder()
        .client(setlistfmClient)
        .baseUrl("https://api.setlist.fm/rest/1.0/")
        .addConverterFactory(MoshiConverterFactory.create().asLenient())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()


    val search : Search = retrofit().create(Search::class.java)

}