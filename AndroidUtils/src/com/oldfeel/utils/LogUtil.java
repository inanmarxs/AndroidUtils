package com.oldfeel.utils;

import android.util.Log;

/**
 * 打印日志工具类
 * 
 * @author oldfeel
 * 
 *         Created on: 2014-1-10
 */
public class LogUtil {
	/** true 为开启打印logcat,false为不打印 */
	public static final boolean IS_DEBUG = true;

	/**
	 * 打印日志
	 * 
	 * @param log
	 */
	public static void showLog(String log) {
		if (IS_DEBUG) {
			Log.d("example", log);
		}
	}
}
