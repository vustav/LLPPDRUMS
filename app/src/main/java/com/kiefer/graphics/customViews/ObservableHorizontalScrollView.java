package com.kiefer.graphics.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class ObservableHorizontalScrollView extends HorizontalScrollView {

    private ObservableHorizontalScrollViewListener scrollViewListener = null;

    public ObservableHorizontalScrollView(Context context) {
        super(context);
    }

    public ObservableHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ObservableHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollViewListener(ObservableHorizontalScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if(scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    public interface ObservableHorizontalScrollViewListener {
        void onScrollChanged(ObservableHorizontalScrollView scrollView, int x, int y, int oldx, int oldy);
    }
}