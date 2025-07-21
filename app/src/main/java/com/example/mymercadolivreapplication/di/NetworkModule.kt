package com.example.mymercadolivreapplication.di

import com.example.mymercadolivreapplication.data.remote.MercadoLivreApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Hilt module responsible for providing network-related dependencies.
 *
 * Why use Hilt?
 * - Official dependency injection framework for Android
 * - Reduces boilerplate code
 * - Simplifies unit testing
 * - Automatically manages the lifecycle of dependencies
 * - Native integration with Android components (Activity, Fragment, ViewModel)
 *
 * Why @InstallIn(SingletonComponent::class)?
 * - Ensures that dependencies have application-wide scope (singleton)
 * - Suitable for dependencies that need to be shared globally
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Provides a configured instance of OkHttpClient.
     *
     * Why configure OkHttpClient?
     * - Allows adding interceptors for logging and error handling
     * - Configures timeouts for better user experience
     * - Enables adding global headers if necessary
     * - Fine control over HTTP request behavior
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    // Log only in debug builds to avoid exposing data in production
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .connectTimeout(30, TimeUnit.SECONDS) // Connection timeout
            .readTimeout(30, TimeUnit.SECONDS)    // Read timeout
            .writeTimeout(30, TimeUnit.SECONDS)   // Write timeout
            .build()
    }

    /**
     * Provides a configured instance of Retrofit.
     *
     * Why these configurations?
     * - GsonConverterFactory: Automatically converts JSON to Kotlin objects
     * - Base URL: Base URL for the Mercado Libre API
     * - Custom OkHttpClient: For logging and timeout configurations
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(MercadoLivreApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Provides an instance of the MercadoLibreApi interface.
     *
     * Why create the API this way?
     * - Retrofit automatically creates the implementation of the interface
     * - Type-safe: compilation errors instead of runtime errors
     * - Easier mocking in unit tests
     */
    @Provides
    @Singleton
    fun provideMercadoLibreApi(retrofit: Retrofit): MercadoLivreApi {
        return retrofit.create(MercadoLivreApi::class.java)
    }
}