package com.example.yemeksiparisapp.di

import com.example.yemeksiparisapp.data.retrofit.YemekApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://kasimadalan.pe.hu/yemekler/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideYemekApiService(retrofit: Retrofit): YemekApiService {
        return retrofit.create(YemekApiService::class.java)
    }
}

