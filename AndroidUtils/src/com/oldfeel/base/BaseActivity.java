package com.oldfeel.base;

import java.lang.reflect.Field;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.ZoomControls;

import com.baidu.mobstat.StatService;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.oldfeel.app.MyApplication;
import com.oldfeel.utils.DialogUtil;
import com.oldfeel.utils.ETUtil;
import com.oldfeel.utils.R;

/**
 * activity基类
 * 
 * @author oldfeel
 * 
 *         Created on: 2014-1-10
 */
public abstract class BaseActivity extends SwipeBackActivity {
	private MyApplication myApplication;
	protected DisplayImageOptions options;
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			ViewConfiguration mconfig = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(mconfig, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		myApplication = (MyApplication) getApplication();
		myApplication.addActivity(this);
		int id = R.drawable.ic_launcher;
		if (id > 0)
			options = new DisplayImageOptions.Builder()
					.showImageForEmptyUri(id).showImageOnFail(id)
					.cacheInMemory(true).cacheOnDisc(true).build();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		myApplication.removeActivity(this);
		super.onDestroy();
	}

	/**
	 * 显示toast提示
	 * 
	 * @param text
	 */
	public void showToast(String text) {
		DialogUtil.getInstance().showToast(this, text);
	}

	/**
	 * 显示一个简易的dialog
	 * 
	 * @param text
	 */
	public void showSimpleDialog(String text) {
		DialogUtil.getInstance().showSimpleDialog(this, text);
	}

	/**
	 * 打开指定activity
	 * 
	 * @param targetClass
	 */
	public void openActivity(Class<?> targetClass) {
		Intent intent = new Intent(this, targetClass);
		startActivity(intent);
	}

	public EditText getEditText(int id) {
		return (EditText) findViewById(id);
	}

	public TextView getTextView(int id) {
		return (TextView) findViewById(id);
	}

	public Button getButton(int id) {
		return (Button) findViewById(id);
	}

	public ImageButton getImageButton(int id) {
		return (ImageButton) findViewById(id);
	}

	public ImageView getImageView(int id) {
		return (ImageView) findViewById(id);
	}

	public LinearLayout getLinearLayout(int id) {
		return (LinearLayout) findViewById(id);
	}

	public Switch getSwitch(int id) {
		return (Switch) findViewById(id);
	}

	public ZoomControls getZoomControls(int id) {
		return (ZoomControls) findViewById(id);
	}

	public RadioGroup getRadioGroup(int id) {
		return (RadioGroup) findViewById(id);
	}

	public RadioButton getRadioButton(int id) {
		return (RadioButton) findViewById(id);
	}

	public Spinner getSpinner(int id) {
		return (Spinner) findViewById(id);
	}

	public ListView getListView(int id) {
		return (ListView) findViewById(id);
	}

	public GridView getGridView(int id) {
		return (GridView) findViewById(id);
	}

	public VideoView getVideoView(int id) {
		return (VideoView) findViewById(id);
	}

	public CheckBox getCheckBox(int id) {
		return (CheckBox) findViewById(id);
	}

	public String getString(EditText et) {
		return ETUtil.getString(et);
	}

	public String getString(TextView tv) {
		return tv.getText().toString().trim();
	}

	public String getString(Spinner sp) {
		return sp.getSelectedItem().toString();
	}

	public int getInt(Spinner sp) {
		return Integer.valueOf(sp.getSelectedItem().toString());
	}

	public int getSelection(int resId, String string) {
		String[] strings = getResources().getStringArray(resId);
		for (int i = 0; i < strings.length; i++) {
			if (string.equals(strings[i])) {
				return i;
			}
		}
		return 0;
	}

	public boolean isEmpty(EditText... ets) {
		return ETUtil.isEmpty(ets);
	}

	public void cancelPd() {
		DialogUtil.getInstance().cancelPd();
	}

	@Override
	protected void onResume() {
		StatService.onResume(this);
		super.onResume();
	}

	@Override
	protected void onPause() {
		StatService.onPause(this);
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}
}
