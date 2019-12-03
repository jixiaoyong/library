package cf.android666.applibrary.logger

import android.content.Context
import android.util.Log
import cf.android666.applibrary.utils.Md5Utils
import cn.leancloud.*
import java.io.File


/**
 *  Created by jixiaoyong1995@gmail.com
 *  Data: 2019/12/2.
 *  Description: 默认上传到Leancloud的日志上传工具
 *  如果要使用请如下准备
 *  1. 添加依赖
 *
//leancloud start
implementation ('cn.leancloud:storage-android:6.0.5'){
exclude group: 'com.alibaba', module: 'fastjson'
exclude group: 'org.ligboy.retrofit2', module: 'converter-fastjson'
}
implementation 'io.reactivex.rxjava2:rxandroid:2.1.1'
implementation 'com.alibaba:fastjson:1.1.71.android'
implementation 'org.ligboy.retrofit2:converter-fastjson-android:2.1.0'
//leancloud end

 *  2. 必须权限如下：
 *
<!-- 基本模块（必须）START -->
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<!-- 基本模块 END -->
 */
class DefaultLeancloudLogUploader(applicationContext: Context, leancloudInfo: LeancloudInfo,
                                  isLeancloudLogger: Boolean = false,
                                  var leancloudLogFileInfo: LeancloudLogFileInfo?) : FileUploader {



    init {
        AVOSCloud.initialize(applicationContext, leancloudInfo.appId, leancloudInfo.appKey, leancloudInfo.serverUrl)
        if (isLeancloudLogger) {
            AVOSCloud.setLogLevel(AVLogger.Level.DEBUG)
        }
        if (leancloudLogFileInfo == null) {
            leancloudLogFileInfo = LeancloudLogFileInfo()
        }
    }

    override fun uploadFileToServer(dirPath: String, logType: Int) {
        LoggerThreadPool.getSingleThreadPool().execute {
            val remoteLogFiles = try {
                val avQuery = AVQuery<AVObject>("logFileInfos")
                avQuery.orderByAscending("createdAt")
                avQuery.find()
            } catch (e: Exception) {
                e.printStackTrace()
                return@execute
            }

            val logDirFile = File(dirPath)
            if (logDirFile.exists() && logDirFile.isDirectory) {
                logDirFile.list().map {
                    val fileAbsPath = dirPath + File.separator + it
                    val fileMd5 = Md5Utils.getFileMd5HexString(File(fileAbsPath))
                    Pair<String, String>(it, fileMd5)
                }.filter { localFile ->
                    remoteLogFiles.find { remoteFile ->
                        remoteFile.get("logFileName") == localFile.first &&
                                remoteFile.get("logFileMd5") == localFile.second
                    } == null
                }.forEach { file ->
                    try {
                        Log.d("TAG", "uploadFileToServer() start upload file $file")
                        val fileAbsPath = dirPath + File.separator + file.first
                        val avFile = AVFile.withAbsoluteLocalPath(file.first, fileAbsPath)
                        avFile.saveInBackground().subscribe({}, {
                            Log.d("TAG", "failed to save file info to db $file")
                        }, {
                            Log.d("TAG", "start save file info to db $file")
                            val avFileInfo = leancloudLogFileInfo?.copy(
                                    logFileName = file.first,
                                    logFileMd5 = file.second,
                                    logFile = avFile,
                                    time = System.currentTimeMillis()
                            )?.parseToAvObject()
                            avFileInfo?.saveInBackground()
                        })
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    return@execute
                }
            }
        }
    }

    data class LeancloudInfo(var appId: String, var appKey: String, var serverUrl: String)

    data class LeancloudLogFileInfo(var logFileName: String = "", var logFileMd5: String = "", var logFile: AVFile? = null,
                                    var appName: String = "", var packageName: String = "", var appVersion: String = "",
                                    var appVersionCode: Int = 0, var flavor: String = "", var time: Long = System.currentTimeMillis()) {

        fun parseToAvObject(): AVObject {
            val logFileInfo = AVObject("logFileInfos")
            logFileInfo.put("logFileName", logFileName)
            logFileInfo.put("logFileMd5", logFileMd5)
            //一个 Array 属性
            logFileInfo.put("logFile", logFile)
            logFileInfo.put("appName", appName)
            logFileInfo.put("packageName", packageName)
            logFileInfo.put("appVersion", appVersion)
            logFileInfo.put("appVersionCode", appVersionCode)
            logFileInfo.put("flavor", flavor)
            logFileInfo.put("time", time)
            return logFileInfo
        }
    }
}