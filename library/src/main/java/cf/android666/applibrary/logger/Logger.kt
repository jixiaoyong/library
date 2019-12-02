package cf.android666.applibrary.logger

import android.app.Application
import android.util.Log
import cf.android666.applibrary.logger.LogCollector.CURRENT_LOG_FILE_PATH
import java.io.File
import java.lang.ref.WeakReference

/**
 * author: jixiaoyong
 * email: jixiaoyong1995@gmail.com
 * website: www.jixiaoyong.github.io
 * date: 2019/1/19
 * description: todo
 */

object Logger {

    @JvmStatic
    var isLog = true
    @JvmStatic
    var isNeedSaveToFile = false

    var defTag: String? = null

    /**
     * 当前日志文件名称
     */
    private var CURRENT_LOG_FILE_NAME = ""


    /**
     * 日志文件最多个数
     */
    private const val MAX_LOG_FILE_COUNT: Int = 10
    /**
     * 单个日志文件最大大小
     */
    private const val SINGLE_LOG_FILE_MAX_SIZE: Long = 1 * 1024 * 1024 //1Mb

    private var applicationWeakReference: WeakReference<Application>? = null

    @JvmStatic
    fun init(application: Application, isLog: Boolean = true) {
        applicationWeakReference = WeakReference(application)
        this.isLog = isLog
    }

    @JvmStatic
    fun generateTag(): String {
        if (!defTag.isNullOrBlank()) {
            return defTag as String
        }
        val stack = Thread.currentThread().stackTrace[7]
        var tag = "${stack.className}.${stack.methodName}(Line:${stack.lineNumber})"
        if (tag.length > 87) {
            //TAG长度超过87左右就会打印不正常
            var applicationId = applicationWeakReference?.get()?.applicationInfo?.packageName
            applicationId = if (applicationId == null) "" else "$applicationId/"
            tag = "${applicationId}${stack.fileName}.${stack.methodName}(Line:${stack.lineNumber})"
            if (tag.length > 87) {
                tag = "${applicationId}${stack.fileName} (Line:${stack.lineNumber})"
            }
        }
        return tag
    }

    @JvmStatic
    fun d(any: Any, tag: String? = null) {
        printLog(Log.DEBUG, any.toString(), tag)
    }

    @JvmStatic
    fun e(any: Any, e: Exception? = null, tag: String? = null) {
        printLog(Log.ERROR, any.toString(), tag, e)
    }

    @JvmStatic
    fun i(any: Any) {
        printLog(Log.INFO, any.toString())
    }

    /**
     * 输出日志到控制台、文件（可选）
     */
    private fun printLog(type: Int, msg: String, tag: String? = null, err: java.lang.Exception? = null) {
        if (!isLog) {
            return
        }
        val _tag = if (!tag.isNullOrBlank()) tag else generateTag()

        when (type) {
            Log.DEBUG -> {
                Log.d(_tag, msg)
            }
            Log.ERROR -> {
                if (err != null) {
                    Log.e(_tag, msg, err)
                } else {
                    Log.e(_tag, msg)
                }
            }
            Log.INFO -> {
                Log.i(_tag, msg)
            }
            Log.WARN -> {
                Log.w(_tag, msg)
            }
        }

        if (isNeedSaveToFile) {
            val currentTime = LoggerHelper.getCurrentTimeFormatted()
            var logContent = "$currentTime $tag $msg\n"
            if (err != null) {
                logContent += "${LoggerHelper.formatCrashInfo(err)}\n"
            }
            Log.d("TAG", "start write to log file:$msg")
            saveLogToFile(logContent)
        }
    }


    fun saveLogToFile(logContent: String, logDirPath: String = CURRENT_LOG_FILE_PATH) {
        LoggerThreadPool.getSingleThreadPool().execute {
            Log.d("TAG", "start write to path: $CURRENT_LOG_FILE_PATH")

            //1. check is path exits
            val logPath = File(logDirPath)
            if (!logPath.exists() || logPath.isFile) {
                logPath.mkdirs()
            }
            //2. is there too much log files?
            val logFiles = logPath.listFiles()
            if (logFiles.size > MAX_LOG_FILE_COUNT) {
                logFiles.sortBy {
                    it.lastModified()
                }
                logFiles.last().delete()
            }
            //3. check is log file name available
            var logFileName = getFileName(logDirPath)
            Log.d("TAG", "start try to write to log file: $logFileName")
            var logFile = getOrCreateFile(logFileName)
            //4. check is the log file to big,todo need to check again?
            if (logFile.length() >= SINGLE_LOG_FILE_MAX_SIZE) {
                CURRENT_LOG_FILE_NAME = ""
                logFileName = getFileName(logDirPath)
                logFile = getOrCreateFile(logFileName)
            }

            Log.d("TAG", "start write to log file:$logFileName $logContent")
            //5. write log to the file
            logFile.appendText(logContent)
            Log.d("TAG", "finish write to log file:$logFileName $logContent")
        }
    }

    private fun getFileName(logDirPath: String): String {
        CURRENT_LOG_FILE_NAME =
                if (CURRENT_LOG_FILE_NAME.isBlank()) "log-${LoggerHelper.getCurrentTimeFormatted()}.log"
                        .replace(" ", "-").replace(":", "-")
                else CURRENT_LOG_FILE_NAME
        return logDirPath + File.separator + CURRENT_LOG_FILE_NAME
    }

    private fun getOrCreateFile(logFileName: String): File {
        val logFile = File(logFileName)
        if (!logFile.exists() || logFile.isDirectory) {
            logFile.createNewFile()
        }
        return logFile
    }


}