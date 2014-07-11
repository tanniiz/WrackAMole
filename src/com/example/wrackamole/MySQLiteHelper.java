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
	private static final String TABLE_RESULT = "RESULT";
	private static final String TABLE_ONCLICK = "ONCLICK";
	private static final String KEY_USERID = "userid";
	private static final String KEY_USERNAME = "username";
	private static final String KEY_SCORE = "score";
	private static final String KEY_LEVEL = "level";
	private static final String KEY_DURATION = "duration";
	private static final String KEY_SEQUENCE = "sequence";
	private static final String KEY_START = "start_time";
	private static final String KEY_END = "end_time";
	private static final String KEY_DATE = "date";
	private static final String KEY_GAMEID = "gameids";
	private static final String KEY_TARGET_POSX = "target_x";
	private static final String KEY_TARGET_POSY = "target_y";
	private static final String KEY_USER_POSX = "user_x";
	private static final String KEY_USER_POSY = "user_y";
	private static final String KEY_USER_PRESSURE = "pressure";

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
		String CREATE_RESULT_TABLE = "CREATE TABLE result ( "
				+ "gameid INTEGER PRIMARY KEY AUTOINCREMENT, userid INTEGER, score INTEGER, level INTEGER, duration INTEGER, sequence STRING, start_time DATETIME, end_time DATETIME DEFAULT CURRENT_TIME, date DATETIME DEFAULT CURRENT_DATE  )";

		// execute creating table
		db.execSQL(CREATE_USERNAME_TABLE);
		db.execSQL(CREATE_RESULT_TABLE);
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

		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME
				+ " WHERE username=?", new String[] { username });

		if (cursor.moveToFirst()) {
			int id = cursor.getInt(cursor.getColumnIndex(KEY_USERID));
			Cursor c = db.rawQuery("SELECT * FROM RESULT WHERE userid=" + id
					+ " ORDER BY score DESC", null);
			if(c.getCount() > 0) {
				if (c.moveToFirst()) {
					do {
						User user = new User();
						user.setID(id);
						user.setUsername(username);
						user.setScore(c.getInt(c.getColumnIndex(KEY_SCORE)));
						user.setLevel(c.getInt(c.getColumnIndex(KEY_LEVEL)));
						userScore.add(user);
					} while (c.moveToNext());
				}
			}
		}

		return userScore;
	}

	public User getLatestScore(String username) {
		db = this.getWritableDatabase();
		User user = new User();

		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME
				+ " WHERE username=?", new String[] { username });
		if (cursor.moveToFirst()) {
			int id = cursor.getInt(cursor.getColumnIndex(KEY_USERID));
			Cursor c = db.rawQuery("SELECT * FROM RESULT WHERE userid=" + id,
					null);
			if (c.getCount() > 0) {
				c.moveToLast();
				user.setID(id);
				user.setUsername(username);
				user.setScore(c.getInt(c.getColumnIndex(KEY_SCORE)));
				user.setLevel(c.getInt(c.getColumnIndex(KEY_LEVEL)));
			} 
		}

		return user;
	}
	
	//public void ressultRec(String username, int score, int level, int duration, String seq, String startTime, String endTime, String date) {
	public long ressultRec(String username, int score, int level, int duration, String seq) {	
		db = this.getWritableDatabase();
		
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME
				+ " WHERE username=?", new String[] { username });
		cursor.moveToFirst();
		int id = cursor.getInt(cursor.getColumnIndex(KEY_USERID));
		
		ContentValues values = new ContentValues();
		values.put(KEY_USERID, id);
		values.put(KEY_SCORE, score);
		values.put(KEY_LEVEL, level);
		values.put(KEY_DURATION, duration);
		values.put(KEY_SEQUENCE, seq);
		//values.put(KEY_LEVEL, level);
		//values.put(KEY_START, startTime);
		//values.put(KEY_END, endTime);
		//values.put(KEY_DATE, date);
		

		// execute insertion
		if(db.insert(TABLE_RESULT, null, values) != -1) {
			Cursor c = db.rawQuery("SELECT MAX(gameid) FROM " + TABLE_RESULT, null);
			c.moveToFirst();
			return c.getInt(cursor.getColumnIndex(KEY_GAMEID));
		} else {
			return -1;
		}
	}
	/*
	public long posRec(String username, ArrayList<Float> onClick){
		db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME
				+ " WHERE username=?", new String[] { username });
		cursor.moveToFirst();
		int id = cursor.getInt(cursor.getColumnIndex(KEY_USERID));
		
		ContentValues values = new ContentValues();
		values.put(KEY_USERID, onClick[]);
		values.put(KEY_SCORE, score);
		values.put(KEY_LEVEL, level);
		values.put(KEY_DURATION, duration);
		values.put(KEY_SEQUENCE, seq);
		
		ContentValues values = new ContentValues();
		
		return db.insert(TABLE_ONCLICK, null, values);
	}*/
}
