package com.oldfeel.conf;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * 监听异常信息的application
 * 
 * @author oldfeel
 * 
 *         Created on: 2014-2-8
 */
public class MyApplication extends Application {
	private ArrayList<Activity> list = new ArrayList<Activity>();

	@Override
	public void onCreate() {
		super.onCreate();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).build();
		ImageLoader.getInstance().init(config);
	}

	/**
	 * Activity关闭时，删除Activity列表中的Activity对象
	 */
	public void removeActivity(Activity a) {
		list.remove(a);
	}

	/**
	 * 向Activity列表中添加Activity对象
	 */
	public void addActivity(Activity a) {
		list.add(a);
	}

	/**
	 * 关闭Activity列表中的所有Activity
	 */
	public void exit() {
		for (Activity activity : list) {
			if (null != activity) {
				activity.finish();
			}
		}
		// 杀死该应用进程
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	/**
	 * 获取最后一个activity,就是当前显示的activity
	 * 
	 * @return
	 */
	public Activity currentActivity() {
		return list.get(list.size() - 1);
	}
}