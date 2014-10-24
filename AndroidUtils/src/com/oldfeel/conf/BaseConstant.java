package com.oldfeel.conf;

/**
 * 框架中需要的一些常量
 * 
 * @author oldfeel
 * 
 *         Create on: 2014年10月8日
 */
public class BaseConstant {

	private static BaseConstant baseConstant;
	private int pageSize = 20; // listview 每次加载的item数量
	private String rootUrl; // 网络请求的根url

	public static BaseConstant getInstance() {
		if (baseConstant == null) {
			baseConstant = new BaseConstant();
		}
		return baseConstant;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getRootUrl() {
		return rootUrl;
	}

	public void setRootUrl(String rootUrl) {
		this.rootUrl = rootUrl;
	}

}
