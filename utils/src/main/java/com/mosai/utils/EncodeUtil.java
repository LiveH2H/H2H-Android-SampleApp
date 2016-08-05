package com.mosai.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EncodeUtil {
	
	/**
	 * 中文编码
	 * @param url
	 * @param charsetName
	 * @return
	 */
	public static String encode(String url,String charsetName){
		try {
			Matcher matcher = Pattern.compile("[\\u4e00-\\u9fa5]").matcher(url);
			while (matcher.find()) {
				String tmp = matcher.group();
				url = url.replaceAll(tmp,URLEncoder.encode(tmp, charsetName));
			}
			url = URLEncoder.encode(url,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url;
	}
	
	/**
	 * 中文编码(UTF-8)
	 * @param url
	 * @return
	 */
	public static String encode(String url) {
			return  encode(url,"UTF-8");
	}
	public static String encodeUrl(String url){
		return encodeUrl(url,"UTF-8");
	}
	public static String encodeUrl(String url,String charsetName){
		try {
			url = URLEncoder.encode(url,charsetName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url;
	}
}
