package viewpager.chy.howard.com.dynamicheightviewpager

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import viewpager.chy.howard.com.library.ComplicatedDynamicHeightViewPagerItem
import kotlin.random.Random

private val random = Random(System.currentTimeMillis())

class ThirdActivityItem : FrameLayout, ComplicatedDynamicHeightViewPagerItem {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

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

    override fun getOriginContentHeight(): Int = contentHeight

    override fun getResizeView(): View = frameLayout

    override fun onScaleChanged(scale: Float) {

    }
}