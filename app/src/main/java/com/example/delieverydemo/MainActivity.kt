package com.example.delieverydemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.delieverydemo.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

/**
 *
​
 * Purpose – Class act as First Screen of the application
​
 * @author ​Rishabh Gupta
​
 * Created on January 8, 2020
​
 * Modified on January 8, 2020
 *
 * */

class MainActivity : AppCompatActivity() {
    private lateinit var bindingView: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingView = DataBindingUtil.setContentView(this, R.layout.activity_main)

        init()
    }

    /**
     * inital setUps
     */
    private fun init(){
        val findNavController = findNavController(R.id.nav_host_fragment)

        // Setup Action Bar
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(findNavController)

        // Setup Toolbar
        toolbar.setupWithNavController(findNavController)

        findNavController.addOnDestinationChangedListener { navController, destination, arguments ->
            when (destination.id) {
                R.id.delivery_detail_frag -> {
                    toolbar.title = getString(R.string.text_delivery_details)
                    // toolbar.title = getString(com.nthrewards.android.R.string.title_otp)
                    toolbar.setNavigationIcon(R.drawable.img_arrow_left_black)

                }
            }
        }


    }
}
