package com.example.wrackamole;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;

public class Game extends Activity {

	MySQLiteHelper db;

	// Variable
	CountDownTimer cdt;
	String username;
	TextView greeting;
	TextView tvTime;
	TextView tvScore;
	TextView tvSequence;
	Button btSignOut;
	Button btPause;
	TableLayout im_table;
	TableLayout lg_table;
	int[][] targetLocation = new int[9][2];
	int duration;
	long time = 30000;
	int level = 0;
	int pos = 0;
	int score = 0;
	Calendar calendar = Calendar.getInstance();
	SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	String startTime;
	String stopTime;
	// USE THIS TO CHECK FOR LOCATION JUST ONCE
	// SHOULD BE FIX AFTER THIS
	boolean flag = false;

	ArrayList<float[]> pointing = new ArrayList<float[]>();

	ArrayList<ImageView> im_list = new ArrayList<ImageView>();

	// INSERT COLOR
	// Integer[] legends = new Integer[] { Color.BLACK, Color.BLUE, Color.GREEN,
	// Color.MAGENTA, Color.RED, Color.YELLOW, Color.LTGRAY, Color.GRAY,
	// Color.CYAN };

	// INSERT PICTURE HERE
	Integer[] drawable = new Integer[] { R.drawable.mole1, R.drawable.mole2,
			R.drawable.mole3, R.drawable.mole4, R.drawable.mole5,
			R.drawable.mole6, R.drawable.mole7, R.drawable.mole8,
			R.drawable.mole9 };

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

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		// Shuffle Color
		Collections.shuffle(Arrays.asList(drawable));

		// Set Margins
		bo_params.setMargins(50, 50, 50, 50);
		// bo_params.width = 50;
		// bo_params.height = 50;
		lg_params.setMargins(20, 0, 20, 0);
		lg_params.width = 50;
		lg_params.height = 50;

		// Greeting on top
		Intent i = getIntent();
		username = i.getStringExtra("username");
		level = i.getIntExtra("level", -1);

		intro();

