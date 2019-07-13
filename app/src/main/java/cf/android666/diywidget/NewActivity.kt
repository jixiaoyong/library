package cf.android666.diywidget

import android.app.Activity
import android.os.Bundle
import cf.android666.applibrary.Logger
import cf.android666.applibrary.ThumbnailUtils
import kotlinx.android.synthetic.main.new_activity.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import kotlin.concurrent.thread


/**
 * author: jixiaoyong
 * email: jixiaoyong1995@gmail.com
 * website: https://jixiaoyong.github.io
 * date: 2019-07-13
 * description: todo
 */
class NewActivity : Activity() {

    private val imagePath = "/sdcard/temp/image.jpg"
    private val P_READ_EXTERNAL_STORAGE = android.Manifest.permission.READ_EXTERNAL_STORAGE

    companion object {
        private const val RC_READ_EXTERNAL_STORAGE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_activity)

        if (!EasyPermissions.hasPermissions(this, P_READ_EXTERNAL_STORAGE)) {
            EasyPermissions.requestPermissions(this, "read file", RC_READ_EXTERNAL_STORAGE, P_READ_EXTERNAL_STORAGE)
        } else {
            imageView.post {
                readImageFromSdcard()
            }
        }

    }

    @AfterPermissionGranted(RC_READ_EXTERNAL_STORAGE)
    private fun readImageFromSdcard() {
        thread {
            val bitmap = ThumbnailUtils.getRoundThumbnailBitmap(imagePath, 50F, imageView.height, imageView.width)
            Logger.d("scaleBitmap size:${(bitmap?.byteCount ?: 0).toFloat() / (1024 * 1024)}Mb")
            runOnUiThread {
                imageView.setImageBitmap(bitmap)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}