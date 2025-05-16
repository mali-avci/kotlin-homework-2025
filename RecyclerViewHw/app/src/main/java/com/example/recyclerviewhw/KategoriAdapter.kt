package com.example.recyclerviewhw

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewhw.databinding.ItemKategoriBinding

class KategoriAdapter(
    private val kategoriListesi: List<Kategori>
) : RecyclerView.Adapter<KategoriAdapter.KategoriViewHolder>() {

    inner class KategoriViewHolder(val binding: ItemKategoriBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KategoriViewHolder {
        val binding = ItemKategoriBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return KategoriViewHolder(binding)
    }

    override fun onBindViewHolder(holder: KategoriViewHolder, position: Int) {
        val kategori = kategoriListesi[position]
        holder.binding.textViewKategoriBaslik.text = kategori.baslik

        val filmAdapter = FilmlerAdapter(holder.itemView.context, kategori.filmler)
        holder.binding.recyclerAltListe.layoutManager =
            LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
        holder.binding.recyclerAltListe.adapter = filmAdapter
    }

    override fun getItemCount(): Int = kategoriListesi.size
}
