package mattie.freelancer.reaader.screens.details

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import mattie.freelancer.reaader.components.ReaderAppBar
import mattie.freelancer.reaader.data.Resource
import mattie.freelancer.reaader.model.Item
import mattie.freelancer.reaader.navigation.ReaderScreens


private const val TAG = "ReaderBookDetailsScreen"


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BookDetailsScreen(
    navController: NavHostController,
    bookId: String,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "Book Details",
                icon = Icons.Filled.ArrowBack,
                showProfile = false,
                navController = navController,
                onBackArrowClicked = { navController.navigate(ReaderScreens.SEARCH_SCREEN.name) }
            )
        }
    ) {
        Surface(
            modifier = Modifier
                .padding(3.dp)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(top = 12.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val bookInfo = produceState<Resource<Item>>(
                    initialValue = Resource.Loading(),
                    producer = { value = viewModel.getBookInfo(bookId) }).value
                Log.d(TAG, "BookDetailsScreen: book info is ${bookInfo.data.toString()}")

                if (bookInfo.data == null) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            strokeWidth = 4.dp,
                            modifier = Modifier.fillMaxSize(.35f)
                        )
                    }
                } else {
                    Text(text = "Book: ${bookInfo.data.volumeInfo?.title}")
                }
            }
        }
    }
}