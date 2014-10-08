package com.oldfeel.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
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
	public static String getString(EditText et) {
		return et.getText().toString().trim();
	}

	/**
	 * 获取edittext中的int类型
	 * 
	 * @param et
	 * @return
	 */
	public static int getInt(EditText et) {
		String string = getString(et);
		if (string == null || string.length() == 0) {
			return 0;
		}
		return Integer.valueOf(getString(et));
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

	/**
	 * 判断是否为手机号码
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(EditText mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(14[5,7])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(getString(mobiles));
		return m.matches();
	}

	/**
	 * 隐藏软键盘
	 * 
	 * @param editText
	 */
	public static void hideSoftKeyboard(Context context, EditText editText) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}

	/**
	 * 判断两个edittext内容是否一致
	 * 
	 * @param et1
	 * @param et2
	 * @return
	 */
	public static boolean isEquals(EditText et1, EditText et2) {
		if (isEmpty(et1) || isEmpty(et2)) {
			return false;
		}
		return getString(et1).equals(getString(et2));
	}

	/**
	 * 将edittext中光标移动到尾部
	 * 
	 * @param etId
	 */
	public static void setEnd(EditText et) {
		if (!isEmpty(et)) {
			et.setSelection(getString(et).length());
		}
	}

	/**
	 * 设置editetext是否可编辑
	 * 
	 * @param focusable
	 * @param editTexts
	 */
	public static void setETFocus(boolean focusable, EditText... editTexts) {
		for (EditText editText : editTexts) {
			editText.setFocusable(focusable);
		}
	}
}
