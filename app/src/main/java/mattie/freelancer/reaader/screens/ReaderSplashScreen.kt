package mattie.freelancer.reaader.screens

import android.util.Log
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import mattie.freelancer.reaader.components.ReaderLogo
import mattie.freelancer.reaader.navigation.ReaderScreens

private const val TAG = "ReaderSplashScreen"


@Composable
fun ReaderSplashScreen(navController: NavHostController) {
    Log.d(TAG, "ReaderSplashScreen: called")

    val scale = remember {
        Animatable(0f)
    }

    LaunchedEffect(key1 = true, block = {
        scale.animateTo(
            targetValue = 0.8f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = {
                    OvershootInterpolator(5f).getInterpolation(it)
                }
            )
        )
        delay(3500L)
        navController.also {
            it.popBackStack()
            Log.d(TAG, "ReaderSplashScreen: splash screen pop out of stack")
            it.navigate(ReaderScreens.LOGIN_SCREEN.name)
        }
        /*
        if (FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) {
            Log.d(TAG, "ReaderSplashScreen: send user to login screen")
            navController.also {
                it.popBackStack()
                Log.d(TAG, "ReaderSplashScreen: splash screen pop out of stack")
                it.navigate(ReaderScreens.LOGIN_SCREEN.name)
            }
        } else {
            Log.d(TAG, "ReaderSplashScreen: send user straight to home page since user already in")
            navController.also {
                it.popBackStack()
                Log.d(TAG, "ReaderSplashScreen: splash screen pop out of stack")
                it.navigate(ReaderScreens.READER_HOME_SCREEN.name)
            }
        }
        */
    }
    )

    Surface(
        modifier = Modifier
            .padding(15.dp)
            .size(330.dp)
            .scale(scale.value),
        shape = CircleShape,
        color = Color.White,
        border = BorderStroke(width = 2.dp, color = Color.LightGray)
    ) {
        Column(
            modifier = Modifier.padding(1.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ReaderLogo()  // this comes from components package

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "\"Read.. Change.. Yourself..\"",
                style = MaterialTheme.typography.h5,
                color = Color.LightGray
            )
        }
    }
}