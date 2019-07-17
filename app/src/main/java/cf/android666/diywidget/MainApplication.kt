package cf.android666.diywidget

import android.app.Application
import cf.android666.applibrary.Logger
import cf.android666.applibrary.view.Toast

/**
 * author: jixiaoyong
 * email: jixiaoyong1995@gmail.com
 * website: https://jixiaoyong.github.io
 * date: 2019-07-13
 * description: todo
 */
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Logger.isLog = BuildConfig.DEBUG
        Toast.init(this)
    }
}
