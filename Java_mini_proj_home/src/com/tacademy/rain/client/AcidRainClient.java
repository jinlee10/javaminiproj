package com.tacademy.rain.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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
	MyTextArea taScreen;			//게임 본화면 표시할 text area
	JTextArea taList;			//테스트용 ta
	JButton btn, btnStart;
	
	//콘솔 출력 thread 테스트용 리스트와 쓰레드
	ArrayList<String> list;
	ThreadTest ttest;
	
	//이미지 배경화면 설ㅈ어 관련
	Image img;
	
	// 통신 관련 변수
	Socket s;
	ObjectInputStream ois;
	ObjectOutputStream oos;
	
	//쓰레드
	
	
	ActionListener al = new ActionListener() {
		
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			switch(cmd){
			case "G":
				GameStart();
				break;
			case "E":	//단어입력후 엔터 or 버튼클릭
				//EnterWords();
				break;
			case "T":	//transfer chat : 챗 보내기
				//beginGame();
				break;
			}
		}
	};
	
	void GameStart(){
		ttest = new ThreadTest();
		ttest.start();
		ttest.startGame(1, this);
	}
	
	

	//MyTextArea taScreen;// = new MyTextArea();	//위의 JTextArea를 대신함

	/////이미지 출력을 위한 커스텀 textArea
	class MyTextArea extends JTextArea{
		private Image backgroundImage;
		
		public MyTextArea(){
			super();
			setOpaque(false);
			
			File f = new File(".", "atrump.jpg");
			
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
				g.drawImage(backgroundImage, 0, 0, this);
			}
			
			super.paintComponent(g);
		}
		
		
	}
	
	
	public AcidRainClient(){
		
		setGUI();
		
		setThisImageAsBackground();
		
		//클라 시작 즉시 서버에 접속하여 DB와 통신 준비한다
//		try{
//			s = new Socket(getLocalIP(), 12345);
//			oos = new ObjectOutputStream( s.getOutputStream() );
//			// 오브젝트로 통신하기위한 magical sequence
//			oos.flush();
//			
//		}catch(IOException e){
//			System.out.println("클라 접속 오류: " + e);
//		}
		
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
	
	void setThisImageAsBackground(){
		
		DataInputStream dis = null;
		ImageIcon icon = new ImageIcon("atrump.jpg");
		img = icon.getImage();
		
		if(img != null){
			taScreen.setBackgroundImage(img);
		}
		
	}
	
	
	void setGUI(){
		
		f = new JFrame("Weak Adic Rain Game Client (o^0^)==o");
		f.setBounds(new Rectangle(0,0,700,600));
		
		// center panel
		cPanel = new JPanel(new BorderLayout());	
		taScreen = new MyTextArea();
		taScreen.setBorder(BorderFactory.createLineBorder(new Color(222, 200, 233), 3));
		taScreen.setEditable(false);
		
		// cs panels
		csPanel = new JPanel(new GridLayout(2, 1));
		csnPanel = new JPanel(new BorderLayout()); //CSN
		
		btn = new JButton("ENTER");
		btn.setActionCommand("E");
		btn.addActionListener(al);
		tfEntry = new JTextField();
		csnPanel.add(new JLabel("Write here >"), BorderLayout.WEST);
		csnPanel.add(tfEntry, BorderLayout.CENTER);
		csnPanel.add(btn, BorderLayout.EAST);
		
		cssPanel = new JPanel(new BorderLayout());	//CSS
		
		btn = new JButton("ENTER");
		btn.setActionCommand("T"); //transfer chat
		btn.addActionListener(al);
		tfChat = new JTextField();
		cssPanel.add(new JLabel("Enter chat >"), BorderLayout.WEST);
		cssPanel.add(tfChat, BorderLayout.CENTER);
		cssPanel.add(btn, BorderLayout.EAST);
		
		csPanel.add(csnPanel);
		csPanel.add(cssPanel);
		
		
		// east panel
		ePanel = new JPanel(new BorderLayout());
		taList = new JTextArea();
		taList.setBorder(BorderFactory.createLineBorder(new Color(222, 233, 232), 3));
		taList.setEditable(false);
		
		btnStart = new JButton("START GAME");
		btnStart.setActionCommand("G");
		btnStart.addActionListener(al);
		
		ePanel.add(new JLabel("~ ~ ~ players list ~ ~ ~"), BorderLayout.NORTH);
		ePanel.add(taList, BorderLayout.CENTER);
		ePanel.add(btnStart, BorderLayout.SOUTH);
		
		
		
		// add 'em all onto cpanel
		cPanel.add(taScreen, BorderLayout.CENTER);
		cPanel.add(csPanel, BorderLayout.SOUTH);

		
		// f
		f.add(cPanel, BorderLayout.CENTER);
		f.add(ePanel, BorderLayout.EAST);
		
		
		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
	
	public void printOnMyConsole(String s){
		System.out.println("전달받은 스트링: " + s);
	}
	
	public static void main(String[] args){
		new AcidRainClient();
	}
}
