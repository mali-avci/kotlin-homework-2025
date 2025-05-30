package com.example.yemeksiparisapp.data.model

import java.io.Serializable

data class Yemek(
    val yemek_id: String,
    val yemek_adi: String,
    val yemek_resim_adi: String,
    val yemek_fiyat: String
) : Serializable