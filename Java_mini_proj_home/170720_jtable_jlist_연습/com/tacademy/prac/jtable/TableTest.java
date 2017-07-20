package com.tacademy.prac.jtable;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class TableTest {
	
	public TableTest(){
		
		JFrame f = new JFrame("JTable 예제");
		f.setBounds(250, 250, 300, 300);
		
		//자 이제 표를 만들자
		TableModel tModel = new TableModel();	//data는 별도의 .java가 관리하죠?
		
		JTable table = new JTable(tModel);
		
		JScrollPane pane = new JScrollPane(table);
		table.setModel(tModel);
		
		f.add(pane, BorderLayout.CENTER);
		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		
	}
	
	public static void main(String[] args){
		new TableTest();
	}
}
