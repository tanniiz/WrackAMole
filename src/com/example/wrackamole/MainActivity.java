package com.example.wrackamole;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	private MySQLiteHelper db;
	private Button btLogin, btReg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Components
		final EditText etUsername = (EditText) findViewById(R.id.et_username);
		btLogin = (Button) findViewById(R.id.bt_login);
		btReg = (Button) findViewById(R.id.bt_reg);
		db = new MySQLiteHelper(this);

		// Adding action to login button
		btLogin.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				in.hideSoftInputFromWindow(etUsername.getWindowToken(), 0);
				String username = etUsername.getText().toString();
				if (username.length() > 0) {
					try {
						if (db.login(username)) {
							Intent MAIN_MENU = new Intent(
									getApplicationContext(), MainMenu.class);
							MAIN_MENU.putExtra("username", username);

							startActivity(MAIN_MENU);
						} else {
							Toast.makeText(MainActivity.this,
									"Invalid Username!", Toast.LENGTH_LONG)
									.show();
							registerDialog();
						}
					} catch (Exception e) {
						Toast.makeText(MainActivity.this,
								"Some problem occurred", Toast.LENGTH_LONG)
								.show();
					}
				} else {
					Toast.makeText(MainActivity.this, "Username is empty",
							Toast.LENGTH_LONG).show();
				}
			}
		});
		
		// Adding action to register button
		btReg.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				registerDialog();
			}
		});
	}
	
	/*
	 * Creating Registering Dialog Method
	 */
	private void registerDialog() {
		// Dialog properties
		LayoutInflater li = LayoutInflater.from(this);
		View prompt = li.inflate(R.layout.register, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setView(prompt);
		final EditText regUsername = (EditText) prompt.findViewById(R.id.et_reg_username);
		alertDialogBuilder.setTitle("Registration");
		
		// Adding action to register button in dialog
		alertDialogBuilder.setCancelable(false).setPositiveButton("Register",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						String username = regUsername.getText().toString();
						try {
							if (username.length() < 2) {
								Toast.makeText(MainActivity.this,
										"Invalid username", Toast.LENGTH_LONG)
										.show();
								registerDialog();
							} else {
								try {
									long i = db.register(username);
									if (i != -1) {
										Toast.makeText(MainActivity.this,
												"Successfully Registered",
												Toast.LENGTH_LONG).show();
									} else {
										Toast.makeText(MainActivity.this,
												"Username is invalid",
												Toast.LENGTH_LONG).show();
										registerDialog();
									}
								} catch (Exception e) {
									Toast.makeText(MainActivity.this,
											e.getMessage(), Toast.LENGTH_LONG)
											.show();
								}
							}
						} catch (Exception e) {
							Toast.makeText(MainActivity.this, e.getMessage(),
									Toast.LENGTH_LONG).show();
						}
					}
				});
		
		// Dismiss dialog when click cancel
		alertDialogBuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();

					}
				});

		alertDialogBuilder.show();
	}
}
