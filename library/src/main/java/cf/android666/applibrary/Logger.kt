package cf.android666.applibrary

import android.util.Log

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
    fun generateTag(): String {
        val stack = Thread.currentThread().stackTrace[4]
        return "${stack.className}.${stack.methodName}(Line:${stack.lineNumber})"
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