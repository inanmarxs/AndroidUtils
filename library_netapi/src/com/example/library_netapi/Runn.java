package com.example.library_netapi;

/**
 * 自定义的Runnable,用来取消ProgressDialog
 */
public class Runn implements Runnable {
	public Runn() {
		DialogUtil.getInstance().cancelPd();
	}

	@Override
	public void run() {
	}

}
