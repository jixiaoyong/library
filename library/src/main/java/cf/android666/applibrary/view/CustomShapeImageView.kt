package cf.android666.applibrary.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet


/**
 * author: jixiaoyong
 * email: jixiaoyong1995@gmail.com
 * website: https://jixiaoyong.github.io
 * date: 2019-07-13
 * description: 圆角图片
 * 参考 https://www.zybuluo.com/946898963/note/1268593
 */
abstract class CustomShapeImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
                                                              defStyleAttr: Int = 0)
    : AppCompatImageView(context, attrs, defStyleAttr) {

    override fun onDraw(canvas: Canvas) {
        val path = getCustomPath()
        canvas.clipPath(path)
        super.onDraw(canvas)
    }

    abstract fun getCustomPath(): Path

}