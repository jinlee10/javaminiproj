package com.tacademy.board.vo;

import java.io.Serializable;

public class Board implements Serializable{

	private static final long serialVersionUID = 3930025703297077747L;

	private int board_num;
	private String brdid;
	private String brdtitle;
	private String brdtext;
	private String brdip;
	private String board_regdate;
	
	public int getBoard_num() {
		return board_num;
	}

	public void setBoard_num(int board_num) {
		this.board_num = board_num;
	}

	public String getBrdid() {
		return brdid;
	}

	public void setBrdid(String brdid) {
		this.brdid = brdid;
	}

	public String getBrdtitle() {
		return brdtitle;
	}

	public void setBrdtitle(String brdtitle) {
		this.brdtitle = brdtitle;
	}

	public String getBrdtext() {
		return brdtext;
	}

	public void setBrdtext(String brdtext) {
		this.brdtext = brdtext;
	}

	public String getBrdip() {
		return brdip;
	}

	public void setBrdip(String brdip) {
		this.brdip = brdip;
	}

	public String getBoard_regdate() {
		return board_regdate;
	}

	public void setBoard_regdate(String board_regdate) {
		this.board_regdate = board_regdate;
	}


	@Override
	public String toString() {
		return "Board [board_num=" + board_num + ", brdid=" + brdid + ", brdtitle=" + brdtitle + ", brdtext=" + brdtext
				+ ", brdip=" + brdip + ", board_regdate=" + board_regdate + "]";
	}
}
