package com.univera.uni_port.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.univera.uni_port.Login
import com.univera.uni_port.MainActivity
import com.univera.uni_port.R
import com.univera.uni_port.service.Settings
import kotlinx.android.synthetic.main.fragment_settings.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val BASE_URL = "https://edk.univera.com.tr:8443/mobile/"
    private var settingsModels: ArrayList<com.univera.uni_port.model.Settings>? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity?)?.fragmentLock = 1
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_settings, container, false)
        activity?.setTitle("Ayarlar")

        val sharedPref : SharedPreferences?= activity?.getSharedPreferences("com.univera.uni_port", Context.MODE_PRIVATE);
        val set1 = sharedPref?.getInt("set1", 0)
        val set2 = sharedPref?.getInt("set2", 0)
        val set3 = sharedPref?.getInt("set3", 0)
        val set4 = sharedPref?.getInt("set4", 0)
        val set5 = sharedPref?.getInt("set5", 0)
        if (set1 == 1) {root.switch1.performClick()}
        if (set2 == 1) {root.switch2.performClick()}
        if (set3 == 1) {root.switch3.performClick()}
        if (set4 == 1) {root.switch4.performClick()}
        if (set5 == 1) {root.switch5.performClick()}

        root.switch1.setOnClickListener{
            if(sharedPref?.getInt("set1", 0) == 1){sharedPref?.edit()?.putInt("set1", 0)?.apply()
                root.settingsSave.performClick() }
            else {sharedPref?.edit()?.putInt("set1", 1)?.apply()
                root.settingsSave.performClick()}
        }
        root.switch2.setOnClickListener{
            if(sharedPref?.getInt("set2", 0) == 1){sharedPref?.edit()?.putInt("set2", 0)?.apply()
                root.settingsSave.performClick()}
            else {sharedPref?.edit()?.putInt("set2", 1)?.apply()
                root.settingsSave.performClick()}
        }
        root.switch3.setOnClickListener{
            if(sharedPref?.getInt("set3", 0) == 1){sharedPref?.edit()?.putInt("set3", 0)?.apply()
                root.settingsSave.performClick()}
            else {sharedPref?.edit()?.putInt("set3", 1)?.apply()
                root.settingsSave.performClick()}
        }
        root.switch4.setOnClickListener{
            if(sharedPref?.getInt("set4", 0) == 1){sharedPref?.edit()?.putInt("set4", 0)?.apply()
                root.settingsSave.performClick()}
            else {sharedPref?.edit()?.putInt("set4", 1)?.apply()
                root.settingsSave.performClick()}
        }
        root.switch5.setOnClickListener{
            if(sharedPref?.getInt("set5", 0) == 1){sharedPref?.edit()?.putInt("set5", 0)?.apply()
                root.settingsSave.performClick()}
            else {sharedPref?.edit()?.putInt("set5", 1)?.apply()
                root.settingsSave.performClick()}
        }

        root.logOutBtn.setOnClickListener {
            val i = Intent(activity, Login::class.java)
            sharedPref?.edit()?.putInt("auth", 0)?.apply()
            sharedPref?.edit()?.putInt("rights", -1)?.apply()
            sharedPref?.edit()?.putString("token", null)?.apply()
            sharedPref?.edit()?.putString("userName", null)?.apply()

            val toast = Toast.makeText(activity, "Çıkış yapıldı.", Toast.LENGTH_LONG)
            toast.show()

            startActivity(i)
        }

        root.settingsSave.setOnClickListener {
            root.settingsSave.text = "SENKRONİZE EDİLİYOR..."
            root.settingsSave.isClickable = false
            root.switch1.isClickable = false
            root.switch2.isClickable = false
            root.switch3.isClickable = false
            root.switch4.isClickable = false
            root.switch5.isClickable = false
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service = retrofit.create(Settings::class.java)
            val call = service.login(sharedPref?.getString("userName", ""), sharedPref?.getString("token", ""),
                sharedPref?.getInt("set1", 0), sharedPref?.getInt("set2", 0), sharedPref?.getInt("set3", 0), sharedPref?.getInt("set4", 0), sharedPref?.getInt("set5", 0))

            call.enqueue(object : Callback<List<com.univera.uni_port.model.Settings>> {


                override fun onResponse(
                    call: Call<List<com.univera.uni_port.model.Settings>>,
                    response: Response<List<com.univera.uni_port.model.Settings>>
                ) {
                    if (response.isSuccessful) {
                        println("setting sync...")
                        response.body()?.let {
                            settingsModels = ArrayList(it)

                            for (settingModel: com.univera.uni_port.model.Settings in settingsModels!!) {

                                if (settingModel.status == 1) {
                                    root.settingsSave.text = "AYAR SYNC"
                                    root.settingsSave.isClickable = true
                                    val toast = Toast.makeText(
                                        activity,
                                        "Eşitleme tamamlandı.",
                                        Toast.LENGTH_SHORT
                                    )
                                    toast.show()
                                    root.switch1.isClickable = true
                                    root.switch2.isClickable = true
                                    root.switch3.isClickable = true
                                    root.switch4.isClickable = true
                                    root.switch5.isClickable = true
                                }
                                else {
                                    val toast = Toast.makeText(
                                        activity,
                                        "Ayarlar sunucu ile eşitlenemedi.",
                                        Toast.LENGTH_SHORT
                                    )
                                    toast.show()
                                    root.switch1.isClickable = true
                                    root.switch2.isClickable = true
                                    root.switch3.isClickable = true
                                    root.switch4.isClickable = true
                                    root.switch5.isClickable = true
                                }
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<List<com.univera.uni_port.model.Settings>>, t: Throwable) {
                    val toast = Toast.makeText(
                        activity,
                        "Ayarlar sunucu ile eşitlenemedi.",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                    root.switch1.isClickable = true
                    root.switch2.isClickable = true
                    root.switch3.isClickable = true
                    root.switch4.isClickable = true
                    root.switch5.isClickable = true
                }



            })
        }





        (activity as MainActivity?)?.fragmentLock = 0
        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
