package com.tacademy.rain.vo;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable{	//직렬화하여 보낼 객체
	//사실 얘도 최상위 하나 만들어놓고 용도별로 다르게
	//쓰려면 변수추가할게 아니라 상속시켜서 사용해야함
	
	
	private static final long serialVersionUID = -3546158357126396496L;
	
	private int type = 0;
	private AcidRain acidrain;
	
	//select를 위한 ArrayList보관용
	private ArrayList<AcidRain> list;

	
	// ================ GETTER SETTER ====================
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public AcidRain getAcidrain() {
		return acidrain;
	}

	public void setAcidrain(AcidRain acidrain) {
		this.acidrain = acidrain;
	}

	public ArrayList<AcidRain> getList() {
		return list;
	}

	public void setList(ArrayList<AcidRain> list) {
		this.list = list;
	}
	
	
	// toString
	@Override
	public String toString() {
		return "Message [type=" + type + ", acidrain=" + acidrain + ", list=" + list + "]";
	}
	
	
}
