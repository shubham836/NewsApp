package com.shubh.news.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.shubh.news.model.Article
import com.shubh.news.NewsAdapter
import com.shubh.news.NewsResult
import com.shubh.news.NewsViewModel
import com.shubh.news.R
import com.shubh.news.databinding.FragmentTopHeadlinesBinding
import com.shubh.news.db.ArticleEntity

class TopHeadlines : Fragment() {
    private val TAG = "TopHeadlines"
    private var _binding: FragmentTopHeadlinesBinding? = null
    private val binding get() = _binding!!
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var newsViewModel: NewsViewModel
    private var allBookmarks = listOf<ArticleEntity>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTopHeadlinesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsViewModel = ViewModelProvider(requireActivity()).get(NewsViewModel::class.java)
        binding.searchView.clearFocus()
        newsViewModel.getAllBookmark()
        newsViewModel.allBookmarks.observe(viewLifecycleOwner, Observer {
            allBookmarks = it
        })
        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = true
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query!!.isNotBlank()) {
                    newsViewModel.getSearchNews(query.trim())
                }
                binding.searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        binding.topHeadlinesRecyclerView.layoutManager = LinearLayoutManager(context)


        newsViewModel.getTopHeadlines()
        newsViewModel.articlesList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NewsResult.Success -> {
                    binding.topHeadlinesRecyclerView.visibility = View.VISIBLE
                    binding.loadingTextView.visibility = View.GONE
                    newsAdapter = NewsAdapter(requireContext(), it.data!!, { position ->
                        val article = Article(
                            it.data[position].description,
                            it.data[position].image,
                            it.data[position].publishedAt,
                            it.data[position].title,
                            it.data[position].url
                        )
                        val bundle = Bundle().apply {
                            putParcelable("article", article)
                        }
                        val newsArticleFragment = NewsArticle()
                        newsArticleFragment.arguments = bundle

                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView, newsArticleFragment)
                            .addToBackStack(null)
                            .commit()
                    }) { position ->
                        var alreadyBookmarked = false
                        allBookmarks.forEach { articleEntity ->
                            if (articleEntity.title.trim() == it.data[position].title.trim()) {
                                alreadyBookmarked = true
                            }
                        }
                        if (alreadyBookmarked) {
                            Toast.makeText(
                                requireContext(),
                                "Already Bookmarked",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle("Add Bookmark")
                                .setMessage("Do you want to bookmark selected News?")
                                .setNegativeButton("Cancel") { dialog, which ->
                                    dialog.dismiss()
                                }
                                .setPositiveButton("Bookmark") { dialog, which ->
                                    newsViewModel.addBookmark(
                                        ArticleEntity(
                                            description = it.data[position].description,
                                            image = it.data[position].image,
                                            url = it.data[position].url,
                                            title = it.data[position].title,
                                            publishedAt = it.data[position].publishedAt
                                        )
                                    )
                                    Toast.makeText(
                                        requireContext(),
                                        "Added to bookmarks",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                                .show()
                        }

                    }
                    binding.topHeadlinesRecyclerView.adapter = newsAdapter
                }

                is NewsResult.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }

                is NewsResult.Loading -> {
                    binding.loadingTextView.visibility = View.VISIBLE
                }
            }

        })


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}