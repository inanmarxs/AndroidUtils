package com.example.library_netapi;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

public class NetApi {
	private static String session_value;

	/**
	 * 该请求对网络是否连接进行监听,如果没有连接,则弹出是否连接的dialog
	 * 
	 * @param context
	 * @param np
	 * @param requestListener
	 */
	public static void request(final Activity activity, final Handler handler,
			final NetParameters np, final RequestListener requestListener) {
		new Thread() {
			@Override
			public void run() {
				try {
					JSONObject json = getJSONResult(np);
					if (json == null) {
						netError(activity, handler);
						return;
					}
					requestListener.onComplete(json);
				} catch (Exception e) {
					netError(activity, handler);
					e.printStackTrace();
				}
			};
		}.start();
	}

	/**
	 * 网络连接错误
	 * 
	 * @param context
	 */
	protected static void netError(final Activity activity,
			final Handler handler) {
		LogUtil.showLog("网络连接错误...");
		Looper.prepare();
		handler.post(new Runn() {
			@Override
			public void run() {
				super.run();
				// 判断是否网络链接
				if (!NetUtil.isNetworkConnect(activity.getApplicationContext())) {
					NetUtil.whetherOpenNet(activity.getApplicationContext());
				} else {
					LogUtil.showLog("弹出暂无数据 toast");
					DialogUtil.getInstance().showToast(activity, "暂无数据");
					// 弹出暂无数据 dialog
					// DialogUtil.getInstance().showEmpty(activity);
				}
			}
		});
	}

	/**
	 * 获取json字符串
	 * 
	 * @param params
	 *            url请求的参数
	 * @return json 字符串
	 * @throws IOException
	 * @throws JSONException
	 */
	public static JSONObject getJSONResult(NetParameters params)
			throws IOException, JSONException {
		String path = params.getPath();
		LogUtil.showLog("path is " + path);
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		if (session_value != null && session_value.length() != 0) {
			conn.setRequestProperty("Cookie", session_value);
		}
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(8 * 1000);
		conn.setReadTimeout(8 * 1000);
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			LogUtil.showLog("获取session");
			String tmp = conn.getHeaderField("Set-Cookie");
			if (tmp != null) {
				session_value = tmp;
			}
			LogUtil.showLog("session values is --->" + session_value);
			StringBuffer out = new StringBuffer();
			InputStreamReader in = new InputStreamReader(conn.getInputStream(),
					"UTF-8");
			char[] buffer = new char[4 * 1024];
			int count;
			while ((count = in.read(buffer, 0, buffer.length)) != -1) {
				out.append(buffer, 0, count);
			}
			in.close();
			// 解决文本内容中带<>无法解析的问题...
			String data = out.toString();
			data = data.replaceAll("&lt", "<");
			data = data.replaceAll("&gt", ">");
			data = data.replace("&nbsp;", " ");
			return new JSONObject(data);
		}
		return null;
	}

	/**
	 * 发起访问接口的请求时所需的回调接口
	 */
	public interface RequestListener {
		/**
		 * 用于获取服务器返回的响应内容
		 */
		public void onComplete(JSONObject result);
	}

}
