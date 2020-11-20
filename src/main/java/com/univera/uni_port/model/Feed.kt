package com.univera.uni_port.model

data class Feed(
    val id: Int,
    val author: String,
    val message: String,
    val cat: Int,
    val time: String,
    val version: String,
    val paket: String,
    val aciklama: String,
    val proje: String
)