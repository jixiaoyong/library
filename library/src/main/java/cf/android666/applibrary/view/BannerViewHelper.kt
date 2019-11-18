package cf.android666.applibrary.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment

/**
 * author: jixiaoyong
 * email: jixiaoyong1995@gmail.com
 * website: https://jixiaoyong.github.io
 * date: 2019-11-18
 * description: todo
 */
object BannerViewHelper {

    fun initImageBannerOf(context: Context, size: Int = 0,
                          loadImageViewResource: (ImageView, Int) -> Unit): ArrayList<Fragment> {
        val fragmentList = arrayListOf<Fragment>()
        for (i in 0 until size) {
            val imageView = ImageView(context)
            loadImageViewResource.invoke(imageView, i)
            val fragment = ImageViewFragment()
            fragment.imageView = imageView
            fragmentList.add(fragment)
        }
        return fragmentList
    }


    class ImageViewFragment : androidx.fragment.app.Fragment() {

        var imageView: View? = null

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return if (imageView != null) imageView else super.onCreateView(inflater, container, savedInstanceState)
        }
    }
}