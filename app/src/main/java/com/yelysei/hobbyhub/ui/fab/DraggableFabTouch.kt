package com.yelysei.hobbyhub.ui.fab

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton

object DraggableFabTouch : View.OnTouchListener {

    private const val CLICK_DRAG_TOLERANCE = 10f
    private const val TOUCH_SCALE_FACTOR = 0.9f
    private const val MOVE_UP_SCALE_FACTOR = 1f

    private var downRawX: Float = 0f
    private var downRawY: Float = 0f
    private var dX: Float = 0f
    private var dY: Float = 0f

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                downRawX = motionEvent.rawX
                downRawY = motionEvent.rawY
                dX = view.x - downRawX
                dY = view.y - downRawY

                view.animate().scaleX(TOUCH_SCALE_FACTOR).scaleY(TOUCH_SCALE_FACTOR)
                    .setDuration(100).start()

                return true // Consumed
            }

            MotionEvent.ACTION_MOVE -> {
                val viewWidth = view.width
                val viewHeight = view.height
                val viewParent = view.parent as View
                val parentWidth = viewParent.width
                val parentHeight = viewParent.height
                var newX = motionEvent.rawX + dX
                newX = newX.coerceAtLeast(layoutParams.leftMargin.toFloat())
                newX =
                    newX.coerceAtMost(parentWidth - viewWidth - layoutParams.rightMargin.toFloat())
                var newY = motionEvent.rawY + dY
                newY = newY.coerceAtLeast(layoutParams.topMargin.toFloat())
                newY =
                    newY.coerceAtMost(parentHeight - viewHeight - layoutParams.bottomMargin.toFloat())
                view.animate()
                    .x(newX)
                    .y(newY)
                    .setDuration(0)
                    .start()
                return true // Consumed
            }

            MotionEvent.ACTION_UP -> {
                val upRawX = motionEvent.rawX
                val upRawY = motionEvent.rawY
                val upDX = upRawX - downRawX
                val upDY = upRawY - downRawY
                view.animate().scaleX(MOVE_UP_SCALE_FACTOR).scaleY(MOVE_UP_SCALE_FACTOR)
                    .setDuration(100).start()

                if (Math.abs(upDX) >= CLICK_DRAG_TOLERANCE || Math.abs(upDY) >= CLICK_DRAG_TOLERANCE) {
                    // A drag
                    return true // Consumed
                } else {
                    // A click
                    view.performClick()
                    return true // Consumed
                }
            }

            else -> return view.onTouchEvent(motionEvent)
        }
    }
}

@SuppressLint("ClickableViewAccessibility")
fun FloatingActionButton.setMovableBehavior() {
    this.setOnTouchListener { v, event ->
        DraggableFabTouch.onTouch(v, event)
    }
}