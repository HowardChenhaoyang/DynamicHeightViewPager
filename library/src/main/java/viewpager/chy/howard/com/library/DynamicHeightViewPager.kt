package viewpager.chy.howard.com.library

import android.animation.ValueAnimator
import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.lang.RuntimeException
import java.lang.ref.WeakReference

interface DynamicHeightViewPagerView{
    fun getDynamicHeightViewPagerItemInterface(): DynamicHeightViewPagerItemInterface
}

interface DynamicHeightViewPagerItemInterface {
    fun getOriginContentHeight(): Int
    fun getResizeView(): View
    fun onScaleChanged(scale: Float)
    fun resize(viewPager: ViewPager, isCurrentItem: Boolean, height: Int, moveDirection: Float)
}

class DynamicHeightViewPager : ViewPager {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    // viewPager滚动停止后用来更正高度，使用动画使得过程更平滑
    private var preHeight = 0
    private var animator: ValueAnimator? = null
    private var animatedItem: WeakReference<DynamicHeightViewPagerItemInterface>? = null
    private val animationDuration = 50L

    private var animatorUpdateListener: ValueAnimator.AnimatorUpdateListener = ValueAnimator.AnimatorUpdateListener {
        val animatedItem = animatedItem?.get() ?: return@AnimatorUpdateListener
        resize.invoke(null, animatedItem, it.animatedValue as Int)
    }

    private lateinit var dynamicHeightItemViews: List<DynamicHeightViewPagerItemInterface>

    // 用来渐变高度的PageTransformer，内部调用了outerTransformer的方法
    private val innerTransformer: PageTransformer by lazy {
        createPageTransformer()
    }

    // 外部设置的PageTransformer
    private var outerTransformer: PageTransformer? = null

    // 滑动方向
    private var moveDirection = 0f
    // 辅助判断滑动方向
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

    init {
        setPageTransformer(false, innerTransformer)
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

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
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

    private fun getDynamicHeightViewPagerItemInterfaceFromView(view: View): DynamicHeightViewPagerItemInterface{
        return if (view is DynamicHeightViewPagerView){
            view.getDynamicHeightViewPagerItemInterface()
        }else if (view.tag is DynamicHeightViewPagerItemInterface){
            view.tag as DynamicHeightViewPagerItemInterface
        }else{
            throw RuntimeException("child of viewPager must implement DynamicHeightViewPagerView or set the tag to DynamicHeightViewPagerItemInterface")
        }
    }

    private fun createPageTransformer(): PageTransformer {
        return PageTransformer { view, delta ->
            val itemViews = dynamicHeightItemViews
            val itemView = getDynamicHeightViewPagerItemInterfaceFromView(view)
            val index = itemViews.indexOf(itemView)
            if (delta >= -1f && delta <= 1f) {
                if (moveDirection > 0f) { // 向左翻页
                    if (delta < 0) {
                        if (index >= itemViews.size - 1) {
                            return@PageTransformer
                        }
                        val nextViewHeight = itemViews[index + 1].getOriginContentHeight() +
                                ((itemView.getOriginContentHeight() - itemViews[index + 1].getOriginContentHeight()) * (1 - Math.abs(
                                        delta
                                ))).toInt()
                        preHeight = nextViewHeight
                        resize.invoke(itemView, itemViews[index + 1], nextViewHeight)
                    }
                } else {
                    if (delta > 0) {
                        if (index < 1) {
                            return@PageTransformer
                        }
                        val nextViewHeight = itemViews[index - 1].getOriginContentHeight() +
                                ((itemView.getOriginContentHeight() - itemViews[index - 1].getOriginContentHeight()) * (1 - Math.abs(
                                        delta
                                ))).toInt()
                        preHeight = nextViewHeight
                        resize.invoke(itemView, itemViews[index - 1], nextViewHeight)
                    }
                }
            }
            if (animatedItem?.get() == itemView) {
                animator?.cancel()
            }
            if (delta == 0f) {
                animator?.cancel()
                val animator = ValueAnimator.ofInt(preHeight, itemView.getOriginContentHeight())
                animatedItem = WeakReference(itemView)
                animator.addUpdateListener(animatorUpdateListener)
                animator.duration = animationDuration
                animator.start()
                this.animator = animator
                preHeight = itemView.getOriginContentHeight()
            }

            outerTransformer?.transformPage(view, delta)
        }
    }

    private fun updateCurrentItem(currentItem: Int) {
        checkIndex(currentItem)
        resize.invoke(
                null,
                dynamicHeightItemViews[currentItem],
                dynamicHeightItemViews[currentItem].getOriginContentHeight()
        )
    }

    override fun setPageTransformer(reverseDrawingOrder: Boolean, transformer: PageTransformer?, pageLayerType: Int) {
        super.setPageTransformer(reverseDrawingOrder, innerTransformer, pageLayerType)
        if (transformer == innerTransformer) {
            super.setPageTransformer(reverseDrawingOrder, transformer, pageLayerType)
        } else {
            super.setPageTransformer(reverseDrawingOrder, innerTransformer, pageLayerType)
            outerTransformer = transformer
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