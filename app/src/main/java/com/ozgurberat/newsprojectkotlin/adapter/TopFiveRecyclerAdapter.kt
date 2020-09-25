package com.ozgurberat.newsprojectkotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ozgurberat.newsprojectkotlin.R

class TopFiveRecyclerAdapter(var news: List<News>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
            viewHolder.firstCardImageView!!.setImageDrawable(news[position].image)
            viewHolder.firstCardTitleText!!.text = news[position].title
            viewHolder.firstCardPublicationDateText!!.text = news[position].publicationDate
        }
        else {
            val viewHolder: OthersViewHolder = holder as OthersViewHolder
            viewHolder.othersCardImageView!!.setImageDrawable(news[position].image)
            viewHolder.othersCardTitleText!!.text = news[position].title
            viewHolder.othersCardPublicationDateText!!.text = news[position].publicationDate
        }
    }

    override fun getItemCount(): Int {
        return news.size
    }

    private class FirstViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var firstCardImageView: ImageView? = null
        var firstCardTitleText: TextView? = null
        var firstCardPublicationDateText: TextView? = null

        init {
            firstCardImageView = itemView.findViewById(R.id.card_first_image)
            firstCardTitleText = itemView.findViewById(R.id.card_first_title)
            firstCardPublicationDateText = itemView.findViewById(R.id.card_first_publication_date)
        }

    }

    private class OthersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var othersCardImageView: ImageView? = null
        var othersCardTitleText: TextView? = null
        var othersCardPublicationDateText: TextView? = null

        init {
            othersCardImageView = itemView.findViewById(R.id.card_others_image)
            othersCardTitleText = itemView.findViewById(R.id.card_others_title)
            othersCardPublicationDateText = itemView.findViewById(R.id.card_others_publication_date)
        }

    }

}














