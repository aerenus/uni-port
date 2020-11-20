package com.univera.uni_port

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.loading.*

class Loading : AppCompatActivity() {
    lateinit var sharedPreferences : SharedPreferences

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Android Device ID
        val deviceID: String = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        println(deviceID)

        setContentView(R.layout.loading)
        sharedPreferences = this.getSharedPreferences("com.univera.uni_port", MODE_PRIVATE)
        val getAuth = sharedPreferences.getInt("auth",0)
        val sessionID = sharedPreferences.getString("token","null")
        val userID = sharedPreferences.getString("userName","null")
        println("session id is " + sessionID)
        println("user id is " + userID)
        imageViewLoading.alpha = 0f
        imageViewLoading.animate().setDuration(0).alpha(1f).withEndAction{
            if (getAuth == 0){
                //redirectToLogin
                val i = Intent(this,Login::class.java)
                startActivity(i)
            } else {
                //loggedInAlready
                val i = Intent(this,MainActivity::class.java)
                startActivity(i)
            }
        }

    }
}