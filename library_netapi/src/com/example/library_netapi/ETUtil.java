package com.example.library_netapi;

import android.widget.EditText;

/**
 * 处理edittext的帮助类
 * 
 * @author oldfeel
 * 
 */
public class ETUtil {
	/**
	 * 获取edittext中内容
	 * 
	 * @param et
	 * @return
	 */
	public static String getText(EditText et) {
		return et.getText().toString().trim();
	}

	/**
	 * 判断edittext是否为空
	 * 
	 * @param editTexts
	 * @return
	 */
	public static boolean isEmpty(EditText... editTexts) {
		for (EditText editText : editTexts) {
			String content = editText.getText().toString().trim();
			if (content == null || content.length() == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 将edittext回复为空
	 * 
	 * @param editTexts
	 */
	public static void setEmpty(EditText... editTexts) {
		for (EditText editText : editTexts) {
			editText.setText("");
		}
	}
}
