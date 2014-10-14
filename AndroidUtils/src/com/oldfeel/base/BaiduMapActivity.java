package com.oldfeel.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mobstat.StatService;
import com.oldfeel.utils.R;

/**
 * 百度地图类的基类
 * 
 * @author oldfeel
 * 
 */
public class BaiduMapActivity extends BaseActivity {
	private RelativeLayout rlContent;
	protected MapView mMapView;
	protected BaiduMap mBaiduMap;
	public LocationClient mLocationClient;
	public boolean isFirstLoc = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.base_map_activity);
		initMap();
	}

	public void setContentLayout(int id) {
		rlContent = (RelativeLayout) findViewById(R.id.base_map_activity_content);
		View view = LayoutInflater.from(BaiduMapActivity.this)
				.inflate(id, null);
		rlContent.addView(view);
	}

	private void initMap() {
		mMapView = (MapView) findViewById(R.id.bmapsView);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		mBaiduMap.setMaxAndMinZoomLevel(19, 17);
		mLocationClient = new LocationClient(getApplicationContext());

		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02
		option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
		mLocationClient.setLocOption(option);

		// 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
		BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_geo);
		MyLocationConfiguration config = new MyLocationConfiguration(
				MyLocationConfiguration.LocationMode.NORMAL, true,
				mCurrentMarker);
		mBaiduMap.setMyLocationConfigeration(config);
	}

	public BDLocationListener myListener = new BDLocationListener() {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (mBaiduMap == null || location == null) {
				return;
			}
			// 构造定位数据
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			// 设置定位数据
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
		}
	};

	@Override
	protected void onResume() {
		mLocationClient.registerLocationListener(myListener);
		mLocationClient.start();
		mLocationClient.requestLocation();
		mMapView.onResume();
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		StatService.onResume(this);
		super.onResume();
	}

	@Override
	protected void onPause() {
		mLocationClient.stop();
		mLocationClient.unRegisterLocationListener(myListener);
		mMapView.onPause();
		// 当不需要定位图层时关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mBaiduMap = null;
		StatService.onPause(this);
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		mMapView.onDestroy();
		super.onDestroy();
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
