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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun ReaderHomeScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            ReaderAppBar(title = "A. Reader", navController = navController)
        },
        floatingActionButton = {
            FABContent {navController.navigate(route = ReaderScreens.SEARCH_SCREEN.name)}
        }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            // add home content
            HomeContent(navController = navController)
        }
    }
}

// create the reading right now composable
@Composable
fun ReadingRightNowArea(books: List<MBook>, navController: NavController) {
// books list row
    ListCard()
}


@Composable
fun HomeContent(
    navController: NavController
) {
    val listOfBooks = listOf(
        MBook("imm", "Kotlin Tutorial", "Mattie", "Simplify kotlin"),
        MBook("im", "Java Tutorial", "Matthew oduamafu", "Simplify kotlin"),
        MBook("mm", "Python Tutorial", "Partey", "Simplify kotlin"),
        MBook("m", "C++ Tutorial", "Anima", "Simplify kotlin"),
        MBook("um", "C# Tutorial", "Balica Stinson", "Simplify kotlin"),
        MBook("iom", "Objective - C Tutorial", "Denexon", "Simplify kotlin"),
        MBook("ipm", "Android Tutorial", "Sefah Lotto", "Simplify kotlin"),
        MBook("ium", "C Tutorial", "Kwamena Iden", "Simplify kotlin"),
        MBook("uum", "Ruby Tutorial", "Dr. Acquah Isaac", "Simplify kotlin")
    )


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
        ReadingRightNowArea(books = listOf(), navController = navController)

        // reading list
        TitleSection(label = "Reading List")

        BookListArea(listOfBooks = listOfBooks, navController = navController)
    }
}

@Composable
fun BookListArea(listOfBooks: List<MBook>, navController: NavController) {
    HorizontalScrollableComponent(listOfBooks) {
        //TODO(on card clicked navigate to details screen)
        Log.d(TAG, "BookListArea: the item clicked = $it")
    }
}

@Composable
fun HorizontalScrollableComponent(listOfBooks: List<MBook>, onCardPressed: (String) -> Unit) {
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(200.dp)
            .horizontalScroll(scrollState)
    ) {
        for (book in listOfBooks) {
            ListCard(book = book) {
                onCardPressed(it)
            }
        }
    }
}
