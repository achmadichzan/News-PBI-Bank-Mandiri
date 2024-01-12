package com.achmadichzan.newspbibankmandiri

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.achmadichzan.newspbibankmandiri.databinding.ActivityMainBinding
import com.achmadichzan.newspbibankmandiri.ui.home.HomeFragment

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentManager = supportFragmentManager
        val homeFragment = HomeFragment()
        val fragment = fragmentManager.findFragmentByTag(HomeFragment::class.java.simpleName)

        if (fragment !is HomeFragment) {
            fragmentManager.beginTransaction()
                .add(R.id.main_activity, homeFragment, HomeFragment::class.java.simpleName)
                .commit()
        }
    }
}