package com.mosai.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

public class MyLog {

	private static final String PATH = "/ltdz";

	public static void i(String TAG, String msg) {
		if(BuildConfig.DEBUG){
			Log.i(TAG, msg);
		}
	}

	public static void e(String TAG, String msg) {
		if(BuildConfig.DEBUG){
			Log.e(TAG, msg);
		}
	}

	public static void d(String TAG, String msg) {
		if(BuildConfig.DEBUG){
			Log.d(TAG, msg);
		}
//		if(TAG.equals("SpeedTestService")){
//			writeFileSdcard(msg, 1);
//		}
	}
	
	public static void w(String TAG,String msg){
		if(BuildConfig.DEBUG){
			Log.w(TAG, msg);
		}
	}
	
	public static void v(String TAG,String msg){
		if(BuildConfig.DEBUG){
			Log.v(TAG, msg);
		}
	}
	
	/**
	 * 写日志到logerr文件
	 * @param content
	 */
	public static void errorWriteFile(Context context,String content){
		writeFileSdcard(context,content, 2);
	}
	
	/**
	 * 写日志到loginfo文件
	 * @param content
	 */
	public static void infoWriteFile(Context context,String content){
		writeFileSdcard(context,content, 1);
	}

	private static void isExist(String path) {
		File file = new File(Environment.getExternalStorageDirectory() + PATH);
		if (!file.exists()) {
			file.mkdir();
		}
		file = new File(path);
		if (!file.exists()) {
			file.mkdir();
		}
	}
	
	private static void writeFileSdcard(Context context,String message, int type) {
		String tag = Tools.getApplicationName(context);
		writeFileSdcard(message, type,tag);
	}
	
	public static void infoWriteFile(String message,String tag) {
		writeFileSdcard(message, 1,tag);
	}
	
	public static void writeFileSdcard(String message, int type,String tag) {
		try {
			boolean sdCardExist = Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
			if (sdCardExist) {
				String fileName = "";
				if (type == 2) {
					isExist(Environment.getExternalStorageDirectory() + PATH + "/logerr");
					fileName = Environment.getExternalStorageDirectory() + PATH + "/logerr/" + tag
							+ DateTimeUtil.getCurrDateStr().replace("-", "") + ".log";
				} else if (type == 1) {
					isExist(Environment.getExternalStorageDirectory() + PATH + "/loginfo");
					fileName = Environment.getExternalStorageDirectory() + PATH + "/loginfo/" + tag
							+ DateTimeUtil.getCurrDateStr().replace("-", "") + ".log";
				}
				message = "\r\n" + DateTimeUtil.getCurrDateTimeStrMill() + "\t" + message;
				FileOutputStream fout = new FileOutputStream(fileName, true);
				byte[] bytes = message.getBytes();
				fout.write(bytes);
				fout.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
