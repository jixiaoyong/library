package cf.android666.applibrary

import android.app.Application
import android.util.Log
import java.lang.ref.WeakReference

/**
 * author: jixiaoyong
 * email: jixiaoyong1995@gmail.com
 * website: www.jixiaoyong.github.io
 * date: 2019/1/19
 * description: 日志打印类
 */

object Logger {

    private var applicationWeakReference: WeakReference<Application>? = null

    private var isLog = true

    @JvmStatic
    fun init(application: Application, isLog: Boolean = true) {
        applicationWeakReference = WeakReference(application)
        this.isLog = isLog
    }

    @JvmStatic
    fun generateTag(): String {
        val stack = Thread.currentThread().stackTrace[4]
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
    fun d(any: Any) {
        if (isLog) {
            Log.d(generateTag(), any.toString())
        }
    }

    @JvmStatic
    fun e(any: Any, e: Exception? = null) {
        if (isLog) {
            if (e != null) {
                Log.e(generateTag(), any.toString(), e)
            } else {
                Log.e(generateTag(), any.toString())
            }
        }
    }

    @JvmStatic
    fun i(any: Any) {
        if (isLog) {
            Log.i(generateTag(), any.toString())
        }
    }
}