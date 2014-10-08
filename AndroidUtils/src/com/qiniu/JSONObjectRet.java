package com.qiniu;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class JSONObjectRet extends CallRet {
	public JSONObjectRet() {
	}

	protected int mIdx;

	public JSONObjectRet(int idx) {
		mIdx = idx;
	}

	@Override
	public void onSuccess(byte[] body) {
		if (body == null) {
			onSuccess(new JSONObject());
		}
		try {
			String result = new String(body);
			JSONObject obj = new JSONObject(result);
			onSuccess(obj);
		} catch (JSONException e) {
			e.printStackTrace();
			onFailure(new Exception(new String(body)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public abstract void onSuccess(JSONObject obj);
}
