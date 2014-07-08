package com.meipinke.adapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.example.meipinke.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WishListAdapter extends BaseAdapter {
	private List<String> imgList = new ArrayList<String>();
	private List<String> nameList = new ArrayList<String>();
	private List<String> priceList = new ArrayList<String>();
	private Context context;

	public WishListAdapter(Context context, List<List<String>> itemList) {
		this.context = context;
		this.imgList = itemList.get(0);
		this.nameList = itemList.get(1);
		this.priceList = itemList.get(2);
	}

	@Override
	public int getCount() {
		return nameList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		v = LayoutInflater.from(context).inflate(R.layout.wishlist_item_login_activity, null);
		ImageView img = (ImageView)v.findViewById(R.id.wish_img);
		TextView tv_name = (TextView)v.findViewById(R.id.wish_name);
		TextView tv_price = (TextView)v.findViewById(R.id.wish_price);
		
		tv_name.setText(nameList.get(position));
		tv_price.setText("$" + priceList.get(position));
		//img
		Class c = R.drawable.class;
		Field field = null;
		try {
			field = c.getField(imgList.get(position));
			img.setImageResource(field.getInt(imgList.get(position)));
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		
		return v;
	}

}
