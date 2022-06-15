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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import mattie.freelancer.reaader.R
import mattie.freelancer.reaader.components.InputField
import mattie.freelancer.reaader.components.ReaderAppBar
import mattie.freelancer.reaader.model.MBook
import mattie.freelancer.reaader.utils.Constants

private const val TAG = "ReaderBookSearchScreen"


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ReaderBookSearchScreen(
    navController: NavHostController,
    searchScreenViewModel: SearchScreenViewModel = hiltViewModel()
) {
    Log.d(TAG, "ReaderBookSearchScreen: called")
    Log.d(TAG, "ReaderBookSearchScreen: testing the viewModel")
    searchScreenViewModel.getBooks("Android")

    Scaffold(
        topBar = {
            ReaderAppBar(
                title = stringResource(id = R.string.search_screen_title),
                icon = Icons.Filled.ArrowBack,
                navController = navController,
                showProfile = false
            ) {
                navController.popBackStack()
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
                ) {
                    Log.d(TAG, "ReaderBookSearchScreen: search query is $it")
                }

                Spacer(modifier = Modifier.height(13.dp))

                BookList(navController = navController)
            }
        }
    }
}

@Composable
fun BookList(navController: NavHostController) {

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

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(items = listOfBooks) { book ->
            BookRow(book, navController = navController)
        }
    }
}

@Composable
fun BookRow(book: MBook, navController: NavHostController) {
    Card(
        modifier = Modifier
            .clickable { }
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
            val imageUrl = Constants.TEST_URL
            Image(
                painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = stringResource(id = R.string.book_cover_image_description),
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight()
                    .padding(end = 4.dp)
            )
            Column {
                Text(text = book.title.toString(), overflow = TextOverflow.Ellipsis)
                Text(
                    text = "Author: ${book.authors.toString()}",
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.caption
                )
                // TODO(Add more fields later)
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