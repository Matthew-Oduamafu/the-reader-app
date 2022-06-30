package mattie.freelancer.reaader.di

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mattie.freelancer.reaader.network.BooksApi
import mattie.freelancer.reaader.repository.FireRepository
import mattie.freelancer.reaader.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideFireBookRepository() = FireRepository(queryBook = FirebaseFirestore.getInstance().collection("books"))

    @Singleton
    @Provides
    fun provideBookApi(): BooksApi {
        return with(Retrofit.Builder()) {
            baseUrl(Constants.BASE_URL)
            addConverterFactory(GsonConverterFactory.create())
            build().create(BooksApi::class.java)
        }

    }
}