## 利用Tint属性优化Selector

博客文章地址 ：  [Tint属性优化Selector](http://www.jianshu.com/p/c37628fbc397) 
![final.gif](http://upload-images.jianshu.io/upload_images/1155837-c8fda7cfe387b486.gif?imageMogr2/auto-orient/strip)

## 关于Selector的使用
Selector中文的意思选择器，在Android中常常用来作组件的背景，这样做的好处是省去了用代码控制实现组件在不同状态下不同的背景颜色或图片的变换。使用十分方便。Selector就是状态列表（StateList）， 它分为两种，一种`Color-Selector` 和`Drawable-Selector`。一般使用语法如下：
```java
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android"
    android:constantSize=["true" | "false"]//drawable的大小是否当中状态变化，true表示是变化，false表示不变换，默认为false
    android:dither=["true" | "false"]//当位图与屏幕的像素配置不一样时(例如，一个ARGB为8888的位图与RGB为555的屏幕)会自行递色(dither)。设置为false时不可递色。默认true
    android:variablePadding=["true" | "false"] >//内边距是否变化，默认false
    <item
        android:drawable="@[package:]drawable/drawable_resource"//图片资源
        android:state_pressed=["true" | "false"]//是否触摸
        android:state_focused=["true" | "false"]//是否获取到焦点
        android:state_hovered=["true" | "false"]//光标是否经过
        android:state_selected=["true" | "false"]//是否选中
        android:state_checkable=["true" | "false"]//是否可勾选
        android:state_checked=["true" | "false"]//是否勾选
        android:state_enabled=["true" | "false"]//是否可用
        android:state_activated=["true" | "false"]//是否激活
        android:state_window_focused=["true" | "false"] />//所在窗口是否获取焦点
</selector>
```
比如我们创建一个 DrawableSelector , 在/res/drawable/文件夹下新建btn_seletor.xml :
```java
<?xml version="1.0" encoding="utf-8" ?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:state_pressed="true" android:drawable="@drawable/ic_btn_back_pressed" />
    <item android:drawable="@drawable/ic_btn_back_normal" />
</selector>
```
调用时直接设置在`android:background`属性上 : 
```java
<Button
            android:layout_width="@dimen/image_size"
            android:layout_height="@dimen/image_size"
            android:layout_margin="5dp"
            android:background="@drawable/btn_seletor"/>
```
编译之后就能看到 点击按钮时，按钮图标就会变。

![normal.gif](http://upload-images.jianshu.io/upload_images/1155837-51bce70ab3565c02.gif?imageMogr2/auto-orient/strip)


我们会发现，一般实现Selector的方法，需要以下两点 : 
+ 需要两张大小一样，表示不同状态效果的图片
+ 创建一个selector的xml文件

但是两张状态效果一样的图片，除了颜色，其它都是一样的，我们能不能只用一张图片来实现，在代码里修改图片的颜色呢？ 答案当然是可以的，那就是利用`android:tint`属性。

## 如何使用tint 属性
`android:tint` 是ImageView 特有的属性，所以我们用ImageView 来实现上面的例子，首先创建一个btn_single_seletor.xml, 注意使用的是同一张图片ic_btn_back_normal:
```java
<?xml version="1.0" encoding="utf-8" ?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:state_pressed="true" android:drawable="@drawable/ic_btn_back_normal" />
    <item android:drawable="@drawable/ic_btn_back_normal" />
</selector>
```
接着再创建image_color.xml 的 ColorDrawable , 并放在 /res/color/文件夹下 :
```java
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:state_pressed="true" android:color="#fac300" />
    <item android:color="#00000000" />
</selector>
```
最后设置到 ImageView 上:
```java
<ImageView
            android:layout_width="@dimen/image_size"
            android:layout_height="@dimen/image_size"
            android:layout_margin="5dp"
            android:tint="@color/image_color"
            android:src="@drawable/btn_single_seletor"
            android:clickable="true"/>
```
编译之后就能看到 点击按钮时，效果与上面的例子是一样的。也许你会注意到在image_color.xml 中我们设置的颜色是#00000000 , 这是为了让图片不着色。如果我们一定要用Button来实现呢，但是Button没有`android:src`和`android:tint`属性怎么办？ 虽然在普通view可以设置这两个属性，但是没有效果。其实我们可以用`android:background`、`android:backgroundTint`属性，我们添加一个Button 如下 ：
```java
<Button
            android:layout_width="@dimen/image_size"
            android:layout_height="@dimen/image_size"
            android:layout_margin="5dp"
            android:background="@drawable/btn_single_seletor"
            android:backgroundTint="@color/image_color"/>
```
编译安装之后你会发现，在普通状态时，按钮的图片是透明的，这和我们设置的普通状态下 tint color = #00000000"有关 , 不知道这是不是android的 bug ,为什么在src + tint 时就可以，在 background + backgroundTint 时就不行，知道答案的可以交流一下。

![single-selector.gif](http://upload-images.jianshu.io/upload_images/1155837-64940622c47fd763.gif?imageMogr2/auto-orient/strip)


## 更优雅地使用 tint 属性
在上面的例子中，我们设置了两个属性 ，其实我们可以只设置 `android:background` 或 `android:src` , 而不用设置 tint , 我们再添加 bitmap.xml :
```java
<?xml version="1.0" encoding="utf-8"?>
<bitmap xmlns:android="http://schemas.android.com/apk/res/android"
    android:src="@drawable/ic_btn_back_normal"
    android:tint="#fac300" />
```
再新增 btn_notint_seletor.xml ， 添加 bitmap.xml : 
```java
<?xml version="1.0" encoding="utf-8" ?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:state_pressed="true" android:drawable="@drawable/bitmap" />
    <item android:drawable="@drawable/ic_btn_back_normal" />
</selector>
```
最后设置到 ImageView 与 Button 上 ，去掉  `android:tint` 、`android:backgroundTint`属性:
```java
<ImageView
            android:layout_width="@dimen/image_size"
            android:layout_height="@dimen/image_size"
            android:layout_margin="5dp"
            android:src="@drawable/btn_notint_seletor"
            android:clickable="true"/>

<Button
            android:layout_width="@dimen/image_size"
            android:layout_height="@dimen/image_size"
            android:layout_margin="5dp"
            android:background="@drawable/btn_notint_seletor"/>
```
编译安装之后就能看到，效果与上面的例子是一样的。虽然添加多了一个bitmap.xml文件，但是我们免去了设置tint属性，不过上面的代码还可以再优化，就是去掉bitmap.xml  , 我们在 btn_notint_seletor.xml  的基础上修改成 btn_notint_final_seletor.xml  文件：
```java
<?xml version="1.0" encoding="utf-8" ?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:state_pressed="true">
        <bitmap android:src="@drawable/ic_btn_back_pressed" android:tint="#fac300" />
    </item>
    <item android:drawable="@drawable/ic_btn_back_normal" />
</selector>
```
重新设置成btn_notint_seletor之后，效果是一样的，而且代码更加简洁。这就是设置selector的最简洁的方式，我们只使用了一张图片，一个selector文件 就实现了效果。

## 使用 RotateDrawable  实现更多效果 
我们在实际开发中可能会经常碰到，左右翻页，上下翻页的情形，就是我们需要两张左右对称的图片，来实现左右箭头。如果加上点击效果的selector，那就需要4张图片了。如果我们用上面的方法，那也至少需要两张图片，那我们能不能在代码上实现图片的旋转呢，那就是使用 RotateDrawable 。 关于 RotateDrawable 的用法，可以参考[这篇文章](http://blog.csdn.net/lonelyroamer/article/details/8252533) ，我们创建一个btn_rotate_final_seletor.xml ：
```java
<?xml version="1.0" encoding="utf-8" ?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:state_pressed="true">
        <rotate
            android:visible="true"
            android:fromDegrees="-180"
            android:pivotX="50%"
            android:pivotY="50%">
            <bitmap
                android:src="@drawable/ic_btn_back_normal"
                android:tint="#fac300" />
        </rotate>
    </item>
    <item>
        <rotate
            android:drawable="@drawable/ic_btn_back_normal"
            android:visible="true"
            android:fromDegrees="-180"
            android:pivotX="50%"
            android:pivotY="50%">
        </rotate>
    </item>
</selector>
```
重新设置成 Button的background之后，编译安装，效果实现了，而且还是左右对称的两个按钮。是不是觉得很简洁，我们只用了一张图片就实现了左右箭头的两个按钮。


## 总结
使用这种方式来实现Selector ，不仅让代码更加简洁，而且省去了很多图片，减少了APK的大小。如果按平常的思路,在实现左右箭头的这个例子我们需要四张图片，平时一张图片至少也要几K 到 几十K，而现在只需要一张，减少了四分之三的大小。而且在后期中，如果要更改效果，我们只需要修改一下颜色值就可以了，炒鸡方便。

## 参考
+ [Android中selector的使用](http://blog.csdn.net/wenwen091100304/article/details/49667293)
+ [安卓着色器(tint)使用实践](http://www.jianshu.com/p/6bd7dd1cd491)
+ [Android Drawable Resource学习（十一）、RotateDrawable](http://blog.csdn.net/lonelyroamer/article/details/8252533)