package com.example.wrackamole;

import android.app.Activity;
import android.view.View.OnClickListener;
import android.content.Context;
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
	final Context context = this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Components
		final EditText etUsername = (EditText) findViewById(R.id.et_username);
		btLogin = (Button) findViewById(R.id.bt_login);
		btReg = (Button)findViewById(R.id.bt_reg);
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
		
		btReg.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				in.hideSoftInputFromWindow(etUsername.getWindowToken(), 0);
				String username = etUsername.getText().toString();
				if (username.length() > 0) {
					try {
						long i = db.register(username);
						if (i != -1) {
							Toast.makeText(MainActivity.this,
									"Successfully Registered",
									Toast.LENGTH_LONG).show();
						} else {
							Toast.makeText(MainActivity.this,
									"Username is already used",
									Toast.LENGTH_LONG).show();
						}
					} catch (Exception e) {
						Toast.makeText(MainActivity.this, "Error Occur",
								Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(MainActivity.this, "Username is empty",
							Toast.LENGTH_LONG).show();
				}
				
			}
		});
	}
}
	/*
	public void register(View v) {
		final Dialog dialog = new Dialog(MainActivity.this);
		dialog.setContentView(R.layout.register);
		dialog.setTitle("Registration");
		
		Button btReg = (Button)findViewById(R.id.bt_reg_create);
		final EditText regUsername = (EditText)findViewById(R.id.et_reg_username);
		
		 Adding action to register button
		btReg.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String username = regUsername.getText().toString();
				if (username.length() > 0) {
					try {
						long i = db.register(username);
						if (i != -1) {
							Toast.makeText(getApplicationContext(),
									"Successfully Registered",
									Toast.LENGTH_LONG).show();
							dialog.dismiss();
						} else {
							Toast.makeText(getApplicationContext(),
									"Username is already used",
									Toast.LENGTH_LONG).show();
						}
					} catch (Exception e) {
						Toast.makeText(getApplicationContext(), "Error Occur",
								Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(getApplicationContext(), "Username is empty",
							Toast.LENGTH_LONG).show();
				}
			}
		});
		dialog.show();
	}*/

