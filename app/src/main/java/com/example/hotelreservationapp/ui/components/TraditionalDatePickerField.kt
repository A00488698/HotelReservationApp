package com.example.hotelreservationapp.ui.components

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.widget.DatePicker
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.hotelreservationapp.R
//import java.util.Calendar

@SuppressLint("InflateParams")
@Composable
fun TraditionalDatePickerField(
    label: String,
    // 允许选择的最小日期毫秒值
    minDateMillis: Long,
    // 初始日期设定
    initialYear: Int,
    initialMonth: Int, // 注意：月份从0开始（0代表一月）
    initialDay: Int,
    // 用户选择日期后的回调
    onDateChanged: (year: Int, month: Int, day: Int) -> Unit
) {
//    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        // 使用 AndroidView 加载 XML 中的 DatePicker
        AndroidView(
            factory = { ctx ->
                // 通过 LayoutInflater 加载传统布局文件
                val view = LayoutInflater.from(ctx).inflate(
                    R.layout.traditional_date_picker,
                    null
                )
                val datePicker = view.findViewById<DatePicker>(R.id.traditionalDatePicker)
                // 调用 init 设置初始日期及监听器
                datePicker.init(initialYear, initialMonth, initialDay) { _, year, month, day ->
                    onDateChanged(year, month, day)
                }
                // 设置最小可选日期
                datePicker.minDate = minDateMillis
                datePicker
            },
            update = { view ->
                // 每次重组时确保更新最小日期（如果需要动态调整）
                view.minDate = minDateMillis
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}