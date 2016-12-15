package com.meetingroom.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.meetingroom.utils.Tools;
import com.mosai.utils.DensityUtil;

import itutorgroup.h2h.R;

/**
 * @author kakajika
 * @since 2016/07/01
 */
public class RelativeDialog extends Dialog {
    protected int width;
    protected int height;
    protected Context mContext;
    protected WindowManager.LayoutParams layoutParams;
    public RelativeDialog(Context context) {
        this(context, R.style.dialog);
    }

    public RelativeDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
    }
    protected View view;

    protected void initConfig() {
        setContentView(view);
        layoutParams = getWindow().getAttributes();
        layoutParams.width =width;
        layoutParams.height = height;
        layoutParams.gravity = Gravity.LEFT|Gravity.TOP;
//        layoutParams.gravity = Gravity.NO_GRAVITY;
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(true);
//        view.setLayoutParams(new ViewGroup.LayoutParams(width,height));
    }
    public void showOnAnchor(View anchor, Orientation orientation){
        showOnAnchor(anchor,orientation,0,0);
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
            layoutParams.x = mRect.left
                    + x;
            layoutParams.y = mRect.bottom + y;
        } else if (orientation == Orientation.center) {
            layoutParams.x = (mRect.left + mRect.right - width) / 2
                    + x;
            layoutParams.y =  mRect.bottom + y;
        } else if (orientation == Orientation.right) {
            layoutParams.x = mRect.right
                    - width + x;
            layoutParams.y =  mRect.bottom + y;
        }
        try {
            layoutParams.y = layoutParams.y- Tools.getStateBarHeight(mContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        show();
    }

    public enum Orientation {
        left, right, center;
    }


    private DismissListener dismissListener;

    public void setDismissListener(DismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    public interface DismissListener {
        void dismiss();
    }

}

