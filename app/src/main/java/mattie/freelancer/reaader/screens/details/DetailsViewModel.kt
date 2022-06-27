package mattie.freelancer.reaader.screens.details

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import mattie.freelancer.reaader.data.Resource
import mattie.freelancer.reaader.model.Item
import mattie.freelancer.reaader.repository.BookRepository
import javax.inject.Inject

private const val TAG = "DetailsViewModel"

@HiltViewModel
class DetailsViewModel @Inject constructor(private val bookRepository: BookRepository) :
    ViewModel() {
    suspend fun getBookInfo(bookId: String): Resource<Item> {
        Log.d(TAG, "getBookInfo: called")
        Log.d(TAG, "getBookInfo: data is ${bookRepository.getBookInfo(bookId)}")
        return bookRepository.getBookInfo(bookId)
    }

}