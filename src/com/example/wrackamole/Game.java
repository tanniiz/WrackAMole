package com.example.wrackamole;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import java.util.ArrayList;
import java.util.Collections;

public class Game extends Activity {
	String sequence;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		
		TextView greeting = (TextView)findViewById(R.id.tv_greeting);
		
		Intent i = getIntent();
		final String username = i.getStringExtra("username");
	    greeting.setText("Welcome, " + username);
		// Components
		final Button btSignOut = (Button) findViewById(R.id.bt_signOut);
		final Button btPausePlay = (Button) findViewById(R.id.bt_pausePlay);

		// Receiving the data

		// Adding action to logout button

	}
	
	public void genSequence(){
		ArrayList<Integer> temp = new ArrayList<Integer>(){{
			add(1);
			add(2);
			add(3);
			add(4);
			add(5);
			add(6);
			add(7);
			add(8);
			add(9);
		}};
		Collections.shuffle(temp);
		sequence = temp.toString();
	}
	
	public boolean shuffleBoard(){
		
		return true;
	}
}
