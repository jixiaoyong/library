package cf.android666.diywidget.log

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import cf.android666.applibrary.logger.LogCollector
import cf.android666.applibrary.logger.Logger
import cf.android666.diywidget.R
import cf.android666.diywidget.utils.LogUtils
import pub.devrel.easypermissions.EasyPermissions

/**
 * author: jixiaoyong
 * email: jixiaoyong1995@gmail.com
 * website: https://jixiaoyong.github.io
 * date: 2019-09-07
 * description: 日志测试类
 */
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        EasyPermissions.requestPermissions(this, "read storage to save log",
                1, Manifest.permission.READ_EXTERNAL_STORAGE)

        Log.d("TTTTTTTTTTag", "开始请求权限")
        LogUtils.wtf("开始请求权限 cg")
//        Logger.d("kaishi 开始打印Logger")
        Logger.d("开始打印Logger 2")

        LogCollector.uploadLogToServer(false)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}