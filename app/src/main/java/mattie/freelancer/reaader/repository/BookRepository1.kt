package mattie.freelancer.reaader.repository

import android.util.Log
import mattie.freelancer.reaader.data.DataOrException
import mattie.freelancer.reaader.model.Item
import mattie.freelancer.reaader.network.BooksApi
import javax.inject.Inject

private const val TAG = "BookRepository"

class BookRepository1 @Inject constructor(private val booksApi: BooksApi) {
    private val dataOrException = DataOrException<List<Item>, Boolean, Exception>()
    private val bookInfoDataOrException = DataOrException<Item, Boolean, Exception>()

    suspend fun getBooks(searchQuery: String): DataOrException<List<Item>, Boolean, Exception> {
        Log.d(TAG, "getBooks: called")
        try {
            dataOrException.loading = true
            dataOrException.data = booksApi.getAllBooks(query = searchQuery).items

            if (dataOrException.data!!.isNotEmpty()) dataOrException.loading = false

        } catch (e: Exception) {
            dataOrException.e = e
        }
        return dataOrException
    }

    suspend fun getBookInfo(bookId: String): DataOrException<Item, Boolean, Exception> {
        Log.d(TAG, "getBookInfo: called")
        try {
            bookInfoDataOrException.loading = true
            bookInfoDataOrException.data = booksApi.bookInfo(bookId = bookId)

            if (bookInfoDataOrException.data.toString()
                    .isNotEmpty()
            ) bookInfoDataOrException.loading = false

        } catch (e: Exception) {
            bookInfoDataOrException.e = e
        }
        return bookInfoDataOrException
    }
}