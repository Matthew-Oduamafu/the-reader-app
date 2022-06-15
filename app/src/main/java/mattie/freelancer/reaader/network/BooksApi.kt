package mattie.freelancer.reaader.network

import mattie.freelancer.reaader.model.Book
import mattie.freelancer.reaader.model.Item
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton


@Singleton
interface BooksApi {
    @GET("volumes")
    suspend fun getAllBooks(@Query("q") query: String): Book

    @GET("volumes/{bookOd}")
    suspend fun bookInfo(@Path("bookId") bookId: String): Item
}