package com.meipinke.login;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.meipinke.R;
import com.meipinke.application.mApplication;
import com.meipinke.database.mDatabase;
import com.meipinke.entry.MainActivity;

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

public class RegisterActivity extends Activity {
	mDatabase mdb = new mDatabase();
	String cartStatusEmpty = "default";
	String cartStatusItem = "default";
	String wishListStatus = "default";
	String productDetailStatus = "default";
	String productListStatus = "default";
	String categoryName= "default";
	String productName= "default";
	Bundle extras;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_activity);
		
		initActionBar();
		getIntentTxt();
		initView();
	
	}

	private void getIntentTxt() {
		
		extras = getIntent().getExtras();
		cartStatusEmpty = extras.getString("cart_empty");
		cartStatusItem = extras.getString("cart_item");
		wishListStatus = extras.getString("wish");
		productDetailStatus = extras.getString("productDetail");
		productListStatus = extras.getString("productList");

	}

	private void initActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("Create Account");
	}

	private void initView() {
		final EditText edt_email = (EditText)findViewById(R.id.reg_edt_email);
		final EditText edt_pwd = (EditText)findViewById(R.id.reg_edt_pwd);
		final EditText edt_confPwd = (EditText)findViewById(R.id.reg_edt_conf_pwd);
		final EditText edt_firstName = (EditText)findViewById(R.id.reg_edt_firstname);
		final EditText edt_lastName = (EditText)findViewById(R.id.reg_edt_lastname);
		final EditText edt_address = (EditText)findViewById(R.id.reg_edt_address);
		final EditText edt_city = (EditText)findViewById(R.id.reg_edt_city);
		final EditText edt_state = (EditText)findViewById(R.id.reg_edt_state);
		final EditText edt_zip = (EditText)findViewById(R.id.reg_edt_zip);
		Button btn_submit = (Button)findViewById(R.id.reg_btn_submit);
		Button btn_signIn = (Button)findViewById(R.id.reg_btn_signin);
		
		btn_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mdb.openDatabase();
				String email = edt_email.getText().toString();
				String pwd = edt_pwd.getText().toString();
				String confPwd = edt_confPwd.getText().toString();
				String firstName = edt_firstName.getText().toString();
				String lastName = edt_lastName.getText().toString();
				String address = edt_address.getText().toString();
				String city = edt_city.getText().toString();
				String state = edt_state.getText().toString();
				String zip = edt_zip.getText().toString();
				
				String a = mdb.verifyAccountExist(email);
				Boolean isEmail = isEmail(email);
//					Toast.makeText(RegisterActivity.this, a, Toast.LENGTH_LONG).show();
				//check empty
				if(isEmail == false){
					Toast.makeText(RegisterActivity.this, "Not an email", Toast.LENGTH_SHORT).show();
				}else{
					if(email.equals("")||pwd.equals("")||confPwd.equals("")||firstName.equals("")||lastName.equals("")
							||address.equals("")||city.equals("")||state.equals("")||zip.equals("")){
					
						Toast.makeText(RegisterActivity.this, "Please fill all the blank", Toast.LENGTH_LONG).show();
						
					}else{
						//check whether password match confirm password
						if(pwd.equals(confPwd)){
							//check whether userName exist
							if(a == null){
								mdb.register(email, pwd, lastName, firstName, address, city, state, zip);
								Toast.makeText(RegisterActivity.this, "Register Successful", Toast.LENGTH_LONG).show();
								cleanText();
							}else{
								Toast.makeText(RegisterActivity.this, "Email already exist", Toast.LENGTH_LONG).show();
							}
						}else{
							Toast.makeText(RegisterActivity.this, "Password not match", Toast.LENGTH_LONG).show();
						}
					}
				}
			}
				
			private Boolean isEmail(String strEmail) {
				// TODO Auto-generated method stub
				String strPattern = "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";

				Pattern p = Pattern.compile(strPattern);
				Matcher m = p.matcher(strEmail);
				return m.matches();
			}

			private void cleanText() {
				edt_email.setText("");
				edt_pwd.setText("");
				edt_confPwd.setText("");
				edt_firstName.setText("");
				edt_lastName.setText("");
				edt_address.setText("");
				edt_city.setText("");
				edt_state.setText("");
				edt_zip.setText("");
			}

							
		});
		
		btn_signIn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RegisterActivity.this,SignInActivity.class);
				intent.putExtra("cart_empty", "none");
				intent.putExtra("cart_item", "none");
				intent.putExtra("wish", "none");
				intent.putExtra("productDetail", "none");
				intent.putExtra("productList", "none");
				startActivity(intent);
			}
		});
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case android.R.id.home: 
			String none = "none";
			Intent upIntent = new Intent(this, SignInActivity.class);
			if(cartStatusEmpty.equals("cartEmpty")){
				upIntent.putExtra("cart_empty", cartStatusEmpty);
				upIntent.putExtra("cart_item", none);
				upIntent.putExtra("wish", none);
				upIntent.putExtra("productDetail", none);
				upIntent.putExtra("productList", none);
				if(mApplication.productsMainActivity == true){
					categoryName = extras.getString("categoryName");
					upIntent.putExtra("categoryName", categoryName);
				}else if(mApplication.productsDetailActivity == true){
					productName = extras.getString("productName");
					upIntent.putExtra("productName", productName);
				}
				
			}else if(cartStatusItem.equals("cartWithItem")){
				upIntent.putExtra("cart_empty", none);
				upIntent.putExtra("cart_item", cartStatusItem);
				upIntent.putExtra("wish", none);
				upIntent.putExtra("productDetail", none);
				upIntent.putExtra("productList", none);
				if(mApplication.productsMainActivity == true){
					categoryName = extras.getString("categoryName");
					upIntent.putExtra("categoryName", categoryName);
				}else if(mApplication.productsDetailActivity == true){
					productName = extras.getString("productName");
					upIntent.putExtra("productName", productName);
				}
			}else if(wishListStatus.equals("wish")){
				upIntent.putExtra("cart_empty", none);
				upIntent.putExtra("cart_item", none);
				upIntent.putExtra("wish", wishListStatus);
				upIntent.putExtra("productDetail", none);
				upIntent.putExtra("productList", none);
				if(mApplication.productsMainActivity == true){
					categoryName = extras.getString("categoryName");
					upIntent.putExtra("categoryName", categoryName);
				}else if(mApplication.productsDetailActivity == true){
					productName = extras.getString("productName");
					upIntent.putExtra("productName", productName);
				}
			}else if(productListStatus.equals("productList")){//if the page come from the dialog "sign in" button, productList page 
				categoryName = extras.getString("categoryName");
				upIntent.putExtra("cart_empty", none);
				upIntent.putExtra("cart_item", none);
				upIntent.putExtra("wish", none);
				upIntent.putExtra("productDetail", none);
				upIntent.putExtra("productList", productListStatus);
				upIntent.putExtra("categoryName", categoryName);
				
			}else if(productDetailStatus.equals("productDetail")){//if the page come forom the dialog "sign in" button, productDeatil page
				productName = extras.getString("productName");
				upIntent.putExtra("cart_empty", none);
				upIntent.putExtra("cart_item", none);
				upIntent.putExtra("wish", none);
				upIntent.putExtra("productDetail", productDetailStatus);
				upIntent.putExtra("productList", none);
				upIntent.putExtra("productName", productName);
				
			}else{//back to my Account unLogin page
				upIntent.putExtra("cart_empty", none);
				upIntent.putExtra("cart_item", none);
				upIntent.putExtra("wish", none);
				upIntent.putExtra("productDetail", none);
				upIntent.putExtra("productList", none);
				
			}
			
			if (NavUtils.shouldUpRecreateTask(this, upIntent)) {                   
                TaskStackBuilder.from(this)
                        //如果这里有很多原始的Activity,它们应该被添加在这里
                        .addNextIntent(upIntent)
                        .startActivities();
                finish();
            } else {                   
                NavUtils.navigateUpTo(this, upIntent);
            }
			
	            
	            return true;			
			}
			return super.onOptionsItemSelected(item);
		}

}
