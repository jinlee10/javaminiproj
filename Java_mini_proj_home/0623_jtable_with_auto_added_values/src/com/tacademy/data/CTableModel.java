package src.com.tacademy.data;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class CTableModel extends AbstractTableModel{
	
	String string;
	String[] colNames = {"이름", "번호", "국어", "영어", "math", "total", "avrg", "grade"} ;
	
	ArrayList<AStudent> list = null; //받아올 가짜더미를 ㅏㅁㄴ든다
	
	
	
	@Override
	public int getColumnCount() {
		
		return colNames.length;
	}

	@Override
	public int getRowCount() {
		
		return list.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) { //반환얼~해야제~
		AStudent as = list.get(rowIndex);
		switch(columnIndex){
		case 0:
			return as.getName();
		case 1:
			return as.getNumber();
		case 2:
			return as.getKor();
		case 3:
			return as.getEng();
		case 4:
			return as.getMath();
		case 5:
			return as.getTot();
		case 6:
			return as.getAvrg();
		case 7:
			return as.getGrade();
		}
			return null;
	}
	
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		boolean flag = false;
		
		switch(columnIndex){
		case 2: case 3: case 4:
			flag = true;
			break;
		}
		return flag;
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		AStudent as = list.get(rowIndex); //*번째 학생을 갖고온다
		
		//aValue는 Object 형이니까 string으로 바꿔서 Int로 바꾸자
		String s = aValue.toString();
		
		//column번째의 값을 바꾸면 setValue할거야
		switch(columnIndex){
		case 2:
			as.setKor(Integer.parseInt(s)); //string넣으면 망함ㅋㅋ숫자만넣어야함
			break;
		case 3:
			as.setEng(Integer.parseInt(s));
			break;
		case 4:
			as.setMath(Integer.parseInt(s));
			break;
		}
		
		as.findTotal();
		as.findAverage();
		as.findGrade();
	}

	@Override
	public String getColumnName(int column) {
		
		return colNames[column];
	}
	////////////////////////////////////////////
	public ArrayList<AStudent> getList() {
		return list;
	}
	public void setList(ArrayList<AStudent> list) {
		this.list = list;
	}
	
	
}
