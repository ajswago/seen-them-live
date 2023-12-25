package com.swago.seenthemlive.api.spotify

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object SpotifyApiBuilder {

    class AuthInterceptor(token: String) : Interceptor {

        private val token = token
        override fun intercept(chain: Interceptor.Chain): Response {
            val newUrl = chain.request().url()
                .newBuilder()
                .build()

            val newRequest = chain.request()
                .newBuilder()
                .url(newUrl)
                .addHeader("authorization", "Bearer $token")
                .build()

            return chain.proceed(newRequest)
        }
    }

    private fun spotifyClient(token: String): OkHttpClient {
        return OkHttpClient().newBuilder()
            .addInterceptor(AuthInterceptor(token))
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    fun retrofit(token: String) : Retrofit = Retrofit.Builder()
        .client(spotifyClient(token))
        .baseUrl("https://api.spotify.com/v1/")
        .addConverterFactory(MoshiConverterFactory.create().asLenient())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()


    fun profile(token: String) : Profile = retrofit(token).create(Profile::class.java)

    fun playlist(token: String) : Playlist = retrofit(token).create(Playlist::class.java)
}