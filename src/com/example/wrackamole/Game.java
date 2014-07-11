package com.example.wrackamole;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Game extends Activity {

	MySQLiteHelper db;

	// Components
	CountDownTimer cdt;
	String username;
	TextView greeting;
	TextView tvTime;
	TextView tvScore;
	TextView tvSequence;
	Button btSignOut;
	Button btplayPause;
	TableLayout im_table;
	TableLayout lg_table;
	int duration;

	ArrayList<ImageButton> im_list = new ArrayList<ImageButton>();

	Integer[] legends = new Integer[] { Color.BLACK, Color.BLUE, Color.GREEN,
			Color.MAGENTA, Color.RED, Color.YELLOW, Color.LTGRAY, Color.GRAY,
			Color.CYAN };
	int[] drawable = new int[] { R.drawable.wrack1, R.drawable.wrack2,
			R.drawable.wrack3, R.drawable.wrack4, R.drawable.wrack5,
			R.drawable.wrack6, R.drawable.wrack7, R.drawable.wrack8,
			R.drawable.wrack9 };

	TableRow.LayoutParams bo_params = new TableRow.LayoutParams();
	TableRow.LayoutParams lg_params = new TableRow.LayoutParams();

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
	int level = 0;
	int pos = 0;
	int time = 5;
	int score = 0;

	// OnClickPrivate
	private View.OnClickListener imgButtonOnClicklistener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// Toast.makeText(Game.this, v.getId() + "",
			// Toast.LENGTH_LONG).show();

			switch (v.getId()) {
			case 1:
				isCorrect(1);
				break;
			case 2:
				isCorrect(2);
				break;
			case 3:
				isCorrect(3);
				break;
			case 4:
				isCorrect(4);
				break;
			case 5:
				isCorrect(5);
				break;
			case 6:
				isCorrect(6);
				break;
			case 7:
				isCorrect(7);
				break;
			case 8:
				isCorrect(8);
				break;
			case 9:
				isCorrect(9);
			}

		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		// Shuffle Color
		Collections.shuffle(Arrays.asList(legends));

		// Set Margins
		bo_params.setMargins(50, 50, 50, 50);
		lg_params.setMargins(20, 0, 20, 0);

		// Greeting on top
		Intent i = getIntent();
		username = i.getStringExtra("username");
		level = i.getIntExtra("level", -1);

		intro();

		this.db = new MySQLiteHelper(this);
		// Refer xml components
		greeting = (TextView) findViewById(R.id.tv_name);
		greeting.setText("Welcome, " + username);
		tvTime = (TextView) findViewById(R.id.tv_time);
		tvScore = (TextView) findViewById(R.id.tv_score);
		tvSequence = (TextView) findViewById(R.id.tv_sequence);
		btSignOut = (Button) findViewById(R.id.bt_signOut);
		btplayPause = (Button) findViewById(R.id.bt_playPause);
		im_table = (TableLayout) findViewById(R.id.im_table);
		lg_table = (TableLayout) findViewById(R.id.lg_table);

		// create imageButton

		shuffleSequence();
		shuffleBoard();

		// setTime tv
		tvTime.setText("Time Remaining : " + time);

		// setScore tv
		tvScore.setText("Score : " + score);

		// Show Sequence
		tvSequence.setText(sequence.toString());

		// Show Legends
		showLegends();
		// Timer
		cdt = new CountDownTimer(30000, 1000) {

			public void onTick(long millisUntilFinished) {
				tvTime.setText("seconds remaining: " + millisUntilFinished
						/ 1000);
				duration = 30 - (int) millisUntilFinished / 1000;
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
	public void shuffleBoard() {
		im_table.removeAllViews();
		im_list.clear();

		for (int z = 0; z < 9; z++) {
			ImageButton temp = new ImageButton(this);
			// temp.setImageResource(drawable[z]);
			temp.setMinimumHeight(100);
			temp.setMinimumWidth(100);
			temp.setBackgroundColor(legends[z]);
			temp.setTag(z + 1);
			temp.setId(z + 1);
			temp.setLayoutParams(bo_params);
			temp.setOnClickListener(imgButtonOnClicklistener);
			im_list.add(temp);
		}
		Collections.shuffle(im_list);
		for (int i = 0; i < 3; i++) {
			TableRow temp = new TableRow(this);
			for (int y = 0; y < 3; y++) {
				temp.addView(im_list.get((y) + (i * 3)));
			}
			im_table.addView(temp);
		}

	}

	// Check input
	public boolean isCorrect(int in) {
		if (in == sequence.get(pos)) {
			pos++;
			score += 1;
			tvScore.setText("Score : " + score);
			shuffleBoard();
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
		long i = db.ressultRec(username, score, level, duration,
				sequence.toString());
		if (i != -1) {
			Toast.makeText(Game.this, "Your score has been recorded",
					Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(Game.this,
					"Problem occured while trying to record your score",
					Toast.LENGTH_LONG).show();
		}
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

		tvDuration.setText("Duration : " + duration);
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

	public void intro() {
		// Dialog properties
		LayoutInflater li = LayoutInflater.from(this);
		View prompt = li.inflate(R.layout.instruction, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setView(prompt);
		final TextView tvUsername = (TextView) prompt
				.findViewById(R.id.tv_instruction);
		tvUsername.setText("Hi, " + username
				+ "\n This is an instruction .....");
		alertDialogBuilder.setTitle("Instruction");

		// Dismiss dialog when click cancel
		alertDialogBuilder.setNegativeButton("OKAY",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		alertDialogBuilder.show();

	}

	public void showLegends() {
		TableRow numRow = new TableRow(this);
		TableRow colorRow = new TableRow(this);
		for (int z = 0; z < 9; z++) {
			ImageButton temp = new ImageButton(this);
			temp.setMinimumHeight(50);
			temp.setMinimumWidth(50);
			temp.setBackgroundColor(legends[z]);
			temp.setTag(z + 11);
			temp.setId(z + 11);
			temp.setLayoutParams(lg_params);
			colorRow.addView(temp);
		}
		for (int z = 0; z < 9; z++) {
			TextView temp = new TextView(this);
			temp.setTypeface(null, Typeface.BOLD);
			temp.setTextSize(30);
			temp.setMinimumHeight(50);
			temp.setMinimumWidth(50);
			temp.setLayoutParams(lg_params);
			temp.setGravity(Gravity.CENTER);
			temp.setText((z + 1) + "");
			numRow.addView(temp);
		}

		lg_table.addView(colorRow);
		lg_table.addView(numRow);
	}

}
