package src.com.tacademy.data;

import java.io.Serializable;

public class AStudent implements Serializable{ /**
	 * 
	 */
	private static final long serialVersionUID = -3290558208507825806L;
	//[객체 직렬화]serializable에 그치지말고 ui값을구하라
	//class 변경할때마다 uid값이 바뀌니까(내부적으로 재생성)
	//여기서 final로 해줘버려 그럼 이클래스 버전업해도 이전데이타날라가지않는다
	
	//내부선 class이름으로비교하는게아니고 사실 serialVersionUId로 같은클래스인지 비교하던것이었다
	private String name;
	private String number;
	private int kor;
	private int eng;
	private int math;
	private int tot;	//sum은 MySQL에서 내장함수라서 충돌날수잇죠?
	private int avrg;
	private int rank;   //challenge
	private char grade;
	private String password;
	
	//개발자가 힘들어서 만든게 사용자한테 편해야한다
	
	public AStudent(){}
	
	public AStudent(String name, String number){
		this.name = name;
		this.number = number;
	}
	
	public AStudent(String name, String number, int kor,
			int eng, int math){
		this.name = name;
		this.number = number;
		this.kor = kor;
		this.eng = eng;
		this.math = math;
	}
	
	//위에서부터 의미있는것들을 집어넣어버릇하자
	//getter setter 우에다가 의미잇는 메솓 넣자^^*
	
	public void findTotal(){
		tot = kor + eng + math;
	}
	
	public void findAverage(){
		avrg = tot / 3;
	}
	
	public void findGrade(){
		switch(avrg / 10){
		case 10: case 9:
			grade = 'A';
			break;
		case 8:
			grade = 'B';
			break;
		case 7:
			grade = 'C';
			break;
		case 6:
			grade = 'D';
			break;
		default:
			grade = 'F';
			break;
		}
	}
	
	
	//////////////////////////////
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public int getKor() {
		return kor;
	}
	public void setKor(int kor) {
		this.kor = kor;
	}
	public int getEng() {
		return eng;
	}
	public void setEng(int eng) {
		this.eng = eng;
	}
	public int getMath() {
		return math;
	}
	public void setMath(int math) {
		this.math = math;
	}
	public int getTot() {
		return tot;
	}
	public void setTot(int tot) {
		this.tot = tot;
	}
	public int getAvrg() {
		return avrg;
	}
	public void setAvrg(int avrg) {
		this.avrg = avrg;
	}
	
	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public char getGrade() {
		return grade;
	}

	public void setGrade(char grade) {
		this.grade = grade;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	

	@Override
	public String toString() {
		return "AStudent [name=" + name + ", number=" + number + ", kor=" + kor + ", eng=" + eng + ", math=" + math
				+ ", tot=" + tot + ", avrg=" + avrg + ", rank=" + rank + ", grade=" + grade + ", password=" + password
				+ "]";
	}

	


	
}
