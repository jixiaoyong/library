//package cf.android666.diywidget.view
//
//import android.content.Context
//import android.content.res.TypedArray
//import android.graphics.Canvas
//import android.graphics.Path
//import android.graphics.RectF
//import android.os.Build
//import android.support.annotation.RequiresApi
//import android.support.v7.widget.AppCompatImageView
//import android.util.AttributeSet
//import cf.android666.diywidget.R
//import kotlin.math.min
//
//
///**
// * author: jixiaoyong
// * email: jixiaoyong1995@gmail.com
// * website: https://jixiaoyong.github.io
// * date: 2019-07-13
// * description: 圆角图片
// * 参考 https://www.zybuluo.com/946898963/note/1268593
// */
//class CustomShapeImageView : AppCompatImageView {
//
//    private var arr: TypedArray? = null
//    private var imageType: ImageType = ImageType.RoundRect
//
//    private var defR = 10F
//    private var rx = defR
//    private var ry = defR
//
//    @JvmOverloads
//    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
//            : super(context, attrs, defStyleAttr) {
//        arr = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView, defStyleAttr, 0)
//    }
//
////    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
////    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int)
////            : super(context, attrs, defStyleAttr, defStyleRes) {
////        arr = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView, defStyleAttr, defStyleRes)
////    }
//
//    init {
//
//        arr?.recycle()
//    }
//
//    override fun onDraw(canvas: Canvas) {
//        val rectF = RectF(0F, 0F, width.toFloat(), height.toFloat())
//        val path = Path()
//        when (imageType) {
//            ImageType.Circle -> path.addCircle((width / 2).toFloat(), (height / 2).toFloat(), (min(width, height) / 2).toFloat(), Path.Direction.CCW)
//            ImageType.RoundRect -> path.addRoundRect(rectF, rx, ry, Path.Direction.CCW)
//        }
//        canvas.clipPath(path)
//
//        super.onDraw(canvas)
//    }
//
//    sealed class ImageType {
//        object Circle : ImageType()
//        object RoundRect : ImageType()
//    }
//}