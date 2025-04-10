package com.example.hotelreservationapp.api
// 导入 Hotel 类
import com.example.hotelreservationapp.model.Hotel
import com.example.hotelreservationapp.model.ReservationRequest
// 导入 ReservationResponse 类
import com.example.hotelreservationapp.model.ReservationResponse

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface HotelApi {
    @GET("api/hotels/")
    suspend fun getHotels(): List<Hotel>

    @POST("api/reservations/")
    suspend fun reserve(@Body request: ReservationRequest): ReservationResponse
}