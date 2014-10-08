package com.oldfeel.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
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
		toast.show();
	}

	private ProgressDialog pd;

	public void showPd(Context context, String message) {
		if (pd != null) {
			pd.cancel();
		}
		pd = new ProgressDialog(context);
		pd.setTitle(message);
		pd.show();
	}

	public void cancelPd() {
		if (pd != null) {
			pd.cancel();
		}
	}

	/**
	 * 显示一个简易的dialog
	 * 
	 * @param activity
	 * @param text
	 */
	public void showSimpleDialog(Activity activity, String text,
			OnClickListener listener) {
		new AlertDialog.Builder(activity).setTitle(text)
				.setPositiveButton("确定", listener).show();
	}

	/**
	 * 显示一个简易的dialog
	 * 
	 * @param activity
	 * @param text
	 */
	public void showSimpleDialog(Activity activity, String text) {
		showSimpleDialog(activity, text, null);
	}
}
