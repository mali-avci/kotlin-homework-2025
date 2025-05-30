package com.example.yemeksiparisapp.di

import com.example.yemeksiparisapp.data.repo.YemekRepository
import com.example.yemeksiparisapp.data.retrofit.YemekApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideYemekRepository(apiService: YemekApiService): YemekRepository {
        return YemekRepository(apiService)
    }
}
