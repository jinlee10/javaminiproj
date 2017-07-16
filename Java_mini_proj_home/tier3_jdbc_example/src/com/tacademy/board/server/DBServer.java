package com.tacademy.board.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class DBServer {

	// 3티어로 돌려보자!
	public DBServer() {

		ServerSocket ss = null;

		try {
			ss = new ServerSocket(12345);
			System.out.println("서버 생성 성공");
		} catch (IOException e) {
			System.out.println("ServerSocketError" + e);
			// 서버 소켓 생성이실패하면 메서드를 여기서 종료한다.
			return;
		}

		Socket s = null;
		while( true ) {
			
			try {
				System.out.println("서버가 접속을 대기중입니다.");
				s = ss.accept();
				System.out.println("누군가 서버와 접속했습니다 . . . . : " + s.getInetAddress().getHostAddress());
				// 연결에 성공했으니 연결한 녀석은 따로 놀게 해주자.( 쓰. 레. 드. )
				new Com(s).start();
			} catch (IOException e) {
				System.out.println("소켓 생성 실패 : " + e);
			}
			
		}
	}

	public static void main(String[] args) {
		new DBServer();
	}

}
