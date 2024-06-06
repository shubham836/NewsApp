package com.shubh.news.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.shubh.news.R
import com.shubh.news.databinding.FragmentNewsArticleBinding
import com.shubh.news.db.ArticleEntity
import com.shubh.news.db.NewsDatabase
import com.shubh.news.model.Article
import com.shubh.news.ui.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class NewsArticle : Fragment() {
    private val TAG = "NewsArticle"
    private var _binding: FragmentNewsArticleBinding? = null
    private val binding get() = _binding!!
    private lateinit var newsDatabase: NewsDatabase


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsArticleBinding.inflate(inflater, container, false)
        val activity = requireActivity() as MainActivity
        activity.hideTabandViewPager()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsDatabase = NewsDatabase.getDatabase(requireContext())
        val article:Article? = arguments?.getParcelable("article")
        binding.webview.loadUrl(article!!.url)

        binding.addBookmarkFab.setOnClickListener {
            runBlocking {
                withContext(Dispatchers.IO) {
                    try {
                        binding.addBookmarkFab.isClickable = false
                        newsDatabase.getArticleDao().addBookmark(
                            ArticleEntity(
                                description = article!!.description,
                                image = article.image,
                                url = article.url,
                                title = article.title,
                                publishedAt = article.publishedAt
                            )
                        )
                        binding.addBookmarkFab.setImageResource(R.drawable.bookmark_added)
                    } catch (e: Exception) {
                        Log.e(TAG, "onViewCreated: ${e.stackTraceToString()}")
                    }

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val activity = requireActivity() as MainActivity
        activity.showTabandViewPager()
        _binding = null
    }
}