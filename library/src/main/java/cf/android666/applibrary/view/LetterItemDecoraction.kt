package cf.android666.applibrary.view


import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import cf.android666.applibrary.R
import cf.android666.applibrary.utils.DpPxUtils
import kotlin.math.abs

/**
 * author: jixiaoyong
 * email: jixiaoyong1995@gmail.com
 * website: https://jixiaoyong.github.io
 * date: 11/22/20
 * description: todo
 */
class LetterItemDecoration : RecyclerView.ItemDecoration {


    private lateinit var mContext: Context
    private var mDatas = arrayListOf<String>()
    private var mColorLetterText: Int = Color.parseColor("#FFB2B6BE")
    private var mColorBg: Int = Color.parseColor("#FFF6F6F6")
    private var mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mDividerBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var mTextSize = 0F
    private var defDividerHeight = 0
    private var mTitleHeight = 0


    constructor(context: Context, data: List<String>?) : this(context, null, data)

    constructor(context: Context, attrs: AttributeSet?, data: List<String>?) : super() {
        mContext = context
        if (data != null) {
            mDatas.addAll(data)
        }
        val arr: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.LetterItemDecoration)
        mColorLetterText = arr.getColor(R.styleable.LetterItemDecoration_color_letter_text, Color.parseColor("#FFB2B6BE"))
        mColorBg = arr.getColor(R.styleable.LetterItemDecoration_color_background, Color.parseColor("#FFF6F6F6"))
        mTextSize = arr.getDimension(R.styleable.LetterItemDecoration_size_title_text, DpPxUtils.sp2px(context, 15).toFloat())
        mTitleHeight = arr.getDimensionPixelSize(R.styleable.LetterItemDecoration_title_height, DpPxUtils.dip2px(context, 25))

        mLinePaint.color = mColorBg
        mLinePaint.strokeWidth = 3F
        mDividerBackgroundPaint.color = Color.WHITE

        mTextSize = DpPxUtils.sp2px(context, 15).toFloat()
        defDividerHeight = DpPxUtils.dip2px(context, 2)
        mTitleHeight = DpPxUtils.dip2px(context, 25)

    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val right = parent.right - parent.paddingRight.toFloat()
        val left = DpPxUtils.dip2px(mContext, 15).toFloat()

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child: View = parent.getChildAt(i)
            val cLeft = child.left.toFloat() + child.paddingLeft.toFloat() + left
            val position = (child.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition
            if (position != -1 && !mDatas.isNullOrEmpty()) {
                val text = mDatas[position].substring(0, 1).toUpperCase()
                when {
                    position == 0 -> {
                        drawText(c, cLeft, child, text)
                    }
                    text != mDatas[position - 1].substring(0, 1).toUpperCase() -> {
                        drawText(c, cLeft, child, text)
                    }
                    else -> {
                        val top = child.top - defDividerHeight.toFloat()
                        val lineTop = child.top - defDividerHeight / 2F
                        c.drawRect(child.left.toFloat(), top, right + child.paddingRight,
                                child.top.toFloat(), mDividerBackgroundPaint)
                        c.drawLine(cLeft, lineTop, right - DpPxUtils.dip2px(mContext, 15),
                                lineTop, mLinePaint)
                    }
                }
            }
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = (view.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition
        if (position != -1 && !mDatas.isNullOrEmpty()) {
            val text = mDatas[position].substring(0, 1).toUpperCase()
            if (position == 0) {
                outRect.set(0, mTitleHeight, 0, 0)
            } else if (text != null && text != mDatas!![position - 1].substring(0, 1).toUpperCase()) {
                outRect.set(0, mTitleHeight, 0, 0)
            } else {
                outRect.set(0, defDividerHeight, 0, 0)
            }
        }
    }

    private fun drawText(canvas: Canvas, left: Float, child: View, text: String) {
        mTextPaint.color = mColorBg
        mTextPaint.reset()
        mTextPaint.color = mColorLetterText
        mTextPaint.textSize = mTextSize
        mTextPaint.textAlign = Paint.Align.LEFT
        val bounds = Rect()
        mTextPaint.getTextBounds(text, 0, text!!.length, bounds)

        val baseline = mTitleHeight / 2 + abs(mTextPaint.ascent() + mTextPaint.descent()) / 2F
        val dividerTop = (child.top.toFloat() - mTitleHeight.toFloat())
        canvas.drawText(text, left, baseline + dividerTop, mTextPaint)
    }

    /**
     * 更新列表的itemDecoration
     */
    fun updateLetters(data: List<String>) {
        mDatas.clear()
        mDatas.addAll(data)
    }
}
