package mattie.freelancer.reaader.repository

import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import mattie.freelancer.reaader.data.DataOrException
import mattie.freelancer.reaader.model.MBook
import javax.inject.Inject

class FireRepository @Inject constructor(private val queryBook: Query) {
    suspend fun getALlBooksFromDatabase(): DataOrException<List<MBook>, Boolean, Exception> {
        val dataOrException = DataOrException<List<MBook>, Boolean, Exception>()

        try {
            dataOrException.loading = true
            dataOrException.data = queryBook.get().await().documents.map { documentSnapshot ->
                documentSnapshot.toObject(MBook::class.java)!!
            }

            if(dataOrException.data.toString().isNotEmpty()){
                dataOrException.loading = false
            }
        } catch (e: FirebaseFirestoreException) {
            dataOrException.e = e
        }

        return dataOrException
    }
}