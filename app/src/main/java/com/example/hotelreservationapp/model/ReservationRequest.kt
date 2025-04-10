package com.example.hotelreservationapp.model

data class ReservationRequest(
    val hotel_name: String,
    val check_in: String,
    val check_out: String,
    val customer_name: String,
    val guests_list: List<Guest>,
    val total_price: String
)

data class ReservationResponse(
    val confirmation_number: String
)