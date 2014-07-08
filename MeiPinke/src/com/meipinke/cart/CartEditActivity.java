package com.meipinke.cart;

import java.util.ArrayList;
import java.util.List;

import com.example.meipinke.R;
import com.meipinke.adapter.CartEditListAdapter;
import com.meipinke.cart.UnLoginItemCartActivity.Utility;
import com.meipinke.database.mDatabase;
import com.meipinke.login.SignInActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CartEditActivity extends Activity {
	mDatabase mdb = new mDatabase();
	CartEditListAdapter mAdapter;
	List<List<String>> itemList = new ArrayList<List<String>>();
	String userName;
	private String categoryName;
	private String productName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cart_edit_activity);
		
		initActionBar();
		getIntentTxt();
		
		initView();
		
	}
	
	private void initActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("Edit Cart");		
	}

	private void getIntentTxt() {
		//get userName
		Bundle extras = getIntent().getExtras();
		userName = extras.getString("userName");	
		categoryName = extras.getString("categoryName");
		productName = extras.getString("productName");
	}

	private void initView() {
		ListView list = (ListView)findViewById(R.id.edit_listview);
		Button btn_save = (Button)findViewById(R.id.edit_save);
		Button btn_cancel = (Button)findViewById(R.id.edit_cancel);
		
		mdb.openDatabase();
		itemList = mdb.getCartItemList(userName);
		mdb.closeDatabase();
		
		mAdapter = new CartEditListAdapter(this, itemList, userName);
		list.setAdapter(mAdapter);
		
		new Utility().setListViewHeightBasedOnChildren(list);
		
		btn_save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
							
				processItemInDB();
                mdb.openDatabase();
                                
                String pName = mdb.checkShoppintCart(userName);
				if(userName.equals("temp")){
					if(pName == null){
						Intent intent = new Intent(CartEditActivity.this,UnLoginEmptyCartActivity.class);
						intent.putExtra("categoryName", categoryName);
						intent.putExtra("productName", productName);
						startActivity(intent);
					}else{
						Intent intent = new Intent(CartEditActivity.this,UnLoginItemCartActivity.class);
						intent.putExtra("userName", userName);
						intent.putExtra("categoryName", categoryName);
						intent.putExtra("productName", productName);

						startActivity(intent);
					}
				}else{
					if(pName == null){
						Intent intent = new Intent(CartEditActivity.this,LoginEmptyCartActivity.class);
						intent.putExtra("categoryName", categoryName);
						intent.putExtra("productName", productName);
						startActivity(intent);
					}else{
						Intent intent = new Intent(CartEditActivity.this,LoginItemCartActivity.class);
						intent.putExtra("userName", userName);
						intent.putExtra("categoryName", categoryName);
						intent.putExtra("productName", productName);
						
						startActivity(intent);
					}
				}
                mdb.closeDatabase();
			}

			private void processItemInDB() {
				List<String> name = mAdapter.getQtyData().get(0);//name
				List<String> newQty = mAdapter.getQtyData().get(1);//new qty
				List<String> stock = mAdapter.getQtyData().get(2);//new stock
				List<String> delName = mAdapter.getDelData().get(0);
				List<String> delQty = mAdapter.getDelData().get(1);
				//if just delete instead of change qty
				mdb.openDatabase();
                for(int a = 0; a<name.size(); a++){
                	for(int b = 0; b<delName.size();b++){
                		if(name.get(a).equals(delName.get(b))){
                			mdb.deleteCartItem(delName.get(b), userName);
                			mdb.updateProductStock(Integer.valueOf(delQty.get(b)), delName.get(b));
//            				Toast.makeText(CartEditActivity.this, "删除后的库存是： " + delQty.get(0), Toast.LENGTH_LONG).show();

                			name.remove(a);
                			newQty.remove(a);
                			stock.remove(a);
                			delName.remove(b);
                			delQty.remove(b);
                		}
                	}
                }
              //update qty 
                for(int i=0;i<name.size();i++){
                	mdb.updateShoppingCartQty(Integer.valueOf(newQty.get(i)), name.get(i), userName);
                	mdb.updateProductStock(Integer.valueOf(stock.get(i)),name.get(i));
                }
                //delete item in cart DB
                for(int j=0;j < delName.size();j++){
                	mdb.deleteCartItem(delName.get(j), userName);
                	mdb.updateProductStock(Integer.valueOf(delQty.get(j)), delName.get(j));
                }
                mdb.closeDatabase();
			}
		});
		
		btn_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(userName.equals("temp")){
					Intent intent = new Intent(CartEditActivity.this,UnLoginItemCartActivity.class);
					intent.putExtra("categoryName", categoryName);
					intent.putExtra("productName", productName);
					

					startActivity(intent);
				}else{
					Intent intent = new Intent(CartEditActivity.this,LoginItemCartActivity.class);
					intent.putExtra("userName", userName);
					intent.putExtra("categoryName", categoryName);
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
			if(userName.equals("temp")){
				
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
		            return true;
			}else{
				
				Intent upIntent = new Intent(this, LoginItemCartActivity.class);
				upIntent.putExtra("categoryName", categoryName);
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
		            return true;
			}
						
			}
			return super.onOptionsItemSelected(item);
		}
	
	public class Utility {
        public  void setListViewHeightBasedOnChildren(ListView listView) {
               //get list adapter
               ListAdapter listAdapter = listView.getAdapter();
               if (listAdapter == null) {
                      return;
               }

               int totalHeight = 0;
               for (int i = 0, len = listAdapter.getCount(); i < len; i++) {   //listAdapter.getCount()
                      View listItem = listAdapter.getView(i, null, listView);
                      listItem.measure(0, 0);  //calculate item height
                      totalHeight += listItem.getMeasuredHeight();  //calculate whole height
               }

               ViewGroup.LayoutParams params = listView.getLayoutParams();
               params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
               //listView.getDividerHeight()
               //params.height
               listView.setLayoutParams(params);
        }
	}
}
