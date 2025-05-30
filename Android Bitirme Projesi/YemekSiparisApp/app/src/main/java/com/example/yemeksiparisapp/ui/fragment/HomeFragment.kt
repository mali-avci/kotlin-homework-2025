package com.example.yemeksiparisapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.yemeksiparisapp.databinding.FragmentHomeBinding
import com.example.yemeksiparisapp.ui.adapter.YemekAdapter
import com.example.yemeksiparisapp.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import androidx.appcompat.widget.SearchView

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: YemekAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupRecyclerView()
        setupSearchBar()
        observeYemekVeFavoriler()

        return binding.root
    }

    private fun setupRecyclerView() {

        adapter = YemekAdapter(
            onDetayTikla = { yemek ->
                val action = HomeFragmentDirections.actionNavHomeToDetailFragment(yemek)
                findNavController().navigate(action)
            },
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
        binding.rvYemekler.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvYemekler.adapter = adapter
    }

    private fun observeYemekVeFavoriler() {
        lifecycleScope.launch {
            combine(
                viewModel.filtreliListe,
                viewModel.adetMap,
                viewModel.favoriler
            ) { yemekler, adetMap, favoriSet ->
                Triple(yemekler, adetMap, favoriSet)
            }.collectLatest { (yemekler, adetMap, favoriSet) ->
                val yemeklerWithAdet = yemekler.map { yemek ->
                    Pair(yemek, adetMap[yemek.yemek_adi] ?: 0)
                }
                adapter.submitList(yemeklerWithAdet)
                adapter.updateFavoriler(favoriSet)
            }
        }
    }
    private fun setupSearchBar() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.filtreleYemekler(newText.orEmpty())
                return true
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.tumYemekleriYukle()
        viewModel.sepetiYukle()
    }
}
