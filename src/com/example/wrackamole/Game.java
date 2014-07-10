 package com.example.wrackamole;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

;

public class Game extends Activity {
	String sequence;
	int time = 5;
	int score = 0;
	// Timer
	Timer timer = new Timer();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		genSequence();
		// Greeting on top
		TextView greeting = (TextView) findViewById(R.id.tv_name);
		Intent i = getIntent();
		final String username = i.getStringExtra("username");
		greeting.setText("Welcome, " + username);

		// Components
		final TextView tvTime = (TextView) findViewById(R.id.tv_time);
		tvTime.setText(time);
		final TextView tvScore = (TextView) findViewById(R.id.tv_score);
		tvScore.setText(score);
		final TextView tvSequence = (TextView) findViewById(R.id.tv_sequence);
		tvSequence.setText(sequence);
		final Button btSignOut = (Button) findViewById(R.id.bt_signOut);
		final Button btplayPause = (Button) findViewById(R.id.bt_playPause);
		final Button bt1 = (Button) findViewById(R.id.bt_bt1);
		final Button bt2 = (Button) findViewById(R.id.bt_bt2);
		final Button bt3 = (Button) findViewById(R.id.bt_bt3);
		final Button bt4 = (Button) findViewById(R.id.bt_bt4);
		final Button bt5 = (Button) findViewById(R.id.bt_bt5);
		final Button bt6 = (Button) findViewById(R.id.bt_bt6);
		final Button bt7 = (Button) findViewById(R.id.bt_bt7);
		final Button bt8 = (Button) findViewById(R.id.bt_bt8);
		final Button bt9 = (Button) findViewById(R.id.bt_bt9);
		// Receiving the data

		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				tvTime.setText(time);
				time--;
			}

		}, 2 * 60 * 1000);
	}

	// Generate Sequence
	public void genSequence() {
		ArrayList<Integer> temp = new ArrayList<Integer>() {
			{
				add(1);
				add(2);
				add(3);
				add(4);
				add(5);
				add(6);
				add(7);
				add(8);
				add(9);
			}
		};
		Collections.shuffle(temp);
		sequence = temp.toString();
	}

	// Shuffle the digit symbol
	public boolean shuffleBoard() {
		
		return true;
	}

	public boolean onClickCheck() {
		  Toast.makeText(this, "Clicked on Button", Toast.LENGTH_LONG).show();
		return true;
	}

}
