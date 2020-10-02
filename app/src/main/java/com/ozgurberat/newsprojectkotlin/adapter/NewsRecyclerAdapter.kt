package com.ozgurberat.newsprojectkotlin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ozgurberat.newsprojectkotlin.R
import com.ozgurberat.newsprojectkotlin.model.Article
import com.ozgurberat.newsprojectkotlin.util.PicassoHelper
import com.squareup.picasso.Picasso

class NewsRecyclerAdapter(private val context: Context, private var articleList: ArrayList<Article>, val listener: NewsListener) : RecyclerView.Adapter<TopFiveRecyclerAdapter.OthersViewHolder>() {

    interface NewsListener {
        fun onNewClicked(url: String?)
    }
    
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TopFiveRecyclerAdapter.OthersViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.top_five_card_others, parent, false)
        return TopFiveRecyclerAdapter.OthersViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopFiveRecyclerAdapter.OthersViewHolder, position: Int) {
        holder.othersCardTitleText?.text = articleList[position].title
        holder.othersCardPublicationDateText?.text = articleList[position].publishedAt
        Picasso.get()
            .load(articleList[position].imageUrl)
            .placeholder(PicassoHelper.getAnimatedCircularProgressDrawable(context))
            .error(R.drawable.android_placeholder)
            .into(holder.othersCardImageView)
        holder.othersCardView?.setOnClickListener {
            listener.onNewClicked(articleList[position].url)
        }
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    fun updateData(shuffledArticleList: ArrayList<Article>) {
        articleList.clear()
        articleList.addAll(shuffledArticleList)
        notifyDataSetChanged()
    }

}