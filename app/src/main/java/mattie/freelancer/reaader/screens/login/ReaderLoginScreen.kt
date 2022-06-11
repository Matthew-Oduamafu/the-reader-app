package mattie.freelancer.reaader.screens.login

import android.util.Log
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

private const val TAG = "ReaderLoginScreen"
@Composable
fun ReaderLoginScreen(navController: NavHostController) {
    Log.d(TAG, "ReaderLoginScreen: called")
    Text(text = "Login screen")
}