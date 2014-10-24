package com.oldfeel.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.VideoView;

import com.baidu.mobstat.StatService;
import com.oldfeel.utils.DialogUtil;
import com.oldfeel.utils.ETUtil;
import com.oldfeel.utils.R;
import com.oldfeel.utils.ViewUtil;

/**
 * dialogfragment基类
 * 
 * @author oldfeel
 * 
 *         Created on: 2014-1-14
 */
public class BaseDialogFragment extends DialogFragment {

	public OkListener okListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_TITLE,
				R.style.Theme_AppCompat_Light_Dialog);
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);
	}

	/**
	 * 确定按钮监听
	 * 
	 * @author oldfeel
	 * 
	 *         Created on: 2014-1-15
	 */
	public interface OkListener {
		public void ok(Object... objects);
	}

	public void setListener(OkListener okListener) {
		this.okListener = okListener;
	}

	public interface onCreatedListener {
		public void created(Fragment fragment);
	}

	protected onCreatedListener createdListener;

	public void setOnCreatedListener(onCreatedListener createdListener) {
		this.createdListener = createdListener;
	}

	public VideoView getVideoView(View view, int id) {
		return ViewUtil.getVideoView(view, id);
	}

	public EditText getEditText(View view, int id) {
		return ViewUtil.getEditText(view, id);
	}

	public ImageView getImageView(View view, int id) {
		return ViewUtil.getImageView(view, id);
	}

	public GridView getGridView(View view, int id) {
		return ViewUtil.getGridView(view, id);
	}

	public Button getButton(View view, int id) {
		return ViewUtil.getButton(view, id);
	}

	public void showToast(String text) {
		DialogUtil.getInstance().showToast(getActivity(), text);
	}

	public String getString(EditText et) {
		return ETUtil.getString(et);
	}

	@Override
	public void onPause() {
		StatService.onPause(this);
		super.onPause();
	}

	@Override
	public void onResume() {
		StatService.onResume(this);
		super.onResume();
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		getActivity().overridePendingTransition(R.anim.slide_in_right,
				R.anim.slide_out_left);
	}
}
