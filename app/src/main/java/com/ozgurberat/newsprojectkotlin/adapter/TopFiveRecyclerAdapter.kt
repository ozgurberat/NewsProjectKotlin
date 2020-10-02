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

class TopFiveRecyclerAdapter(val context: Context, var articles: ArrayList<Article>, val listener: NewsListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface NewsListener {
        fun onNewClicked(url: String?)
    }

    private val FIRST_VIEWHOLDER = 0
    private val OTHERS_VIEWHOLDER = 1

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) FIRST_VIEWHOLDER else OTHERS_VIEWHOLDER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View
        if (viewType == FIRST_VIEWHOLDER) {
            view = inflater.inflate(R.layout.top_five_card_first, parent, false)
            return FirstViewHolder(view)
        }
        else {
            view = inflater.inflate(R.layout.top_five_card_others, parent, false)
            return OthersViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == 0) {
            val viewHolder: FirstViewHolder = holder as FirstViewHolder
            viewHolder.firstCardTitleText!!.text = articles[position].title
            viewHolder.firstCardPublicationDateText!!.text = articles[position].publishedAt
            Picasso.get()
                .load(articles[position].imageUrl)
                .placeholder(PicassoHelper.getAnimatedCircularProgressDrawable(context))
                .error(R.drawable.android_placeholder)
                .into(viewHolder.firstCardImageView)

            viewHolder.firstCardView?.setOnClickListener {
                listener.onNewClicked(articles[position].url)
            }
        }
        else {
            val viewHolder: OthersViewHolder = holder as OthersViewHolder
            viewHolder.othersCardTitleText!!.text = articles[position].title
            viewHolder.othersCardPublicationDateText!!.text = articles[position].publishedAt
            Picasso.get()
                .load(articles[position].imageUrl)
                .placeholder(PicassoHelper.getAnimatedCircularProgressDrawable(context))
                .error(R.drawable.android_placeholder)
                .into(viewHolder.othersCardImageView)

            viewHolder.othersCardView?.setOnClickListener {
                listener.onNewClicked(articles[position].url)
            }
        }
    }

    fun updateData(newArticleList: List<Article>) {
        articles.clear()
        articles.addAll(newArticleList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    private class FirstViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var firstCardView: CardView? = null
        var firstCardImageView: ImageView? = null
        var firstCardTitleText: TextView? = null
        var firstCardPublicationDateText: TextView? = null

        init {
            firstCardView = itemView.findViewById(R.id.card_first)
            firstCardImageView = itemView.findViewById(R.id.card_first_image)
            firstCardTitleText = itemView.findViewById(R.id.card_first_title)
            firstCardPublicationDateText = itemView.findViewById(R.id.card_first_publication_date)
        }

    }

    class OthersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var othersCardView: CardView? = null
        var othersCardImageView: ImageView? = null
        var othersCardTitleText: TextView? = null
        var othersCardPublicationDateText: TextView? = null

        init {
            othersCardView = itemView.findViewById(R.id.card_others)
            othersCardImageView = itemView.findViewById(R.id.card_others_image)
            othersCardTitleText = itemView.findViewById(R.id.card_others_title)
            othersCardPublicationDateText = itemView.findViewById(R.id.card_others_publication_date)
        }

    }

}














