package com.example.wrackamole;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SelectMode extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_mode);
		
		Button btEasy = (Button)findViewById(R.id.bt_easy);
		Button btHard = (Button)findViewById(R.id.bt_hard);
		Button btBack = (Button)findViewById(R.id.im_table);
		TextView tv = (TextView)findViewById(R.id.tv);
		
		tv.setTextSize(30.0f);
		tv.setTypeface(null, Typeface.BOLD);
		
		Intent i = getIntent();
	    // Receiving the data
	    final String username = i.getStringExtra("username");
	    
	    btEasy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent GAMEPLAY = new Intent(
						SelectMode.this, Game.class);
				GAMEPLAY.putExtra("username", username);
				GAMEPLAY.putExtra("level", 0);

				startActivity(GAMEPLAY);
            }
        });
	    
	    btHard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent GAMEPLAY = new Intent(
						SelectMode.this, Game.class);
				GAMEPLAY.putExtra("username", username);
				GAMEPLAY.putExtra("level", 1);

				startActivity(GAMEPLAY);
            }
        });
	    
		btBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
	
	}
}
