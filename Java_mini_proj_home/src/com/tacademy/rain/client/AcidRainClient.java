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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.tacademy.rain.vo.AcidRain;
import com.tacademy.rain.vo.Message;

public class AcidRainClient {
	
	private JFrame f;
	private JPanel cPanel, csPanel, csnPanel, cssPanel,
			ePanel, ecPanel, ecnPanel, esPanel; 
	private JComboBox<String> cbox;
	private JTextField tfEntry, tfChat;	//단어입력란, (추후 추가할)챗 입력란
	private MyTextArea taScreen;			//게임 본화면 표시할 text area
	private JTextArea taList;			//테스트용 ta
	private JButton btn, btnStart;
	
	//콘솔 출력 thread 테스트용 리스트와 쓰레드
	private ArrayList<String> list;
	private ThreadTest ttest;
	
	//이미지 배경화면 설ㅈ어 관련
	private Image img;
	private AcidRainClientPanel ap;

	private String[] words = {"무지개", "멱살", "줄넘기", "기지개", 
	"게살버거", "새우버거", "모니터", "프린터", "자바", "스프링"};
	
	// 통신 관련 변수
	private Socket s;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	//쓰레드
	
	
	//JDBC 3 tier 연동
	ArrayList<AcidRain> rList; //단어 받아오는 리스트
	
	
	
	ActionListener al = new ActionListener() {
		
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			switch(cmd){
			case "G":
				gameStart();
				break;
			case "E":	//단어입력후 엔터 or 버튼클릭
				EnterWords();
				break;
			case "T":	//transfer chat : 챗 보내기 // no need 4 now
				//beginGame();
				break;
				
			//////////////////////////////////////////////////////////
			// CRUD //
			case "I":
				insertUser();
				break;
			case "S":
				selectWords();	
				break;
			case "U":
				updateUserScore();	
				break;
			case "D":
				deleteUser();	 
				break;
			case "N":
				selectWordTypeName();
				break;
			}
		}
	};
	
	//////////////////////////////////////////////////////////////
	//				C	R	U	D	
	//////////////////////////////////////////////////////////////
	
	void insertUser(){

		AcidRain rain = new AcidRain();
		rain.getUsername();
		rain.getIp();

		// 3티어로 가자!
		// 서버 소켓 연동
		Message msg = new Message();
		msg.setType(0);	// 0 : INSERT
		msg.setAcidrain(rain);
		
		try {
			oos.writeObject(msg);
			System.out.println("전송 성공");
		} catch (IOException e) {
			System.out.println("오브젝트 전송 오류 " + e);
		}
	}
	
	void selectWords(){
		// 0: insert 1:select 2:update 3:delete
		
		AcidRain acidrain = new AcidRain();
		
		acidrain.setTypeidx(1); //select
		
	}
	
	void updateUserScore(){
		
	}
	
	void deleteUser(){
		
	}
	
	// default word type
	void selectWordTypeName(){
		AcidRain acidrain = new AcidRain();
		acidrain.setTypeidx(1);//select
		//acidrain.set
		
		Message msg = new Message();
		msg.setType(1);	//select
		msg.setAcidrain(acidrain);//객체화시켜서 보낼거야
		
		AcidRain tempR = null;//템프
		
		try{
			oos.writeObject(msg);	// 보드말고 메세지로 객체화시켜보낸다
			System.out.println("MSG sent well!");
			
			msg = (Message) ois.readObject();
			rList = msg.getList();
			
			for(int i = 0; i < rList.size(); i++){
				tempR = rList.get(i);
			}
			
		} catch(IOException e){
			System.out.println("msg selectDefault error: " + e);
		} catch (ClassNotFoundException e) {
			System.out.println("msg 잘못 받음! selectDefault error: " + e);
		}
		
		
		
		
		
		
		
	}
	
	// ==========================================================
	
	
	void gameStart(){
		
		
		
		list = new ArrayList<String>();
		for(int i = 0; i < rList.size(); i++){
			list.add(rList.get(i).getWord()); //받아온걸 넣어
		}
		
		ap.setList(list);
		ap.startAniThread();
		
		//버튼 끝날때까지 비활성화
		btnStart.setEnabled(false);
	}
	
	//버튼 활성화
	public void gameIsOver(){
		btnStart.setEnabled(true);
	}
	
	void putValues2List(){
		list = new ArrayList<String>();
		for(int i = 0; i < words.length; i++){
			list.add(words[i]);
		}
	}
	
	void EnterWords(){
		String input = tfEntry.getText();
		
		//
		//쓰레드가 돌아가고있을때를 체크하고 돌고있을대만 아래 메소드를
		//실행하고 싶으면 어떻게할까?
		//
		
		ap.matchWord(input);	//텍스트를 받아온 후 보내버린다(비교하러)
		tfEntry.setText("");
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
		
//		setThisImageAsBackground();
		
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
		ap = new AcidRainClientPanel(this);
		
		
		// cs panels
		csPanel = new JPanel(new GridLayout(2, 1));
		csnPanel = new JPanel(new BorderLayout()); //CSN
		
		btn = new JButton("ENTER");
		btn.setActionCommand("E");
		btn.addActionListener(al);
		
		//tfEntry에 ActionListener추가하여 엔터 인식하도록
		tfEntry = new JTextField();	
		tfEntry.setActionCommand("E");
		tfEntry.addActionListener(al);
		tfEntry.setBorder(BorderFactory.createLineBorder(new Color(222, 200, 222), 4));
		
		csnPanel.add(new JLabel("Write here >"), BorderLayout.WEST);
		csnPanel.add(tfEntry, BorderLayout.CENTER);
		csnPanel.add(btn, BorderLayout.EAST);
		
		cssPanel = new JPanel(new BorderLayout());	//CSS
		
		btn = new JButton("ENTER");
		btn.setActionCommand("T"); //transfer chat
		btn.addActionListener(al);
		tfChat = new JTextField();
		tfChat.setBorder(BorderFactory.createLineBorder(new Color(212, 222, 222), 4));
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
		
		ePanel.add(taList, BorderLayout.CENTER);
		ePanel.add(btnStart, BorderLayout.SOUTH);
		ePanel.setBorder(BorderFactory.createLineBorder(new Color(232, 220, 210), 3));
		
		ecPanel = new JPanel(new GridLayout(2, 1)); //east Center
		
		ecnPanel = new JPanel(new BorderLayout());
		ecnPanel.add(new JLabel("~ ~ ~ players list ~ ~ ~"), BorderLayout.NORTH);
		ecnPanel.add(taList, BorderLayout.CENTER);
		
		ecPanel.add(ecnPanel);
		ecPanel.add(new JTextArea());
		
		esPanel = new JPanel(new GridLayout(2, 1));
		esPanel.setBorder(BorderFactory.createLineBorder(new Color(232, 240, 220), 3));
		
		cbox = new JComboBox<String>();
		
		esPanel.add(cbox);
		esPanel.add(btnStart);
		
		ePanel.add(ecPanel, BorderLayout.CENTER);
		ePanel.add(esPanel, BorderLayout.SOUTH);
		
		
		
		
		
		
		
		
		// add 'em all onto cpanel
//		cPanel.add(taScreen, BorderLayout.CENTER);
		cPanel.add(ap, BorderLayout.CENTER);
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
