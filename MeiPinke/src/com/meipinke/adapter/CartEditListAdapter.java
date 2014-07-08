package com.meipinke.adapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.example.meipinke.R;
import com.meipinke.database.mDatabase;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;

public class CartEditListAdapter extends BaseAdapter {
	private List<String> imgList = new ArrayList<String>();
	private List<String> nameList = new ArrayList<String>();
	private List<String> stockList = new ArrayList<String>();
	private List<String> priceList = new ArrayList<String>();
	private Context context;
	private String userName;
	private int newQty = 1;
	private List<String> updateQtyList = new ArrayList<String>();
	private List<String> updateQtyNameList = new ArrayList<String>();
	private List<String> updateNewStockList = new ArrayList<String>();
	private List<List<String>> qtyList = new ArrayList<List<String>>();
	private List<String> delNameList = new ArrayList<String>();
	private List<String> delQtyList = new ArrayList<String>();
	private List<List<String>> delList = new ArrayList<List<String>>();

	mDatabase mdb = new mDatabase();
	
	public CartEditListAdapter(Context context, List<List<String>> itemList, String userName) {
		this.context = context;
		this.imgList = itemList.get(0);
		this.nameList = itemList.get(1);
		this.stockList = itemList.get(2);
		this.priceList = itemList.get(3);
		this.userName = userName;
	}

	@Override
	public int getCount() {
		return nameList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public List<List<String>> getQtyData(){
		return qtyList;
	}
	
	public List<List<String>> getDelData(){
		return delList;
		
	}

	@Override
	public View getView(final int position, View v, ViewGroup parent) {
		ViewHolder holder = null;
		if(v == null){
			v = LayoutInflater.from(context).inflate(R.layout.cart_edit_listview_item, null);
			holder = new ViewHolder();
			holder.img = (ImageView)v.findViewById(R.id.edit_img);
			holder.tv_name =(TextView)v.findViewById(R.id.edit_name);
			holder.tv_price = (TextView)v.findViewById(R.id.edit_price);
			holder.tv_qty = (TextView)v.findViewById(R.id.edit_qty);
			holder.btn_del = (TextView)v.findViewById(R.id.edit_del);
			v.setTag(holder);
		}else{
			holder = (ViewHolder)v.getTag();
		}
		
		holder.tv_name.setText(nameList.get(position));
		holder.tv_price.setText("$" + priceList.get(position));
		holder.tv_qty.setText("Qty:" + stockList.get(position));
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
		
		
		
		holder.tv_qty.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mdb.openDatabase();
				String stock = mdb.getOriginStock(nameList.get(position));
				mdb.closeDatabase();
				final NumberPicker mPicker = new NumberPicker(context);
				mPicker.setMinValue(1);
				int a = Integer.valueOf(stock);
				mPicker.setMaxValue(a);
				
                
                mPicker.setOnValueChangedListener(new OnValueChangeListener() {
					

					@Override
					public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
						newQty = newVal;	
					}				
				});       
                
                

                
                AlertDialog mAlertDialog = new AlertDialog.Builder(context).
                		setView(mPicker).
                		setTitle("Set Quantity").
                		setPositiveButton("OK", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								
								processNewAvail();
	
								stockList.add(position, String.valueOf(newQty));//update text
								notifyDataSetChanged();	
								//reset qty
								newQty = 1;
							}

							private void processNewAvail() {
								// TODO Auto-generated method stub
								String name = nameList.get(position);
								int oldQty = Integer.valueOf(stockList.get(position));
								int newStock = 0;
								mdb.openDatabase();
								//calculate new availability
								int nowStock = Integer.valueOf(mdb.getStock(name));//calculate stock
								
								newStock = nowStock - (newQty - oldQty);
								
								mdb.closeDatabase();
								
								//store modified data 
								updateQtyNameList.add(name);
								updateQtyList.add(String.valueOf(newQty));
								updateNewStockList.add(String.valueOf(newStock));	
							}			
														
						}).create();
                
                mAlertDialog.show();
                
			}
		});
		
		qtyList.add(updateQtyNameList);
		qtyList.add(updateQtyList);
		qtyList.add(updateNewStockList);
		
		holder.btn_del.setOnClickListener(new OnClickListener() {
			
			

			@Override
			public void onClick(View v) {
				
				processNewAvail();
				
				imgList.remove(position);
				nameList.remove(position);
				stockList.remove(position);
				priceList.remove(position);
				notifyDataSetChanged();		
				
			}

			private void processNewAvail() {
				// TODO Auto-generated method stub
				mdb.openDatabase();
				int nowStock = Integer.valueOf(mdb.getStock(nameList.get(position)));
				int newStock = Integer.valueOf(mdb.getCartItemList(userName).get(2).get(position)) + nowStock;
				//store modified data 
				delNameList.add(nameList.get(position));
				delQtyList.add(String.valueOf(newStock));
				mdb.closeDatabase();
			}
		});
		
		delList.add(delNameList);
		delList.add(delQtyList);
		        
		return v;
	}
	
	final static class ViewHolder{
		ImageView img;
		TextView tv_name;
		TextView tv_price;
		TextView tv_qty;
		TextView btn_del;
	}

}
