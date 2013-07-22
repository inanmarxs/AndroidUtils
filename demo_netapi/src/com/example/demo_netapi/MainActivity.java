package com.example.demo_netapi;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.library_netapi.LogUtil;
import com.example.library_netapi.NetApi;
import com.example.library_netapi.NetApi.RequestListener;
import com.example.library_netapi.NetParameters;
import com.example.library_netapi.Runn;

public class MainActivity extends Activity {
	private static final String LOGIN_PATH = "http://app.9998.tv/User/App_Login.ashx?UserName=username&UserPwd=password";
	private static final String CHECK_SESSION_PATH = "http://app.9998.tv/User/Session";
	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	/**
	 * 开始登录
	 * 
	 * @param view
	 */
	public void startLogin(View view) {
		NetParameters np = new NetParameters(LOGIN_PATH);
		NetApi.request(MainActivity.this, handler, np, new RequestListener() {
			@Override
			public void onComplete(final JSONObject result) {
				handler.post(new Runn() {
					@Override
					public void run() {
						LogUtil.showLog(result.toString());
						super.run();
					}
				});
			}
		});
	}

	/**
	 * 检查session是否获取成功
	 * 
	 * @param view
	 */
	public void checkSession(View view) {
		NetParameters np = new NetParameters(CHECK_SESSION_PATH);
		NetApi.request(MainActivity.this, handler, np, new RequestListener() {
			@Override
			public void onComplete(final JSONObject result) {
				handler.post(new Runn() {
					@Override
					public void run() {
						LogUtil.showLog(result.toString());
						super.run();
					}
				});
			}
		});
	}
}
