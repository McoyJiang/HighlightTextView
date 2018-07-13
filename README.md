# HighlightTextView
**初始化**

`HighlightTextView` 的初始化很简单，只要在xml中声明，然后在Activity中find即可

activity_highlight.xml

```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <com.example.dannyjiang.highlight.HighlightTextView
        android:id="@+id/highlight"
        android:text="Decorating TextView via SpannableString in Android"
        android:textSize="40dp"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />

</LinearLayout>
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

**单独将某一个text高亮显示**

如果想高亮某一个text时，只需要调用 `highlightText` 方法即可
```
highlightTextView.highlightText("Decorating", 300, 2000);
```
其中highlightText方法有3个需要传入的参数，分别代表的意思是需要高亮的text、显示高亮之前的时间delay、高亮的时间(duration). <br>
因此上述代码的意思将"Decorating"字符在300毫秒之后，高亮显示2秒钟. 效果如下
<img src="https://github.com/McoyJiang/HighlightTextView/blob/master/IMAGES/highlightText.gif">

**点击并高亮某一个TextView中的text**

HighlightTextView已经内置实现的各个text的点击事件，因此不需要添加其它额外的代码，可以直接点击HighlightTextView中的任意一个字符，就会将点击的text进行高亮. 但是一般情况下，我们需要在Activity中获取一个点击Text的Callback，因此只要实现HighlightTextView中的OnWordClickListener并传给HighlightTextView即可。
```
highlightTextView.setOnWordClickListener(new HighlightTextView.OnWordClickListener() {
            @Override
            public void onWordClicked(String text) {
                Toast.makeText(HighlightActivity.this, "点击了：" + text, Toast.LENGTH_SHORT).show();
            }
        });
```
