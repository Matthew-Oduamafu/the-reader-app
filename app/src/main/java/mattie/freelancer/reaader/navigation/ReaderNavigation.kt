package mattie.freelancer.reaader.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mattie.freelancer.reaader.screens.ReaderSplashScreen
import mattie.freelancer.reaader.screens.details.BookDetailsScreen
import mattie.freelancer.reaader.screens.home.ReaderHomeScreen
import mattie.freelancer.reaader.screens.login.ReaderLoginScreen
import mattie.freelancer.reaader.screens.search.ReaderBookSearchScreen
import mattie.freelancer.reaader.screens.search.SearchScreenViewModel
import mattie.freelancer.reaader.screens.stats.ReaderStatsScreen
import mattie.freelancer.reaader.screens.update.BookUpdateScreen

@Composable
fun ReaderNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ReaderScreens.SPLASH_SCREEN.name) {
        // navigate to splash screen
        composable(ReaderScreens.SPLASH_SCREEN.name) {
            ReaderSplashScreen(navController = navController)
        }

        // navigate to Home screen
        composable(ReaderScreens.READER_HOME_SCREEN.name) {
            ReaderHomeScreen(navController = navController)
        }

        // navigate to login screen
        composable(ReaderScreens.LOGIN_SCREEN.name) {
            ReaderLoginScreen(navController = navController)
        }

        // navigate to Book details screen
        composable(ReaderScreens.DETAILS_SCREEN.name) {
            BookDetailsScreen(navController = navController)
        }

        // navigate to Reader Book Search screen
        composable(ReaderScreens.SEARCH_SCREEN.name) {
            val searchScreenViewModel: SearchScreenViewModel = hiltViewModel()
            ReaderBookSearchScreen(navController = navController, searchScreenViewModel)
        }

        // navigate to Reader Stats Screen
        composable(ReaderScreens.READER_STATS_SCREEN.name) {
            ReaderStatsScreen(navController = navController)
        }

        // navigate to Reader Book Update screen
        composable(ReaderScreens.UPDATE_SCREEN.name) {
            BookUpdateScreen(navController = navController)
        }
    }
}