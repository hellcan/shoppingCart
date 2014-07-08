package com.meipinke.entry;

import com.example.meipinke.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TabInfoActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_information);
		
		TextView tv = (TextView)findViewById(R.id.info_content);
		tv.setText("MEIPink is an online retailer based in New York City, that sells a broad "
				+ "range of products including Computers, Electronics, Clothing, home fashion "
				+ "products, beauty products, accessories, watches, phones, outdoor & sports "
				+ "products, etc. Meipinke.com is an online platform dedicated to selling high "
				+ "end branded products from USA and around the world which offers fashion and "
				+ "high quality items. We also support small online retail businesses across "
				+ "the United States by providing them a national customer database and we "
				+ "provide smart database solutions and analytics for customer behaviors. "
				+ "A recent Nielsen State of the Media: Consumer Usage Report placed meipinke.com "
				+ "among the top 30 most visited mass merchandiser websites. We also own a high quality "
				+ "logistics team which provide on time delivery of our merchandise to customers all over "
				+ "the world. MEIPinke.com (http://www.meipinke.com) regularly posts information about the "
				+ "company and other related matters under Investor Relations on its website.");


	}

}
