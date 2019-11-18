package cf.android666.applibrary.view

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.widget.TextView
import androidx.annotation.RequiresApi

/**
 * author: jixiaoyong
 * email: jixiaoyong1995@gmail.com
 * website: https://jixiaoyong.github.io
 * date: 2019-07-07
 * description: todo
 */
class ColorfulTextView : TextView {

    private var mTranslate: Float = 0F
    private var mGradientMatrix: Matrix? = null
    private lateinit var mLinearGradient: LinearGradient
    private var mViewWidth = 0F

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes)


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (mViewWidth == 0F) {
            mViewWidth = measuredWidth.toFloat()
            if (mViewWidth > 0) {
                mLinearGradient = LinearGradient(0F, 0F, mViewWidth, 0F,
                        intArrayOf(Color.GREEN, Color.RED, Color.BLUE), null, Shader.TileMode.CLAMP)
                paint.shader = mLinearGradient
                mGradientMatrix = Matrix()
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        mTranslate += mViewWidth / 10
        if (mTranslate > 2 * mViewWidth) {
            mTranslate = -mViewWidth
        }
        mGradientMatrix?.setTranslate(mTranslate, 0F)
        mLinearGradient.setLocalMatrix(mGradientMatrix)
        postInvalidateDelayed(100)
    }
}