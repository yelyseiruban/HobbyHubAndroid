package com.yelysei.hobbyharbor.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

fun getScreenWidthInDp(context: Context): Float {
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)

    // Calculate width in dp
    val screenWidthInDp = displayMetrics.widthPixels / displayMetrics.density

    return screenWidthInDp
}