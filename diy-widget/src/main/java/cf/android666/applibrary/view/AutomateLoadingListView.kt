package cf.android666.applibrary.view

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ListView
import cf.android666.applibrary.R

/**
 * author: jixiaoyong
 * email: jixiaoyong1995@gmail.com
 * website: https://jixiaoyong.github.io
 * date: 2019-07-11
 * description: 自动加载数据的ListView
 *
 * 使用方法
val adapter = object : BaseAdapter() {
override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
var view = layoutInflater.inflate(android.R.layout.activity_list_item, parent, false)
view.findViewById<TextView>(android.R.id.text1).text = "TextView:$position"
return view
}

override fun getItem(position: Int) = null

override fun getItemId(position: Int) = position.toLong()

override fun getCount() = itemCount

}
listView.adapter = adapter
listView.loadingListener = object : AutomateLoadingListView.LoadingListener {
override fun getFootViewType(): AutomateLoadingListView.FootViewType {
Logger.d("itemCount:$itemCount")
return if (itemCount < 150) {
AutomateLoadingListView.FootViewType.LOADING
} else {
AutomateLoadingListView.FootViewType.NO_MORE
}
}

override fun loading() {
thread {
Thread.sleep((Math.random() * 3_000 + 3_000).toLong())
runOnUiThread {
itemCount += 10
listView.removeFootView()
}
}
}

}
 */
class AutomateLoadingListView : ListView {

    private val loadingFootView = LayoutInflater.from(context).inflate(R.layout.item_foot_loading, null, false)
    private val noMoreFootView = LayoutInflater.from(context).inflate(R.layout.item_foot_no_more, null, false)
    private var currentFootView: View? = null
    var loadingListener: LoadingListener? = null

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes)

    private var lastY = 0F

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        loadingListener?.let { listener ->
            when (ev?.action) {
                MotionEvent.ACTION_DOWN -> lastY = ev.rawY
                MotionEvent.ACTION_MOVE -> {
                    val dY = ev.rawY - lastY
                    if (dY < 0) {
                        //scroll up
                        if (lastVisiblePosition >= childCount && footerViewsCount <= 0) {
                            currentFootView = when (listener.getFootViewType()) {
                                FootViewType.LOADING -> {
                                    listener.loading()
                                    loadingFootView
                                }
                                FootViewType.NO_MORE -> noMoreFootView
                                FootViewType.EMPTY -> null
                            }
                            addFooterView(currentFootView)
                        }
                    } else if (currentFootView == noMoreFootView) {
                        removeFootView()
                    }
                }
            }
        }

        return super.onTouchEvent(ev)
    }

    fun removeFootView() {
        removeFooterView(currentFootView)
        currentFootView = null
    }

    interface LoadingListener {
        fun getFootViewType(): FootViewType
        fun loading()
    }

    sealed class FootViewType {
        object EMPTY : FootViewType()
        object LOADING : FootViewType()
        object NO_MORE : FootViewType()
    }
}