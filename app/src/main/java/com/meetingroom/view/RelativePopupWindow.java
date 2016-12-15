package com.meetingroom.view;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.widget.PopupWindowCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.mosai.utils.DensityUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author kakajika
 * @since 2016/07/01
 */
public class RelativePopupWindow extends PopupWindow {
    protected int width;
    protected int height;
    protected Context mContext;

    @IntDef({
            VerticalPosition.CENTER,
            VerticalPosition.ABOVE,
            VerticalPosition.BELOW,
            VerticalPosition.ALIGN_TOP,
            VerticalPosition.ALIGN_BOTTOM,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface VerticalPosition {
        int CENTER = 0;
        int ABOVE = 1;
        int BELOW = 2;
        int ALIGN_TOP = 3;
        int ALIGN_BOTTOM = 4;
    }

    @IntDef({
            HorizontalPosition.CENTER,
            HorizontalPosition.LEFT,
            HorizontalPosition.RIGHT,
            HorizontalPosition.ALIGN_LEFT,
            HorizontalPosition.ALIGN_RIGHT,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface HorizontalPosition {
        int CENTER = 0;
        int LEFT = 1;
        int RIGHT = 2;
        int ALIGN_LEFT = 3;
        int ALIGN_RIGHT = 4;
    }

    protected View view;

    protected void initConfig() {
        setHeight(height);
        setWidth(width);
        view.setFocusable(true); // 这个很重要
        view.setFocusableInTouchMode(true);
        setFocusable(true);
        setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
        setContentView(view);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (isShowing()) {
                        dismiss();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public RelativePopupWindow(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }

    public RelativePopupWindow(Context mContext, AttributeSet attrs) {
        super(mContext, attrs);
        this.mContext = mContext;
    }

    public RelativePopupWindow(Context mContext, AttributeSet attrs, int defStyleAttr) {
        super(mContext, attrs, defStyleAttr);
        this.mContext = mContext;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public RelativePopupWindow(Context mContext, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(mContext, attrs, defStyleAttr, defStyleRes);
        this.mContext = mContext;
    }

    public RelativePopupWindow() {
        super();
    }

    public RelativePopupWindow(View contentView) {
        super(contentView);
    }

    public RelativePopupWindow(int width, int height) {
        super(width, height);
    }

    public RelativePopupWindow(View contentView, int width, int height) {
        super(contentView, width, height);
    }

    public RelativePopupWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
    }

    /**
     * Show at relative position to anchor View.
     *
     * @param anchor   Anchor View
     * @param vertPos  Vertical Position Flag
     * @param horizPos Horizontal Position Flag
     */
    public void showOnAnchor(@NonNull View anchor, @VerticalPosition int vertPos, @HorizontalPosition int horizPos) {
        showOnAnchor(anchor, vertPos, horizPos, 0, 0);
    }

    /**
     * Show at relative position to anchor View with translation.
     *
     * @param anchor   Anchor View
     * @param vertPos  Vertical Position Flag
     * @param horizPos Horizontal Position Flag
     * @param x        Translation X
     * @param y        Translation Y
     */
    public void showOnAnchor(@NonNull View anchor, @VerticalPosition int vertPos, @HorizontalPosition int horizPos, int x, int y) {
        View contentView = getContentView();
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        final int measuredW = contentView.getMeasuredWidth();
        final int measuredH = contentView.getMeasuredHeight();
        switch (vertPos) {
            case VerticalPosition.ABOVE:
                y -= measuredH + anchor.getHeight();
                break;
            case VerticalPosition.ALIGN_BOTTOM:
                y -= measuredH;
                break;
            case VerticalPosition.CENTER:
                y -= anchor.getHeight() / 2 + measuredH / 2;
                break;
            case VerticalPosition.ALIGN_TOP:
                y -= anchor.getHeight();
                break;
            case VerticalPosition.BELOW:
                // Default position.
                break;
        }
        switch (horizPos) {
            case HorizontalPosition.LEFT:
                x -= measuredW;
                break;
            case HorizontalPosition.ALIGN_RIGHT:
                x -= measuredW - anchor.getWidth();
                break;
            case HorizontalPosition.CENTER:
                x += anchor.getWidth() / 2 - measuredW / 2;
                break;
            case HorizontalPosition.ALIGN_LEFT:
                // Default position.
                break;
            case HorizontalPosition.RIGHT:
                x += anchor.getWidth();
                break;
        }
        PopupWindowCompat.showAsDropDown(this, anchor, x, y, Gravity.NO_GRAVITY);
        setAlpha();

    }
    protected boolean isAlpha(){
        return false;
    }
    public void showOnAnchor(View anchor, Orientation orientation) {
        showOnAnchor(anchor, orientation, 0, 0);
    }

    public void showOnAnchor(View anchor, Orientation orientation, int x, int y) {
        Rect mRect = new Rect();
        x = DensityUtil.dip2px(mContext, x);
        y = DensityUtil.dip2px(mContext, y);
        int[] mLocation = new int[2];
        anchor.getLocationOnScreen(mLocation);
        mRect.set(mLocation[0], mLocation[1], mLocation[0] + anchor.getWidth(),
                mLocation[1] + anchor.getHeight());
//        getContentView().measure(0, 0);
        if (Orientation.left == orientation) {
            showAtLocation(anchor, Gravity.NO_GRAVITY, mRect.left
                            + x,
                    mRect.bottom + y);
        } else if (orientation == Orientation.center) {
            showAtLocation(anchor, Gravity.NO_GRAVITY, (mRect.left + mRect.right - width) / 2
                            + x,
                    mRect.bottom + y);
        } else if (orientation == Orientation.right) {
            showAtLocation(anchor, Gravity.NO_GRAVITY, mRect.right
                            - width + x,
                    mRect.bottom + y);
        }
        setAlpha();
    }

    public enum Orientation {
        left, right, center;
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    private void backgroundAlpha(Activity context, float bgAlpha,boolean isShow) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        if(isShow){
            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }else{
            context.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }

        context.getWindow().setAttributes(lp);
    }

    private DismissListener dismissListener;

    public void setDismissListener(DismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    public interface DismissListener {
        void dismiss();
    }

    /**
     * 恢复背景
     */
    public void changeLight2close() {
//        ((Activity)mContext).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        final ValueAnimator animation = ValueAnimator.ofFloat(0.5f, 1.0f);
        animation.setDuration(300);
        animation.start();
        final WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                lp.alpha = (float) valueAnimator.getAnimatedValue();
                ((Activity) mContext).getWindow().setAttributes(lp);
            }
        });

    }


    /**
     * 背景变暗
     */
    public void changeLight2Show() {
//        ((Activity)mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        final ValueAnimator animation = ValueAnimator.ofFloat(1.0f, 0.5f);
        animation.setDuration(300);
        animation.start();
        final WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                lp.alpha = (float) valueAnimator.getAnimatedValue();
                ((Activity) mContext).getWindow().setAttributes(lp);
            }
        });
    }
    protected boolean isDim(){
        return false;
    }
    protected void setAlpha() {
        if(isDim())
        backgroundAlpha(((Activity) mContext), 1.0f,true);
        if(isAlpha())
        changeLight2Show();
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                if(isDim())
                backgroundAlpha(((Activity) mContext), 1.0f,false);
                if(isAlpha())
                changeLight2close();
                if (dismissListener != null) {
                    dismissListener.dismiss();
                }
            }
        });
    }
}

