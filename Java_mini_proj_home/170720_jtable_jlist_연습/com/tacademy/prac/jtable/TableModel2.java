package com.tacademy.prac.jtable;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class TableModel2 extends AbstractTableModel{
	
	String string;
	String[] colNames = {"번호", "이름", "키", "몸무게", "BMI수치"};
	
	//값 받아올 가짜 더미를 만든다
	ArrayList<AStudent> list = null;
	
	
	@Override
	public int getColumnCount() {
		return colNames.length;
	}

	@Override
	public int getRowCount() {
		return list.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		AStudent s = list.get(rowIndex);
		switch(columnIndex){
		case 0:
			return rowIndex + 1 + "";	//번호 리턴이죠?
		case 1:
			return s.getName();
		case 2:
			return s.getHeight();
		case 3:
			return s.getWeight();
		case 4:
			return s.getBMIgrade();
		}
			return null;
	}

	// list 받아오는거
	public ArrayList<AStudent> getList() {
		return list;
	}
	
	public void setList(ArrayList<AStudent> list) {
		this.list = list;
	}
	
	
	
	
}
