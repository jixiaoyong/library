package cf.android666.diywidget.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Build
import android.util.AttributeSet
import androidx.annotation.RequiresApi
import cf.android666.applibrary.utils.DpPxUtils
import cf.android666.applibrary.view.CommonSurfaceView
import kotlin.math.sin

/**
 * author: jixiaoyong
 * email: jixiaoyong1995@gmail.com
 * website: https://jixiaoyong.github.io
 * date: 2019-07-07
 * description: todo
 */
open class SineView : CommonSurfaceView {

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0)
            : super(context, attrs, defStyleAttr, defStyleRes)

    protected var offsetX: Float = 0F
        private set
    protected var mPaint: Paint = Paint()
    private var mPath = Path()
    private var mX = 0F
    private var mY = 0F

    init {
        mPaint.color = Color.BLACK
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = DpPxUtils.dip2px(context, 1).toFloat()
    }

    override fun drawSth(mCanvas: Canvas?) {
        mCanvas?.let {
            offsetX = if (mX - width > 0) -(mX - width) else 0F
            it.translate(offsetX, 0F)
            mX++
            mY = (100 * sin(mX * 2 * Math.PI / 180) + 400).toFloat()
            mPath.lineTo(mX, mY)
            it.drawColor(Color.WHITE)
            it.drawPath(mPath, mPaint)
            it.save()
        }
    }
}