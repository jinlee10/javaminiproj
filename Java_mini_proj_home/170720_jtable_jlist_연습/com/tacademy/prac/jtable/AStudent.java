package com.tacademy.prac.jtable;

import java.io.Serializable;
import java.util.Random;

public class AStudent implements Serializable{
	
	private static final long serialVersionUID = -8631929114968211267L;
	//[객체 직렬화]serializable에 그치지말고 ui값을구하라
	//class 변경할때마다 uid값이 바뀌니까(내부적으로 재생성)
	//여기서 final로 해줘버려 그럼 이클래스 버전업해도 이전데이타날라가지않는다

	//내부선 class이름으로비교하는게아니고 사실 serialVersionUId로 같은클래스인지 비교하던것이었다
	private Random random = new Random();
	
	private String name;
	private int height;
	private int weight;
	private char BMIgrade;
	
	private String password;	//transient할거
	
	//개발자가 힘들어서 만든게 사용자한테 편리해야한다
	
	public void findBMI(){
		int hOverW = height / weight;
		switch(hOverW){
		case 1:
			BMIgrade = 'X';
			break;
		case 2:
			BMIgrade = 'O';
			break;
		default:
			BMIgrade = 'N';
			break;
		}
	}
	
	//생성자죠?
	public AStudent(String name){
		this.name = name;
		findHeight();
		findWeight();
		findBMI();
	}
	
	//getter setter
	public void findHeight(){
		height = random.nextInt(30) + 150;
	}
	
	public void findWeight(){
		weight = random.nextInt(30) + 45;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public char getBMIgrade() {
		return BMIgrade;
	}
	public void setBMIgrade(char BMIgrade) {
		this.BMIgrade = BMIgrade;
	}
	
	
	
	
}
