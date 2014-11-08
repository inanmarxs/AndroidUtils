package com.oldfeel.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * json工具类
 * 
 * @author oldfeel
 * 
 *         Created on: 2014年2月17日
 */
public class JsonUtil {
	/**
	 * 判断是否成功
	 * 
	 * @param result
	 * @return
	 */
	public static boolean isSuccess(String result) {
		try {
			JSONObject json = new JSONObject(result);
			return json.getInt("Code") == 0;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String getData(String result) {
		try {
			JSONObject json = new JSONObject(result);
			return json.get("Data").toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
