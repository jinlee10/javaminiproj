package com.tacademy.rain.client;

import java.io.IOException;
import java.io.ObjectInputStream;

import com.tacademy.rain.vo.Message;

public class ReaderThreadClient extends Thread{
	private AcidRainClient client;
	private ObjectInputStream ois;
	
	//생-성-자
	public ReaderThreadClient(AcidRainClient client, ObjectInputStream ois){
		this.client = client;
		this.ois = ois;
	}
	
	
	//내 단계에서 서버랑 주고받고 할란다!
	public void run(){
		
		Message msg = null;
		int msgType = 0;
		
		try{
			while(true){
				msg = (Message) ois.readObject();
				
				msgType = msg.getType();
				
				// 5 유저리스트 refresh, 6: userScore refresh
				switch(msgType){
				case 5:
					
					break;
				}// switch문 끝
			}// while문 끝
		} catch(IOException e){
			System.out.println("readerThread error: " + e);
		} catch (ClassNotFoundException e) {
			System.out.println("Msg readObject error: " + e);
		}
		
		
		
		
	}
	
	
}
