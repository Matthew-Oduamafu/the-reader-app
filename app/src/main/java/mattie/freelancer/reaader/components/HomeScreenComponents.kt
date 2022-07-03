package mattie.freelancer.reaader.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import mattie.freelancer.reaader.R
import mattie.freelancer.reaader.model.MBook
import mattie.freelancer.reaader.navigation.ReaderScreens

private const val TAG = "HomeScreenComponents"

@Composable
fun TitleSection(
    modifier: Modifier = Modifier,
    label: String
) {
    Surface(modifier = modifier.padding(start = 5.dp, top = 1.dp)) {
        Column {
            Text(
                text = label,
                fontSize = 19.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Left
            )
        }
    }
}


@Composable
fun ReaderAppBar(
    title: String,
    icon: ImageVector? = null,
    showProfile: Boolean = true,
    navController: NavController,
    onBackArrowClicked: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (showProfile) {
                    Icon(
                        imageVector = Icons.Filled.Book,
                        contentDescription = "Logo Icon",
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .scale(.9f)
                    )
                } else Box {}

                if (icon != null) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "back arrow",
                        tint = Color.Red.copy(alpha = .7f),
                        modifier = Modifier.clickable { onBackArrowClicked.invoke() })
                }

                Spacer(modifier = Modifier.width(40.dp))


                Text(
                    text = title,
                    color = Color.Red.copy(.6f),
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 28.sp)
                )
                Spacer(modifier = Modifier.width(150.dp))
            }
        },
        actions = {
            if (showProfile) {
                IconButton(onClick = {
                    FirebaseAuth.getInstance().signOut().run {
                        navController.also {
                            it.popBackStack()
                            it.navigate(ReaderScreens.LOGIN_SCREEN.name)
                        }
                    }
                }) {
                    Icon(
                        imageVector = Icons.Filled.Logout,
                        contentDescription = "Logout icon",
                        tint = Color.Green.copy(0.4f)
                    )
                }
            } else Box {}
        },
        backgroundColor = Color.Transparent,
        elevation = 0.dp
    )
}


@Composable
fun FABContent(onTap: () -> Unit) {
    FloatingActionButton(
        onClick = { onTap() },
        shape = RoundedCornerShape(50.dp),
        backgroundColor = Color(0xFF92CBDF)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add a book",
            tint = Color.White
        )
    }
}


@Composable
fun BookRating(score: Double) {
    Surface(
        modifier = Modifier
            .height(78.dp)
            .padding(4.dp),
        shape = RoundedCornerShape(56.dp),
        elevation = 6.dp,
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(4.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.StarBorder,
                contentDescription = "Rating start",
                modifier = Modifier.padding(3.dp)
            )
            Text(text = score.toString(), style = MaterialTheme.typography.subtitle1)
        }
    }
}


//@Preview
@Composable
fun RoundedButton(
    label: String = "Reading",
    radius: Int = 29,
    onPress: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.clip(
            RoundedCornerShape(
                topStartPercent = radius,
                bottomEndPercent = radius
            )
        ),
        color = Color(0xFF92CBDE)
    ) {
        Column(
            modifier = Modifier
                .width(90.dp)
                .heightIn(40.dp)
                .clickable { onPress.invoke() },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, style = TextStyle(color = Color.White, fontSize = 15.sp))
        }
    }
}


//@Preview
@Composable
fun ListCard(
    book: MBook = MBook(),
    onPressDetails: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val resources = context.resources
    val displayMetrics = resources.displayMetrics
    // the above 3 lines can be written as
//    val displayMetrics1 = LocalContext.current.resources.displayMetrics
    val spacing = 10.dp

    val getScreenWidth = displayMetrics.widthPixels / displayMetrics.density
    Log.d(
        TAG, """
        ListCard:
        screen width ${displayMetrics.widthPixels / displayMetrics.density}
        screen height ${displayMetrics.heightPixels / displayMetrics.density}
        screen density ${displayMetrics.density}
        screen xdpi ${displayMetrics.xdpi}
        screen scaledDensity ${displayMetrics.scaledDensity}
        screen ydpi ${displayMetrics.ydpi}
    """.trimIndent()
    )

    Card(
        shape = RoundedCornerShape(29.dp),
        backgroundColor = Color.White,
        elevation = 6.dp,
        modifier = Modifier
            .padding(16.dp)
            .height(242.dp)
            .width(202.dp)
            .clickable { onPressDetails.invoke(book.title.toString()) }
    ) {
        Column(
            modifier = Modifier.width(getScreenWidth.dp - (spacing * 2)),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                // cover image of the book
                Image(
                    painter = rememberAsyncImagePainter(
                        model = book.photoUrl, placeholder = painterResource(
                            id = R.drawable.book_black
                        ), error = painterResource(id = R.drawable.book_black)
                    ),
                    contentDescription = "Cover image of the book",
                    modifier = Modifier
                        .height(140.dp)
                        .width(100.dp)
                        .padding(4.dp)
                )

                Spacer(modifier = Modifier.width(50.dp))

                // rating | fav | section
                Column(
                    modifier = Modifier.padding(top = 25.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // fav icon
                    Icon(
                        imageVector = Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorite icon",
                        modifier = Modifier.padding(bottom = 1.dp)
                    )

                    BookRating(score = book.rating?.toDouble() ?: 0.0)
                }
            }

            // book title
            Text(
                text = book.title.toString(),
                modifier = Modifier.padding(4.dp),
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // author's name
            Text(
                text = book.authors.toString(),
                modifier = Modifier.padding(4.dp),
                style = MaterialTheme.typography.caption
            )
        }


        val isStartedReading = remember { mutableStateOf(false) }
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom
        ) {
            isStartedReading.value = book.startedReading != null
            RoundedButton(
                label = if (isStartedReading.value) "Reading" else "Not Yet",
                radius = 70
            )
        }
    }
}
