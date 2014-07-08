package com.meipinke.entry;

import java.util.ArrayList;
import java.util.List;

import com.example.meipinke.R;
import com.meipinke.application.mApplication;
import com.meipinke.cart.LoginEmptyCartActivity;
import com.meipinke.cart.LoginItemCartActivity;
import com.meipinke.cart.UnLoginEmptyCartActivity;
import com.meipinke.cart.UnLoginItemCartActivity;
import com.meipinke.database.mDatabase;
import com.meipinke.products.ProductsMainActivity;
import com.meipinke.wish.LoginWishListActivity;
import com.meipinke.wish.UnLoginWishListActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;



@SuppressLint("NewApi")
public class MainActivity extends TabActivity {

	RelativeLayout bottom_layout;
	TabHost tabHost;
	TabHost.TabSpec tabSpec;
	RadioGroup radioGroup;
	ImageView img;
	int startLeft;
	mDatabase mdb = new mDatabase();

	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		
		initActionBar();
		
		mApplication.setProductMainActBckTag(false);
        mApplication.setProductDetailActBckTag(false);
        
        initView();
	}
	
	@SuppressWarnings("deprecation")
	private void initView() {
		// TODO Auto-generated method stub
        bottom_layout = (RelativeLayout) findViewById(R.id.layout_bottom);
        
        tabHost = getTabHost();
        tabHost.addTab(tabHost.newTabSpec("home").setIndicator("Home").setContent(new Intent(this, TabHomeActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("info").setIndicator("Info").setContent(new Intent(this, TabInfoActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("cust").setIndicator("Cust").setContent(new Intent(this, TabCustServiceActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("extras").setIndicator("Extras").setContent(new Intent(this, TabExtrasActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("acc").setIndicator("Acc").setContent(new Intent(this, TabAccActivity.class)));
        
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(checkedChangeListener);
        
        img = new ImageView(this);
        img.setImageResource(R.drawable.tab_front_bg);
        bottom_layout.addView(img);
	}

	private void initActionBar() {
		// TODO Auto-generated method stub
		//actionBar
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(false);
//		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		
//		actionBar.setDisplayShowTitleEnabled(false);
		
		mdb.openDatabase();
		String welcomeUser = mdb.checkStatus();
		if(welcomeUser == null){
			
		}else{
			actionBar.setTitle("Welcome, " + mdb.getFirstName(welcomeUser) + "  ");
		}
		mdb.closeDatabase();			
	}

	private OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.radio_home:
				tabHost.setCurrentTabByTag("home");
				MoveBg.moveFrontBg(img, startLeft, 0, 0, 0);
				startLeft = 0;
				break;
			case R.id.radio_info:
				tabHost.setCurrentTabByTag("info");
				MoveBg.moveFrontBg(img, startLeft, img.getWidth(), 0, 0);
				startLeft = img.getWidth();
				break;
			case R.id.radio_cust:
				tabHost.setCurrentTabByTag("cust");
				MoveBg.moveFrontBg(img, startLeft, img.getWidth() * 2, 0, 0);
				startLeft = img.getWidth() * 2;
				break;
			case R.id.radio_extras:
				tabHost.setCurrentTabByTag("extras");
				MoveBg.moveFrontBg(img, startLeft, img.getWidth() * 3, 0, 0);
				startLeft = img.getWidth() * 3;
				break;
			case R.id.radio_acc:
				tabHost.setCurrentTabByTag("acc");
				MoveBg.moveFrontBg(img, startLeft, img.getWidth() * 4, 0, 0);
				startLeft = img.getWidth() * 4;
				break;

			default:
				break;
			}
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.m_menu, menu);
		return true;				
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		mdb.openDatabase();
	    String temp = "temp";
		String userName = mdb.checkStatus();
		String productName = mdb.checkShoppintCart(temp);
		switch(item.getItemId()){
        ///Wish List
		case R.id.menu_wish:
			if(userName == null) {
				Intent intent = new Intent(MainActivity.this,UnLoginWishListActivity.class);
				mApplication.setMainActBckTag(true);
				startActivity(intent);
				}else{
					Intent intent = new Intent(MainActivity.this,LoginWishListActivity.class);
					mApplication.setMainActBckTag(true);
					intent.putExtra("userName", userName);
					startActivity(intent);
				}
			break;
		case R.id.menu_cart:
			//if not log in
			String none ="none";
			//if not log in
			if(userName == null){
				//if not login and no item in cart
				if(productName == null){
				Intent intent = new Intent(MainActivity.this,UnLoginEmptyCartActivity.class);	
				mApplication.setMainActBckTag(true);
				startActivity(intent);
				}else{//not login and have item in cart
					Intent intent = new Intent(MainActivity.this, UnLoginItemCartActivity.class);
					intent.putExtra("categroyName", none);
					intent.putExtra("productName", none);
					mApplication.setMainActBckTag(true);
					startActivity(intent);
				}
			}else{
				String tempProductName = mdb.checkShoppintCart(temp);
				String userProductName = mdb.checkShoppintCart(userName);
				//if login and no item in temp cart and user cart
				if(tempProductName == null && userProductName == null){
						Intent intent = new Intent(MainActivity.this,LoginEmptyCartActivity.class);
						mApplication.setMainActBckTag(true);;
						startActivity(intent);					
				}else if(tempProductName !=null && userProductName !=null){//login and have item in cart
					processCartItem(userName);
					Intent intent = new Intent(MainActivity.this,LoginItemCartActivity.class);
					
					mApplication.setMainActBckTag(true);
					intent.putExtra("userName", userName);
					intent.putExtra("categroyName", none);
					intent.putExtra("productName", none);
					startActivity(intent);
				}else if(tempProductName !=null && userProductName == null){
					processCartItem(userName);
					Intent intent = new Intent(MainActivity.this,LoginItemCartActivity.class);
					mApplication.setMainActBckTag(true);
					intent.putExtra("userName", userName);
					intent.putExtra("categroyName", none);
					intent.putExtra("productName", none);
					startActivity(intent);
				}else if(tempProductName == null && userProductName != null){
					processCartItem(userName);
					Intent intent = new Intent(MainActivity.this,LoginItemCartActivity.class);
					mApplication.setMainActBckTag(true);
					intent.putExtra("userName", userName);
					intent.putExtra("categroyName", none);
					intent.putExtra("productName", none);
					startActivity(intent);
				}								
			}	
								
					
            break;			
		default:
			break;
			
		}
		
		mdb.closeDatabase();	
		return super.onOptionsItemSelected(item);
	}
	

	

	private void processCartItem(String userName) {
		mDatabase mdb = new mDatabase();
		mdb.openDatabase();
		List<String> tempItemList = new ArrayList<String>();
		List<String> userItemList = new ArrayList<String>();
		List<String> sameItemName = new ArrayList<String>();
		//get item under temp
		tempItemList = mdb.getCartItemList("temp").get(1);
		//get item under loged userName
		userItemList = mdb.getCartItemList(userName).get(1);
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
					userItemQty = mdb.verifyItemInCart(sameItemName.get(a), userName);
					newQty = Integer.valueOf(tempItemQty) + Integer.valueOf(userItemQty);
					//update qty in user cart
					mdb.updateShoppingCartQty(newQty, sameItemName.get(a), userName);
					//delete item in temp cart
					mdb.deleteCartItem(sameItemName.get(a), "temp");
				}							
				//move rest item form temp cart to user cart								
				mdb.updateCartUserName(userName);
			}else{
				//if not have same item, just input all item
				mdb.updateCartUserName(userName);	
			}
										
		}else{
			//if user cart is empty, just move input all item
			mdb.updateCartUserName(userName);	
		}
			
		mdb.closeDatabase();
	}
		
}



