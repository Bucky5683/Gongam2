package com.cono.gongam.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View

class OverlayView(context: Context) : View(context) {
    private val TAG = "OverlayView"
    private var boundingBoxes: List<Rect> = emptyList()
    private var paint: Paint = Paint()

    init {
        // 기본적인 페인트 설정
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 4f
    }

    fun setBoundingBoxes(boundingBoxes: List<Rect>, paint: Paint) {
        this.boundingBoxes = boundingBoxes
        this.paint = paint
        invalidate() // 바운딩 박스를 다시 그리도록 요청
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (boundingBox in boundingBoxes) {
            canvas.drawRect(boundingBox, paint)
        }
    }
}