package com.example.coroutinesapp

import retrofit2.Call
import retrofit2.http.GET

// to deal with the JSON object in the programming area
interface APIInterface {
    @GET("https://api.adviceslip.com/advice") //all the API url
    fun doGetListResources(): Call<Advices?>?
}