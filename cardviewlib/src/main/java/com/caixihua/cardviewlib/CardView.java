package com.caixihua.cardviewlib;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Cxh
 * Time : 2018/8/1  上午11:54
 * Desc :
 */
public class CardView extends FrameLayout {

    private static final int[] COLOR_BACKGROUND_ATTR = {android.R.attr.colorBackground};
    private static ICardView IMPL;

    static {

//        if (Build.VERSION.SDK_INT >= 21) {
//            IMPL = new CardViewApi21Impl();
//        } else if (Build.VERSION.SDK_INT >= 17) {
//            IMPL = new CardViewApi17Impl();
//        } else {
//            IMPL = new CardViewBaseImpl();
//        }

        if(Build.VERSION.SDK_INT >= 17) {
            IMPL = new CardViewApi17Impl();
        } else {
            IMPL = new CardViewBaseImpl();
        }

        IMPL.initStatic();
    }

    private boolean mCompatPadding;
    private boolean mPreventCornerOverlap;

    int mUserSetMinWidth, mUserSetMinHeight;

    final Rect mContentPadding = new Rect();
    final Rect mShadowBounds = new Rect();

    public CardView(@NonNull Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public CardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public CardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        // No Operate
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        // No Operate
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!(IMPL instanceof CardViewApi21Impl)) {
            final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            switch (widthMode) {
                case MeasureSpec.EXACTLY:
                case MeasureSpec.AT_MOST:
                    final int minWidth = (int) Math.ceil(IMPL.getMinWidth(mCardViewDelegate));
                    widthMeasureSpec = MeasureSpec.makeMeasureSpec(Math.max(minWidth,
                            MeasureSpec.getSize(widthMeasureSpec)), widthMode);
                    break;
                case MeasureSpec.UNSPECIFIED:
                    // Do nothing
                    break;
            }

            final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            switch (heightMode) {
                case MeasureSpec.EXACTLY:
                case MeasureSpec.AT_MOST:
                    final int minHeight = (int) Math.ceil(IMPL.getMinHeight(mCardViewDelegate));
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec(Math.max(minHeight,
                            MeasureSpec.getSize(heightMeasureSpec)), heightMode);
                    break;
                case MeasureSpec.UNSPECIFIED:
                    // Do nothing
                    break;
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void initialize(@NonNull Context context, @Nullable AttributeSet attrs, int
            defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R
                        .styleable.CardView, defStyleAttr, R.style.CardView);
        ColorStateList backgroundColor;
        if (a.hasValue(R.styleable.CardView_cardBackgroundColor)) {
            backgroundColor = a.getColorStateList(R.styleable
                    .CardView_cardBackgroundColor);
        } else {
            // There isn't one set, so we'll compute one based on the theme
            final TypedArray aa = getContext().obtainStyledAttributes(COLOR_BACKGROUND_ATTR);
            final int themeColorBackground = aa.getColor(0, 0);
            aa.recycle();

            // If the theme colorBackground is light, use our own light color, otherwise dark
            final float[] hsv = new float[3];
            Color.colorToHSV(themeColorBackground, hsv);
            backgroundColor = ColorStateList.valueOf(hsv[2] > 0.5f ? getResources().getColor(R
                    .color.cardview_light_background) : getResources().getColor(R.color
                    .cardview_dark_background));
        }

        // Shadow颜色
        @ColorInt  int mShadowStartColor = Color.TRANSPARENT;
        @ColorInt  int mShadowEndColor = Color.TRANSPARENT;
        if(a.hasValue(R.styleable.CardView_cardShadowStartColor)){
            mShadowStartColor = a.getColor(R.styleable.CardView_cardShadowStartColor, Color.TRANSPARENT);
        }
        if(a.hasValue(R.styleable.CardView_cardShadowEndColor)){
            mShadowEndColor = a.getColor(R.styleable.CardView_cardShadowEndColor, Color.TRANSPARENT);
        }

        float radius = a.getDimension(R.styleable.CardView_cardCornerRadius, 0);
        float elevation = a.getDimension(R.styleable.CardView_cardElevation, 0);
        float maxElevation = a.getDimension(R.styleable.CardView_cardMaxElevation, 0);
        mCompatPadding = a.getBoolean(R.styleable.CardView_cardUseCompatPadding, false);
        mPreventCornerOverlap = a.getBoolean(R.styleable.CardView_cardPreventCornerOverlap, true);
        int defaultPadding = a.getDimensionPixelSize(R.styleable.CardView_contentPadding, 0);
        mContentPadding.left = a.getDimensionPixelSize(R.styleable.CardView_contentPaddingLeft,
                defaultPadding);
        mContentPadding.top = a.getDimensionPixelSize(R.styleable.CardView_contentPaddingTop,
                defaultPadding);
        mContentPadding.right = a.getDimensionPixelSize(R.styleable.CardView_contentPaddingRight,
                defaultPadding);
        mContentPadding.bottom = a.getDimensionPixelSize(R.styleable
                .CardView_contentPaddingBottom, defaultPadding);
        if (elevation > maxElevation) {
            maxElevation = elevation;
        }
        mUserSetMinWidth = a.getDimensionPixelSize(R.styleable.CardView_android_minWidth, 0);
        mUserSetMinHeight = a.getDimensionPixelSize(R.styleable.CardView_android_minHeight, 0);
        a.recycle();

        IMPL.initialize(mCardViewDelegate, context, mShadowStartColor, mShadowEndColor,
                backgroundColor, radius, elevation, maxElevation);
    }

    @Override
    public void setMinimumWidth(int minWidth) {
        mUserSetMinWidth = minWidth;
        super.setMinimumWidth(minWidth);
    }

    @Override
    public void setMinimumHeight(int minHeight) {
        mUserSetMinHeight = minHeight;
        super.setMinimumHeight(minHeight);
    }

    public void setCardBackgroundColor(@ColorInt int color) {
        IMPL.setBackgroundColor(mCardViewDelegate, ColorStateList.valueOf(color));
    }

    public void setCardBackgroundColor(@Nullable ColorStateList color) {
        IMPL.setBackgroundColor(mCardViewDelegate, color);
    }

    public ColorStateList getCardBackgroundColor() {
        return IMPL.getBackgroundColor(mCardViewDelegate);
    }

    public int getContentPaddingLeft() {
        return mContentPadding.left;
    }

    public int getContentPaddingRight() {
        return mContentPadding.right;
    }

    public int getContentPaddingTop() {
        return mContentPadding.top;
    }

    public int getContentPaddingBottom() {
        return mContentPadding.bottom;
    }

    public void setRadius(float radius) {
        IMPL.setRadius(mCardViewDelegate, radius);
    }

    public float getRadius() {
        return IMPL.getRadius(mCardViewDelegate);
    }

    public void setCardElevation(float elevation) {
        IMPL.setElevation(mCardViewDelegate, elevation);
    }

    public float getCardElevation() {
        return IMPL.getElevation(mCardViewDelegate);
    }

    public void setMaxCardElevation(float maxElevation) {
        IMPL.setMaxElevation(mCardViewDelegate, maxElevation);
    }

    public float getMaxCardElevation() {
        return IMPL.getMaxElevation(mCardViewDelegate);
    }

    public boolean getUseCompatPadding() {
        return mCompatPadding;
    }

    public boolean getPreventCornerOverlap() {
        return mPreventCornerOverlap;
    }

    public void setPreventCornerOverlap(boolean preventCornerOverlap) {
        if (preventCornerOverlap != mPreventCornerOverlap) {
            mPreventCornerOverlap = preventCornerOverlap;
            IMPL.onPreventCornerOverlapChanged(mCardViewDelegate);
        }
    }

    public void setUseCompatPadding(boolean useCompatPadding) {
        if (mCompatPadding != useCompatPadding) {
            mCompatPadding = useCompatPadding;
            IMPL.onCompatPaddingChanged(mCardViewDelegate);
        }
    }

    public void setContentPadding(int left, int top, int right, int bottom) {
        mContentPadding.set(left, top, right, bottom);
        IMPL.updatePadding(mCardViewDelegate);
    }


    private final ICardViewDelegate mCardViewDelegate = new ICardViewDelegate() {

        private Drawable mCardBackground;

        @Override
        public void setCardBackground(Drawable drawable) {
            mCardBackground = drawable;
            setBackground(mCardBackground);
        }

        @Override
        public Drawable getCardBackground() {
            return mCardBackground;
        }

        @Override
        public boolean getUseCompatPadding() {
            return CardView.this.getUseCompatPadding();
        }

        @Override
        public boolean getPreventCornerOverlap() {
            return CardView.this.getPreventCornerOverlap();
        }

        @Override
        public void setShadowPadding(int left, int top, int right, int bottom) {
            mShadowBounds.set(left, top, right, bottom);
            CardView.super.setPadding(left + mContentPadding.left, top + mContentPadding.top,
                    right + mContentPadding.right, bottom + mContentPadding.bottom);
        }

        @Override
        public void setMinWidthHeightInternal(int width, int height) {
            if (width > mUserSetMinWidth) {
                CardView.super.setMinimumWidth(width);
            }
            if (height > mUserSetMinHeight) {
                CardView.super.setMinimumHeight(height);
            }
        }

        @Override
        public View getCardView() {
            return CardView.this;
        }
    };

}
