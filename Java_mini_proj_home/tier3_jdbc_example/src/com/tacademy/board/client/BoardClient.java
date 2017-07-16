package com.tacademy.board.client;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.tacademy.board.vo.Board;
import com.tacademy.board.vo.Message;

public class BoardClient {

	public JFrame f;
	JPanel nPanel, sPanel;
	JTextField tfAuthor, tfTitle ;
	JTextArea taContent;
	JButton btn;
	
	// 통신 관련 변수
	Socket s;
	ObjectInputStream ois;
	ObjectOutputStream oos;
	
	ActionListener al =  new ActionListener() {
		
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			switch( cmd ) {
			case "I":
				insertBoard();
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
	
	void insertBoard() {
		
		Board board = new Board();
		board.setBrdid(tfAuthor.getText()); // 문자열단위놉 클래스루만들어!
		board.setBrdtitle(tfTitle.getText());
		board.setBrdtext(taContent.getText());
		board.setBrdip(getLocalIP());

		// 3티어로 가자!
		// 서버 소켓 연동
		Message msg = new Message();
		msg.setType(0);	// 0 : INSERT
		msg.setBoard(board);
		
		try {
			oos.writeObject(msg);
			System.out.println("전송 성공");
		} catch (IOException e) {
			System.out.println("오브젝트 전송 오류 " + e);
		}
		
/*
		// DB 연동을 시작해보자.
		// 이제 Maven을 써보자. 인터넷에 연결되어 있다면, 알아서 라이브러리를 관리해준다. 착하다.
		// DAO를 통해서 데이터 입출력을 관리하기로 했다.
		// 객체를 만들어서 필요한 객체만 전달해주자. 
		BoardDAO dao = new BoardDAO();
		dao.insertBoard(board);
*/	
		
	}
	

	
	
	void selectBoard(){
		// 0: insert 1:select 2:update 3:delete

		Board board = new Board();

		board.setBrdid(tfAuthor.getText()); // id만으로 검색할래

		board.setBrdip(getLocalIP());


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
				taContent.append(tBoard.getBrdtext() + "\n");
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
		
		board.setBrdip(getLocalIP());
		
		
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

		board.setBrdip(getLocalIP());


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
	
	public String getLocalIP() {
		
		String localIP = "127.0.0.1";
		
		try {
			localIP = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			System.out.println("Local IP 얻기 실패" + e);
		}
		
		return localIP;
		
	}
	
	
	public BoardClient() {
		
		setGUI();
		
		// 클라이언트도 시작과 동시에 서버에 접속을해서 DB와 통신을 위한 준비를 해야 한다.
		try {
			s = new Socket("192.168.219.102", 12345);
			oos = new ObjectOutputStream( s.getOutputStream() );
			// 오브젝트로 통신을 하기 위한 magical sequence를 보내자.
			// 그래서 oos를 위로 올려서 먼저 신호를 보냈다.
			oos.flush();	
			ois = new ObjectInputStream( s.getInputStream() );

		} catch (IOException e) {
			System.out.println("클라이언트 접속 오류" + e);
		}

		
	}
	
	void setGUI(){
		
		f = new JFrame("board client");
		f.setBounds(0, 0, 500, 600);
		f.addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { f.dispose(); } });
		
		nPanel = new JPanel();
		nPanel.add(new JLabel("Author: "));
		tfAuthor = new JTextField(10);
		nPanel.add(tfAuthor);
		tfTitle = new JTextField(15);
		nPanel.add(new JLabel("Title: "));
		nPanel.add(tfTitle);
		
		taContent = new JTextArea("msg");
		taContent = new JTextArea(4,30);
		nPanel.add(taContent);
		
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
		
		
		f.add(sPanel, "South");
		
		f.setVisible(true);
		
		
	}
	
	public static void main( String[] args ) {
			
		new BoardClient();
		
	}
	
}
