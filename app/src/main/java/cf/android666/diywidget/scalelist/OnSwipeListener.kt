package cf.android666.diywidget.scalelist


/**
 * author: jixiaoyong
 * email: jixiaoyong1995@gmail.com
 * website: https://jixiaoyong.github.io
 * date: 2019-06-30
 * description: todo
 */
interface OnSwipeListener<T> {

    /**
     * 卡片还在滑动时回调
     *
     * @param viewHolder 该滑动卡片的viewHolder
     * @param ratio      滑动进度的比例
     * @param direction  卡片滑动的方向，CardConfig.SWIPING_LEFT 为向左滑，CardConfig.SWIPING_RIGHT 为向右滑，
     * CardConfig.SWIPING_NONE 为不偏左也不偏右
     */
    fun onSwiping(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, ratio: Float, direction: Int)

    /**
     * 卡片完全滑出时回调
     *
     * @param viewHolder 该滑出卡片的viewHolder
     * @param t          该滑出卡片的数据
     * @param direction  卡片滑出的方向，CardConfig.SWIPED_LEFT 为左边滑出；CardConfig.SWIPED_RIGHT 为右边滑出
     */
    fun onSwiped(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, t: T, direction: Int)

    /**
     * 所有的卡片全部滑出时回调
     */
    fun onSwipedClear()


    object CardConfig {
        const val SWIPING_LEFT = -1
        const val SWIPING_RIGHT = 1
        const val SWIPING_NONE = 0
    }

}