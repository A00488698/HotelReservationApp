// app/src/main/java/com/example/hotelreservationapp/navigation/NavGraph.kt
package com.example.hotelreservationapp.navigation

import com.example.hotelreservationapp.ui.screens.Screen2
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.hotelreservationapp.ui.screens.Screen1
import com.example.hotelreservationapp.ui.screens.Screen3
import com.example.hotelreservationapp.ui.screens.Screen4


@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.mainNavGraph(navController: NavController) {
    // Screen1 (不需要参数)
    composable("screen1") {
        Screen1(navController = navController)
    }

    // Screen2 (带参数的路由)
    composable(
        "screen2/{checkIn}/{checkOut}/{guests}/{customerName}",
        arguments = listOf(
            navArgument("checkIn") { type = NavType.StringType },
            navArgument("checkOut") { type = NavType.StringType },
            navArgument("guests") { type = NavType.StringType },
            navArgument("customerName") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        Screen2(
            checkIn = backStackEntry.arguments?.getString("checkIn") ?: "",
            checkOut = backStackEntry.arguments?.getString("checkOut") ?: "",
            guestsCount = backStackEntry.arguments?.getString("guests") ?: "1",
            customerName = backStackEntry.arguments?.getString("customerName") ?: "",
            navController = navController
        )
    }
    composable(
        "screen3/{checkIn}/{checkOut}/{guests}/{encodedHotelName}/{customerName}/{hotelPrice}",
        arguments = listOf(
            navArgument("checkIn") { type = NavType.StringType },
            navArgument("checkOut") { type = NavType.StringType },
            navArgument("guests") { type = NavType.StringType },
            navArgument("encodedHotelName") { type = NavType.StringType },
            navArgument("customerName") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        Screen3(
            checkIn = backStackEntry.arguments?.getString("checkIn") ?: "",
            checkOut = backStackEntry.arguments?.getString("checkOut") ?: "",
            guestsCount = backStackEntry.arguments?.getString("guests") ?: "1",
            encodedHotelName = backStackEntry.arguments?.getString("encodedHotelName") ?: "",
            customerName = backStackEntry.arguments?.getString("customerName") ?: "",
            hotelPrice = backStackEntry.arguments?.getString("hotelPrice") ?: "",
            navController = navController
        )
    }
    composable(
        route = "screen4/{confirmationNumber}",
        arguments = listOf(navArgument("confirmationNumber") { type = NavType.StringType })
    ) { backStackEntry ->
        Screen4(
            confirmationNumber = backStackEntry.arguments?.getString("confirmationNumber") ?: "",
            navController = navController
        )
    }
}