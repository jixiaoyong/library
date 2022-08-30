package cf.android666.diywidget.banner

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import cf.android666.applibrary.view.BannerView
import cf.android666.applibrary.view.BannerViewHelper
import cf.android666.diywidget.NewActivity
import cf.android666.diywidget.R
import com.bumptech.glide.Glide

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
        val imgs = arrayOf(
            "https://www.wanandroid.com/blogimgs/4f70771f-2d7a-4494-b9fd-0d11eca0bd6e.png",
            "https://www.wanandroid.com/blogimgs/90c6cc12-742e-4c9f-b318-b912f163b8d0.png",
            "https://www.wanandroid.com/blogimgs/62c1bd68-b5f3-4a3c-a649-7ca8c7dfabe6.png"
        )

        val indicatorDescList = arrayListOf(
            "在自定义控件里面检测当前view是否被遮住显示不全", "View作为参数去判断显示情况"
        )

        val bannerView = findViewById<BannerView>(R.id.bannerView)
        val button = findViewById<Button>(R.id.button)

        BannerViewHelper.Builder(bannerView)
            .setFragmentManager(supportFragmentManager)
            .addFragments(BannerViewHelper.initImageBannerOf(imgs.size) { imageView, i ->
                val imgUrl = imgs[i]
                Glide.with(imageView).load(imgUrl).centerCrop().into(imageView)
            })
            .addIndicatorDesc(indicatorDescList)
            .build()


        button.setOnClickListener {
            startActivity(Intent(this@MainActivity, NewActivity::class.java))
        }
    }
}