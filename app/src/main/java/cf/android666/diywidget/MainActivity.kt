package cf.android666.diywidget

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.ToggleButton
import cf.android666.applibrary.Logger
import cf.android666.applibrary.view.AutomateLoadingListView
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

/**
 * Created by jixiaoyong on 2018/2/18.
 */

class MainActivity : Activity() {

    private val toggleButton: ToggleButton? = null
    private var itemCount = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

    }

}
