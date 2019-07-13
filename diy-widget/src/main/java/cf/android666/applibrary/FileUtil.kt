package cf.android666.applibrary

/**
 *  Created by jixiaoyong1995@gmail.com
 *  Data: 2019/7/13.
 *  检查文件类型
 */
object FileUtil {

    private val supportImageType = arrayListOf("png", "jpg", "bmp", "jpeg")
    private val supportVideoType = arrayListOf("mp4", "rmvb", "3gp")

    fun checkImageFile(path: String): Boolean {
        return isSupport(path, supportImageType)
    }

    fun checkVideoFile(path: String): Boolean {
        return isSupport(path, supportVideoType)
    }

    private fun isSupport(path: String, arrayList: ArrayList<String>): Boolean {
        arrayList.map {
            if (path.endsWith(it, true)) return true
        }
        return false
    }

}
