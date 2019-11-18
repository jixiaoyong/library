package cf.android666.diywidget.scalelist

import android.content.Context
import android.view.MotionEvent
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager

/**
 * author: jixiaoyong
 * email: jixiaoyong1995@gmail.com
 * website: https://jixiaoyong.github.io
 * date: 2019-06-29
 * description: todo
 */
class ScaleLinearLayoutManager(context: Context, private val mRecyclerView: androidx.recyclerview.widget.RecyclerView,
                               private val mItemTouchHelper: ItemTouchHelper, orientation: Int = VERTICAL,
                               reverseLayout: Boolean = false)
    : LinearLayoutManager(context, orientation, reverseLayout) {

    private var DEFAULT_TRANSLATION_X = 3F
    private var DEFAULT_SHOW_ITEM_COUNT = 3
    private var DEFAULT_SCALE = 0.1F

    override fun onLayoutChildren(recycler: androidx.recyclerview.widget.RecyclerView.Recycler?, state: androidx.recyclerview.widget.RecyclerView.State?) {
        super.onLayoutChildren(recycler, state)

        if (recycler == null) {
            return
        }

        removeAllViews()
        detachAndScrapAttachedViews(recycler)

        val mItemCount = if (itemCount <= DEFAULT_SHOW_ITEM_COUNT) {
            itemCount
        } else {
            DEFAULT_SHOW_ITEM_COUNT
        }

        for (i in mItemCount - 1 downTo 0) {
            val childView = recycler.getViewForPosition(i)
            addView(childView)
            measureChild(childView, 0, 0)

            val widthSpace = width - getDecoratedMeasuredWidth(childView)
            val heightSpace = height - getDecoratedMeasuredHeight(childView)
            layoutDecoratedWithMargins(childView, widthSpace / 2, heightSpace / 2,
                    getDecoratedMeasuredWidth(childView) + widthSpace / 2,
                    getDecoratedMeasuredHeight(childView) + heightSpace / 2)

            DEFAULT_TRANSLATION_X = (getDecoratedMeasuredHeight(childView) / 2).toFloat() / DEFAULT_SHOW_ITEM_COUNT

            when {
                i == DEFAULT_SHOW_ITEM_COUNT -> {
                    childView.scaleX = 1 - (i - 1) * DEFAULT_SCALE
                    childView.scaleY = 1 - (i - 1) * DEFAULT_SCALE
                }
                i > 0 -> {
                    childView.scaleX = 1 - (i) * DEFAULT_SCALE
                    childView.scaleY = 1 - (i) * DEFAULT_SCALE
                    childView.translationY = (i.toFloat()) * DEFAULT_TRANSLATION_X
                }
                else -> {
                    childView.setOnTouchListener { v, event ->
                        val holder = mRecyclerView.getChildViewHolder(v)
                        if (event.action == MotionEvent.ACTION_DOWN) {
                            mItemTouchHelper.startSwipe(holder)
                        }
                        return@setOnTouchListener false
                    }
                }
            }


        }
    }

    override fun canScrollHorizontally(): Boolean {
        return false
    }

    override fun canScrollVertically(): Boolean {
        return false
    }
}