package com.example.stopwatch

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes

class CustomProgress @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var color = 0
    private val paint = Paint()

    var progress = 0f
        set(value) {
            field = value
            invalidate()
        }

    init {
        if (attrs != null) {
            val styledAttrs = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.CustomProgress,
                defStyleAttr,
                0
            )
            color = styledAttrs.getColor(R.styleable.CustomProgress_color, Color.RED)
            progress = styledAttrs.getFloat(R.styleable.CustomProgress_progress, 0f)
            styledAttrs.recycle()
        }

        paint.color = color
        paint.strokeWidth = height.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawRect(0.toFloat(), 0.toFloat(), progress * width.toFloat(), height.toFloat(), paint)
    }
}