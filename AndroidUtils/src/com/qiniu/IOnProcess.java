package com.qiniu;

public interface IOnProcess {
	public void onProcess(long current, long total);
	public void onFailure(Exception ex);
}
