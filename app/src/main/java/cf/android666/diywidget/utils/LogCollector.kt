package cf.android666.diywidget.utils

import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.File
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*

/**
 * author: jixiaoyong
 * email: jixiaoyong1995@gmail.com
 * website: https://jixiaoyong.github.io
 * date: 2019-09-07
 * description: 日志收集管理类
 * 需要读写文件权限
 */
object LogCollector {

    private val TAG = "LogCollector"
    private var contextWeakReference: WeakReference<Context>? = null
    /**
     * log.config
     * 是否打印日志的配置文件
     * 内容结构如下，分别对应Android日志 debug，error，info，verbose，warn，wtf
     *   allowD = true
     *   allowE = true
     *   allowI = true
     *   allowV = true
     *   allowW = true
     *   allowWtf = true
     */
    private const val LOG_CONFIG_NAME = "log.config"
    private const val LOG_PATH_NAME = "log"
    @JvmField
    var TAG_LOG_FILE_NAME = ""

    private val LOG_CONFIG_DIR = Environment.getExternalStorageDirectory().absolutePath + File.separator

    /**
     * 初始化LogUtils
     * @param context Android Context
     * @param isDebug 是否为Debug模式（该模式下默认输出所有日志）
     */
    @JvmStatic
    @JvmOverloads
    fun initDebug(context: Context, isDebug: Boolean = false) {
        contextWeakReference = WeakReference(context)

        val logConfigFile = File(LOG_CONFIG_DIR + LOG_CONFIG_NAME)
        if (logConfigFile.exists() && logConfigFile.isFile) {
            checkLogConfig(logConfigFile)
        } else {
            updateLogConfig(allowD = isDebug, allowE = isDebug, allowI = isDebug, allowV = isDebug, allowW = isDebug, allowWtf = isDebug)
        }
    }

    private fun checkLogConfig(logConfigFile: File) {
        try {
            logConfigFile.readLines().map {
                val allowValue = it.substring(it.lastIndexOf("=") + 1)
                when {
                    it.contains("allowD", true) -> LogUtils.allowD = checkStringBoolean(allowValue)
                    it.contains("allowE", true) -> LogUtils.allowE = checkStringBoolean(allowValue)
                    it.contains("allowI", true) -> LogUtils.allowI = checkStringBoolean(allowValue)
                    it.contains("allowV", true) -> LogUtils.allowV = checkStringBoolean(allowValue)
                    it.contains("allowW", true) -> LogUtils.allowW = checkStringBoolean(allowValue)
                    it.contains("allowWtf", true) -> LogUtils.allowWtf = checkStringBoolean(allowValue)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "读取日志配置信息失败", e)
            updateLogConfig()
        }

    }

    @JvmStatic
    fun getContext() = contextWeakReference?.get()

    @JvmStatic
    var isSaveTagLog: Boolean = true

    @JvmStatic
    fun getLogTagPath(): String {
        var cachePath = contextWeakReference?.get()?.cacheDir?.absolutePath
        cachePath?.let {
            cachePath += File.separator + LOG_PATH_NAME
            val file = File(cachePath)
            if (file.isFile || !file.exists()) {
                file.mkdirs()
            }
        }
        return cachePath ?: ""
    }

    @JvmStatic
    fun getTagLogFileName(): String {
        TAG_LOG_FILE_NAME = LOG_PATH_NAME + getCurrentTimeFormatted() + ".log"
        return TAG_LOG_FILE_NAME
    }

    @JvmStatic
    @JvmOverloads
    fun updateLogConfig(allowD: Boolean = false, allowE: Boolean = false, allowI: Boolean = false,
                        allowV: Boolean = false, allowW: Boolean = false, allowWtf: Boolean = false) {
        LogUtils.allowD = allowD
        LogUtils.allowE = allowE
        LogUtils.allowI = allowI
        LogUtils.allowV = allowV
        LogUtils.allowW = allowW
        LogUtils.allowWtf = allowWtf
    }

    private fun checkStringBoolean(bool: String): Boolean {
        val lowBool = bool.trim().toLowerCase()
        return "true" == lowBool
    }

    private fun getCurrentTimeFormatted(): String {
        val date = Date()
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSSZ")
        return simpleDateFormat.format(date)
    }
}
