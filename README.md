# LabelTextView

## 简介
一款文字标签，继承于TextView，实现TextView加标签。

标签是由一个圆角矩形和一个三角形绘制而成，使用了画布的裁剪

考虑到扩展性
推荐您在android:background中设置drawable设置selector。

重点介绍两个属性

### labelHeightWeight和labelWidthWeight

类似于LinearLayout中的权重。

labelHeightWeight代表标签宽度和总宽度的比例

labelWidthWeight代表标签高度和总高度的比例


### labelTextPaddingCenter和labelTextPaddingBottom

这两个属性都是以标签文字和文字底边作为参照

labelTextPaddingCenter表示标签文字距离底边中线的偏移量

labelTextPaddingBottom表示标签文字距离底边的高度偏移量

具体思路以及实现可以参考我的博客

http://blog.csdn.net/aroundme/article/details/54630352

## 注意

* labelHeightWeight和labelWidthWeight两个属性都有默认值，
如果使用这个属性的话一定要大于0，不然会抛出异常

* 请在TextView中设置background和textColor(可选),具体可参考demo

* 考虑到在大部分场合下需要第一时间显示的几率很小，大部分是通过代码调用显示，
所以默认是不显示标签的。显示代码见使用

## 使用

```
<com.foxmail.aroundme.library.LabelTextView
        android:id="@+id/labelTextView"
        android:layout_width="match_parent"
        android:layout_height="33dp"
        android:gravity="center"
        android:text="ABC"
        android:textColor="@drawable/pop_channel_item_bg_text_selector"
        android:textSize="14sp"
        app:labelHeightWeight="1"
        app:labelTextColor="@android:color/black"
        app:labelTextPaddingCenter="1dp"
        app:labelBgColor="#ff8800"
        app:labelTextSize="9sp"
        app:labelWidthWeight="4"
        app:roundRectBorderWidth="1dp"
        app:roundRectRadius="16dp" />

```

显示：

```
LabelTextView labelTextView = (LabelTextView) view.findViewById(R.id.labelTextView);

labelTextView.setLabelBgColor(context.getResources()
        .getColor(R.color.brand_color))
        .setLabelText("NEW")
        .update();

```


## 属性介绍
```
<!--标签属性-->
        <!--标签背景颜色-->
        <attr name="labelBgColor" format="color"/>
        <!--标签内容-->
        <attr name="labelText" format="string"/>
        <!--标签字体颜色-->
        <attr name="labelTextColor" format="color"/>
        <!--标签字体大小-->
        <attr name="labelTextSize" format="dimension"/>
        <!--标签宽度权重,起点为左上角-->
        <attr name="labelWidthWeight" format="float"/>
        <!--标签高度权重,起点为左上角-->
        <attr name="labelHeightWeight" format="float"/>
        <!--文字和底部间距-->
        <attr name="labelTextPaddingBottom" format="dimension"/>
        <!--文字和中点的间距-->
        <attr name="labelTextPaddingCenter" format="dimension"/>

        <!--圆角矩形-->
        <!--矩形边界宽度-->
        <attr name="roundRectBorderWidth" format="dimension"/>
        <!--矩形四边半径-->
        <attr name="roundRectRadius" format="dimension"/>

```

显示效果如下：

<img src="http://ojwjax1r0.bkt.clouddn.com/Screenshot_2017-01-20-10-51-32.png" width=300 height=540 />

## 版本更新

* 1.2
修改混合模式

之前一直是用裁剪方式，现在换成混合模式实现

* 1.1

修改裁剪方式的实现

原来是用版本区分，大于19使用Path.op，小于19使用canvas.clipPath

现在统一使用canvas.clipPath方式实现
