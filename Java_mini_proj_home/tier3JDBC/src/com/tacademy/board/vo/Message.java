package com.tacademy.board.vo;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable{	// 객체 단위 입출력을 위해 들어가야할것
	/**
	 *		versionid를 상수로 때려박긔~ 
	 */
	private static final long serialVersionUID = 4456011244063136867L;
	
	private int type = 0; // 0: insert 1:update 2:delete 3:select
	private Board board;
	
	// select를위한 arraylist보관용 ㅋㅋ
	private ArrayList<Board> list;
	
	
	
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Board getBoard() {
		return board;
	}
	public void setBoard(Board board) {
		this.board = board;
	}
	
	public ArrayList<Board> getList() {
		return list;
	}
	public void setList(ArrayList<Board> list) {
		this.list = list;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}	
	
}
