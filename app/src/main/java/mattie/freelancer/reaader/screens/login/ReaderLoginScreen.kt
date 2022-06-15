package mattie.freelancer.reaader.screens.login

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import mattie.freelancer.reaader.R
import mattie.freelancer.reaader.components.EmailInput
import mattie.freelancer.reaader.components.PasswordInputField
import mattie.freelancer.reaader.components.ReaderLogo
import mattie.freelancer.reaader.navigation.ReaderScreens

private const val TAG = "ReaderLoginScreen"

@Composable
fun ReaderLoginScreen(
    navController: NavHostController,
    loginScreenViewModel: LoginScreenViewModel = viewModel()
) {
    Log.d(TAG, "ReaderLoginScreen: called")

    val showLonginForm = rememberSaveable { mutableStateOf(true) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            ReaderLogo()  // this comes from the components package

            // check whether to show login form or signUp form
            if (showLonginForm.value) {
                UserForm(loading = false, isCreateAccount = false, onDone = { email, password ->
                    Log.d(
                        TAG,
                        "ReaderLoginScreen: email is $email, and the password is $password"
                    )
                    loginScreenViewModel.signInWithEmailAndPassword(
                        email,
                        password,
                        home = {
                            navController.also {
                                it.popBackStack()
                                it.navigate(ReaderScreens.READER_HOME_SCREEN.name)
                            }
                        })
                })
            } else {
                Log.d(TAG, "ReaderLoginScreen: showing the sign up form")
                UserForm(loading = false, isCreateAccount = true, onDone = { email, password ->
                    Log.d(
                        TAG,
                        "ReaderLoginScreen: email $email and password $password for firebase account"
                    )
                    // creating an account
                    loginScreenViewModel.createUserWithEmailAndPassword(
                        email,
                        password,
                        home = {
                            navController.also {
                                it.popBackStack()
                                it.navigate(ReaderScreens.READER_HOME_SCREEN.name)
                            }
                        })
                })
            }

            // ask if new user or already user
            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.padding(15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // text to display whether "Login" or "SignUp"
                val text =
                    if (showLonginForm.value) stringResource(id = R.string.signup_label) else stringResource(
                        id = R.string.signin_label
                    )
                // text to display whether "New User?" or "Have Account Already?"
                val textMsg =
                    if (showLonginForm.value) stringResource(id = R.string.new_user_label) else stringResource(
                        id = R.string.have_account_label
                    )

                Text(text = textMsg) // "New User?" or "Have Account Already?"
                Text( // "Login" or "SignUp"
                    text = text,
                    modifier = Modifier
                        .clickable { showLonginForm.value = !showLonginForm.value }
                        .padding(5.dp),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.secondaryVariant
                )
            }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserForm(
    loading: Boolean = false,
    isCreateAccount: Boolean = false,
    onDone: (String, String) -> Unit
) {
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordVisibility = rememberSaveable { mutableStateOf(false) }
    val passwordFocusRequest = FocusRequester.Default
    val keyboardController = LocalSoftwareKeyboardController.current

    val valid = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }

    val modifier = Modifier
        .height(250.dp)
        .background(MaterialTheme.colors.background)
        .verticalScroll(rememberScrollState())
    // creating a column
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // create account screen then show some text at the top
        if (isCreateAccount) {
            Text(
                text = stringResource(id = R.string.create_account_msg),
                modifier = Modifier.padding(4.dp)
            )
        } else {
            Text("")
        }

        // this comes from the components package as well
        EmailInput(
            emailState = email,
            enabled = !loading,
            onAction = KeyboardActions { passwordFocusRequest.requestFocus() })

        // this comes from the components package as well
        PasswordInputField(
            modifier = Modifier.focusRequester(passwordFocusRequest),
            passwordState = password,
            labelId = stringResource(id = R.string.passwordLabelId),
            enabled = !loading,
            passwordVisibility = passwordVisibility,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                keyboardController?.hide()
                onDone(email.value.trim(), password.value.trim())  // comes from component package
            }
        )

        SubmitButton(
            textId = if (isCreateAccount) "Create Account" else "Login",
            loading = loading,
            validInput = valid,
            onClick = {
                onDone(email.value.trim(), password.value.trim()) // comes from component package
                keyboardController?.hide()
            }
        )
    }
}

@Composable
fun SubmitButton(textId: String, loading: Boolean, validInput: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick, modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(.97f),
        enabled = !loading && validInput,
        shape = CircleShape
    ) {
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.size(25.dp))
        } else {
            Text(text = textId, modifier = Modifier.padding(5.dp))
        }
    }
}


