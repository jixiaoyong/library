package cf.android666.diywidget.scalelist

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView
import cf.android666.applibrary.ImmersiveUtils
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
class ScaleHorizontalActivity2 : Activity() {

    private val mItemCount: Int = 100

    private var orientation = LinearLayoutManager.HORIZONTAL
    private val hasDecorationMap: MutableMap<Int, String> = mutableMapOf()
    private val datas: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scale2)

        ImmersiveUtils.setTransparentStateBar(window)

        initData()

        val adapter: RecyclerView.Adapter<RecyclerView.ViewHolder> = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = RoundDialogView.VH(
                    layoutInflater.inflate(R.layout.item_scale_view2, parent, false))

            override fun getItemCount() = datas.size

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                holder?.itemView?.findViewById<TextView>(R.id.textview)?.text = datas[position]
            }

        }
        recycler.adapter = adapter
        recycler.layoutManager = ScaleLinearLayoutManager2(this, orientation, false)
//        LinearSnapHelper().attachToRecyclerView(recycler)
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
            datas.add(wuDiString.replace("N", num, true) + "\n$x")
        }
        datas.mapIndexed { index, s ->
            if (!hasDecorationMap.containsValue(s)) {
                hasDecorationMap[index] = s
            }
        }
    }
}