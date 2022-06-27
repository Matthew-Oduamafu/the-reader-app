package mattie.freelancer.reaader.screens.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mattie.freelancer.reaader.data.Resource
import mattie.freelancer.reaader.model.Item
import mattie.freelancer.reaader.repository.BookRepository
import javax.inject.Inject

private const val TAG = "SearchScreenViewModel"


@HiltViewModel
class SearchScreenViewModel @Inject constructor(private val bookRepository: BookRepository) :
    ViewModel() {
    var list: List<Item> by mutableStateOf(listOf())
    var isLoading: Boolean by mutableStateOf(true)

    init {
        loadBooks()
    }

    private fun loadBooks() {
        searchBooks("Android")
    }

    fun searchBooks(searchQuery: String) {
        viewModelScope.launch(Dispatchers.Default) {
            if (searchQuery.isEmpty())
                return@launch

            try {
                when (val response = bookRepository.getBooks(searchQuery)) {
                    is Resource.Error -> {
                        isLoading = false
                        Log.e(TAG, "searchBooks: Failed getting books")
                    }
                    is Resource.Loading -> {
                        isLoading = true
                        Log.d(TAG, "searchBooks: Data loading or loading done")
                    }
                    is Resource.Success -> {
                        list = response.data!!

                        if(list.toString().isNotEmpty())
                            isLoading = false

                        Log.d(TAG, "searchBooks: ${response.data}")
                    }
                }
            } catch (e: Exception) {
                isLoading = false
            }
        }
    }

    fun getBooks(searchQuery: String) = viewModelScope.launch {
        Log.d(TAG, "getBooks: ${bookRepository.getBooks(searchQuery)}")
    }

    fun getBookInfo(bookId: String) = viewModelScope.launch {
        Log.d(TAG, "getBooks: ${bookRepository.getBookInfo(bookId)}")
    }
}