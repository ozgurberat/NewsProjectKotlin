package com.ozgurberat.newsprojectkotlin.adapter

import android.graphics.drawable.Drawable

data class News(
    val image: Drawable,
    val title: String,
    val publicationDate: String
)