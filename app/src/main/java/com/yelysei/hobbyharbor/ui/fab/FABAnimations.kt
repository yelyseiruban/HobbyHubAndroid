package com.yelysei.hobbyharbor.ui.fab

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation

object FABAnimations {
    fun appear(fab: View) {
        val animationDuration = 300L

        val screenWidth = fab.resources.displayMetrics.widthPixels.toFloat()
        val screenHeight = fab.resources.displayMetrics.heightPixels.toFloat()

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
        fab.startAnimation(appearSet)
    }

    fun disappear(fab: View) {
        val animationDuration = 300L

        val screenWidth = fab.resources.displayMetrics.widthPixels.toFloat()
        val screenHeight = fab.resources.displayMetrics.heightPixels.toFloat()

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
        fab.startAnimation(disappearSet)
    }
}