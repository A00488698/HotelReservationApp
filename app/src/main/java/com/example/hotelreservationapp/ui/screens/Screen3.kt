package com.example.hotelreservationapp.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hotelreservationapp.viewmodel.BookingViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.hotelreservationapp.viewmodel.MockBookingViewModel
import com.example.hotelreservationapp.model.Guest
import com.example.hotelreservationapp.ui.theme.HotelBookingAppTheme
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import com.example.hotelreservationapp.ui.components.AppHeaderBarWithBack
import com.example.hotelreservationapp.ui.components.GuestForm

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun Screen3Preview() {
    val mockNavController = rememberNavController()
    val mockViewModel: BookingViewModel = MockBookingViewModel()
    HotelBookingAppTheme {
        Screen3(
            checkIn = "2025-04-10",
            checkOut = "2025-04-15",
            guestsCount = "2",
            encodedHotelName = "Hotel+Amano", // URL编码后的酒店名称示例
            customerName = "lisi",
            hotelPrice = "100.0",  // 示例酒店单价
            navController = mockNavController,
            viewModel = mockViewModel  // 传入模拟数据
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GuestFormPreview() {
    GuestForm(
        index = 1,
        guest = Guest(guest_name = "John Doe", gender = "Male"),
        onGuestChanged = { /* 空实现 */ },
        showError = false
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Screen3(
    checkIn: String,
    checkOut: String,
    guestsCount: String,
    encodedHotelName: String,
    customerName: String,
    hotelPrice: String,  // 酒店单价
    navController: NavController,
    viewModel: BookingViewModel = viewModel()
) {
    val hotelName = URLDecoder.decode(encodedHotelName, StandardCharsets.UTF_8.toString())
    val guestCount = guestsCount.toIntOrNull() ?: 1

    // 计算入住天数
    val formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-M-d")
    val checkInDate = LocalDate.parse(checkIn, formatter)
    val checkOutDate = LocalDate.parse(checkOut, formatter)
    val daysBetween = ChronoUnit.DAYS.between(checkInDate, checkOutDate)
    // 将酒店单价转换为 Double 并计算总费用：单价 * 天数 * 客人数量
    val price = hotelPrice.toDoubleOrNull() ?: 0.0
    val totalCost = price * daysBetween * guestCount

    // 同步传入参数到 ViewModel
    LaunchedEffect(hotelName, checkIn, checkOut, guestsCount, customerName) {
        viewModel.selectedHotelName = hotelName
        viewModel.checkInDate = checkIn
        viewModel.checkOutDate = checkOut
        viewModel.guestCount = guestsCount
        viewModel.customerName = customerName
    }

    // 使用 local state 保存所有客人的信息
    val guestListState = remember { mutableStateListOf<Guest>() }
    if (guestListState.size != guestCount) {
        guestListState.clear()
        repeat(guestCount) {
            guestListState.add(Guest("", "Male"))
        }
    }

    // 用于控制是否显示错误状态（提交后触发）
    var submitAttempted by remember { mutableStateOf(false) }

    // 验证所有 Guest 的姓名是否非空
    val areGuestNamesValid = guestListState.all { it.guest_name.trim().isNotEmpty() }

    Scaffold(
        topBar = {
            AppHeaderBarWithBack(
                title = "Hotel Reservation",
                onNavigateUp = { navController.navigateUp() }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "Total Cost: \$${totalCost}",
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        if (!areGuestNamesValid) {
                            submitAttempted = true
                        } else {
                            viewModel.guests = guestListState.toMutableList()
                            viewModel.totalPrice = totalCost.toString()
                            viewModel.submitReservation(navController)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Submit Reservation")
                }
            }
        }
    ) { innerPadding ->
        // 使用 Column 固定 Header，LazyColumn 滚动 GuestForm 列表
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 固定 Header 区域
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Your selected hotel is $hotelName",
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                val dateFormatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MMM-dd")
                val formattedCheckIn = checkInDate.format(dateFormatter)
                val formattedCheckOut = checkOutDate.format(dateFormatter)
                Text(
                    text = "Dates of stay: $formattedCheckIn to $formattedCheckOut",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Hotel rate: $price per day",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text("Number of Guests: $guestsCount", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.width(50.dp))
                    Text("Customer: $customerName", style = MaterialTheme.typography.bodyMedium)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // 滚动区域，仅 GuestForm 列表滚动
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                itemsIndexed(guestListState) { index, guest ->
                    GuestForm(
                        index = index + 1,
                        guest = guest,
                        onGuestChanged = { updatedGuest ->
                            guestListState[index] = updatedGuest
                        },
                        showError = submitAttempted
                    )
                }
            }
        }
    }
}

