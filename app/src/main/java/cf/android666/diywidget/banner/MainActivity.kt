package cf.android666.diywidget.banner

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cf.android666.applibrary.Logger
import cf.android666.applibrary.view.BannerViewHelper
import cf.android666.diywidget.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_banner_main.*

/**
 * author: jixiaoyong
 * email: jixiaoyong1995@gmail.com
 * website: https://jixiaoyong.github.io
 * date: 2019-11-18
 * description: todo
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banner_main)
        val imgs = arrayOf("https://www.wanandroid.com/blogimgs/4f70771f-2d7a-4494-b9fd-0d11eca0bd6e.png",
                "https://www.wanandroid.com/blogimgs/90c6cc12-742e-4c9f-b318-b912f163b8d0.png",
                "https://www.wanandroid.com/blogimgs/62c1bd68-b5f3-4a3c-a649-7ca8c7dfabe6.png")


        val fragments = BannerViewHelper.initImageBannerOf(this, imgs.size) { imageView, i ->
            val imgUrl = imgs[i]
            Glide.with(imageView).load(imgUrl).into(imageView)
            Logger.d("start load image:$imgUrl")
        }


        bannerView.setViewsAndIndicator(supportFragmentManager, fragments)
        bannerView.viewPager.setOnClickListener {
            Logger.d("viewPager:${it}")
        }
    }
}