package com.shubh.news.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.shubh.news.R
import com.shubh.news.databinding.ActivityMainBinding
import com.shubh.news.ui.fragment.Bookmarks
import com.shubh.news.ui.fragment.TopHeadlines

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
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
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

//        navHostFragment.navController
//            .addOnDestinationChangedListener { controller, destination, arguments ->
//                when (destination.id) {
//                    R.id.newsArticle -> {
//                        binding.viewPager.visibility = View.GONE
//                        binding.tabLayout.visibility = View.GONE
//                        binding.fragmentContainerView.visibility = View.VISIBLE
//                    }
//
//                    else -> {
//                        binding.viewPager.visibility = View.VISIBLE
//                        binding.tabLayout.visibility = View.VISIBLE
//                        binding.fragmentContainerView.visibility = View.GONE
//                    }
//                }
//            }

//        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                when (position) {
//                    0 -> {
//                        binding.tabLayout.visibility = View.VISIBLE
//                        binding.viewPager.visibility = View.VISIBLE
//                        binding.fragmentContainerView.visibility = View.GONE
//                    }
//                    1 -> {
//                        binding.tabLayout.visibility = View.VISIBLE
//                        binding.viewPager.visibility = View.VISIBLE
//                        binding.fragmentContainerView.visibility = View.GONE
//                    }
//                    else -> {
//                        binding.tabLayout.visibility = View.GONE
//                        binding.viewPager.visibility = View.GONE
//                        binding.fragmentContainerView.visibility = View.VISIBLE
//                        throw IllegalArgumentException("Unexpected position $position")
//                    }
//                }
//
//            }
//        })
//        binding.tabLayout.selectT
//        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//                if (tab?.position == 1)
//                    binding.fragmentContainerView.findNavController().navigate(R.id.bookmarks)
//                else
//                    binding.fragmentContainerView.findNavController().navigate(R.id.topHeadlines)
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {
//
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//
//            }
//
//        })


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

