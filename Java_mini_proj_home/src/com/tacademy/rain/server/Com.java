package com.tacademy.rain.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.tacademy.rain.dao.AcidRainDAO;
import com.tacademy.rain.vo.AcidRain;
import com.tacademy.rain.vo.Message;

public class Com extends Thread{
	
	DBServer server;
	Socket s;
	ObjectInputStream ois;
	ObjectOutputStream oos;
	
	//user 닉네임을 위해서
	String name;
	static int nameCnt = 0;	//이 객체가 여러번 만들어져도 static이라 하나야
	
	boolean onAir = true;
	
	public Com(DBServer server, Socket s){
		name = "user" + ++nameCnt; //default 네임 표시용
		this.server = server;
		this.s = s;
		
		try{
			ois = new ObjectInputStream( s.getInputStream() );
			oos = new ObjectOutputStream( s.getOutputStream() );
			//이것만 해주면 magical sequence 내부적으로 보내서 해결댐
			// 최초 접속시 접속을 알리는 그것인가! 조금 찾아보면 좋겠다.
			// 4byte 'magical' squence AC ED 00 05 를 보내야 네트워크에서 통신이 가능하다.
			oos.flush();
			
		} catch (IOException e){
			System.out.println("Com 생성자 오류: " + e);
		}
	}
	
	// 각각 클라로 쏴주는 부분 /////////////////////////////////////
	
	public void sendMessage(int msgType, String str){
		try{
			//보내기용 템프 메세지 객체
			Message message = new Message();
				
			message.setType(msgType);
			message.setuListString(str);
			oos.writeObject(message);
			
		}catch(IOException e){
			System.out.println("sendMessage 에러: " + e);
		}
	}
	
	// =========================================================
	// 				C	R	U	D
	// =========================================================
	
	void insertUser(AcidRain acidrain){
		
		AcidRainDAO dao = new AcidRainDAO();
		dao.insertUser(acidrain);
		
	}
	
	void selectWords(AcidRain acidrain){
		AcidRainDAO dao = new AcidRainDAO();
		
		Message msg = new Message();
		msg = dao.selectWords(acidrain);
		msg.setType(101);//select
		
		System.out.println("msg는 X맨이..." + (msg == null ? "맞았습니다!" : "아니었습니다!"));
		
		try{
			oos.writeObject(msg);
			Thread.sleep(1000);
		} catch(IOException e){
			System.out.println("서버에서 받아온 acidrain 에러" + e);
		} catch (InterruptedException e) {
		}
	}
	
	void updateUserScore(AcidRain acidrain){
		AcidRainDAO dao = new AcidRainDAO();
		
		dao.updateUserScore(acidrain);
	}
	
	void updateUserName(AcidRain acidrain, String newName){
		AcidRainDAO dao = new AcidRainDAO();
		
		dao.updateUserName(acidrain, newName);
	}
	
	void deleteUser(AcidRain acidrain){
		AcidRainDAO dao = new AcidRainDAO();
		
		dao.deleteUser(acidrain);
	}
	
	void selectWordTypeName(AcidRain acidrain){
		AcidRainDAO dao = new AcidRainDAO();
		System.out.println("com도 여기까지 왔다 1");
		
		Message msg = null;
		
		msg = dao.selectWordTypeName(acidrain);

		System.out.println("sql selecttypename 메소드 안이야 1");
		try{
			oos.writeObject(msg);
			System.out.println(msg.getList().size());
			System.out.println("sql selecttypename 메소드 안이야 2");
		}catch(IOException e){
			System.out.println("서버에서 받아온 typename에러 : " + e);
		}
	}
	
	
	
	/////////////////////////////////////////////////////////////
	
	public void run(){
		
		Message msg = null;
		int msgType = 0;
		
		//시작하자마자 이름 보내고 싶다
		//server.sendUserList(this);
		
		//server.sendUserList2All(11);
		
		try{
			while(onAir){
				msg = (Message) ois.readObject();
				msgType = msg.getType();
				
				switch(msgType){
				case 0:
					insertUser(msg.getAcidrain());
					break;
				case 1:
					selectWords(msg.getAcidrain());
					break;
				case 2:
					updateUserScore(msg.getAcidrain());
					break;
				case 22:
					updateUserName(msg.getAcidrain(), msg.getNameString());
//					server.sendUserList2All(11);
					break;
				case 3:
					deleteUser(msg.getAcidrain());
					break;
				case 4:
					selectWordTypeName(msg.getAcidrain());
//					server.sendUserList(this);
					server.sendUserList2All(11);
					break;
				case 9:
					server.exitcom(this);
					server.sendUserList2All(11);
					onAir = false;	//while을 벗어나야 catch걸리기전에 꺼버리지
					break;
				}//switch문 끝
				
			}// while문 끝
			

			if(oos != null){
				try{
					oos.close();	//출력 객체 종료
				}catch(IOException e){
				}
			}

			if(ois != null){
				try{
					ois.close();	//입력 객체 종료
				}catch(IOException e){
				}
			}
			if(s != null){
				try{
					s.close();		//소켓 객체 종료
				}catch(IOException e){
				}
			}
			

		} catch (IOException e) {
			System.out.println(e);
		} catch (ClassNotFoundException e) {
			System.out.println(e);
		}
		
	}
	
	
}
