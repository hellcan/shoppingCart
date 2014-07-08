package com.meipinke.adapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.example.meipinke.R;
import com.meipinke.adapter.CartEditListAdapter.ViewHolder;
import com.meipinke.database.mDatabase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WishEditListAdapter extends BaseAdapter {
	private List<String> imgList = new ArrayList<String>();
	private List<String> nameList = new ArrayList<String>();
	private List<String> priceList = new ArrayList<String>();
	private Context context;
	mDatabase mdb = new mDatabase();
	String userName;
	private List<String> delItemNameList = new ArrayList<String>();

	public WishEditListAdapter(Context context, List<List<String>> itemList, String userName) {
		this.context = context;
		this.imgList = itemList.get(0);
		this.nameList = itemList.get(1);
		this.priceList = itemList.get(2);
		this.userName = userName;
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
	
	public List<String> getChangedData(){
		return delItemNameList;
		
	}

	@Override
	public View getView(final int position, View v, ViewGroup parent) {
		ViewHolder holder = null;
		if(v == null){
			v = LayoutInflater.from(context).inflate(R.layout.wish_edit_lisview_item, null);
			holder = new ViewHolder();
			holder.img = (ImageView)v.findViewById(R.id.wish_edit_img);
			holder.tv_name = (TextView)v.findViewById(R.id.wish_edit_name);
			holder.tv_price = (TextView)v.findViewById(R.id.wish_edit_price);
			holder.btn_del = (TextView)v.findViewById(R.id.wish_edit_del);
			v.setTag(holder);
		}else{
			holder = (ViewHolder)v.getTag();
		}
		
		holder.tv_name.setText(nameList.get(position));
		holder.tv_price.setText("$" + priceList.get(position));
		//img
		Class c = R.drawable.class;
		Field field = null;
		try {
			field = c.getField(imgList.get(position));
			holder.img.setImageResource(field.getInt(imgList.get(position)));
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		
		holder.btn_del.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				mdb.openDatabase();
//				mdb.deltedItem(nameList.get(position), userName);
//				mdb.closeDatabase();
				delItemNameList.add(nameList.get(position));
				
				imgList.remove(position);
				nameList.remove(position);
				priceList.remove(position);
				notifyDataSetChanged();	
			}
		});
		return v;
	}
	
	final static class ViewHolder{
		ImageView img;
		TextView tv_name;
		TextView tv_price;
		TextView btn_del;
	}

}
