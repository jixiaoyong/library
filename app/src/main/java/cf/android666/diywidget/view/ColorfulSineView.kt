package cf.android666.diywidget.view

import android.content.Context
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Shader
import android.os.Build
import android.util.AttributeSet
import androidx.annotation.RequiresApi

/**
 * author: jixiaoyong
 * email: jixiaoyong1995@gmail.com
 * website: https://jixiaoyong.github.io
 * date: 2019-07-07
 * description: todo
 */
class ColorfulSineView : SineView {

    private var mMatrix: Matrix = Matrix()
    private lateinit var mLinearGradient: LinearGradient
    private var mTranslateX: Float = 0F

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0)
            : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mLinearGradient = LinearGradient(0F, 0F, width.toFloat(), 0F,
                intArrayOf(Color.GREEN, Color.RED, Color.BLUE), null,
                Shader.TileMode.CLAMP)
        mPaint.shader = mLinearGradient
        postInvalidateDelayed(100)
    }


}