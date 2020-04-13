package cf.android666.applibrary.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.RequiresApi

/**
 *  Created by jixiaoyong1995@gmail.com
 *  Data: 2020/4/10.
 *  Description: 波浪线的进度条
 *
 *  android PorterDuffXferMode真正的效果测试集合（对比官方 https://www.jianshu.com/p/3feaa8b347f2
 */
class WaveProgressView : View {

    /**
     * 进度
     */
    var mProgress: Float = 0F

    private var waveWidth: Float = 0F
    private var mHorizontalOffsetPercent: Float = 0F
    private val defWidth = 100
    private val defHeight = 100
    private val waveDivWidth = 2F

    private lateinit var wavePaint: Paint
    private lateinit var secondWavePaint: Paint
    private lateinit var circlePaint: Paint
    private lateinit var circleStrokePaint: Paint

    private lateinit var wavePath: Path
    private lateinit var secondWavePath: Path


    /**
     * 波浪水平移动的动画
     */
    private val mWaveHorizontalOffsetVa = ValueAnimator.ofFloat(0f, 1f)

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

        mWaveHorizontalOffsetVa.duration = 1000
        mWaveHorizontalOffsetVa.repeatCount = ValueAnimator.INFINITE
        mWaveHorizontalOffsetVa.interpolator = LinearInterpolator()
        mWaveHorizontalOffsetVa.addUpdateListener { animation -> //获取从0-1的变化值
            mHorizontalOffsetPercent = animation.animatedValue as Float
            invalidate()
        }
        mWaveHorizontalOffsetVa.start()

        initPaint()
    }

    /**
     * 开始模拟进度增加
     */
    fun startFakeProgressGrown() {
        val mFakeProgressValueAnimator = ValueAnimator.ofFloat(0f, 1f)
        mFakeProgressValueAnimator.duration = 50000
        mFakeProgressValueAnimator.repeatCount = ValueAnimator.INFINITE
        mFakeProgressValueAnimator.interpolator = LinearInterpolator()
        mFakeProgressValueAnimator.addUpdateListener { animation -> //获取从0-1的变化值
            mProgress = animation.animatedValue as Float
            invalidate()
        }
        mFakeProgressValueAnimator.start()
    }

    private fun initPaint() {
        circlePaint = Paint()
        circlePaint.color = Color.WHITE
        circlePaint.style = Paint.Style.FILL_AND_STROKE
        circlePaint.strokeWidth = 3F

        wavePaint = Paint()
        wavePaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        wavePaint.style = Paint.Style.FILL_AND_STROKE
        wavePaint.color = Color.parseColor("#DD039BE5")

        secondWavePaint = Paint()
        secondWavePaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        secondWavePaint.style = Paint.Style.FILL_AND_STROKE
        secondWavePaint.color = Color.parseColor("#CB60E0EE")

        circleStrokePaint = Paint()
        circleStrokePaint.color = Color.parseColor("#DD039BE5")
        circleStrokePaint.style = Paint.Style.STROKE
        circleStrokePaint.strokeWidth = 3F
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return
        val sc =
            canvas.saveLayer(0F, 0F, width.toFloat(), height.toFloat(), null, Canvas.ALL_SAVE_FLAG)
        canvas.drawCircle(width / 2F, height / 2F, width / 2F - 5F, circlePaint)

        val offsetX = -waveDivWidth * waveWidth * mHorizontalOffsetPercent
        val secondOffsetX = waveDivWidth * waveWidth * mHorizontalOffsetPercent
        val offsetY = height * (1 - mProgress)

        secondWavePath.offset(secondOffsetX, offsetY)
        canvas.drawPath(secondWavePath, secondWavePaint)
        secondWavePath.offset(-secondOffsetX, -offsetY)

        wavePath.offset(offsetX, offsetY)
        canvas.drawPath(wavePath, wavePaint)
        wavePath.offset(-offsetX, -offsetY)

        canvas.restoreToCount(sc)

        canvas.drawCircle(width / 2F, height / 2F, width / 2F - 5F, circleStrokePaint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Log.d("TAG", "onSizeChanged w:$w,h:$h")
        initWavePath(w.toFloat(), h.toFloat())
    }

    private fun initWavePath(w: Float, h: Float) {
        val waveHeight = -h / 15F
        waveWidth = w / waveDivWidth

        wavePath = Path()
        wavePath.moveTo(0F, 0F)
        for (x in 0..(2 * waveDivWidth - 1).toInt()) {
            wavePath.quadTo(
                waveWidth / 2 + waveWidth * x,
                waveHeight * (if (x % 2 == 0) 1 else -1),
                waveWidth * (1 + x),
                0F
            )
        }
        wavePath.lineTo(2 * waveDivWidth * waveWidth, h)
        wavePath.lineTo(0F, h)
        wavePath.close()

        secondWavePath = Path()
        secondWavePath.moveTo(w, 0F)
        //第二个波浪多画半扇波浪，以便看起来更加真实
        for (x in 0..(2 * waveDivWidth).toInt()) {
            secondWavePath.quadTo(
                w - (waveWidth / 2 + waveWidth * x),
                waveHeight * (if (x % 2 == 0) 1 else -1),
                w - waveWidth * (1 + x),
                0F
            )
        }
        secondWavePath.offset(waveWidth / 3, 0F)
        secondWavePath.lineTo(-w, h)
        secondWavePath.lineTo(w, h)
        secondWavePath.close()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val h = getUsefulSize(heightMeasureSpec, defHeight)
        val w = getUsefulSize(widthMeasureSpec, defWidth)
        val r = Math.min(w, h)
        Log.d("TAG", "onMeasure w:$w,h:$h,R:$r")
        setMeasuredDimension(r, r)
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
