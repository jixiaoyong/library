package cf.android666.applibrary.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.RequiresApi
import kotlin.random.Random


/**
 *  Created by jixiaoyong1995@gmail.com
 *  Data: 2020/4/9.
 *  Description:
 *  计算三阶贝塞尔曲线控制点：https://wenku.baidu.com/view/c790f8d46bec0975f565e211.html
 *  paint绘制：https://blog.csdn.net/itermeng/article/details/77968042
 *  贝塞尔曲线在线练习：
 *  https://bezier.method.ac/
 *  https://aaaaaaaty.github.io/bezierMaker.js/playground/playground.html
 *
 *Android - 绘制带有渐变色的折线图 https://www.jianshu.com/p/58c839d867e1
 *  path 动画参考：https://www.jianshu.com/p/4b440fca400a
 *  https://juejin.im/post/5d465c5cf265da03986bd5ce （比较符合目标）
 *  https://blog.csdn.net/ITermeng/article/details/80264577
 *  https://blog.csdn.net/ITermeng/article/details/80291361?depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-1&utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-1
 */
class BezierViewAnim : View {

    private val defWidth = 100
    private val defHeight = 100

    private val pointList = arrayListOf<PointF>()
    private var mTouchPointIndex = -1

    private var bezierPath = Path()
    private var shadowPath = Path()

    private var bezierPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var coordinatePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var pointPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var progress = 0F
    private var totalPathLength = 0F

    private var mValueAnimator = ValueAnimator.ofFloat(0f, 1f)


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
        val random = Random(System.currentTimeMillis())
        for (x in 0 until 12) {
            val yPoint = random.nextFloat() * 500
            pointList.add(PointF(x.toFloat() * 50, yPoint))
        }

        initBezierPath()
        initPaint()

