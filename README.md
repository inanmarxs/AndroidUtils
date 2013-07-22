android_netapi
==============

简化http get请求的操作，包括异步请求/添加参数/路径。


		NetParameters np = new NetParameters(PATH);
		np.addPath("User", "App_Login.ashx");
		np.addParams("UserName", "username");
		np.addParams("UserPwd", "userpwd");
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