		this.db = new MySQLiteHelper(this);
		// Refer xml components
		greeting = (TextView) findViewById(R.id.tv_name);
		tvTime = (TextView) findViewById(R.id.tv_time);
		tvScore = (TextView) findViewById(R.id.tv_score);
		tvSequence = (TextView) findViewById(R.id.tv_sequence);
		btSignOut = (Button) findViewById(R.id.bt_signOut);
		btSignOut.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				im_table.setVisibility(View.INVISIBLE);
				tvSequence.setVisibility(View.INVISIBLE);
				lg_table.setVisibility(View.INVISIBLE);
				cdt.cancel();
				AlertDialog alertDialog = new AlertDialog.Builder(Game.this)
						.create();
				alertDialog.setMessage("Are you sure?");
				alertDialog.setButton2("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								Intent login = new Intent(Game.this,
										MainActivity.class);
								// Closing main menu and return to login
								startActivity(login);
								finish();
							}
						});

				alertDialog.setButton("No",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								im_table.setVisibility(View.VISIBLE);
								tvSequence.setVisibility(View.VISIBLE);
								lg_table.setVisibility(View.VISIBLE);
								startTimer();
							}
						});
				alertDialog.setCanceledOnTouchOutside(false);
				alertDialog.show();

			}
		});
		btPause = (Button) findViewById(R.id.bt_playPause);
		btPause.setOnClickListener(new View.OnClickListener() {
			// PausePlay button listener
			@Override
			public void onClick(View v) {
				pause();
			}
		});
		im_table = (TableLayout) findViewById(R.id.im_table);
		lg_table = (TableLayout) findViewById(R.id.lg_table);

	}

	public void startTimer() {
		// Timer
		cdt = new CountDownTimer(time, 1000) {

			public void onTick(long millisUntilFinished) {
				tvTime.setText("Time Remaining: " + millisUntilFinished
						/ 1000);
				duration = 30 - (int) millisUntilFinished / 1000;
				time = millisUntilFinished;
				if (millisUntilFinished < 30000 && !flag) {
					flag = true;
					getTargetLocation();
				}
			}

			public void onFinish() {

				tvTime.setText("seconds remaining: 0");
				duration++;
				end();
			}
		}.start();
	}

	// Generate Sequence
	public void shuffleSequence() {
		Collections.shuffle(sequence);
	}

	// Shuffle the board
	public void shuffleBoard() {
		im_table.removeAllViews();
		im_list.clear();

		for (int z = 0; z < 9; z++) {
			ImageView temp = new ImageView(this);

			// USE THIS STATEMENT IF WANT TO USE PICTURE
			temp.setMinimumHeight(100);
			temp.setMinimumWidth(100);
			// temp.setBackgroundColor(legends[z]);
			temp.setImageResource(drawable[z]);

			temp.setTag(z + 1);
			temp.setId(z + 1);
			temp.setLayoutParams(bo_params);
			temp.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN: {
						onTouchEvent(event);
						if (isCorrect(v)) {
							v.setBackgroundColor(Color.GREEN);
						} else {
							v.setBackgroundColor(Color.RED);
						}

						break;
					}
					case MotionEvent.ACTION_UP: {
						v.setBackgroundColor(Color.TRANSPARENT);
						break;
					}
					}

					return true;

				}
			});
			temp.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					isCorrect(v);
				}
			});
			im_list.add(temp);
		}
		Collections.shuffle(im_list);
		for (int i = 0; i < 3; i++) {
			TableRow temp = new TableRow(this);
			for (int y = 0; y < 3; y++) {
				temp.addView(im_list.get((y) + (i * 3)));
			}
			im_table.addView(temp);
			im_table.startAnimation(new AnimationUtils().loadAnimation(this,
					R.anim.abc_fade_in));
		}
	}

	// Check input
	public boolean isCorrect(View v) {
		ImageView pressed = (ImageView) v;
		// Correct
		if (pressed.getId() == sequence.get(pos)) {
			pos++;
			score += 1;
			tvScore.setText("Score : " + score);
			if (level == 1) {
				shuffleBoard();
			}
			// End Game
			if (pos == 9) {
				cdt.cancel();
				end();
			}
			return true;
		} else {
			// Incorrect
			score -= 1;
			tvScore.setText("Score : " + score);
			return false;
		}

	}

	// End
	public void end() {
		// Dialog properties

		// Get stop Time
		stopTime = dateFormat.format(calendar.getTime());
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

		long i = db.resultRec(username, score, level, duration,
				sequence.toString(), startTime, stopTime);

		if (i != -1) {
			db.posRec(i, pointing);
			Toast.makeText(Game.this, "Your score has been recorded: " + i,
					Toast.LENGTH_LONG).show();

		} else {
			Toast.makeText(Game.this,
					"Problem occured while trying to record your score",
					Toast.LENGTH_LONG).show();
		}

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
		alertDialogBuilder.setCancelable(false);
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

		// Get current time

		// Dismiss dialog when click cancel
		alertDialogBuilder.setNegativeButton("OKAY",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// Put starter here
						// Shuffle sequence
						shuffleSequence();

						// Get Current Time
						startTime = dateFormat.format(calendar.getTime());
						// Show board
						shuffleBoard();

						// Show Username
						greeting.setText("Welcome, " + username);

						// setTime tv
						tvTime.setText("Time Remaining : " + time);

						// setScore tv
						tvScore.setText("Score : " + score);

						// Show Sequence
						tvSequence.setText(sequence.toString());

						// Show Legends
						showLegends();

						// Start timer
						startTimer();

						dialog.cancel();
					}
				});
		alertDialogBuilder.setCancelable(false);
		alertDialogBuilder.show();

	}

	public void showLegends() {
		TableRow numRow = new TableRow(this);
		TableRow colorRow = new TableRow(this);
		for (int z = 0; z < 9; z++) {
			ImageView temp = new ImageView(this);
			temp.setMinimumHeight(50);
			temp.setMinimumWidth(50);
			// temp.setBackgroundColor(legends[z]);
			temp.setImageResource(drawable[z]);
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

	// On touch
	public boolean onTouchEvent(MotionEvent event) {
		// Calculate the distance from user input to the center of the target
		// Math.sqrt((y2-y1) + (x2-x1))
		// loop im_list , seek for the current button that == to sequence pos
		// if yes get it's x y from targetlocation[][], calculate distance
		float targetX = 0;
		float targetY = 0;
		float userX = 0;
		float userY = 0;

		float distance = 0;

		for (int i = 0; i < im_list.size(); i++) {
			if (im_list.get(i).getId() == sequence.get(pos)) {
				targetX = targetLocation[i][0];
				targetY = targetLocation[i][1];

				userX = event.getRawX();
				userY = event.getRawY();

				distance = (float) Math.sqrt(Math.pow(targetY - userY, 2)
						+ Math.pow(targetX - userX, 2));
			}
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// FLOAT[TARGET X, TARGET Y, USERINPUT X, USERINPUT Y, PRESSURE,
			// SIZE, DISTANCE]
			pointing.add(new float[] { targetX, targetY, userX, userY,
					event.getPressure(), event.getSize(), distance });

			// DEBUGGING
			// Toast.makeText(
			// this,
			// "X is " + userX + "Y is " + userY + "Pressure is "
			// + event.getPressure() + "Size is "
			// + event.getSize() + "\n Distance Is : " + distance,
			// Toast.LENGTH_LONG).show();

			// case MotionEvent.ACTION_MOVE:
			// case MotionEvent.ACTION_UP:
		}
		return false;
	}

	// On back
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			cdt.cancel();
		}
		return super.onKeyDown(keyCode, event);
	}

	// Get target position
	public void getTargetLocation() {
		for (int i = 0; i < 9; i++) {
			im_list.get(i).getLocationOnScreen(targetLocation[i]);
		}
	}

	// Pasue and play method
	// Make new CountDownTimer everytime with time remaining
	public void pause() {

		im_table.setVisibility(View.INVISIBLE);
		tvSequence.setVisibility(View.INVISIBLE);
		lg_table.setVisibility(View.INVISIBLE);
		cdt.cancel();
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setMessage("GAME IS PAUSE");
		alertDialog.setButton("Continue",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						im_table.setVisibility(View.VISIBLE);
						tvSequence.setVisibility(View.VISIBLE);
						lg_table.setVisibility(View.VISIBLE);
						startTimer();
					}
				});
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();

	}
}
