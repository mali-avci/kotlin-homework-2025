// HomeViewModel.kt
package com.example.yemeksiparisapp.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yemeksiparisapp.data.local.FavoritesManager
import com.example.yemeksiparisapp.data.model.SepetYemek
import com.example.yemeksiparisapp.data.model.Yemek
import com.example.yemeksiparisapp.data.repo.YemekRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: YemekRepository,
    @ApplicationContext context: Context
) : ViewModel() {

    private val favoritesManager = FavoritesManager(context)

    private val _yemekListesi = MutableStateFlow<List<Yemek>>(emptyList())
    val yemekListesi: StateFlow<List<Yemek>> = _yemekListesi

    private val _filtreliListe = MutableStateFlow<List<Yemek>>(emptyList())
    val filtreliListe: StateFlow<List<Yemek>> = _filtreliListe

    private val _sepetListesi = MutableStateFlow<List<SepetYemek>>(emptyList())
    val sepetListesi: StateFlow<List<SepetYemek>> = _sepetListesi

    private val _adetMap = MutableStateFlow<Map<String, Int>>(emptyMap())
    val adetMap: StateFlow<Map<String, Int>> = _adetMap

    private val _favoriler = MutableStateFlow<Set<String>>(emptySet())
    val favoriler: StateFlow<Set<String>> = _favoriler

    private val kullaniciAdi = "mali_test"

    init {
        tumYemekleriYukle()
        sepetiYukle()
        observeFavoriler()
    }

    private fun observeFavoriler() {
        viewModelScope.launch {
            favoritesManager.favoriYemekler.collectLatest { set ->
                _favoriler.value = set
            }
        }
    }

    fun favoriyeEkle(yemekAdi: String) {
        viewModelScope.launch {
            favoritesManager.favoriEkle(yemekAdi)
        }
    }

    fun favoridenCikar(yemekAdi: String) {
        viewModelScope.launch {
            favoritesManager.favoriSil(yemekAdi)
        }
    }

    fun tumYemekleriYukle() {
        viewModelScope.launch {
            try {
                val cevap = repository.tumYemekleriGetir()
                _yemekListesi.value = cevap.yemekler
                _filtreliListe.value = cevap.yemekler
            } catch (_: Exception) {}
        }
    }

    fun filtreleYemekler(query: String) {
        val liste = _yemekListesi.value
        val filtreli = if (query.isBlank()) liste else liste.filter {
            it.yemek_adi.contains(query, ignoreCase = true)
        }
        _filtreliListe.value = filtreli
    }

    fun sepeteEkle(
        yemek_adi: String,
        yemek_resim_adi: String,
        yemek_fiyat: Int,
        yemek_siparis_adet: Int
    ) {
        viewModelScope.launch {
            try {
                val cevap = repository.sepettekiYemekleriGetir(kullaniciAdi)
                val mevcut = cevap.sepet_yemekler.find { it.yemek_adi == yemek_adi }
                val yeniAdet = yemek_siparis_adet
                if (mevcut != null) {
                    repository.sepettenYemekSil(mevcut.sepet_yemek_id.toInt(), kullaniciAdi)
                }
                repository.sepeteYemekEkle(
                    yemek_adi, yemek_resim_adi, yemek_fiyat, yeniAdet, kullaniciAdi
                )
                sepetiYukle()
            } catch (_: Exception) {}
        }
    }

    fun sepetiYukle() {
        viewModelScope.launch {
            try {
                val cevap = repository.sepettekiYemekleriGetir(kullaniciAdi)
                _sepetListesi.value = cevap.sepet_yemekler
                val map = cevap.sepet_yemekler.associate {
                    it.yemek_adi to (it.yemek_siparis_adet.toIntOrNull() ?: 1)
                }
                _adetMap.value = map
            } catch (_: Exception) {
                _sepetListesi.value = emptyList()
                _adetMap.value = emptyMap()
            }
        }
    }

    fun setAdet(yemekAdi: String, yeniAdet: Int) {
        _adetMap.value = _adetMap.value.toMutableMap().apply {
            if (yeniAdet <= 0) remove(yemekAdi)
            else this[yemekAdi] = yeniAdet
        }
    }

    fun setAdetMap(yeniMap: Map<String, Int>) {
        _adetMap.value = yeniMap
    }

    fun sepettenSil(yemekAdi: String) {
        viewModelScope.launch {
            try {
                val cevap = repository.sepettekiYemekleriGetir(kullaniciAdi)
                val mevcut = cevap.sepet_yemekler.find { it.yemek_adi == yemekAdi }
                if (mevcut != null) {
                    repository.sepettenYemekSil(mevcut.sepet_yemek_id.toInt(), kullaniciAdi)
                    sepetiYukle()
                }
            } catch (_: Exception) {}
        }
    }
}
