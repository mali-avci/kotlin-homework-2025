package com.example.yemeksiparisapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.yemeksiparisapp.databinding.FragmentFavoritesBinding
import com.example.yemeksiparisapp.ui.adapter.YemekAdapter
import com.example.yemeksiparisapp.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var adapter: YemekAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        setupRecyclerView()
        observeFavoriler()
        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = YemekAdapter(
            onDetayTikla = { yemek -> /* Detay navigasyonu eklenebilir */ },
            onFavoriTikla = { yemek, ekle ->
                if (ekle) viewModel.favoriyeEkle(yemek.yemek_adi)
                else viewModel.favoridenCikar(yemek.yemek_adi)
            },
            onSepeteEkle = { yemek, adet ->
                if (adet <= 0) {
                    viewModel.sepettenSil(yemek.yemek_adi)
                    viewModel.setAdet(yemek.yemek_adi, 0)
                } else {
                    viewModel.sepeteEkle(
                        yemek_adi = yemek.yemek_adi,
                        yemek_resim_adi = yemek.yemek_resim_adi,
                        yemek_fiyat = yemek.yemek_fiyat.toIntOrNull() ?: 0,
                        yemek_siparis_adet = adet
                    )
                    viewModel.setAdet(yemek.yemek_adi, adet)
                }
            },
            onAdetDegisti = { yemekAdi, yeniAdet ->
                viewModel.setAdet(yemekAdi, yeniAdet)
            }
        )
        binding.rvFavoriler.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvFavoriler.adapter = adapter
    }

    private fun observeFavoriler() {
        lifecycleScope.launch {
            combine(
                viewModel.yemekListesi,
                viewModel.favoriler,
                viewModel.adetMap
            ) { yemekler, favoriler, adetMap ->
                val favoriYemekler = yemekler.filter { it.yemek_adi in favoriler }
                    .map { yemek -> Pair(yemek, adetMap[yemek.yemek_adi] ?: 0) }
                Triple(favoriYemekler, favoriler, adetMap)
            }.collectLatest { (favoriYemekler, favoriSet, adetMap) ->
                adapter.submitList(favoriYemekler)
                adapter.updateFavoriler(favoriSet)
                adapter.updateAdetMap(adetMap)
            }
        }
    }
    override fun onResume() {
        super.onResume()
        viewModel.tumYemekleriYukle()
        viewModel.sepetiYukle()
    }
}
