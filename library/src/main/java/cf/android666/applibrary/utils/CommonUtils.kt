package cf.android666.applibrary.utils

import android.graphics.Paint
import android.view.View

/**
 *  Created by jixiaoyong1995@gmail.com
 *  Data: 2020/4/8.
 *  Description:
 */
object CommonUtils {

    /**
     * 计算Paint绘制文字的时y坐标
     */
    fun calculateTextBaseline(fontMetrics: Paint.FontMetrics, centerY: Float = 0F): Float {
        return centerY + (fontMetrics.ascent - fontMetrics.descent) / 2F
    }

    /**
     * 根据Mode获取合适的size
     */
    private fun getUsefulMeasureSize(sizeMeasureSpec: Int, defSize: Int): Int {
        val mode = View.MeasureSpec.getMode(sizeMeasureSpec)
        val size = View.MeasureSpec.getSize(sizeMeasureSpec)

        return when (mode) {
            View.MeasureSpec.EXACTLY, View.MeasureSpec.AT_MOST -> size
            View.MeasureSpec.UNSPECIFIED -> defSize
            else -> defSize
        }
    }
}