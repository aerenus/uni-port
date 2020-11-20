package com.univera.uni_port.service

import com.univera.uni_port.model.Settings
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Settings {

    @GET("settings.php")
    fun login(
        @Query("userName") userName: String?,
        @Query("token") token: String?,
        @Query("set1") set1: Int?,
        @Query("set2") set2: Int?,
        @Query("set3") set3: Int?,
        @Query("set4") set4: Int?,
        @Query("set5") set5: Int?
    ): Call<List<Settings>>

}