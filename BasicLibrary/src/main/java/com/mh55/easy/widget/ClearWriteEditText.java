package com.mh55.easy.widget;

import static androidx.core.widget.TextViewKt.addTextChangedListener;
import static com.blankj.utilcode.util.ClipboardUtils.getText;
import static com.mh55.easy.manager.AppManager.getContext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

import com.mh55.easy.R;
import com.mh55.easy.utils.LogUtil;

public class ClearWriteEditText extends androidx.appcompat.widget.AppCompatEditText implements View.OnFocusChangeListener, TextWatcher {
    private Drawable mClearDrawable;
    private boolean neverShowClearDrawable;
    private boolean showClearDrawableNoFocus;
    private TextWatcher watcher;

    public ClearWriteEditText(Context context) {
        this(context, null);
    }

    public ClearWriteEditText(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 16842862);
    }

    public ClearWriteEditText(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(attributeSet);
    }

    private void init(AttributeSet attributeSet) {
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
        TypedArray obtainStyledAttributes = attributeSet == null ? null : getContext().obtainStyledAttributes(attributeSet, R.styleable.ClearWriteEditText);
        if (obtainStyledAttributes != null) {
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i = 0; i < indexCount; i++) {
                int index = obtainStyledAttributes.getIndex(i);
                if (index == R.styleable.ClearWriteEditText_et_right_image) {
                    Drawable drawable = obtainStyledAttributes.getDrawable(R.styleable.ClearWriteEditText_et_right_image);
                    this.mClearDrawable = drawable;
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), this.mClearDrawable.getIntrinsicHeight());
                    setClearIconVisible(false);
                } else if (index == R.styleable.ClearWriteEditText_et_left_image) {
                    Drawable drawable2 = obtainStyledAttributes.getDrawable(R.styleable.ClearWriteEditText_et_left_image);
                    drawable2.setBounds(0, 0, drawable2.getIntrinsicWidth(), drawable2.getIntrinsicHeight());
                    setCompoundDrawables(drawable2, getCompoundDrawables()[1], getCompoundDrawables()[2], getCompoundDrawables()[3]);
                }
            }
        }
    }

    public void setBoundsHind() {
        this.mClearDrawable.setBounds(0, 0, 0, 0);
    }

    @Override // android.widget.TextView, android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        if (!this.neverShowClearDrawable) {
            setClearIconVisible(charSequence.length() > 0);
        }
        TextWatcher textWatcher = this.watcher;
        if (textWatcher == null) {
            return;
        }
        textWatcher.onTextChanged(charSequence, i, i2, i3);
    }

    protected void setClearIconVisible(boolean z) {
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], z ? this.mClearDrawable : null, getCompoundDrawables()[3]);
    }

    @Override // android.widget.TextView, android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        if (getCompoundDrawables()[2] != null) {
            boolean z = true;
            if (event.getAction() == 1) {
                boolean touchable = event.getX() > (float)(this.getWidth() - this.getPaddingRight() - this.mClearDrawable.getIntrinsicWidth()) && event.getX() < (float)(this.getWidth() - this.getPaddingRight());
                if (touchable) {
                    setText("");
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override // android.view.View.OnFocusChangeListener
    public void onFocusChange(View view, boolean z) {
        if (z && !this.neverShowClearDrawable) {
            setClearIconVisible(getText().length() > 0);
        } else if (this.showClearDrawableNoFocus) {
        } else {
            setClearIconVisible(false);
        }
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        TextWatcher textWatcher = this.watcher;
        if (textWatcher == null) {
            return;
        }
        textWatcher.beforeTextChanged(charSequence, i, i2, i3);
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
        TextWatcher textWatcher = this.watcher;
        if (textWatcher == null) {
            return;
        }
        textWatcher.afterTextChanged(editable);
    }

    public void setShakeAnimation() {
        startAnimation(shakeAnimation(3));
    }

    public static Animation shakeAnimation(int i) {
        TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 10.0f, 0.0f, 0.0f);
        translateAnimation.setInterpolator(new CycleInterpolator(i));
        translateAnimation.setDuration(500L);
        return translateAnimation;
    }

    public Drawable getClearDrawable() {
        return this.mClearDrawable;
    }

    public void setClearDrawable(Drawable drawable) {
        this.mClearDrawable = drawable;
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), this.mClearDrawable.getIntrinsicHeight());
        setClearIconVisible(false);
    }

    public void setClearDrawableNeverShow(boolean z) {
        this.neverShowClearDrawable = z;
    }

    public void setShowClearDrawableNoFocus(boolean z) {
        this.showClearDrawableNoFocus = z;
    }

    public void addCommonTextChangedListener(TextWatcher textWatcher) {
        this.watcher = textWatcher;
    }
}