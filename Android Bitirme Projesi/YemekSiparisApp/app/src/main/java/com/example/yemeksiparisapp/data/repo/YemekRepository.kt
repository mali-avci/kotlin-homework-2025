package com.example.yemeksiparisapp.data.repo

import com.example.yemeksiparisapp.data.model.CRUDCevap
import com.example.yemeksiparisapp.data.model.SepetYemeklerCevap
import com.example.yemeksiparisapp.data.model.YemeklerCevap
import com.example.yemeksiparisapp.data.retrofit.RetrofitClient
import com.example.yemeksiparisapp.data.retrofit.YemekApiService
import javax.inject.Inject

class YemekRepository @Inject constructor(
    private val yemekApi: YemekApiService
) {

    suspend fun tumYemekleriGetir(): YemeklerCevap {
        return yemekApi.tumYemekleriGetir()
    }

    suspend fun sepeteYemekEkle(
        yemek_adi: String,
        yemek_resim_adi: String,
        yemek_fiyat: Int,
        yemek_siparis_adet: Int,
        kullanici_adi: String
    ): CRUDCevap {
        return yemekApi.sepeteYemekEkle(
            yemek_adi,
            yemek_resim_adi,
            yemek_fiyat,
            yemek_siparis_adet,
            kullanici_adi
        )
    }

    suspend fun sepettekiYemekleriGetir(kullaniciAdi: String): SepetYemeklerCevap {
        return try {
            yemekApi.sepettekiYemekleriGetir(kullaniciAdi)
        } catch (e: Exception) {
            SepetYemeklerCevap(emptyList(), success = 0)
        }
    }


    suspend fun sepettenYemekSil(sepet_yemek_id: Int, kullanici_adi: String): CRUDCevap {
        return yemekApi.sepettenYemekSil(sepet_yemek_id, kullanici_adi)
    }
}
