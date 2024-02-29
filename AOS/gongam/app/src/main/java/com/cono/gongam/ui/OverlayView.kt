package com.cono.gongam.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

class OverlayView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val TAG = "OverlayView"
    private val rectPaint = Paint().apply {
        color = Color.GREEN
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

    private val rects = mutableListOf<Rect>()

    fun addRect(rect: Rect) {
        rects.add(rect)
        invalidate()
    }

    fun clear() {
        rects.clear()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (rect in rects) {
            canvas.drawRect(rect, rectPaint)
        }
    }
}