package com.example.library_netapi;

import android.util.Log;

public class LogUtil {
	public static boolean isDebug = true;

	public static void showLog(Object log) {
		if (isDebug)
			Log.d("example", log.toString());
	}
}
