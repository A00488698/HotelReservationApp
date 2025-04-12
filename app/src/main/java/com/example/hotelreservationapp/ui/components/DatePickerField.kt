package com.example.hotelreservationapp.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePickerField(
    label: String,
    date: String,
    onDateSelected: (String) -> Unit
) {

    // 初始化日期选择器状态：如果已有日期，则转换为时间戳，否则默认使用当前时间
    val initialDateMillis = if (date.isNotEmpty()) {
        LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant().toEpochMilli()
    } else {
        System.currentTimeMillis()
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDateMillis
    )

    // 监听日期选择变化，自动更新日期
    LaunchedEffect(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let { millis ->
            // 获取当前时区偏移（毫秒）
            val offsetMillis = ZoneId.systemDefault().rules.getOffset(Instant.now()).totalSeconds * 1000L
            // 调整时间戳
            val adjustedMillis = millis + offsetMillis
            val selectedDate = Instant.ofEpochMilli(adjustedMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            onDateSelected(selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE))
        }
    }

    // 显示标签和 DatePicker 组件
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))
        DatePicker(state = datePickerState)
    }
}

