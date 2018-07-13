package com.example.dannyjiang.highlight;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class HighlightActivity extends AppCompatActivity {

    private HighlightTextView highlightTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highlight);

        highlightTextView = findViewById(R.id.highlight);
        highlightTextView.highlight(false);
        highlightTextView.setOnWordClickListener(new HighlightTextView.OnWordClickListener() {
            @Override
            public void onWordClicked(String text) {
                Toast.makeText(HighlightActivity.this, "点击了：" + text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void highlightText(View view) {
        highlightTextView.highlightText("Decorating", 300, 2000);
    }
}
