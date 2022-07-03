package mattie.freelancer.reaader

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import mattie.freelancer.reaader.navigation.ReaderNavigation
import mattie.freelancer.reaader.ui.theme.ReaaderTheme

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: called")
        super.onCreate(savedInstanceState)
        setContent {
            ReaaderTheme {
                /*
                val db = FirebaseFirestore.getInstance()
                val user: MutableMap<String, Any> = HashMap()
                user["fName"] = "Mattie"
                user["lName"] = "Oduamafu"

                with(db.collection("users")) {
                    add(user).addOnSuccessListener {
                        Log.d(TAG, "onCreate: users:: ${it.id} saved")
                    }.addOnFailureListener {
                        Log.d(TAG, "onCreate: users:: $it failed to save")
                    }
                }
                */

                // calling the reader app
                ReaderApp()  // this serves as the parent to all the composable
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun ReaderApp() {
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ReaderNavigation()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ReaaderTheme {
    }
}