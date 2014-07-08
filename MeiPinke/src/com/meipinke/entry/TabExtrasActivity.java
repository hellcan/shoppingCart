package com.meipinke.entry;

import com.example.meipinke.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class TabExtrasActivity extends Activity {
	String  s;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_extras);
		
		final EditText edt_1 = (EditText)findViewById(R.id.ex_edt_1);
		final EditText edt_2 = (EditText)findViewById(R.id.ex_edt_2);
		final EditText edt_3 = (EditText)findViewById(R.id.ex_edt_3);
		final EditText edt_4 = (EditText)findViewById(R.id.ex_edt_4);
		final EditText edt_5 = (EditText)findViewById(R.id.ex_edt_5);
		final EditText edt_6 = (EditText)findViewById(R.id.ex_edt_6);
		final EditText edt_7 = (EditText)findViewById(R.id.ex_edt_7);

		Button btn = (Button)findViewById(R.id.ex_submit);
		
        RadioGroup group = (RadioGroup)this.findViewById(R.id.rbtn_group);
        
        group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				int radioButtonId = group.getCheckedRadioButtonId();
				RadioButton rb = (RadioButton)findViewById(radioButtonId);
				s = rb.getText().toString();			
			}
		});
		
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(edt_1.getText().toString().equals("")||edt_2.getText().toString().equals("")||
						edt_3.getText().toString().equals("")||edt_4.getText().toString().equals("")
						||edt_5.getText().toString().equals("")||edt_7.getText().toString().equals("")){
					
					Toast.makeText(TabExtrasActivity.this, "Please fill all the blank", Toast.LENGTH_SHORT).show();				
				}else{
					Toast.makeText(TabExtrasActivity.this, "Request received", Toast.LENGTH_SHORT).show();		
					edt_1.setText("");
					edt_2.setText("");
					edt_3.setText("");
					edt_4.setText("");
					edt_5.setText("");
					edt_6.setText("");
					edt_7.setText("");
				}
			}
		});

	}

}
