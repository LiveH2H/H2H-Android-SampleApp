package com.mosai.utils;

import android.content.Context;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AssetsUtil {
	
	/**
	 * 复制Assets目录下文件到sd卡
	 * @param context
	 * @param assetsFileName
	 * @param outFileName
	 * @throws IOException
	 */
	public static void copyToSD(Context context, String assetsFileName,String outFileName) throws IOException {
		InputStream myInput;
		OutputStream myOutput = new FileOutputStream(outFileName);
		myInput = context.getAssets().open(assetsFileName);
		byte[] buffer = new byte[1024];
		int length = myInput.read(buffer);
		while (length > 0) {
			myOutput.write(buffer, 0, length);
			length = myInput.read(buffer);
		}
		myOutput.flush();
		myInput.close();
		myOutput.close();
	}
}
