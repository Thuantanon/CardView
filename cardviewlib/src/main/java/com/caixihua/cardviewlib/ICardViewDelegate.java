package com.caixihua.cardviewlib;

import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by Cxh
 * Time : 2018/8/1  上午11:50
 * Desc :
 */
interface ICardViewDelegate {

    void setCardBackground(Drawable drawable);

    Drawable getCardBackground();

    boolean getUseCompatPadding();

    boolean getPreventCornerOverlap();

    void setShadowPadding(int left, int top, int right, int bottom);

    void setMinWidthHeightInternal(int width, int height);

    View getCardView();
}
