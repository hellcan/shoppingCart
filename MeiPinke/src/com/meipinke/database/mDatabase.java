package com.meipinke.database;

import java.util.ArrayList;
import java.util.List;













import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class mDatabase {
	final String dbPath = "/data/data/com.example.meipinke/database/";
	final String dbName = "meipink.db";
	final String productsTable = "Products";
	final String accountsTable = "Accounts";
	final String shoppingCartsTable = "ShoppingCarts";
	final String wishListTable = "WishList";
	final String paymentTable = "Payment";
	SQLiteDatabase mdb = null;
	
	public mDatabase(){}

	public void openDatabase(){
		mdb = SQLiteDatabase.openDatabase(dbPath + dbName, null, SQLiteDatabase.OPEN_READWRITE);
	}
	
	public void closeDatabase(){
		mdb.close();
	}
	
	
	//check some account already sign in and get userName
	public String checkStatus(){
		String userName = null;
		String sql = "SELECT UserName FROM " + accountsTable + " WHERE Status = 1";
		Cursor result = mdb.rawQuery(sql, null);
		for(result.moveToFirst();!result.isAfterLast();result.moveToNext()){
			userName = result.getString(0);	
		}
		return userName;
	}
	
	//check shopping cart whether have item under temp
	public String checkShoppintCart(String userName){
		String productName = null;
		String sql = "SELECT ProductName FROM " + shoppingCartsTable + " WHERE UserName=" + "\"" + userName + "\"";
		Cursor result = mdb.rawQuery(sql, null);
		for(result.moveToFirst();!result.isAfterLast();result.moveToNext()){
			productName = result.getString(0);	
		}
		return productName;		
	}
	
	public String checkWishList(String userName) {
		String productName = null;
		String sql = "SELECT ProductName FROM " + wishListTable + " WHERE UserName=" + "\"" + userName + "\"";
		Cursor result = mdb.rawQuery(sql, null);
		for(result.moveToFirst();!result.isAfterLast();result.moveToNext()){
			productName = result.getString(0);	
		}
		return productName;
	}
	
	public String checkItemWished(String productName, String userName) {
		String s = null;
		String sql = "SELECT ProductName FROM " + wishListTable + " WHERE ProductName=" + "\"" + productName + "\"" + "AND UserName =" + "\"" + userName + "\"";
		Cursor result = mdb.rawQuery(sql, null);
		for(result.moveToFirst();!result.isAfterLast();result.moveToNext()){
			s = result.getString(0);	
		}
		return s;
	}
	
	//check whether item already exist in cart
	public String verifyItemInCart(String productName, String userName){
		String qty = null;
		String sql = "SELECT Stock FROM " + shoppingCartsTable + " WHERE ProductName =" + "\"" + productName + "\"" + "AND UserName =" + "\"" + userName + "\"";
		Cursor result = mdb.rawQuery(sql, null);
		for(result.moveToFirst();!result.isAfterLast();result.moveToNext()){
			qty = result.getString(0);	
		}
		return qty;
		
	}
	
	//get productList information
	public List<List<String>> getProducts(String categoryName){
		List<List<String>> productList = new ArrayList<List<String>>();
		List<String> productNameList = new ArrayList<String>();
		List<String> productPriceList = new ArrayList<String>();
		List<String> productImgList = new ArrayList<String>();
		String sql = "SELECT ProductName, Price, Img FROM " + productsTable + " WHERE Category=" + "\"" + categoryName + "\"";
		Cursor result = mdb.rawQuery(sql, null);
		for(result.moveToFirst();!result.isAfterLast();result.moveToNext()){
			productNameList.add(result.getString(0));
			productPriceList.add(result.getString(1));
			productImgList.add(result.getString(2));
		}
		productList.add(productNameList);
		productList.add(productPriceList);
		productList.add(productImgList);
		return productList;
	}
	
	//get 1 product detail
	public List<String> getDetail(String s){
		List<String> detailList = new ArrayList<String>();		
		String sql = "SELECT Img, ImgB, Model, Stock, Price, Description FROM " + productsTable + " WHERE ProductName=" + "\"" + s + "\"";
		Cursor result = mdb.rawQuery(sql, null);
		for(result.moveToFirst();!result.isAfterLast();result.moveToNext()){
			detailList.add(result.getString(0));//img small
			detailList.add(result.getString(1));//img big
			detailList.add(result.getString(2));//model
			detailList.add(result.getString(3));//stock
			detailList.add(result.getString(4));//price
			detailList.add(result.getString(5));//description
		}
		
		return detailList;	
	}
	
	//verify whether account already exist
	public String verifyAccountExist(String userName){
		String a = null ;
		String sql = "SELECT * FROM " + accountsTable + " WHERE UserName =" + "\"" + userName + "\"";
		Cursor result = mdb.rawQuery(sql, null);
		for(result.moveToFirst();!result.isAfterLast();result.moveToNext()){
			a = result.getString(1);
		}
		return a;
	}
	
	//verify password
	public int verifyPassword(String userName, String pwdInput){
		String pwdInDb = null;
		String sql = "SELECT Password FROM " + accountsTable + " WHERE UserName = " + "\"" + userName + "\"";
		Cursor result = mdb.rawQuery(sql, null);
		for(result.moveToFirst();!result.isAfterLast();result.moveToNext()){
			pwdInDb = result.getString(0);
		}
		
		//right
		if(pwdInDb.equals(pwdInput)){
			return 0;
		}else{//wrong
			return 1;
		}
		
	}
	
	//login account
	public void login(String userName){
		String sql = "UPDATE " + accountsTable + " SET Status = 1 WHERE UserName=" + "\"" + userName + "\"";
		mdb.execSQL(sql);
	}
	
	//sign out
	public void signOut(String userName) {
		// TODO Auto-generated method stub
		String sql = "UPDATE " + accountsTable + " SET Status = 0 WHERE UserName=" + "\"" + userName + "\"";
		mdb.execSQL(sql);
	}
	
	//create an account
	public void register (String userName, String pwd, String lastName, String firstName, 
			String address, String city, String state, String zip){
		
		ContentValues mContentValue = new ContentValues();
		mContentValue.put("UserName", userName);
		mContentValue.put("Password", pwd);
		mContentValue.put("LastName", lastName);
		mContentValue.put("FirstName", firstName);
		mContentValue.put("Address", address);
		mContentValue.put("City", city);
		mContentValue.put("State", state);
		mContentValue.put("Zip", zip);
		mContentValue.put("Status", "0");		
		this.mdb.insertOrThrow(accountsTable, null, mContentValue);
	}
	
	//add 1 item in shopping cart
	public void addCartItem(String imgS, String productName, String price, String qty, String userName){
		ContentValues mContentValue = new ContentValues();
		mContentValue.put("UserName", userName);
		mContentValue.put("ProductName", productName);
		mContentValue.put("Price", price);
		mContentValue.put("Stock", qty);
		mContentValue.put("Img", imgS);
		this.mdb.insertOrThrow(shoppingCartsTable, null, mContentValue);

	}
	
	//add 1 item in wish list
	public void addWishItem(String productImgS, String productName,
			String productPrice, String userName) {
		ContentValues mContentValue = new ContentValues();
		mContentValue.put("UserName", userName);
		mContentValue.put("ProductName", productName);
		mContentValue.put("Price", productPrice);
		mContentValue.put("Img", productImgS);
		this.mdb.insertOrThrow(wishListTable, null, mContentValue);
	}

    //sign out account
	public void resetStatus() {
		String sql = "UPDATE " + accountsTable + " SET Status = 0";
		mdb.execSQL(sql);
	}
	
	//update cart userName: temp ¡ú sign in userName
	public void updateCartUserName(String s) {
		String temp = "temp";
		String sql = "UPDATE " + shoppingCartsTable + " "
				+ "SET UserName =" + "\"" + s + "\"" + "WHERE UserName=" + "\"" + temp + "\"";
		mdb.execSQL(sql);
	}
	
	//update qty
	public void updateShoppingCartQty(int newQty, String productName, String userName) {
		String qty = String.valueOf(newQty);
		String sql = "UPDATE " + shoppingCartsTable + " "
				+ "SET Stock =" + "\"" + qty + "\"" + "WHERE ProductName=" + "\"" + productName + "\"" + "AND UserName =" + "\"" + userName + "\"";
		mdb.execSQL(sql);
	}
	
	
	//update product stock after item add
	public void updateProductStock(int afterAvail, String productName) {
		String qty = String.valueOf(afterAvail);
		String sql = "UPDATE " + productsTable + " SET Stock=" + "\"" + qty + "\"" + "WHERE ProductName=" + "\"" + productName + "\"";
		mdb.execSQL(sql);
	}
	
	//remove all the item under userName: temp in cart
	public void resetCart() {
		String temp = "temp";
		String sql = "DELETE FROM " + shoppingCartsTable + " WHERE UserName=" + "\"" + temp + "\"";
		mdb.execSQL(sql);
	}
	
	public void deleteCartItem(String productName, String userName){
		String sql = "DELETE FROM " + shoppingCartsTable + " WHERE ProductName=" + "\"" + productName + "\"" + "AND UserName =" + "\"" + userName + "\"";
		mdb.execSQL(sql);
	}

	//get total price
	public float getSubPrice(String s) {
		float subtotal = 0;
		List<String> priceList = new ArrayList<String>();
		String sql = "SELECT Stock*Price FROM " + shoppingCartsTable + " WHERE UserName =" + "\"" + s + "\"" ;
		Cursor result = mdb.rawQuery(sql, null);
		for(result.moveToFirst();!result.isAfterLast();result.moveToNext()){
			priceList.add(result.getString(0));
		}
		for(int i = 0; i < priceList.size(); i++){
			subtotal = subtotal + Float.valueOf(priceList.get(i));
		}
		subtotal =(float)(Math.round(subtotal*100))/100;
		return subtotal;
	}

	//get all the item in cart under specific userName
	public List<List<String>> getCartItemList(String userName) {
		List<List<String>> list = new ArrayList<List<String>>();
		List<String> imgList = new ArrayList<String>();
		List<String> nameList = new ArrayList<String>();
		List<String> stockList = new ArrayList<String>();
		List<String> priceList = new ArrayList<String>();
		String sql = "SELECT Img, ProductName, Stock, Price From " + shoppingCartsTable + " WHERE UserName =" + "\"" + userName + "\"";
		Cursor result = mdb.rawQuery(sql, null);
		for(result.moveToFirst();!result.isAfterLast();result.moveToNext()){
			imgList.add(result.getString(0));
			nameList.add(result.getString(1));
			stockList.add(result.getString(2));
			priceList.add(result.getString(3));
		}
		list.add(imgList);
		list.add(nameList);
		list.add(stockList);
		list.add(priceList);
		return list;
	}

	//get all the item in wishList under specific UserName
	public List<List<String>> getWishList(String UserName) {
		List<List<String>> list = new ArrayList<List<String>>();
		List<String> imgList = new ArrayList<String>();
		List<String> nameList = new ArrayList<String>();
		List<String> priceList = new ArrayList<String>();
		String sql = "SELECT Img, ProductName, Price From " + wishListTable + " WHERE UserName =" + "\"" + UserName + "\"";
		Cursor result = mdb.rawQuery(sql, null);
		for(result.moveToFirst();!result.isAfterLast();result.moveToNext()){
			imgList.add(result.getString(0));
			nameList.add(result.getString(1));
			priceList.add(result.getString(2));
		}
		list.add(imgList);
		list.add(nameList);
		list.add(priceList);
		return list;
	}

	public String getStock(String productName) {
		String stock = null;
		String sql = "SELECT Stock FROM " + productsTable + " WHERE ProductName=" + "\"" + productName + "\"";
		Cursor result = mdb.rawQuery(sql, null);
		for(result.moveToFirst();!result.isAfterLast();result.moveToNext()){
			stock = result.getString(0);
		}

		return stock;
	}

	public String getOriginStock(String productName) {
		String originStock = null;
		String sql = "SELECT OriginStock FROM " + productsTable + " WHERE ProductName=" + "\"" + productName + "\"";
		Cursor result = mdb.rawQuery(sql, null);
		for(result.moveToFirst();!result.isAfterLast();result.moveToNext()){
			originStock = result.getString(0);
		}

		return originStock;		
	}

	public void deleteWishItem(String name, String userName) {
		String sql = "DELETE FROM " + wishListTable + " WHERE ProductName=" + "\"" + name + "\"" + "AND UserName =" + "\"" + userName + "\"";
		mdb.execSQL(sql);		
	}

	public String getCategory(String productName) {
		String categoryName = null; 
		String sql = "SELECT Category FROM " + productsTable + " WHERE ProductName=" + "\"" + productName + "\"";
		Cursor result = mdb.rawQuery(sql, null);
		for(result.moveToFirst();!result.isAfterLast();result.moveToNext()){
			categoryName = result.getString(0);
		}
		return categoryName;
	}
	
	public String getFirstName(String userName){
		String name = null;
		String sql = "SELECT FirstName FROM " + accountsTable + " WHERE UserName=" + "\"" + userName + "\"";
		Cursor result = mdb.rawQuery(sql, null);
		for(result.moveToFirst();!result.isAfterLast();result.moveToNext()){
			name = result.getString(0);
		}
		return name;
		
	}

	public List<List<String>> getSearchResult(String s) {
		// TODO Auto-generated method stub
		List<List<String>> reusltList = new ArrayList<List<String>>();
		List<String> productNameList = new ArrayList<String>();
		List<String> productPriceList = new ArrayList<String>();
		List<String> productImgList = new ArrayList<String>();
		String sql = "SELECT ProductName, Price, Img FROM " + productsTable + " WHERE ProductName LIKE" + "'%" + s + "%'";
		Cursor result = mdb.rawQuery(sql, null);
		for(result.moveToFirst();!result.isAfterLast();result.moveToNext()){
			productNameList.add(result.getString(0));
			productPriceList.add(result.getString(1));
			productImgList.add(result.getString(2));
		}

		reusltList.add(productNameList);
		reusltList.add(productPriceList);
		reusltList.add(productImgList);
		return reusltList;
		
	}

	public List<String> getCardInfo(String userName) {
		// TODO Auto-generated method stub
		List<String> cardNum = new ArrayList<String>();
		String sql = "SELECT CardNumber FROM " + paymentTable + " WHERE UserName = " + "\"" + userName + "\"";
		Cursor result = mdb.rawQuery(sql, null);
		for(result.moveToFirst();!result.isAfterLast();result.moveToNext()){
			cardNum.add(result.getString(0));
		}
		return cardNum;
	}

	public void insertCard(String fstName, String lstName, String cardNum,
			String year, String month, String sCode, String userName) {
		// TODO Auto-generated method stub
		ContentValues mContentValue = new ContentValues();
		mContentValue.put("FirstName", fstName);
		mContentValue.put("LastName", lstName);
		mContentValue.put("UserName", userName);
		mContentValue.put("CardNumber", cardNum);
		mContentValue.put("Month", month);
		mContentValue.put("Year	", year);
		mContentValue.put("SecurityCode", sCode);
		this.mdb.insertOrThrow(paymentTable, null, mContentValue);
	}



	



	



	

	

}
