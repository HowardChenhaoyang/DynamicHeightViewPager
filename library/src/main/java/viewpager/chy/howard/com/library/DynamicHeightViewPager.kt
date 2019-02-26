package viewpager.chy.howard.com.library

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

interface DynamicHeightViewPagerItemInterface {
    fun getOriginContentHeight(): Int
    fun getResizeView(): View
    fun onScaleChanged(scale: Float)
    fun resize(viewPager: ViewPager, isCurrentItem: Boolean, height: Int, moveDirection: Float)
}

class DynamicHeightViewPager : ViewPager {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private lateinit var dynamicHeightItemViews: List<DynamicHeightViewPagerItemInterface>

    private var moveDirection = 0f

    private var touchPoint = object {
        var x = 0f
        var y = 0f
    }

    private val resize: (nextShowAdaptiveViewPagerView: DynamicHeightViewPagerItemInterface?, currentAdaptiveViewPagerView: DynamicHeightViewPagerItemInterface, height: Int) -> Unit =
        { nextShowItemView, currentItemView, height ->
            layoutParams = layoutParams.apply {
                this.height = height
            }
            nextShowItemView?.resize(this, false, height, moveDirection)
            currentItemView.resize(this, true, height, moveDirection)
        }


    fun init(dynamicHeightItemViews: List<DynamicHeightViewPagerItemInterface>, selectedItem: Int = 0) {
        this.dynamicHeightItemViews = dynamicHeightItemViews
        checkIndex(selectedItem)
        currentItem = selectedItem
        if (!dynamicHeightItemViews.isNullOrEmpty()) {
            resize.invoke(
                null,
                dynamicHeightItemViews[selectedItem],
                dynamicHeightItemViews[selectedItem].getOriginContentHeight()
            )
        }
    }

    private fun checkIndex(index: Int) {
        if (index !in 0 until dynamicHeightItemViews.size) {
            throw IllegalArgumentException("index is out of bounds, dynamicHeightItemViews.size is ${dynamicHeightItemViews.size}")
        }
    }

    override fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        super.setCurrentItem(item, smoothScroll)
        updateCurrentItem(item)
    }

    override fun setCurrentItem(item: Int) {
        super.setCurrentItem(item)
        updateCurrentItem(item)
    }

    private fun updateCurrentItem(currentItem: Int) {
        checkIndex(currentItem)
        resize.invoke(
            null,
            dynamicHeightItemViews[currentItem],
            dynamicHeightItemViews[currentItem].getOriginContentHeight()
        )
    }

    init {
        setPageTransformer(false) { view, delta ->
            val itemViews = dynamicHeightItemViews ?: return@setPageTransformer
            val itemView = (view as DynamicHeightViewPagerItemInterface)
            val index = itemViews.indexOf(itemView)
            if (delta >= -1f && delta <= 1f) {
                if (moveDirection > 0f) { // 向左翻页
                    if (delta < 0) {
                        if (index >= itemViews.size - 1) {
                            return@setPageTransformer
                        }
                        val nextViewHeight = itemViews[index + 1].getOriginContentHeight() +
                                ((itemView.getOriginContentHeight() - itemViews[index + 1].getOriginContentHeight()) * (1 - Math.abs(
                                    delta
                                ))).toInt()
                        resize.invoke(itemView, itemViews[index + 1], nextViewHeight)
                    }
                } else {
                    if (delta > 0) {
                        if (index < 1) {
                            return@setPageTransformer
                        }
                        val nextViewHeight = itemViews[index - 1].getOriginContentHeight() +
                                ((itemView.getOriginContentHeight() - itemViews[index - 1].getOriginContentHeight()) * (1 - Math.abs(
                                    delta
                                ))).toInt()
                        resize.invoke(itemView, itemViews[index - 1], nextViewHeight)
                    }
                }
            }
        }
    }


    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                touchPoint.x = event.x
                touchPoint.y = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                moveDirection = event.x - touchPoint.x
                touchPoint.x = event.x
                touchPoint.y = event.y
            }
        }
        return super.dispatchTouchEvent(event)
    }
}