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
class SepetViewModel @Inject constructor(
    private val repository: YemekRepository
) : ViewModel()  {

    private val _sepetListesi = MutableStateFlow<List<SepetYemek>>(emptyList())
    val sepetListesi: StateFlow<List<SepetYemek>> = _sepetListesi

    private val _hataMesaji = MutableStateFlow<String?>(null)
    val hataMesaji: StateFlow<String?> = _hataMesaji

    private val _basariMesaji = MutableStateFlow<String?>(null)
    val basariMesaji: StateFlow<String?> = _basariMesaji

    fun sepetiYukle(kullaniciAdi: String) {
        viewModelScope.launch {
            try {
                val cevap = repository.sepettekiYemekleriGetir(kullaniciAdi)
                _sepetListesi.value = cevap.sepet_yemekler
            } catch (e: Exception) {
                _hataMesaji.value = "Sepet yüklenemedi: ${e.localizedMessage}"
            }
        }
    }

    fun sepettenSil(sepetYemekId: Int, kullaniciAdi: String) {
        viewModelScope.launch {
            try {
                repository.sepettenYemekSil(sepetYemekId, kullaniciAdi)
                sepetiYukle(kullaniciAdi)
                _basariMesaji.value = "Ürün sepetten silindi"
            } catch (e: Exception) {
                _hataMesaji.value = "Silme başarısız: ${e.localizedMessage}"
            }
        }
    }

    fun sepeteEkle(
        yemek_adi: String,
        yemek_resim_adi: String,
        yemek_fiyat: Int,
        yemek_siparis_adet: Int,
        kullanici_adi: String
    ) {
        viewModelScope.launch {
            try {
                // Aynı ürünü sil
                val sepetCevap = repository.sepettekiYemekleriGetir(kullanici_adi)
                val mevcutYemek = sepetCevap.sepet_yemekler.find {
                    it.yemek_adi == yemek_adi
                }

                if (mevcutYemek != null) {
                    repository.sepettenYemekSil(mevcutYemek.sepet_yemek_id.toInt(), kullanici_adi)
                }

                // Yeni adetle tekrar ekle
                repository.sepeteYemekEkle(
                    yemek_adi = yemek_adi,
                    yemek_resim_adi = yemek_resim_adi,
                    yemek_fiyat = yemek_fiyat,
                    yemek_siparis_adet = yemek_siparis_adet,
                    kullanici_adi = kullanici_adi
                )

                sepetiYukle(kullanici_adi)
                _basariMesaji.value = "Adet güncellendi"

            } catch (e: Exception) {
                _hataMesaji.value = "Sepet güncellenemedi: ${e.localizedMessage}"
            }
        }
    }
    fun toplamFiyat(): Int {
        return _sepetListesi.value.sumOf {
            (it.yemek_fiyat.toIntOrNull() ?: 0) * (it.yemek_siparis_adet.toIntOrNull() ?: 1)
        }
    }
}
