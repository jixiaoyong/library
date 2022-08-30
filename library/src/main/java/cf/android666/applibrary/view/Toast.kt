package cf.android666.applibrary.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import cf.android666.applibrary.R
import java.lang.ref.WeakReference

/**
 *  Created by jixiaoyong1995@gmail.com
 *  website: www.android666.cf
 *  Data: 2018/9/21.
 *  Description: 自定义的Toast，全局显示，统一样式
 */
object Toast {

    private lateinit var weakReferenceContext: WeakReference<Context>
    private lateinit var toast: Toast

    const val LENGTH_SHORT = Toast.LENGTH_SHORT
    const val LENGTH_LONG = Toast.LENGTH_LONG

    @JvmStatic
    fun init(context: Context) {
        weakReferenceContext = WeakReference(context)
        val context = weakReferenceContext.get()
        toast = Toast.makeText(context, "", Toast.LENGTH_SHORT)
        toast.view = LayoutInflater.from(context).inflate(R.layout.layout_toast, null, false)
        toast.setGravity(Gravity.CENTER, 0, 0)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    @JvmStatic
    @JvmOverloads
    fun show(text: String, toastType: ToastType, gravity: Int? = Gravity.CENTER) {
        val drawableId = when (toastType) {
            ToastType.TIPS -> android.R.drawable.ic_dialog_info
            ToastType.WARNING -> android.R.drawable.stat_sys_warning
            ToastType.ERROR -> android.R.drawable.ic_delete
        }
        val icon = weakReferenceContext.get()?.resources?.getDrawable(drawableId)
        show(text, Toast.LENGTH_SHORT, icon, gravity)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    @JvmStatic
    @JvmOverloads
    fun show(text: String, icon: Drawable? = null, gravity: Int? = Gravity.CENTER) {
        show(text, Toast.LENGTH_SHORT, icon, gravity)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    @JvmStatic
    @JvmOverloads
    fun show(text: String, duration: Int, icon: Drawable? = null, gravity: Int? = Gravity.CENTER) {
        gravity?.let { toast.setGravity(it, 0, 0) }
        val isIconShow = if (icon == null) View.GONE else View.VISIBLE

        val toastView = toast.view
        if (toastView?.isAttachedToWindow == true) {
            toastView.findViewById<TextView>(R.id.text).text = text
            toastView.findViewById<ImageView>(R.id.icon).setImageDrawable(icon)
            toastView.findViewById<ImageView>(R.id.icon).visibility = isIconShow
        } else {
            val view = LayoutInflater.from(weakReferenceContext.get())
                .inflate(R.layout.layout_toast, null, false)
            view.findViewById<TextView>(R.id.text).text = text
            view.findViewById<ImageView>(R.id.icon).setImageDrawable(icon)
            view.findViewById<ImageView>(R.id.icon).visibility = isIconShow
            toast.duration = duration
            toast.view = view
            toast.show()
        }
    }


    @JvmStatic
    fun cancel() {
        toast.cancel()
    }

    sealed class ToastType {
        object TIPS : ToastType()
        object WARNING : ToastType()
        object ERROR : ToastType()
    }

}