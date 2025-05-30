package com.example.yemeksiparisapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yemeksiparisapp.R
import com.example.yemeksiparisapp.databinding.FragmentSepetBinding
import com.example.yemeksiparisapp.ui.adapter.SepetAdapter
import com.example.yemeksiparisapp.ui.viewmodel.HomeViewModel
import com.example.yemeksiparisapp.ui.viewmodel.SepetViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SepetFragment : Fragment() {

    private lateinit var binding: FragmentSepetBinding
    private val viewModel: SepetViewModel by viewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var adapter: SepetAdapter

    private val kullaniciAdi = "mali_test"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSepetBinding.inflate(inflater, container, false)
        setupRecyclerView()
        observeVeriAkisi()
        setupOnaylaButton()
        viewModel.sepetiYukle(kullaniciAdi)
        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = SepetAdapter(
            onSil = { sepetYemek ->
                viewModel.sepettenSil(sepetYemek.sepet_yemek_id.toInt(), kullaniciAdi)
                homeViewModel.setAdet(sepetYemek.yemek_adi, 0)
            },
            onAdetGuncelle = { sepetYemek, yeniAdet ->
                viewModel.sepeteEkle(
                    yemek_adi = sepetYemek.yemek_adi,
                    yemek_resim_adi = sepetYemek.yemek_resim_adi,
                    yemek_fiyat = sepetYemek.yemek_fiyat.toInt(),
                    yemek_siparis_adet = yeniAdet,
                    kullanici_adi = kullaniciAdi
                )
                homeViewModel.setAdet(sepetYemek.yemek_adi, yeniAdet)
            }
        )
        binding.rvSepet.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSepet.adapter = adapter
    }

    private fun observeVeriAkisi() {
        lifecycleScope.launch {
            viewModel.sepetListesi.collectLatest { sepetYemekler ->
                adapter.submitList(sepetYemekler)

                // Adet güncellemesi
                val map = sepetYemekler.associate {
                    it.yemek_adi to (it.yemek_siparis_adet.toIntOrNull() ?: 1)
                }
                homeViewModel.setAdetMap(map)

                // Toplam fiyat güncelle
                val toplam = sepetYemekler.sumOf {
                    (it.yemek_fiyat.toIntOrNull() ?: 0) * (it.yemek_siparis_adet.toIntOrNull() ?: 1)
                }
                binding.textViewToplam.text = "Toplam: ₺$toplam"
            }
        }

        lifecycleScope.launch {
            viewModel.hataMesaji.collectLatest {
                it?.let { msg ->
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupOnaylaButton() {
        binding.buttonSepetiOnayla.setOnClickListener {
            val toplam = viewModel.sepetListesi.value.sumOf {
                (it.yemek_fiyat.toIntOrNull() ?: 0) * (it.yemek_siparis_adet.toIntOrNull() ?: 1)
            }

            Toast.makeText(
                requireContext(),
                "Sipariş onaylandı! Toplam tutar: ₺$toplam",
                Toast.LENGTH_LONG
            ).show()
            homeViewModel.tumYemekleriYukle()
            // Ana sayfaya yönlendir
            val navController = requireActivity()
                .supportFragmentManager
                .findFragmentById(R.id.navHostFragment)
                ?.findNavController()

            navController?.navigate(R.id.nav_home)

            // Tüm ürünleri sil
            val silinecekler = viewModel.sepetListesi.value
            silinecekler.forEach {
                viewModel.sepettenSil(it.sepet_yemek_id.toInt(), kullaniciAdi)
            }
            // İsteğe bağlı: Sepeti temizleme vs. burada yapılabilir
        }
    }
}
