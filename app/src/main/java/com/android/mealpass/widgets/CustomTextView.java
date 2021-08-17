package com.android.mealpass.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import mealpass.com.mealpass.R;


/**
 * Created by singh.gagandeep on 18/09/17.
 */

public class CustomTextView extends androidx.appcompat.widget.AppCompatTextView {

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public CustomTextView(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTextView);
            String fontName = a.getString(R.styleable.CustomTextView_fontG);
            try {
                if (fontName != null) {
                    Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + fontName);
                    setTypeface(myTypeface);
                } else {
                    setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/" + Fonts.NORMAL));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            a.recycle();
        }
    }


/**
 * to show hash tags on text view use this method.
 */
//    @Override
//    public void setText(CharSequence text, BufferType type) {
//        SpannableString hashtagintitle = new SpannableString(text);
//        Matcher matcher = Pattern.compile("#([A-Za-z0-9_-]+)").matcher(hashtagintitle);
//        while (matcher.find())
//        {
//            hashtagintitle.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(),R.color.colorAccent)), matcher.start(), matcher.end(), 0);
//
//        }
//        super.setText(hashtagintitle, type);
//    }
}