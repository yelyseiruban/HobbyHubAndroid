package com.yelysei.hobbyharbor.ui.fab

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import com.google.android.material.floatingactionbutton.FloatingActionButton

fun FloatingActionButton.appear(colorStateList: ColorStateList? = null) {
    if (colorStateList != null) {
        this.supportImageTintList = colorStateList
    }
    val animationDuration = 300L

    val screenWidth = resources.displayMetrics.widthPixels.toFloat()
    val screenHeight = resources.displayMetrics.heightPixels.toFloat()

    val startX = screenWidth
    val startY = screenHeight
    val endX = 0f
    val endY = 0f

    val appearTranslateAnimation = TranslateAnimation(startX, endX, startY, endY)
    appearTranslateAnimation.duration = animationDuration
    appearTranslateAnimation.fillAfter = true

    val appearScaleAnimation = ScaleAnimation(
        0.8f,
        1f,
        0.8f,
        1f,
        Animation.RELATIVE_TO_SELF,
        0.5f,
        Animation.RELATIVE_TO_SELF,
        0.5f
    )
    appearScaleAnimation.duration = animationDuration

    val appearSet = AnimationSet(true)
    appearSet.addAnimation(appearTranslateAnimation)
    appearSet.addAnimation(appearScaleAnimation)
    this.startAnimation(appearSet)
}

fun FloatingActionButton.disappear() {
    val animationDuration = 300L

    val screenWidth = resources.displayMetrics.widthPixels.toFloat()
    val screenHeight = resources.displayMetrics.heightPixels.toFloat()

    val startXDisappear = 0f
    val startYDisappear = 0f
    val endXDisappear = screenWidth
    val endYDisappear = screenHeight

    val disappearTranslateAnimation =
        TranslateAnimation(startXDisappear, endXDisappear, startYDisappear, endYDisappear)
    disappearTranslateAnimation.duration = animationDuration
    disappearTranslateAnimation.fillAfter = true


    val disappearScaleAnimation = ScaleAnimation(
        1f,
        0.8f,
        1f,
        0.8f,
        Animation.RELATIVE_TO_SELF,
        0.5f,
        Animation.RELATIVE_TO_SELF,
        0.5f
    )
    disappearTranslateAnimation.duration = animationDuration

    val disappearSet = AnimationSet(true)
    disappearSet.addAnimation(disappearTranslateAnimation)
    disappearSet.addAnimation(disappearScaleAnimation)
    this.startAnimation(disappearSet)
}

fun FloatingActionButton.reAppear(drawable: Drawable? = null) {
    this.disappear()
    Handler(Looper.getMainLooper()).postDelayed({
        this.appear()
        if (drawable != null) {
            this.setImageDrawable(drawable)
        }
    }, 300)
}

@SuppressLint("ClickableViewAccessibility")
fun FloatingActionButton.setMovableBehavior() {
    this.setOnTouchListener { v, event ->
        DraggableFabTouch.onTouch(v, event)
    }
}