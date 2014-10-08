package com.oldfeel.utils;

import android.content.pm.PackageManager.NameNotFoundException;
import android.support.v4.app.FragmentActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * 版本工具类
 * 
 * @author oldfeel
 * 
 */
public class VersionUtil {
	/**
	 * 获取应用的版本名字
	 * 
	 * @param activity
	 * @return
	 */
	public static String getVersionName(FragmentActivity activity) {
		try {
			return activity.getPackageManager().getPackageInfo(
					activity.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "1.0.0";
	}

	/**
	 * true为需要更新,false为不需要更新
	 * 
	 * @param activity
	 * @param result
	 * @return
	 */
	public static boolean isNeedUpdate(FragmentActivity activity, String result) {
		String versionName = getVersionName(activity);
		String newVersion = getNewVersion(result);
		return !versionName.equals(newVersion);
	}

	/**
	 * 解析json,获取新版本
	 * 
	 * @param result
	 * @return
	 */
	public static String getNewVersion(String result) {
		JsonObject json = new Gson().fromJson(result, JsonObject.class);
		JsonObject data = json.getAsJsonObject("data");
		String versionname = data.get("versionname").getAsString();
		return versionname;
	}

}
