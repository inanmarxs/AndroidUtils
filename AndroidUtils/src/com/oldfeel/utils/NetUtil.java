package com.oldfeel.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.widget.Toast;

import com.oldfeel.conf.BaseConstant;
import com.qiniu.IO;
import com.qiniu.JSONObjectRet;
import com.qiniu.PutExtra;

/**
 * 网络接口,post请求string或者get请求json,里面只包含一个线程,只能同时发送一个网络请求
 * 
 * @author oldfeel
 * 
 */
public class NetUtil extends Handler {
	/** 打开网络连接 */
	public static final int OPEN_NETWORK = -1;
	/** 超时时间限制 */
	public static final int TIME_OUT = 30 * 1000;
	private Activity activity;
	private JSONObject params = new JSONObject();
	private ProgressDialog pd;
	private Thread requestThread;
	private AlertDialog dialog;
	private String api;

	/**
	 * 构造一个netapi对象
	 * 
	 * @param Activity
	 * 
	 * @param api
	 *            这次请求需要调用的api
	 */
	public NetUtil(Activity Activity, String api) {
		this.activity = Activity;
		this.api = api;
	}

	/**
	 * 添加参数
	 * 
	 * @param key
	 * @param value
	 */
	public void setParams(String key, Object value) {
		if (!isEmpty(key) && !isEmpty(value)) {
			try {
				params.put(key.trim(), value.toString().trim());// *.trim(),取消首尾空格
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 检查该参数是否有内容,没有的话就不用添加了
	 * 
	 * @param str
	 * @return true为没有内容,false为有内容
	 */
	public boolean isEmpty(Object str) {
		if (str == null || str.toString().length() == 0)
			return true;
		else
			return false;
	}

	/**
	 * 显示进度条提示
	 * 
	 * @param message
	 */
	private void showPd(String message) {
		if (message == null || message.length() == 0) {
			return;
		}
		if (pd != null) {
			pd.cancel();
			pd = null;
		}
		pd = new ProgressDialog(activity);
		pd.setMessage(message);
		pd.setCanceledOnTouchOutside(false);
		pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				if (failListener != null) {
					failListener.cancel();
				}
				if (requestThread != null) {
					requestThread.interrupt();
				}
			}
		});
		pd.setCancelable(true);
		pd.show();
	}

	public void postRequest(int resId, RequestStringListener stringListener) {
		postRequest(activity.getString(resId), stringListener);
	}

	/**
	 * 发起一个post请求,返回string对象
	 * 
	 * @param stringListener
	 * @param isLoadCache
	 *            true 为加载缓存，false为不加载缓存
	 */
	public void postRequest(final String text,
			final RequestStringListener stringListener) {
		if (!isNetworkConnect()) {
			whetherOpenNet();
			return;
		}
		showPd(text);
		Runnable task = new Runnable() {

			@Override
			public void run() {
				try {
					final String result = postStringResult();
					if (requestThread.isInterrupted()) {
						showLog("is interrupted");
						return;
					}
					if (result == null) {
						netError();
						return;
					}
					post(new Complete() {

						@Override
						public void run() {
							super.run();
							if (stringListener != null) {
								stringListener.onComplete(result);
							}
						}
					});
				} catch (SocketTimeoutException e) {
					timeOut(text, stringListener);
					e.printStackTrace();
				} catch (Exception e) {
					netError();
					e.printStackTrace();
				}

			}
		};
		requestThread = new Thread(task);
		requestThread.start();
	}

	/**
	 * 发送一个post请求,不提示,不处理返回数据
	 */
	public void postRequest() {
		this.postRequest(null, null);
	}

