package com.example.kotlinlesson.Odevler.Odev2


fun main() {

    fun toFahrenhiet(derece:Double) : Double{
        return (derece * 1.8 + 32)
    }

    fun cevreHesapla(edge1:Double, edge2:Double): Double{
        return 2*(edge1+edge2)
    }

    fun faktoriyelHesapla(sayi: Int):Int{ //recursive ile faktoriyel hesaplama
        if (sayi<=1) {
            return 1
        }else{
            return sayi * faktoriyelHesapla(sayi - 1)
        }
    }

    fun charCount(word:String) : String{
        var count= 0
        for(w in word){
            if (w == 'a'){
                count++
            }
        }
        return ("Kelimedeki 'a' harflerinin sayisi : $count")
    }

    fun icAcilarToplami(kenarSayisi: Int):Int{
        return (kenarSayisi-2)*180
    }

    fun maasHesapla(gunSayisi:Int):Int{
        val saatlikUcret = 10
        val gunlukCalismaSaati = 8
        val saatlikMesaiUcreti = 20
        val normalSaat = 160
        val toplamSaat = gunSayisi*gunlukCalismaSaati
        val mesaiSaati = toplamSaat-normalSaat
        if (toplamSaat>normalSaat){
             return (mesaiSaati * saatlikMesaiUcreti)+(normalSaat*saatlikUcret)
        }
        return saatlikUcret * toplamSaat

    }

    fun paketUcretlendirme(kota:Int):Int{
        val maxKota = 50
        val asimUcreti = 4
        var toplamUcret = 100

        if (maxKota<kota){
            return toplamUcret + (kota- maxKota) * asimUcreti
        }
        return toplamUcret
    }

}