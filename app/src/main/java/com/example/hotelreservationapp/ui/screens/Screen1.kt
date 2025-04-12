package com.example.hotelreservationapp.ui.screens

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hotelreservationapp.ui.theme.HotelReservationAppTheme
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import com.example.hotelreservationapp.ui.components.AppHeaderBarSimple
import androidx.core.content.edit
import com.example.hotelreservationapp.ui.components.TraditionalDatePickerField
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Screen1(
    navController: NavController,
) {
    // 1. 获取SharedPreferences对象
    val context = LocalContext.current
    val sharedPrefs = context.getSharedPreferences("hotelPrefs", Context.MODE_PRIVATE)



    var guestCount by remember { mutableStateOf("1") }
    var customerName by remember { mutableStateOf("") }
    var submitAttempted by remember { mutableStateOf(false) }

    // 2. 在Composable初始化时读取已经保存过的数据
    LaunchedEffect(Unit) {
        val storedName = sharedPrefs.getString("customerName", "") ?: ""
        val storedGuestCount = sharedPrefs.getString("guestCount", "1") ?: "1"
        customerName = storedName
        guestCount = storedGuestCount
    }
    // 获取当前日期
    val calendar = Calendar.getInstance()
    val todayYear = calendar.get(Calendar.YEAR)
    val todayMonth = calendar.get(Calendar.MONTH)
    val todayDay = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.set(todayYear, todayMonth, todayDay)
    val todayMillis = calendar.timeInMillis

    // 定义默认的入住日期为今天，退房日期为明天
    val defaultCheckIn = "$todayYear-${todayMonth + 1}-$todayDay"
    val tomorrowCalendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 1) }
    val defaultCheckOut = "${tomorrowCalendar.get(Calendar.YEAR)}-${tomorrowCalendar.get(Calendar.MONTH) + 1}-${tomorrowCalendar.get(Calendar.DAY_OF_MONTH)}"

    var checkInDate by remember { mutableStateOf(defaultCheckIn) }
    var checkOutDate by remember { mutableStateOf(defaultCheckOut) }

    // 计算 Check out 默认最小日期：若已选 Check in，则为 Check in 日期＋1，否则为明天
    val checkoutMinMillis = if (checkInDate.isNotEmpty()) {
        // 解析 checkInDate 字符串：假设格式 "yyyy-M-d"
        val parts = checkInDate.split("-")
        val year = parts[0].toInt()
        val month = parts[1].toInt() - 1 // 注意月份从0开始
        val day = parts[2].toInt()
        Calendar.getInstance().apply {
            set(year, month, day)
            add(Calendar.DAY_OF_MONTH, 1)
        }.timeInMillis
    } else {
        Calendar.getInstance().apply {
            set(todayYear, todayMonth, todayDay)
            add(Calendar.DAY_OF_MONTH, 1)
        }.timeInMillis
    }

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
//            // 选择入住日期
//            DatePickerField(
//                label = "Check in Date",
//                date = checkInDate,
//                onDateSelected = { checkInDate = it }
//            )
            // 使用传统 DatePicker 选择 Check in 日期
            TraditionalDatePickerField(
                label = "Check in Date",
                minDateMillis = todayMillis,
                initialYear = todayYear,
                initialMonth = todayMonth,
                initialDay = todayDay
            ) { year, month, day ->
                // 构建字符串：月份记得 +1
                checkInDate = "$year-${month + 1}-$day"
            }
            Spacer(modifier = Modifier.height(16.dp))

//            // 选择退房日期
//            DatePickerField(
//                label = "Check out Date",
//                date = checkOutDate,
//                onDateSelected = { checkOutDate = it }
//            )
            // 使用传统 DatePicker 选择 Check out 日期
            TraditionalDatePickerField(
                label = "Check out Date",
                minDateMillis = checkoutMinMillis,
                // 默认初始日期：如果 check in 已选，则用 check in 日期+1，否则明天
                initialYear = if (checkInDate.isNotEmpty()) {
                    val parts = checkInDate.split("-")
                    parts[0].toInt()
                } else todayYear,
                initialMonth = if (checkInDate.isNotEmpty()) {
                    val parts = checkInDate.split("-")
                    // 注意：月份从0开始
                    parts[1].toInt() - 1
                } else todayMonth,
                initialDay = if (checkInDate.isNotEmpty()) {
                    val parts = checkInDate.split("-")
                    parts[2].toInt() + 1
                } else todayDay + 1
            ) { year, month, day ->
                checkOutDate = "$year-${month + 1}-$day"
            }
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
                        // 3. 点击按钮时，保存姓名和客人数到SharedPreferences
                        sharedPrefs.edit() {
                            putString("customerName", customerName)
                            putString("guestCount", guestCount)
                        }

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

