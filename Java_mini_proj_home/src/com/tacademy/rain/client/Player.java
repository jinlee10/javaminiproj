package com.tacademy.rain.client;

import java.io.Serializable;

public class Player implements Serializable{
	
	private static final long serialVersionUID = 4371292893094964475L;
	
	private String pName;
	private int pScore;
	
	
	// getter setter //
	
	public String getpName() {
		return pName;
	}
	public void setpName(String pName) {
		this.pName = pName;
	}
	public int getpScore() {
		return pScore;
	}
	public void setpScore(int pScore) {
		this.pScore = pScore;
	}
	
	
}
