package com.tacademy.rain.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
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
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.tacademy.rain.vo.AcidRain;
import com.tacademy.rain.vo.Message;

public class AcidRainClient {
	
	private JFrame f;
	private JPanel cPanel, csPanel, csnPanel, cssPanel,
			ePanel, ecPanel, ecnPanel, esPanel,
			nPanel, nwPanel;
	private JTextField tfTypeSelect;
	private JTextField tfEntry, tfChat, tfUsername;	//단어입력란, (추후 추가할)챗 입력란
//	private MyTextArea taScreen;			//게임 본화면 표시할 text area
	private JTextArea taList, taTypename;			//테스트용 ta
	private JButton btn, btnSignUp, btnStart;
	
	//나의 점수와 이름
	private int score;
	private String name;
	private boolean nameNeverChanged = true;
	
	//상대방(들)의 점수
	private ArrayList<AcidRain> others; //이름, 점수
	private HashMap<String, Integer> users;
	
	//콘솔 출력 thread 테스트용 리스트와 쓰레드
	private ArrayList<String> typeList;
	private ArrayList<String> list;
	private ThreadTest ttest;
	
	//이미지 배경화면 설ㅈ어 관련
	private Image img;
	private AcidRainClientPanel ap;
	
	// 통신 관련 변수
	private Socket s;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	//쓰레드
	private boolean startFlag = false;
	
	
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
				selectWordTypeName();	//readerThread시작전에만드는거
				break;
			}
		}
	};
	
	//////////////////////////////////////////////////////////////
	//				C	R	U	D	
	//////////////////////////////////////////////////////////////
	
	void insertUser(){

		String dummy = tfUsername.getText();
		
		AcidRain rain = new AcidRain();
		rain.setUsername(dummy);
		rain.setIp(getLocalIP());

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
	
	ArrayList<String> selectWords(){
		// 0: insert 1:select 2:update 3:delete
		int typeidx = 0;
		
		for(int i = 0; i < typeList.size(); i++){
			if(tfTypeSelect.getText().equals(typeList.get(i))){
				typeidx = i + 1;
			}
		}
		
		AcidRain acidrain = new AcidRain();
		
		acidrain.setTypeidx(1); //select
		acidrain.setTypeidx(typeidx);
		
		Message msg = new Message();
		msg.setType(1);
		msg.setAcidrain(acidrain);
		
		try{
			oos.writeObject(msg);
			System.out.println("MSG sent well!");
			
			msg = (Message) ois.readObject();
			
			ArrayList<AcidRain> alist = msg.getList();
			
			System.out.println("list size: " + alist.size());
			
			for(int i = 0; i < alist.size(); i++){
				list.add(i, alist.get(i).getWord());
			}
			
			//워드를 받았으면 
		}catch(IOException e){
			System.out.println("MSG sent error: " + e);
		}catch(ClassNotFoundException e){
			System.out.println("MSG receive error(select): " + e);
		}
		

		
		System.out.println("일단 여까지 왔어 4");
		return list;
	}
	
	void updateUserScore(){
		//유저업데이트
	}
	
	void updateUserName(){
		//유저이름 업데이트
	}
	
	void deleteUser(){
		//유저딜리트
	}
	
	// default word type //이건 처음 한 번만
	void selectWordTypeName(){
		AcidRain acidrain = new AcidRain();
		acidrain.setTypeidx(4);//select
		//acidrain.set
		
		Message msg = new Message();
		msg.setType(4);	//select type name
		msg.setAcidrain(acidrain);//객체화시켜서 보낼거야
		
		String tempR = null;//템프
		
		try{
			oos.writeObject(msg);	// 보드말고 메세지로 객체화시켜보낸다
			System.out.println("MSG sent well!");
			
			msg = (Message) ois.readObject();
			
			rList = msg.getList();
			
			for(int i = 0; i < rList.size(); i++){
				tempR = rList.get(i).getTypename();
				System.out.println("tempR : " + tempR);
			}
			
		} catch(IOException e){
			System.out.println("msg selectDefault error: " + e);
		} catch (ClassNotFoundException e) {
			System.out.println("msg 잘못 받음! selectDefault error: " + e);
		}
		
		typeList = new ArrayList<String>();
		
		for(int i = 0; i < rList.size(); i++){
			System.out.println(rList.get(i).getTypename());
			taTypename.append(rList.get(i).getTypename() + "\n");
			
			////////////////////////////////////////////////
			typeList.add(i, rList.get(i).getTypename());
			////////////////////////////////////////////////
		}

		
		
	}
	
	// ==========================================================
	
	
	void gameStart(){
//		if(!startFlag){
//			
//			
//			
//			startFlag = true;
//			return;
//		}
		
		list = new ArrayList<String>();
		
		list = selectWords();				// ??????????????
		
		for(int i = 0; i < list.size(); i++){
			System.out.println(list.get(i));
		}
		
		System.out.println("일단 여까지 왔어 5(게임스타트메솓)");
		
		
		ap.setList(list);
		ap.startAniThread();
		
		//버튼 끝날때까지 비활성화
		btnStart.setEnabled(false);
	}
	
	// ============================================================
	
	//버튼 활성화
	public void gameIsOver(){
		btnStart.setEnabled(true);
	}
	
	
	void EnterWords(){
		String input = tfEntry.getText();
		
		//
		//쓰레드가 돌아가고있을때를 체크하고 돌고있을대만 아래 메소드를
		//실행하고 싶으면 어떻게할까? ==> 몰라 이눔아
		//
		
		if(input == null){	//널이면 리턴
			return;
		}
		ap.matchWord(input);	//텍스트를 받아온 후 보내버린다(비교하러)
		tfEntry.setText("");
	}
	
	
	
	public AcidRainClient(){
		
		setGUI();
		
		
		//클라 시작 즉시 서버에 접속하여 DB와 통신 준비한다
		try{
			s = new Socket(getLocalIP(), 12345);
			oos = new ObjectOutputStream( s.getOutputStream() );
			// 오브젝트로 통신하기위한 magical sequence
			oos.flush();
			ois = new ObjectInputStream( s.getInputStream() );
			
			//wordType 갖고와서 넣어준다
			selectWordTypeName();
			
			////////// READER THREAD 시작한다 //////////
			new ReaderThreadClient(this, ois).start();
			
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
	
	public void exitClient(){
		//프로토콜 보내기용 메세지
		Message msg = new Message();
		msg.setType(9);
		
		try{
			if(oos != null){
				oos.writeObject(msg);
			}
		} catch(IOException e){
			
		} finally{
			if(ois != null){
				try{
					ois.close();
				} catch(IOException e){}
			}
			if(oos != null){
				try{
					ois.close();
				} catch(IOException e){}
			}
			if(s != null){
				try{
					s.close();
				} catch(IOException e){}
			}
		}
		
		f.setVisible(false);
		System.exit(0);
	}
	
	// user 더하기 빼기 업뎃하기 리스트 보여주기
	void addUser(String name){
		users.put(name, 0); //이름과 기본점수 0을 넣는다(l8er 수정)
	}
	
	void deleteUser(String name){
		users.remove(name);
	}
	
	void updateUserName(String oldName, String newName){
		int scoreSaved = users.remove(oldName);
		users.put(newName,  scoreSaved);
	}
	
	void updateUserScore(String name, int newScore){
		users.replace(name, newScore);
	}
	
	void rename(){ //이거랑 update랑 insert랑 연결시켜야되나?
		//nameNeverChange가 true면 insert랑 연결, 아니면 updateName이랑 연결
		
		name = tfUsername.getText().trim();
		if(name.isEmpty()) return;
		
		if(nameNeverChanged){
			//인서트랑 관련있게
			
		} else{
			sendRename(name);
		}
		
		setFrameName(name);
		
		//다 끝났으면 다시 공백으로
		tfUsername.setText("");
	}
	
	public void setFrameName(String name){
		this.name = name;
		f.setTitle(String.format("%s's excitement packed Weak Acid Rain (o^0^)==o", this.name));
		
	}
	
	void sendRename(String name){ //이름업데이트
		try{
			Message msg = new Message();
			msg.setType(22);		// updateUName은 프로토콜 외쳐!EE!
			msg.setNameString(name);
			oos.writeObject(msg);
			
		}catch(IOException e){
			
		}
	}

	
	
	void setGUI(){
		
		f = new JFrame("Weak Adic Rain Game Client (o^0^)==o");
		f.setBounds(new Rectangle(0,0,700,600));
		
		//north panel
		nPanel = new JPanel(new BorderLayout());
		nwPanel = new JPanel(new BorderLayout());
		tfUsername = new JTextField();
		nwPanel.add(new JLabel(" NAME "), BorderLayout.WEST);
		nwPanel.add(tfUsername, BorderLayout.CENTER);
		btnSignUp = new JButton("등록");
		btnSignUp.setActionCommand("I");
		btnSignUp.addActionListener(al);
		
		nPanel.add(nwPanel, BorderLayout.CENTER);
		nPanel.add(btnSignUp, BorderLayout.EAST);
		
		
		
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
		taTypename = new JTextArea();
		ecPanel.add(taTypename);
		
		esPanel = new JPanel(new GridLayout(2, 1));
		esPanel.setBorder(BorderFactory.createLineBorder(new Color(232, 240, 220), 3));
		
		tfTypeSelect = new JTextField();
		esPanel.add(tfTypeSelect);
		esPanel.add(btnStart);
		
		ePanel.add(ecPanel, BorderLayout.CENTER);
		ePanel.add(esPanel, BorderLayout.SOUTH);
		
		
		
		
		
		
		
		
		// add 'em all onto cpanel
//		cPanel.add(taScreen, BorderLayout.CENTER);
		cPanel.add(ap, BorderLayout.CENTER);
		cPanel.add(csPanel, BorderLayout.SOUTH);

		
		// f
		f.add(nPanel, BorderLayout.NORTH);
		f.add(cPanel, BorderLayout.CENTER);
		f.add(ePanel, BorderLayout.EAST);
		
		
		
		f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		f.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				int opNum = JOptionPane.showConfirmDialog(f, "Are you sure you want to exit the game?", 
						"Quit", JOptionPane.OK_CANCEL_OPTION);
				if(opNum == JOptionPane.OK_OPTION){
					exitClient();
				}
			}
			
		});
		f.setVisible(true);
	}
	
	public void printOnMyConsole(String s){
		System.out.println("전달받은 스트링: " + s);
	}
	
	public static void main(String[] args){
		new AcidRainClient(); //
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	
}
