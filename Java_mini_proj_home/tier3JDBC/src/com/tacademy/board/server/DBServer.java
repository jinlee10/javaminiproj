package com.tacademy.board.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class DBServer {
	
	public DBServer(){
		
		ServerSocket ss = null;
		try{
			System.out.println("서버 생성 성공1");
			ss = new ServerSocket(12345);	// 포트알려주구가~
			//ss.accept();	
			System.out.println("서버 생성 성공2");
		}catch(IOException e){
			System.out.println("서버 생성 실패: " + e); //찍어보는습관갖자
			
			return;	//아예 끊어버려
		}
		
		Socket s = null;	// 실제입출력은 소켓이하게됩니다 접속은 ss가기달리구
		
		while(true){
			try {
				s = ss.accept();			// 올떄까지 기다릴꼐^오^			// checked exception
				
				System.out.println("접속한 상대 정보: " + s.getInetAddress().getHostAddress());
				new Com(s).start();//한사람이 접속하면 관리할객체만들어서 얘가 쓰레드라는 전제하에 start시킨다
				
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}
	
	public static void main(String[] args){
		new DBServer();
	}
}
