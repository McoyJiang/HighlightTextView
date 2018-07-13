package com.example.dannyjiang.highlight;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HighlightActivity extends AppCompatActivity {

    private HighlightTextView highlightTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highlight);

        highlightTextView = findViewById(R.id.highlight);
        //highlightTextView.highlight(true);
    }
}
