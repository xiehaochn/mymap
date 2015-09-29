package com.test.mymap;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;

import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;

import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import android.app.Activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private MapView mapView=null;
	private BaiduMap baiduMap;
	private LocationClient locationClient;
	private MyLocationListener myListener = new MyLocationListener();
	private ImageButton my_location;
	public LatLng my_point;
	public int iss;
	public int error_type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext()); 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		mapView=(MapView)findViewById(R.id.bmapView);
		baiduMap=mapView.getMap();
		my_location=(ImageButton)findViewById(R.id.my_location);
		my_location.setOnClickListener(this);
		locationClient = new LocationClient(getApplicationContext());
		locationClient.registerLocationListener( myListener );
		initLocation();
		locationClient.start();
		
		
	}

	private void initLocation() {
		// TODO Auto-generated method stub
		 LocationClientOption option = new LocationClientOption();
	     option.setLocationMode(LocationMode.Hight_Accuracy);
	     option.setCoorType("bd09ll");
	     int span=1000;
	     option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
	     option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
	     option.setOpenGps(false);//可选，默认false,设置是否使用gps
	     option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
	     option.setIsNeedLocationPoiList(false);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
	    locationClient.setLocOption(option);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override  
    protected void onDestroy() {  
        super.onDestroy();  
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理  
        locationClient.unRegisterLocationListener( myListener );
        locationClient.stop();
        mapView.onDestroy();  
        
    }  
    @Override  
    protected void onResume() {  
        super.onResume();  
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理  
        mapView.onResume();  
        }  
    @Override  
    protected void onPause() {  
        super.onPause();  
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理  
        mapView.onPause();  
        }  
    class MyLocationListener implements BDLocationListener{
    	
		
		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			baiduMap.setMyLocationEnabled(true);
			MyLocationData locData = new MyLocationData.Builder()  
					.accuracy(location.getRadius())
				    .direction(100).latitude(location.getLatitude())  
				    .longitude(location.getLongitude()).build();  
			baiduMap.setMyLocationData(locData); 
            error_type=location.getLocType();
            my_point=new LatLng(location.getLatitude(),location.getLongitude());
            if (location.getLocType() == BDLocation.TypeGpsLocation){// GPS定位结果                             
                iss=0;
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){// 网络定位结果                               
                iss=0;
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果                              
                iss=0;
            } else{                          
                iss=1;
            }
			
            }
    }
    	
    
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case(R.id.my_location):{
			if(iss==0){
				MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(my_point);  
	            baiduMap.animateMapStatus(msu);  
	            Toast.makeText(this, "Successed.Your location is "+my_point.latitude+"    "+my_point.longitude, Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(this, "Failed"+error_type, Toast.LENGTH_SHORT).show();
			}
			break;
		}
		default:
			break;
		}
	}
}
