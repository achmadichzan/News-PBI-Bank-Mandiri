package com.achmadichzan.newspbibankmandiri

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.achmadichzan.newspbibankmandiri.adapter.HeadlineAdapter
import com.achmadichzan.newspbibankmandiri.adapter.NewsAdapter
import com.achmadichzan.newspbibankmandiri.model.HeadlineArticleItem
import com.achmadichzan.newspbibankmandiri.model.NewsArticleItem
import com.achmadichzan.newspbibankmandiri.databinding.ActivityMainBinding
import com.achmadichzan.newspbibankmandiri.ui.detail.DetailFragment
import com.achmadichzan.newspbibankmandiri.ui.detail.DetailNewsActivity
import com.achmadichzan.newspbibankmandiri.ui.home.HomeFragment
import com.achmadichzan.newspbibankmandiri.ui.home.HomeViewModel
import com.achmadichzan.newspbibankmandiri.util.ViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
                .addToBackStack(null)
                .commit()
        }
    }
}