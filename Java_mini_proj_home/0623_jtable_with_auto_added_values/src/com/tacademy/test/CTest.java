package src.com.tacademy.test;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import src.com.tacademy.data.AStudent;
import src.com.tacademy.data.CTableModel;


public class CTest extends JFrame{ //입출력대상에 쓸거아니면 serializable안해두대
	
	//activity는 extends JFrame이랑 같아... 이 클래스 자체가 View 역할을 하는것
	
	ArrayList<AStudent> list;
	CTableModel model;
	
	Random random = new Random();
	
	ActionListener al = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			doAction1();
		}
	};
	
	public void doAction1(){
		String msg = JOptionPane.showInputDialog(this, "이름을 입력하시오");
		AStudent ns = new AStudent(msg, "T001");
		ns.setKor(random.nextInt(101));
		ns.setEng(random.nextInt(101));
		ns.setMath(random.nextInt(101));
		
		ns.findTotal();
		ns.findAverage();
		ns.findGrade();
				
		list.add(ns); //힝 속았지? 값 바꼈다고 모델한테 알려줘야돼!
		
		model.fireTableDataChanged();//이걸해줘야 야! 야야! 값 바꼈어! 라고 알려줌
	}
	
	
	public CTest(){
		
		setTitle("JTable 학생목록 리스트");
		setBounds(new Rectangle(900, 350, 500, 400));
		
		JTable table = new JTable();//얘가 안드서 listView야
		//그냥하면안되고 스크롤집어넣어줘야지
		//JScrollPane pane = new JScrollPane(table);	
		list = new ArrayList<AStudent>();

		list.add(new AStudent("JOHN CENA", "U CANT C ME", 99, 47, 75));
		list.add(new AStudent("밥로스", "so ez", 88, 91, 77));
		list.add(new AStudent("심영", "(__)", 55, 99, 94));
		list.add(new AStudent("구마적", "1004", 77, 47, 77));
		list.add(new AStudent("시라소니", "8282", 39, 47, 57));
		list.add(new AStudent("문영철", "52", 37, 68, 76));
		list.add(new AStudent("쌍칼", "SW0RD", 78, 29, 90));
		
		//MVC: Data따로, 보여지는거 따로, Control 따로!
		//JTable은 2 b accurate, Control만한다
		//Model관리하는 클라스 따로있어야겠지? //abstract table모델 상속해서 관리s
		
		model = new CTableModel();
		model.setList(list);
		table.setModel(model);
		
		for(AStudent s :list){
			s.findTotal();
			s.findAverage();
			s.findGrade();
		}
		
		
		add(new JScrollPane(table), BorderLayout.CENTER);
		
		
		//버튼 및 누르면 인간하나 생성 추가하기~
		JButton btn = new JButton("add");
		btn.addActionListener(al);
		
		add(btn, BorderLayout.SOUTH);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public static void main(String[] args){
		new CTest();
	}
}
