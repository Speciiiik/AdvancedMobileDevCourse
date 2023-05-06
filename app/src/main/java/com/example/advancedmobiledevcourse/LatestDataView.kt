package com.example.advancedmobiledevcourse

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView

class LatestDataView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    val maxRows = 5
    // rest of the basic methods here from the template above
    init {

        this.orientation = VERTICAL

        var someTextView : TextView = TextView(context)

        someTextView.measure(0,0)

        var rowHeight = someTextView.measuredHeight

        this.measure(0,0)

        var additionalHeight = this.measuredHeight

        this.minimumHeight = rowHeight * maxRows + additionalHeight
    }


    fun addData(message: String) {


        while(this.childCount >= maxRows) {
            this.removeViewAt(0)
        }


        var newTextView : TextView = TextView(context) as TextView
        newTextView.setText(message)
        newTextView.setBackgroundColor(Color.WHITE)
        newTextView.setTextColor(Color.BLACK)
        this.addView(newTextView)
    }
}