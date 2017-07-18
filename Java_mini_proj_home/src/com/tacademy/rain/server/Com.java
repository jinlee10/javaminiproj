package com.tacademy.rain.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.tacademy.rain.dao.AcidRainDAO;
import com.tacademy.rain.vo.AcidRain;
import com.tacademy.rain.vo.Message;

public class Com extends Thread{
	
	Socket s;
	ObjectInputStream ois;
	ObjectOutputStream oos;
	
	boolean onAir = true;
	
	public Com(Socket s){
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
	
	void insertUser(AcidRain acidrain){
		
		AcidRainDAO dao = new AcidRainDAO();
		dao.insertUser(acidrain);
		
	}
	
	void selectWords(AcidRain acidrain){
		AcidRainDAO dao = new AcidRainDAO();
		
		Message msg = new Message();
		
		msg = dao.selectWords(acidrain);
		
		try{
			oos.writeObject(msg);
		} catch(IOException e){
			System.out.println("서버에서 받아온 acidrain 에러" + e);
		}
	}
	
	void updateUserScore(AcidRain acidrain){
		AcidRainDAO dao = new AcidRainDAO();
		
		dao.updateUserScore(acidrain);
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
		
		try{
			oos.writeObject(msg);
		}catch(IOException e){
			System.out.println("서버에서 받아온 typename에러 : " + e);
		}
	}
	
	
	
	/////////////////////////////////////////////////////////////
	
	public void run(){
		
		Message msg = null;
		
		try{
			while(onAir){
				msg = (Message) ois.readObject();
				
				switch(msg.getType()){
				case 0:
					insertUser(msg.getAcidrain());
					break;
				case 1:
					selectWords(msg.getAcidrain());
					break;
				case 2:
					updateUserScore(msg.getAcidrain());
					break;
				case 3:
					deleteUser(msg.getAcidrain());
					break;
				case 4:
					selectWordTypeName(msg.getAcidrain());
					break;
				case 9:
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
