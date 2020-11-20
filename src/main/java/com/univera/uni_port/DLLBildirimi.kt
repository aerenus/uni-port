package com.univera.uni_port

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.univera.uni_port.model.DLLGecisi
import com.univera.uni_port.model.ProjectList
import kotlinx.android.synthetic.main.activity_d_l_l_bildirimi.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList


class DLLBildirimi : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener{

    lateinit var textView: TextView
    lateinit var button: Button
    var day = 0
    var month: Int = 0
    var year: Int = 0
    var hour: Int = 0
    var minute: Int = 0
    var myDay = 0
    var myMonth: Int = 0
    var myYear: Int = 0
    var myHour: Int = 0
    var myMinute: Int = 0
    var strHour: String = ""
    var strTime: String = ""
    val BASE_URL = "https://edk.univera.com.tr:8443/mobile/"
    var projectModels: ArrayList<com.univera.uni_port.model.ProjectList>? = null
    var dllGecisiModels: ArrayList<com.univera.uni_port.model.DLLGecisi>? = null
    lateinit var sharedPreferences : SharedPreferences

    var sendDLL: String = ""
    var sendDB: String = ""
    var sendConfig: String = ""
    var sendSet: String = ""
    var tipCount: Int = 0

    var projects = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_d_l_l_bildirimi)
        title = "DLL geçiş bildirimi"



        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true);
        }

        button = findViewById(R.id.btnPick)
        button.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            day = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)
            val datePickerDialog =
                DatePickerDialog(this@DLLBildirimi, this@DLLBildirimi, year, month, day)
            datePickerDialog.show()
    }


        getData()

        buttonSave.setOnClickListener {
            val sendProje = autoComplete.text.toString()
            if (sendProje.count()>2) {
                if (checkbox_dll.isChecked) {
                    sendDLL = "DLL Geçişi "
                    tipCount++
                }
                if (checkbox_dbupdate.isChecked) {
                    sendDB = "DB Update "
                    tipCount++
                }
                if (checkbox_config.isChecked) {
                    sendConfig = "Config Geçişi "
                    tipCount++
                }
                if (checkbox_set.isChecked) {
                    sendSet = "SET Geçişi "
                    tipCount++
                }
                if (tipCount>0) {
                    buttonSave.isClickable = false
                    val sendTip = sendDLL + sendDB + sendConfig + sendSet
                    val sendTarih = textView4.text.toString()
                    val sendYorum = editTextTextMultiLine.text.toString()

                    postData(sendProje, sendTip, sendTarih, sendYorum)
                    buttonSave.isClickable = true
                }
            }
        }

    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {

        myDay = day
        myYear = year
        myMonth = month
        val calendar: Calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(
            this@DLLBildirimi, this@DLLBildirimi, hour, minute,
            DateFormat.is24HourFormat(this)
        )
        timePickerDialog.show()

    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {

        myHour = p1
        myMinute = p2
        if(myHour<10){strHour = "0$myHour"} else {strHour = myHour.toString()}
        if(myMinute<10){strTime = "0$myMinute"} else {strTime = myMinute.toString()}
        val warn = "Seçilen tarih: $myDay/$myMonth/$myYear $strHour:$strTime"
        textView4.text = warn

    }



    private fun getData(){
        println("requesting...")
        val id = 0

        sharedPreferences = this.getSharedPreferences("com.univera.uni_port", MODE_PRIVATE)
        val userName = sharedPreferences.getString("userName", "")
        val token = sharedPreferences.getString("token", "")

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(com.univera.uni_port.service.ProjectList::class.java)
        val call = service.projectList(userName!!, token!!)

        call.enqueue(object : Callback<List<ProjectList>> {

            override fun onResponse(

                call: Call<List<ProjectList>>,
                response: Response<List<ProjectList>>
            ) {
                if (response.isSuccessful) {
                    println("response.isSuccessful")
                    response.body()?.let {
                        projectModels = ArrayList(it)

                        for (projectModel: ProjectList in projectModels!!) {
                            projects?.add(projectModel.proje)
                        }

                        val arrayAdapter = ArrayAdapter<String>(
                            this@DLLBildirimi,
                            android.R.layout.simple_list_item_1,
                            projects
                        )
                        autoComplete.setAdapter(arrayAdapter)
                        autoComplete.showDropDown()

                    }
                } else {
                    val toast = Toast.makeText(
                        applicationContext,
                        "Veri alınamadı.",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                }
            }

            override fun onFailure(
                call: Call<List<ProjectList>>,
                t: Throwable
            ) {
                val toast = Toast.makeText(
                    applicationContext,
                    // "Veri alınamadı. ${t.localizedMessage}",
                    "Proje listesi alınamadı. Lütfen internet bağlantınızı kontrol edin.",
                    Toast.LENGTH_SHORT
                )
                toast.show()
            }

        })

    }




    private fun postData(proje: String, tip: String, tarih: String, yorum: String){
        println("requesting...")
        buttonSave.isClickable = false
        buttonSave.text = "LÜTFEN BEKLEYİNİZ..."
        val id = 0

        sharedPreferences = this.getSharedPreferences("com.univera.uni_port", MODE_PRIVATE)
        val userName = sharedPreferences.getString("userName", "")
        val token = sharedPreferences.getString("token", "")

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(com.univera.uni_port.service.DLLGecisi::class.java)
        val call = service.postDLL(userName!!, token!!, proje, tip, tarih, yorum)

        call.enqueue(object : Callback<List<DLLGecisi>> {

            override fun onResponse(

                call: Call<List<DLLGecisi>>,
                response: Response<List<DLLGecisi>>
            ) {
                if (response.isSuccessful) {
                    println("response.isSuccessful")
                    response.body()?.let {
                        buttonSave.isClickable = false
                        buttonSave.text = "LÜTFEN BEKLEYİNİZ..."

                        val toast = Toast.makeText(
                            applicationContext,
                            "DLL bildirim girişi yapıldı.",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                        val i = Intent(applicationContext, MainActivity::class.java)
                        startActivity(i)


                    }
                } else {
                    val toast = Toast.makeText(
                        applicationContext,
                        "Veri alınamadı.",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                    buttonSave.isClickable = true
                    buttonSave.text = "TEKRAR DENE"
                }
            }

            override fun onFailure(
                call: Call<List<DLLGecisi>>,
                t: Throwable
            ) {
                val toast = Toast.makeText(
                    applicationContext,
                    // "Veri alınamadı. ${t.localizedMessage}",
                    "Bildirim oluşturulamadı. ${t.localizedMessage}",
                    Toast.LENGTH_SHORT
                )
                toast.show()
                buttonSave.text = "TEKRAR DENE"
            }

        })

    }


}