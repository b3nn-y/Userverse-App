package com.bennysamuel.userverse.userdetailsretrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserDataApiService {
    @GET("/api?")
    suspend fun getUserData(@Query("results") results: String): Response<UserData>
}