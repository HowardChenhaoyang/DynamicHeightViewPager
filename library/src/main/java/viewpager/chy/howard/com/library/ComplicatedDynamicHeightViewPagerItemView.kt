package viewpager.chy.howard.com.library

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView

class ComplicatedDynamicHeightViewPagerItemView(private val context: Context, private val init: ImageView.() -> Unit) : ComplicatedDynamicHeightViewPagerItem {

    private var itemView: SingleImageItemView? = null

    private fun initItemView(){
        if (itemView == null) {
            itemView = SingleImageItemView(context)
            itemView!!.init(init)
            itemView!!.tag = this
        }
    }

    override fun getOriginContentHeight(): Int {
        initItemView()
        return itemView!!.getOriginContentHeight()
    }

    override fun getResizeView(): View {
        initItemView()
        return itemView!!.getResizeView()
    }

    override fun onScaleChanged(scale: Float) {

    }

    fun getItemView():View{
        initItemView()
        return itemView!!
    }

    fun removeViewFromParent(container: ViewGroup) {
        container.removeView(itemView)
        itemView = null
    }
}

private class SingleImageItemView : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val imageView: ImageView = ImageView(context).apply { scaleType = ImageView.ScaleType.CENTER_CROP }

    fun getOriginContentHeight() = imageView.drawable.intrinsicHeight

    fun init(init: ImageView.() -> Unit) {
        imageView.init()
    }

    fun getResizeView() = imageView

    init {
        addView(
            imageView,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )
    }
}