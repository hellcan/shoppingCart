package com.meipinke.products;

import java.lang.reflect.Field;
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
import com.meipinke.login.SignInActivity;
import com.meipinke.wish.LoginWishListActivity;
import com.meipinke.wish.UnLoginWishListActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProductsDetailActivity extends Activity {
	private String productName;
	mDatabase mdb = new mDatabase();
	private String productImgS;
	private String productImg;
	private String productModel;
	private String productPrice;
	private String productAvail;
	private String productDes;
	private String userName;
	private Bundle extras;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_detail_activity);
		
		initActionBar();
	
		getIntentTxt();
				
		initDB();
		
		initView();
			
	}

	private void getIntentTxt() {
		//get intent
		extras = getIntent().getExtras();
		productName = extras.getString("productName");	
		
		mApplication.setMainActBckTag(false);
		mApplication.setProductMainActBckTag(false);
	}

	private void initDB() {
		mdb.openDatabase();
		productImgS = mdb.getDetail(productName).get(0);
		productImg = mdb.getDetail(productName).get(1);
		productModel = mdb.getDetail(productName).get(2);
		productAvail = mdb.getDetail(productName).get(3);
		productPrice = mdb.getDetail(productName).get(4);
		productDes = mdb.getDetail(productName).get(5);		
	}

	private void initActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	private void initView() {
		TextView tv_name = (TextView)findViewById(R.id.detail_name);
		TextView tv_model = (TextView)findViewById(R.id.detail_model);
		final TextView tv_avail = (TextView)findViewById(R.id.detail_avail);
		TextView tv_price = (TextView)findViewById(R.id.detail_price);
		TextView tv_tax = (TextView)findViewById(R.id.detail_tax);
		TextView tv_des = (TextView)findViewById(R.id.detail_des);
		Button btn_add = (Button)findViewById(R.id.detail_add);
		final Button btn_save = (Button)findViewById(R.id.detail_save);
		final EditText edt_qty = (EditText)findViewById(R.id.detail_edt_qty);
		ImageView detail_img = (ImageView)findViewById(R.id.detail_img);	
		tv_name.setText(productName);
		//customize save button
		String user = mdb.checkStatus();
		if(user != null){
		String isItemWished = mdb.checkItemWished(productName, user);
		if(isItemWished != null){
			btn_save.setText("Item Saved");
			btn_save.setTextColor(Color.BLACK);
			btn_save.setBackgroundResource(R.color.grey_5);
			}
		}
		mdb.closeDatabase();
		tv_model.setText("Model: " + productModel);
		tv_avail.setText("Availability: " + productAvail);
		tv_price.setText("Price: " + "$" + productPrice);
		tv_tax.setText("Ex Tax: " + "$"+productPrice);
		tv_des.setText(productDes.replaceAll("//", "\n"));
		//img dynamic
		Class c = R.drawable.class;
		Field field = null;
		try {
			field = c.getField(productImg);
			detail_img.setImageResource(field.getInt(productImg));
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		
	    btn_add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mdb.openDatabase();
				String qty = edt_qty.getText().toString();
				String productAvail = mdb.getStock(productName);
	
				if(Integer.valueOf(qty) > Integer.valueOf(productAvail)){
					Toast.makeText(ProductsDetailActivity.this, "Out of Stock", Toast.LENGTH_SHORT).show();
				}else{
					userName = mdb.checkStatus();
					
					if(userName == null){
						userName = "temp";
						String preQty = mdb.verifyItemInCart(productName, userName);
						//if item not already exist
						if(preQty == null){
							mdb.addCartItem(productImgS,productName, productPrice, qty, userName);
							
							Toast.makeText(ProductsDetailActivity.this, "Item Added", Toast.LENGTH_SHORT).show();
							tv_avail.setText("Availability: " + processStock(qty, productAvail));
						}else{//if item not already exist
							//calculate new Qty
							int newQty = Integer.valueOf(qty) + Integer.valueOf(preQty);					
							mdb.updateShoppingCartQty(newQty, productName, userName);
							Toast.makeText(ProductsDetailActivity.this, "Qty updated", Toast.LENGTH_SHORT).show();
							tv_avail.setText("Availability: " + processStock(qty, productAvail));
	
						}				
					}else{
						String preQty = mdb.verifyItemInCart(productName, userName);
						if(preQty == null){
							mdb.addCartItem(productImgS,productName, productPrice, qty, userName);
							Toast.makeText(ProductsDetailActivity.this, "Item Added", Toast.LENGTH_SHORT).show();
							tv_avail.setText("Availability: " + processStock(qty, productAvail));
							
						}else{
							int newQty = Integer.valueOf(qty) + Integer.valueOf(preQty);
							mdb.updateShoppingCartQty(newQty, productName, userName);
							Toast.makeText(ProductsDetailActivity.this, "Qty updated", Toast.LENGTH_SHORT).show();
							tv_avail.setText("Availability: " + processStock(qty, productAvail));
						}
					}
					
				}
				mdb.closeDatabase();
			}
			
		});
		
		
		btn_save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mdb.openDatabase();
				userName = mdb.checkStatus();
				if(userName == null){
					initAlertDialog();
				}else{
					String isItemWished = mdb.checkItemWished(productName, userName);
					if(isItemWished != null){
						Toast.makeText(ProductsDetailActivity.this, "Already saved", Toast.LENGTH_SHORT).show();
					}else{
						mdb.addWishItem(productImgS,productName,productPrice,userName);
						Toast.makeText(ProductsDetailActivity.this, "Item saved", Toast.LENGTH_SHORT).show();
						btn_save.setText("Item Saved");
						btn_save.setTextColor(Color.BLACK);
						btn_save.setBackgroundResource(R.color.grey_5);
					}
					
				}
				mdb.closeDatabase();
			}
		});
	
	
	}

	protected String processStock(String qty, String productAvail2) {
		int afterAvail = Integer.valueOf(productAvail2)-Integer.valueOf(qty);
		mdb.updateProductStock(afterAvail,productName);
		return String.valueOf(afterAvail);
	}

	protected void initAlertDialog() {
		Dialog alertDialog = new AlertDialog.Builder(this).
				setTitle("Save failed").
				setMessage("You need to login before save item").
				setIcon(R.drawable.ic_launcher).
				setPositiveButton("Sign in", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String none = "none";
						Intent intent = new Intent(ProductsDetailActivity.this,SignInActivity.class);
						intent.putExtra("cart_empty", none);
						intent.putExtra("cart_item", none);
						intent.putExtra("wish", none);
						intent.putExtra("productDetail", "productDetail");
						intent.putExtra("productName", productName);
						intent.putExtra("productList", none);
						startActivity(intent);
					}
				}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).create();
		
		alertDialog.show();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.m_menu, menu);
		return true;
	}
	
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		mdb.openDatabase();

		switch(item.getItemId()){
		case android.R.id.home: 
			Intent upIntent = new Intent(this, ProductsMainActivity.class);
			mdb.openDatabase();
			String categoryName = mdb.getCategory(productName);
			mdb.closeDatabase();
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
            break;
		case R.id.menu_wish:
			String ss = mdb.checkStatus();
			if(ss == null) {
				Intent intent = new Intent(ProductsDetailActivity.this,UnLoginWishListActivity.class);
				intent.putExtra("productName", productName);
				mApplication.setProductDetailActBckTag(true);

				startActivity(intent);
				}else{
					Intent intent = new Intent(ProductsDetailActivity.this,LoginWishListActivity.class);
					mApplication.setProductDetailActBckTag(true);

					intent.putExtra("userName", ss);
					intent.putExtra("productName", productName);

					startActivity(intent);
				}
			break;
		case R.id.menu_cart:
			String s  = mdb.checkStatus();
			String tagProductName = extras.getString("productName");
			String tempProductName = mdb.checkShoppintCart("temp");
			
			//if not log in
			if(s == null){
				//if not login and no item in cart
				if(tempProductName == null){
				Intent intent = new Intent(ProductsDetailActivity.this,UnLoginEmptyCartActivity.class);

				mApplication.setProductDetailActBckTag(true);
				intent.putExtra("productName", tagProductName);
				startActivity(intent);
				}else{//not login and have item in cart
					Intent intent = new Intent(ProductsDetailActivity.this, UnLoginItemCartActivity.class);

					mApplication.setProductDetailActBckTag(true);
					intent.putExtra("productName", productName);
					startActivity(intent);
				}
			}else{
				String userProductName = mdb.checkShoppintCart(s);
				//if login and no item in temp cart and user cart
				if(tempProductName == null && userProductName == null){
						Intent intent = new Intent(ProductsDetailActivity.this,LoginEmptyCartActivity.class);

						mApplication.setProductDetailActBckTag(true);
						intent.putExtra("productName", productName);
						startActivity(intent);					
				}else if(tempProductName !=null && userProductName !=null){//login and have item in cart
					processCartItem();
					Intent intent = new Intent(ProductsDetailActivity.this,LoginItemCartActivity.class);
					
					mApplication.setProductDetailActBckTag(true);
					intent.putExtra("productName", productName);
					intent.putExtra("userName", s);
					startActivity(intent);
					Toast.makeText(ProductsDetailActivity.this, productName, Toast.LENGTH_LONG).show();
				}else if(tempProductName !=null && userProductName == null){
					processCartItem();
					Intent intent = new Intent(ProductsDetailActivity.this,LoginItemCartActivity.class);

					mApplication.setProductDetailActBckTag(true);
					intent.putExtra("productName", productName);
					intent.putExtra("userName", s);
					startActivity(intent);
				}else if(tempProductName == null && userProductName != null){
					processCartItem();
					Intent intent = new Intent(ProductsDetailActivity.this,LoginItemCartActivity.class);

					mApplication.setProductDetailActBckTag(true);
					intent.putExtra("productName", productName);
					intent.putExtra("userName", s);
					startActivity(intent);
				}
								
			}		
			mdb.closeDatabase();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void processCartItem() {
		// TODO Auto-generated method stub
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
