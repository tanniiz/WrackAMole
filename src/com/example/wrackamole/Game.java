package com.example.wrackamole;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class Game extends Activity {

	// Components
	CountDownTimer cdt;
	String username;
	TextView greeting;
	TextView tvTime;
	TextView tvScore;
	TextView tvSequence;
	Button btSignOut;
	Button btplayPause;

	// Imagebutton (s)
	ImageButton ibt1;
	ImageButton ibt2;
	ImageButton ibt3;
	ImageButton ibt4;
	ImageButton ibt5;
	ImageButton ibt6;
	ImageButton ibt7;
	ImageButton ibt8;
	ImageButton ibt9;

	ArrayList<Integer> sequence = new ArrayList<Integer>() {
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
	int pos = 0;
	int time = 5;
	int score = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		// Refer xml components
		greeting = (TextView) findViewById(R.id.tv_name);
		tvTime = (TextView) findViewById(R.id.tv_time);
		tvScore = (TextView) findViewById(R.id.tv_score);
		tvSequence = (TextView) findViewById(R.id.tv_sequence);
		btSignOut = (Button) findViewById(R.id.bt_signOut);
		btplayPause = (Button) findViewById(R.id.bt_playPause);
		ibt1 = (ImageButton) findViewById(R.id.ibt1);
		ibt2 = (ImageButton) findViewById(R.id.ibt2);
		ibt3 = (ImageButton) findViewById(R.id.ibt3);
		ibt4 = (ImageButton) findViewById(R.id.ibt4);
		ibt5 = (ImageButton) findViewById(R.id.ibt5);
		ibt6 = (ImageButton) findViewById(R.id.ibt6);
		ibt7 = (ImageButton) findViewById(R.id.ibt7);
		ibt8 = (ImageButton) findViewById(R.id.ibt8);
		ibt9 = (ImageButton) findViewById(R.id.ibt9);

		shuffleSequence();
		// Greeting on top
		Intent i = getIntent();
		username = i.getStringExtra("username");
		greeting.setText("Welcome, " + username);

		// setTime tv
		tvTime.setText("Time Remaining : " + time);

		// setScore tv
		tvScore.setText("Score : " + score);

		// Show Sequence
		tvSequence.setText(sequence.toString());

		// Timer
		cdt = new CountDownTimer(30000, 1000) {

			public void onTick(long millisUntilFinished) {
				tvTime.setText("seconds remaining: " + millisUntilFinished
						/ 1000);
			}

			public void onFinish() {
				end();
			}
		}.start();

	}

	// Generate Sequence
	public void shuffleSequence() {
		Collections.shuffle(sequence);
	}

	// Shuffle the digit symbol
	public boolean shuffleBoard() {

		return true;
	}

	// Button's action listener
	public void onClickCheck(View view) {
		switch (view.getId()) {
		case R.id.ibt1:
			isCorrect(1);
			break;
		case R.id.ibt2:
			isCorrect(2);
			break;
		case R.id.ibt3:
			isCorrect(3);
			break;
		case R.id.ibt4:
			isCorrect(4);
			break;
		case R.id.ibt5:
			isCorrect(5);
			break;
		case R.id.ibt6:
			isCorrect(6);
			break;
		case R.id.ibt7:
			isCorrect(7);
			break;
		case R.id.ibt8:
			isCorrect(8);
			break;
		case R.id.ibt9:
			isCorrect(9);
		}

	}

	// Check input
	public boolean isCorrect(int in) {
		if (in == sequence.get(pos)) {
			pos++;
			score += 1;
			tvScore.setText("Score : " + score);
			if (pos == 9) {
				cdt.cancel();
				end();
			}
			return true;
		} else {
			score -= 1;
			tvScore.setText("Score : " + score);
			return false;
		}

	}

	// End
	public void end() {
		// Dialog properties
		LayoutInflater li = LayoutInflater.from(this);
		View prompt = li.inflate(R.layout.result, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setView(prompt);
		final TextView tvUsername = (TextView) prompt
				.findViewById(R.id.tvUsername);
		tvUsername.setText(username);
		final TextView tvRScore = (TextView) prompt.findViewById(R.id.tvScore);
		tvRScore.setText("Score : " + score);
		final TextView tvDuration = (TextView) prompt
				.findViewById(R.id.tvDuration);
		tvDuration.setText("Duration : Deaw kon na ja");
		alertDialogBuilder.setTitle("Result");

		// Dismiss dialog when click cancel
		alertDialogBuilder.setNegativeButton("OKAY",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						Intent score = new Intent(Game.this, MainMenu.class);
						score.putExtra("username", username);

						startActivity(score);
					}
				});

		alertDialogBuilder.show();
	}

}
