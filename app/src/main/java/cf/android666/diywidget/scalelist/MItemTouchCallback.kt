package cf.android666.diywidget.scalelist

import androidx.recyclerview.widget.ItemTouchHelper


/**
 * author: jixiaoyong
 * email: jixiaoyong1995@gmail.com
 * website: https://jixiaoyong.github.io
 * date: 2019-06-30
 * description: todo
 */
class MItemTouchCallback(private val dataList: ArrayList<String>,
                         private val adapter: androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>,
                         private val mListener: OnSwipeListener<String>?) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(recyclerView: androidx.recyclerview.widget.RecyclerView, p1: androidx.recyclerview.widget.RecyclerView.ViewHolder): Int {
        val dragFlag = 0
        var swipeFlag = 0
        if (recyclerView.layoutManager is ScaleLinearLayoutManager) {
            swipeFlag = ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        }
        return makeMovementFlags(dragFlag, swipeFlag)
    }

    override fun onMove(p0: androidx.recyclerview.widget.RecyclerView, p1: androidx.recyclerview.widget.RecyclerView.ViewHolder, p2: androidx.recyclerview.widget.RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, direction: Int) {
        viewHolder.itemView.setOnTouchListener(null)

        val currentPosition = viewHolder.layoutPosition
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