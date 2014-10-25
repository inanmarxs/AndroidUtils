package com.oldfeel.base;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.oldfeel.conf.BaseConstant;
import com.oldfeel.utils.DialogUtil;
import com.oldfeel.utils.JSONUtil;
import com.oldfeel.utils.NetUtil;
import com.oldfeel.utils.NetUtil.OnNetFailListener;
import com.oldfeel.utils.NetUtil.RequestStringListener;
import com.oldfeel.utils.R;

/**
 * 下拉刷新,上拉加载更多的fragment
 * 
 * @author oldfeel
 * 
 *         Created on: 2014年3月2日
 */
public abstract class BaseListFragment extends ListFragment implements
		OnRefreshListener, OnScrollListener {

	protected NetUtil netUtil;
	protected BaseBaseAdapter<?> adapter;
	private PullToRefreshLayout mPullToRefreshLayout;
	private int lastVisibleIndex;
	private int page;
	private ProgressBar progressBar;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ViewGroup viewGroup = (ViewGroup) view;
		mPullToRefreshLayout = new PullToRefreshLayout(viewGroup.getContext());
		ActionBarPullToRefresh
				.from((ActionBarActivity) getActivity())
				.insertLayoutInto(viewGroup)
				.theseChildrenArePullable(android.R.id.list, android.R.id.empty)
				.listener(this).setup(mPullToRefreshLayout);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		progressBar = new ProgressBar(getActivity());
		getListView().addFooterView(progressBar);
		initAdapter();
		setListAdapter(adapter);
		setListShownNoAnimation(true);
		getListView().setOnScrollListener(this);
		getListView().setOnCreateContextMenuListener(this);
		mPullToRefreshLayout.setRefreshing(true);
		if (netUtil != null) {
			getData(0);
		}
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
				if (JSONUtil.isSuccess(result)) {
					adapter.addResult(page, result);
				} else {
					showToast("加载失败," + JSONUtil.getMessage(result));
				}
				refreshComplete();
			}
		});
	}

	protected void refreshComplete() {
		if (adapter.getCount() < BaseConstant.getInstance().getPageSize()) {
			if (isVisible() && getListView() != null && progressBar != null) {
				getListView().removeFooterView(progressBar);
			}
		}
		mPullToRefreshLayout.setRefreshComplete();
	}

	@Override
	public void onRefreshStarted(View view) {
		getData(0);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& lastVisibleIndex == getListAdapter().getCount()) {
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

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		position = position - l.getHeaderViewsCount();
		onItemClick(position);
	}

	public void showToast(String text) {
		DialogUtil.getInstance().showToast(getActivity(), text);
	}

	public abstract void onItemClick(int position);

	public abstract void initAdapter();

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		getActivity().overridePendingTransition(R.anim.slide_in_right,
				R.anim.slide_out_left);
	}
}
