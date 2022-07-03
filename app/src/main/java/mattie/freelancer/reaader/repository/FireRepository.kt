package mattie.freelancer.reaader.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import mattie.freelancer.reaader.data.DataOrException
import mattie.freelancer.reaader.model.MBook
import javax.inject.Inject

private const val TAG = "FireRepository"

class FireRepository @Inject constructor(private val queryBook: Query) {
    suspend fun getALlBooksFromDatabase(): DataOrException<List<MBook>, Boolean, Exception> {
        Log.d(TAG, "getALlBooksFromDatabase: called to load data from firebase")
        val dataOrException = DataOrException<List<MBook>, Boolean, Exception>()

        try {
            dataOrException.loading = true
            dataOrException.data = queryBook.get().await().documents.map { documentSnapshot ->
                documentSnapshot.toObject(MBook::class.java)!!
            }

            if (!dataOrException.data.isNullOrEmpty()) {
                Log.d(TAG, "getALlBooksFromDatabase: data loaded")
                dataOrException.loading = false
            }
        } catch (e: FirebaseFirestoreException) {
            dataOrException.e = e
        }

        return dataOrException
    }
}