package com.univera.uni_port

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.univera.uni_port.fragments.FeedFragment
import com.univera.uni_port.fragments.ModuleFragment
import com.univera.uni_port.fragments.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var fragmentLock: Int = 0
    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val feedFragment = FeedFragment()
        val moduleFragment = ModuleFragment()
        val settingsFragment = SettingsFragment()

        changeFragment(feedFragment)

        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_feed -> changeFragment(feedFragment)
                R.id.ic_module -> changeFragment(moduleFragment)
                R.id.ic_settings -> changeFragment(settingsFragment)
            }
            true
        }

    }

    private fun changeFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {

        if (fragmentLock == 0) {

            replace(R.id.fl_wrapper, fragment)
            commit()
        }
            else {
            println("process running, fragment change locked.")}
        }
    }
}