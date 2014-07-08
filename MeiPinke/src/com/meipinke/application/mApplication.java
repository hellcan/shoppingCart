package com.meipinke.application;

public class mApplication {
	public static String none = "none";
	public static String productName;
	public static Boolean mainActivity = false;
	public static Boolean productsMainActivity = false;
	public static Boolean productsDetailActivity = false;
	
    public static void setProductName(String s){
    	productName = s;
    }
    
    public static String getProductName(){
    	return productName;
    }
    
    public static void setMainActBckTag(Boolean s){
    	mainActivity = s;
    }
    
    public static void setProductMainActBckTag(Boolean s){
    	productsMainActivity = s;
    }
    
    public static void setProductDetailActBckTag(Boolean s){
    	productsDetailActivity = s;
    }
    

    
    public static Boolean getMainActBckTag(){
    	return mainActivity;
    }
    
    public static Boolean getProductMainActBckTag(){
    	return productsMainActivity;
    }
    
    public static Boolean getProductDetailActBckTag(){
    	return productsDetailActivity;
    }
    
    
}
