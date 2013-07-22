package com.example.library_netapi;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.content.Context;

/**
 * 网络请求参数
 * 
 * @author oldfeel
 * 
 */
public class NetParameters {

	private ArrayList<String> keys = new ArrayList<String>();
	private ArrayList<String> values = new ArrayList<String>();
	private String path = "";

	/**
	 * 构造一个不带提示框的NetParameters
	 * 
	 * @param path
	 */
	public NetParameters(String path) {
		this.path = path;
	}

	/**
	 * 构造函数.实例化该参数对象的同时弹出一个提示框,告诉用户应用正在做什么
	 * 
	 * @param context
	 * 
	 * @param path
	 *            这次请求需要调用的url
	 * @param text
	 *            弹出提示框显示的内容
	 */
	public NetParameters(Context context, String path, String text) {
		DialogUtil.getInstance().showPd(context, text);
		this.path = path;
	}

	/**
	 * 添加参数
	 * 
	 * @param key
	 * @param value
	 */
	public void addParams(String key, Object value) {
		if (!isEmpty(key) && !isEmpty(value.toString())) {
			keys.add(key);
			values.add(value.toString());
		}
	}

	/**
	 * 补充路径,比如添加 /信息类别id/新闻id
	 * 
	 * @param objects
	 */
	public void addPath(Object... objects) {
		for (Object object : objects) {
			path = path + "/" + object.toString();
		}
	}

	/**
	 * 检查该参数是否有内容,没有的话就不用添加了
	 * 
	 * @param str
	 * @return true为没有内容,false为有内容
	 */
	public static boolean isEmpty(CharSequence str) {
		if (str == null || str.length() == 0)
			return true;
		else
			return false;
	}

	/**
	 * 将参数对象转换成字符串类型.
	 */
	public String getPath() {
		if (keys == null || keys.size() == 0) {
			return path + "";
		}
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (int i = 0; i < keys.size(); i++) {
			if (first) {
				first = false;
				sb.append("?");
			} else {
				sb.append("&");
			}
			String key = keys.get(i);
			String value = values.get(i);
			if (value != null) {
				try {
					sb.append(URLEncoder.encode(key, "utf-8") + "="
							+ URLEncoder.encode(value, "utf-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		return path + sb.toString();
	}

}
