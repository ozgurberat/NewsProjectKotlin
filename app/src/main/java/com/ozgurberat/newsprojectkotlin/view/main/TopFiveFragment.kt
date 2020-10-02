package com.ozgurberat.newsprojectkotlin.view.main

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ozgurberat.newsprojectkotlin.R
import com.ozgurberat.newsprojectkotlin.adapter.TopFiveRecyclerAdapter
import com.ozgurberat.newsprojectkotlin.model.Article
import com.ozgurberat.newsprojectkotlin.util.Constants.API_KEY
import com.ozgurberat.newsprojectkotlin.util.Constants.COUNTRY_CN
import com.ozgurberat.newsprojectkotlin.util.Constants.COUNTRY_DE
import com.ozgurberat.newsprojectkotlin.util.Constants.COUNTRY_FR
import com.ozgurberat.newsprojectkotlin.util.Constants.COUNTRY_IL
import com.ozgurberat.newsprojectkotlin.util.Constants.COUNTRY_IT
import com.ozgurberat.newsprojectkotlin.util.Constants.COUNTRY_RU
import com.ozgurberat.newsprojectkotlin.util.Constants.COUNTRY_TR
import com.ozgurberat.newsprojectkotlin.util.Constants.COUNTRY_US
import com.ozgurberat.newsprojectkotlin.viewmodel.TopFiveViewModel
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_top_five.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.util.*

class TopFiveFragment : Fragment(), TopFiveRecyclerAdapter.NewsListener {

    private val TAG = "TopFiveFragment"
    private lateinit var mAdapter: TopFiveRecyclerAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mFirestore: FirebaseFirestore
    private lateinit var mViewModel: TopFiveViewModel

    private var couldntFindMessage: TextView? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mAuth = FirebaseAuth.getInstance()
        mFirestore = FirebaseFirestore.getInstance()
        mViewModel = ViewModelProvider(this).get(TopFiveViewModel::class.java)

        if (mAuth.currentUser != null) {
            val sharedPreferences = activity?.getSharedPreferences(
                mAuth.currentUser!!.uid,
                Context.MODE_PRIVATE
            )
            sharedPreferences!!.edit().putBoolean("login", true).apply()
        }

        return inflater.inflate(R.layout.fragment_top_five, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inflateToolbarMenu()
        initRecycler()
        showWelcomeText()
        couldntFindMessage = view.findViewById(R.id.top_five_couldnt_find)

        CoroutineScope(Dispatchers.Main).launch {
            val job = withTimeoutOrNull(5000L) {
                mViewModel.getTopHeadlines(COUNTRY_US, API_KEY)
            }
            if (job == null) {
                showCouldntFindMessage(true)
                println("yessss")
            }
            else {
                showCouldntFindMessage(false)
            }
        }
        subscribeObservers()


    }

    private fun showCouldntFindMessage(visibility: Boolean) {
        if (visibility) {
            couldntFindMessage?.visibility = View.VISIBLE
            fragment_top_five_recycler.visibility = View.GONE
        }
        else {
            couldntFindMessage?.visibility = View.GONE
            fragment_top_five_recycler.visibility = View.VISIBLE
        }
    }

    private fun showWelcomeText() {

        val sharedPreferences = activity?.getSharedPreferences(
            mAuth.currentUser!!.uid,
            Context.MODE_PRIVATE
        )
        val name = sharedPreferences!!.getString("name", "")
        val surname = sharedPreferences.getString("surname", "")

        val fullname = "${name!!.capitalize(Locale.getDefault())} ${surname!!.capitalize(Locale.getDefault())}"
        toolbar_username.text = fullname

    }

