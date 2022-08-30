package cf.android666.applibrary.view

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.View

/**
 *  Created by jixiaoyong1995@gmail.com
 *  Data: 2019/6/10.
 *  Description:
 */
class CircleIndicator : View {

    private var mPaint: Paint = Paint()
    private var mWidth: Int = 20
    private var mHeight: Int = 20

    var defaultNum = 8
    var defaultRadius = 20.0F
    var defaultAnimSpeed = 160L
    var defaultColor = Color.GRAY
    private var defaultScale = 1.0F
    private var defaultAlpha = 255

    var isRunning = true
        private set

    private var scales = arrayListBy(defaultScale, defaultNum)
    private var alphas = arrayListBy(defaultAlpha, defaultNum)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes)

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr)

    private val handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg?.what) {
                TRY_ANIM -> checkAnim()
            }
        }
    }

    init {
        handler.sendEmptyMessageDelayed(TRY_ANIM, defaultAnimSpeed)
    }

    fun checkAnim() {
        if (!isRunning) {
            return
        }
        revertNum(scales)
        revertNum(alphas)
        invalidate()
        handler.sendEmptyMessageDelayed(TRY_ANIM, defaultAnimSpeed)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val cX = mWidth.toFloat() / 2
        val cY = mHeight.toFloat() / 2

        canvas?.save()
        for (x in 0 until defaultNum) {
            mPaint.color = defaultColor
            mPaint.alpha = alphas[x]
            mPaint.isAntiAlias = true
            canvas?.drawCircle(cX, defaultRadius * scales[x], defaultRadius * scales[x], mPaint)
            mPaint.color = Color.RED
            canvas?.rotate(360F / defaultNum, cX, cY)
        }
        canvas?.restore()
    }

    private fun arrayListBy(defVar: Int, size: Int): ArrayList<Int> {
        val arr = arrayListOf<Int>()
        for (x in 0 until size) {
            arr.add(((x + 1).toFloat() / size.toFloat()).times(defVar.toFloat()).toInt())
        }
        return arr
    }

    private fun arrayListBy(defVar: Float, size: Int): ArrayList<Float> {
        val arr = arrayListOf<Float>()
        for (x in 0 until size) {
            arr.add(((x + 1).toFloat() / size.toFloat()).times(defVar))
        }
        return arr
    }

    private fun <T> revertNum(array: ArrayList<T>) {
        val first = array[array.size - 1]
        for (x in array.size - 1 downTo 1) {
            array[x] = array[x - 1]
        }
        array[0] = first
    }

    fun start() {
        isRunning = true
    }

    fun stop() {
        isRunning = false
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        handler.sendEmptyMessage(TRY_ANIM)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        handler.removeCallbacksAndMessages(null)
    }

    companion object {
        private const val TRY_ANIM = 1
    }
}