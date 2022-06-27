package mattie.freelancer.reaader.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mattie.freelancer.reaader.network.BooksApi
import mattie.freelancer.reaader.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

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