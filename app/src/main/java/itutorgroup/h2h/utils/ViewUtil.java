package itutorgroup.h2h.utils;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import itutorgroup.h2h.R;


public class ViewUtil {

	public static <T> T findViewById(View v, int id) {
        //noinspection unchecked
        return (T) v.findViewById(id);
	}

	public static <T> T findViewById(Activity activity, int id) {
        //noinspection unchecked
        return (T) activity.findViewById(id);
	}

	/**
	 * 该方法使api 19以上的设备状态栏颜色改变
	 * 注意：状态栏的颜色可以在主题的status_bar_bg里面设置，默认为黑色
	 * @param activity
	 */
	public static void initStatusBar(Activity activity) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			try {
				int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
				if (resourceId > 0) {
					int statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
					TypedArray array = activity.getTheme().obtainStyledAttributes(new int[]{R.attr.colorPrimaryDark});
					int backgroundColor = array.getColor(0, Color.TRANSPARENT);
					array.recycle();
					ViewGroup contentParent = (ViewGroup) activity.findViewById(android.R.id.content).getParent();
					View view = new View(activity);
					view.setTag("statusBar");
					view.setBackgroundColor(backgroundColor);
					ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
					contentParent.addView(view, 0, layoutParams);
					activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void setStatusBarBg(Activity activity, int bg) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			ViewGroup viewGroup = (ViewGroup) activity.findViewById(android.R.id.content).getParent();
			View view = viewGroup.findViewWithTag("statusBar");
			if (view != null) {
				view.setBackgroundColor(bg);
			}
		}
	}

}
