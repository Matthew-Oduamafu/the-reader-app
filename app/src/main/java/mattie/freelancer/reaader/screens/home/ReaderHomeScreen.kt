package mattie.freelancer.reaader.screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import mattie.freelancer.reaader.R
import mattie.freelancer.reaader.components.FABContent
import mattie.freelancer.reaader.components.ListCard
import mattie.freelancer.reaader.components.ReaderAppBar
import mattie.freelancer.reaader.components.TitleSection
import mattie.freelancer.reaader.model.MBook
import mattie.freelancer.reaader.navigation.ReaderScreens

private const val TAG = "ReaderHomeScreen"

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ReaderHomeScreen(
    navController: NavHostController,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            ReaderAppBar(title = "A. Reader", navController = navController)
        },
        floatingActionButton = {
            FABContent { navController.navigate(route = ReaderScreens.SEARCH_SCREEN.name) }
        }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            // add home content
            HomeContent(navController = navController, viewModel)
        }
    }
}

// create the reading right now composable
@Composable
fun ReadingRightNowArea(books: List<MBook>, navController: NavController) {
// books list row

    val booksReading = books.filter { mBook: MBook ->
        mBook.startedReading != null && mBook.finishedReading == null
    }
    HorizontalScrollableComponent(listOfBooks = booksReading, onCardPressed = {
        Log.d(TAG, "ReadingRightNowArea: called with $it")
        navController.navigate(ReaderScreens.UPDATE_SCREEN.name + "/$it")
    })

}


@Composable
fun HomeContent(
    navController: NavController,
    viewModel: HomeScreenViewModel
) {
    var listOfBooks = emptyList<MBook>()
    val currentUser = FirebaseAuth.getInstance().currentUser

    if (!viewModel.data.value.data.isNullOrEmpty()) {
        if (currentUser != null) {
            listOfBooks = viewModel.data.value.data!!.toList().filter { mBook ->
                mBook.userId == currentUser.uid
            }
        }
        Log.d(TAG, "HomeContent: user's books $listOfBooks")
    }


    // get current user name
    val email = FirebaseAuth.getInstance().currentUser?.email
    val currentUserName = if (!email.isNullOrEmpty()) email.split("@")[0] else "N/A"
    Column(
        modifier = Modifier.padding(2.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // reading status and profile row
        Row(
            modifier = Modifier.align(Alignment.Start)
        ) {
            TitleSection(label = stringResource(id = R.string.reading_activity_label))

            Spacer(modifier = Modifier.fillMaxWidth(.7f))


            Column(modifier = Modifier
                .clickable { navController.navigate(route = ReaderScreens.READER_STATS_SCREEN.name) }
            ) {
                // user profile
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .size(45.dp),
                    tint = MaterialTheme.colors.secondaryVariant
                )

                //user name
                Text(
                    text = currentUserName,
                    modifier = Modifier.padding(2.dp),
                    style = MaterialTheme.typography.overline,
                    color = Color.Red,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Divider()
            }
        }
        // row of books in reading process
        ReadingRightNowArea(books = listOfBooks, navController = navController)

        // reading list
        TitleSection(label = "Reading List")

        BookListArea(listOfBooks = listOfBooks, navController = navController)
    }
}

@Composable
fun BookListArea(listOfBooks: List<MBook>, navController: NavController) {

    val addedBooks = listOfBooks.filter { mBook: MBook ->
        mBook.startedReading == null && mBook.finishedReading == null
    }

    HorizontalScrollableComponent(addedBooks) {
        Log.d(TAG, "BookListArea: the item clicked = $it")
        navController.navigate(ReaderScreens.UPDATE_SCREEN.name + "/$it")
    }
}

@Composable
fun HorizontalScrollableComponent(
    listOfBooks: List<MBook>,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onCardPressed: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(200.dp)
            .horizontalScroll(scrollState)
    ) {
        if (viewModel.data.value.loading == true) {
            Column(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = Color.Green.copy(0.55f))
            }
        } else {
            if (listOfBooks.isEmpty()) {
                Surface(modifier = Modifier.padding(23.dp)) {
                    Text(
                        text = "No Books Found. Add a book",
                        style = TextStyle(
                            color = Color.Red.copy(0.4f),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    )
                }
            } else {

                for (book in listOfBooks) {
                    ListCard(book = book) {
                        onCardPressed(book.googleBookId.toString())
                    }
                }
            }
        }
    }
}
