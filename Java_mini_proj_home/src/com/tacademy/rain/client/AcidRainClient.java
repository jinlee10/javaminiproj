package com.tacademy.rain.client;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class AcidRainClient {
	
	JFrame f;
	JPanel cPanel, ePanel, csPanel, csnPanel, cssPanel;
	JTextField tfEntry, tfChat;	//단어입력란, (추후 추가할)챗 입력란
	JTextArea taScreen;			//게임 본화면 표시할 text area
	JButton btn;
	
	// 테스트용 스트링(db 연결 전)
	String[] words = {"무지개", "멱살", "줄넘기", "기지개", 
			"게살버거", "새우버거", "모니터", "프린터", "자바", "스프링"};
	
	// 통신 관련 변수
	Socket s;
	ObjectInputStream ois;
	ObjectOutputStream oos;
	
	ActionListener al = new ActionListener() {
		
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			switch(cmd){
			case "B":
				beginGame();
				break;
			}
		}
	};
	
	void beginGame(){
		
	}
	
	

	//MyTextArea taScreen;// = new MyTextArea();	//위의 JTextArea를 대신함

	/////이미지 출력을 위한 커스텀 textArea
	class MyTextArea extends JTextArea{
		private Image backgroundImage;
		
		public MyTextArea(){
			super();
			setOpaque(false);
			
			File f = new File(".", "exe.txt");
			
		}
		
		public void setBackgroundImage(Image image){
			this.backgroundImage = image;
			this.repaint();
		}

		@Override
		protected void paintComponent(Graphics g) {
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
			
			if(backgroundImage != null){
				g.drawImage(backgroundImage,  0,  0,  this);
			}
			
			super.paintComponent(g);
		}
		
		
	}
	
	
	public AcidRainClient(){
		
		setGUI();
		
		//클라 시작 즉시 서버에 접속하여 DB와 통신 준비한다
		try{
			s = new Socket(getLocalIP(), 12345);
			oos = new ObjectOutputStream( s.getOutputStream() );
			// 오브젝트로 통신하기위한 magical sequence
			oos.flush();
			
		}catch(IOException e){
			System.out.println("클라 접속 오류: " + e);
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
	
	
	void setGUI(){
		
		f = new JFrame("Weak Adic Rain Game Client (o^0^)==o");
		f.setBounds(new Rectangle(0,0,550,700));
		
		// center panel
		cPanel = new JPanel(new BorderLayout());	
		taScreen = new MyTextArea();
		
		// cs panels
		csPanel = new JPanel(new BorderLayout());
		

		
		
		
		
		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
	
	public static void main(String[] args){
		new AcidRainClient();
	}
}
