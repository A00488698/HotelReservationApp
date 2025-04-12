package com.example.hotelreservationapp.ui.screens

import android.annotation.SuppressLint
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

@SuppressLint("CommitPrefEdits")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Screen1(
    navController: NavController,
) {
    val context = LocalContext.current
    val sharedPrefs = context.getSharedPreferences("hotelPrefs", Context.MODE_PRIVATE)

    var guestCount by remember { mutableStateOf("1") }
    var customerName by remember { mutableStateOf("") }
    var submitAttempted by remember { mutableStateOf(false) }

    // 当前日期
    val calendar = Calendar.getInstance()
    val todayYear = calendar.get(Calendar.YEAR)
    val todayMonth = calendar.get(Calendar.MONTH)
    val todayDay = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.set(todayYear, todayMonth, todayDay)
    val todayMillis = calendar.timeInMillis

    val defaultCheckIn = "$todayYear-${todayMonth + 1}-$todayDay"
    val tomorrowCalendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 1) }
    val defaultCheckOut = "${tomorrowCalendar.get(Calendar.YEAR)}-${tomorrowCalendar.get(Calendar.MONTH) + 1}-${tomorrowCalendar.get(Calendar.DAY_OF_MONTH)}"

    var checkInDate by remember { mutableStateOf(defaultCheckIn) }
    var checkOutDate by remember { mutableStateOf(defaultCheckOut) }
    var dataLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val storedName = sharedPrefs.getString("customerName", "") ?: ""
        val storedGuestCount = sharedPrefs.getString("guestCount", "1") ?: "1"
        customerName = storedName
        guestCount = storedGuestCount

        val storedCheckIn = sharedPrefs.getString("checkInDate", defaultCheckIn) ?: defaultCheckIn
        val storedCheckOut = sharedPrefs.getString("checkOutDate", defaultCheckOut) ?: defaultCheckOut
        checkInDate = storedCheckIn
        checkOutDate = storedCheckOut

        println("DEBUG: Loaded checkInDate = $storedCheckIn, checkOutDate = $storedCheckOut")

        dataLoaded = true
    }

    val checkoutMinMillis = if (checkInDate.isNotEmpty()) {
        val parts = checkInDate.split("-")
        val year = parts[0].toInt()
        val month = parts[1].toInt() - 1
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

    // 只有加载完成后才渲染 UI
    if (dataLoaded) {
        // 解析 checkInDate 和 checkOutDate
        val parsedCheckIn = try {
            val parts = checkInDate.split("-").map { it.toInt() }
            Triple(parts[0], parts[1] - 1, parts[2])
        } catch (e: Exception) {
            Triple(todayYear, todayMonth, todayDay)
        }
        val parsedCheckOut = try {
            val parts = checkOutDate.split("-").map { it.toInt() }
            Triple(parts[0], parts[1] - 1, parts[2])
        } catch (e: Exception) {
            Triple(tomorrowCalendar.get(Calendar.YEAR), tomorrowCalendar.get(Calendar.MONTH), tomorrowCalendar.get(Calendar.DAY_OF_MONTH))
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
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
            ) {
                TraditionalDatePickerField(
                    label = "Check in Date",
                    minDateMillis = todayMillis,
                    initialYear = parsedCheckIn.first,
                    initialMonth = parsedCheckIn.second,
                    initialDay = parsedCheckIn.third
                ) { year, month, day ->
                    checkInDate = "$year-${month + 1}-$day"
                    sharedPrefs.edit(commit = true) { putString("checkInDate", checkInDate) }
                }
                Spacer(modifier = Modifier.height(16.dp))

                TraditionalDatePickerField(
                    label = "Check out Date",
                    minDateMillis = checkoutMinMillis,
                    initialYear = parsedCheckOut.first,
                    initialMonth = parsedCheckOut.second,
                    initialDay = parsedCheckOut.third
                ) { year, month, day ->
                    checkOutDate = "$year-${month + 1}-$day"
                    sharedPrefs.edit(commit = true) { putString("checkOutDate", checkOutDate) }
                }
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = guestCount,
                    onValueChange = { guestCount = it },
                    label = { Text("Number of Guests") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

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

                Button(
                    onClick = {
                        if (checkInDate.isNotEmpty() && checkOutDate.isNotEmpty() && customerName.trim().isNotEmpty()) {
                            sharedPrefs.edit().apply {
                                putString("customerName", customerName)
                                putString("guestCount", guestCount)
                                putString("checkInDate", checkInDate)
                                putString("checkOutDate", checkOutDate)
                            }
                            val encodedCustomerName = URLEncoder.encode(
                                customerName,
                                StandardCharsets.UTF_8.toString()
                            )
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
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun Screen1Preview() {
    HotelReservationAppTheme {
        Screen1(navController = rememberNavController())
    }
}



