package mattie.freelancer.reaader.screens.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import mattie.freelancer.reaader.data.DataOrException
import mattie.freelancer.reaader.model.MBook
import mattie.freelancer.reaader.repository.FireRepository
import javax.inject.Inject

private const val TAG = "HomeScreenViewModel"

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val repository: FireRepository): ViewModel() {
    val data: MutableState<DataOrException<List<MBook>, Boolean, Exception>> = mutableStateOf(DataOrException(
        listOf(), true, Exception("")))

    init {
        Log.d(TAG, "init block: grabbing all books")
        getAllBooksFromDatabase()
    }

    private fun getAllBooksFromDatabase() {
        viewModelScope.launch {
            data.value.loading = true
            data.value = repository.getALlBooksFromDatabase()
            if(!data.value.data.isNullOrEmpty()){
                data.value.loading = false
                Log.d(TAG, "getAllBooksFromDatabase: data loaded ${data.value.data.toString()}")
            }
        }
    }
}