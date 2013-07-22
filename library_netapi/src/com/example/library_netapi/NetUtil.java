package com.example.library_netapi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

public class NetUtil {

	/**
	 * 判断网络连接
	 * */
	public static boolean isNetworkConnect(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
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
	public static void whetherOpenNet(final Context context) {
		new AlertDialog.Builder(context)
				.setTitle("网络木有连接")
				.setMessage("是否打开网络连接")
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								context.startActivity(new Intent(
										Settings.ACTION_WIRELESS_SETTINGS));
							}
						}).setNeutralButton(android.R.string.cancel, null)
				.show();
	}

	// 从服务器下载apk:
	public static File getFileFromServer(String path, ProgressDialog pd)
			throws Exception {
		// 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			URL url = new URL(path);
			Log.i("pathyou", path + "");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			// 获取到文件的大小
			pd.setMax(conn.getContentLength());
			InputStream is = conn.getInputStream();
			File dir = new File(Environment.getExternalStorageDirectory()
					+ "/wine/");
			if (!dir.exists()) {
				dir.mkdir();
			}
			String name = path.substring(path.lastIndexOf("/") + 1,
					path.length());
			Log.i("name", name + "");
			File file = new File(dir, name);
			FileOutputStream fos = new FileOutputStream(file);

			byte[] buffer = new byte[1024];
			int len;
			int total = 0;
			while ((len = is.read(buffer)) != -1) {
				Log.i("len", len + "");
				fos.write(buffer, 0, len);
				total += len;
				// 获取当前下载量
				pd.setProgress(total);
			}
			fos.close();
			is.close();
			pd.dismiss();
			return file;
		} else {
			return null;
		}
	}

}
