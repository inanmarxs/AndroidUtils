package com.oldfeel.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.oldfeel.conf.BaseConstant;
import com.oldfeel.utils.DialogUtil;
import com.oldfeel.utils.JsonUtil;
import com.oldfeel.utils.NetUtil;
import com.oldfeel.utils.NetUtil.OnNetFailListener;
import com.oldfeel.utils.NetUtil.RequestStringListener;
import com.oldfeel.utils.R;

/**
 * 
 * @author oldfeel
 * 
 *         Create on: 2014年11月23日
 */
public abstract class BaseListFragment extends BaseFragment implements
		OnRefreshListener, OnScrollListener {
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private ListView mListView;
	protected NetUtil netUtil;
	protected BaseBaseAdapter<?> adapter;
	private int lastVisibleIndex;
	private int page;
	private ProgressBar progressBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.base_list_fragment, container,
				false);
		mSwipeRefreshLayout = (SwipeRefreshLayout) view
				.findViewById(R.id.swiperefresh);
		mSwipeRefreshLayout.setColorSchemeResources(R.color.swipe_color_1,
				R.color.swipe_color_2, R.color.swipe_color_3,
				R.color.swipe_color_4);
		mListView = (ListView) view.findViewById(R.id.listview);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initHeaderView();
		progressBar = new ProgressBar(getActivity());
		getListView().addFooterView(progressBar);
		initAdapter();
		getListView().setAdapter(adapter);
		getListView().setOnScrollListener(this);
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				position = position - mListView.getHeaderViewsCount();
				BaseListFragment.this.onItemClick(position);
			}
		});
		if (netUtil != null) {
			getData(0);
		}
		mSwipeRefreshLayout.setOnRefreshListener(this);
		mSwipeRefreshLayout.setRefreshing(true);
	}

	public ListView getListView() {
		return mListView;
	}

	public void setNetUtil(NetUtil netUtil) {
		this.netUtil = netUtil;
		getData(0);
	}

	public void getData(final int page) {
		this.page = page;
		if (page != 0 && adapter.isAddOver()) {
			return;
		}
		netUtil.setParams("page", page);
		netUtil.setOnNetFailListener(new OnNetFailListener() {

			@Override
			public void onTimeOut() {
				DialogUtil.getInstance().showToast(getActivity(), "网络链接超时");
				refreshComplete();
			}

			@Override
			public void onError() {
				DialogUtil.getInstance().showToast(getActivity(), "网络连接错误");
				refreshComplete();
			}

			@Override
			public void cancel() {
				refreshComplete();
			}
		});
		netUtil.postRequest("", new RequestStringListener() {

			@Override
			public void onComplete(String result) {
				if (JsonUtil.isSuccess(result)) {
					adapter.addResult(page, result);
				} else {
					showToast(JsonUtil.getData(result));
				}
				refreshComplete();
			}
		});
	}

	protected void refreshComplete() {
		if (adapter.getCount() - mListView.getHeaderViewsCount() < BaseConstant
				.getInstance().getPageSize()) {
			if (isVisible() && getListView() != null && progressBar != null) {
				getListView().removeFooterView(progressBar);
			}
		}
		mSwipeRefreshLayout.setRefreshing(false);
	}

	@Override
	public void onRefresh() {
		getData(0);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& lastVisibleIndex == adapter.getCount()
						- mListView.getHeaderViewsCount()) {
			if (!adapter.isAddOver()) {
				getData(++page);
			} else { // 加载完成后移除底部进度条
				getListView().removeFooterView(progressBar);
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
	}

	public void showToast(String text) {
		DialogUtil.getInstance().showToast(getActivity(), text);
	}

	public abstract void onItemClick(int position);

	public abstract void initHeaderView();

	public abstract void initAdapter();

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		getActivity().overridePendingTransition(R.anim.slide_in_right,
				R.anim.slide_out_left);
	}
}
