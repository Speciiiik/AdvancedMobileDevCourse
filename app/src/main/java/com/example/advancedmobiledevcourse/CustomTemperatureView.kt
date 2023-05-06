package com.example.advancedmobiledevcourse

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View

class CustomTemperatureView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var temperature : Int = 0

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    // your helper variables etc. can be here
    init
    {
        paint.color = Color.BLUE
        textPaint.color = Color.WHITE

        textPaint.textSize = 90f
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD))
        textPaint.textAlign = Paint.Align.CENTER
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(width.toFloat() / 2, width.toFloat() / 2, width.toFloat() / 2, paint)

        // parameters: content, x, y, color
        canvas.drawText("${temperature}℃", width.toFloat() / 2, width.toFloat() / 2 + 28, textPaint);
    }
    val size = 200

    override fun onMeasure(widthMeasureSpec : Int, heightMeasureSpec : Int){
        // Try for a width based on our minimum
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        var w: Int = View.resolveSizeAndState(minw, widthMeasureSpec, 1)

        // if no exact size given (either dp or match_parent)
        // use this one instead as default (wrap_content)
        if (w == 0)
        {
            w = size * 2
        }

        // Whatever the width ends up being, ask for a height that would let the view
        // get as big as it can
        // val minh: Int = View.MeasureSpec.getSize(w) + paddingBottom + paddingTop
        // in this case, we use the height the same as our width, since it's a circle
        val h: Int = View.resolveSizeAndState(
            View.MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )

        setMeasuredDimension(w, h)
    }

    fun changeTemperature(t : Int) {
        temperature = t

        if(temperature > 0) {
            paint.color = Color.RED
        }
        else {
            paint.color = Color.BLUE
        }

        invalidate()
        requestLayout()
    }
}