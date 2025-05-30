package com.example.yemeksiparisapp.data.retrofit

import com.example.yemeksiparisapp.data.model.CRUDCevap
import com.example.yemeksiparisapp.data.model.SepetYemeklerCevap
import com.example.yemeksiparisapp.data.model.YemeklerCevap
import retrofit2.http.*

interface YemekApiService {

    // 1. Tüm yemekleri getir
    @GET("tumYemekleriGetir.php")
    suspend fun tumYemekleriGetir(): YemeklerCevap

    // 2. Sepete yemek ekle
    @FormUrlEncoded
    @POST("sepeteYemekEkle.php")
    suspend fun sepeteYemekEkle(
        @Field("yemek_adi") yemek_adi: String,
        @Field("yemek_resim_adi") yemek_resim_adi: String,
        @Field("yemek_fiyat") yemek_fiyat: Int,
        @Field("yemek_siparis_adet") yemek_siparis_adet: Int,
        @Field("kullanici_adi") kullanici_adi: String
    ): CRUDCevap

    // 3. Sepetteki yemekleri getir
    @FormUrlEncoded
    @POST("sepettekiYemekleriGetir.php")
    suspend fun sepettekiYemekleriGetir(
        @Field("kullanici_adi") kullanici_adi: String
    ): SepetYemeklerCevap

    // 4. Sepetten yemek sil
    @FormUrlEncoded
    @POST("sepettenYemekSil.php")
    suspend fun sepettenYemekSil(
        @Field("sepet_yemek_id") sepet_yemek_id: Int,
        @Field("kullanici_adi") kullanici_adi: String
    ): CRUDCevap
}
