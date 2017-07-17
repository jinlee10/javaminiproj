package com.tacademy.board.vo;

import java.io.Serializable;

// 이름이 board야? ==> table이랑 유사할확률이높겠지?
// 입출력..가능성... ==> 직렬화

// select할떄 달라져? select 할때에 맞게 만들어줘랑

public class Board implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4484631494349119503L;
	private int brdnum;
	private String brdid;
	private String brdtitle;
	private String brdtext;
	private String idate;		// 날짜도 string으로잡아랑
	private String ip;
	
	// getter setter
	
	public int getBrdnum() {
		return brdnum;
	}
	public void setBrdnum(int brdnum) {
		this.brdnum = brdnum;
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
	public void setBrdtitle(String brdtite) {
		this.brdtitle = brdtite;
	}
	public String getBrdtext() {
		return brdtext;
	}
	public void setBrbtext(String brdtext) {
		this.brdtext = brdtext;
	}
	public String getIdate() {
		return idate;
	}
	public void setIdate(String idate) {
		this.idate = idate;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	// tostring으로 멤버필드 확인
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	
}
