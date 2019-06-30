package cf.android666.diywidget.scalelist

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log

/**
 * author: jixiaoyong
 * email: jixiaoyong1995@gmail.com
 * website: https://jixiaoyong.github.io
 * date: 2019-06-29
 * description: todo
 */
class ScaleLinearLayoutManager2(context: Context, private val mOrientation: Int = VERTICAL,
                                reverseLayout: Boolean = false)
    : LinearLayoutManager(context, mOrientation, reverseLayout) {

    private var offset = 0F
    private var DEFAULT_TRANSLATION_X = 3F
    private var DEFAULT_TRANSLATION_X_WITH_OFFSET = DEFAULT_TRANSLATION_X + offset
    private var DEFAULT_SHOW_ITEM_COUNT = 3
    private var DEFAULT_SHOW_ITEM_ITEM = 1

    private var DEFAULT_SCALE = 0.6F

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        super.onLayoutChildren(recycler, state)
        drawChild(recycler)
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        val result = super.scrollHorizontallyBy(dx, recycler, state)
        if (mOrientation == HORIZONTAL) {
            offset += dx
            drawChild(recycler)
        }
        return result
    }

    private fun drawChild(recycler: RecyclerView.Recycler?) {

        if (recycler == null) {
            return
        }

        removeAllViews()
        detachAndScrapAttachedViews(recycler)

        var mItemCount = if (itemCount <= DEFAULT_SHOW_ITEM_COUNT) {
            itemCount
        } else {
            DEFAULT_SHOW_ITEM_COUNT
        }

        for (i in mItemCount - 3 until mItemCount) {
            val childView = recycler.getViewForPosition(i)
            addView(childView)
            measureChild(childView, 0, 0)

            val widthSpace = width - getDecoratedMeasuredWidth(childView)
            val heightSpace = height - getDecoratedMeasuredHeight(childView)
            layoutDecoratedWithMargins(childView, widthSpace / 2, heightSpace / 2,
                    getDecoratedMeasuredWidth(childView) + widthSpace / 2,
                    getDecoratedMeasuredHeight(childView) + heightSpace / 2)

            DEFAULT_TRANSLATION_X = ((getDecoratedMeasuredHeight(childView) / 3) * 2.toFloat())
            DEFAULT_TRANSLATION_X_WITH_OFFSET = DEFAULT_TRANSLATION_X + offset

            val first = findFirstVisibleItemPosition()
            val last = findLastVisibleItemPosition()

            if (offset > DEFAULT_TRANSLATION_X) {
                recycler.recycleView(recycler.getViewForPosition(DEFAULT_SHOW_ITEM_ITEM - 1))
                DEFAULT_SHOW_ITEM_COUNT = (offset / DEFAULT_TRANSLATION_X).toInt() + 3
                DEFAULT_SHOW_ITEM_ITEM = (offset / DEFAULT_TRANSLATION_X).toInt() + 1
            }


            logd("DEFAULT_SHOW_ITEM_ITEM:$DEFAULT_SHOW_ITEM_ITEM ,i:$i")

            when {
                i == DEFAULT_SHOW_ITEM_ITEM -> {
                    childView.scaleX = (1F - (offset / DEFAULT_TRANSLATION_X) * (1 - DEFAULT_SCALE))
                    childView.scaleY = (1F - (offset / DEFAULT_TRANSLATION_X) * (1 - DEFAULT_SCALE))
                    childView.translationX = -offset
                }
                i > DEFAULT_SHOW_ITEM_ITEM -> {
                    childView.scaleX = DEFAULT_SCALE + (offset / DEFAULT_TRANSLATION_X) * (1 - DEFAULT_SCALE)
                    childView.scaleY = DEFAULT_SCALE + (offset / DEFAULT_TRANSLATION_X) * (1 - DEFAULT_SCALE)
                    childView.translationX = DEFAULT_TRANSLATION_X - offset
                }
                i < DEFAULT_SHOW_ITEM_ITEM -> {
                    childView.scaleX = DEFAULT_SCALE
                    childView.scaleY = DEFAULT_SCALE
                    childView.translationX = -DEFAULT_TRANSLATION_X_WITH_OFFSET
                }
            }


        }
    }


    fun logd(s: String) {
        Log.d("TAG", s)
    }
}