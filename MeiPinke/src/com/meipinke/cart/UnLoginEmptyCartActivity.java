package com.meipinke.cart;

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

public class UnLoginEmptyCartActivity extends Activity {
	private Boolean mainActivity;
	private Boolean prodcutsMainActivity;
	private Boolean prodcutsDetailActivity;
	private String categoryName;
	private String productName;
	private Bundle extras;

	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cart_unlogin_empty);
		
		initActionBar();
		getIntentTxt();
		initBtnView();
					
	}

	

	private void getIntentTxt() {
		// TODO Auto-generated method stub
		extras = getIntent().getExtras();
		mainActivity = mApplication.getMainActBckTag();
		prodcutsMainActivity = mApplication.getProductMainActBckTag();
		prodcutsDetailActivity = mApplication.getProductDetailActBckTag();

	}



	private void initActionBar() {
		
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Shopping Cart");
		actionBar.setDisplayHomeAsUpEnabled(true);		
	}
	
	private void initBtnView() {
		Button btn_sign = (Button)findViewById(R.id.cart_signin);
		
		btn_sign.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mainActivity == true){
					Intent intent = new Intent(UnLoginEmptyCartActivity.this, SignInActivity.class);
					String tag = "cartEmpty";
					//send self tag to make signIn activity know which activity should go next after sign in
					intent.putExtra("cart_empty", tag);
					//other tag, if not set will cause nullPointer error
					intent.putExtra("cart_item", "none");
					intent.putExtra("wish", "none");
					intent.putExtra("productDetail", "none");
					intent.putExtra("productList", "none");
					startActivity(intent);	
				}else if(prodcutsMainActivity==true){
					categoryName = extras.getString("categoryName");
					Intent intent = new Intent(UnLoginEmptyCartActivity.this, SignInActivity.class);
					String tag = "cartEmpty";
					//send self tag to make signIn activity know which activity should go next after sign in
					intent.putExtra("cart_empty", tag);
					//other tag, if not set will cause nullPointer error
					intent.putExtra("cart_item", "none");
					intent.putExtra("wish", "none");
					intent.putExtra("productDetail", "none");
					intent.putExtra("productList", "none");
					intent.putExtra("categoryName", categoryName);
					startActivity(intent);
				}else if(prodcutsDetailActivity==true){
					productName = extras.getString("productName");
					Intent intent = new Intent(UnLoginEmptyCartActivity.this, SignInActivity.class);
					String tag = "cartEmpty";
					//send self tag to make signIn activity know which activity should go next after sign in
					intent.putExtra("cart_empty", tag);
					//other tag, if not set will cause nullPointer error
					intent.putExtra("cart_item", "none");
					intent.putExtra("wish", "none");
					intent.putExtra("productDetail", "none");
					intent.putExtra("productList", "none");
					intent.putExtra("productName", productName);
					startActivity(intent);
				}
				
							
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
			}
			
            return true;			
		}
		return super.onOptionsItemSelected(item);
	}
	
	

}
