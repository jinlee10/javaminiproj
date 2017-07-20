package com.tacademy.prac.jtable;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class TableTest2 extends JFrame{
	
	// activity는 extends JFrame이랑 같아...
	// 이 클래스 자체가 View역할을 하는것
	
	ArrayList<AStudent> list;
	TableModel2 model;
	
	Random random = new Random();
	
	ActionListener al = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			addEntry(); //add 버튼 클릭시 
		}
	};
	
	void addEntry(){
		String input = JOptionPane.showInputDialog(this, "이름을 입력하시오!");
		AStudent as = new AStudent(input);
		
		list.add(as);	//모델에 더한다
		
		model.fireTableDataChanged();	//이걸쏴줘야 바꼈다고 알림
	}
	
	public TableTest2(){
		
		setTitle("학생 목록 리스트");
		setBounds(200, 200, 500, 500);
		
		JTable table = new JTable();//얘가 안드서 listView야
		list = new ArrayList<AStudent>();
		
		list.add(new AStudent("John Cena"));
		list.add(new AStudent("Bob Ross"));
		list.add(new AStudent("Mariah Carey"));
		list.add(new AStudent("김두한"));
		list.add(new AStudent("심영"));
		
		//MVC: Data따로, 보여지는거 따로, Control 따로!
		//JTable은 2 b accurate, Control만한다
		//Model관리하는 클라스 따로있어야겠지? //abstract table모델 상속해서 관리s

		model = new TableModel2();	//데이터
		model.setList(list);		//리스트 넣어주죠?
		table.setModel(model);
		
		//그냥하면안되고 스크롤집어넣어줘야지
		//JScrollPane pane = new JScrollPane(table);	
		add(new JScrollPane(table), BorderLayout.CENTER);

		//버튼 및 누르면 인간하나 생성 추가하기~
		JButton btn = new JButton("Add User");
		btn.setActionCommand("A");
		btn.addActionListener(al);
		
		add(btn, BorderLayout.SOUTH);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public static void main (String[] args){
		new TableTest2();
	}
}
