package com.oldfeel.utils;

import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.oldfeel.interfaces.MyLocationListener;

/**
 * 常用工具类
 * 
 * @author oldfeel
 * 
 *         Create on: 2014年7月27日
 */
public class Utils {
	/**
	 * 打电话
	 * 
	 * @param context
	 * @param phone
	 */
	public static void call(Context context, String phone) {
		Intent intent = new Intent("android.intent.action.CALL",
				Uri.parse("tel:" + phone));
		context.startActivity(intent);
	}

	/**
	 * 
	 * @return true为中国语言,false为不是中国语言
	 */
	public static boolean isChinese() {
		String def = Locale.getDefault().toString();
		String china = Locale.CHINA.toString();
		String chinese = Locale.CHINESE.toString();
		return def.equals(china) || def.equals(chinese);
	}

	/**
	 * 开始定位
	 * 
	 * @param activity
	 * @param myLocationListener
	 */
	public static void startLocation(Activity activity,
			MyLocationListener myLocationListener) {
		if (isChinese()) {
			startLocationBaidu(activity, myLocationListener);
		} else {
			startLocationGoogle(activity, myLocationListener);
		}

	}

	/**
	 * 谷歌定位
	 * 
	 * @param activity
	 * @param myLocationListener
	 */
	private static void startLocationGoogle(Activity activity,
			MyLocationListener myLocationListener) {
	}

	/**
	 * 百度定位
	 * 
	 * @param activity
	 * @param myLocationListener
	 */
	private static void startLocationBaidu(final Activity activity,
			final MyLocationListener myLocationListener) {
		final LocationClient myLocationClient = new LocationClient(activity);
		myLocationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				myLocationListener.location(location.getLatitude(),
						location.getLongitude());
				myLocationClient.stop();
				DialogUtil.getInstance().cancelPd();
				DialogUtil.getInstance().showToast(activity, "定位成功");
			}
		});
		myLocationClient.start();
		DialogUtil.getInstance().showPd(activity, "正在获取位置信息...");
	}
}
