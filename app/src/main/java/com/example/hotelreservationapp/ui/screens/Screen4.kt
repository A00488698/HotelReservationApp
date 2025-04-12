package com.example.hotelreservationapp.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hotelreservationapp.ui.components.AppHeaderBarSimple


@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun Screen4Preview() {
    Screen4(confirmationNumber = "123456", navController = rememberNavController())
}
@Composable
fun Screen4(
    confirmationNumber: String,
    navController: NavController,

) {
    Scaffold(
        topBar = {
            AppHeaderBarSimple(title = "Hotel Reservation")
        }
    ) {
        innerPadding ->
        // 确保 viewModel.confirmationNumber 已经由后台返回数据填充
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Thank you for your reservation, your confirmation number $confirmationNumber",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    // 导航回首页，例如 "screen1"，并清空当前堆栈
                    navController.navigate("screen1") {
                        popUpTo("screen1") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Return to Home")
            }
        }
    }

}