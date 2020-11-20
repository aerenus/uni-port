package com.univera.uni_port.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.univera.uni_port.DLLBildirimi
import com.univera.uni_port.FeedDetails
import com.univera.uni_port.MainActivity
import com.univera.uni_port.R
import kotlinx.android.synthetic.main.fragment_module.*
import kotlinx.android.synthetic.main.fragment_module.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ModuleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ModuleFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        activity?.setTitle("Modüller")
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_module, container, false)

        root.cardDLL.setOnClickListener {
            val i = Intent(context, DLLBildirimi::class.java)
            startActivity(i)

        }

        root.cardAdmin.setOnClickListener {
            val toast = Toast.makeText(
                activity,
                "Bu modüle giriş yetkiniz yoktur.",
                Toast.LENGTH_SHORT
            )
            toast.show()
        }

        root.cardNotif.setOnClickListener {
            val toast = Toast.makeText(
                activity,
                "Bu modüle giriş yetkiniz yoktur.",
                Toast.LENGTH_SHORT
            )
            toast.show()
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
         * @return A new instance of fragment ModuleFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ModuleFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}