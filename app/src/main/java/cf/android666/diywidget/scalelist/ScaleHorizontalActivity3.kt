package cf.android666.diywidget.scalelist

import android.app.Activity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import cf.android666.applibrary.utils.ImmersiveUtils
import cf.android666.diywidget.R
import cf.android666.diywidget.view.RoundDialogView
import kotlinx.android.synthetic.main.activity_scale.*

/**
 * author: jixiaoyong
 * email: jixiaoyong1995@gmail.com
 * website: https://jixiaoyong.github.io
 * date: 2019-06-29
 * description: todo
 */
class ScaleHorizontalActivity3 : Activity() {

    private var shouldScroll: Boolean = false
    private var shouldScrollIndex: Int = 0
    private var selectCategorys = ""

    private val mItemCount: Int = 300

    private var orientation = androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
    private val hasDecorationMap: MutableMap<Int, String> = mutableMapOf()
    private val datas: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scale)

        ImmersiveUtils.setTransparentStateBar(window)

        initData()

        recycler.layoutManager = LinearLayoutManager(this, orientation, false)
        recycler.adapter = object : androidx.recyclerview.widget.RecyclerView.Adapter<RoundDialogView.VH>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoundDialogView.VH = RoundDialogView.VH(
                    layoutInflater.inflate(R.layout.item_scale_view, parent, false))

            override fun getItemCount() = mItemCount

            override fun onBindViewHolder(holder: RoundDialogView.VH, position: Int) {
                holder.itemView.findViewById<TextView>(R.id.textview)?.text = "Hello $position"
            }

        }
//        LinearSnapHelper().attachToRecyclerView(recycler)
        recycler.addItemDecoration(object : DiyDividerItemDecoration(this) {
            override fun shouldHaveDecoration(position: Int): Boolean {
                return when (position) {
                    0 -> true
                    else -> (datas[position - 1] != datas[position])
                }
            }

            override fun getTitle(position: Int) = datas[position]

        })
        recycler.setOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recycler.layoutManager as androidx.recyclerview.widget.LinearLayoutManager
                val first = layoutManager.findFirstVisibleItemPosition()
                selectCategorys = datas[first]
                listMenu.adapter?.notifyDataSetChanged()

                if (shouldScroll) {
                    shouldScroll = false
                    val currentChild = layoutManager.getChildAt(shouldScrollIndex - first) ?: return
                    val offset = currentChild.top
                    recycler.smoothScrollBy(0, offset)
                }
            }
        })


        listMenu.layoutManager = LinearLayoutManager(this, orientation, false)
        listMenu.adapter = object : androidx.recyclerview.widget.RecyclerView.Adapter<RoundDialogView.VH>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoundDialogView.VH = RoundDialogView.VH(
                    layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false))

            override fun getItemCount() = hasDecorationMap.size

            override fun onBindViewHolder(holder: RoundDialogView.VH, position: Int) {
                holder.itemView.findViewById<TextView>(android.R.id.text1)?.text =
                        hasDecorationMap[hasDecorationMap.keys.toIntArray()[position]]
                holder.itemView.setOnClickListener {
                    val key = hasDecorationMap.keys.toIntArray()[position]
                    smoothScrollToTop(recycler, key)
                }
            }

        }


    }

    private fun smoothScrollToTop(recycler: androidx.recyclerview.widget.RecyclerView, index: Int) {
        shouldScrollIndex = index
        val layoutManager = recycler.layoutManager as androidx.recyclerview.widget.LinearLayoutManager
        val first = layoutManager.findFirstVisibleItemPosition()
        val last = layoutManager.findLastVisibleItemPosition()

        when {
            index < first -> recycler.smoothScrollToPosition(index)
            index < last -> {
                val currentChild = layoutManager.getChildAt(index - first) ?: return
                val offset = currentChild.top
                recycler.smoothScrollBy(0, offset)
            }
            else -> {
                shouldScroll = true
                recycler.scrollToPosition(index)
            }
        }
//直接滑动index到顶部        layoutManager.scrollToPositionWithOffset(index,0)
    }

    private val wuDiString = "宇宙第N无敌帅"
    private fun initData() {
        for (x in 0..mItemCount) {
            val num = when {
                x <= 6 -> 1
                x <= 36 -> 2
                x <= 53 -> 3
                x <= 66 -> 4
                x <= 69 -> 5
                else -> 6
            }.toString()
            datas.add(wuDiString.replace("N", num, true))
        }
        datas.mapIndexed { index, s ->
            if (!hasDecorationMap.containsValue(s)) {
                hasDecorationMap[index] = s
            }
        }
    }
}