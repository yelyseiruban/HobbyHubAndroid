package com.yelysei.hobbyharbor.ui.fab

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import com.google.android.material.floatingactionbutton.FloatingActionButton

fun FloatingActionButton.appear(colorStateList: ColorStateList? = null) {
    if (colorStateList != null) {
        this.supportImageTintList = colorStateList
    }
    FABAnimations.appear(this)

}

fun FloatingActionButton.disappear() {
    FABAnimations.disappear(this)
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