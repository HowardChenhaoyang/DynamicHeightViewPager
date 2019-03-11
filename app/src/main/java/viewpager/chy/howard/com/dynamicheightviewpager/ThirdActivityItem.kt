package viewpager.chy.howard.com.dynamicheightviewpager

import android.content.Context
import android.support.annotation.DrawableRes
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import viewpager.chy.howard.com.library.ComplicatedDynamicHeightViewPagerItem
import viewpager.chy.howard.com.library.DynamicHeightViewPagerItemInterface
import viewpager.chy.howard.com.library.DynamicHeightViewPagerView
import kotlin.random.Random

private val random = Random(System.currentTimeMillis())

class ThirdActivityItem(private val context: Context, private val position: Int, @DrawableRes private val imageId: Int) : ComplicatedDynamicHeightViewPagerItem {

    private var thirdActivityItemView: ThirdActivityItemView? = null

    private fun initThirdActivityItemView() {
        if (thirdActivityItemView == null) {
            thirdActivityItemView = ThirdActivityItemView(context, this)
            thirdActivityItemView!!.init(position){
                setImageResource(imageId)
            }
        }
    }

    override fun getOriginContentHeight(): Int {
        initThirdActivityItemView()
        return thirdActivityItemView!!.getOriginContentHeight()
    }

    override fun getResizeView(): View {
        initThirdActivityItemView()
        return thirdActivityItemView!!.getResizeView()
    }

    fun getItemView(): View {
        initThirdActivityItemView()
        return thirdActivityItemView!!
    }

    fun removeFromParent(parent: ViewGroup){
        parent.removeView(thirdActivityItemView)
        thirdActivityItemView = null
    }

    override fun onScaleChanged(scale: Float) {

    }
}

private class ThirdActivityItemView(
    context: Context,
    private val dynamicHeightViewPagerItemInterface: DynamicHeightViewPagerItemInterface
) : FrameLayout(context), DynamicHeightViewPagerView {

    private val frameLayout = FrameLayout(context)
    private val imageView = ImageView(context)
    private val textView = TextView(context)
    private val contentHeight: Int

    init {
        frameLayout.setBackgroundResource(R.color.primary_dark_material_dark)
        textView.gravity = Gravity.CENTER
        frameLayout.addView(
            imageView,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT,
                Gravity.TOP or Gravity.CENTER_HORIZONTAL
            )
        )
        frameLayout.addView(
            textView,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT,
                Gravity.CENTER
            )
        )

        contentHeight = random.nextInt(300, 2000)
        addView(
            frameLayout,
            FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, contentHeight)
        )
    }

    fun init(index: Int, invoke: ImageView.() -> Unit) {
        imageView.invoke()
        textView.text = "当前是第${index + 1}张图片"
    }

    fun getOriginContentHeight(): Int = contentHeight

    fun getResizeView(): View = frameLayout

    override fun getDynamicHeightViewPagerItemInterface() = dynamicHeightViewPagerItemInterface
}