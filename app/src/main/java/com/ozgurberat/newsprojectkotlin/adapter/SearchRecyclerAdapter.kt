package com.ozgurberat.newsprojectkotlin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ozgurberat.newsprojectkotlin.R
import com.ozgurberat.newsprojectkotlin.model.Article
import com.ozgurberat.newsprojectkotlin.util.PicassoHelper
import com.squareup.picasso.Picasso

class SearchRecyclerAdapter(val context: Context, var articles: ArrayList<Article>, val listener: NewsListener) : RecyclerView.Adapter<SearchRecyclerAdapter.SearchCardViewHolder>() {

    interface NewsListener {
        fun onNewClicked(url: String?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchCardViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.search_card, parent, false)
        return SearchCardViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchCardViewHolder, position: Int) {
        holder.searchTitle?.text = articles[position].title
        holder.searchSource?.text = articles[position].source?.name
        holder.searchPublicationDate?.text = articles[position].publishedAt
        Picasso.get()
            .load(articles[position].imageUrl)
            .placeholder(PicassoHelper.getAnimatedCircularProgressDrawable(context))
            .error(R.drawable.android_placeholder)
            .into(holder.searchImageView)

        holder.searchCardView?.setOnClickListener {
            listener.onNewClicked(articles[position].url)
        }
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    fun updateData(articleList: List<Article>) {
        articles.clear()
        articles.addAll(articleList!!)
        notifyDataSetChanged()
    }

    class SearchCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var searchCardView: CardView? = null
        var searchImageView: ImageView? = null
        var searchSource: TextView? = null
        var searchTitle: TextView? = null
        var searchPublicationDate: TextView? = null

        init {
            searchCardView = itemView.findViewById(R.id.search_cardview)
            searchImageView = itemView.findViewById(R.id.search_image_view)
            searchSource = itemView.findViewById(R.id.search_source)
            searchTitle = itemView.findViewById(R.id.search_title)
            searchPublicationDate = itemView.findViewById(R.id.search_publication_date)
        }

    }


}