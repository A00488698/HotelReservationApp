package com.example.hotelreservationapp.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hotelreservationapp.ui.theme.HotelReservationAppTheme
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import com.example.hotelreservationapp.ui.components.AppHeaderBarSimple
import com.example.hotelreservationapp.ui.components.DatePickerField

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Screen1(
    navController: NavController,
) {
    var checkInDate by remember { mutableStateOf("") }
    var checkOutDate by remember { mutableStateOf("") }
    var guestCount by remember { mutableStateOf("1") }
    var customerName by remember { mutableStateOf("") }
    var submitAttempted by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            AppHeaderBarSimple(title = "Hotel Reservation")
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .verticalScroll(rememberScrollState())  // 允许垂直滚动
                .fillMaxSize()
        ) {
            // 选择入住日期
            DatePickerField(
                label = "Check in Date",
                date = checkInDate,
                onDateSelected = { checkInDate = it }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 选择退房日期
            DatePickerField(
                label = "Check out Date",
                date = checkOutDate,
                onDateSelected = { checkOutDate = it }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 客人数量输入
            OutlinedTextField(
                value = guestCount,
                onValueChange = { guestCount = it },
                label = { Text("Number of Guests") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 预订者姓名输入，并添加错误提示
            OutlinedTextField(
                value = customerName,
                onValueChange = { customerName = it },
                label = { Text("Enter Your Name") },
                modifier = Modifier.fillMaxWidth(),
                isError = submitAttempted && customerName.trim().isEmpty()
            )
            if (submitAttempted && customerName.trim().isEmpty()) {
                Text(
                    text = "Name cannot be empty",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 搜索按钮
            Button(
                onClick = {
                    if (checkInDate.isNotEmpty() && checkOutDate.isNotEmpty() && customerName.trim().isNotEmpty()) {
                        val encodedCustomerName = URLEncoder.encode(customerName, StandardCharsets.UTF_8.toString())
                        navController.navigate("screen2/$checkInDate/$checkOutDate/$guestCount/$encodedCustomerName")
                    } else {
                        submitAttempted = true
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = checkInDate.isNotEmpty() && checkOutDate.isNotEmpty()
            ) {
                Text("Search")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun Screen1Preview() {
    HotelReservationAppTheme { // 确保项目中已定义该主题
        Screen1(navController = rememberNavController())
    }
}