package com.tacademy.board.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.tacademy.board.dao.BoardDAO;
import com.tacademy.board.vo.Board;
import com.tacademy.board.vo.Message;

public class Com extends Thread{
	
	Socket s;
	ObjectInputStream ois = null;	//객체입력객체
	ObjectOutputStream oos = null;	//객체출력객체
	
	public Com(Socket s){
		this.s = s;
		//옛날에는 DataInput/OutputStream썼는데 이제는..
		
		//소켓에서 입력용, 출력용
		try{
			ois = new ObjectInputStream(s.getInputStream()); // :D!!!
			oos = new ObjectOutputStream(s.getOutputStream());	//얘네 필터야~
			oos.flush();	//이것만 해주면 magical sequence 내부적으로 보내서 해결댐
		}catch(IOException e){
			System.out.println("com 생성자 오류: " + e); // 항상 디버깅을 염두에두고 하세요d.d
		}
	}
	
	void insertBoard(Board board){	//보드를 받아야지
		// DB연동 (3티어방식이라 새로워!!!)
		// LOBS
		// 
		//Android: Maven 최신정보가있아면 다운받아 사용받을수잇게 다운받아준다
		// 
		BoardDAO dao = new BoardDAO();	//자이제 인서트하자
		
		dao.insertBoard(board); //
		
		
		
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
	
	public void run(){
		//상대방이 글쓰기하면 받아서써주고.....으워.....반복....
		
		Message msg = null;	//메세지로 주거니받거니할고야
		
		try{
			while(true){	//상대방이 계속 뭘 던지면 계속 반복해줄고야
				//던지면 받어서 작업하고 던져주고
				msg = (Message) ois.readObject(); // 얘는 리턴타입이 obj인데 형변환해야지 마!
				
				switch(msg.getType()){		// 0: insert 1:select 2:update 3:delete
				case 0:
					//db에 insert하겠다구
					insertBoard(msg.getBoard());		//이동만하고있는거야 (클라이언트에 받은거 서버한테내가주는거야)
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
			
		} catch(IOException e){		//오류나면 무한반복 않고 걍 꺼지겠습니다.
			System.out.println("com msg 오류: " + e);
		} catch (ClassNotFoundException e) {
			System.out.println("class notfound exp : " + e);
		}
		
	}
	
}
