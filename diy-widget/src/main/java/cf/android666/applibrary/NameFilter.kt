package cf.android666.applibrary

//package cf.android666.applibrary.temp
//
//import android.widget.BaseAdapter
//import android.widget.Filter
//import com.github.promeg.pinyinhelper.Pinyin
//
///**
// *  Created by jixiaoyong1995@gmail.com
// *  Data: 2019/6/18.
// *  Description: 按照给定字段匹配对应数据
// */
//class NameFilter(var adapter: BaseAdapter, private val list: ArrayList<String>) : Filter() {
//    var copyList: ArrayList<String> = list.clone() as ArrayList<String>
//    private val regexAllWord = "[a-zA-Z]*"
//
//    override fun performFiltering(constraint: CharSequence?): FilterResults {
//        val searchContent = Pinyin.toPinyin(constraint?.toString()
//                ?: "", regexAllWord) + regexAllWord
//        val filterResult = FilterResults()
//        if (searchContent.isNullOrEmpty()) {
//            filterResult.values = copyList
//        } else {
//            val resultList = arrayListOf<String>()
//            copyList.map {
//                if (Pinyin.toPinyin(it, "").toLowerCase()
//                                .contains(Regex(searchContent, RegexOption.IGNORE_CASE))) {
//                    resultList.add(it)
//                }
//            }
//
//            filterResult.values = resultList
//        }
//        return filterResult
//    }
//
//    override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//        val result: ArrayList<String> =
//                if (results?.values != null) results.values as ArrayList<String> else arrayListOf()
//        list.clear()
//        list.addAll(result)
//        if (result.isNullOrEmpty()) {
//            adapter.notifyDataSetInvalidated()
//        } else {
//            adapter.notifyDataSetChanged()
//        }
//    }
//
//}
