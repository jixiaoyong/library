package cf.android666.applibrary.utils

import android.annotation.TargetApi
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager

/**
 *  Created by jixiaoyong1995@gmail.com
 *  Data: 2019/6/20.
 *  Description:沉浸式工具
 */
object ImmersiveUtils {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun setStateBarColor(window: Window, colorInt: Int) {
        //5.0以上版本
        //设置FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS属性才能调用setStatusBarColor方法来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        //设置FLAG_TRANSLUCENT_STATUS透明状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        //根据输入的颜色和透明度显示
        window.statusBarColor = colorInt
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun setTransparentStateBar(window: Window) {
        //兼容5.0及以上支持全透明
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
    }
}
