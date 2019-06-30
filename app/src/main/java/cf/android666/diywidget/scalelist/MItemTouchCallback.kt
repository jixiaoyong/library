package cf.android666.diywidget.scalelist

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper


/**
 * author: jixiaoyong
 * email: jixiaoyong1995@gmail.com
 * website: https://jixiaoyong.github.io
 * date: 2019-06-30
 * description: todo
 */
class MItemTouchCallback(private val dataList: ArrayList<String>,
                         private val adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
                         private val mListener: OnSwipeListener<String>?) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
        val dragFlag = 0
        var swipeFlag = 0
        if (recyclerView?.layoutManager is ScaleLinearLayoutManager) {
            swipeFlag = ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        }
        return makeMovementFlags(dragFlag, swipeFlag)
    }

    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
        viewHolder?.itemView?.setOnTouchListener(null)

        val currentPosition = viewHolder!!.layoutPosition
        val remove = dataList.removeAt(currentPosition)
        adapter.notifyDataSetChanged()
        mListener?.onSwiped(viewHolder, remove,
                if (direction == ItemTouchHelper.LEFT) OnSwipeListener.CardConfig.SWIPING_LEFT else OnSwipeListener.CardConfig.SWIPING_RIGHT)
        // 当没有数据时回调 OnSwipeListener 监听器
        if (adapter.itemCount === 0) {
            mListener?.onSwipedClear()
        }
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return false
    }

}