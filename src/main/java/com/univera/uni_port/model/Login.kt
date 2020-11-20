package com.univera.uni_port.model

data class Login(
    val auth: Int,
    val rights: Int,
    val token: String,
    val userName: String,
    val playerID: String,
    val set1: Int,
    val set2: Int,
    val set3: Int,
    val set4: Int,
    val set5: Int
)