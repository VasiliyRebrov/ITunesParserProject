package com.data.remote.apiServices

import com.data.common.API_BASE_URL
import com.data.remote.BaseRemoteAlbums
import com.data.remote.BaseRemoteTracks
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("search")
    fun loadAlbums(
        @Query("term") term: String,
        @Query("media") media: String = "music",
        @Query("entity") entity: String = "album"
    ): Call<BaseRemoteAlbums>

    @GET("lookup")
    fun loadTracksByAlbum(
        @Query("id") id: String,
        @Query("entity") entity: String = "song"
    ): Call<BaseRemoteTracks>

    companion object {
        fun create(): Api = with(Retrofit.Builder()) {
            baseUrl(API_BASE_URL)
            addConverterFactory(GsonConverterFactory.create())
            build()
        }.create(Api::class.java)
    }
}