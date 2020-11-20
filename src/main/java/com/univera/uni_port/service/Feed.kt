package com.univera.uni_port.service

import com.univera.uni_port.model.Feed
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Feed {
    @GET("feed.php")
    fun feed(
        @Query("userName") userName: String?,
        @Query("token") token: String?,
        @Query("filter") filter: Int?
    ): Call<List<Feed>>
}