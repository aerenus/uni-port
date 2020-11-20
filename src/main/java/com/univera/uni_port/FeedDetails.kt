package com.univera.uni_port

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import kotlinx.android.synthetic.main.activity_feed_details.*

class FeedDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_details)

        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true);
        }

        val feedId = intent?.getIntExtra("feedId",0)
        val feedAcanDetay = intent?.getStringExtra("feedAcanDetay")
        val feedbildirim = intent?.getStringExtra("feedBaslik")
        val feedKategori = intent?.getStringExtra("feedKategori")
        val feedTarihDetay = intent?.getStringExtra("feedTarihDetay")
        val feedversiyonDetay = intent?.getStringExtra("feedversiyonDetay")
        val feedProje = intent?.getStringExtra("feedProje")
        val feedAciklama = intent?.getStringExtra("feedAciklama")
        val feedPaket = intent?.getStringExtra("feedPaket")


        title = "Bildirim Detay: $feedId"
        feedacandetay.text = feedAcanDetay
        feedbaslik.text = feedAciklama
        feedkategori.text = feedKategori
        feedtarihdetay.text = feedTarihDetay
        feedversiyondetay.text = feedversiyonDetay
        feedproje.text = feedProje
        feedpaket.text = feedPaket
        feedbildirim2.text = feedbildirim
    }
}