package cf.android666.applibrary

import android.content.Context

/**
 *  Created by jixiaoyong1995@gmail.com
 *  Data: 2019/6/14.
 *  Description: dp sp 与 px转换工具
 */
object DpPxUtils {
    /**
     * dp转px */
    fun dip2px(context: Context, dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density + 0.5F).toInt()
    }

    /** px转换dip  */
    fun px2dip(context: Context, px: Int): Int {
        val density = context.resources.displayMetrics.density
        return (px / density + 0.5F).toInt()
    }

    /** px转换sp  */
    fun px2sp(context: Context, pxValue: Int): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5F).toInt()
    }

    /** sp转换px  */
    fun sp2px(context: Context, spValue: Int): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5F).toInt()
    }
}