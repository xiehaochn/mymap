package com.test.mymap;

import java.util.ArrayList;
import java.util.List;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SelectCity extends Activity {
	private ListView city;
	private List<String> cityList;
	private CityAdapter adapter;
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.selectcity);
		city=(ListView)findViewById(R.id.select_city);
		cityList=get_city();
		adapter=new CityAdapter(SelectCity.this,R.layout.city_item,cityList);
		city.setAdapter(adapter);
		city.setOnItemClickListener(new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			String city = cityList.get(position);

			Intent intent = new Intent();
			intent.putExtra("city_data", city);
			setResult(RESULT_OK,intent);
			finish();
			}});
	}
	private List<String> get_city() {
		// TODO Auto-generated method stub
		List<String> CityList=new ArrayList<String>();
		CityList.add("成都");
		CityList.add("北京");
		return CityList;
	}
	
}
