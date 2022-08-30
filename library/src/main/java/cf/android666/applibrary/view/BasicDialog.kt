package cf.android666.applibrary.view

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Bundle
import android.view.View

/**
 * author: jixiaoyong
 * email: jixiaoyong1995@gmail.com
 * website: https://jixiaoyong.github.io
 * date: 2019-06-06
 * description: todo
 */
abstract class BasicDialog @JvmOverloads constructor(context: Context, themeResId: Int = 0)
    : Dialog(context, themeResId) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(getContextView())

        window?.setLayout(900, 1200)
        val radius = 50F
        val rrs = RoundRectShape(floatArrayOf(radius, radius, radius, radius, radius, radius, radius, radius), null, null)
        val backgroundDrawable = ShapeDrawable(rrs)
        backgroundDrawable.paint.color = Color.WHITE
        window?.setBackgroundDrawable(backgroundDrawable)
    }

    abstract fun getContextView(): View
}