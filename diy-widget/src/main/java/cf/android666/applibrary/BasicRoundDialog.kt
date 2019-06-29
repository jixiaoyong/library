package cf.android666.applibrary

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView

/**
 *  Created by jixiaoyong1995@gmail.com
 *  Data: 2019/6/3.
 *  Description: BasicDialog
 */
abstract class BasicRoundDialog @JvmOverloads constructor(mContext: Context,
                                                          var onButtonClickListener: OnButtonClickListener? = null,
                                                          var title: String? = "") : Dialog(mContext) {

    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mView = layoutInflater.inflate(R.layout.dialog_basic_view, null, false)
        mView.findViewById<FrameLayout>(R.id.content_view).addView(getContentView())
        mView.findViewById<TextView>(R.id.dialog_title).text = title
        mView.findViewById<Button>(R.id.dialog_save).setOnClickListener { onButtonClickListener?.onConfirm(it) }
        mView.findViewById<Button>(R.id.dialog_cancel).setOnClickListener { onButtonClickListener?.onCancel(it) }
        setContentView(mView)

        setCanceledOnTouchOutside(false)

        //去掉dialog顶部蓝线 android4.x
        val dividerId = context.resources.getIdentifier("android:id/titleDivider", null, null)
        window?.decorView?.rootView?.findViewById<View>(dividerId)?.setBackgroundColor(Color.TRANSPARENT)

        val lp = window?.attributes
        val mD = window?.windowManager?.defaultDisplay
        val point = Point()
        mD?.getSize(point)

        //屏幕大小固定，不用考虑适配其他屏幕
        lp?.width = if (point.x != 0) (point.x * 0.6).toInt() else DpPxUtils.dip2px(context, 800)
//        lp?.height = if (point.y != 0) (point.y * 0.75).toInt() else 900
        window?.attributes = lp
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    abstract fun getContentView(): View

    interface OnButtonClickListener {
        fun onConfirm(view: View)
        fun onCancel(view: View)
    }

}

