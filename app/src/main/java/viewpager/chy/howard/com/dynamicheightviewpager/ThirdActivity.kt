package viewpager.chy.howard.com.dynamicheightviewpager

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_third.*

class ThirdActivity : AppCompatActivity() {

    private val imageIds =
        intArrayOf(
            R.drawable.image01,
            R.drawable.image02,
            R.drawable.image03,
            R.drawable.image04,
            R.drawable.image05,
            R.drawable.image06,
            R.drawable.image07,
            R.drawable.image08,
            R.drawable.image09,
            R.drawable.image10,
            R.drawable.image11,
            R.drawable.image12
        )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)
        val dynamicHeightItemViews = imageIds.mapIndexed { index, imageId ->
            createThirdItemView(index, imageId)
        }
        dynamicHeightViewPager.adapter = object : PagerAdapter() {
            override fun isViewFromObject(p0: View, p1: Any): Boolean {
                return p0 == p1
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val dynamicHeightItemView = dynamicHeightItemViews[position]
                container.addView(dynamicHeightItemView.getItemView())
                return dynamicHeightItemView.getItemView()
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                dynamicHeightItemViews[position].removeFromParent(container)
            }

            override fun getCount(): Int = dynamicHeightItemViews.size
        }
        dynamicHeightViewPager.init(dynamicHeightItemViews = dynamicHeightItemViews)
        dynamicHeightViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
                textView.text = "${p0 + 1}/${imageIds.size}"
            }
        })
        textView.text = "0/${imageIds.size}"
    }

    private fun createThirdItemView(position: Int, @DrawableRes imageId: Int) =
        ThirdActivityItem(this, position, imageId)

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, ThirdActivity::class.java))
        }
    }
}