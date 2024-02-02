package com.bennysamuel.userverse.userdetailsretrofit

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object{

        val baseUrl = "https://randomuser.me/"

        fun getRestrofitInstance(): Retrofit{

            return Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create(GsonBuilder().create())).build()


        }
    }
}