package com.oldfeel.base;

import java.io.Serializable;

import com.oldfeel.utils.StringUtil;

/**
 * item 基类
 * 
 * @author oldfeel
 * 
 *         Created on: 2014-1-14
 */
public abstract class BaseItem implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 获取时间
	 * 
	 * @param time
	 * @return
	 */
	public String getTime(String time) {
		return StringUtil.friendly_time(time);
	}
}
