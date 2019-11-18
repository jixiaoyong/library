package cf.android666.diywidget

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import cf.android666.applibrary.Logger
import cf.android666.applibrary.utils.DpPxUtils
import cf.android666.applibrary.utils.ImmersiveUtils
import kotlinx.android.synthetic.main.activity_scrolling.*
import kotlinx.android.synthetic.main.content_scrolling.*

class ScrollingActivity : AppCompatActivity() {

    private var top: Float = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)

        ImmersiveUtils.setTransparentStateBar(window)

        top = DpPxUtils.dip2px(this, 150).toFloat()

        nest_scroll_view.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { p0, p1, p2, p3, p4 ->
            if (nest_scroll_view.scrollY <= top) {
                toolbar.alpha = (nest_scroll_view.scrollY / top)
            }
        })

        var oldY = 0F
        nest_scroll_view.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> oldY = event.rawY
                MotionEvent.ACTION_MOVE -> {
                    val dy = event.rawY - oldY
                    Logger.d("dy:$dy")
                    if (dy > 0 && canOverScroll(dy)) {
                        text.translationY += dy
                    }
                    oldY = event.rawY
                }
                MotionEvent.ACTION_UP -> {
                    val oa = ValueAnimator.ofFloat(text.translationY, -50F, 0F)
                    oa.duration = 200
                    oa.addUpdateListener {
                        text.translationY = it.animatedValue as Float
                    }
                    oa.start()
                }
                else -> {
                }
            }

            false
        }

        text.setOnClickListener {
            Logger.d("Hello world")
        }
    }

    private fun canOverScroll(dy: Float): Boolean {
        return (text.translationY + dy < DpPxUtils.dip2px(this, 60)) &&
                nest_scroll_view.scrollY <= 0
    }
}
