package com.example.wrackamole;

public class User {
	private int userid;
	private String username;
	private int userScore;
	private int level;
	
	public User() {
		this.userScore = 0;
	}
	
	public User(int userid, String username) {
		this.userid = userid;
		this.username = username;
		this.userScore = 0;
	}
	
	public void setID(int id) {
		this.userid = id;
	}
	
	public int getID() {
		return this.userid;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public int getScore() {
		return userScore;
	}

	public void setScore(int userScore) {
		this.userScore = userScore;
	}
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	
}
