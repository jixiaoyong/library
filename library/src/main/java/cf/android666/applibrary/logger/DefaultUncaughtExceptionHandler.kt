package cf.android666.applibrary.logger

import android.os.Process
import android.util.Log
import cf.android666.applibrary.logger.LogCollector.CURRENT_ERROR_FILE_PATH

/**
 *  Created by jixiaoyong1995@gmail.com
 *  Data: 2019/12/2.
 *  Description:
 */
class DefaultUncaughtExceptionHandler : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(t: Thread?, e: Throwable?) {
        val logContent = "Thread(${t?.name}-${t?.id})\n${LoggerHelper.formatCrashInfo(e)}"
        Log.d("TAG", "Thread(${t?.name}-${t?.id}) UncaughtException,start save log to file")
        Logger.saveLogToFile(logContent, CURRENT_ERROR_FILE_PATH)
        LogCollector.uploadLogToServer(true)
        if (LogCollector.mDefaultUncaughtExceptionHandler != null) {
            LogCollector.mDefaultUncaughtExceptionHandler?.uncaughtException(t, e)
        } else {
            Log.e("UncaughtException", "Uncaugth exception, exit!", e)
            Process.killProcess(Process.myPid())
        }
    }


}