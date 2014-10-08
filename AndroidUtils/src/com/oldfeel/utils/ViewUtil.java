package com.oldfeel.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.VideoView;

/**
 * 视图工作类
 * 
 * @author oldfeel
 * 
 *         Created on: 2014年3月1日
 */
public class ViewUtil {

	public static GridView getGridView(View view, int id) {
		return (GridView) view.findViewById(id);
	}

	public static TextView getTextView(View view, int id) {
		return (TextView) view.findViewById(id);
	}

	public static Button getButton(View view, int id) {
		return (Button) view.findViewById(id);
	}

	public static ImageView getImageView(View view, int id) {
		return (ImageView) view.findViewById(id);
	}

	public static ListView getListView(View view, int id) {
		return (ListView) view.findViewById(id);
	}

	public static VideoView getVideoView(View view, int id) {
		return (VideoView) view.findViewById(id);
	}

	public static EditText getEditText(View view, int id) {
		return (EditText) view.findViewById(id);
	}

	public static ImageButton getImageButton(View view, int id) {
		return (ImageButton) view.findViewById(id);
	}

	public static RatingBar getRatingBar(View view, int id) {
		return (RatingBar) view.findViewById(id);
	}

	public static void updateListViewHeight(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
}
