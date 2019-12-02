package cf.android666.diywidget.log

import android.app.Application
import cf.android666.applibrary.logger.DefaultLeancloudLogUploader
import cf.android666.diywidget.BuildConfig
import cf.android666.diywidget.utils.LogCollector
import cf.android666.diywidget.utils.LogUtils

/**
 * author: jixiaoyong
 * email: jixiaoyong1995@gmail.com
 * website: https://jixiaoyong.github.io
 * date: 2019-09-07
 * description: todo
 */
class MainApp : Application() {


    var leancloudAppId = "eiCorogUwe1OdpPG65oSWEsP-MdYXbMMI"
    var leancloudAppKey = "glorq3CiX4j8RNIWf7kq5LBW"
    var leancloudServerURL = "https://api.xiaoyong.ml"

    override fun onCreate() {
        super.onCreate()
//        LogCollector.initDebug(this)
        LogCollector.initDebug(this, BuildConfig.DEBUG)
        LogUtils.d("init method()")
        LogUtils.wtf("wwwttf")

        cf.android666.applibrary.logger.LogCollector.init(this, isSaveToFile = true,
                logUploader = DefaultLeancloudLogUploader(this, DefaultLeancloudLogUploader.LeancloudInfo(
                        leancloudAppId, leancloudAppKey, leancloudServerURL
                )))

    }
}