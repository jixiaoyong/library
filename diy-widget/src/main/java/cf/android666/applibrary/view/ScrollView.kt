package cf.android666.applibrary.view

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ScrollView

/**
 *  Created by jixiaoyong1995@gmail.com
 *  Data: 2018/12/19.
 *  Description:
 */
class ScrollView @JvmOverloads constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0)
    : ScrollView(context, attrs, defStyleAttr) {

    /**
     * 防止EditText输入超长文本时被“顶”到屏幕上方
     * 如果ScrollView的子控件或者子控件的子控件是EditText的话，就固定EditText位置
     */
    override fun requestChildRectangleOnScreen(child: View?, rectangle: Rect?, immediate: Boolean): Boolean {

        Log.d("TAG", "开始防止EditText输入超长文本时被“顶”到屏幕上方")

        if (child is EditText) {
            Log.d("TAG", " $child 防止EditText输入超长文本时被“顶”到屏幕上方")

            return true
        }
        if (child is ViewGroup) {
            var count = child.childCount
            for (x in 0 until count) {
                if (child.getChildAt(x) is EditText) {
                    Log.d("TAG", "${child.getChildAt(x)} 防止EditText输入超长文本时被“顶”到屏幕上方")
                    return true
                }
            }
        }
        return super.requestChildRectangleOnScreen(child, rectangle, immediate)
    }
}
