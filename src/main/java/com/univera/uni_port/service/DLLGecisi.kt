package com.univera.uni_port.service

import com.univera.uni_port.model.DLLGecisi
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DLLGecisi {
    @GET("dllgecisi.php")
    fun postDLL(
        @Query("userName") userName: String?,
        @Query("token") token: String?,
        @Query("proje") proje: String?,
        @Query("tip") tip: String?,
        @Query("tarih") tarih: String?,
        @Query("yorum") yorum: String?

    ): Call<List<DLLGecisi>>
}