	/**
	 * 网络连接超时
	 * 
	 * @param text
	 * @param stringListener
	 */
	protected void timeOut(final String text,
			final RequestStringListener stringListener) {
		Looper.prepare();
		post(new Complete() {

			@Override
			public void run() {
				super.run();
				if (failListener != null) {
					failListener.onTimeOut();
				}
				new AlertDialog.Builder(activity)
						.setTitle("网络连接超时")
						.setNegativeButton("取消", null)
						.setPositiveButton("重连",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										showLog("重连");
										postRequest(text, stringListener);
									}
								}).show();
			}
		});

	}

	/**
	 * 网络连接错误或返回数据为空
	 * 
	 */
	protected void netError() {
		Looper.prepare();
		post(new Complete() {

			@Override
			public void run() {
				super.run();
				if (failListener != null) {
					failListener.onError();
				}
				if (!isNetworkConnect()) {
					whetherOpenNet();
				} else {
					Toast.makeText(activity, "网络连接失败,请稍后重试", Toast.LENGTH_LONG)
							.show();
				}
			}
		});
	}

	/**
	 * 发送post上传文件,获取字符串结果
	 * 
	 * @param isLoadCache
	 *            true为加载缓存，false为不加载缓存
	 * @throws JSONException
	 * @throws Exception
	 */
	public String postStringResult() throws SocketTimeoutException,
			JSONException {
		try {
			URL url = new URL(BaseConstant.getInstance().getRootUrl() + api);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(TIME_OUT);
			conn.setReadTimeout(TIME_OUT);
			conn.connect();

			DataOutputStream outStream = new DataOutputStream(
					conn.getOutputStream());
			Iterator<?> iterator = params.keys();
			StringBuilder sb = new StringBuilder();
			while (iterator.hasNext()) {
				String key = iterator.next().toString();
				try {
					outStream.writeBytes("&"
							+ URLEncoder.encode(key, "utf-8")
							+ "="
							+ URLEncoder.encode(params.get(key).toString(),
									"utf-8"));
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}

				// 打印出get方法请求的url
				if (sb.length() == 0) {
					sb.append("?");
				} else {
					sb.append("&");
				}
				sb.append(URLEncoder.encode(key, "utf-8")
						+ "="
						+ URLEncoder
								.encode(params.get(key).toString(), "utf-8"));
			}
			LogUtil.showLog("request url is "
					+ BaseConstant.getInstance().getRootUrl() + api
					+ sb.toString());
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				StringBuffer out = new StringBuffer();
				BufferedReader input = new BufferedReader(
						new InputStreamReader(conn.getInputStream()));
				String line = null;
				while ((line = input.readLine()) != null) {
					out.append(line);
				}
				input.close();
				String result = out.toString();
				return result;
			}
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	class Complete implements Runnable {
		@Override
		public void run() {
			if (pd != null) {
				pd.cancel();
			}
			requestThread.interrupt();
		}
	}

	/**
	 * 判断网络连接
	 */
	private boolean isNetworkConnect() {
		ConnectivityManager cm = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		cm.getActiveNetworkInfo();
		if (cm.getActiveNetworkInfo() != null) {
			return cm.getActiveNetworkInfo().isAvailable();
		}
		return false;
	}

	/**
	 * 打开网络对话框
	 */
	private void whetherOpenNet() {
		if (dialog != null) {
			dialog.cancel();
			dialog = null;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle("网络木有连接");
		builder.setMessage("是否打开网络连接");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				activity.startActivity(new Intent(
						Settings.ACTION_WIRELESS_SETTINGS));
			}
		});
		builder.setNegativeButton("取消", null);
		dialog = builder.create();
		dialog.show();
	}

	/**
	 * 打印日志
	 * 
	 * @param log
	 */
	private void showLog(String log) {
		LogUtil.showLog(log);
	}

	/**
	 * 获取当前NetApi绑定的activity
	 * 
	 * @return
	 */
	public Activity getActivity() {
		return this.activity;
	}

	/**
	 * 请求string的监听
	 */
	public interface RequestStringListener {
		/** 返回字符串 */
		public void onComplete(String result);
	}

	/**
	 * 网络连接失败,包括取消请求/网络错误/网络延时
	 * 
	 * @author oldfeel
	 * 
	 *         Create on: 2014年4月18日
	 */
	public interface OnNetFailListener {
		public void cancel();

		public void onError();

		public void onTimeOut();
	}

	private OnNetFailListener failListener;

	/**
	 * 取消网络请求监听。。。
	 * 
	 * @param cancelListener
	 */
	public void setOnNetFailListener(OnNetFailListener cancelListener) {
		this.failListener = cancelListener;
	}

	/**
	 * 上传文件,七牛云专用
	 *
	 * @param text
	 * @param file
	 * @param uptoken
	 * @param key
	 * @param requestStringListener
	 */
	public void postFile(String text, String key, File file, String uptoken,
			final RequestStringListener requestStringListener) {
		showPd(text);
		PutExtra extra = new PutExtra();
		IO.putFile(getActivity(), uptoken, key, Uri.fromFile(file), extra,
				new JSONObjectRet() {
					@Override
					public void onProcess(long current, long total) {
					}

					@Override
					public void onSuccess(JSONObject resp) {
						requestStringListener.onComplete(resp.toString());
						if (pd != null) {
							pd.cancel();
							pd = null;
						}
					}

					@Override
					public void onFailure(Exception ex) {
						requestStringListener.onComplete(ex.toString());
						if (pd != null) {
							pd.cancel();
							pd = null;
						}
						LogUtil.showLog("fail " + ex);
					}
				});
	}
}