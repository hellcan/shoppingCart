package com.meipinke.login;

import java.util.ArrayList;
import java.util.List;

import com.example.meipinke.R;
import com.meipinke.application.mApplication;
import com.meipinke.cart.LoginEmptyCartActivity;
import com.meipinke.cart.LoginItemCartActivity;
import com.meipinke.cart.UnLoginEmptyCartActivity;
import com.meipinke.cart.UnLoginItemCartActivity;
import com.meipinke.database.mDatabase;
import com.meipinke.entry.MainActivity;
import com.meipinke.products.ProductsDetailActivity;
import com.meipinke.products.ProductsMainActivity;
import com.meipinke.wish.LoginWishListActivity;
import com.meipinke.wish.UnLoginWishListActivity;

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
import android.widget.EditText;
import android.widget.Toast;

public class SignInActivity extends Activity {
	mDatabase mdb = new mDatabase();
	String cartStatusEmpty = "default";
	String cartStatusItem = "default";
	String wishListStatus = "default";
	String productDetailStatus = "default";
	String productName = "defafult";
	String productListStatus = "default";
	String categoryName = "default";
	Bundle extras;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_in_activity);
		
		initActionBar();
		
		getIntentTxt();
		
		initView();
		
	}


	private void initActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("Sign in");		
	}


	private void getIntentTxt() {
		//get text from previous activity
		extras = getIntent().getExtras();
		cartStatusEmpty = extras.getString("cart_empty");
		cartStatusItem = extras.getString("cart_item");
		wishListStatus = extras.getString("wish");
		productDetailStatus = extras.getString("productDetail");
		productListStatus = extras.getString("productList");
		
		
	}


	private void initView() {
		final EditText edt_acc = (EditText)findViewById(R.id.login_edt_account);
		final EditText edt_pwd = (EditText)findViewById(R.id.login_edt_pwd);
		Button btn_signIn = (Button)findViewById(R.id.login_btn_signin);
		Button btn_register = (Button)findViewById(R.id.login_btn_register);
		
		btn_signIn.setOnClickListener(new OnClickListener() {
						
			@Override
			public void onClick(View v) {
				mdb.openDatabase();
				String userName = edt_acc.getText().toString();
				String pwd = edt_pwd.getText().toString();
				//login check
				//whether empty userName or password
				if(userName.length()==0||pwd.length()==0){
					Toast.makeText(SignInActivity.this, "UserName or Password cannot be empty ", Toast.LENGTH_LONG).show();
				}else{
					//whether wrong userName or password
					String isAcountExist = mdb.verifyAccountExist(userName);
					if(isAcountExist == null){
						Toast.makeText(SignInActivity.this, "No such account", Toast.LENGTH_SHORT).show();
					}else{
						//right password and userName
						if(mdb.verifyPassword(userName, pwd) == 0){
							mdb.login(userName);
							
							Intent intent = new Intent(SignInActivity.this, MainActivity.class);
							startActivity(intent);
							
							//wrong password or userName
						}else if(mdb.verifyPassword(userName, pwd) == 1){
							Toast.makeText(SignInActivity.this, "Wrong UserName or Password", Toast.LENGTH_SHORT).show();
						}
					}					
				}
				
				mdb.closeDatabase();
			}
		});
		
		
	    btn_register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String none ="none";
				Intent intent = new Intent(SignInActivity.this, RegisterActivity.class);
				if(cartStatusEmpty.equals("cartEmpty")){
					intent.putExtra("cart_empty", cartStatusEmpty);
					intent.putExtra("cart_item", none);
					intent.putExtra("wish", none);
					intent.putExtra("productDetail", none);
					intent.putExtra("productList", none);
					if(mApplication.productsMainActivity == true){
						categoryName = extras.getString("categoryName");
						intent.putExtra("categoryName", categoryName);
					}else if(mApplication.productsDetailActivity == true){
						productName = extras.getString("productName");
						intent.putExtra("productName", productName);
					}
				}else if(cartStatusItem.equals("cartWithItem")){
					intent.putExtra("cart_empty", none);
					intent.putExtra("cart_item", cartStatusItem);
					intent.putExtra("wish", none);
					intent.putExtra("productDetail", none);
					intent.putExtra("productList", none);
					if(mApplication.productsMainActivity == true){
						categoryName = extras.getString("categoryName");
						intent.putExtra("categoryName", categoryName);
					}else if(mApplication.productsDetailActivity == true){
						productName = extras.getString("productName");
						intent.putExtra("productName", productName);
					}
				}else if(wishListStatus.equals("wish")){
					intent.putExtra("cart_empty", none);
					intent.putExtra("cart_item", none);
					intent.putExtra("wish", wishListStatus);
					intent.putExtra("productDetail", none);
					intent.putExtra("productList", none);
					if(mApplication.productsMainActivity == true){
						categoryName = extras.getString("categoryName");
						intent.putExtra("categoryName", categoryName);
					}else if(mApplication.productsDetailActivity == true){
						productName = extras.getString("productName");
						intent.putExtra("productName", productName);
					}
				}else if(productListStatus.equals("productList")){
					categoryName = extras.getString("categoryName");
					intent.putExtra("cart_empty", none);
					intent.putExtra("cart_item", none);
					intent.putExtra("wish", none);
					intent.putExtra("productDetail", none);
					intent.putExtra("productList", productListStatus);
					intent.putExtra("categoryName", categoryName);
				}else if(productDetailStatus.equals("productDetail")){
					productName = extras.getString("productName");
					intent.putExtra("cart_empty", none);
					intent.putExtra("cart_item", none);
					intent.putExtra("wish", none);
					intent.putExtra("productDetail", productDetailStatus);
					intent.putExtra("productList", none);
					intent.putExtra("productName", productName);
				
				}else{
					intent.putExtra("cart_empty", none);
					intent.putExtra("cart_item", none);
					intent.putExtra("wish", none);
					intent.putExtra("productDetail", none);
					intent.putExtra("productList", none);
				}
				startActivity(intent);
			}
		});
	}


	protected void processCartItem(String logedUserName) {
		mdb.openDatabase();
		List<String> tempItemList = new ArrayList<String>();
		List<String> userItemList = new ArrayList<String>();
		List<String> sameItemName = new ArrayList<String>();
		//get item under temp
		tempItemList = mdb.getCartItemList("temp").get(1);
		//get item under loged userName
		userItemList = mdb.getCartItemList(logedUserName).get(1);
		//whether the user alrealy have this item in his cart
		if(userItemList.size() != 0){
			for(int i = 0; i < tempItemList.size(); i++){
				for(int j = 0; j< userItemList.size(); j++){
					if(tempItemList.get(i).equals(userItemList.get(j))){
						//save productName if user already have one in cart
						sameItemName.add(tempItemList.get(i));
					}
				}
			}
			
			//if have same item
			if(sameItemName.size() != 0){
				//get qty
				String tempItemQty;
				String userItemQty;
				int newQty;
				for(int a = 0; a < sameItemName.size(); a++){								
					tempItemQty = mdb.verifyItemInCart(sameItemName.get(a), "temp");
					userItemQty = mdb.verifyItemInCart(sameItemName.get(a), logedUserName);
					newQty = Integer.valueOf(tempItemQty) + Integer.valueOf(userItemQty);
					//update qty in user cart
					mdb.updateShoppingCartQty(newQty, sameItemName.get(a), logedUserName);
					//delete item in temp cart
					mdb.deleteCartItem(sameItemName.get(a), "temp");
				}							
				//move rest item form temp cart to user cart								
				mdb.updateCartUserName(logedUserName);
			}else{
				//if not have same item, just input all item
				mdb.updateCartUserName(logedUserName);	
			}
										
		}else{
			//if user cart is empty, just move input all item
			mdb.updateCartUserName(logedUserName);	
		}
			
		mdb.closeDatabase();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case android.R.id.home: 
			if(cartStatusEmpty.equals("cartEmpty")){
				categoryName = extras.getString("categoryName");
				productName = extras.getString("productName");
				Intent upIntent = new Intent(this, UnLoginEmptyCartActivity.class);
				upIntent.putExtra("categoryName", categoryName);
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
	            
			}else if(cartStatusItem.equals("cartWithItem")){
				categoryName = extras.getString("categoryName");
				productName = extras.getString("productName");
				Intent upIntent = new Intent(this, UnLoginItemCartActivity.class);
				upIntent.putExtra("categoryName", categoryName);
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
	            
			}else if(wishListStatus.equals("wish")){
				Intent upIntent = new Intent(this, UnLoginWishListActivity.class);
				if (NavUtils.shouldUpRecreateTask(this, upIntent)) {                   
	                TaskStackBuilder.from(this)
	                        //如果这里有很多原始的Activity,它们应该被添加在这里
	                        .addNextIntent(upIntent)
	                        .startActivities();
	                finish();
	            } else {                   
	                NavUtils.navigateUpTo(this, upIntent);
	            }
	            
			}else if(productListStatus.equals("productList")){
				Intent upIntent = new Intent(this, ProductsMainActivity.class);
				categoryName = extras.getString("categoryName");
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
	            
			}else if(productDetailStatus.equals("productDetail")){
				Intent upIntent = new Intent(this, ProductsDetailActivity.class);
				productName = extras.getString("productName");
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
