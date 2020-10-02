package com.ozgurberat.newsprojectkotlin.view.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.ozgurberat.newsprojectkotlin.R
import com.ozgurberat.newsprojectkotlin.adapter.NewsRecyclerAdapter
import com.ozgurberat.newsprojectkotlin.model.Article
import com.ozgurberat.newsprojectkotlin.requests.responses.NewsResponse
import com.ozgurberat.newsprojectkotlin.util.Constants.API_KEY
import com.ozgurberat.newsprojectkotlin.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class NewsFragment : Fragment(), NewsRecyclerAdapter.NewsListener {

    private val TAG = "NewsFragment"

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mFirestore: FirebaseFirestore
    private lateinit var mViewModel: NewsViewModel
    private var mArticleList = arrayListOf<Article>()
    private lateinit var mAdapter: NewsRecyclerAdapter

    private var couldntFindMessage: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mAuth = FirebaseAuth.getInstance()
        mFirestore = FirebaseFirestore.getInstance()
        mViewModel = ViewModelProvider(this).get(NewsViewModel::class.java)

        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        couldntFindMessage = view.findViewById<TextView>(R.id.search_couldnt_find_message)
        initRecycler()


        val documentReference: DocumentReference = mFirestore.collection("users").document(mAuth.currentUser!!.uid)
        documentReference.addSnapshotListener(object : EventListener<DocumentSnapshot> {
            override fun onEvent(snapshot: DocumentSnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.d(TAG, "onEvent: ${error.message}")
                    return
                }

                // GET THE TOPICS
                var dataMap: HashMap<String, ArrayList<String>> = hashMapOf()
                dataMap = snapshot?.data as HashMap<String, ArrayList<String>>
                val topicsList = dataMap["topics"]

                //REQUEST ALL THE TOPICS SEQUENTIALLY AND SET THEM ALL TO A LIVEDATA
                val responseList = arrayListOf<NewsResponse>()
                topicsList?.let {
                    CoroutineScope(Dispatchers.Main).launch {
                        showProgressBar(true)
                        withContext(Dispatchers.IO) {
                            val timeout = withTimeoutOrNull(5000L) {
                                for (topic in topicsList) {
                                    responseList.add(mViewModel.getEverything(topic, API_KEY)!!)
                                }
                            }
                            if (timeout == null) {
                                Log.d(TAG, "onEvent: TIMEOUT")
                                showCouldntFindMessage(true)
                            }
                            else {
                                showCouldntFindMessage(false)
                                mViewModel.updateLiveData(responseList)
                            }
                        }
                        showProgressBar(false)
                    }
                } ?: throw Exception("NULL")

            }
        })

        subscribeObservers()
    }

    private fun showCouldntFindMessage(visibility: Boolean) {
        if (visibility) {
            couldntFindMessage?.visibility = View.VISIBLE
            news_fragment_recycler.visibility = View.GONE
        }
        else {
            couldntFindMessage?.visibility = View.GONE
            news_fragment_recycler.visibility = View.VISIBLE

        }
    }

    private fun subscribeObservers() {
        mViewModel.newsResponseList.observe(viewLifecycleOwner) { newsResponseArrayList ->
            for (newsResponse in newsResponseArrayList) {
                if (newsResponse.articleList != null) {
                    for (article in newsResponse.articleList) {
                        println("article : ${article.source}")
                    }
                    mArticleList.addAll(newsResponse.articleList as ArrayList<Article>)
                }
            }
            mArticleList.shuffle()
            mAdapter.updateData(mArticleList)
            mArticleList.clear()
        }

        mViewModel.isError.observe(viewLifecycleOwner) {
            if (it) {
                Log.d(TAG, "subscribeObservers: NewsFragment: ERROR")
            }
        }

    }

    private fun showProgressBar(visibility: Boolean) {
        if (visibility)
            news_progress.visibility = View.VISIBLE
        else
            news_progress.visibility = View.GONE
    }

    private fun initRecycler() {
        mAdapter = NewsRecyclerAdapter(requireContext(), arrayListOf(), this)
        news_fragment_recycler.layoutManager = LinearLayoutManager(requireContext())
        news_fragment_recycler.adapter = mAdapter
    }

    override fun onNewClicked(url: String?) {
        val uri: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
}







