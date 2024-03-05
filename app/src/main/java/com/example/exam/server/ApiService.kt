package com.example.exam.server

import com.example.exam.model.Car
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("/cars/")
    fun getEntities(): Call<List<Car>>

    @GET("/search/")
    fun searchEntities(): Call<List<Car>>

    @GET("/car/{id}/")
    fun getEntity(@Path("id") id: Int): Call<Car>

    @POST("/car/")
    fun createEntity(@Body entity: Car): Call<Car>

    @PUT("/car/{id}/")
    fun updateEntity(@Path("id") id: Int, @Body entity: Car): Call<Car>

    @DELETE("/car/{id}/")
    fun deleteEntity(@Path("id") id: Int): Call<Void>

    @GET("/carstypes/")
    fun getCarsTypes(): Call<List<Car>>

    @PUT("/requestcar/{type}/")
    fun requestCar(@Path("type") id: String): Call<Car>

    @GET("/carorders/")
    fun getAnalitics(): Call<List<Car>>






}