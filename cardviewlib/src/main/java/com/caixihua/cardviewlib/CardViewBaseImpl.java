package com.caixihua.cardviewlib;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;

/**
 * Created by Cxh
 * Time : 2018/8/1  下午3:20
 * Desc :
 */
public class CardViewBaseImpl implements ICardView {

    private final RectF mCornerRect = new RectF();

    @Override
    public void initStatic() {


        RoundRectDrawableWithShadow.sRoundRectHelper =
                new RoundRectDrawableWithShadow.RoundRectHelper() {
                    @Override
                    public void drawRoundRect(Canvas canvas, RectF bounds, float cornerRadius,
                                              Paint paint) {
                        final float twoRadius = cornerRadius * 2;
                        final float innerWidth = bounds.width() - twoRadius - 1;
                        final float innerHeight = bounds.height() - twoRadius - 1;
                        if (cornerRadius >= 1f) {
                            // increment corner radius to account for half pixels.
                            float roundedCornerRadius = cornerRadius + .5f;
                            mCornerRect.set(-roundedCornerRadius, -roundedCornerRadius,
                                    roundedCornerRadius,
                                    roundedCornerRadius);
                            int saved = canvas.save();
                            canvas.translate(bounds.left + roundedCornerRadius,
                                    bounds.top + roundedCornerRadius);
                            canvas.drawArc(mCornerRect, 180, 90, true, paint);
                            canvas.translate(innerWidth, 0);
                            canvas.rotate(90);
                            canvas.drawArc(mCornerRect, 180, 90, true, paint);
                            canvas.translate(innerHeight, 0);
                            canvas.rotate(90);
                            canvas.drawArc(mCornerRect, 180, 90, true, paint);
                            canvas.translate(innerWidth, 0);
                            canvas.rotate(90);
                            canvas.drawArc(mCornerRect, 180, 90, true, paint);
                            canvas.restoreToCount(saved);
                            //draw top and bottom pieces
                            canvas.drawRect(bounds.left + roundedCornerRadius - 1f, bounds.top,
                                    bounds.right - roundedCornerRadius + 1f,
                                    bounds.top + roundedCornerRadius, paint);

                            canvas.drawRect(bounds.left + roundedCornerRadius - 1f,
                                    bounds.bottom - roundedCornerRadius,
                                    bounds.right - roundedCornerRadius + 1f, bounds.bottom, paint);
                        }
                        // center
                        canvas.drawRect(bounds.left, bounds.top + cornerRadius,
                                bounds.right, bounds.bottom - cornerRadius, paint);
                    }
                };
    }

    @Override
    public void initialize(ICardViewDelegate cardView, Context context, @ColorInt int
            mShadowStartColor, @ColorInt int mShadowEndColor, ColorStateList backgroundColor,
                           float radius, float elevation, float maxElevation) {
        RoundRectDrawableWithShadow background = createBackground(context, mShadowStartColor,
                mShadowEndColor, backgroundColor, radius, elevation, maxElevation);
        background.setAddPaddingForCorners(cardView.getPreventCornerOverlap());
        cardView.setCardBackground(background);
        updatePadding(cardView);
    }

    private RoundRectDrawableWithShadow createBackground(Context context, @ColorInt int
            mShadowStartColor, @ColorInt int mShadowEndColor, ColorStateList backgroundColor,
                                                         float radius, float elevation, float
                                                                 maxElevation) {
        return new RoundRectDrawableWithShadow(context.getResources(), mShadowStartColor,
                mShadowEndColor, backgroundColor, radius, elevation, maxElevation);
    }

    @Override
    public void updatePadding(ICardViewDelegate cardView) {
        Rect shadowPadding = new Rect();
        getShadowBackground(cardView).getMaxShadowAndCornerPadding(shadowPadding);
        cardView.setMinWidthHeightInternal((int) Math.ceil(getMinWidth(cardView)),
                (int) Math.ceil(getMinHeight(cardView)));
        cardView.setShadowPadding(shadowPadding.left, shadowPadding.top,
                shadowPadding.right, shadowPadding.bottom);
    }

    @Override
    public void onCompatPaddingChanged(ICardViewDelegate cardView) {
        // NO OP
    }

    @Override
    public void onPreventCornerOverlapChanged(ICardViewDelegate cardView) {
        getShadowBackground(cardView).setAddPaddingForCorners(cardView.getPreventCornerOverlap());
        updatePadding(cardView);
    }

    @Override
    public void setBackgroundColor(ICardViewDelegate cardView, @Nullable ColorStateList color) {
        getShadowBackground(cardView).setColor(color);
    }

    @Override
    public ColorStateList getBackgroundColor(ICardViewDelegate cardView) {
        return getShadowBackground(cardView).getColor();
    }

    @Override
    public void setRadius(ICardViewDelegate cardView, float radius) {
        getShadowBackground(cardView).setCornerRadius(radius);
        updatePadding(cardView);
    }

    @Override
    public float getRadius(ICardViewDelegate cardView) {
        return getShadowBackground(cardView).getCornerRadius();
    }

    @Override
    public void setElevation(ICardViewDelegate cardView, float elevation) {
        getShadowBackground(cardView).setShadowSize(elevation);
    }

    @Override
    public float getElevation(ICardViewDelegate cardView) {
        return getShadowBackground(cardView).getShadowSize();
    }

    @Override
    public void setMaxElevation(ICardViewDelegate cardView, float maxElevation) {
        getShadowBackground(cardView).setMaxShadowSize(maxElevation);
        updatePadding(cardView);
    }

    @Override
    public float getMaxElevation(ICardViewDelegate cardView) {
        return getShadowBackground(cardView).getMaxShadowSize();
    }

    @Override
    public float getMinWidth(ICardViewDelegate cardView) {
        return getShadowBackground(cardView).getMinWidth();
    }

    @Override
    public float getMinHeight(ICardViewDelegate cardView) {
        return getShadowBackground(cardView).getMinHeight();
    }

    private RoundRectDrawableWithShadow getShadowBackground(ICardViewDelegate cardView) {
        return ((RoundRectDrawableWithShadow) cardView.getCardBackground());
    }
}
