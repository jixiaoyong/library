package cf.android666.diywidget.view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cf.android666.applibrary.BasicRoundDialog
import cf.android666.applibrary.DpPxUtils

/**
 * author: jixiaoyong
 * email: jixiaoyong1995@gmail.com
 * website: https://jixiaoyong.github.io
 * date: 2019-06-06
 * description: todo
 */
class RoundDialogView(context: Context) : BasicRoundDialog(context, null, "Choose something") {
    override fun getContentView(): View {
        val view = RecyclerView(context)
        view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        view.adapter = object : RecyclerView.Adapter<VH>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(layoutInflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false))

            override fun getItemCount() = 300

            override fun onBindViewHolder(holder: VH, position: Int) {
                holder?.itemView?.findViewById<TextView>(android.R.id.text1)?.text = "Hello $position"
            }

        }
        view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DpPxUtils.dip2px(context, 300))
        return view
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView)

}