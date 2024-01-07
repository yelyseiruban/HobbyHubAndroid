package com.yelysei.hobbyharbor.ui.fab

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton


fun ExtendedFloatingActionButton.appear(colorStateList: ColorStateList? = null) {
    if (colorStateList != null) {
//        this.supportImageTintList = colorStateList
    }
    FABAnimations.appear(this)

}

fun ExtendedFloatingActionButton.disappear() {
    FABAnimations.disappear(this)
}

fun ExtendedFloatingActionButton.reAppear(colorStateList: ColorStateList, drawable: Drawable? = null, text: String? = null) {
    this.backgroundTintList = colorStateList
//    this.disappear()
    Handler(Looper.getMainLooper()).postDelayed({
//        this.appear()
        if (drawable != null) {
            this.icon = drawable
        }
        if (text != null) {
            this.text = text
            this.extend()
        } else {
            this.text = null
            this.hide()
        }
    }, 300)
}
@SuppressLint("ClickableViewAccessibility")
fun ExtendedFloatingActionButton.setMovableBehavior() {
    this.setOnTouchListener { v, event ->
        DraggableFabTouch.onTouch(v, event)
    }
}