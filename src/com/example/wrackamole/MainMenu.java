package com.example.wrackamole;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainMenu extends Activity {
		protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		
		// Components
		final Button btStart = (Button)findViewById(R.id.bt_start);
		final Button btScore = (Button)findViewById(R.id.bt_score);
		final Button btLogout = (Button)findViewById(R.id.bt_logout);
		final TextView txtHi = (TextView)findViewById(R.id.txtHi);
		
		Intent i = getIntent();
	    // Receiving the data
	    final String username = i.getStringExtra("username");
	    txtHi.setText("Welcome, " + username);
	    
	    btStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
	    
	    btScore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent score = new Intent(
						MainMenu.this, Score.class);
				score.putExtra("username", username);

				startActivity(score);
            }
        });
	    
	    // Adding action to logout button
	    btLogout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Closing main menu and return to login
                finish();
            }
        });
	}

}
