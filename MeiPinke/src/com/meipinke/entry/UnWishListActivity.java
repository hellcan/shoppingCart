package com.meipinke.entry;

import com.example.meipinke.R;
import com.meipinke.login.SignInActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class UnWishListActivity extends Activity {
	
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cart_unlogin_empty);
		
		//actionbar
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("My Wish List");
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		TextView tv1 = (TextView)findViewById(R.id.cart_1);
		TextView tv2 = (TextView)findViewById(R.id.cart_2);
		Button btn = (Button)findViewById(R.id.cart_signin);
		tv1.setText("Can't view your Wish List yet");
		tv2.setText("Sign in to view items that you saved to your Wish List");
		btn.setBackgroundColor(getResources().getColor(R.color.green_5));
		
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UnWishListActivity.this, SignInActivity.class);
				startActivity(intent);
				
			}
		});
	}

}
