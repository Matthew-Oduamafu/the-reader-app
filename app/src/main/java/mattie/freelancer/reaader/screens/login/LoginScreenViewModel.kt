package mattie.freelancer.reaader.screens.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import mattie.freelancer.reaader.model.MUser


private const val TAG = "LoginScreenViewModel"


//@HiltViewModel
class LoginScreenViewModel : ViewModel() {
//    val loadingState = MutableStateFlow(LoadingState.IDLE)

    private val auth: FirebaseAuth = Firebase.auth

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    fun createUserWithEmailAndPassword(email: String, password: String, home: () -> Unit) =
        viewModelScope.launch {
            run {
                if (_loading.value == false) {
                    _loading.value == true
                    try {
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d(
                                        TAG,
                                        "createUserWithEmailAndPassword: account created successfully"
                                    )
                                    val displayName = task.result.user?.email?.split("@")?.get(0)

                                    createUser(displayName = displayName)
                                    home()
                                } else {
                                    Log.d(
                                        TAG,
                                        "createUserWithEmailAndPassword: account creation failed"
                                    )
                                    Log.d(
                                        TAG,
                                        "createUserWithEmailAndPassword: task returned ${task.result}"
                                    )
                                }
                                _loading.value = false
                            }
                    } catch (e: Exception) {
                        Log.d(TAG, "createUserWithEmailAndPassword: this try run")
                        when (e) {
                            is FirebaseAuthUserCollisionException -> Log.d(
                                TAG,
                                "createUserWithEmailAndPassword: user already exist"
                            )
                            is FirebaseAuthWeakPasswordException -> Log.d(
                                TAG,
                                "createUserWithEmailAndPassword:Invalid password (password must be 6+ characters)"
                            )
                            is FirebaseNetworkException -> {
                                // TODO()
                            }
                            else -> Log.d(
                                TAG,
                                "createUserWithEmailAndPassword: ${e.message}"
                            )
                        }
                    }
                }
            }

        }

    private fun createUser(displayName: String?) {
        val userId = auth.currentUser?.uid
        val user = MUser.toMap(
            id = null,
            userId = userId.toString(),
            displayName = displayName.toString(),
            avatarUrl = "",
            quote = "Life is Great",
            profession = "Android Developer"
        ).toMap()

        // adding new user to the firebase id
        FirebaseFirestore.getInstance().collection("users").add(user)
    }

    fun signInWithEmailAndPassword(email: String, password: String, home: () -> Unit) =
        viewModelScope.launch {
            run {
                try {
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "signInWithEmailAndPassword: user login successful")
                            Log.d(TAG, "signInWithEmailAndPassword: task returned ${task.result}")

                            //TODO("Take user to home screen")
                            home() // this lambda function implements the navigation to home screen
                        } else {
                            Log.d(TAG, "signInWithEmailAndPassword: user login not successful")
                            Log.d(TAG, "signInWithEmailAndPassword: task returned ${task.result}")
                        }
                    }
                } catch (e: Exception) {
                    when (e) {
                        is FirebaseAuthInvalidUserException -> Log.d(
                            TAG,
                            "signInWithEmailAndPassword: unrecognized user"
                        )

                        is FirebaseException -> {}
                    }
                    Log.d(TAG, "SingInWithEmailAndPassword: exception occurred")
                    Log.d(TAG, "SingInWithEmailAndPassword: ${e.printStackTrace()}")
                }
            }
        }
}