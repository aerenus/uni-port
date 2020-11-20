package com.univera.uni_port

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.univera.uni_port.model.Login
import com.onesignal.OneSignal
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Login : AppCompatActivity() {
    private val BASE_URL = "https://edk.univera.com.tr:8443/mobile/"
    private var loginModels: ArrayList<Login>? = null

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        title = "Giriş"
        //Android Device ID
        val deviceID: String = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ANDROID_ID
        )
        println(deviceID)




        // OneSignal Initialization
        OneSignal.startInit(this)
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .unsubscribeWhenNotificationsAreDisabled(true)
            .init()
        val status = OneSignal.getPermissionSubscriptionState()
        status.permissionStatus.enabled



        sendPostBtn.setOnClickListener{
            sendPostBtn.text = "Lütfen bekleyiniz..."
            sendPostBtn.isClickable = false
            val userName = userNameLogin.text.toString()
            val password = passwordLogin.text.toString()

            if(userName != "" && password != ""){
                val playerID = status.subscriptionStatus.userId
                println("playerid is")
                println(playerID)
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val i = Intent(this, MainActivity::class.java)
                val service = retrofit.create(com.univera.uni_port.service.Login::class.java)
                val call = service.login(userName, password, deviceID, playerID)
                println("login strings")
                println(userName)
                println(password)
                println(deviceID)
                println(playerID)
                call.enqueue(object : Callback<List<Login>> {

                    override fun onResponse(
                        call: Call<List<Login>>,
                        response: Response<List<Login>>
                    ) {
                        if (response.isSuccessful) {
                            getData(response)
                        }
                        else {
                            val toast = Toast.makeText(
                                applicationContext,
                                "Bildirim API bir cihaz ID döndürmedi. İnternet bağlantınızı kontrol edip tekrar deneyiniz.",
                                Toast.LENGTH_LONG
                            )
                            toast.show()
                            sendPostBtn.text = "GİRİŞ"
                            sendPostBtn.isClickable = true

                        }
                    }

                    override fun onFailure(call: Call<List<Login>>, t: Throwable) {
                        val toast = Toast.makeText(
                            applicationContext,
                            t.localizedMessage,
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                        sendPostBtn.text = "GİRİŞ"
                        sendPostBtn.isClickable = true
                    }

                })

            }
            else {
                sendPostBtn.text = "GİRİŞ"
                sendPostBtn.isClickable = true
                val toast = Toast.makeText(
                    applicationContext,
                    "Kullanıcı adı ya da şifre boş olamaz.",
                    Toast.LENGTH_SHORT
                )
                toast.show()
            }
        }





    }

    fun getData(response: Response<List<Login>>) {
        response.body()?.let {
            loginModels = ArrayList(it)

            for (loginModel: Login in loginModels!!) {

                if (loginModel.auth == 1) {


                    println("login taken")
                    val sharedPreferences = getSharedPreferences(
                        "com.univera.uni_port",
                        MODE_PRIVATE
                    )
                    sharedPreferences.edit().putInt("auth", loginModel.auth)
                        .apply()
                    println("auth value is ${loginModel.auth}")
                    sharedPreferences.edit()
                        .putInt("rights", loginModel.rights)
                        .apply()
                    sharedPreferences.edit()
                        .putString("token", loginModel.token)
                        .apply()
                    sharedPreferences.edit().putString(
                        "userName",
                        loginModel.userName
                    ).apply()
                    println("username is ${loginModel.userName}")

                    sharedPreferences.edit().putInt("set1", loginModel.set1)
                        .apply()
                    println("login model set 1 value is ${loginModel.set1}")
                    sharedPreferences.edit().putInt("set2", loginModel.set2)
                        .apply()
                    sharedPreferences.edit().putInt("set3", loginModel.set3)
                        .apply()
                    sharedPreferences.edit().putInt("set4", loginModel.set4)
                        .apply()
                    sharedPreferences.edit().putInt("set5", loginModel.set5)
                        .apply()

                    val toast = Toast.makeText(
                        applicationContext,
                        "Giriş yapıldı.",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                    val i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                } else {
                    val toast = Toast.makeText(
                        applicationContext,
                        "Kullanıcı adı veya şifreniz hatalıdır.",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                    sendPostBtn.text = "GİRİŞ"
                    sendPostBtn.isClickable = true
                }
            }
        }
    }
}