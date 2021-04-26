package com.imorning.tns.ui;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.imorning.senseinfohelper.utils.StringUtils;
import com.imorning.tns.R;

public class ContentTextView extends AppCompatTextView {
    public ContentTextView(@NonNull Context context) {
        super(context);
    }

    public ContentTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ContentTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (text == null || StringUtils.isEmpty(text.toString())) {
            text = getResources().getText(R.string.null_text);
        }
        super.setText(text, type);
    }
}
