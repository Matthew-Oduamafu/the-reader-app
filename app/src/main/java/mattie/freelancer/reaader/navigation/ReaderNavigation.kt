package mattie.freelancer.reaader.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import mattie.freelancer.reaader.screens.ReaderSplashScreen
import mattie.freelancer.reaader.screens.details.BookDetailsScreen
import mattie.freelancer.reaader.screens.details.DetailsViewModel
import mattie.freelancer.reaader.screens.home.HomeScreenViewModel
import mattie.freelancer.reaader.screens.home.ReaderHomeScreen
import mattie.freelancer.reaader.screens.login.ReaderLoginScreen
import mattie.freelancer.reaader.screens.search.ReaderBookSearchScreen
import mattie.freelancer.reaader.screens.search.SearchScreenViewModel
import mattie.freelancer.reaader.screens.stats.ReaderStatsScreen
import mattie.freelancer.reaader.screens.update.BookUpdateScreen

@RequiresApi(Build.VERSION_CODES.N)
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
            val viewModel: HomeScreenViewModel = hiltViewModel()
            ReaderHomeScreen(navController = navController, viewModel)
        }

        // navigate to login screen
        composable(ReaderScreens.LOGIN_SCREEN.name) {
            ReaderLoginScreen(navController = navController)
        }

        // navigate to Book details screen
        val detailName = ReaderScreens.DETAILS_SCREEN.name
        composable(
            "$detailName/{bookId}",
            arguments = listOf(navArgument("bookId", builder = { type = NavType.StringType }))
        ) {
            val detailsViewModel = hiltViewModel<DetailsViewModel>()
            it.arguments?.getString("bookId").let { bookId ->
                if (bookId != null) {
                    BookDetailsScreen(
                        navController = navController,
                        bookId = bookId,
                        viewModel = detailsViewModel
                    )
                }
            }
        }

        // navigate to Reader Book Search screen
        composable(ReaderScreens.SEARCH_SCREEN.name) {
            val searchScreenViewModel: SearchScreenViewModel = hiltViewModel()
            ReaderBookSearchScreen(navController = navController, searchScreenViewModel)
        }

        // navigate to Reader Stats Screen
        composable(ReaderScreens.READER_STATS_SCREEN.name) {
            val homeViewModel = hiltViewModel<HomeScreenViewModel>()
            ReaderStatsScreen(navController = navController, viewModel = homeViewModel)
        }

        // navigate to Reader Book Update screen
        val updateName = ReaderScreens.UPDATE_SCREEN.name
        composable("$updateName/{bookItemId}", arguments = listOf(navArgument("bookItemId") {
            type = NavType.StringType
        })) { navBackStackEntry ->
            navBackStackEntry.arguments?.getString("bookItemId").let {
                BookUpdateScreen(navController = navController, bookItemId = it)
            }
        }
    }
}