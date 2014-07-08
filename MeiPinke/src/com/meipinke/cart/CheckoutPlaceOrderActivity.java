package com.meipinke.cart;

import java.util.ArrayList;
import java.util.List;

import com.example.meipinke.R;
import com.meipinke.adapter.CartItemListAdapter;
import com.meipinke.application.mApplication;
import com.meipinke.cart.LoginItemCartActivity.Utility;
import com.meipinke.database.mDatabase;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CheckoutPlaceOrderActivity extends Activity {
	Bundle extras;
	String userName;
	String categoryName;
	String productName;
	String subPrice;
	private List<List<String>> itemList = new ArrayList<List<String>>();

	mDatabase mdb = new mDatabase();
	private CartItemListAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cart_checkout_place_order);
		
		initActionBar();
		
		getIntentxt();
		
		initDB();
		
		initView();
	}

	private void initDB() {
		// TODO Auto-generated method stub
		mdb.openDatabase();
		subPrice = String.valueOf(mdb.getSubPrice(userName));
		itemList = mdb.getCartItemList(userName);
		mdb.closeDatabase();
	}

	private void initView() {
		// TODO Auto-generated method stub
		TextView tv_subtotal = (TextView)findViewById(R.id.chk_subtotal_price);
		TextView tv_shippment = (TextView)findViewById(R.id.chk_shippment_price);
		TextView tv_total = (TextView)findViewById(R.id.chk_total_price);
		Button btn_place_order = (Button)findViewById(R.id.chek_place_order);
		ListView list = (ListView)findViewById(R.id.chk_item_listview);		
		mAdapter = new CartItemListAdapter(this, itemList);
		list.setAdapter(mAdapter);
		new Utility().setListViewHeightBasedOnChildren(list);

		tv_subtotal.setText("$" +  subPrice);
		tv_shippment.setText("$15");
		tv_total.setText("$" +(String.valueOf((Double.valueOf(subPrice)+15))));
		
		btn_place_order.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(CheckoutPlaceOrderActivity.this, "Order received", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void getIntentxt() {
		// TODO Auto-generated method stub
		extras = getIntent().getExtras();
		userName = extras.getString("userName");
	}

	private void initActionBar() {
		// TODO Auto-generated method stub
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("Check Out: Step 2");
		
	}
	
	//adjust listView when under scrollView
	public class Utility {
        public  void setListViewHeightBasedOnChildren(ListView listView) {
               //get list adapter
               ListAdapter listAdapter = listView.getAdapter();
               if (listAdapter == null) {
                      return;
               }

               int totalHeight = 0;
               for (int i = 0, len = listAdapter.getCount(); i < len; i++) {   //listAdapter.getCount()
                      View listItem = listAdapter.getView(i, null, listView);
                      listItem.measure(0, 0);  //calculate item height
                      totalHeight += listItem.getMeasuredHeight();  //calculate listView whole height
               }

               ViewGroup.LayoutParams params = listView.getLayoutParams();
               params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
               //listView.getDividerHeight()
               //params.height
               listView.setLayoutParams(params);
        }
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if(mApplication.mainActivity == true){
				Intent upIntent = new Intent(this, LoginItemCartActivity.class);
				upIntent.putExtra("userName", userName);
	            if (NavUtils.shouldUpRecreateTask(this, upIntent)) {                   
	                TaskStackBuilder.from(this)
	                        //如果这里有很多原始的Activity,它们应该被添加在这里
	                        .addNextIntent(upIntent)
	                        .startActivities();
	                finish();
	            } else {                   
	                NavUtils.navigateUpTo(this, upIntent);
	            }
			}else if(mApplication.productsMainActivity == true){
				categoryName = extras.getString("categoryName");

				Intent upIntent = new Intent(this, LoginItemCartActivity.class);
				upIntent.putExtra("categoryName", categoryName);
				upIntent.putExtra("userName", userName);

	            if (NavUtils.shouldUpRecreateTask(this, upIntent)) {                   
	                TaskStackBuilder.from(this)
	                        //如果这里有很多原始的Activity,它们应该被添加在这里
	                        .addNextIntent(upIntent)
	                        .startActivities();
	                finish();
	            } else {                   
	                NavUtils.navigateUpTo(this, upIntent);
	            }
			}else if(mApplication.productsDetailActivity == true){
				productName = extras.getString("productName");

				Intent upIntent = new Intent(this, LoginItemCartActivity.class);
				upIntent.putExtra("productName", productName);
				upIntent.putExtra("userName", userName);
				

	            if (NavUtils.shouldUpRecreateTask(this, upIntent)) {                   
	                TaskStackBuilder.from(this)
	                        //如果这里有很多原始的Activity,它们应该被添加在这里
	                        .addNextIntent(upIntent)
	                        .startActivities();
	                finish();
	            } else {                   
	                NavUtils.navigateUpTo(this, upIntent);
	            }
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
