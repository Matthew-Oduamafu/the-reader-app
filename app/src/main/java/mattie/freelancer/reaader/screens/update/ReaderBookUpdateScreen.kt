package mattie.freelancer.reaader.screens.update

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import mattie.freelancer.reaader.R
import mattie.freelancer.reaader.components.InputField
import mattie.freelancer.reaader.components.RatingBar
import mattie.freelancer.reaader.components.ReaderAppBar
import mattie.freelancer.reaader.components.RoundedButton
import mattie.freelancer.reaader.data.DataOrException
import mattie.freelancer.reaader.model.MBook
import mattie.freelancer.reaader.navigation.ReaderScreens
import mattie.freelancer.reaader.screens.home.HomeScreenViewModel
import mattie.freelancer.reaader.utils.formatDate

private const val TAG = "ReaderBookUpdateScreen"

@RequiresApi(Build.VERSION_CODES.N)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BookUpdateScreen(
    navController: NavHostController,
    bookItemId: String?,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "Update Book",
                icon = Icons.Filled.ArrowBack,
                showProfile = false,
                navController = navController,
                onBackArrowClicked = { navController.popBackStack() }
            )
        }
    ) {
//        val bookInfo =
//            produceState<DataOrException<List<MBook>, Boolean, Exception>>(
//                initialValue = DataOrException(
//                    emptyList(), true, Exception("")
//                )
//            ) {
//                value = viewModel.data.value
//            }.value
        val bookInfo = viewModel.data.value

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(3.dp)
        ) {
            Column(
                modifier = Modifier.padding(top = 3.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (bookInfo.loading == true) {
                    Text(
                        text = "Loading",
                        fontSize = 24.sp,
                        style = MaterialTheme.typography.caption,
                        color = Color.LightGray.copy(.35f)
                    )
                    CircularProgressIndicator()
                } else {
                    Surface(
                        modifier = Modifier
                            .padding(2.dp)
                            .fillMaxWidth(),
                        shape = CircleShape,
                        elevation = 4.dp
                    ) {
                        ShowBookUpdate(bookInfo = bookInfo, bookItemId = bookItemId)
                    }
                    ShowSimpleForm(bookInfo.data?.first { mBook: MBook ->
                        mBook.googleBookId == bookItemId
                    }, navController)
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun ShowSimpleForm(book: MBook?, navController: NavHostController) {
    val notesText = remember(book) { mutableStateOf(book?.notes) }
    val isStartedReading = remember { mutableStateOf(false) }
    val isFinishedReading = remember { mutableStateOf(false) }
    val ratingVal = remember(book) { mutableStateOf(book?.rating) }


    if (book != null) {
        SimpleForm(
            defaultValue = book.notes.toString().ifEmpty { "No thoughts available" },
            onSearch = { note -> notesText.value = note })
    }
    Row(
        modifier = Modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        if (book != null) {
            TextButton(
                onClick = { isStartedReading.value = true },
                enabled = book.startedReading == null
            ) {
                if (book.startedReading == null) {
                    if (!isStartedReading.value) {
                        Text(text = "Start Reading")
                    } else {
                        Text(
                            text = "Started Reading!",
                            modifier = Modifier.alpha(0.6f),
                            color = Color.Red.copy(0.45f)
                        )
                    }
                } else {
                    Text(text = "Started on: ${formatDate(book.startedReading!!)}")  //TODO(format date)
                }
            }

            Spacer(modifier = Modifier.width(4.dp))

            TextButton(
                onClick = { isFinishedReading.value = true },
                enabled = book.finishedReading == null
            ) {
                if (book.finishedReading == null) {
                    if (!isFinishedReading.value) {
                        Text(text = "Mark as Read")
                    } else {
                        Text(text = "Finished Reading!")
                    }
                } else {
                    Text(text = "Finished on: ${formatDate(book.finishedReading!!)}")  //TODO(format date later)
                }
            }
        }
    }
    Text(text = "Rating", modifier = Modifier.padding(3.dp))
    if (book != null) {
        book.rating?.toInt().let { rating ->
            RatingBar(rating = rating!!, onPressRating = { ratingV -> ratingVal.value = ratingV })
        }
    }

    Spacer(
        modifier = Modifier
            .height(16.dp)
//        .padding(bottom = 16.dp)
    )

    // updating the book
    Row(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val changedNotes = book?.notes != notesText.value
        val changedRating = book?.rating != ratingVal.value
        val isFinishedTimestamp =
            if (isFinishedReading.value) Timestamp.now() else book?.finishedReading
        val isStartedTimestamp =
            if (isStartedReading.value) Timestamp.now() else book?.startedReading

        val bookUpdate =
            changedNotes || changedRating || isStartedReading.value || isFinishedReading.value

        val bookToUpdate = hashMapOf(
            "finished_reading" to isFinishedTimestamp,
            "started_reading" to isStartedTimestamp,
            "rating" to ratingVal.value,
            "notes" to notesText.value
        ).toMap()
        val context = LocalContext.current


        // first save button
        RoundedButton("Update") {
            Log.d(TAG, "ShowBookInfo: book saved")
            // updating the book to firebase

            if (bookUpdate) {
                if (book != null) {
                    FirebaseFirestore.getInstance().collection("books").document(book.id!!)
                        .update(bookToUpdate).addOnCompleteListener { task ->
                            Log.d(TAG, "ShowSimpleForm: book updated")
                            Log.d(TAG, "ShowSimpleForm: update result returned ${task.result}")
                            Toast.makeText(context, "Book updated!", Toast.LENGTH_SHORT).show()
                            navController.also {
                                it.popBackStack()
                                it.navigate(ReaderScreens.READER_HOME_SCREEN.name)
                            }
                        }.addOnFailureListener {
                            Log.d(TAG, "ShowSimpleForm: failed to update book", it)
                            Toast.makeText(context, "Failed to update book", Toast.LENGTH_SHORT)
                                .show()
                        }
                }
            }
        }

        // cancel book
        val openDialog = remember { mutableStateOf(false) }
        if (openDialog.value) {
            ShowAlertDialog(
                message = stringResource(id = R.string.delete_alert_lbl),
                openDialog,
                onYesPressed = {
                    if (book != null) {
                        FirebaseFirestore.getInstance().collection("books").document(book.id!!)
                            .delete().addOnCompleteListener {
                                if (it.isSuccessful) {
                                    Toast.makeText(
                                        context,
                                        "Book deleted Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    openDialog.value = false
                                    navController.also { navHostController ->
                                        navHostController.popBackStack()
                                        navHostController.navigate(ReaderScreens.READER_HOME_SCREEN.name)
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Failed to deleted book",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }.addOnFailureListener {

                            }
                    }
                }
            )
        }
        RoundedButton("Delete") {
            Log.d(TAG, "ShowBookInfo: deleting book")
            openDialog.value = true
        }
    }
}

@Composable
fun ShowAlertDialog(message: String, openDialog: MutableState<Boolean>, onYesPressed: () -> Unit) {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text(text = "Delete Book?") },
            text = { Text(text = message) },
            buttons = {
                Row(
                    modifier = Modifier.padding(all = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(onClick = { onYesPressed.invoke() }) {
                        Text(text = "Yes")
                    }
                    TextButton(onClick = { openDialog.value = false }) {
                        Text(text = "No")
                    }
                }
            }
        )
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SimpleForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    defaultValue: String = "Great Book",
    onSearch: (String) -> Unit
) {
    Column {
        val textFieldValue = rememberSaveable { mutableStateOf(defaultValue) }
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(textFieldValue.value) {
            textFieldValue.value.trim().isNotEmpty()
        }

        InputField(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .padding(3.dp)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            valueState = textFieldValue,
            labelId = "Enter your thoughts",
            enabled = true,
            imeAction = ImeAction.Done,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions

                onSearch(textFieldValue.value.trim())
                keyboardController?.hide()

            })
    }
}


@Composable
fun ShowBookUpdate(
    bookInfo: DataOrException<List<MBook>, Boolean, Exception>,
    bookItemId: String?
) {
    Row {
        Spacer(modifier = Modifier.width(43.dp))

        Column(
            modifier = Modifier.padding(4.dp),
            verticalArrangement = Arrangement.Center
        ) {
            CardListItem(book = bookInfo.data?.first { mBook ->
                mBook.googleBookId == bookItemId
            }, onPressDetails = {})
        }
    }
}

@Composable
fun CardListItem(book: MBook?, onPressDetails: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(start = 4.dp, end = 4.dp, top = 4.dp, bottom = 8.dp)
            .clip(
                RoundedCornerShape(20.dp)
            )
            .clickable { }, elevation = 8.dp
    ) {
        if (book != null) {
            Row(horizontalArrangement = Arrangement.Start) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = book.photoUrl, placeholder = painterResource(
                            id = R.drawable.book_black
                        ), error = painterResource(id = R.drawable.book_black)
                    ), contentDescription = stringResource(
                        id = R.string.book_cover_image_description
                    ),
                    modifier = Modifier
                        .height(100.dp)
                        .width(120.dp)
                        .padding(4.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 120.dp,
                                topEnd = 20.dp,
                                bottomStart = 0.dp,
                                bottomEnd = 0.dp
                            )
                        )
                )
                Column(

                ) {
                    Text(
                        text = book.title.toString(),
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp)
                            .width(120.dp),
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = book.authors.toString(),
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(
                            start = 8.dp,
                            end = 8.dp,
                            top = 2.dp,
                            bottom = 0.dp
                        )
                    )
                    Text(
                        text = book.publishedDate.toString(),
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(
                            start = 8.dp,
                            end = 8.dp,
                            top = 0.dp,
                            bottom = 8.dp
                        )
                    )
                }
            }
        }
    }
}
