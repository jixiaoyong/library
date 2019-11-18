package cf.android666.applibrary.utils

import android.animation.ValueAnimator
import android.view.MotionEvent
import android.view.View
import android.widget.ListView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlin.math.abs

/**
 *  Created by jixiaoyong1995@gmail.com
 *  Data: 2019/6/26.
 *  Description: 两个View联动
 */

object LinkageUtils {

    fun showOrHiddenWhenScroll(host: View, client: View) {
        var lastY = 0F
        host.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastY = event.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    var dY = event.rawY - lastY

                    if (abs(dY) < client.height) {
                        val x = client.translationY + dY

                        if (dY < 0) {
                            //up
                            if (-x >= client.height) {
                                return@setOnTouchListener false
                            }

                        } else if (x >= 0) {
                            return@setOnTouchListener false
                        }
                        client.translationY = x
                        host.translationY = client.height + x
                    }
                    lastY = event.rawY
                }
                MotionEvent.ACTION_UP -> {
                    if (client.translationY < 0) {
                        val tY = abs(client.translationY)
                        val valueAnimator: ValueAnimator
                        valueAnimator = if (tY >= (client.height / 2)) {
                            ValueAnimator.ofFloat(-tY, -client.height.toFloat())
                        } else {
                            ValueAnimator.ofFloat(-tY, 0F)
                        }
                        valueAnimator.duration = 300
                        valueAnimator.addUpdateListener {
                            val value = it.animatedValue as Float
                            client.translationY = value
                            host.translationY = client.height - abs(value)
                        }
                        valueAnimator.start()
                    }
                }
            }

            return@setOnTouchListener false
        }

    }

    @JvmOverloads
    @JvmStatic
    fun overScroll(host: View, maxDragDistance: Float = 100F) {
        var lastY = 0F
        host.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastY = event.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    val dY = event.rawY - lastY
                    if (dY > 0 && (host.translationY + dY) <= maxDragDistance && checkCanOverScrollDownOrRight(host)) {
                        host.translationY += dY
                    } else if (dY < 0 && host.translationY > 0 && (host.translationY + dY) >= 0) {
                        //如果已经有OverScroll，那么在上滑时应该优先消耗这部分空间
                        host.translationY += dY
                    }
                    lastY = event.rawY
                }
                MotionEvent.ACTION_UP -> {
                    if (host.translationY != 0F) {
                        val valueAnimator: ValueAnimator = ValueAnimator.ofFloat(host.translationY, -50F, 0F)
                        valueAnimator.duration = 300
                        valueAnimator.addUpdateListener {
                            val value = it.animatedValue as Float
                            host.translationY = value
                        }
                        valueAnimator.start()
                    }
                }
            }
            return@setOnTouchListener false
        }
    }

    //是否向下或者向右拖动
    private fun checkCanOverScrollDownOrRight(view: View): Boolean {
        return when (view) {
            is ListView -> view.getChildAt(0)?.top == 0
            is androidx.recyclerview.widget.RecyclerView -> {
                val isVertical = when (val mng = view.layoutManager) {
                    is androidx.recyclerview.widget.LinearLayoutManager -> mng.orientation == androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
                    is androidx.recyclerview.widget.GridLayoutManager -> mng.orientation == androidx.recyclerview.widget.GridLayoutManager.HORIZONTAL
                    is androidx.recyclerview.widget.StaggeredGridLayoutManager -> mng.orientation == androidx.recyclerview.widget.StaggeredGridLayoutManager.HORIZONTAL
                    else -> return false//不知名布局，为了效果，不可OverScroll
                }
                if (isVertical) {
                    view.getChildAt(0).top == 0
                } else {
                    view.getChildAt(0).left == 0
                }
            }
            else -> true//不知名View，默认可以OverScroll
        }
    }

}
