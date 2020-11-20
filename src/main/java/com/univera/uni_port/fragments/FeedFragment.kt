package com.univera.uni_port.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.univera.uni_port.FeedDetails
import com.univera.uni_port.MainActivity
import com.univera.uni_port.R
import com.univera.uni_port.adapter.FeedAdapter
import com.univera.uni_port.model.Feed
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.android.synthetic.main.fragment_feed.view.*
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
 * Use the [FeedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FeedFragment : Fragment(), FeedAdapter.Listener {


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val BASE_URL = "https://edk.univera.com.tr:8443/mobile/"
    private var feedModels: ArrayList<com.univera.uni_port.model.Feed>? = null
    private var recyclerViewAdapter : FeedAdapter? = null

    lateinit var option : Spinner
    var filterMenuItem: MenuItem? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)


        }
        (activity as MainActivity?)?.fragmentLock = 1
        setHasOptionsMenu(true)

        val sharedPref: SharedPreferences? = activity?.getSharedPreferences(
            "com.univera.uni_port",
            Context.MODE_PRIVATE
        );
        val feedFilter = sharedPref?.getInt("feedFilter", 0)


        getData(feedFilter!!)
        (activity as MainActivity?)?.fragmentLock = 0


    }

    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity?)?.fragmentLock = 1
        // Inflate the layout for this fragment
        (activity as MainActivity?)?.fragmentLock = 1
        val root = inflater.inflate(R.layout.fragment_feed, container, false)

        println("layoutManager: RecyclerView.LayoutManager")
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        root.RecyclerView.layoutManager = layoutManager

        val sharedPref: SharedPreferences? = activity?.getSharedPreferences(
            "com.univera.uni_port",
            Context.MODE_PRIVATE
        );
        val feedFilter = sharedPref?.getInt("feedFilter", 0)

        activity?.setTitle("Akış")

        //SWIPE
        root.itemsswipetorefresh.isRefreshing = true
        root.itemsswipetorefresh.setColorSchemeColors(Color.parseColor("#b8213c"))
        root.itemsswipetorefresh.setOnRefreshListener {
            val sharedPrefGet: SharedPreferences? = activity?.getSharedPreferences(
                "com.univera.uni_port",
                Context.MODE_PRIVATE
            );
            val feedFilterGet = sharedPrefGet?.getInt("feedFilter", 0)

            println("print load feed cache")
            println(feedFilterGet)
            getData(feedFilterGet!!)
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
         * @return A new instance of fragment FeedFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FeedFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }



    fun getData(filter: Int){
        println("requesting...")
        (activity as MainActivity?)?.fragmentLock = 1
        val sharedPref: SharedPreferences? = activity?.getSharedPreferences(
            "com.univera.uni_port",
            Context.MODE_PRIVATE
        );
        val token = sharedPref?.getString("token", "")
        val userName = sharedPref?.getString("userName", "")

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(com.univera.uni_port.service.Feed::class.java)
        val call = service.feed(userName, token, filter)

        call.enqueue(object : Callback<List<com.univera.uni_port.model.Feed>> {

            override fun onResponse(

                call: Call<List<com.univera.uni_port.model.Feed>>,
                response: Response<List<com.univera.uni_port.model.Feed>>
            ) {
                if (response.isSuccessful) {
                    (activity as MainActivity?)?.fragmentLock = 1
                    println("response.isSuccessful")
                    response.body()?.let {
                        feedModels = ArrayList(it)

                        recyclerViewAdapter = FeedAdapter(
                            it as ArrayList<Feed>,this@FeedFragment
                        )

                        if (RecyclerView != null) {
                            RecyclerView.adapter = recyclerViewAdapter
                        } else {
                        }

                    }
                    (activity as MainActivity?)?.fragmentLock = 0

                    if (itemsswipetorefresh != null) {
                        itemsswipetorefresh.isRefreshing = false
                    } else {
                    }
                } else {
                    (activity as MainActivity?)?.fragmentLock = 0
                    val toast = Toast.makeText(
                        activity,
                        "Veri alınamadı.",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                    itemsswipetorefresh.isRefreshing = false
                }
            }

            override fun onFailure(
                call: Call<List<com.univera.uni_port.model.Feed>>,
                t: Throwable
            ) {
                (activity as MainActivity?)?.fragmentLock = 0
                val toast = Toast.makeText(
                    activity,
                    // "Veri alınamadı. ${t.localizedMessage}",
                    "Sonuç bulunamadı.",
                    Toast.LENGTH_SHORT
                )
                toast.show()
                itemsswipetorefresh.isRefreshing = false
            }

        })

    }


    override fun onItemClick(feedModel: Feed) {
        println("print frag onitemclick")
        println(feedModel.message)
       if(feedModel.cat == 3) {
           val i = Intent(context, FeedDetails::class.java)
           i.putExtra("feedAcanDetay", feedModel.author)
           i.putExtra("feedId", feedModel.id)
           i.putExtra("feedBaslik", feedModel.message)
           i.putExtra("feedKategori", feedModel.paket)
           i.putExtra("feedTarihDetay", feedModel.time)
           i.putExtra("feedversiyonDetay", feedModel.version)
           i.putExtra("feedAciklama", feedModel.aciklama)
           i.putExtra("feedProje", feedModel.proje)
           i.putExtra("feedPaket", feedModel.paket)

           startActivity(i)
       }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.feed_fragment_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val sharedPref: SharedPreferences? = activity?.getSharedPreferences(
            "com.univera.uni_port",
            Context.MODE_PRIVATE
        );

        val feedFilter = sharedPref?.getInt("feedFilter", 0)

        when (item.itemId){
            R.id.option_1_tum -> {
                sharedPref?.edit()?.putInt("feedFilter", 0)?.apply()
                getData(0)
                return true
            }
            R.id.option_1_sms -> {
                sharedPref?.edit()?.putInt("feedFilter", 1)?.apply()
                getData(1)
                return true
            }
            R.id.option_1_dll -> {
                sharedPref?.edit()?.putInt("feedFilter", 2)?.apply()
                getData(2)
                return true
            }
            R.id.option_1_tfs -> {
                sharedPref?.edit()?.putInt("feedFilter", 3)?.apply()
                getData(3)
                return true
            }
            R.id.option_1_nob -> {
                sharedPref?.edit()?.putInt("feedFilter", 4)?.apply()
                getData(4)
                return true
            }
            R.id.option_1_yon -> {
                sharedPref?.edit()?.putInt("feedFilter", 5)?.apply()
                getData(5)
                return true
            }




            R.id.option_2_satir -> {
                sharedPref?.edit()?.putInt("gorunumTip", 0)?.apply()
                getData(feedFilter!!)
                return true
            }

            R.id.option_2_izgara -> {
                sharedPref?.edit()?.putInt("gorunumTip", 1)?.apply()
                getData(feedFilter!!)
                return true
            }




        }
        return super.onOptionsItemSelected(item)
    }

}