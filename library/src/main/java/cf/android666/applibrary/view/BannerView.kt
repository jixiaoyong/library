package cf.android666.applibrary.view

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import cf.android666.applibrary.R
import kotlinx.android.synthetic.main.view_banner.view.*

/**
 * author: jixiaoyong
 * email: jixiaoyong1995@gmail.com
 * website: https://jixiaoyong.github.io
 * date: 2019-11-18
 * description: todo
 */
class BannerView : RelativeLayout {

    private var indicatorType: Int = 0
    lateinit var viewPager: androidx.viewpager.widget.ViewPager
    private lateinit var indicatorLayout: FrameLayout

    val startItemIndex = Int.MAX_VALUE / 2

    @JvmOverloads
    constructor (context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr) {
        initView(context, attrs, defStyleAttr)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor (context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        initView(context, attrs, defStyleAttr, defStyleRes)
    }

    private fun initView(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int = 0) {
        val view = LayoutInflater.from(context).inflate(R.layout.view_banner, this, true)
        viewPager = view.viewPager
        indicatorLayout = view.bottomView
        val a = context.obtainStyledAttributes(attrs, R.styleable.BannerView, defStyleAttr, defStyleRes)
        indicatorType = a.getInt(R.styleable.BannerView_indicatorType, IndicatorType.POINT)
        a.recycle()
    }

    fun setViewsAndIndicator(fragmentMng: FragmentManager, fragments: List<Fragment>, indicatorValue: Array<String>? = null) {
        val newFragment = fragments.toMutableList()
        val lastFragment = fragments.last()
        val firstFragment = fragments.first()
        val prefixFragment = BannerViewHelper.ImageViewFragment.getFragment(lastFragment.view)
        val suffixFragment = BannerViewHelper.ImageViewFragment.getFragment(firstFragment.view)
        newFragment.add(0, prefixFragment)
        newFragment.add(suffixFragment)
        viewPager.adapter = VpAdapter(fragmentMng, newFragment)
        viewPager.currentItem = 1
        viewPager.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                val realIndex = when (position) {
                    newFragment.size - 1 -> 1
                    0 -> newFragment.size - 2
                    else -> position
                }

                viewPager.setCurrentItem(realIndex, false)
                onIndicatorSelected(realIndex)
            }

        })
        setupIndicator(fragments.size, indicatorValue)
    }

    open fun onIndicatorSelected(position: Int) {
        if (indicatorType == 1) {
            val indicatorView = indicatorLayout.getChildAt(0) as LinearLayout
            for (i in 0 until indicatorView.childCount) {
                val imgView = indicatorView.getChildAt(i) as ImageView
                imgView.setBackgroundResource(if (i == position) R.drawable.ic_point_enable else R.drawable.ic_point_disable)
            }
        }
    }

    private fun setupIndicator(size: Int, indicatorValue: Array<String>?) {
        if (indicatorType == 1) {
            val indicatorView = LayoutInflater.from(context).inflate(R.layout.layout_banner_indicator, null, false) as LinearLayout
            for (i in 0 until size) {
                val imageView = LayoutInflater.from(context).inflate(R.layout.view_banner_indicator_point, null, false)
                imageView.setBackgroundResource(if (i == 0) R.drawable.ic_point_enable else R.drawable.ic_point_disable)
                indicatorView.addView(imageView)
            }
            indicatorLayout.addView(indicatorView)
        }
    }

    inner class VpAdapter(fragmentManager: FragmentManager, private val fragments: List<Fragment>)
        : androidx.fragment.app.FragmentPagerAdapter(fragmentManager) {

        override fun getItem(position: Int): Fragment {
//            val realIndex = abs(position - startItemIndex) % fragments.size
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

    }

    companion object {
        object IndicatorType {
            const val POINT = 0
            const val TEXT = 1
            const val NONE = -1
        }
    }
}