package com.example.hotelreservationapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotelreservationapp.api.HotelApi
import com.example.hotelreservationapp.api.RetrofitClient
import com.example.hotelreservationapp.model.*
import com.example.hotelreservationapp.model.ReservationRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavController

open class BookingViewModel : ViewModel() {
    // API 接口
    private val api = RetrofitClient.instance.create(HotelApi::class.java)

    // 状态管理
    private val _hotels = MutableStateFlow<List<Hotel>>(emptyList())
    val hotels = _hotels.asStateFlow()
    var selectedHotelName: String = ""

    var checkInDate: String = ""
    var checkOutDate: String = ""
    var guestCount: String = "1"
    var selectedHotel by mutableStateOf<Hotel?>(null)
        private set
    var guests: MutableList<Guest> = mutableListOf()
    var customerName: String = ""
    var totalPrice: String = ""

    // 加载酒店列表
    fun loadHotels() {
        viewModelScope.launch {
            _hotels.value = api.getHotels()
        }
    }
    // 新增方法：更新选中的酒店
    fun selectHotel(hotel: Hotel) {
        selectedHotel = hotel
    }

    // 提交预订
    fun submitReservation(navController: NavController) {
        viewModelScope.launch {
            try {
                val request = ReservationRequest(
                    hotel_name = selectedHotelName,
                    check_in = checkInDate,
                    check_out = checkOutDate,
                    customer_name = customerName, // 确保预定者姓名在 ViewModel 中有值
                    guests_list = guests,
                    total_price = totalPrice
                )

                val response = api.reserve(request)
                // 假设返回的数据包含 confirmation_number 字段
                val confirmation = response.confirmation_number
                // 导航时把确认号作为参数传过去
                navController.navigate("screen4/$confirmation")
            } catch (e: Exception) {
                // 处理错误
            }
        }
    }
}
