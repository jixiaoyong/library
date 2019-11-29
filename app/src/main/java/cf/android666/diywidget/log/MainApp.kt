package cf.android666.diywidget.log

import android.app.Application
import cf.android666.applibrary.Logger
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

    override fun onCreate() {
        super.onCreate()
//        LogCollector.initDebug(this)
        LogCollector.initDebug(this, BuildConfig.DEBUG)
        LogUtils.d("init method()")
        LogUtils.wtf("wwwttf")
        Logger.init(this)
    }
}