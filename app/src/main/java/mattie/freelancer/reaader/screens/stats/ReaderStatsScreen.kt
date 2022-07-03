package mattie.freelancer.reaader.screens.stats

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.sharp.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import mattie.freelancer.reaader.R
import mattie.freelancer.reaader.components.ReaderAppBar
import mattie.freelancer.reaader.model.MBook
import mattie.freelancer.reaader.screens.home.HomeScreenViewModel
import mattie.freelancer.reaader.utils.formatDate
import java.util.*

private const val TAG = "ReaderStatsScreen"


@RequiresApi(Build.VERSION_CODES.N)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ReaderStatsScreen(
    navController: NavHostController,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    Log.d(TAG, "ReaderStatsScreen: called")
    var books: List<MBook>
    val currentUser = FirebaseAuth.getInstance().currentUser

    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "Book Stats",
                icon = Icons.Filled.ArrowBack,
                showProfile = false,
                navController = navController,
                onBackArrowClicked = { navController.popBackStack() }
            )
        }
    ) {
        Surface {
            // only show books by this user that have been read
            books = if (!viewModel.data.value.data.isNullOrEmpty()) {
                viewModel.data.value.data!!.filter { mBook: MBook ->
                    mBook.userId == currentUser?.uid
                }
            } else {
                emptyList()
            }

            Column(modifier = Modifier.padding(8.dp)) {
                Row(modifier = Modifier.padding(8.dp)) {
                    Box(
                        modifier = Modifier
                            .size(45.dp)
                            .padding(2.dp)
                    ) {
                        Icon(imageVector = Icons.Sharp.Person, contentDescription = "icon")
                    }
                    if (currentUser != null) {
                        Text(
                            text = "Hi, ${
                                currentUser.email.toString().split("@")[0].replaceFirstChar {
                                    if (it.isLowerCase()) it.titlecase(
                                        Locale.ROOT
                                    ) else it.toString()
                                }
                            }"
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = CircleShape,
                    elevation = 5.dp
                ) {
                    val readBookList: List<MBook> =
                        if (!viewModel.data.value.data.isNullOrEmpty()) {
                            books.filter { mBook: MBook ->
                                (mBook.userId == currentUser?.uid) && mBook.finishedReading != null
                            }
                        } else {
                            emptyList()
                        }

                    val readingBooks: List<MBook> = books.filter { mBook: MBook ->
                        (mBook.startedReading != null) && (mBook.finishedReading == null)
                    }
                    Column(
                        modifier = Modifier.padding(start = 25.dp, top = 4.dp, bottom = 4.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(text = "Your Stats", style = MaterialTheme.typography.h5)

                        Divider()
                        var tmp = readingBooks.size
                        Text(text = "You're reading: $tmp book${if (tmp <= 1) "" else "s"}")
                        tmp = readBookList.size
                        Text(text = "You've read: $tmp book${if (tmp <= 1) "" else "s"}")
                    }
                }

                if (viewModel.data.value.loading == true) {
                    LinearProgressIndicator()
                } else {
                    Divider()

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        // filter books by finished ones
                        val readBooks: List<MBook> =
                            if (!viewModel.data.value.data.isNullOrEmpty()) {
                                viewModel.data.value.data!!.filter { mBook: MBook ->
                                    (mBook.userId == currentUser?.uid) && mBook.finishedReading != null
                                }
                            } else {
                                emptyList()
                            }

                        items(items = readBooks) { book ->
                            BookRowStats(book = book)
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun BookRowStats(book: MBook) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(3.dp),
        shape = RectangleShape,
        elevation = 7.dp
    ) {
        Row(
            modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.Top
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = book.photoUrl,
                    placeholder = painterResource(id = R.drawable.book_black),
                    error = painterResource(id = R.drawable.book_black),
                ),
                contentDescription = stringResource(id = R.string.book_cover_image_description),
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight()
                    .padding(end = 4.dp)
            )

            Column {
                val authorName =
                    if (book.authors.isNullOrEmpty()) "Unknown Author" else book.authors.toString()


                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = book.title.toString(), overflow = TextOverflow.Ellipsis)
                    if (book.rating != null) {
                        if (book.rating!! >= 4) {
                            Spacer(modifier = Modifier.fillMaxWidth(0.8f))
                            Icon(
                                imageVector = Icons.Filled.ThumbUp,
                                contentDescription = "Thumb Up icon",
                                tint = Color.Blue.copy(.45f)
                            )
                        }
                    }
                }

                Text(
                    text = "Author: $authorName",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption
                )

                Text(
                    text = "Started: ${formatDate(book.startedReading!!)}",
                    softWrap = true,
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption
                )

                Text(
                    text = "Finished: ${formatDate(book.finishedReading!!)}",
                    softWrap = true,
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption
                )
            }
        }
    }
}
