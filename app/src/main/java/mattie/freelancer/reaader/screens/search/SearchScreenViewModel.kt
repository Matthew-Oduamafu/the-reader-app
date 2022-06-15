package mattie.freelancer.reaader.screens.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import mattie.freelancer.reaader.repository.BookRepository
import javax.inject.Inject

private const val TAG = "SearchScreenViewModel"


@HiltViewModel
class SearchScreenViewModel @Inject constructor(private val bookRepository: BookRepository): ViewModel() {
    fun getBooks(searchQuery: String)  = viewModelScope.launch {
        Log.d(TAG, "getBooks: ${bookRepository.getBooks(searchQuery)}")
    }

    fun getBookInfo(bookId: String)  = viewModelScope.launch {
        Log.d(TAG, "getBooks: ${bookRepository.getBookInfo(bookId)}")
    }
}