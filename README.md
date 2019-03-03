blog 地址： https://www.jianshu.com/p/14389b125390

先看效果图

![高度随内容平滑改变](https://upload-images.jianshu.io/upload_images/5515535-32d099ff3a44eebc.gif?imageMogr2/auto-orient/strip)

可以看到ViewPager随内容的高度平滑改变。

###实现原理

要实现ViewPager的高度随ItemView而变化，那么在滚动前需要获取到itemView的原始高度。

然后监听ViewPager的滚动过程，获取将要展示的下一个ItemView的高度，根据横向滚动比例，实时计算高度。

具体实现可以参考类[DynamicHeightViewPager](https://github.com/HowardChenhaoyang/DynamicHeightViewPager/blob/master/library/src/main/java/viewpager/chy/howard/com/library/DynamicHeightViewPager.kt)的```createPageTransformer```方法

###使用方式

使用方式非常简单，只需要以下几步：

1. ViewPager使用```DynamicHeightViewPager```类来代替，改类相对```ViewPager ```来说只增加了初始化方法```init(dynamicHeightItemViews: List<DynamicHeightViewPagerItemInterface>, selectedItem: Int = 0)```

2. ViewPager的每个ItemView需要实现[ComplicatedDynamicHeightViewPagerItem](https://github.com/HowardChenhaoyang/DynamicHeightViewPager/blob/master/library/src/main/java/viewpager/chy/howard/com/library/ComplicatedDynamicHeightViewPagerItem.kt)接口，改接口中的```getOriginContentHeight```方法需要返回ItemView的原始高度，根据此高度才能动态计算ViewPager的高度。ItemView需要继承```FrameLayout```。

3. 调用ViewPager的初始化方法```init(dynamicHeightItemViews: List<DynamicHeightViewPagerItemInterface>, selectedItem: Int = 0)```。


具体使用方法可以参考文末的demo。

#####viewPager中每个Item不会局限于图片，可以是其他UI组件。
当每个Item的子View需要根据ViewPager高度的变化进行缩放时，可以响应```ComplicatedDynamicHeightViewPagerItem```的```onScaleChanged(scale: Float)```方法。

github地址：https://github.com/HowardChenhaoyang/DynamicHeightViewPager

