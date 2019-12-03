package cf.android666.applibrary.logger

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Log
import java.io.PrintWriter
import java.io.StringWriter
import java.io.UnsupportedEncodingException
import java.io.Writer
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*

/**
 *  Created by jixiaoyong1995@gmail.com
 *  Data: 2019/12/2.
 *  Description:
 */
object LoggerHelper {

    const val TAG = "TAG"

    fun getVerName(c: Context): String? {
        val pm = c.packageManager
        var pi: PackageInfo? = null
        pi = try {
            pm.getPackageInfo(c.packageName, PackageManager.GET_ACTIVITIES)
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "Error while collect package info", e)
            e.printStackTrace()
            return "error"
        }
        return if (pi == null) {
            "error1"
        } else pi.versionName ?: return "not set"
    }

    fun getVerCode(c: Context): String? {
        val pm = c.packageManager
        var pi: PackageInfo? = null
        pi = try {
            pm.getPackageInfo(c.packageName, PackageManager.GET_ACTIVITIES)
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "Error while collect package info", e)
            e.printStackTrace()
            return "error"
        }
        if (pi == null) {
            return "error1"
        }
        val versionCode = pi.versionCode
        return versionCode.toString()
    }

    fun getMD5Str(str: String): String? {
        var messageDigest: MessageDigest? = null
        try {
            messageDigest = MessageDigest.getInstance("MD5")
            messageDigest.reset()
            messageDigest.update(str.toByteArray(charset("UTF-8")))
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            return ""
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            return ""
        }
        val byteArray = messageDigest.digest()
        val md5StrBuff = StringBuffer()
        for (i in byteArray.indices) {
            if (Integer.toHexString(0xFF and byteArray[i].toInt()).length == 1) md5StrBuff.append("0").append(Integer.toHexString(0xFF and byteArray[i].toInt())) else md5StrBuff.append(Integer.toHexString(0xFF and byteArray[i].toInt()))
        }
        return md5StrBuff.toString()
    }

    fun formatCrashInfo(ex: Throwable?): String? {
        val lineSeparator = "\r\n"
        val sb = StringBuilder()
        val logTime = "logTime:" + getCurrentTimeFormatted()
        val exception = "exception:$ex"
        val info: Writer = StringWriter()
        val printWriter = PrintWriter(info)
        ex?.printStackTrace(printWriter)
        val dump = info.toString()
        val crashMD5 = ("crashMD5:" + getMD5Str(dump))
        val crashDump = "crashDump:{$dump}"
        printWriter.close()
        sb.append("&start---").append(lineSeparator)
        sb.append(logTime).append(lineSeparator)
        sb.append(LogCollector.appVerName).append(lineSeparator)
        sb.append(LogCollector.appVerCode).append(lineSeparator)
        sb.append(LogCollector.OsVer).append(lineSeparator)
        sb.append(LogCollector.vendor).append(lineSeparator)
        sb.append(LogCollector.model).append(lineSeparator)
        sb.append(exception).append(lineSeparator)
        sb.append(crashMD5).append(lineSeparator)
        sb.append(crashDump).append(lineSeparator)
        sb.append("&end---").append(lineSeparator).append(lineSeparator)
                .append(lineSeparator)
        return sb.toString()
    }

    fun getCurrentTimeFormatted(): String {
        val timeFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
        return timeFormatter.format(System.currentTimeMillis())
    }


}