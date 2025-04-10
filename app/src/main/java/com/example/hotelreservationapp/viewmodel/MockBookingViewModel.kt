// 文件路径: app/src/main/java/com/example/hotelreservationapp/viewmodel/MockBookingViewModel.kt
package com.example.hotelreservationapp.viewmodel

import com.example.hotelreservationapp.model.Guest
import com.example.hotelreservationapp.model.Hotel

class MockBookingViewModel : BookingViewModel() {
    init {
        // 初始化模拟数据
        checkInDate = "2025-04-10"
        checkOutDate = "2025-04-15"
        guestCount = "2"
        val hotel = Hotel(
            id = "1",
            name = "Grand Hotel",
            price = 199.0,
            available = true
        )

        selectHotel(hotel)
        guests = mutableListOf( // 初始化空的客人列表
            Guest(guest_name = "John Doe", gender = "Male"),
            Guest(guest_name = "Jane Doe", gender = "Female")
        )
    }
}