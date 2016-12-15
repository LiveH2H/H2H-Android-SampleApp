package com.meetingroom.utils;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;

public class StringUtil {
	public static String formatMeetingId(String reverseStr) {
		String resultStr = "";
		if (reverseStr == null) return resultStr;
		for (int i = 0; i < reverseStr.length(); i++) {
			if (i * 3 + 3 > reverseStr.length()) {
				resultStr += reverseStr.substring(i * 3, reverseStr.length());
				break;
			}
			resultStr += reverseStr.substring(i * 3, i * 3 + 3) + "-";
		}
		if (resultStr.endsWith("-")) {
			resultStr = resultStr.substring(0, resultStr.length() - 1);
		}
		return resultStr;
	}
	public static String captureName(String name) {
		//     name = name.substring(0, 1).toUpperCase() + name.substring(1);
//        return  name;
		char[] cs=name.toCharArray();
		cs[0]-=32;
		return String.valueOf(cs);

	}
	public static String addTab(int maxlength,int length){
		StringBuffer result = new StringBuffer();
		for(int i=0;i<maxlength-length;i++){
			result.append("  ");
		}
		return result.toString();
	}
	/**
	 * 半角转全角
	 * @param input String.
	 * @return 全角字符串.
	 */
	public static String ToSBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000';
			} else if (c[i] < '\177') {
				c[i] = (char) (c[i] + 65248);
			}
		}
		return new String(c);
	}

	/**
	 * 全角转半角
	 * @param input String.
	 * @return 半角字符串
	 */
	public static String ToDBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);
			}
		}
		String returnString = new String(c);
		return returnString;
	}
	public static String limitStringLength(String src,int count){
		return src.length()>count?src.substring(0,count):src;
	}

    public static Spanned fromHtml(Context context, int resId, Object... formatArgs) {
        String source = context.getString(resId, formatArgs);
        return fromHtml(source);
    }

    public static Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }
}
