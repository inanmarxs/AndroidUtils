package com.oldfeel.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.oldfeel.utils.ETUtil;
import com.oldfeel.utils.R;
import com.oldfeel.utils.ViewUtil;

/**
 * fragment基类
 * 
 * @author oldfeel
 * 
 *         Created on: 2014-1-20
 */
public class BaseFragment extends Fragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	protected void showToast(String text) {
		((BaseActivity) getActivity()).showToast(text);
	}

	protected void showSimpleDialog(String text) {
		((BaseActivity) getActivity()).showSimpleDialog(text);
	}

	protected void openActivity(Class<?> className) {
		Intent intent = new Intent(getActivity(), className);
		startActivity(intent);
	}

	public String getString(EditText et) {
		return ETUtil.getString(et);
	}

	public GridView getGridView(View view, int id) {
		return ViewUtil.getGridView(view, id);
	}

	public ListView findListView(View view, int id) {
		return ViewUtil.getListView(view, id);
	}

	public TextView getTextView(View view, int id) {
		return ViewUtil.getTextView(view, id);
	}

	public ImageView getImageView(View view, int id) {
		return ViewUtil.getImageView(view, id);
	}

	public ListView getListView(View view, int id) {
		return ViewUtil.getListView(view, id);
	}

	public Button getButton(View view, int id) {
		return ViewUtil.getButton(view, id);
	}

	public EditText getEditText(View view, int id) {
		return ViewUtil.getEditText(view, id);
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		getActivity().overridePendingTransition(R.anim.slide_in_right,
				R.anim.slide_out_left);
	}
}
