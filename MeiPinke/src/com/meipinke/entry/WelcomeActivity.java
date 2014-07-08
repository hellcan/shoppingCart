package com.meipinke.entry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.example.meipinke.R;
import com.meipinke.database.mDatabase;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;

public class WelcomeActivity extends Activity {
	public static String dbName = "meipink.db";
	public static String DATABASE_PATH = "/data/data/com.example.meipinke/database/";
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_activity);
		
		//hide actionbar
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		//automatic transfer
		new Thread(new Runnable(){

			@Override
			public void run() {
				try{
					Thread.sleep(3000);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
				Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
//				Intent intent = new Intent(WelcomeActivity.this,DrawerMainActivity.class);

				startActivity(intent);
				finish();
			}			
		}).start();
		
		//database processing
		//check database
		boolean dbExist = checkDatabase();
		if(dbExist){
			
		}else{
			try{
				copyDatabase();
			}catch(IOException e){
				throw new Error("Error copying database");
			}
			
		}
		
		resetStatus();
		resetCart();
		
	}

	private void resetCart() {
		mDatabase mdb = new mDatabase();
		mdb.openDatabase();
		
		//reset stock
		String userName = mdb.checkShoppintCart("temp");
		if(userName!=null){
			List<String> nameList = mdb.getCartItemList("temp").get(1);
			List<String> stockList = mdb.getCartItemList("temp").get(2);
			for(int i =0; i< nameList.size(); i++){
				int stock = Integer.valueOf(mdb.getStock(nameList.get(i)));
				int tempStock = Integer.valueOf(stockList.get(i));
				int newQty = stock + tempStock;
				mdb.updateProductStock(newQty, nameList.get(i));
				//remove all temp cart
				mdb.resetCart();
			}
		}else{
			
		}
		
		
		

		mdb.closeDatabase();
		
	}

	private void resetStatus() {
		mDatabase mdb = new mDatabase();
		mdb.openDatabase();
		mdb.resetStatus();
		mdb.closeDatabase();
	}

	private boolean checkDatabase() {
		SQLiteDatabase checkDB = null;
		try{
			String databaseFileName = DATABASE_PATH + dbName;
			checkDB = SQLiteDatabase.openDatabase(databaseFileName, null, 
					SQLiteDatabase.OPEN_READONLY);
		}catch(SQLiteException e){
			
		}
		if(checkDB != null){
			checkDB.close();
		}
		return checkDB != null?  true : false;
	}

	private void copyDatabase() throws IOException {
		String databaseFilenames = DATABASE_PATH + dbName;
		File dir = new File(DATABASE_PATH);
		if(!dir.exists())
			dir.mkdir();//create file path if not
		
			FileOutputStream os = null;
			try{
				os = new FileOutputStream(databaseFilenames);//get database outputStream
			}catch(FileNotFoundException e){
				e.printStackTrace();
			}
			InputStream is = WelcomeActivity.this.getResources().
					openRawResource(R.raw.meipink); //get database file, which in raw folder
			byte[] buffer = new byte[8192];
			int count = 0;
			try{
				while((count = is.read(buffer))>0){
					os.write(buffer,0,count);
					os.flush();
				}
			}catch(IOException e){
					
			}
			try{
				is.close();
				os.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			
		}
	}

	


