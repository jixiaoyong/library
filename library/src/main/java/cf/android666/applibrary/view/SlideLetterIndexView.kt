package cf.android666.applibrary.view


import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import cf.android666.applibrary.utils.DpPxUtils
import kotlin.math.min

/**
 * author: jixiaoyong
 * email: jixiaoyong1995@gmail.com
 * website: https://jixiaoyong.github.io
 * date: 11/22/20
 * description: 一个竖排的“#” + A-Z字母排列View
 */
class SlideIndexView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
                                               defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private val mLetters = arrayListOf<String>()

    // 选中的文字索引
    private var mSelectIndex = 0

    private var defWidth = DpPxUtils.dip2px(context, 17)
    private var defHeight = DpPxUtils.dip2px(context, 476)

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    var onItemClickListener: OnItemClickListener? = null

    init {
        // 默认为 “#”+“A-Z”
        mLetters.add("#")
        for (i in 0 until 26) {
            mLetters.add(('A' + i).toString())
        }
        textPaint.color = Color.parseColor("#FF6E7482")
        textPaint.textSize = DpPxUtils.sp2px(context, 12).toFloat()
        textPaint.textAlign = Paint.Align.CENTER
    }

    /**
     * 只显示指定的索引
     */
    fun updateLetters(arrayList: List<String>) {
        mLetters.clear()
        mLetters.addAll(arrayList)
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(getRightMeasureSpec(widthMeasureSpec, defWidth),
                getRightMeasureSpec(heightMeasureSpec, defHeight))
    }

    private fun getRightMeasureSpec(oldMeasureSpec: Int, defMeasureValue: Int): Int {
        val measureValue = MeasureSpec.getSize(oldMeasureSpec)
        val measureModel = MeasureSpec.getMode(oldMeasureSpec)

        val value = when (measureModel) {
            MeasureSpec.UNSPECIFIED -> defMeasureValue
            MeasureSpec.EXACTLY -> measureValue
            else -> min(defMeasureValue, measureValue)
        }
        return MeasureSpec.makeMeasureSpec(value, measureModel)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val mHeight = height - paddingTop - paddingBottom
        val mWidth = width - paddingStart - paddingEnd

        val childHeight = mHeight.toFloat() / mLetters.size
        val childWidth = mWidth.toFloat()

        val textRect = Rect()

        mLetters.forEachIndexed { index, s ->
            // 第0位的“热门”，字体大小为9sp，其余为12sp
            textPaint.textSize = DpPxUtils.sp2px(context, if (index == 0) 9 else 12).toFloat()
            textPaint.color =
                    Color.parseColor(if (index == mSelectIndex) "#FFE73D02" else "#FF6E7482")
            textPaint.getTextBounds(s, 0, s.length, textRect)

            val left = (childWidth) / 2F
            val top =
                    childHeight * index + childHeight / 2F - textRect.centerY().toFloat()
            canvas.drawText(s, left, top, textPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val height = height.toFloat()
        when (event!!.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE, MotionEvent.ACTION_SCROLL -> {
                val y = if (event.y >= 0 && event.y < height) {
                    // 只处理在字母索引范围内的滑动/点击事件
                    event.y
                } else {
                    return true
                }
                mSelectIndex = (y / height * mLetters.size).toInt()
                invalidate()
                mLetters.getOrNull(mSelectIndex)?.let {
                    if (event.action != MotionEvent.ACTION_DOWN) {
                        onItemClickListener?.onSweep(mSelectIndex, it)
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                invalidate()
                mLetters.getOrNull(mSelectIndex)?.let {
                    onItemClickListener?.onClick(mSelectIndex, it)
                }
            }
        }
        return true
    }

    interface OnItemClickListener {
        /**
         * 点击
         */
        fun onClick(index: Int, string: String)

        /**
         * 划过，可以不实现
         */
        fun onSweep(index: Int, string: String) {}
    }
}
