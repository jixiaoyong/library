package cf.android666.applibrary.logger

import android.content.Context
import android.os.Build
import android.os.Environment
import java.io.File

/**
 *  Created by jixiaoyong1995@gmail.com
 *  Data: 2019/12/2.
 *  Description:
 */
object LogCollector {

    var CURRENT_LOG_FILE_PATH: String = File.separator + "log"
    var CURRENT_ERROR_FILE_PATH = File.separator + "error"


    var appVerName = "appVerName:"
    var appVerCode = "appVerCode:"
    val OsVer = "OsVer:" + Build.VERSION.RELEASE
    val vendor = "vendor:" + Build.MANUFACTURER
    val model = "model:" + Build.MODEL


    private var logUploader: FileUploader? = null
    var isOnlyError: Boolean = true
    var isDeleteLogFileAfterUploaded = true
    var mDefaultUncaughtExceptionHandler: Thread.UncaughtExceptionHandler? = null


    fun init(applicationContext: Context, isLog: Boolean = true, isSaveToFile: Boolean = false, defTag: String? = null,
             logUploader: FileUploader? = null,
             defaultUncaughtExceptionHandler: Thread.UncaughtExceptionHandler = DefaultUncaughtExceptionHandler()) {
        Logger.isLog = isLog
        Logger.isNeedSaveToFile = isSaveToFile
        Logger.defTag = defTag
        this.logUploader = logUploader

        appVerName = "appVerName:" + LoggerHelper.getVerName(applicationContext)
        appVerCode = "appVerCode:" + LoggerHelper.getVerCode(applicationContext)

        val LOG_ROOT_PATH = "${Environment.getExternalStorageDirectory().absolutePath}/Android/data/${applicationContext.packageName}"

        CURRENT_LOG_FILE_PATH = "$LOG_ROOT_PATH/log"
        CURRENT_ERROR_FILE_PATH = "$LOG_ROOT_PATH/error"

        mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(defaultUncaughtExceptionHandler)
    }

    /**
     * 上传日志到服务器
     */
    fun uploadLogToServer(isOnlyErrorFile: Boolean = isOnlyError) {
        logUploader?.let {
            if (!isOnlyErrorFile) {
                it.uploadFileToServer(CURRENT_LOG_FILE_PATH)
            }
            it.uploadFileToServer(CURRENT_ERROR_FILE_PATH, LoggerType.ERROR)
        }
    }

    object LoggerType {
        const val LOG = 0
        const val ERROR = 1
    }
}