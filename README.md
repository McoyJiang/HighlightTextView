# HighlightTextView
**初始化**

`HighlightTextView` 的初始化很简单，只要在xml中声明，然后在Activity中find即可

activity_highlight.xml

```
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.example.dannyjiang.highlight.HighlightTextView
        android:id="@+id/highlight"
        android:text="Decorating TextView via SpannableString in Android"
        android:textSize="40dp"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />

</FrameLayout>
```

HighlightTextView 显示的内容为 “Decorating TextView via SpannableString in Android”， 如下所示
<img src="https://github.com/McoyJiang/HighlightTextView/raw/master/IMAGES/default_text.png" width=320 height=480 />

HighlightActivity.java

```
public class HighlightActivity extends AppCompatActivity {

    private HighlightTextView highlightTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highlight);

        highlightTextView = findViewById(R.id.highlight);
    }
}
```

**自动循环高亮播放TextView中的各个text内容**
添加循环高亮播放效果，只要在初始化之后添加如下一行代码即可实现
```
highlightTextView.highlight(true);
```
当传入 `true` 时则会循环高亮播放，否则播放完一遍之后自动停止。 以上效果如下
<img src="https://github.com/McoyJiang/HighlightTextView/blob/master/IMAGES/highlight.gif">

**点击并高亮某一个TextView中的text**
