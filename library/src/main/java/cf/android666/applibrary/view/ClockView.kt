package cf.android666.applibrary.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat

/**
 *  Created by jixiaoyong1995@gmail.com
 *  Data: 2020/4/3.
 *  Description:
 */
class ClockView : View {

    private val defWidth = 100
    private val defHeight = 100
    private lateinit var paint: Paint
    private var timeHour = 4
    private var timeMinute = 3
    private var timeSecond = 30

    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
    ) : super(context, attrs, defStyleAttr) {
        initView()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView()
    }

    private fun initView() {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.BLACK
        paint.strokeWidth = 10F
        paint.strokeCap = Paint.Cap.ROUND

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val h = getUsefulSize(heightMeasureSpec, defHeight)
        val w = getUsefulSize(widthMeasureSpec, defWidth)
        val r = Math.min(w, h)

        setMeasuredDimension(r, r)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas == null) {
            return
        }

        val w = measuredWidth - paddingStart - paddingEnd
        val h = measuredHeight - paddingTop - paddingBottom
        val diameter = Math.min(w, h)

        val centerX = (measuredWidth + paddingStart - paddingEnd) / 2F
        val centerY = (measuredHeight + paddingTop - paddingBottom) / 2F
        val r = diameter / 2F

        canvas.translate(centerX, centerY)
        pivotY = centerY
        pivotX = centerX

        paint.strokeWidth = diameter / 180F
        paint.style = Paint.Style.STROKE

        val clockR = r * 0.9F
        paint.setShadowLayer(10f, 50f, 50f, Color.GREEN)
        canvas.drawCircle(0F, 0F, clockR, paint)

        val nodeCenterX = r * 0.6F
        val nodeR = r * 0.05F

        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        textPaint.color = Color.BLACK
        textPaint.textSize = 32F

        for (x in 0..360 step 30) {

            if (x % 90 == 0) {
                //number
                val text = when (x) {
                    0 -> "12"
                    90 -> "3"
                    180 -> "9"
                    270 -> "9"
                    else -> ""
                }
                val x = -textPaint.measureText(text) / 2 //the width of the text.
                val fontMetrics = textPaint.fontMetrics
                val y =
                    (fontMetrics.descent - fontMetrics.ascent) / 2F - fontMetrics.descent - clockR * 0.8F
                canvas.drawText(text, x, y, textPaint)
            } else {
                //node
                paint.strokeWidth = 1F
                paint.style = Paint.Style.FILL
                canvas.drawCircle(nodeCenterX, 0F, nodeR, paint)
            }

            canvas.rotate(30F)
        }
        //将表盘归位
        canvas.rotate(-30F)

        //hour
        val hourRotate =
            (timeHour * 60 * 60 + timeMinute * 60 + timeSecond).toFloat() / (12 * 60 * 60F) * 360F
        paint.strokeWidth = 5F
        canvas.rotate(hourRotate)
        canvas.drawLine(0F, 0F, 0F, -clockR * 0.3F, paint)
        canvas.rotate(-hourRotate)

        //minute
        val minuteRotate = (timeMinute * 60 + timeSecond).toFloat() / (60 * 60F) * 360
        paint.strokeWidth = 3F
        canvas.rotate(minuteRotate)
        canvas.drawLine(0F, 0F, 0F, -clockR * 0.5F, paint)
        canvas.rotate(-minuteRotate)

        //second
        val secondRotate = timeSecond / 60F * 360
        paint.strokeWidth = 1F
        canvas.rotate(secondRotate)
        canvas.drawLine(0F, 0F, 0F, -clockR * 0.7F, paint)
        canvas.rotate(-secondRotate)

        canvas.save()
    }


    /**
     * 根据Mode获取合适的size
     */
    private fun getUsefulSize(sizeMeasureSpec: Int, defSize: Int): Int {
        val mode = MeasureSpec.getMode(sizeMeasureSpec)
        val size = MeasureSpec.getSize(sizeMeasureSpec)

        return when (mode) {
            MeasureSpec.EXACTLY, MeasureSpec.AT_MOST -> size
            MeasureSpec.UNSPECIFIED -> defSize
            else -> defSize
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        handler.removeCallbacks(timeUpdateRunnable)
        handler.postDelayed(timeUpdateRunnable, 0)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        handler.removeCallbacks(timeUpdateRunnable)
    }

    private val timeUpdateRunnable = Runnable {
        updateClockTime()
    }

    private fun updateClockTime() {
        val currentTime = System.currentTimeMillis()
        val c = SimpleDateFormat("hh:mm:ss").format(currentTime).split(":").map {
            it.toIntOrNull() ?: 0
        }

        timeHour = c.get(0)
        timeMinute = c.get(1)
        timeSecond = c.get(2)
        invalidate()
        handler.postDelayed(timeUpdateRunnable, getNextSecondDelay())
    }

    private fun getNextSecondDelay(): Long {
        val currentTime = System.currentTimeMillis()
        val c = SimpleDateFormat("hh:mm:ss").format(currentTime).split(":").map {
            it.toIntOrNull() ?: 0
        }
        val next = c[0] * 60 * 60 * 1000 + c[1] * 60 * 1000 + (c[2] + 1) * 1000L
        return next - System.currentTimeMillis()
    }

}