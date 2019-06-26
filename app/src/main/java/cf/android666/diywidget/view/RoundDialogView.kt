package cf.android666.diywidget.view

import android.content.Context
import android.view.View
import android.widget.TextView
import cf.android666.applibrary.BasicDialog

/**
 * author: jixiaoyong
 * email: jixiaoyong1995@gmail.com
 * website: https://jixiaoyong.github.io
 * date: 2019-06-06
 * description: todo
 */
class RoundDialogView(context: Context, themeResId: Int = 0) : BasicDialog(context, themeResId) {

    override fun getContextView(): View? {
        val text = TextView(context)
        text.text = "TTTTTTTTTTTT"
        return text
    }

}