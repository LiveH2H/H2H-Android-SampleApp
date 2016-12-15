package fm.jiecao.jcvideoplayer_lib;

import android.util.Log;

/**
 * Created by Rays on 2016/11/24.
 */

class JCLogUtil {
    private static final boolean IS_PRINT = false;

    public static void i(String tag, String msg) {
        if (IS_PRINT) {
            Log.i(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (IS_PRINT) {
            Log.e(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (IS_PRINT) {
            Log.d(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (IS_PRINT) {
            Log.v(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (IS_PRINT) {
            Log.w(tag, msg);
        }
    }
}
