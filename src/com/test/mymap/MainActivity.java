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
	     option.setScanSpan(span);//��ѡ��Ĭ��0��������λһ�Σ����÷���λ����ļ����Ҫ���ڵ���1000ms������Ч��
	     option.setIsNeedAddress(true);//��ѡ�������Ƿ���Ҫ��ַ��Ϣ��Ĭ�ϲ���Ҫ
	     option.setOpenGps(false);//��ѡ��Ĭ��false,�����Ƿ�ʹ��gps
	     option.setIsNeedLocationDescribe(true);//��ѡ��Ĭ��false�������Ƿ���Ҫλ�����廯�����������BDLocation.getLocationDescribe��õ�����������ڡ��ڱ����찲�Ÿ�����
	     option.setIsNeedLocationPoiList(false);//��ѡ��Ĭ��false�������Ƿ���ҪPOI�����������BDLocation.getPoiList��õ�
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
        //��activityִ��onDestroyʱִ��mMapView.onDestroy()��ʵ�ֵ�ͼ�������ڹ���  
        locationClient.unRegisterLocationListener( myListener );
        locationClient.stop();
        mapView.onDestroy();  
        
    }  
    @Override  
    protected void onResume() {  
        super.onResume();  
        //��activityִ��onResumeʱִ��mMapView. onResume ()��ʵ�ֵ�ͼ�������ڹ���  
        mapView.onResume();  
        }  
    @Override  
    protected void onPause() {  
        super.onPause();  
        //��activityִ��onPauseʱִ��mMapView. onPause ()��ʵ�ֵ�ͼ�������ڹ���  
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
            if (location.getLocType() == BDLocation.TypeGpsLocation){// GPS��λ���                             
                iss=0;
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){// ���綨λ���                               
                iss=0;
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// ���߶�λ���                              
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
