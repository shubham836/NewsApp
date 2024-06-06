package com.shubh.news.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.shubh.news.model.Article
import com.shubh.news.NewsAdapter
import com.shubh.news.NewsViewModel
import com.shubh.news.R
import com.shubh.news.databinding.FragmentTopHeadlinesBinding
import com.shubh.news.db.ArticleEntity
import com.shubh.news.db.NewsDatabase
import com.shubh.news.ui.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class TopHeadlines : Fragment() {
    private val TAG = "TopHeadlines"
    private var _binding: FragmentTopHeadlinesBinding? = null
    private val binding get() = _binding!!
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var newsDatabase: NewsDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTopHeadlinesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchView.clearFocus()
        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = true
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query!!.isNotBlank()) {
                    newsViewModel.searchNews(query.trim())
                }
                binding.searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        newsDatabase = NewsDatabase.getDatabase(requireContext())
        binding.topHeadlinesRecyclerView.layoutManager = LinearLayoutManager(context)
        newsViewModel = ViewModelProvider(requireActivity()).get(NewsViewModel::class.java)


        newsViewModel.getTopHeadlines()
        newsViewModel.articlesList.observe(viewLifecycleOwner, Observer {
            binding.topHeadlinesRecyclerView.visibility = View.VISIBLE
            binding.loadingTextView.visibility = View.GONE
            newsAdapter = NewsAdapter(requireContext(), it, { position ->
                val article = Article(
                    it[position].description,
                    it[position].image,
                    it[position].publishedAt,
                    it[position].title,
                    it[position].url
                )
                val bundle = Bundle().apply {
                    putParcelable("article",article)
                }
                val newsArticleFragment = NewsArticle()
                newsArticleFragment.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, newsArticleFragment).addToBackStack(null)
                    .commit()
            }) { position ->
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Add Bookmark")
                    .setMessage("Do you want to bookmark selected News?")
                    .setNegativeButton("Cancel") { dialog, which ->
                        dialog.dismiss()
                    }
                    .setPositiveButton("Bookmark") { dialog, which ->
                        runBlocking {
                            withContext(Dispatchers.IO) {
                                newsDatabase.getArticleDao().addBookmark(
                                    ArticleEntity(
                                        description = it[position].description,
                                        image = it[position].image,
                                        url = it[position].url,
                                        title = it[position].title,
                                        publishedAt = it[position].publishedAt
                                    )
                                )
                            }
                        }
                        Toast.makeText(requireContext(), "Added to bookmarks", Toast.LENGTH_SHORT)
                            .show()
                    }
                    .show()
            }
            binding.topHeadlinesRecyclerView.adapter = newsAdapter

        })


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}