package com.example.yemeksiparisapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.yemeksiparisapp.R
import com.example.yemeksiparisapp.data.model.Yemek
import com.example.yemeksiparisapp.databinding.CardYemekBinding

class YemekAdapter(
    private val onDetayTikla: (Yemek) -> Unit,
    private val onFavoriTikla: (Yemek, Boolean) -> Unit,
    private val onSepeteEkle: (Yemek, Int) -> Unit,
    private val onAdetDegisti: (String, Int) -> Unit
) : RecyclerView.Adapter<YemekAdapter.YemekViewHolder>() {

    private val yemeklerWithAdet = mutableListOf<Pair<Yemek, Int>>()
    private var favoriYemekAdlari: Set<String> = emptySet()
    private var adetMap: Map<String, Int> = emptyMap()

    fun submitList(liste: List<Pair<Yemek, Int>>) {
        yemeklerWithAdet.clear()
        yemeklerWithAdet.addAll(liste)
        notifyDataSetChanged()
    }

    fun updateFavoriler(favoriler: Set<String>) {
        favoriYemekAdlari = favoriler
        notifyDataSetChanged()
    }

    fun updateAdetMap(newAdetMap: Map<String, Int>) {
        adetMap = newAdetMap
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YemekViewHolder {
        val binding = CardYemekBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return YemekViewHolder(binding)
    }

    override fun onBindViewHolder(holder: YemekViewHolder, position: Int) {
        val (yemek, adet) = yemeklerWithAdet[position]
        holder.bind(yemek, adet)
    }

    override fun getItemCount(): Int = yemeklerWithAdet.size

    inner class YemekViewHolder(private val binding: CardYemekBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(yemek: Yemek, adet: Int) = with(binding) {
            textViewYemekAd.text = yemek.yemek_adi
            textViewYemekFiyat.text = "₺ ${yemek.yemek_fiyat}"
            textViewUcretsizGonderim.text = "🚴‍♂️ Ücretsiz Gönderim"
            textViewAdet.text = adet.toString()

            val imageUrl = "http://kasimadalan.pe.hu/yemekler/resimler/${yemek.yemek_resim_adi}"
            Glide.with(imageViewYemek.context)
                .load(imageUrl)
                .into(imageViewYemek)

            adetKontrolLayout.visibility = if (adet > 0) View.VISIBLE else View.GONE

            buttonArttir.setOnClickListener {
                val yeniAdet = (textViewAdet.text.toString().toIntOrNull() ?: 0) + 1
                textViewAdet.text = yeniAdet.toString()
                adetKontrolLayout.visibility = View.VISIBLE
                onAdetDegisti(yemek.yemek_adi, yeniAdet)
                onSepeteEkle(yemek, yeniAdet)
            }

            buttonAzalt.setOnClickListener {
                val mevcutAdet = textViewAdet.text.toString().toIntOrNull() ?: 0
                val yeniAdet = mevcutAdet - 1
                if (yeniAdet <= 0) {
                    textViewAdet.text = "0"
                    adetKontrolLayout.visibility = View.GONE
                    onAdetDegisti(yemek.yemek_adi, 0)
                    onSepeteEkle(yemek, 0)
                } else {
                    textViewAdet.text = yeniAdet.toString()
                    onAdetDegisti(yemek.yemek_adi, yeniAdet)
                    onSepeteEkle(yemek, yeniAdet)
                }
            }

            buttonSepeteEkle.setOnClickListener {
                val yeniAdet = (textViewAdet.text.toString().toIntOrNull() ?: 0) + 1
                textViewAdet.text = yeniAdet.toString()
                adetKontrolLayout.visibility = View.VISIBLE
                onAdetDegisti(yemek.yemek_adi, yeniAdet)
                onSepeteEkle(yemek, yeniAdet)
            }

            val favoriMi = favoriYemekAdlari.contains(yemek.yemek_adi)

            buttonFavori.setImageResource(
                if (favoriMi) R.drawable.ic_favorite else R.drawable.ic_favorite_border
            )
            buttonFavori.setColorFilter(
                ContextCompat.getColor(buttonFavori.context,
                    if (favoriMi) android.R.color.holo_red_light else android.R.color.black)
            )

            buttonFavori.setOnClickListener {
                val yeniFavoriDurum = !favoriMi
                onFavoriTikla(yemek, yeniFavoriDurum)
            }

            imageViewYemek.setOnClickListener {
                onDetayTikla(yemek)
            }
        }
    }
}
