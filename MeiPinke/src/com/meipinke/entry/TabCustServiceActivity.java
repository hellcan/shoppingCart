package com.meipinke.entry;

import com.example.meipinke.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TabCustServiceActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_custservice);
		
		final EditText edt_1 = (EditText)findViewById(R.id.cust_edt_1);
		final EditText edt_2 = (EditText)findViewById(R.id.cust_edt_2);
		final EditText edt_3 = (EditText)findViewById(R.id.cust_edt_3);
		Button btn_submit = (Button)findViewById(R.id.cust_submit);
		

		btn_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String a = edt_1.getText().toString();	
				String b = edt_2.getText().toString();
				String c = edt_3.getText().toString();
				if(a.equals("")||b.equals("")||c.equals("")){
					Toast.makeText(TabCustServiceActivity.this, "Please fill all the blank", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(TabCustServiceActivity.this, "Information received", Toast.LENGTH_SHORT).show();
					edt_1.setText("");
					edt_2.setText("");
					edt_3.setText("");
				}
			}
		});
	}

}
