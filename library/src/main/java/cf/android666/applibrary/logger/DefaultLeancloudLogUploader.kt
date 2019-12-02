package cf.android666.applibrary.logger

import android.content.Context
import android.util.Log
import cn.leancloud.AVFile
import cn.leancloud.AVLogger
import cn.leancloud.AVOSCloud
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
                                  isLeancloudLogger: Boolean = false) : FileUploader {

    init {
        AVOSCloud.initialize(applicationContext, leancloudInfo.appId, leancloudInfo.appKey, leancloudInfo.serverUrl)
        if (isLeancloudLogger) {
            AVOSCloud.setLogLevel(AVLogger.Level.DEBUG)
        }
    }

    override fun uploadFileToServer(dirPath: String, logType: Int) {
        LoggerThreadPool.getSingleThreadPool().execute {
            val logDirFile = File(dirPath)
            if (logDirFile.exists() && logDirFile.isDirectory) {
                logDirFile.list().forEach {
                    Log.d("TAG", "start upload file $it")
                    val avFile = AVFile.withAbsoluteLocalPath(it, dirPath + File.separator + it)
                    avFile.saveInBackground().subscribe({},{},{
                        Log.d("TAG", "finish upload file $it")
                    })
                }
            }
        }
    }

    data class LeancloudInfo(var appId: String, var appKey: String, var serverUrl: String)
}