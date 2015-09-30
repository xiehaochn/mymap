package com.test.mymap;

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
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener, OnGetGeoCoderResultListener {
	private MapView mapView=null;
	private BaiduMap baiduMap;
	private LocationClient locationClient;
	private MyLocationListener myListener = new MyLocationListener();
	private ImageButton my_location;
	private ImageButton traffic;
	private ImageButton sattelite;
	private ImageButton search;
	private Button select_city;
	private EditText search_text;
	private String city;
	public LatLng my_point;
	public int iss;
	public int error_type;
	public String my_address;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext()); 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		mapView=(MapView)findViewById(R.id.bmapView);
		baiduMap=mapView.getMap();
		my_location=(ImageButton)findViewById(R.id.show_location);
		my_location.setOnClickListener(this);
		traffic=(ImageButton)findViewById(R.id.traffic);
        traffic.setOnClickListener(this);
        sattelite=(ImageButton)findViewById(R.id.normal_satellite);
        sattelite.setOnClickListener(this);
        search=(ImageButton)findViewById(R.id.search);
        search.setOnClickListener(this);
        select_city=(Button)findViewById(R.id.select_city);
        select_city.setOnClickListener(this);
        search_text=(EditText)findViewById(R.id.title_search);
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
	     option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
	     option.setOpenGps(true);//可选，默认false,设置是否使用gps
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
			
			my_address=location.getAddrStr();
            error_type=location.getLocType();
            my_point=new LatLng(location.getLatitude(),location.getLongitude());
            MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(my_point);  
	        baiduMap.animateMapStatus(msu);
            if (location.getLocType() == BDLocation.TypeGpsLocation){// GPS定位结果                             
                iss=0;
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){// 网络定位结果                               
                iss=1;
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果                              
                iss=2;
            } else{                          
                iss=3;
            }
			
            }
    }
    	
    
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case(R.id.normal_satellite):{
			if(baiduMap.getMapType()==BaiduMap.MAP_TYPE_SATELLITE){
			baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
			}else{
				baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
			}
			break;
		}
		case(R.id.traffic):{
			if(baiduMap.isTrafficEnabled()){
				baiduMap.setTrafficEnabled(false);
			}else{
				baiduMap.setTrafficEnabled(true);
			}
			break;
		}
		case(R.id.show_location):{
			locationClient.requestLocation();
			
			if(iss==0){
				  
	            Toast.makeText(this, "GPS定位成功"+"\n"+"您当前的位置："+my_address, Toast.LENGTH_SHORT).show();
			}else if(iss==1){	
				
				Toast.makeText(this, "网络定位成功"+"\n"+"您当前的位置："+my_address, Toast.LENGTH_SHORT).show();
			}else if(iss==2){
				
				Toast.makeText(this, "离线定位成功", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(this, "定位失败"+"\n"+"error type:"+error_type, Toast.LENGTH_SHORT).show();
			}
				break;
			
		}
		case(R.id.select_city):{
			Intent intent=new Intent(MainActivity.this,SelectCity.class);
			startActivityForResult(intent,1);
			break;
		}
		case(R.id.search):{
			if(city!=null){
			String text=search_text.getText().toString();
			GeoCoder coder =GeoCoder.newInstance();
			GeoCodeOption search_address=new GeoCodeOption();
			search_address.city(city);
			search_address.address(text);
			coder.geocode(search_address);
			coder.setOnGetGeoCodeResultListener(this);
			}
			else{
				Toast.makeText(MainActivity.this, "请先选择城市", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		default:
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode,int resultCode,Intent data){
		switch(requestCode){
		case 1:
			if (resultCode==RESULT_OK){
				city=data.getStringExtra("city_data");
				
			}
				break;
		default:
		}
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult arg0) {
		// TODO Auto-generated method stub
		LatLng search_point=arg0.getLocation();
		if(search_point!=null){
		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka1);		
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newLatLng(search_point);
		baiduMap.animateMapStatus(mMapStatusUpdate);
		OverlayOptions search_point_overlay = new MarkerOptions().position(search_point).icon(bitmap);				
		Marker my_marker = (Marker) baiduMap.addOverlay(search_point_overlay);
		}
		else{
			Toast.makeText(MainActivity.this, "未能找到结果", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
		// TODO Auto-generated method stub
		
	}
}
