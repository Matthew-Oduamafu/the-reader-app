package mattie.freelancer.reaader.screens.details

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import mattie.freelancer.reaader.components.ReaderAppBar
import mattie.freelancer.reaader.components.RoundedButton
import mattie.freelancer.reaader.data.Resource
import mattie.freelancer.reaader.model.Item
import mattie.freelancer.reaader.model.MBook
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
                modifier = Modifier.padding(8.dp),
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
                        Text(
                            text = "Loading",
                            fontSize = 24.sp,
                            style = MaterialTheme.typography.caption,
                            color = Color.LightGray.copy(.35f)
                        )
                        CircularProgressIndicator(
                            strokeWidth = 4.dp,
                            modifier = Modifier.fillMaxSize(.35f)
                        )
                    }
                } else {
                    ShowBookInfo(bookInfo, navController)
                }
            }
        }
    }
}

@Composable
fun ShowBookInfo(bookInfo: Resource<Item>, navController: NavHostController) {
    val bookData = bookInfo.data?.volumeInfo
    val googleBookId = bookInfo.data?.id

    val context = LocalContext.current

    if (bookData != null) {  // bypassing safe-call on null  // thus, casting bookData to non-nullable
        Card(
            modifier = Modifier.padding(34.dp),
            shape = CircleShape,
            elevation = 4.dp
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = bookData.imageLinks.thumbnail),
                contentDescription = stringResource(id = mattie.freelancer.reaader.R.string.book_cover_image_description),
                modifier = Modifier
                    .width(120.dp)
                    .height(120.dp)
                    .padding(1.dp)
            )

        }

        Text(
            text = bookData.title,
            style = MaterialTheme.typography.h6, overflow = TextOverflow.Ellipsis, maxLines = 5
        )
        Text(text = "Authors: ${bookData.authors}")
        Text(text = "Page count: ${bookData.pageCount}")
        Text(
            text = "Categories: ${bookData.categories}",
            style = MaterialTheme.typography.subtitle1,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = "Published: ${bookData.publishedDate}",
            style = MaterialTheme.typography.subtitle1
        )

        Spacer(modifier = Modifier.height(4.dp))

        // we parse the description since it may contain html tags
        val cleanDescription =
            HtmlCompat.fromHtml(bookData.description, FROM_HTML_MODE_LEGACY).toString()

        val localDims = LocalContext.current.resources.displayMetrics
        Surface(
            modifier = Modifier
                .height(localDims.heightPixels.dp.times(0.09f))
                .padding(4.dp),
            shape = RectangleShape,
            border = BorderStroke(1.dp, color = Color.DarkGray)
        ) {
            LazyColumn(modifier = Modifier.padding(3.dp)) {
                item {
                    Text(text = cleanDescription)
                }
            }
        }

        // adding save and cancel button
        Row(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // first save button
            RoundedButton("Save") {
                Log.d(TAG, "ShowBookInfo: book saved")
                // saving to fireBase

                val book = MBook(
                    title = bookData.title,
                    authors = bookData.authors.toString(),
                    description = HtmlCompat.fromHtml(bookData.description, FROM_HTML_MODE_LEGACY)
                        .toString(),
                    categories = bookData.categories.toString(),
                    notes = "",
                    photoUrl = bookData.imageLinks.thumbnail,
                    publishedDate = bookData.publishedDate,
                    pageCount = bookData.pageCount.toString(),
                    rating = 0,
                    googleBookId = googleBookId,
                    userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
                )
                saveToFireBase(book, navController, context)
            }

            // cancel book
            RoundedButton("Cancel") {
                Log.d(TAG, "ShowBookInfo: book cancelled... going to search screen")
                navController.popBackStack()
            }
        }
    }
}


fun saveToFireBase(book: MBook, navController: NavHostController, context: Context) {
    val db = FirebaseFirestore.getInstance()
    val dbCollections = db.collection("books")

    if (book.toString().isNotEmpty()) {
        Log.d(TAG, "saveToFireBase: saving book:: $book")
        dbCollections.add(book).addOnSuccessListener { documentRef ->
            val docId = documentRef.id
            dbCollections.document(docId).update(hashMapOf<String, Any>("id" to docId))
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Book Saved!", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    } else {
                        Toast.makeText(context, "Saving Failed!", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                Toast.makeText(context, "Saving Failed!", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "SaveToFireBase: failed to save ${it.printStackTrace()}")
            }
        }
    } else {
        Log.d(TAG, "saveToFireBase: can't save an empty book")
    }
}
