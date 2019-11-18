package cf.android666.applibrary.view

import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.annotation.RequiresApi
import kotlin.concurrent.thread

/**
 * author: jixiaoyong
 * email: jixiaoyong1995@gmail.com
 * website: https://jixiaoyong.github.io
 * date: 2019-07-07
 * description: 一个通用的SurfaceView基类
 */
abstract class CommonSurfaceView : SurfaceView, SurfaceHolder.Callback, Runnable {

    private var mCanvas: Canvas? = null
    private var mIsDrawing: Boolean = false
    private var mHolder: SurfaceHolder = holder

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0)
            : super(context, attrs, defStyleAttr, defStyleRes)


    init {
        mHolder.addCallback(this)
        isFocusable = true
        isFocusableInTouchMode = true
        keepScreenOn = true
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        mIsDrawing = true
        thread { run() }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {

    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        mIsDrawing = false
    }

    override fun run() {
        while (mIsDrawing) {
            try {
                mCanvas = mHolder.lockCanvas()
                drawSth(mCanvas)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (mCanvas != null) {
                    mHolder.unlockCanvasAndPost(mCanvas)
                }
            }
        }
    }

    abstract fun drawSth(mCanvas: Canvas?)
}