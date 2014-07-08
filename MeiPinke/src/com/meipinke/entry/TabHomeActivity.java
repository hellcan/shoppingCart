package com.meipinke.entry;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.meipinke.R;
import com.meipinke.products.ProductsMainActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class TabHomeActivity extends Activity {
	String[] categoryName = {"Clothing", "Home & Garden", "Desktops", "Laptops & Notebooks", "Watches & Accessory", "Beauty",
			"Handbags", "Phones & PDAs", "Sports"}; 
	int[] categoryImg = {R.drawable.cloth, R.drawable.home, R.drawable.desktops, R.drawable.laptops, R.drawable.watch, R.drawable.beauty,
			R.drawable.handbags, R.drawable.phone, R.drawable.sports};
	PopupWindow pop;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_home);

		initViews();
	}

	private void initViews() {
		initTextView();
		initListView();
        initPopupWindow();	    
	}


	
	private void initTextView() {
		
		TextView tv_home = (TextView)findViewById(R.id.tv_home);
		tv_home.setText("Home & Garden");
		
		TextView tv_lap = (TextView)findViewById(R.id.tv_laptop);
		tv_lap.setText("Laptops & Notebooks");
		
		TextView tv_cloth = (TextView)findViewById(R.id.tv_cloth);
		TextView tv_desktop = (TextView)findViewById(R.id.tv_desktop);

        tv_cloth.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TabHomeActivity.this,ProductsMainActivity.class);
				intent.putExtra("categoryName", categoryName[0]);
				startActivity(intent);
			}
		});
        
        tv_home.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TabHomeActivity.this,ProductsMainActivity.class);
				intent.putExtra("categoryName", categoryName[1]);
				startActivity(intent);
			}
		});
        
        tv_desktop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TabHomeActivity.this,ProductsMainActivity.class);
				intent.putExtra("categoryName", categoryName[2]);
				startActivity(intent);
			}
		});
        
        tv_lap.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TabHomeActivity.this,ProductsMainActivity.class);
				intent.putExtra("categoryName", categoryName[3]);
				startActivity(intent);				
			}
		});
		
	}
	
	private void initListView() {

		ListView list = (ListView)findViewById(R.id.home_category_list);
		List<HashMap<String, Object>> data=new ArrayList<HashMap<String,Object>>();   
	       for(int i = 0; i < categoryName.length;i++){   
	         HashMap<String, Object> hashMap=new HashMap<String, Object>();  
	         hashMap.put("img", categoryImg[i]);  
	         hashMap.put("name", categoryName[i]);  
	         data.add(hashMap);
	         }
	    SimpleAdapter adapter=new SimpleAdapter(this,data,R.layout.category_listview_item,
	    		new String[]{"img","name"},new int[]{R.id.category_img,R.id.category_name});              
	    list.setAdapter(adapter);  
	    
	    list.setOnItemClickListener(new OnItemClickListener() {


			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String name = categoryName[position];
//				Toast.makeText(TabHomeActivity.this, name, Toast.LENGTH_LONG).show();	
				Intent intent = new Intent(TabHomeActivity.this, ProductsMainActivity.class);
				intent.putExtra("categoryName", name);
				startActivity(intent);				
			}
	    	
		});		
	}

	private void initPopupWindow() {
		//popupwindow
        LayoutInflater inflater = LayoutInflater.from(this); 
        final View view = inflater.inflate(R.layout.popupwindow, null); 
        pop = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false); 
        pop.setBackgroundDrawable(new BitmapDrawable()); 
        pop.setOutsideTouchable(true); 
        pop.setFocusable(true);
        
  
	    //bar
	    ImageButton more = (ImageButton)findViewById(R.id.btn_more);
	    more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(pop.isShowing()) { 
					pop.dismiss();
				}else{
					pop.showAsDropDown(v, 0, 18);						
					initTextWiget(view);					
				}			
			}

			private void initTextWiget(View view) {
				TextView tv_watch = (TextView)view.findViewById(R.id.tv_watch);
				TextView tv_beauty = (TextView)view.findViewById(R.id.tv_beauty);
				TextView tv_hand = (TextView)view.findViewById(R.id.tv_handbags);
				TextView tv_phone = (TextView)view.findViewById(R.id.tv_phone);
				TextView tv_sports = (TextView)view.findViewById(R.id.tv_sports);

				tv_watch.setText("Watches & Accessory");
				tv_phone.setText("Phones & PDAs");	
				
				tv_watch.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(TabHomeActivity.this,ProductsMainActivity.class);
						intent.putExtra("categoryName", categoryName[4]);
						startActivity(intent);						
					}
				});
				
				tv_beauty.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(TabHomeActivity.this,ProductsMainActivity.class);
						intent.putExtra("categoryName", categoryName[5]);
						startActivity(intent);						
					}
				});
				
				tv_hand.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(TabHomeActivity.this,ProductsMainActivity.class);
						intent.putExtra("categoryName", categoryName[6]);
						startActivity(intent);						
					}
				});
				
				tv_phone.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(TabHomeActivity.this,ProductsMainActivity.class);
						intent.putExtra("categoryName", categoryName[7]);
						startActivity(intent);						
					}
				});
				
				tv_sports.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(TabHomeActivity.this,ProductsMainActivity.class);
						intent.putExtra("categoryName", categoryName[8]);
						startActivity(intent);						
					}
				});
			}
		});		
	}

	@Override
	protected void onPause() {
		// avoid popupWindow leak
		super.onPause();
		if(pop.isShowing()){
			pop.dismiss();
		}
	}
}
