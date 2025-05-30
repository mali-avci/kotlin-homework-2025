package com.example.yemeksiparisapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yemeksiparisapp.data.model.SepetYemek
import com.example.yemeksiparisapp.data.repo.YemekRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: YemekRepository
) : ViewModel()  {

    private val _sepeteEkleDurumu = MutableStateFlow<String?>(null)
    val sepeteEkleDurumu: StateFlow<String?> = _sepeteEkleDurumu

    fun sepeteEkle(
        yemek_adi: String,
        yemek_resim_adi: String,
        yemek_fiyat: Int,
        yemek_siparis_adet: Int,
        kullanici_adi: String
    ) {
        viewModelScope.launch {
            try {
                repository.sepeteYemekEkle(
                    yemek_adi = yemek_adi,
                    yemek_resim_adi = yemek_resim_adi,
                    yemek_fiyat = yemek_fiyat,
                    yemek_siparis_adet = yemek_siparis_adet,
                    kullanici_adi = kullanici_adi
                )
                _sepeteEkleDurumu.value = "Sepete eklendi"
            } catch (e: Exception) {
                _sepeteEkleDurumu.value = "Hata: ${e.localizedMessage}"
            }
        }
    }
    suspend fun getSepetYemekListesi(kullaniciAdi: String): List<SepetYemek> {
        return try {
            repository.sepettekiYemekleriGetir(kullaniciAdi).sepet_yemekler
        } catch (e: Exception) {
            emptyList()
        }
    }
    suspend fun sepettenSil(sepetYemekId: Int, kullaniciAdi: String) {
        try {
            repository.sepettenYemekSil(sepetYemekId, kullaniciAdi)
        } catch (e: Exception) { }
    }

}
