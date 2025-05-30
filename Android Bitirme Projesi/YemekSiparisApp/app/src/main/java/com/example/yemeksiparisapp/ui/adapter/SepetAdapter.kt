package com.example.yemeksiparisapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.yemeksiparisapp.data.model.SepetYemek
import com.example.yemeksiparisapp.databinding.CardSepetYemekBinding

class SepetAdapter(
    private val onSil: (SepetYemek) -> Unit,
    private val onAdetGuncelle: (SepetYemek, Int) -> Unit
) : RecyclerView.Adapter<SepetAdapter.SepetViewHolder>() {

    private val sepetYemekListesi = mutableListOf<SepetYemek>()

    fun submitList(liste: List<SepetYemek>) {
        sepetYemekListesi.clear()
        sepetYemekListesi.addAll(liste)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SepetViewHolder {
        val binding = CardSepetYemekBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SepetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SepetViewHolder, position: Int) {
        holder.bind(sepetYemekListesi[position])
    }

    override fun getItemCount(): Int = sepetYemekListesi.size

    inner class SepetViewHolder(private val binding: CardSepetYemekBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(sepetYemek: SepetYemek) {
            var adet = sepetYemek.yemek_siparis_adet.toIntOrNull() ?: 1
            val birimFiyat = sepetYemek.yemek_fiyat.toIntOrNull() ?: 0

            // UI güncelleme fonksiyonu
            fun guncelleUI() {
                binding.textViewSepetYemekAd.text = sepetYemek.yemek_adi
                binding.textViewSepetYemekFiyat.text = "Fiyat: ${birimFiyat * adet} ₺"
                binding.textViewSepetYemekAdet.text = adet.toString()

                val resimUrl = "http://kasimadalan.pe.hu/yemekler/resimler/${sepetYemek.yemek_resim_adi}"
                Glide.with(binding.imageViewSepetYemek.context)
                    .load(resimUrl)
                    .into(binding.imageViewSepetYemek)
            }

            guncelleUI()

            // Sil butonu
            binding.buttonSepettenSil.setOnClickListener {
                onSil(sepetYemek)
            }

            // Arttır
            binding.buttonArttir.setOnClickListener {
                adet++
                guncelleUI()
                onAdetGuncelle(sepetYemek, adet)
            }

            // Azalt → 0 olursa sil
            binding.buttonAzalt.setOnClickListener {
                if (adet > 1) {
                    adet--
                    guncelleUI()
                    onAdetGuncelle(sepetYemek, adet)
                } else {
                    adet = 0
                    guncelleUI()
                    onSil(sepetYemek)
                }
            }
        }
    }
}
