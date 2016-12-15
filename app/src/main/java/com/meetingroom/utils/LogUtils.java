package com.meetingroom.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.mosai.utils.DateTimeUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import itutorgroup.h2h.BuildConfig;
import itutorgroup.h2h.R;

public class LogUtils {
	private static final String DEFAULT_TAG = "LogUtils";
	private static final boolean IS_PRINT = BuildConfig.DEBUG;

	public static void v(String msg) {
		if (IS_PRINT) {
			Log.v(DEFAULT_TAG, msg);
		}
	}

	public static void d(String msg) {
		if (IS_PRINT) {
			Log.d(DEFAULT_TAG, msg);
		}
	}

	public static void i(String msg) {
		if (IS_PRINT) {
			Log.i(DEFAULT_TAG, msg);
		}
	}

	public static void w(String msg) {
		if (IS_PRINT) {
			Log.w(DEFAULT_TAG, msg);
		}
	}

	public static void e(String msg) {
		if (IS_PRINT) {
			Log.e(DEFAULT_TAG, msg);
		}
	}
	
	public static void e(String msg, Throwable tr) {
		if (IS_PRINT) {
			Log.e(DEFAULT_TAG, msg, tr);
		}
	}

	public static void v(String tag, String msg) {
		if (IS_PRINT) {
			Log.v(tag, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (IS_PRINT) {
			Log.d(tag, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (IS_PRINT) {
			Log.i(tag, msg);
		}
	}

	public static void w(String tag, String msg) {
		if (IS_PRINT) {
			Log.w(tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (IS_PRINT) {
			Log.e(tag, msg);
		}
	}

	public static void e(String tag, String msg, Throwable tr) {
		if (IS_PRINT) {
			Log.e(tag, msg, tr);
		}
	}

    public static void writeLog(Context mContext, String logName, String content) {
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = Environment.getExternalStorageDirectory().getPath() + File.separator + "mosai" + File.separator;
                File dir = new File(path);
                if (!dir.exists() && !dir.mkdirs()) {
                    return;
                }
                long timestamp = System.currentTimeMillis();
                String time = DateTimeUtil.getCurrDateTimeStr().replace(":", "_");
                String fileName = String.format(Locale.getDefault(), "%s-%s-%s-%d.log", mContext.getResources().getString(R.string.app_name), logName, time, timestamp);
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(content.getBytes());
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
