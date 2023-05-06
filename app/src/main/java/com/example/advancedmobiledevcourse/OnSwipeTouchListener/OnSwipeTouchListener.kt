package com.example.advancedmobiledevcourse.OnSwipeTouchListener

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import java.lang.Math.abs

//Harkitsin tämän käyttämistä, mutta aika tuli vastaan ja en ehtinyt pohtia järkevää
//tapaa implementoida sitä charttien kanssa.

open class OnSwipeTouchListener(ctx: Context) : View.OnTouchListener {
    val gestureDetector: GestureDetector

    companion object {

        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100
    }

    init {
        gestureDetector = GestureDetector(ctx, GestureListener())
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {


        var isTouch = false

        if (gestureDetector != null && event != null) {

            isTouch = gestureDetector.onTouchEvent(event)
        } else {
            isTouch = true
        }

        return isTouch

    }

    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent): Boolean {
            //return false              //<-- ei koskaan näin
            return true
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            var result = false
            try {


                val diffY = e1?.y?.let { e2?.y?.minus(it) }
                val diffX = e1?.x?.let { e2?.x?.minus(it) }

                if (diffX != null && diffY != null) {

                    if (abs(diffX) > abs(diffY)) {
                        if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight()
                            } else {
                                onSwipeLeft()
                            }
                            result = true
                        }
                    }
                } else {
                    onSwipeRight()
                    result = true
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }

            return result
        }


    }

    open fun onSwipeRight() {}

    open fun onSwipeLeft() {}

    open fun onSwipeUp() {}

    open fun onSwipeDown() {}

}