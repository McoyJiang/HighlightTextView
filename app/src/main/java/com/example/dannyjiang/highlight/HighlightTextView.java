package com.example.dannyjiang.highlight;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorRes;
import android.support.v7.widget.AppCompatTextView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Danny 姜
 */
public class HighlightTextView extends AppCompatTextView {

    private int highlightColor;
    private int highlightIndex;
    private boolean caseInsensitive;
    private boolean isLoopHighlight;
    private boolean highlightForever = true;
    private OnWordClickListener onWordClickListener;
    private MediaCallbackHandler mediaCallbackHandler;
    private Runnable highlightRunnable = new Runnable() {
        @Override
        public void run() {
            String[] allWords = getText().toString().split("\\s+");
            if (highlightIndex < allWords.length) {
                highlightText(allWords[highlightIndex++]);
                mediaCallbackHandler.postDelayed(this, 1000);
            } else {
                clearSpan();
                if (isLoopHighlight) {
                    highlightIndex = 0;
                    mediaCallbackHandler.postDelayed(this, 1000);
                }
            }
        }
    };

    public interface OnWordClickListener {
        void onWordClicked(String text);
    }

    /*
     * DANNY:
     * make Handler static, to avoid memory leak
     * and use WeakReference to avoid memory stress
     */
    static class MediaCallbackHandler extends Handler {
        private static final int HIGH_LIGHT = 1;
        private static final int CLEAR_SPAN = 2;

        WeakReference<HighlightTextView> mMonitor;

        MediaCallbackHandler(HighlightTextView mediaPlayerMonitor) {
            this.mMonitor = new WeakReference<>(mediaPlayerMonitor);
        }

        @Override
        public void handleMessage(Message msg) {
            HighlightTextView highlightTextView = mMonitor.get();
            if (highlightTextView == null) {
                return;
            }
            switch (msg.what) {
                case HIGH_LIGHT:
                    if (msg.obj == null || !(msg.obj instanceof String)) {
                        return;
                    }
                    String text = (String) msg.obj;
                    highlightTextView.highlightText(text);

                    if (msg.arg1 > 0) {
                        sendEmptyMessageDelayed(CLEAR_SPAN, msg.arg1);
                    }
                    break;
                case CLEAR_SPAN:
                    highlightTextView.clearSpan();
                    break;
            }
        }
    }

    public HighlightTextView(Context context) {
        this(context, null);
    }
    public HighlightTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    private void initialize() {
        // default text color
        setTextHighlightColor("#689FD1");
        // default line padding
        setLineSpacing(2, 1);
        // remove highlight background color
        setHighlightColor(Color.TRANSPARENT);
        // ignore case insensitive
        setCaseInsensitive(false);
        // initialize the Handler
        mediaCallbackHandler = new MediaCallbackHandler(this);
    }

    public void setTextHighlightColor(String highlightColorHex) {
        this.highlightColor = Color.parseColor(highlightColorHex);
    }

    public void setTextHighlightColor(@ColorRes int colorResource) {
        this.highlightColor = getResources().getColor(colorResource);
    }

    public void setCaseInsensitive(boolean caseInsensitive) {
        this.caseInsensitive = caseInsensitive;
    }

    public void setHighlightForever(boolean highlightForever) {
        this.highlightForever = highlightForever;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopHighlight();
    }


    public void highlightText(String textToHighlight, long timeDelay, int duration) {
        Message msg = Message.obtain(mediaCallbackHandler, MediaCallbackHandler.HIGH_LIGHT, textToHighlight);
        msg.arg1 = duration;
        mediaCallbackHandler.sendMessageDelayed(msg, timeDelay);
    }

    private void highlightText(String textToHighlight) {
        if (TextUtils.isEmpty(getText()) && TextUtils.isEmpty(textToHighlight)) {
            throw new IllegalStateException("You must specify a text to highlight before using executing the highlight operation.");
        }
        if (highlightColor == 0) {
            throw new IllegalStateException("You must specify a color to highlight the text with before using executing the highlight operation.");
        }

        // before set any Span to this TextView, it's necessary to clear
        // all previous Span to make it display default status
        clearSpan();

        String text = getText().toString();
        // firstly, build a normal ClickableSpan for each word in this TextView
        SpannableStringBuilder ssb = build(text);

        // then apply the ForegroundColorSpan to the specific 'textToHighlight'
        Pattern pattern;
        if (caseInsensitive) {
            pattern = Pattern.compile(Pattern.quote(textToHighlight), Pattern.CASE_INSENSITIVE);
        } else {
            //pattern = Pattern.compile(Pattern.quote(textToHighlight));
            pattern = Pattern.compile("\\b" + textToHighlight + "\\b");
        }
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            ssb.setSpan(new ForegroundColorSpan(highlightColor), matcher.start(),
                    matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // finally, the the SpannableStringBuilder which contains all Spans to TextView
        setText(ssb);
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void highlight(final boolean isLoopHighlight) {
        if (TextUtils.isEmpty(getText())) {
            throw new IllegalStateException("no text need to loop highlight!");
        }
        if (mediaCallbackHandler == null) {
            throw new IllegalStateException("before loop highlight text, should initialize loop Handler");
        }

        this.isLoopHighlight = isLoopHighlight;
        mediaCallbackHandler.postDelayed(highlightRunnable, 1000);
    }

    public void stopHighlight() {
        if (mediaCallbackHandler != null) {
            mediaCallbackHandler.removeCallbacks(highlightRunnable);
        }
        clearSpan();
    }

    public void clearSpan() {
        CharSequence text = getText();
        if (text instanceof SpannableString) {
            SpannableString ss = (SpannableString) getText();
            ForegroundColorSpan[] spans = ((Spanned) text).getSpans(0, text.length(), ForegroundColorSpan.class);
            for (int i = 0; i < spans.length; i++) {
                ss.removeSpan(spans[i]);
            }
        }
    }

    // This builds the pattern span into a `SpannableStringBuilder`
    // Requires a CharSequence to be passed in to be applied to
    public SpannableStringBuilder build(CharSequence editable) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(editable);
            Matcher matcher = Pattern.compile("(\\w+)").matcher(ssb);
            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();
                String text = editable.subSequence(start, end).toString();
                InternalClickableSpan clickableSpan = new InternalClickableSpan(text);
                ssb.setSpan(clickableSpan, start, end, 0);
            }
        return ssb;
    }

    public OnWordClickListener getOnWordClickListener() {
        return onWordClickListener;
    }

    public void setOnWordClickListener(OnWordClickListener onWordClickListener) {
        this.onWordClickListener = onWordClickListener;
    }

    public class InternalClickableSpan extends ClickableSpan {

        private String clicked;

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }

        public InternalClickableSpan(String clickedString) {
            clicked = clickedString;
        }

        @Override
        public void onClick(View view) {
            if (onWordClickListener != null) {
                onWordClickListener.onWordClicked(clicked);
            }
            if (highlightForever) {
                highlightText(clicked);
            } else {
                highlightText(clicked, 0, 1000);
            }
            invalidate();
        }
    }
}
