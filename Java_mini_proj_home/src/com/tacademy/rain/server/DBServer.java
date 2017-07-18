package com.tacademy.rain.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class DBServer {
	
	
	//3티어로 돌려보자~!
	public DBServer(){
		ServerSocket ss = null;
		
		try{
			ss = new ServerSocket(12345);
			System.out.println("서버 생성 성공!");
		}catch(IOException e){
			System.out.println("서버 소켓 에러: " + e);
			return;	//서버소켓 생성 실패시 메소드 완전 종료
		}
		
		Socket s = null;
		
		while(true){ 
			try{
				System.out.println("서버가 접속을 대기중이다");
				s = ss.accept();
				System.out.println("누군가 서버와 접속했다");
				
				new Com(s).start();	//컴 쓰레드 start한다
			} catch(IOException e){
				System.out.println("소켓 생성 실패" + e);
			}
		}
	}
	
	public static void main(String[] args){
		new DBServer();
	}
	
	
}
