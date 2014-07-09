package com.example.wrackamole;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Game extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		TextView greeting = (TextView)findViewById(R.id.tv_greeting);
		
		Intent i = getIntent();
		final String username = i.getStringExtra("username");
	    greeting.setText("Welcome, " + username);
	}
}
