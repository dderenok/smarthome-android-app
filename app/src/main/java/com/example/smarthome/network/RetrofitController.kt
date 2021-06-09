package com.example.smarthome.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitController {
    @POST("/temperature")
    fun sendTemperature(
        @Query("value")
        value: String,
        @Query("name")
        name: String
    ): Call<Boolean>

    @POST("/light")
    fun sendLight(
        @Query("status")
        status: Boolean,
        @Query("name")
        name: String
    ): Call<Boolean>

    @POST("/room")
    fun createRoom(
        @Query("name")
        name: Boolean,
        @Query("sensors")
        sensors: List<String>
    ): Call<Boolean>

    @GET("/get-light")
    fun getLight(
        @Query("name")
        name: String
    ): Call<Boolean>

    @GET("/get-room")
    fun getRoom(
        @Query("name")
        name: String
    ): Call<Boolean>

    @GET("/get-temperature")
    fun getTemperature(
        @Query("name")
        name: String
    ): Call<Boolean>
}
