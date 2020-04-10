package cf.android666.applibrary.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi

/**
 *  Created by jixiaoyong1995@gmail.com
 *  Data: 2020/4/2.
 *  Description:
 */
class ProgressView : View {

    private val defWidth = 100
    private val defHeight = 100
    private var isPlayAnimation = true
    private lateinit var paint: Paint

    private val rotateRunnable = Runnable {
        if (isPlayAnimation) {
            rotateView()
        }
    }

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
        post {
            handler.postDelayed(rotateRunnable, 100)
        }
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
        val r = diameter / 4F

        canvas.translate(centerX, centerY)
        pivotY = centerY
        pivotX = centerX

        paint.strokeWidth = ((Math.PI * diameter) / 48F).toFloat()

        for (x in 0..360 step 30) {
            paint.alpha = ((x / 360F) * 200F + 35).toInt()
            canvas.drawLine(r, 0F, 1.8F * r, 0F, paint)
            canvas.rotate(30F)
        }

        canvas.save()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        isPlayAnimation = true
        handler.removeCallbacks(rotateRunnable)
        handler.postDelayed(rotateRunnable, 100)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        isPlayAnimation = false
        handler.removeCallbacks(rotateRunnable)
    }

    fun rotateView() {
        rotation += 30F
        handler.postDelayed(rotateRunnable, 100)
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
}