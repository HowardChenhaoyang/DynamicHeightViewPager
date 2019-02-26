package viewpager.chy.howard.com.library

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView

class ComplicatedDynamicHeightViewPagerItemView : FrameLayout, ComplicatedDynamicHeightViewPagerItem {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val imageView: ImageView = ImageView(context).apply { scaleType = ImageView.ScaleType.CENTER_CROP }

    init {
        addView(
            imageView,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )
    }

    fun init(init: ImageView.() -> Unit) {
        imageView.init()
    }

    override fun getOriginContentHeight(): Int = imageView.drawable.intrinsicHeight

    override fun getResizeView() = imageView

    override fun onScaleChanged(scale: Float) {

    }
}