package com.caixihua.cardviewlib;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;

/**
 * Created by Cxh
 * Time : 2018/8/1  上午11:49
 * Desc :
 */
public interface ICardView {

    void initialize(ICardViewDelegate cardView, Context context, @ColorInt int mShadowStartColor,
                    @ColorInt int mShadowEndColor, ColorStateList backgroundColor, float radius,
                    float elevation, float maxElevation);

    void setRadius(ICardViewDelegate cardView, float radius);

    float getRadius(ICardViewDelegate cardView);

    void setElevation(ICardViewDelegate cardView, float elevation);

    float getElevation(ICardViewDelegate cardView);

    void initStatic();

    void setMaxElevation(ICardViewDelegate cardView, float maxElevation);

    float getMaxElevation(ICardViewDelegate cardView);

    float getMinWidth(ICardViewDelegate cardView);

    float getMinHeight(ICardViewDelegate cardView);

    void updatePadding(ICardViewDelegate cardView);

    void onCompatPaddingChanged(ICardViewDelegate cardView);

    void onPreventCornerOverlapChanged(ICardViewDelegate cardView);

    void setBackgroundColor(ICardViewDelegate cardView, @Nullable ColorStateList color);

    ColorStateList getBackgroundColor(ICardViewDelegate cardView);
}