        mValueAnimator.duration = 10000
        mValueAnimator.repeatCount = ValueAnimator.INFINITE
        mValueAnimator.interpolator = AccelerateDecelerateInterpolator()
        mValueAnimator.addUpdateListener { animation -> //获取从0-1的变化值
            progress = animation.animatedValue as Float
            //不断刷新绘图，实现路径绘制
            invalidate()
        }
        mValueAnimator.start()
    }

    private fun initPaint() {
        //坐标画笔
        coordinatePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        coordinatePaint.style = Paint.Style.STROKE
        coordinatePaint.color = Color.GRAY
        coordinatePaint.strokeCap = Paint.Cap.SQUARE
        coordinatePaint.strokeWidth = 1F

        //阴影画笔
        shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        shadowPaint.style = Paint.Style.FILL
        shadowPaint.strokeWidth = 1F
        shadowPaint.color = Color.GRAY
        val shader =
            LinearGradient(0F, -500F, 0F, 0F, Color.GREEN, Color.TRANSPARENT, Shader.TileMode.CLAMP)
        shadowPaint.shader = shader

        //bezierPaint
        bezierPaint.style = Paint.Style.STROKE
        bezierPaint.color = Color.RED
        bezierPaint.strokeWidth = 3F

        //point
        pointPaint.style = Paint.Style.FILL
        pointPaint.color = Color.RED
        pointPaint.strokeWidth = 2F
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val h = getUsefulSize(heightMeasureSpec, defHeight)
        val w = getUsefulSize(widthMeasureSpec, defWidth)

        setMeasuredDimension(h, w)
    }

    val dstPath = Path()
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) {
            return
        }

        //画面稍微向右移动，留出一些间隙
        canvas.translate(10F, 10F)

        drawCoordinateLines(canvas)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1F
        paint.color = Color.GRAY
        canvas.drawPath(bezierPath, paint)

        // 按照进度绘制贝塞尔曲线
        val stopD = progress * totalPathLength
        mPathMeasure.getSegment(0F, stopD, dstPath, true)
        if (progress <= 0.001F) {
            dstPath.reset()
        }
        //bezier anim
        canvas.drawPath(dstPath, bezierPaint)
        // shadow
        canvas.drawPath(shadowPath, shadowPaint)

        //key point
        val maxIndex = pointList.size * progress
        pointList.forEachIndexed { index, point ->
            if (index <= maxIndex) {
                canvas.drawCircle(point.x, -point.y, 5F, pointPaint)
            }
        }
    }

    /**
     * 绘制坐标线
     */
    private fun drawCoordinateLines(canvas: Canvas) {
        canvas.translate(0F, 600F)
        canvas.drawLine(0F, 0F, 600F, 0F, coordinatePaint)// x
        canvas.drawLine(0F, 0F, 0F, -600F, coordinatePaint)// y
    }

    private var mPathMeasure: PathMeasure = PathMeasure()

    fun initBezierPath() {
        bezierPath = Path()
        bezierPath.moveTo(pointList.first().x, -pointList.first().y)
        pointList.forEachIndexed { index, startPoint ->
            when (index) {
                pointList.lastIndex -> {

                }
                else -> {
                    val endPoint = pointList[index + 1]
                    bezierPath.cubicTo(
                        (startPoint.x + endPoint.x) / 2,
                        -startPoint.y,
                        (startPoint.x + endPoint.x) / 2,
                        -endPoint.y,
                        endPoint.x,
                        -endPoint.y
                    )
                }
            }

        }

        val lastPoint = pointList.last()
        shadowPath = Path(bezierPath)
        shadowPath.lineTo(lastPoint.x, 0F)
        shadowPath.lineTo(0F, 0F)
        shadowPath.close()

        mPathMeasure.setPath(bezierPath, false)
        totalPathLength = mPathMeasure.length
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {

        mTouchPointIndex = when (event?.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                findClosetPointIndex(pointList, PointF(event.x, event.y))
            }
            else -> -1
        }
        Log.d(
            "TAG",
            "touch ${event?.action} :(${event?.x},${event?.y}) mTouchPoint:$mTouchPointIndex"
        )

        invalidate()
        return true
    }


    /**
     * 这种方法绘制的贝塞尔曲线，每个线的转折点不一定在给出的点上
     */
    private fun drawBezierLines(canvas: Canvas, paint: Paint) {
        pointList.forEachIndexed { index, pointF ->
            canvas.drawCircle(pointF.x, pointF.y, 3F, paint)
            when (index) {
                0 -> {
                    drawLine(
                        canvas,
                        paint,
                        pointF,
                        pointF,
                        pointList[index + 1],
                        pointList[index + 2]
                    )
                }
                pointList.lastIndex - 1 -> {
                    drawLine(
                        canvas,
                        paint,
                        pointList[index - 1],
                        pointF,
                        pointList[index + 1],
                        pointList[index + 1]
                    )
                }
                pointList.lastIndex -> {

                }
                else ->
                    drawLine(
                        canvas,
                        paint,
                        pointList[index - 1],
                        pointF,
                        pointList[index + 1],
                        pointList[index + 2]
                    )
            }
        }
    }

    // 计算三阶贝塞尔曲线控制点方法：https://wenku.baidu.com/view/c790f8d46bec0975f565e211.html
    // 这个方法计算的控制点产生的贝塞尔曲线的转折点不一定在每个点上面
    fun drawLine(
        canvas: Canvas,
        paint: Paint,
        beforePointF: PointF,
        startPoint: PointF,
        endPoint: PointF,
        afterPoint: PointF,
        a: Float = 1 / 6F
    ) {
        val b = a

        val controlPoint1X = startPoint.x + (endPoint.x - beforePointF.x) * a
        val controlPoint1Y = startPoint.y + (endPoint.y - beforePointF.y) * a

        val controlPoint2X = endPoint.x - (afterPoint.x - startPoint.x) * b
        val controlPoint2Y = endPoint.y - (afterPoint.y - startPoint.y) * b

        val path = Path()
        path.moveTo(startPoint.x, startPoint.y)
        path.cubicTo(
            controlPoint1X,
            controlPoint1Y,
            controlPoint2X,
            controlPoint2Y,
            endPoint.x,
            endPoint.y
        )

        canvas.drawPath(path, paint)
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

    private fun findClosetPointIndex(points: ArrayList<PointF>, target: PointF): Int {
        var minIndex = 0
        var offset = Math.abs(points[0].x - target.x)

        points.forEachIndexed { index, point ->
            val newOffset = Math.abs(point.x - target.x)
            if (newOffset < offset) {
                offset = newOffset
                minIndex = index
            }
        }

        return minIndex
    }

}