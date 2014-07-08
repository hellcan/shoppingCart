package com.meipinke.wish;

import com.example.meipinke.R;
import com.meipinke.application.mApplication;
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
import android.widget.Button;
import android.widget.TextView;

public class UnLoginWishListActivity extends Activity {
	private Bundle extras;
	private String categoryName;
	private Boolean mainActivity;
	private Boolean productsMainActivity;
	private Boolean productsDetailActivity;
	private String productName;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cart_unlogin_empty);
		
		initActionBar();
		
		getIntentTxt();
		initView();
	}

	private void getIntentTxt() {
		// TODO Auto-generated method stub
		extras = getIntent().getExtras();
		
		
		mainActivity = mApplication.getMainActBckTag();
		productsMainActivity = mApplication.getProductMainActBckTag();
		productsDetailActivity = mApplication.getProductDetailActBckTag();
	}

	private void initActionBar() {
		// TODO Auto-generated method stub
		//actionbar
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Wish List");
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	private void initView() {
		TextView tv1 = (TextView)findViewById(R.id.cart_1);
		TextView tv2 = (TextView)findViewById(R.id.cart_2);
		Button btn = (Button)findViewById(R.id.cart_signin);
		tv1.setText("Can't view your Wish List yet");
		tv2.setText("Sign in to view items that you saved to your Wish List");
		btn.setBackgroundResource(R.drawable.button_selector_wish_signin);
		
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UnLoginWishListActivity.this, SignInActivity.class);
				intent.putExtra("cart_empty", "none");
				intent.putExtra("cart_item", "none");
				intent.putExtra("wish", "wish");
				startActivity(intent);
				
			}
		});		
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
			}else if(productsMainActivity == true){
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
				
			}else if(productsDetailActivity == true){
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
