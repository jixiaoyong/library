package cf.android666.applibrary

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

/**
 *  Created by jixiaoyong1995@gmail.com
 *  Data: 2019/1/10.
 *  Description:
 */
object NetworkUtil {

    fun isNetworkAvalible(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } else {
            try {
                val p = Runtime.getRuntime().exec("ping -c 3 www.baidu.com")
                val res = p.waitFor()
                res == 0
            } catch (e: Exception) {
                false
            }
        }

    }
}