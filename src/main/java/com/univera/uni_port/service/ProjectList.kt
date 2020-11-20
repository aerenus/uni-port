package com.univera.uni_port.service

import com.univera.uni_port.model.ProjectList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ProjectList {
    @GET("project.php")
    fun projectList(
        @Query("userName") userName: String,@Query("token") token: String
    ): Call<List<ProjectList>>
}