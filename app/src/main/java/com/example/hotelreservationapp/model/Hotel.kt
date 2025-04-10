package com.example.hotelreservationapp.model

data class Hotel(
    val id: String,
    val name: String,
    val price: Double,
    val available: Boolean
)