    private fun subscribeObservers() {
        mViewModel.newsResponse.observe(viewLifecycleOwner) {
            val articles = it.articleList
            if (articles != null) {
                val topFiveArticles = arrayListOf<Article>()
                for (i in 0 until 5) {
                    topFiveArticles.add(articles[i])
                }
                mAdapter.updateData(topFiveArticles)
            }
            else
                Log.d(TAG, "subscribeObservers: TopFiveFragment: null")
        }

        mViewModel.isLoading.observe(viewLifecycleOwner) {
            showProgressBar(it)
        }

        mViewModel.isError.observe(viewLifecycleOwner) {
            if (it)
                Log.d(TAG, "subscribeObservers: TopFiveFragment: ERROR")
        }
    }

    private fun showProgressBar(visibility: Boolean) {
        if (visibility)
            top_five_progress.visibility = View.VISIBLE
        else
            top_five_progress.visibility = View.GONE
    }
    private fun initRecycler() {
        mAdapter = TopFiveRecyclerAdapter(requireContext(), arrayListOf(), this)
        fragment_top_five_recycler.layoutManager = LinearLayoutManager(requireContext())
        fragment_top_five_recycler.adapter = mAdapter
    }

    @SuppressLint("SetTextI18n")
    private fun inflateToolbarMenu() {
        fragment_top_five_toolbar.inflateMenu(R.menu.fragment_top_five_toolbar_menu)
        fragment_top_five_toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_dropdown_menu_icon -> {
                    Log.d(TAG, "inflateToolbarMenu: Clicked: ICON")
                    return@setOnMenuItemClickListener true
                }
                R.id.action_us -> {
                    Log.d(TAG, "inflateToolbarMenu: Clicked: US")
                    country_text_view.text = "US"
                    mViewModel.getTopHeadlines(COUNTRY_US, API_KEY)
                    return@setOnMenuItemClickListener true
                }
                R.id.action_tr -> {
                    Log.d(TAG, "inflateToolbarMenu: Clicked: TR")
                    country_text_view.text = "TR"
                    mViewModel.getTopHeadlines(COUNTRY_TR, API_KEY)
                    return@setOnMenuItemClickListener true
                }
                R.id.action_ru -> {
                    Log.d(TAG, "inflateToolbarMenu: Clicked: TR")
                    country_text_view.text = "RU"
                    mViewModel.getTopHeadlines(COUNTRY_RU, API_KEY)
                    return@setOnMenuItemClickListener true
                }
                R.id.action_de -> {
                    Log.d(TAG, "inflateToolbarMenu: Clicked: TR")
                    country_text_view.text = "DE"
                    mViewModel.getTopHeadlines(COUNTRY_DE, API_KEY)
                    return@setOnMenuItemClickListener true
                }
                R.id.action_fr -> {
                    Log.d(TAG, "inflateToolbarMenu: Clicked: TR")
                    country_text_view.text = "FR"
                    mViewModel.getTopHeadlines(COUNTRY_FR, API_KEY)
                    return@setOnMenuItemClickListener true
                }
                R.id.action_cn -> {
                    Log.d(TAG, "inflateToolbarMenu: Clicked: TR")
                    country_text_view.text = "CN"
                    mViewModel.getTopHeadlines(COUNTRY_CN, API_KEY)
                    return@setOnMenuItemClickListener true
                }
                R.id.action_il -> {
                    Log.d(TAG, "inflateToolbarMenu: Clicked: TR")
                    country_text_view.text = "IL"
                    mViewModel.getTopHeadlines(COUNTRY_IL, API_KEY)
                    return@setOnMenuItemClickListener true
                }
                R.id.action_it -> {
                    Log.d(TAG, "inflateToolbarMenu: Clicked: TR")
                    country_text_view.text = "IT"
                    mViewModel.getTopHeadlines(COUNTRY_IT, API_KEY)
                    return@setOnMenuItemClickListener true
                }
                else -> {
                    Log.d(TAG, "inflateToolbarMenu: Unknown menu item error")
                    return@setOnMenuItemClickListener false
                }
            }
        }
    }

    override fun onNewClicked(url: String?) {
        val uri: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
}















