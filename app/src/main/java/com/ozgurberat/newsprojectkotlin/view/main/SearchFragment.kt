package com.ozgurberat.newsprojectkotlin.view.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.ozgurberat.newsprojectkotlin.R
import com.ozgurberat.newsprojectkotlin.adapter.SearchRecyclerAdapter
import com.ozgurberat.newsprojectkotlin.adapter.TopFiveRecyclerAdapter
import com.ozgurberat.newsprojectkotlin.util.Constants.API_KEY
import com.ozgurberat.newsprojectkotlin.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

class SearchFragment : Fragment(), SearchRecyclerAdapter.NewsListener {

    private val TAG = "SearchFragment"

    private lateinit var mViewModel: SearchViewModel
    private lateinit var mAdapter: SearchRecyclerAdapter
    private lateinit var mAuth: FirebaseAuth

    private var couldntFindMessage: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        mAuth = FirebaseAuth.getInstance()
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()

        couldntFindMessage = view.findViewById(R.id.search_couldnt_find_message)

        fragment_search_button.setOnClickListener {
            val query = fragment_search_edit_text.text.toString()
            fragment_search_edit_text.clearFocus()
            if (query.isNullOrEmpty()) {
                return@setOnClickListener
            }
            CoroutineScope(Dispatchers.Main).launch {
                val job = withTimeoutOrNull(5000L) {
                    mViewModel.getQueriedNews(query, API_KEY)
                }
                if (job == null) {
                    showCouldntFindMessage(true)
                }
                else {
                    showCouldntFindMessage(false)
                }
            }
        }

        subscribeObservers()

    }

    private fun subscribeObservers() {
        mViewModel.newsResponse.observe(viewLifecycleOwner) {
            if (it.articleList!!.isEmpty()) {
                showCouldntFindMessage(true)
            }
            else {
                showCouldntFindMessage(false)
                mAdapter.updateData(it.articleList)
            }

        }

        mViewModel.isLoading.observe(viewLifecycleOwner) {
            showProgressBar(it)
        }

        mViewModel.isError.observe(viewLifecycleOwner) {
            if (it) {
                Log.d(TAG, "subscribeObservers: SearchFragment: ERROR")
            }
        }
    }

    private fun showCouldntFindMessage(visibility: Boolean) {
        if (visibility) {
            couldntFindMessage?.visibility = View.VISIBLE
            search_fragment_recycler.visibility = View.GONE
        }
        else {
            couldntFindMessage?.visibility = View.GONE
            search_fragment_recycler.visibility = View.VISIBLE

        }
    }

    private fun showProgressBar(visibility: Boolean) {
        if (visibility) {
            search_fragment_progress.visibility = View.VISIBLE
        }
        else {
            search_fragment_progress.visibility = View.GONE
        }
    }

    private fun initRecycler() {
        mAdapter = SearchRecyclerAdapter(this.requireContext(), arrayListOf(), this)
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        search_fragment_recycler.layoutManager = layoutManager
        search_fragment_recycler.adapter = mAdapter
    }

    override fun onNewClicked(url: String?) {
        val uri: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
}