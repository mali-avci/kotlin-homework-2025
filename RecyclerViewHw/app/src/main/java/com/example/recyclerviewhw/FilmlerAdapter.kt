package com.example.recyclerviewhw

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewhw.databinding.CardTasarimBinding
import com.google.android.material.snackbar.Snackbar

class FilmlerAdapter(var mContext: Context,var filmlerListesi: List<Filmler>) : RecyclerView.Adapter<FilmlerAdapter.CardTasarimHolder>() {

    inner class CardTasarimHolder(var tasarim: CardTasarimBinding ) : RecyclerView.ViewHolder(tasarim.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardTasarimHolder {
        var binding = CardTasarimBinding.inflate(LayoutInflater.from(mContext),parent,false)
        return CardTasarimHolder(binding)
    }

    override fun onBindViewHolder(holder: CardTasarimHolder, position: Int) {
        val film = filmlerListesi.get(position)
        val t = holder.tasarim

        t.imageViewFilm.setImageResource(mContext.resources.getIdentifier(film.resim,"drawable",mContext.packageName))
        t.cardViewFilm.setOnClickListener {
            Snackbar.make(it, "Geliştirme aşamasında...", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return filmlerListesi.size
    }


}