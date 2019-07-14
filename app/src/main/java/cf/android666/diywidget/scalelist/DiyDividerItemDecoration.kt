package cf.android666.diywidget.scalelist

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import cf.android666.applibrary.utils.DpPxUtils

/**
 * author: jixiaoyong
 * email: jixiaoyong1995@gmail.com
 * website: https://jixiaoyong.github.io
 * date: 2019-06-29
 * description: todo
 */
abstract class DiyDividerItemDecoration(private val context: Context)
    : RecyclerView.ItemDecoration() {

    private val outBounds: Rect = Rect()
    private var decorationWidth = 100
    private var lastTitle = ""

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if (parent == null || c == null) {
            return
        }

        val paint = Paint()
        paint.color = Color.BLACK
        paint.isAntiAlias = true

        val left: Int
        val right: Int

        if (parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
            c.clipRect(left, parent.paddingTop, right, parent.height - parent.paddingBottom)
        } else {
            left = 0
            right = parent.width
        }

        val childCount = parent.childCount
        for (x in 0 until childCount) {
            val childView = parent.getChildAt(x)
            val position = parent.getChildAdapterPosition(childView)
            if (shouldHaveDecoration(position)) {
                parent.getDecoratedBoundsWithMargins(childView, outBounds)
                val top = outBounds.top
                val bottom = top + decorationWidth
                paint.color = Color.LTGRAY
                c.drawRect(Rect(left + 50, top, right - 50, bottom), paint)
                drawText(paint, c, getTitle(position), right, left, top)
            }

        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        if (parent == null || c == null) {
            return
        }

        val left: Int
        val right: Int

        if (parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
            c.clipRect(left, parent.paddingTop, right, parent.height - parent.paddingBottom)
        } else {
            left = 0
            right = parent.width
        }

        val paint = Paint()
        paint.isAntiAlias = true
        paint.color = Color.LTGRAY
        c.drawRect(Rect(left, 0, right, decorationWidth), paint)

        val position = parent.getChildLayoutPosition(parent.getChildAt(0))
        val currentTitle = getTitle(position)

        drawText(paint, c, currentTitle, right, left)
        lastTitle = currentTitle

    }

    private fun drawText(paint: Paint, c: Canvas, currentTitle: String, right: Int, left: Int, top: Int = 0) {
        paint.color = Color.WHITE
        paint.textSize = DpPxUtils.sp2px(context, 22).toFloat()
        paint.textAlign = Paint.Align.CENTER

        c.drawText(currentTitle, (right + left) / 2.toFloat(),
                ((decorationWidth + (paint.fontMetrics.bottom - paint.fontMetrics.top) / 2) / 2) + top, paint)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent != null) {
            val position = parent.getChildAdapterPosition(view)
            if (shouldHaveDecoration(position)) {
                outRect?.top = outRect?.top?.plus(decorationWidth)
            }
        }
    }

    abstract fun getTitle(position: Int): String
    abstract fun shouldHaveDecoration(position: Int): Boolean
}