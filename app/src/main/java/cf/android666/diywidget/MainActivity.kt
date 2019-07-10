package cf.android666.diywidget

import android.animation.ValueAnimator
import android.app.Activity
import android.graphics.Canvas
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.util.Log
import android.widget.ToggleButton
import cf.android666.applibrary.Logger
import cf.android666.applibrary.ProgressButton
import cf.android666.diywidget.view.RoundDialogView
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

/**
 * Created by jixiaoyong on 2018/2/18.
 */

class MainActivity : Activity() {

    private val toggleButton: ToggleButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val progressButton = findViewById<ProgressButton>(R.id.progress)

        val animator = ValueAnimator.ofFloat(0F, 1F)
        animator.duration = 10000
        animator.repeatCount = ValueAnimator.INFINITE
        animator.repeatMode = ValueAnimator.RESTART
        animator.addUpdateListener { animation -> progressButton.progress = animation.animatedValue as Float }
        animator.start()

        Log.d(Logger.generateTag(), "" + Logger.isLog)

        click_btn.setOnClickListener {
            RoundDialogView(this).show()
        }
//        go_scale_activity.setOnClickListener {
//        startActivity(Intent(this@MainActivity, ScaleHorizontalActivity::class.java))
//        }

        thread {
            Logger.e("warning")
        }
    }

    @RequiresApi(17)
    fun api17() {
        val c = Canvas()

    }
}
