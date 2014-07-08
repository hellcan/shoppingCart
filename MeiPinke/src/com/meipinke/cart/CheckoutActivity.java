package com.meipinke.cart;


import java.util.ArrayList;
import java.util.List;

import com.example.meipinke.R;
import com.meipinke.application.mApplication;
import com.meipinke.database.mDatabase;
import com.meipinke.entry.MainActivity;
import com.meipinke.products.ProductsDetailActivity;
import com.meipinke.products.ProductsMainActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


public class CheckoutActivity extends Activity {
	mDatabase mdb = new mDatabase();
	private Bundle extras;
	private String userName;
	private String categoryName;
	private String productName;
	List<String> cardNumber = new ArrayList<String>();
	int selectedPosition;


	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cart_checkout);
		
		initActionBar();
		getIntentTxt();
		final CheckBox mCheckBox = (CheckBox)findViewById(R.id.chkout_select_card);
		Button btn_select_card = (Button)findViewById(R.id.chkout_btn_select);
		final EditText edt_firstName = (EditText)findViewById(R.id.chk_edt_firstname);
		final EditText edt_lastName = (EditText)findViewById(R.id.chk_edt_lastname);
		final EditText edt_cardNum = (EditText)findViewById(R.id.chk_edt_card_number);
		final EditText edt_exp = (EditText)findViewById(R.id.chk_edt_exp);
		final EditText edt_date = (EditText)findViewById(R.id.chk_edt_date);
		final EditText edt_sec = (EditText)findViewById(R.id.chk_edt_sec);
		Button btn_continue = (Button)findViewById(R.id.chkout_btn_continue);

		mdb.openDatabase();
		cardNumber = mdb.getCardInfo(userName);
		if(cardNumber.size()==0){
			mCheckBox.setText("No card available");
		}else{
			mCheckBox.setText(cardNumber.get(0));
		}
		mdb.closeDatabase();
		
		btn_select_card.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if(cardNumber.size() == 0){
					Dialog alertDialog = new AlertDialog.Builder(CheckoutActivity.this).
							setTitle("Select a card").
							setMessage("No card available").
							setIcon(R.drawable.ic_launcher).
							setNegativeButton("OK", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {

								}
							}).create();
					alertDialog.show();
							
				}else{
					String[] items = new String[cardNumber.size()];

					for(int i =0; i < cardNumber.size(); i++){
						items[i] = cardNumber.get(i);
					}
					
					Dialog alertDialog = new AlertDialog.Builder(CheckoutActivity.this).
							setTitle("Select a card").
							setIcon(R.drawable.ic_launcher).
							setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {

							selectedPosition = which;
							
						}
					}).setNegativeButton("Save", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							mCheckBox.setText(cardNumber.get(selectedPosition));
							if(mCheckBox.isChecked() == false){
								mCheckBox.setChecked(true);

							}
						}
					}).setPositiveButton("Cancel",new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					}).create();
					
					alertDialog.show();
				}
				
			}
		});
		btn_continue.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String fstName = edt_firstName.getText().toString();
				final String lstName = edt_lastName.getText().toString();
				final String cardNum = edt_cardNum.getText().toString();
				final String year = edt_exp.getText().toString();
				final String month = edt_date.getText().toString();
				final String sCode = edt_sec.getText().toString();
				
				if(mCheckBox.isChecked() == false){
					if(fstName.equals("")||lstName.equals("")||cardNum.equals("")||year.equals("")||
							month.equals("")||sCode.equals("")){
						Toast.makeText(CheckoutActivity.this, "Please fill all the blank", Toast.LENGTH_SHORT).show();
					}else{
												
						Dialog alertDialog = new AlertDialog.Builder(CheckoutActivity.this).
								setTitle("Select a card").
								setMessage("Do you want to save this card ?").
								setIcon(R.drawable.ic_launcher).
								setNegativeButton("Yes", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								mdb.openDatabase();
								mdb.insertCard(fstName, lstName, cardNum, year, month, sCode, userName);
								mdb.closeDatabase();
								
								Toast.makeText(CheckoutActivity.this, "Card saved", Toast.LENGTH_SHORT).show();
								
								Intent intent = new Intent(CheckoutActivity.this, CheckoutPlaceOrderActivity.class);
								intent.putExtra("categoryName", categoryName);
								intent.putExtra("productName", productName);
								intent.putExtra("userName", userName);
								startActivity(intent);	
							}
						}).setPositiveButton("No",new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								Toast.makeText(CheckoutActivity.this, "Card not saved", Toast.LENGTH_SHORT).show();

								Intent intent = new Intent(CheckoutActivity.this, CheckoutPlaceOrderActivity.class);
								intent.putExtra("categoryName", categoryName);
								intent.putExtra("productName", productName);
								intent.putExtra("userName", userName);
								startActivity(intent);	
							}
						}).create();
						
						alertDialog.show();
										
					}
					
				}else{
					
					//get card number
					String selectedCardNum = mCheckBox.getText().toString();
					if(selectedCardNum.equals("No card available")){
						Toast.makeText(CheckoutActivity.this, "Please choose a card", Toast.LENGTH_SHORT).show();
					}else{
						Intent intent = new Intent(CheckoutActivity.this, CheckoutPlaceOrderActivity.class);
						intent.putExtra("categoryName", categoryName);
						intent.putExtra("productName", productName);
						intent.putExtra("userName", userName);
						startActivity(intent);	
					}
					

				}
				
			}
		});
		
		
		


	}



	private void getIntentTxt() {
		// TODO Auto-generated method stub
		extras = getIntent().getExtras();
		userName = extras.getString("userName");
	}



	private void initActionBar() {
		// TODO Auto-generated method stub
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("Check Out: Step 1");
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if(mApplication.mainActivity == true){
				Intent upIntent = new Intent(this, LoginItemCartActivity.class);
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

				Intent upIntent = new Intent(this, LoginItemCartActivity.class);
				upIntent.putExtra("categoryName", categoryName);
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
			}else if(mApplication.productsDetailActivity == true){
				productName = extras.getString("productName");

				Intent upIntent = new Intent(this, LoginItemCartActivity.class);
				upIntent.putExtra("productName", productName);
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
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
