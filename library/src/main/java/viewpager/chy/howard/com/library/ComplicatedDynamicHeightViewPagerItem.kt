package viewpager.chy.howard.com.library

import android.support.v4.view.ViewPager
import android.view.Gravity
import android.widget.FrameLayout

interface ComplicatedDynamicHeightViewPagerItem : DynamicHeightViewPagerItemInterface {
    override fun resize(viewPager: ViewPager, isCurrentItem: Boolean, height: Int, moveDirection: Float) {
        val layoutParams = getResizeView().layoutParams as FrameLayout.LayoutParams
        val proportionOfWidthDivideHeight = viewPager.width.toFloat() / getOriginContentHeight()
        if (height == getOriginContentHeight()) {
            onScaleChanged(1f)
            layoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT
            layoutParams.width = FrameLayout.LayoutParams.MATCH_PARENT
        } else {
            layoutParams.height = height
            layoutParams.width = (height * proportionOfWidthDivideHeight).toInt()
            onScaleChanged(height / getOriginContentHeight().toFloat())
            if (isCurrentItem) {
                if (moveDirection > 0f) {
                    layoutParams.gravity = Gravity.LEFT
                } else {
                    layoutParams.gravity = Gravity.RIGHT
                }
            } else {
                if (moveDirection > 0f) {
                    layoutParams.gravity = Gravity.RIGHT
                } else {
                    layoutParams.gravity = Gravity.LEFT
                }
            }
        }
        getResizeView().layoutParams = layoutParams
    }
}
