package com.jeonc.sajuAndUnse.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jeonc.sajuAndUnse.screens.HomeScreen
import com.jeonc.sajuAndUnse.screens.SajuInputScreen
import com.jeonc.sajuAndUnse.screens.SajuResultScreen
import com.jeonc.sajuAndUnse.screens.TodayFortuneScreen
import com.jeonc.sajuAndUnse.screens.ZodiacScreen
import com.jeonc.sajuAndUnse.screens.CompatibilityScreen

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object SajuInput : Screen("saju_input")
    data object SajuResult : Screen("saju_result/{year}/{month}/{day}/{hour}/{gender}") {
        fun createRoute(year: Int, month: Int, day: Int, hour: Int, gender: Int) =
            "saju_result/$year/$month/$day/$hour/$gender"
    }
    data object TodayFortune : Screen("today_fortune")
    data object Zodiac : Screen("zodiac")
    data object Compatibility : Screen("compatibility")
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.SajuInput.route) {
            SajuInputScreen(navController = navController)
        }
        composable(
            route = Screen.SajuResult.route,
            arguments = listOf(
                navArgument("year") { type = NavType.IntType },
                navArgument("month") { type = NavType.IntType },
                navArgument("day") { type = NavType.IntType },
                navArgument("hour") { type = NavType.IntType },
                navArgument("gender") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val year = backStackEntry.arguments?.getInt("year") ?: 1990
            val month = backStackEntry.arguments?.getInt("month") ?: 1
            val day = backStackEntry.arguments?.getInt("day") ?: 1
            val hour = backStackEntry.arguments?.getInt("hour") ?: 0
            val gender = backStackEntry.arguments?.getInt("gender") ?: 0
            SajuResultScreen(
                year = year, month = month, day = day,
                hour = hour, gender = gender,
                navController = navController
            )
        }
        composable(Screen.TodayFortune.route) {
            TodayFortuneScreen(navController = navController)
        }
        composable(Screen.Zodiac.route) {
            ZodiacScreen(navController = navController)
        }
        composable(Screen.Compatibility.route) {
            CompatibilityScreen(navController = navController)
        }
    }
}
