package cf.android666.applibrary.view

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import cf.android666.applibrary.R

/**
 * author: jixiaoyong
 * email: jixiaoyong1995@gmail.com
 * website: https://jixiaoyong.github.io
 * date: 2020/4/15
 * description:
 * ![](https://jixiaoyong.github.io/images/20200415211144.png)
 */
class XfermodeView : View {

    private lateinit var src: Bitmap
    private lateinit var dst: Bitmap
    private lateinit var srcPaint: Paint
    private lateinit var dstPaint: Paint
    private val defWidth = 100
    private val defHeight = 100

    private var defXfermode: PorterDuff.Mode? = null

    @JvmOverloads
    constructor(
            context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0
    ) : super(context, attrs, defStyleAttr) {
        initView(attrs)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView(attrs)
    }

    private fun initView(attrs: AttributeSet?) {

        dstPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        srcPaint = Paint(Paint.ANTI_ALIAS_FLAG)

        dstPaint.style = Paint.Style.FILL
        dstPaint.color = Color.GREEN

        srcPaint.style = Paint.Style.FILL
        srcPaint.color = Color.BLUE


        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.XfermodeView)
            val xfermodeValue = typedArray.getInt(R.styleable.XfermodeView_xfermode_model, -1)
            if (xfermodeValue != -1) {
                defXfermode = intToMode(xfermodeValue)
            }
        }

    }

    fun intToMode(`val`: Int): PorterDuff.Mode? {
        return when (`val`) {
            0 -> PorterDuff.Mode.CLEAR
            1 -> PorterDuff.Mode.SRC
            2 -> PorterDuff.Mode.DST
            3 -> PorterDuff.Mode.SRC_OVER
            4 -> PorterDuff.Mode.DST_OVER
            5 -> PorterDuff.Mode.SRC_IN
            6 -> PorterDuff.Mode.DST_IN
            7 -> PorterDuff.Mode.SRC_OUT
            8 -> PorterDuff.Mode.DST_OUT
            9 -> PorterDuff.Mode.SRC_ATOP
            10 -> PorterDuff.Mode.DST_ATOP
            11 -> PorterDuff.Mode.XOR
            16 -> PorterDuff.Mode.DARKEN
            17 -> PorterDuff.Mode.LIGHTEN
            13 -> PorterDuff.Mode.MULTIPLY
            14 -> PorterDuff.Mode.SCREEN
            12 -> PorterDuff.Mode.ADD
            15 -> PorterDuff.Mode.OVERLAY
            else -> PorterDuff.Mode.CLEAR
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val saveCount =
                canvas.saveLayer(0F, 0F, width.toFloat(), height.toFloat(), null, Canvas.ALL_SAVE_FLAG)

        //dst  圆  ;  src 方
        canvas.drawBitmap(dst, 0F, 0F, dstPaint)

        srcPaint.xfermode = PorterDuffXfermode(defXfermode)
        canvas.drawBitmap(src, 0F, 0F, srcPaint)
        srcPaint.xfermode = null

        canvas.restoreToCount(saveCount)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val h = getUsefulSize(heightMeasureSpec, defHeight)
        val w = getUsefulSize(widthMeasureSpec, defWidth)
        val r = Math.min(w, h)
        setMeasuredDimension(r, r)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        initDstAndSrc()
    }

    private fun initDstAndSrc() {
        val circleR = width / 3F
        src = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val srcC = Canvas(src)
        srcC.drawRect(RectF(circleR, circleR, width.toFloat(), height.toFloat()), srcPaint)

        dst = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val dstC = Canvas(dst)
        dstC.drawCircle(circleR, circleR, circleR, dstPaint)
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