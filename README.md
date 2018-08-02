# 可以修改阴影颜色的CardView
（copy from support lib, and extend）
尽管很早前网上就有类似的文章出现，为了看看内部原理，还是去copy了源码改成自己想要的样子。

  
  
## 效果图

![效果图](https://github.com/Thuantanon/CardView/blob/master/image/demo.png)

## 集成方式
> Step 1. Add it in your root build.gradle at the end of repositories:
```Java
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
> Step 2. Add the dependency:
```Java
dependencies {
	 implementation 'com.github.Thuantanon:CardView:1.0.1'
}
```
## 参数介绍
> 在support包的CardView基础上新增了2个参数，因为API21过后阴影部分实现在JNI层，所以实际上隐形效果是使用的API21以下的Drawable实现，因此API21以上效果与support包的CardView并不一致。这一点可以读源码了解。
```Java
<com.caixihua.cardviewlib.CardView
     ...
     app:cardShadowStartColor="..."
     app:cardShadowEndColor="..."
     app:cardElevation="..."
     ...
     />
```
> 官方CardView默认色值，可以参考
```Java
<color name="cardview_shadow_end_color">#03000000</color>
<color name="cardview_shadow_start_color">#37000000</color>
```



