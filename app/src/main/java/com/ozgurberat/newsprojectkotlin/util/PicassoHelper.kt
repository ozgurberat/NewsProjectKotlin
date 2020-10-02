package com.ozgurberat.newsprojectkotlin.util

import android.content.Context
import androidx.swiperefreshlayout.widget.CircularProgressDrawable

object PicassoHelper {

    fun getAnimatedCircularProgressDrawable(context: Context): CircularProgressDrawable {
        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 40f
        circularProgressDrawable.start()
        return circularProgressDrawable
    }
}