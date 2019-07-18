package cf.android666.applibrary.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import cf.android666.applibrary.R
import kotlin.math.min


/**
 * author: jixiaoyong
 * email: jixiaoyong1995@gmail.com
 * website: https://jixiaoyong.github.io
 * date: 2019-07-13
 * description: 圆角图片
 * 参考 https://www.zybuluo.com/946898963/note/1268593
 */
class RoundImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : CustomShapeImageView(context, attrs, defStyleAttr) {

    private var arr: TypedArray? = null
    private var imageType: Int = ImageType.roundRect

    private var defR = 10F
    private var rx = defR
    private var ry = defR

    init {
        arr = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView, defStyleAttr, 0)
        initValue()
    }

//    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
//    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int)
//            : super(context, attrs, defStyleAttr, defStyleRes) {
//        arr = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView, defStyleAttr, defStyleRes)
//        initValue()
//    }

    private fun initValue() {
        arr?.let {
            imageType = it.getInt(R.styleable.RoundImageView_imageType, ImageType.roundRect)
            it.recycle()
        }
    }


    override fun getCustomPath(): Path {
        val rectF = RectF(0F, 0F, width.toFloat(), height.toFloat())
        val path = Path()
        when (imageType) {
            ImageType.circle -> path.addCircle((width / 2).toFloat(), (height / 2).toFloat(),
                    (min(width, height) / 2).toFloat(), Path.Direction.CCW)
            ImageType.roundRect -> path.addRoundRect(rectF, rx, ry, Path.Direction.CCW)
        }
        return path
    }

    object ImageType {
        const val circle = 0
        const val roundRect = 1
    }
}