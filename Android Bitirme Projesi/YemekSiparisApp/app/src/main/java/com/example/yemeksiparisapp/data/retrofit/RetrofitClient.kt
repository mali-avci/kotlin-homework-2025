package com.example.yemeksiparisapp.data.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    companion object {
        private const val BASE_URL = "http://kasimadalan.pe.hu/yemekler/"

        fun getClient(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun getYemekApiService(): YemekApiService {
            return getClient().create(YemekApiService::class.java)
        }
    }
}
