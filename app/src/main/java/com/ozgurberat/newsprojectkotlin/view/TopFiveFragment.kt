package com.ozgurberat.newsprojectkotlin.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.ozgurberat.newsprojectkotlin.R
import com.ozgurberat.newsprojectkotlin.adapter.News
import com.ozgurberat.newsprojectkotlin.adapter.TopFiveRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_top_five.*

class TopFiveFragment : Fragment() {

    private val TAG = "TopFiveFragment"
    private lateinit var adapter: TopFiveRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_top_five, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inflateToolbarMenu()
        initRecycler()

    }

    private fun initRecycler() {
        adapter = TopFiveRecyclerAdapter(arrayListOf())

        val new_1 = News(ContextCompat.getDrawable(requireActivity().applicationContext, R.drawable.android_placeholder)!!, "Title #1", "25-09-2020")
        val new_2 = News(ContextCompat.getDrawable(requireActivity().applicationContext, R.drawable.ic_launcher_background)!!, "Title #2", "24-04-2020")
        val new_3 = News(ContextCompat.getDrawable(requireActivity().applicationContext, R.drawable.android_placeholder)!!, "Title #3", "12-08-2020")
        val new_4 = News(ContextCompat.getDrawable(requireActivity().applicationContext, R.drawable.ic_launcher_foreground)!!, "Title #4", "02-01-2020")
        val new_5 = News(ContextCompat.getDrawable(requireActivity().applicationContext, R.drawable.arrow_drop_down_icon)!!, "Title #5", "08-11-2020")

        val newsList = arrayListOf<News>(new_1, new_2, new_3, new_4, new_5)
        adapter.news = newsList

        fragment_top_five_recycler.layoutManager = LinearLayoutManager(requireContext())
        fragment_top_five_recycler.adapter = adapter

    }
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
                    return@setOnMenuItemClickListener true
                }
                R.id.action_tr -> {
                    Log.d(TAG, "inflateToolbarMenu: Clicked: TR")
                    return@setOnMenuItemClickListener true
                }
                else -> {
                    Log.d(TAG, "inflateToolbarMenu: Unknown menu item error")
                    return@setOnMenuItemClickListener false
                }
            }
        }
    }
}