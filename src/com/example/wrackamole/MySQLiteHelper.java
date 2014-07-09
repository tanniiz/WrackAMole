package com.example.wrackamole;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {

	private static final int dbVer = 1;
	private static final String dbName = "username";
	private static final String TABLE_NAME = "USER";
	private static final String KEY_USERID = "userid";
	private static final String KEY_USERNAME = "username";
	private static final String KEY_SCORE = "score";
	private SQLiteDatabase db;

	public MySQLiteHelper(Context context) {
		super(context, dbName, null, dbVer);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// SQL statement to create a table
		String CREATE_USERNAME_TABLE = "CREATE TABLE user ( "
				+ "userid INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "username VARCHAR )";
		String CREATE_SCORE_TABLE = "CREATE TABLE score ( "
				+ "userid INTEGER, " + "score INTEGER )";

		// execute creating table
		db.execSQL(CREATE_USERNAME_TABLE);
		db.execSQL(CREATE_SCORE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db = this.getWritableDatabase();

		// Drop older the table if existed
		db.execSQL("DROP TABLE IF EXISTS username");
		db.execSQL("DROP TABLE IF EXISTS score");

		// create fresh table
		this.onCreate(db);
	}

	public long register(String username) {
		db = this.getWritableDatabase();

		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME
				+ " WHERE username=?", new String[] { username });
		if (cursor.getCount() == 0) {
			// create ContentValues to add column and its value
			ContentValues values = new ContentValues();
			values.put(KEY_USERNAME, username);

			// execute insertion
			return db.insert(TABLE_NAME, null, values);
		} else {
			return -1;
		}

	}

	public boolean login(String username) {
		db = this.getWritableDatabase();

		// create cursor to contain SQL statement execution
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME
				+ " WHERE username=?", new String[] { username });

		// check execution
		if (cursor != null) {
			if (cursor.getCount() > 0) {
				return true;
			}
		} else {
			return false;
		}
		return false;
	}

	public List<User> getScore(String username) {
		db = this.getWritableDatabase();

		List<User> userScore = new ArrayList<User>();

		ContentValues values = new ContentValues();
		values.put(KEY_USERID, 1);
		values.put(KEY_SCORE, 100);
		db.insert("SCORE", null, values);

		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME
				+ " WHERE username=?", new String[] { username });

		if (cursor.moveToFirst()) {
			int id = cursor.getInt(cursor.getColumnIndex(KEY_USERID));
			Cursor c = db.rawQuery("SELECT * FROM SCORE WHERE userid=" + id,
					null);
			if (c.moveToFirst()) {
				do {
					User user = new User();
					user.setID(id);
					user.setUsername(username);
					user.setScore(c.getInt(c.getColumnIndex(KEY_SCORE)));
					userScore.add(user);
				} while (c.moveToNext());
			}
		}

		return userScore;
	}
}
