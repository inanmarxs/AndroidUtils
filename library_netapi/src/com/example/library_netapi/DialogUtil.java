package com.example.library_netapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

/**
 * 该类中的toast会覆盖上一个toast
 */
public class DialogUtil {
	private static DialogUtil tu;

	public static DialogUtil getInstance() {
		if (tu == null) {
			tu = new DialogUtil();
		}
		return tu;
	}

	private static Toast toast;

	public void showToast(Context context, String text) {
		if (toast != null) {
			toast.cancel();
		}
		toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	private ProgressDialog pd;

	public void showPd(Context context, String message) {
		Log.d("example", "start request");
		if (pd != null) {
			pd.cancel();
		}
		pd = new ProgressDialog(context);
		pd.setTitle(message);
		pd.show();
	}

	public void cancelPd() {
		Log.d("example", "finish request");
		if (pd != null) {
			pd.cancel();
		}
	}

	/**
	 * 显示暂无数据的dialog
	 * 
	 * @param activity
	 */
	public void showEmpty(final Activity activity) {
		new AlertDialog.Builder(activity)
				.setTitle("抱歉,这里暂时没有数据...\n返回上一页面?")
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								activity.finish();
							}
						}).show();
	}
}
