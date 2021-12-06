package com.endava.budgetplanner.di.module

import com.endava.budgetplanner.BuildConfig
import com.endava.budgetplanner.common.network.calladapter.NetworkResponseAdapterFactory
import com.endava.budgetplanner.common.network.deserializer.CategoryPropertiesDeserializer
import com.endava.budgetplanner.common.network.deserializer.TransactionTypeDeserializer
import com.endava.budgetplanner.common.utils.CategoryProperties
import com.endava.budgetplanner.common.utils.TransactionType
import com.endava.budgetplanner.data.api.ApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
object NetworkModule {

    @Provides
    @Singleton
    fun provideLogger() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun provideClient(logger: HttpLoggingInterceptor) = OkHttpClient.Builder()
        .addInterceptor(logger)
        .build()

    @Provides
    @Singleton
    fun provideGsonBuilder(): Gson = GsonBuilder()
        .registerTypeAdapter(CategoryProperties::class.java, CategoryPropertiesDeserializer)
        .registerTypeAdapter(TransactionType::class.java, TransactionTypeDeserializer)
        .setLenient()
        .create()

    @Provides
    @Singleton
    fun provideRetrofitInstance(client: OkHttpClient, gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addCallAdapterFactory(NetworkResponseAdapterFactory())
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
}