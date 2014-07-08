package com.meipinke.products;

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

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.CursorLoader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;

public class ProductsMainActivity extends Activity{
	
	ListView list;
	Cursor mCursor;
	CursorLoader mCursorLoader;
	ProductListAdapter mAdapter;
	mDatabase mdb = new mDatabase();
	String categoryName;	
	List<List<String>> productList = new ArrayList<List<String>>();
	String userName;
	
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_main_activity);
				
		getIntentTxt();
		
		initActionBar();
		
		initDB();	
		
		initListView();
		

	}
	

	private void getIntentTxt() {
		//get intent
		Bundle extras = getIntent().getExtras();
		categoryName = extras.getString("categoryName");	
		
		mApplication.setMainActBckTag(false);
		mApplication.setProductDetailActBckTag(false);
	}

	private void initActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);	

		
		actionBar.setTitle(categoryName);
	}
	
	private void initDB() {
		//db
		mdb.openDatabase();
		productList = mdb.getProducts(categoryName);
		userName = mdb.checkStatus();
		mdb.closeDatabase();		
	}
	
	private void initListView() {
		//get listview
				list = (ListView)findViewById(R.id.product_list);
//				list.setTag("mList");
				mAdapter = new ProductListAdapter(this,productList);
				list.setAdapter(mAdapter);
				
				
				list.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						//because listview_item contatin the textview, so we can find this textview, 
						//view stands for the item
						TextView tv_name = (TextView)view.findViewById(R.id.product_name);
						String productName = tv_name.getText().toString();
						Intent intent = new Intent(ProductsMainActivity.this, ProductsDetailActivity.class);
						intent.putExtra("productName", productName);
						startActivity(intent);	
						
					}
				});		
	}


	//-------------------------------------------listViewAdpter----------------------------------------------
	public class ProductListAdapter extends BaseAdapter{
		private Context context;
	    private List<List<String>> productList = new ArrayList<List<String>>();

		
		public ProductListAdapter(Context context, List<List<String>> productList){
			this.context = context;
			this.productList = productList;
			
			
		}

		@Override
		public int getCount() {			
			return productList.get(0).size();//productNameList
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View v, ViewGroup parent) {		
			//use Viewholder to let the TextView/Button clickable, 
			//or the ListView_Item click event will cover TextView click event
			//Viewholder cache the view, improve efficiency
			ViewHolder holder = null;
			if(v == null){
				//use inflate to get the listview_item page
				v = LayoutInflater.from(context).inflate
						(R.layout.product_listview_item, null);
				holder = new ViewHolder();
				//get widgets
				holder.img = (ImageView)v.findViewById(R.id.product_img);
				holder.tv_name =(TextView)v.findViewById(R.id.product_name);
				holder.tv_price = (TextView)v.findViewById(R.id.product_price);
				holder.tv_save = (TextView)v.findViewById(R.id.save);
				holder.tv_add = (TextView)v.findViewById(R.id.add);
				v.setTag(holder);	
			}else{
                holder = (ViewHolder) v.getTag();
			}
						
			//set content
			holder.tv_name.setText(productList.get(0).get(position));
			holder.tv_price.setText("$" + productList.get(1).get(position));
			//get img dynamic, use reflect
	
				Class c = R.drawable.class;
				Field field = null;
				try {
					field = c.getField(productList.get(2).get(position));//getField(name)
					holder.img.setImageResource(field.getInt(productList.get(2).get(position)));//getInt(name)
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			
			

			//set click event
			holder.tv_save.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mdb.openDatabase();
					String userName = mdb.checkStatus();
					String productImgS = productList.get(2).get(position);
					String productName = productList.get(0).get(position);
					String productPrice = productList.get(1).get(position);
					if(userName == null){
						initAlertDialog();
					}else{
						String isItemWished = mdb.checkItemWished(productName, userName);
						if(isItemWished != null){
							Toast.makeText(ProductsMainActivity.this, "Already saved", Toast.LENGTH_SHORT).show();
						}else{
							mdb.addWishItem(productImgS,productName,productPrice, userName);
							Toast.makeText(ProductsMainActivity.this, "Item saved", Toast.LENGTH_SHORT).show();


						}
					}
					mdb.closeDatabase();
				}
			});

			holder.tv_add.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mdb.openDatabase();
					String qty = "1";
					String productImgS = productList.get(2).get(position);
					String productName = productList.get(0).get(position);
					String productPrice = productList.get(1).get(position);
					String productAvail = mdb.getStock(productName);
                    
					if(Integer.valueOf(qty) > Integer.valueOf(productAvail)){
						Toast.makeText(ProductsMainActivity.this, "Out of Stock", Toast.LENGTH_SHORT).show();
					}else{
						String userName = mdb.checkStatus();
						
						if(userName == null){
							userName = "temp";
							String preQty = mdb.verifyItemInCart(productName, userName);
							if(preQty == null){
								mdb.addCartItem(productImgS, productName, productPrice, qty, userName);
								processStock(qty,productName,productAvail);
								Toast.makeText(ProductsMainActivity.this, "Item Added", Toast.LENGTH_SHORT).show();
							}else{
								int newQty = Integer.valueOf(qty) + Integer.valueOf(preQty);
								mdb.updateShoppingCartQty(newQty, productName, userName);
								processStock(qty,productName,productAvail);
								Toast.makeText(ProductsMainActivity.this, "Qty updated", Toast.LENGTH_SHORT).show();
							}						
						}else{
							String preQty = mdb.verifyItemInCart(productName, userName);
							if(preQty == null){
								mdb.addCartItem(productImgS, productName, productPrice, qty, userName);
								processStock(qty,productName,productAvail);
								Toast.makeText(ProductsMainActivity.this, "Item Added", Toast.LENGTH_SHORT).show();
							}else{
								int newQty = Integer.valueOf(qty) + Integer.valueOf(preQty);
								mdb.updateShoppingCartQty(newQty, productName, userName);
								processStock(qty,productName,productAvail);
								Toast.makeText(ProductsMainActivity.this, "Qty updated", Toast.LENGTH_SHORT).show();
							}
						}
						
						
					}
					mdb.closeDatabase();
				}
			});
			
		
  			return v;
		}

		protected void processStock(String qty, String productName,
				String productAvail) {
			int afterAvail = Integer.valueOf(productAvail)-Integer.valueOf(qty);
			mdb.updateProductStock(afterAvail,productName);			
		}

		protected void initAlertDialog() {
			Dialog alertDialog = new AlertDialog.Builder(ProductsMainActivity.this).
					setTitle("Save failed").
					setMessage("You need to login before save item").
					setIcon(R.drawable.ic_launcher).
					setPositiveButton("Sign in", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							String none = "none";
							Intent intent = new Intent(ProductsMainActivity.this,SignInActivity.class);
							intent.putExtra("cart_empty", none);
							intent.putExtra("cart_item", none);
							intent.putExtra("wish", none);
							intent.putExtra("productDetail", none);
							intent.putExtra("productList", "productList");
							intent.putExtra("categoryName", categoryName);
							startActivity(intent);
						}
					}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					}).create();
			
			alertDialog.show();				
		}

	}
	
	final static class ViewHolder{
		ImageView img;
		TextView tv_name;
		TextView tv_price;
		TextView tv_save;
		TextView tv_add;
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		//search
		getMenuInflater().inflate(R.menu.product_search_menu, menu);
		MenuItem searchItem = menu.findItem(R.id.action_searchview);

		final SearchView sv = (SearchView) searchItem.getActionView();
		sv.setQueryHint("Keyword");
		
		final List<List<String>> saveProductList = new ArrayList<List<String>>();
		for(int i = 0; i < productList.size(); i++){
			saveProductList.add(i, productList.get(i));
		}
		
		

		sv.setOnQueryTextListener(new OnQueryTextListener() {
			
			//button click then search
			@Override
			public boolean onQueryTextSubmit(String query) {
				//get search result
				mdb.openDatabase();
				List<List<String>> resultList = mdb.getSearchResult(query);
				mdb.closeDatabase();
				
                //count search result
				int countResult = resultList.get(0).size();
				Toast.makeText(ProductsMainActivity.this, "Find " + countResult + " item", Toast.LENGTH_LONG).show();
                //clear old list: data set of ListView Adapter
				productList.clear();
                //load new data and notify adapter to update
				for(int i =0;i<resultList.size(); i++){
					productList.add(i, resultList.get(i));
				}
				if(list!=null){
					mAdapter.notifyDataSetChanged();
				}
				
				//hide keyboard				
				hideKeyBoard();
				return false;
			}
			
			private void hideKeyBoard() {
				// TODO Auto-generated method stub
				InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				if(inputMethodManager != null){
					View v = ProductsMainActivity.this.getCurrentFocus();
					if(v == null){
						return;
					}
					
					inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
					sv.clearFocus();
				}
			}

			//auto search
			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				if(newText.length()<1){
//					Toast.makeText(ProductsMainActivity.this, "卧槽搜索框空了", Toast.LENGTH_LONG).show();
					productList.clear();
					for(int i =0;i < saveProductList.size(); i++){
						productList.add(i, saveProductList.get(i));
					}
					if(list!=null){
						mAdapter.notifyDataSetChanged();
					}
				}
				return false;

			}
		});
		
		
		
		return super.onCreateOptionsMenu(menu);
				
	}
	
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		    mdb.openDatabase();
		    String temp = "temp";
			String userName = mdb.checkStatus();
			String productName = mdb.checkShoppintCart(temp);
			switch(item.getItemId()){
			case android.R.id.home: 
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
                break;
            ///Wish List
			case R.id.menu_wish:
				if(userName == null) {
					Intent intent = new Intent(ProductsMainActivity.this,UnLoginWishListActivity.class);
					
					mApplication.setProductMainActBckTag(true);
					intent.putExtra("categoryName", categoryName);
					startActivity(intent);
					}else{
						Intent intent = new Intent(ProductsMainActivity.this,LoginWishListActivity.class);
						
						mApplication.setProductMainActBckTag(true);
						intent.putExtra("userName", userName);
						intent.putExtra("categoryName", categoryName);
						
						startActivity(intent);
					}
				break;
			case R.id.menu_cart:
				//if not log in
				if(userName == null){
					//if not login and no item in cart
					if(productName == null){
					Intent intent = new Intent(ProductsMainActivity.this,UnLoginEmptyCartActivity.class);

					mApplication.setProductMainActBckTag(true);
					intent.putExtra("categoryName", categoryName);
					
					startActivity(intent);
					}else{//not login and have item in cart
						Intent intent = new Intent(ProductsMainActivity.this, UnLoginItemCartActivity.class);

						mApplication.setProductMainActBckTag(true);
						intent.putExtra("categoryName", categoryName);

						startActivity(intent);
					}
				}else{
					String tempProductName = mdb.checkShoppintCart("temp");
					String userProductName = mdb.checkShoppintCart(userName);
					//if login and no item in temp cart and user cart
					if(tempProductName == null && userProductName == null){
							Intent intent = new Intent(ProductsMainActivity.this,LoginEmptyCartActivity.class);

							mApplication.setProductMainActBckTag(true);
							intent.putExtra("categoryName", categoryName);

							startActivity(intent);					
					}else if(tempProductName !=null && userProductName !=null){//login and have item in cart
						processCartItem();
						Intent intent = new Intent(ProductsMainActivity.this,LoginItemCartActivity.class);

						mApplication.setProductMainActBckTag(true);
						intent.putExtra("userName", userName);
						intent.putExtra("categoryName", categoryName);

						startActivity(intent);
					}else if(tempProductName !=null && userProductName == null){
						processCartItem();
						Intent intent = new Intent(ProductsMainActivity.this,LoginItemCartActivity.class);

						mApplication.setProductMainActBckTag(true);
						intent.putExtra("userName", userName);
						intent.putExtra("categoryName", categoryName);

						startActivity(intent);
					}else if(tempProductName == null && userProductName != null){
						processCartItem();
						Intent intent = new Intent(ProductsMainActivity.this,LoginItemCartActivity.class);

						mApplication.setProductMainActBckTag(true);
						intent.putExtra("userName", userName);
						intent.putExtra("categoryName", categoryName);

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


	private void processCartItem() {
		mDatabase mdb = new mDatabase();
		mdb.openDatabase();
		List<String> tempItemList = new ArrayList<String>();
		List<String> userItemList = new ArrayList<String>();
		List<String> sameItemName = new ArrayList<String>();
		//get item under temp
		tempItemList = mdb.getCartItemList("temp").get(1);
		//get item under loged userName
		userItemList = mdb.getCartItemList(userName).get(1);
		//whether the user already have this item in his cart
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
