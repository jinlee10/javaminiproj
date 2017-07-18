package com.tacademy.rain.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.tacademy.rain.vo.Message;

public class DBServer {
	
	//매번 만들어지는 클라이언트에 대응하는 소켓을 가진 Comm쓰레드
	//묶어서 관리할 객체
	ArrayList<Com> comList = new ArrayList<Com>();
	ServerSocket ss;
	Socket s;
	
	//유저리스트 구하기
	public String getUserList(){
		String userList = "";
		StringBuilder sb = new StringBuilder();
		
		for(Com cm : comList){
			sb.append(cm.name).append(","); //반복문이죠?
		}
		
		userList = sb.toString();
		userList.substring(0, userList.length() - 1); //마지막한자에서 1뺀만큼( , 이거)
		
		return userList;
	}
	
	
	// 유저리스트 쏘기
	public void sendUserList(Com com){
		// 서버 -> 클라에서 보낼때 약속을 하자
		
		// 각각 com을통해 보내야겠지? 뭘? 사용자 정보 리스트!
		com.sendMessage(11, getUserList());
	}
	
	// 유저 나갔을떄 arraylist에서 com객체 제거하기
	public void exitcom(Com com){
		comList.remove(com);
	}
	
	// 모두에게 뭔가 보내기 (원본은 protocol, string이었다)
	public void sendMsg2All(int protocol, String str){
		for(Com cm : comList){
			cm.sendMessage(6, str);
		}
	}
	
	public void sendUserList2All(int protocol, String str){
		String ul = getUserList();
		for(Com cm : comList){
			cm.sendMessage(11, ul);
		}
	}
	
	
	//3티어로 돌려보자~!
	public DBServer(){
		ss = null;
		
		try{
			ss = new ServerSocket(12345);
			System.out.println("서버 생성 성공!");
		}catch(IOException e){
			System.out.println("서버 소켓 에러: " + e);
			return;	//서버소켓 생성 실패시 메소드 완전 종료
		}
		
		s = null;
		
		while(true){ 
			
			//클라 올때까지 기다리자구
			Com com = null;
			String remoteIP = "";
			try{
				System.out.println("서버가 접속을 대기중이다");
				s = ss.accept();
				remoteIP = s.getInetAddress().getHostAddress();
				
				System.out.println(remoteIP + "가 서버에 접속하였다");
				
				
				// 접속이 이루어진 부분
				// 주거니~ 받거니~ 주거니~ 받거니~
				
				//통신할거 쓰레드 분리해야지
				com = new Com(this, s);
				comList.add(com);	//리스트에 한놈 더한다
				com.start();	//컴 쓰레드 하나 만들엇으니 동작하쇼
				
			} catch(IOException e){
				System.out.println("소켓 생성 실패" + e);
			}
		}
	}
	
	public static void main(String[] args){
		new DBServer();
	}
	
	
}
