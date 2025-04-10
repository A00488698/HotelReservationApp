package com.example.hotelreservationapp
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.hotelreservationapp.ui.theme.HotelReservationAppTheme
import com.example.hotelreservationapp.navigation.mainNavGraph

class MainActivity : ComponentActivity()  {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HotelReservationAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "screen1") {
                        mainNavGraph(navController)
                    } // 委托给 NavGraph
                }
            }
        }
    }
}