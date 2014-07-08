package com.meipinke.cart;

import java.util.ArrayList;
import java.util.List;

import com.example.meipinke.R;
import com.meipinke.adapter.CartItemListAdapter;
import com.meipinke.application.mApplication;
import com.meipinke.database.mDatabase;
import com.meipinke.entry.MainActivity;
import com.meipinke.products.ProductsDetailActivity;
import com.meipinke.products.ProductsMainActivity;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class LoginItemCartActivity extends Activity {
	mDatabase mdb = new mDatabase();
	private CartItemListAdapter mAdapter;
	private List<List<String>> itemList = new ArrayList<List<String>>();
	private String userName;
	private String subPrice;
	private Boolean mainActivity;
	private Boolean prodcutsMainActivity;
	private Boolean prodcutsDetailActivity;
	private String categoryName;
	private String productName;
	private Bundle extras;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cart_login_item);
		
		initActionBar();
		getIntentTxt();
		initDB();	
		initView();
	}
	
	private void initActionBar() {	
			ActionBar actionBar = getActionBar();
			actionBar.setTitle("My Shopping Cart");
			actionBar.setDisplayHomeAsUpEnabled(true);		
		}
	    
	private void getIntentTxt() {
		//get sign in userName from previous activity
		extras = getIntent().getExtras();
		userName = extras.getString("userName");
		mainActivity = mApplication.getMainActBckTag();
		prodcutsMainActivity = mApplication.getProductMainActBckTag();
		prodcutsDetailActivity = mApplication.getProductDetailActBckTag();
	}

	private void initDB() {
		//get item under sign in userName
		mdb.openDatabase();
		subPrice = String.valueOf(mdb.getSubPrice(userName));
		itemList = mdb.getCartItemList(userName);
		mdb.closeDatabase();
	
	}

	private void initView() {
		TextView tv_subPrice = (TextView)findViewById(R.id.login_item_subtotal_price);
		Button btn_checkout = (Button)findViewById(R.id.login_item_checkout);
		Button btn_editCart = (Button)findViewById(R.id.login_item_edit);
		ListView list = (ListView)findViewById(R.id.login_item_listview);
		
		tv_subPrice.setText("$" + subPrice);
		mAdapter = new CartItemListAdapter(this, itemList);
		list.setAdapter(mAdapter);
		//make listView display normally
		new Utility().setListViewHeightBasedOnChildren(list);
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TextView tv_name  = (TextView)view.findViewById(R.id.item_name);
				String name = tv_name.getText().toString();
				Intent intent = new Intent(LoginItemCartActivity.this,ProductsDetailActivity.class);
				//send productName to productDetail page 
				intent.putExtra("productName", name);
				startActivity(intent);
			}
		});
		
		btn_checkout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				categoryName = extras.getString("categoryName");
				productName = extras.getString("productName");

				//send userName
				Intent intent = new Intent(LoginItemCartActivity.this, CheckoutActivity.class);
				intent.putExtra("userName", userName);
				intent.putExtra("categoryName", categoryName);
				intent.putExtra("productName", productName);

				startActivity(intent);
				
			}
		});
		
		btn_editCart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				categoryName = extras.getString("categoryName");
				productName = extras.getString("productName");

				//send userName
				Intent intent = new Intent(LoginItemCartActivity.this, CartEditActivity.class);
				intent.putExtra("userName", userName);
				intent.putExtra("categoryName", categoryName);
				intent.putExtra("productName", productName);

				startActivity(intent);
				
			}
		});

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
		switch(item.getItemId()){
		case android.R.id.home: 
			if(mainActivity == true){
				Intent upIntent = new Intent(this, MainActivity.class);
	            if (NavUtils.shouldUpRecreateTask(this, upIntent)) {                   
	                TaskStackBuilder.from(this)
	                        //如果这里有很多原始的Activity,它们应该被添加在这里
	                        .addNextIntent(upIntent)
	                        .startActivities();
	                finish();
	            } else {                   
	                NavUtils.navigateUpTo(this, upIntent);
	            }
			}else if(prodcutsMainActivity == true){
				categoryName = extras.getString("categoryName");

				Intent upIntent = new Intent(this, ProductsMainActivity.class);
				upIntent.putExtra("categoryName", categoryName);

	            if (NavUtils.shouldUpRecreateTask(this, upIntent)) {                   
	                TaskStackBuilder.from(this)
	                        //如果这里有很多原始的Activity,它们应该被添加在这里
	                        .addNextIntent(upIntent)
	                        .startActivities();
	                finish();
	            } else {                   
	                NavUtils.navigateUpTo(this, upIntent);
	            }
			}else if(prodcutsDetailActivity == true){
				productName = extras.getString("productName");

				Intent upIntent = new Intent(this, ProductsDetailActivity.class);
				upIntent.putExtra("productName", productName);

	            if (NavUtils.shouldUpRecreateTask(this, upIntent)) {                   
	                TaskStackBuilder.from(this)
	                        //如果这里有很多原始的Activity,它们应该被添加在这里
	                        .addNextIntent(upIntent)
	                        .startActivities();
	                finish();
	            } else {                   
	                NavUtils.navigateUpTo(this, upIntent);
	            }
			}else{
				Intent upIntent = new Intent(this,MainActivity.class);
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
