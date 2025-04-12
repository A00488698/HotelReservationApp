package com.example.hotelreservationapp.ui.components

//import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hotelreservationapp.model.Hotel
//import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun HotelItem(
    hotel: Hotel,
    isSelected: Boolean,
    onHotelClicked: () -> Unit

) {
//    val context = LocalContext.current
    // 使用状态控制 AlertDialog 是否显示
    var showUnavailableDialog by remember { mutableStateOf(false) }

    if (showUnavailableDialog) {
        AlertDialog(
            onDismissRequest = { showUnavailableDialog = false },
            title = { Text(text = "Attention") },
            text = { Text(text = "No available room") },
            confirmButton = {
                TextButton(onClick = { showUnavailableDialog = false }) {
                    Text(text = "OK")
                }
            }
        )
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                if (hotel.available) {
                    onHotelClicked()
                } else {
//                    Toast.makeText(context, "No available room", Toast.LENGTH_SHORT).show()
                      showUnavailableDialog = true
                }
            },
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 8.dp else 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = hotel.name,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Price: $${hotel.price}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Available: ${if (hotel.available) "Yes" else "No"}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun HotelItemPreview() {
    // 示例数据：可调整字段值测试不同效果
    val testHotel = Hotel(
        id = 1.toString(),
        name = "Test Hotel",
        price = 1000.00,
        available = false,
    )
    HotelItem(
        hotel = testHotel,
        isSelected = false,
        onHotelClicked = { /* 测试时此处不执行任何操作 */ }
    )
}