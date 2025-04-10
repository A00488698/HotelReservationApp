package com.example.hotelreservationapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hotelreservationapp.viewmodel.BookingViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.hotelreservationapp.ui.components.AppHeaderBarWithBack
import com.example.hotelreservationapp.ui.components.HotelItem // 导入新组件
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun Screen2Preview() {
    val mockNavController = rememberNavController()

    Screen2(
        checkIn = "2025-04-10",
        checkOut = "2025-04-15",
        guestsCount = "2",
        customerName = "zhangsan",
        navController = mockNavController
    )
}

@Composable
fun Screen2(
    checkIn: String,
    checkOut: String,
    guestsCount: String,
    customerName: String,
    navController: NavController
) {
    val viewModel: BookingViewModel = viewModel()
    val hotels by viewModel.hotels.collectAsState()
    val selectedHotel = viewModel.selectedHotel
    LaunchedEffect(Unit) {
        viewModel.loadHotels()
    }

    val decodedCustomerName = URLDecoder.decode(customerName, StandardCharsets.UTF_8.toString())
    Scaffold(
        topBar = {
            AppHeaderBarWithBack(
                title = "Hotel Reservation",
                onNavigateUp = { navController.navigateUp() }
            )
        },
        bottomBar = {
            // 固定在底部的 Next 按钮，按钮宽度为父宽度的 80%
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Button(
                    onClick = {
                        selectedHotel?.let { hotel ->
                            val encodedHotelName = URLEncoder.encode(
                                hotel.name,
                                StandardCharsets.UTF_8.toString()
                            )
                            val hotelPrice = hotel.price.toString()
                            navController.navigate(
                                "screen3/$checkIn/$checkOut/$guestsCount/$encodedHotelName/$customerName/$hotelPrice"
                            )
                        }
                    },
                    enabled = selectedHotel != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                ) {
                    Text("Next")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            // 固定顶部欢迎信息
            Text(
                text = "Welcome $decodedCustomerName, displaying hotel for $guestsCount guests staying from $checkIn to $checkOut",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            // 酒店列表区域：如果为空则显示加载状态，若有数据则使用 LazyColumn 滚动显示
            if (hotels.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(hotels) { hotel ->
                        HotelItem(
                            hotel = hotel,
                            isSelected = (selectedHotel == hotel),
                            onHotelClicked = { viewModel.selectHotel(hotel) }
                        )
                    }
                }
            }
        }
    }
}