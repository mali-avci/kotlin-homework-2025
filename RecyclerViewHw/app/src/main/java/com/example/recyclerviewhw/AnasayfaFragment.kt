package com.example.recyclerviewhw

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.recyclerviewhw.databinding.FragmentAnasayfaBinding


class AnasayfaFragment : Fragment() {
    private lateinit var binding: FragmentAnasayfaBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAnasayfaBinding.inflate(inflater, container, false)

        val kategoriListesi = ArrayList<Kategori>()


        val filmlerSample = listOf(
            Filmler(1, "Django", "django"),
            Filmler(2, "Interstellar", "interstellar"),
            Filmler(3, "Inception", "inception"),
            Filmler(4, "The Hateful Eight", "thehatefuleight"),
            Filmler(5, "The Pianist", "thepianist"),
            Filmler(6, "Anadoluda", "anadoluda")
        )

        // 5 kategori oluştur
        val kategoriBasliklari = listOf(
            "Sıradaki Önerimiz",
            "Bunları Sevebilirsiniz",
            "Bilim Kurgu",
            "Dram Klasikleri",
            "Tarihsel Filmler"
        )

        for (baslik in kategoriBasliklari) {
            kategoriListesi.add(Kategori(baslik, filmlerSample))
        }

        binding.kategorilerRw.layoutManager = LinearLayoutManager(requireContext())
        binding.kategorilerRw.adapter = KategoriAdapter(kategoriListesi)

        return binding.root
    }

}