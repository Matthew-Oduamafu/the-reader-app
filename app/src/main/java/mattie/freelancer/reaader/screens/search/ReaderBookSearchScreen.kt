package mattie.freelancer.reaader.screens.search

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import mattie.freelancer.reaader.R
import mattie.freelancer.reaader.components.InputField
import mattie.freelancer.reaader.components.ReaderAppBar
import mattie.freelancer.reaader.model.Item
import mattie.freelancer.reaader.navigation.ReaderScreens

private const val TAG = "ReaderBookSearchScreen"


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ReaderBookSearchScreen(
    navController: NavHostController,
    searchScreenViewModel: SearchScreenViewModel = hiltViewModel()
) {
    Log.d(TAG, "ReaderBookSearchScreen: called")
    Log.d(TAG, "ReaderBookSearchScreen: testing the viewModel")

    Scaffold(
        topBar = {
            ReaderAppBar(
                title = stringResource(id = R.string.search_screen_title),
                icon = Icons.Filled.ArrowBack,
                navController = navController,
                showProfile = false
            ) {
                navController.also {
                    it.popBackStack()
                    it.navigate(ReaderScreens.READER_HOME_SCREEN.name)
                }
            }
        }
    ) {
        Surface {
            Column {
                // the search box
                SearchForm(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) { searchQuery ->
                    Log.d(TAG, "ReaderBookSearchScreen: search query is $searchQuery")
                    searchScreenViewModel.searchBooks(searchQuery = searchQuery)
                }

                Spacer(modifier = Modifier.height(13.dp))

                BookList(
                    navController = navController,
                    searchScreenViewModel = searchScreenViewModel
                )
            }
        }
    }
}

@Composable
fun BookList(
    navController: NavHostController,
    searchScreenViewModel: SearchScreenViewModel = hiltViewModel()
) {

    val listOfBooks = searchScreenViewModel.list

    if (searchScreenViewModel.isLoading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth(.95f)
                    .padding(8.dp)
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(items = listOfBooks) { book ->
                BookRow(book, navController = navController)
            }
        }
    }
}

@Composable
fun BookRow(book: Item, navController: NavHostController) {
    Card(
        modifier = Modifier
            .clickable { navController.navigate(ReaderScreens.DETAILS_SCREEN.name + "/${book.id}") }
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
            Log.d(TAG, "BookRow: ${book.volumeInfo.imageLinks.thumbnail}")
            Image(
                painter = rememberAsyncImagePainter(
                    model = book.volumeInfo.imageLinks.thumbnail,
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
                    if (book.volumeInfo.authors.isEmpty()) "Unknown Author" else book.volumeInfo.authors.toString()
                Log.d(TAG, "BookRow: ${book.volumeInfo.authors}")

                Text(text = book.volumeInfo.title, overflow = TextOverflow.Ellipsis)
                Text(
                    text = "Author: $authorName",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption
                )

                Text(
                    text = "Date: ${book.volumeInfo.publishedDate}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption
                )

                Text(
                    text = "${book.volumeInfo.categories}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.caption
                )
            }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    hint: String = stringResource(id = R.string.search_hint),
    onSearch: (String) -> Unit = {}
) {
    Log.d(TAG, "ReaderBookSearchScreen: inside search form")
    Column {
        val searchQueryState = rememberSaveable { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(searchQueryState.value) {
            searchQueryState.value.trim().isNotEmpty()
        }
        InputField(
            valueState = searchQueryState,
            labelId = stringResource(id = R.string.search_hint),
            enabled = true,
            isSingleLine = false,
            imeAction = ImeAction.Search,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions

                onSearch(searchQueryState.value.trim())
                keyboardController?.hide()

                searchQueryState.value = ""
            }
        )
    }
}