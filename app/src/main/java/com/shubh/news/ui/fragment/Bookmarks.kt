package com.shubh.news.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.shubh.news.model.Article
import com.shubh.news.NewsAdapter
import com.shubh.news.R
import com.shubh.news.databinding.FragmentBookmarksBinding
import com.shubh.news.db.ArticleEntity
import com.shubh.news.db.NewsDatabase
import com.shubh.news.ui.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class Bookmarks : Fragment() {
    private val TAG = "Bookmarks"
    private var _binding: FragmentBookmarksBinding? = null
    private val binding get() = _binding!!
    private lateinit var newsDatabase: NewsDatabase
    private lateinit var bookmarkedArticles: List<ArticleEntity>
    private var articles = mutableListOf<Article>()
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookmarksBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        val activity = requireActivity() as MainActivity
        activity.showTabandViewPager()
        getData()
        newsAdapter.notifyDataSetChanged()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsDatabase = NewsDatabase.getDatabase(requireContext())

        getData()

        binding.bookmarksRecyclerView.layoutManager = LinearLayoutManager(context)
        newsAdapter = NewsAdapter(requireContext(), articles, {
            val article = Article(
                articles[it].description,
                articles[it].image,
                articles[it].publishedAt,
                articles[it].title,
                articles[it].url
            )
            val bundle = Bundle().apply {
                putParcelable("article",article)
            }
            val newsArticleFragment = NewsArticle()
            newsArticleFragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, newsArticleFragment).addToBackStack(null)
                .commit()
        }, { position ->
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Delete Bookmark")
                .setMessage("Do you want to delete selected bookmark?")
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton("Delete") { dialog, which ->
                    runBlocking {
                        withContext(Dispatchers.IO) {
                            newsDatabase.getArticleDao()
                                .deleteBookmark(bookmarkedArticles[position])
                            articles.removeAt(position)
                        }
                    }
                    newsAdapter.notifyItemRemoved(position)
                }
                .show()
        })
        binding.bookmarksRecyclerView.adapter = newsAdapter
//        findNavController().popBackStack()
    }

    private fun getData() {
        runBlocking {
            withContext(Dispatchers.IO) {
                bookmarkedArticles = newsDatabase.getArticleDao().getAllArticles()
            }
        }
        if (bookmarkedArticles.isNotEmpty()) {
            articles.clear()
            bookmarkedArticles.forEach {
                articles.add(
                    Article(
                        it.description,
                        it.image,
                        it.publishedAt,
                        it.title,
                        it.url
                    )
                )
            }
            Log.d(TAG, "onViewCreated: ${bookmarkedArticles.size}")
        } else {
            binding.bookmarksRecyclerView.visibility = View.GONE
            binding.errorMessageTextView.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}