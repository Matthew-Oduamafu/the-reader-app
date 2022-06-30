package mattie.freelancer.reaader.screens.update

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.navigation.NavHostController
import mattie.freelancer.reaader.components.ReaderAppBar
import mattie.freelancer.reaader.data.DataOrException
import mattie.freelancer.reaader.model.MBook
import mattie.freelancer.reaader.screens.home.HomeScreenViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BookUpdateScreen(
    navController: NavHostController,
    bookItemId: String?,
    viewModel: HomeScreenViewModel
) {
    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "Update Book",
                icon = Icons.Filled.ArrowBack,
                showProfile = false,
                navController = navController
            )
        }
    ) {
        val bookInfo =
            produceState<DataOrException<List<MBook>, Boolean, Exception>>(
                initialValue = DataOrException(
                    emptyList(), true, Exception("")
                )
            ) {
                value = viewModel.data.value
            }.value
    }
}