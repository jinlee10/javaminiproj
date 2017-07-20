package com.tacademy.prac.jtable;

import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel{
	
	//오홍홍 좋아용
	private static final long serialVersionUID = -7766946618277799541L;

	String[] culName = {"번호", "이름", "나이"};
	
	String[][] data = {
			{"John Cena", "17"},
			{"Bob Ross", "22"},
			{"민주", "26"}
	};
	
	
	
	@Override
	public int getColumnCount() {
		return culName.length;
	}

	@Override
	public int getRowCount() {
		return data.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		return (columnIndex == 0) 
				? (rowIndex + 1 + "") : data[rowIndex][columnIndex - 1];
	}
	
	public String getColumnName(int columnIndex){
		return culName[columnIndex];
	}
	
	
	
	
}
