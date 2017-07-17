package com.tacademy.board.client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.tacademy.board.vo.Board;
import com.tacademy.board.vo.Message;

public class BoardClient {
	
	JFrame f;
	JTextField tfAuthor, tfTitle;
	JTextArea taContents;

	//통신관련 변수
	Socket s;
	ObjectInputStream ois = null;	//객체입력객체
	ObjectOutputStream oos = null;	//객체출력객체		//주석많이써야later소스길어지면분석이이뤄질수있다
	
	ActionListener al = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			switch(cmd){
			case "I":
				insertBoard();	//selectBoard, updateBoard, deleteBoard
				break;
			case "S":
				selectBoard();	
				break;
			case "U":
				updateBoard();	
				break;
			case "D":
				deleteBoard();	
				break;
			}
		}
	};
	
	void insertBoard(){
		// 게시판 db 삽입
		// gettext무작정 하지말고
		// author, title, contents를 받아오는 객체를 하나(클래스) 만들생각해라
		// class 만들때 딸랑 세개 만들생각말고
		// 이제 DB만들거니까 당장은 안필요해도 db맵핑해서 만들거 정하기 (DB기준)
		
		// 패키지명은 MVC의 M 따서 model로 하거나 VO(value obj)로 만든다(현-업)
		Board board = new Board();
		
		board.setBrdid(tfAuthor.getText()); // 문자열단위놉 클래스루만들어!
		board.setBrdtitle(tfTitle.getText());
		board.setBrbtext(taContents.getText());
		
		board.setIp(getLocalIP());
		
/*		// DB연동 (3티어방식이라 새로워!!!)
		// LOBS
		// 
		//Android: Maven 최신정보가있아면 다운받아 사용받을수잇게 다운받아준다
		// 
		
		BoardDAO dao = new BoardDAO();	//자이제 인서트하자
		
		dao.insertBoard(board); //
*/		
		
		// 이제 더이상 db연동하는것이 아니라(암호도 뭐도 몰라!!)
		// 서버만 db연동하게 냅두고 보드를 만들면 보드를 가지는 메세지가 가는거야
		Message msg = new Message();
		msg.setType(0);//insert
		msg.setBoard(board); //객체화시켜서 보내버릴것이야~
		
		//원래 채팅도 채팅msg하나만들어서보내는게좋은거래~ (자바끼리는 ) obj in/output하는게 자바스럽고좋은거랭~
		// 클라도 자바, 서버도 자바! 한쪽이 자바가 아니면 obj in/out stream은 못쓰져?
		
		
		try {
			oos.writeObject(msg);	//보드보내면안돼! 메세지보내라!
			oos.flush();
			System.out.println("MSG sent well!");
		} catch (IOException e) {
			System.out.println("MSG send error: " + e);
		}
		
		//클라에서는 dg연동이 업어서 db ip, port, id, pw, table name도 몰러~~! 단지그냥 board라는 객체를 msg객체에 담아 보내는것뿐이다
		// msg박스만 주거니받거니~
		
		//table정보는 미들웨어인 com만알어~
		//db id/pw 바뀌면 middleware만바꾸면대죠?
		//직접티어면 idpw 바뀌면 수천만대 다시 바꿔줘야되는데
		//db종류 바꿔주는거 등등 바뀌는건 미들웨어에만 적용시키면돼
		//					====> 이래서 3tier가좋은것이다
		
		// 안드로이드 앱 업뎃 자꾸 하는거 짜증나죠? --> 안드는 서버가 있으면 안드로이드앱이 들어가죠?
		// 2티어죠? --> 뭔가 바꾸면 수천만대 다 다시 업데이트 받아야한다
		// 홈페이지는 내부 바꿨다고 업뎃하라고 안하고 페이지만바뀌죠? --> 웹서버는 3티어방식
		// 썜의 생각: 웹서버가 훨씬 좋은방식의 서비스이당(왜 2티어가 유행인지모르겟어 (소스 쪼금 바뀔때마다 며칠, 1주일마다 업뎃해야되자너))
		
	}
	
	
	void selectBoard(){
		// 0: insert 1:select 2:update 3:delete

		Board board = new Board();

		board.setBrdid(tfAuthor.getText()); // id만으로 검색할래

		board.setIp(getLocalIP());


		Message msg = new Message();
		msg.setType(1);//select
		msg.setBoard(board); //객체화시켜서 보내버릴것이야~

		Board tBoard = null; //템프


		try {
			oos.writeObject(msg);	//보드보내면안돼! 메세지보내라!
			System.out.println("MSG sent well!");
			
			msg = (Message) ois.readObject();
			ArrayList<Board> list  = msg.getList();
			
			System.out.println(list);
			
			for(int i = 0; i < list.size(); i++){
				tBoard = list.get(i);
				taContents.append(tBoard.getBrdtext() + "\n");
			}
			
		} catch (IOException e) {
			System.out.println("MSG send error: " + e);
		} catch (ClassNotFoundException e) {
			System.out.println("MSG 잘못 받았어 select 에러: " + e);
		}
	}

	void updateBoard(){
		
		//0: insert 1:select 2:update 3:delete
		
		Board board = new Board();
		
		board.setBrdtitle(tfTitle.getText());
		board.setBrdid(tfAuthor.getText()); // 문자열단위놉 클래스루만들어!
		
		board.setIp(getLocalIP());
		
		
		Message msg = new Message();
		msg.setType(2);//update
		msg.setBoard(board); //객체화시켜서 보내버릴것이야~
		
		

		try {
			oos.writeObject(msg);	//보드보내면안돼! 메세지보내라!
			System.out.println("MSG updated well!");
		} catch (IOException e) {
			System.out.println("MSG update error: " + e);
		}
	}

	void deleteBoard(){
		// 0: insert 1:select 2:update 3:delete

		Board board = new Board();

		board.setBrdid(tfAuthor.getText()); // 문자열단위놉 클래스루만들어!

		board.setIp(getLocalIP());


		Message msg = new Message();
		msg.setType(3);//delete
		msg.setBoard(board); //객체화시켜서 보내버릴것이야~



		try {
			oos.writeObject(msg);	//보드보내면안돼! 메세지보내라!
			System.out.println("MSG deleted well!");
		} catch (IOException e) {
			System.out.println("MSG delete error: " + e);
		}
	}

	String getLocalIP(){
		String str = "127.0.0.1";	// 일단 빨간줄부터 없에는고
		
		try {
			str = InetAddress.getLocalHost().getHostAddress(); //hstAdrs가 ip
		} catch (UnknownHostException e) {
			System.out.println("로컬아이피 얻기 실패: " + e);
		}
		
		return str;
	}
	
	public BoardClient(){
		
		gui();
		
		//접속은 어케하죠? 소켓을 생-성한다
		try{
			System.out.println("1");
			//s = new Socket("192.168.205.153", 12345);	//접속 끗! ^.^ //포트는 서버가 하자는대로~
			s = new Socket(getLocalIP(), 12345);
			
			//입출력 객체
			oos = new ObjectOutputStream(s.getOutputStream());	//얘네 필터야~
			oos.flush();
			ois = new ObjectInputStream(s.getInputStream()); // :D!!!
			System.out.println("접속 성공!2");
			
			
		} catch(IOException e){
			System.out.println("2서버 접속 오류!!! " + e);
		}
		
	}
	
	void gui(){
		
		f = new JFrame();
		JPanel npanel = new JPanel();
		npanel.add(new JLabel("Author: "));
		tfAuthor = new JTextField(10);
		npanel.add(tfAuthor);
		tfTitle = new JTextField(15);
		npanel.add(new JLabel("Title: "));
		npanel.add(tfTitle);
		
		taContents = new JTextArea("msg");
		taContents = new JTextArea(4,30);
		npanel.add(taContents);
		
		
		// spanel, button area
		JPanel spanel = new JPanel(new GridLayout(1, 4));
		JButton btn = new JButton("Write");	// java는 "", db는 ''
		btn.setActionCommand("I");
		btn.addActionListener(al);
		spanel.add(btn);
		
		btn = new JButton("Read");
		btn.setActionCommand("S");
		btn.addActionListener(al);
		spanel.add(btn);
		
		btn = new JButton("Update");
		btn.setActionCommand("U");
		btn.addActionListener(al);
		spanel.add(btn);
		
		btn = new JButton("Delete");
		btn.setActionCommand("D");
		btn.addActionListener(al);
		spanel.add(btn);
		
		
		
		
		
		f.add(npanel, BorderLayout.NORTH);
		f.add(spanel, BorderLayout.SOUTH);
		
		
		f.setBounds(new Rectangle(0,0,400,600));
		
		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
	
	public static void main(String[] args){
		new BoardClient(); // 자바 너 오랜만이다? ㅋ
	}
}
