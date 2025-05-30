package com.example.yemeksiparisapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.yemeksiparisapp.R
import com.example.yemeksiparisapp.databinding.FragmentDetailBinding
import com.example.yemeksiparisapp.ui.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val args: DetailFragmentArgs by navArgs()
    private val viewModel: DetailViewModel by viewModels()

    private var adet = 1
    private val kullaniciAdi = "mali_test"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)

        val yemek = args.yemek

        // Yemek bilgilerini yerleştir
        binding.textViewYemekAd.text = yemek.yemek_adi
        binding.textViewYemekFiyat.text = "${yemek.yemek_fiyat} ₺"
        binding.textViewAdet.text = adet.toString()

        val resimUrl = "http://kasimadalan.pe.hu/yemekler/resimler/${yemek.yemek_resim_adi}"
        Glide.with(this).load(resimUrl).into(binding.imageViewYemek)

        // Adet azalt
        binding.buttonAzalt.setOnClickListener {
            if (adet > 1) {
                adet--
                binding.textViewAdet.text = adet.toString()
            }
        }

        // Adet artır
        binding.buttonArttir.setOnClickListener {
            adet++
            binding.textViewAdet.text = adet.toString()
        }

        // Sepete ekle
        binding.buttonSepeteEkle.setOnClickListener {
            val fiyat = yemek.yemek_fiyat.toIntOrNull() ?: 0

            lifecycleScope.launch {
                val sepetListesi = viewModel.getSepetYemekListesi(kullaniciAdi)
                val mevcut = sepetListesi.find {
                    it.yemek_adi == yemek.yemek_adi &&
                            it.yemek_resim_adi == yemek.yemek_resim_adi &&
                            it.yemek_fiyat.toIntOrNull() == fiyat
                }

                val toplamAdet = (mevcut?.yemek_siparis_adet?.toIntOrNull() ?: 0) + adet

                // Mevcut varsa önce sil
                mevcut?.sepet_yemek_id?.toIntOrNull()?.let { sepetId ->
                    viewModel.sepettenSil(sepetId, kullaniciAdi)
                }

                // yeni adetle tekrar ekle
                viewModel.sepeteEkle(
                    yemek_adi = yemek.yemek_adi,
                    yemek_resim_adi = yemek.yemek_resim_adi,
                    yemek_fiyat = fiyat,
                    yemek_siparis_adet = toplamAdet,
                    kullanici_adi = kullaniciAdi
                )
            }
        }


        observeSepeteEkleDurumu()

        return binding.root
    }

    private fun observeSepeteEkleDurumu() {
        lifecycleScope.launch {
            viewModel.sepeteEkleDurumu.collectLatest { mesaj ->
                mesaj?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.nav_home)
                }

            }

        }
    }
}
