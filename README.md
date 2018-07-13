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


**点击并高亮某一个TextView中的text**
