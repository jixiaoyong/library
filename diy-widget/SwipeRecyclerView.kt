package cf.android666.myapplication.dispatchtouchevent

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * author: jixiaoyong
 * email: jixiaoyong1995@gmail.com
 * website: https://jixiaoyong.github.io
 * date: 2019/3/30
 * description: 左右滑动隐藏、显示更多布局的RecycleView
 * 参考资料：https://www.jianshu.com/p/9bfed6e127cc
 *
 * 使用时需要设置 要展示的view Id： recyler.setMoreViewId(R.id.more)
 */
class SwipeRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    RecyclerView(context, attrs, defStyle) {

    //滑动误差范围
    private val SWIPE_OFFSET = 50

    private var startX = 0F
    private var startY = 0F
    private var chooseChild: View? = null
    private var more: View? = null
    private var moreState = MORE_VIEW_STATE.HIDDEN

    private var moreViewId = -1

    fun setMoreViewId(id: Int) {
        moreViewId = id
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = ev.rawX
                startY = ev.rawY
                val newChooseChild = findChildViewUnder(ev.x, ev.y)
                if (chooseChild != newChooseChild) {
                    chooseChild?.scrollTo(0, 0)
                    chooseChild = newChooseChild
                }
//                more = (chooseChild as ViewGroup).getChildAt((chooseChild as ViewGroup).childCount - 1)
                more = if (moreViewId != -1) (chooseChild as ViewGroup).findViewById(moreViewId) else null

                val scrollX = chooseChild?.scrollX ?: 0
                moreState = if (Math.abs(scrollX - (more?.width ?: 0)) <= SWIPE_OFFSET) {
                    MORE_VIEW_STATE.SHOWING
                } else {
                    MORE_VIEW_STATE.HIDDEN
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (chooseChild == null || more == null) {
                    return false
                }
                val dX = ev.rawX - startX
                val dY = ev.rawY - startY
                val moreWidth = more!!.width

                if (Math.abs(dX) > Math.abs(dY) && Math.abs(dX) <= moreWidth) {
                    //swipe to right -->
                    if (dX > 0 && (moreState == MORE_VIEW_STATE.SHOWING || moreState == MORE_VIEW_STATE.TO_HIDDEN)) {
                        chooseChild?.scrollTo(moreWidth - dX.toInt(), 0)
                        moreState = MORE_VIEW_STATE.TO_HIDDEN
                        if (Math.abs(Math.abs(dX) - moreWidth) < SWIPE_OFFSET) {
                            chooseChild?.scrollTo(0, 0)
                            moreState = MORE_VIEW_STATE.HIDDEN
                        }

                    } else if (dX < 0 && (moreState == MORE_VIEW_STATE.HIDDEN || moreState == MORE_VIEW_STATE.TO_SHOW)) {
                        //swipe to left <--
                        chooseChild?.scrollTo(-dX.toInt(), 0)
                        moreState = MORE_VIEW_STATE.TO_SHOW
                        if (Math.abs(Math.abs(dX) - moreWidth) < SWIPE_OFFSET) {
                            chooseChild?.scrollTo(moreWidth, 0)
                            moreState = MORE_VIEW_STATE.SHOWING
                        }
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                val dX = ev.rawX - startX
                val dY = ev.rawY - startY
                if (Math.abs(dX) > Math.abs(dY)) {
                    if (moreState == MORE_VIEW_STATE.TO_HIDDEN || moreState == MORE_VIEW_STATE.TO_SHOW) {
                        chooseChild?.scrollTo(0, 0)
                        moreState = MORE_VIEW_STATE.HIDDEN
                    }
                }
                startX = 0F
                startY = 0F
            }
        }

        return super.dispatchTouchEvent(ev)
    }

    enum class MORE_VIEW_STATE {
        HIDDEN, TO_SHOW, SHOWING, TO_HIDDEN
    }


}