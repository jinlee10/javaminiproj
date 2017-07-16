package com.tacademy.board.server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.tacademy.board.dao.BoardDAO;
import com.tacademy.board.vo.Board;
import com.tacademy.board.vo.Message;

// 클라이언트와 커뮤니티를 하는 녀석을 만들어보았습니다.
// 열심히 쓰레드를 돌려주시기 바랍니다.
public class Com extends Thread {

	Socket s;
	ObjectInputStream ois;
	ObjectOutputStream oos;

	public Com() {}
	public Com(Socket s){
		this.s = s;

		try {
			ois = new ObjectInputStream( s.getInputStream() );
			oos = new ObjectOutputStream( s.getOutputStream() );
			//이것만 해주면 magical sequence 내부적으로 보내서 해결댐
			// 최초 접속시 접속을 알리는 그것인가! 조금 찾아보면 좋겠다.
			// 4byte 'magical' squence AC ED 00 05 를 보내야 네트워크에서 통신이 가능하다.
			oos.flush();
		} catch (IOException e) {
			System.out.println("com 생성자 오류" + e);
		}


	}
	
	void insertBoard(Board board) {
		
		// 인자로 전달받은 board에 이미 값이 있다.
		
		BoardDAO dao = new BoardDAO();
		dao.insertBoard(board);
	}
	
	void selectBoard(Board board){
		BoardDAO dao = new BoardDAO();
		
		Message msg = new Message();
		
		msg = dao.selectBoard(board); //여기가 null떨어졌다
		
		// arraylist를 그냥보내면 나중에 보내는데이터 스까될수도있다고 주의하라고 하심!!!
		
		//System.out.println("server 가 보내는 list: " + list);
		
		
		
		try {
			oos.writeObject(msg);
		} catch (IOException e) {
			System.out.println("서버에서 받아온 board를 제대로 출력하였다!");
		}
	}
	
	void updateBoard(Board board){
		BoardDAO dao = new BoardDAO();
		
		dao.updateBoard(board);
	}
	
	void deleteBoard(Board board){
		BoardDAO dao = new BoardDAO();
		
		dao.deleteBoard(board);
	}
	
	public void run() {
		//상대방이 글쓰기하면 받아서써주고.....으워.....반복....
		
		
		Message msg = null;
		try {
			while( true ) {
				//던지면 받어서 작업하고 던져주고
				msg = (Message) ois.readObject(); // 얘는 리턴타입이 obj인데 형변환해야지 마!
				
				switch( msg.getType() ) {	// 0: insert 1:select 2:update 3:delete
				case 0 : // dbinsert
					insertBoard(msg.getBoard());
					break;
				case 1:
					selectBoard(msg.getBoard());
					break;
				case 2:
					updateBoard(msg.getBoard());
					break;
				case 3:
					deleteBoard(msg.getBoard());
					break;
				}
			}

		} catch (IOException e) {
			System.out.println(e);
		} catch (ClassNotFoundException e) {
			System.out.println(e);
		}



	}

}
