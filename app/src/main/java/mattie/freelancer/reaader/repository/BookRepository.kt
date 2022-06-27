package mattie.freelancer.reaader.repository

import mattie.freelancer.reaader.data.Resource
import mattie.freelancer.reaader.model.Item
import mattie.freelancer.reaader.network.BooksApi
import javax.inject.Inject

private const val TAG = "BookRepository"

class BookRepository @Inject constructor(private val booksApi: BooksApi) {
    suspend fun getBooks(searchQuery: String): Resource<List<Item>>{
        return try {
            Resource.Loading(data = true)  // loading data from api

            val itemList = booksApi.getAllBooks(searchQuery).items  // data loaded
            // if data loaded successfully then we set loading to false
            if(itemList.toString().isNotEmpty())
                Resource.Loading(data =  false)
            Resource.Success(data = itemList)
        }catch (e:Exception){
            Resource.Error(message = e.message.toString())
        }
    }

    suspend fun getBookInfo(bookId: String): Resource<Item>{
        return try {
            Resource.Loading(data = true)  // loading data from api

            val item = booksApi.bookInfo(bookId)  // data loaded
            // if data loaded successfully then we set loading to false
            if(item.toString().isNotEmpty())
                Resource.Loading(data = false)
            Resource.Success(data = item)
        }catch (e:Exception){
            Resource.Error(message = e.message.toString())
        }
    }
}