package com.imorning.tns.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class PriceListView extends ListView {
    public PriceListView(Context context) {
        super(context);
    }

    public PriceListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PriceListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PriceListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
