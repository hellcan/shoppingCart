package com.meipinke.wish;

import java.util.ArrayList;
import java.util.List;

import com.example.meipinke.R;
import com.meipinke.adapter.WishEditListAdapter;
import com.meipinke.application.mApplication;
import com.meipinke.cart.CartEditActivity;
import com.meipinke.cart.UnLoginEmptyCartActivity;
import com.meipinke.cart.UnLoginItemCartActivity;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class WishEditActivity extends Activity {
	private String userName;
	private WishEditListAdapter mAdapter;
	mDatabase mdb = new mDatabase();
	private String productName;
	private String categoryName;
	private List<List<String>> itemList = new ArrayList<List<String>>();
	private Bundle extras;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wish_edit_activity);
		
		initActionBar();
		getIntentTxt();
		initDB();		
		initView();
	}

	private void initActionBar() {
		// TODO Auto-generated method stub
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("Edit Wish List"); 
	}

	private void getIntentTxt() {
		// TODO Auto-generated method stub
		//get userName
		extras = getIntent().getExtras();
		userName = extras.getString("userName");
	}

	private void initDB() {
		// TODO Auto-generated method stub
		//db
		mdb.openDatabase();
		itemList = mdb.getWishList(userName);	
		mdb.closeDatabase();
	}

	private void initView() {
		// TODO Auto-generated method stub
		ListView list = (ListView)findViewById(R.id.wish_edit_listview);
		Button btn_save = (Button)findViewById(R.id.wish_edit_save);
		Button btn_cancel = (Button)findViewById(R.id.wish_edit_cancel);
		
		mAdapter = new WishEditListAdapter(this, itemList, userName);
		list.setAdapter(mAdapter);
		
		btn_save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mdb.openDatabase();
				//delete item
				for(int i = 0; i<mAdapter.getChangedData().size(); i++){
					mdb.deleteWishItem(mAdapter.getChangedData().get(i), userName);
				}				
				mdb.closeDatabase();
				
					Intent intent = new Intent(WishEditActivity.this,LoginWishListActivity.class);
					intent.putExtra("userName", userName);
					intent.putExtra("categoryName", categoryName);
					intent.putExtra("productName", productName);
					
					startActivity(intent);
				
			}
		});
		
		btn_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(WishEditActivity.this,LoginWishListActivity.class);
				intent.putExtra("userName", userName);
				intent.putExtra("categoryName", categoryName);
				intent.putExtra("productName", productName);
				
				startActivity(intent);				
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case android.R.id.home:
			if(mApplication.mainActivity == true){
				Intent upIntent = new Intent(this, LoginWishListActivity.class);
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
				Intent upIntent = new Intent(this, LoginWishListActivity.class);
				upIntent.putExtra("userName", userName);
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
			}else if(mApplication.productsDetailActivity == true){
				productName = extras.getString("productName");
				Intent upIntent = new Intent(this, LoginWishListActivity.class);
				upIntent.putExtra("userName", userName);
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
