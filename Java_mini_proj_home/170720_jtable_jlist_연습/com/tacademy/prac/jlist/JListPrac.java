package com.tacademy.prac.jlist;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class JListPrac {
	
	JFrame f;
	JTextArea ta;
	JList<String> list;
	ArrayList<String> dataList;
	
	ActionListener al = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			switch(cmd){
				case "A":
					doSomething1();
					break;
			}
		}
	};
	
	void doSomething1(){
		String value = list.getSelectedValue();
		ta.setText(value);
	}
	
	public JListPrac(){	///생성자죠?
		
		f = new JFrame("JComponent - JList 예제래!");
		f.setBounds(200, 200, 350, 500);
		
		JPanel p = new JPanel(new BorderLayout());
		
		JButton btn = new JButton("선택");
		btn.setActionCommand("A");
		btn.addActionListener(al);
		
		ta = new JTextArea();
		
		///
		
		list = new JList<String>();		// list가 있어!
		
		dataList = new ArrayList<String>();
		dataList.add("안녕1");
		dataList.add("안녕2");
		dataList.add("안녕3");
		dataList.add("안녕4");
		dataList.add("안녕5");
		dataList.add("안녕6");
		dataList.add("안녕7");		// 값을 저장했어!
		
		Object[] objs = dataList.toArray();		// arraylist를 toarray해서 object 배열에 넣는다
		String[] arr = new String[objs.length];
		
		for(int i =0; i < objs.length; i++){
			arr[i] = objs[i].toString();
		}
		
		list.setListData(arr);
		JScrollPane spn = new JScrollPane(list);
		
		///
		
		//==========================
		
		String[] strArr = {};
		JScrollPane jsp = new JScrollPane(list);
		
		p.add(list, BorderLayout.CENTER);
		p.add(btn, BorderLayout.EAST);
		
		f.add(p, BorderLayout.NORTH);
		f.add(ta, BorderLayout.CENTER);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	public static void main(String[] args){
		new JListPrac();
	}
}
