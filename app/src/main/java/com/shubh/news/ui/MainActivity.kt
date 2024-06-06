package com.shubh.news.ui

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.shubh.news.NewsApp
import com.shubh.news.NewsRepository
import com.shubh.news.NewsViewModel
import com.shubh.news.NewsViewModelProviderFactory
import com.shubh.news.R
import com.shubh.news.databinding.ActivityMainBinding
import com.shubh.news.ui.fragment.Bookmarks
import com.shubh.news.ui.fragment.TopHeadlines

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var newsViewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val newsDB = (application as NewsApp).newsDB
        val newsViewModelFactory = NewsViewModelProviderFactory(NewsRepository(newsDB))
        newsViewModel = ViewModelProvider(this,newsViewModelFactory).get(NewsViewModel::class.java)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.viewPager.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Top Headlines"
                1 -> "Bookmarks"
                else -> null
            }
        }.attach()
    }
    fun hideTabandViewPager(){
        binding.viewPager.visibility = View.GONE
        binding.tabLayout.visibility = View.GONE
        binding.fragmentContainerView.visibility = View.VISIBLE
    }

    fun showTabandViewPager(){
        binding.viewPager.visibility = View.VISIBLE
        binding.tabLayout.visibility = View.VISIBLE
        binding.fragmentContainerView.visibility = View.GONE
    }

    inner class ViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> {
                    TopHeadlines()}
                1 -> {

                    Bookmarks()
                }
                else -> TopHeadlines()
            }
        }

    }
}

