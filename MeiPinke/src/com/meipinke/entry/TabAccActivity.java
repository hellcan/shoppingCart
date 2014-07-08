package com.meipinke.entry;


import com.example.meipinke.R;
import com.meipinke.database.mDatabase;
import com.meipinke.login.RegisterActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TabAccActivity extends Activity {
	mDatabase mdb = new mDatabase();
	private String userName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mdb.openDatabase();
		userName = mdb.checkStatus();
		mdb.closeDatabase();

		if(userName == null){
			setContentView(R.layout.tab_acc_unlog);
			
			initViewUnlog();
			
			

		}else{
			
			
			setContentView(R.layout.tab_acc_log);
			
			initViewLog();
						
		}
	}
	

	private void initViewLog() {
		// TODO Auto-generated method stub
		TextView tv_welcome = (TextView)findViewById(R.id.acc_account);
		
		mdb.openDatabase();
		String firstName = mdb.getFirstName(userName);
		mdb.closeDatabase();
		
		Button btn_signout = (Button)findViewById(R.id.acc_signout);
		
		tv_welcome.setText("Welcome, " + firstName);
		
		btn_signout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mdb.openDatabase();
				mdb.signOut(userName);
				mdb.closeDatabase();
				Toast.makeText(TabAccActivity.this, "Sign out Successful", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(TabAccActivity.this, MainActivity.class);
				startActivity(intent);
				
			}
		});
		
		
	}

	private void initViewUnlog() {
		// TODO Auto-generated method stub
		final EditText edt_1 = (EditText)findViewById(R.id.acc_edt_account);
		final EditText edt_2 = (EditText)findViewById(R.id.acc_edt_pwd);

		Button btn_signin = (Button)findViewById(R.id.acc_btn_signin);
		Button btn_register = (Button)findViewById(R.id.acc_btn_register);
		
		btn_signin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mdb.openDatabase();
				String userName = edt_1.getText().toString();
				String pwd = edt_2.getText().toString();
				
				//login check
				//whether empty userName or password
				if(userName.length()==0||pwd.length()==0){
					Toast.makeText(TabAccActivity.this, "UserName or Password cannot be empty ", Toast.LENGTH_SHORT).show();
				}else{
					//whether wrong userName or password
					String isAcountExist = mdb.verifyAccountExist(userName);
					if(isAcountExist == null){
						Toast.makeText(TabAccActivity.this, "No such account", Toast.LENGTH_SHORT).show();
					}else{
						//right password and userName
						if(mdb.verifyPassword(userName, pwd) == 0){
							mdb.login(userName);
							
							Intent intent = new Intent(TabAccActivity.this, TabAccActivity.class);
							startActivity(intent);
							
							//wrong password or userName
						}else if(mdb.verifyPassword(userName, pwd) == 1){
							Toast.makeText(TabAccActivity.this, "Wrong UserName or Password", Toast.LENGTH_SHORT).show();
						}
					}					
				}
				mdb.closeDatabase();
			}
		});
		
		btn_register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String none = "none";
				Intent intent = new Intent(TabAccActivity.this,RegisterActivity.class);
				intent.putExtra("cart_empty", none);
				intent.putExtra("cart_item", none);
				intent.putExtra("wish", none);
				intent.putExtra("productDetail", none);
				intent.putExtra("productList", none);
				startActivity(intent);
				
			}
		});
	}
	

}
