package com.mosai.utils;

import android.app.Activity;
import android.view.View;

public class ViewUtil {

	public static <T> T findViewById(View v, int id) {
        //noinspection unchecked
        return (T) v.findViewById(id);
	}

	public static <T> T findViewById(Activity activity, int id) {
        //noinspection unchecked
        return (T) activity.findViewById(id);
	}


}
