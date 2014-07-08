package com.meipinke.wish;

import java.util.ArrayList;
import java.util.List;

import com.example.meipinke.R;
import com.meipinke.adapter.CartItemListAdapter;
import com.meipinke.adapter.WishListAdapter;
import com.meipinke.application.mApplication;
import com.meipinke.database.mDatabase;
import com.meipinke.entry.MainActivity;
import com.meipinke.login.SignInActivity;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class LoginWishListActivity extends Activity{
	mDatabase mdb = new mDatabase();
	WishListAdapter mAdapter;
	List<List<String>> itemList = new ArrayList<List<String>>();
	private String userName;
	private String productName;
	Bundle extras;
	private Boolean mainActivity;
	private Boolean prodcutsMainActivity;
	private Boolean prodcutsDetailActivity;
	private String categoryName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wishlist_login_activity);
		
		initActionBar();
		getIntentTxt();
		initDB();
		initView();
		
	}

	private void initView() {
		// TODO Auto-generated method stub
		ListView list = (ListView)findViewById(R.id.wish_list);
		TextView tv = (TextView)findViewById(R.id.wish_status);
		Button btn_edit = (Button)findViewById(R.id.wish_edit);

		if(productName == null){
			list.setVisibility(View.GONE);
			btn_edit.setVisibility(View.GONE);
		}else{
			tv.setVisibility(View.GONE);
			
			mAdapter = new WishListAdapter(this, itemList);
			list.setAdapter(mAdapter);
			
			list.setOnItemClickListener(new OnItemClickListener() {
				
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					TextView tv_name = (TextView)view.findViewById(R.id.wish_name);
					String name = tv_name.getText().toString();
			    	Intent intent = new Intent(LoginWishListActivity.this, ProductsDetailActivity.class);
		   		    intent.putExtra("productName", name);
			    	startActivity(intent);
			    	}
				});
			
			btn_edit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					categoryName = extras.getString("categoryName");
					productName = extras.getString("productName");
					//
					Intent intent = new Intent(LoginWishListActivity.this, WishEditActivity.class);
					intent.putExtra("userName", userName);
					intent.putExtra("categoryName", categoryName);
					intent.putExtra("productName", productName);
					startActivity(intent);

				}
			});
			}
	}

	private void initDB() {
		//db
		mdb.openDatabase();
		itemList = mdb.getWishList(userName);
	    productName = mdb.checkWishList(userName);
		mdb.closeDatabase();
	}

	private void getIntentTxt() {
		//bundle
		extras = getIntent().getExtras();
		userName = extras.getString("userName");
		
		mainActivity = mApplication.getMainActBckTag();
		prodcutsMainActivity = mApplication.getProductMainActBckTag();
		prodcutsDetailActivity = mApplication.getProductDetailActBckTag();
	}

	private void initActionBar() {
		// TODO Auto-generated method stub
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("My Wish List");
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
			}
			
	            return true;			
			}
			return super.onOptionsItemSelected(item);
		}
}
