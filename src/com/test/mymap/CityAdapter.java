package com.test.mymap;

import java.util.List;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CityAdapter extends ArrayAdapter<String> {
	private int resourceId;
	public CityAdapter(Context context, int textViewResourceId, List<String> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		resourceId=textViewResourceId;
	}
	public View getView(int position,View convertView,ViewGroup parent){
		String city=getItem(position);
		View view;
		ViewHolder viewholder=new ViewHolder();
		if (convertView==null){
			view=LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewholder.textView=(TextView)view.findViewById(R.id.city);
			view.setTag(viewholder);
		}
		else{
			view=convertView;
			viewholder=(ViewHolder)view.getTag();
		}
		viewholder.textView.setText(city);
		return view;
	}
class ViewHolder{
	TextView textView;
}
	
}
