package com.univera.uni_port.service

import com.univera.uni_port.model.Login
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Login {
    @GET("auth.php")
    fun login(@Query("userName") userName: String?,@Query("password") password: String?,@Query("deviceID") deviceID: String?,@Query("playerID") playerID: String?): Call<List<Login>>

}