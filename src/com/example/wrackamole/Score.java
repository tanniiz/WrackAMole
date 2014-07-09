package com.example.wrackamole;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Score extends Activity{
	private MySQLiteHelper db;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_score);
		
		db = new MySQLiteHelper(this);
		final TextView tvBar = (TextView)findViewById(R.id.tv_sc);
		final TextView[] tvScore = new TextView[5];
		tvScore[0] = (TextView)findViewById(R.id.tv_score1);
		tvScore[1] = (TextView)findViewById(R.id.tv_score2);
		tvScore[2] = (TextView)findViewById(R.id.tv_score3);
		tvScore[3] = (TextView)findViewById(R.id.tv_score4);
		tvScore[4] = (TextView)findViewById(R.id.tv_score5);
		final Button btMenu = (Button)findViewById(R.id.bt_menu);
		Intent i = getIntent();
	    // Receiving the data
	    String username = i.getStringExtra("username");
	    tvBar.setText(username + "'s Score:");
	    
		
		List<User> sc = db.getScore(username);
		for(int j = 0 ; j < sc.size() && j < 5 ; j++) {
			tvScore[j].setText(sc.get(j).getUsername() + " " + sc.get(j).getScore());
		}

		//} else {
			//tvScore1.setText("NULL");
		//}
		
		btMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
	}
}