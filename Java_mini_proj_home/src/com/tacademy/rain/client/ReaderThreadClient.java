package com.tacademy.rain.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import com.tacademy.rain.vo.AcidRain;
import com.tacademy.rain.vo.Message;

public class ReaderThreadClient extends Thread{
	private AcidRainClient client;
	private ObjectInputStream ois;
	
	//생-성-자
	public ReaderThreadClient(AcidRainClient client, ObjectInputStream ois){
		this.client = client;
		this.ois = ois;
	}
	
	//////////////////////////////////////////////////////////////
	
	//			C 	R	U	D
	
	//////////////////////////////////////////////////////////////
	
	
//	ArrayList<String> selectWords(){
//		// 0: insert 1:select 2:update 3:delete
//		int typeidx = 0;
//		
//		for(int i = 0; i < typeList.size(); i++){
//			if(tfTypeSelect.getText().equals(typeList.get(i))){
//				typeidx = i + 1;
//			}
//		}
//		
//		AcidRain acidrain = new AcidRain();
//		
//		acidrain.setTypeidx(1); //select
//		acidrain.setTypeidx(typeidx);
//		
//		Message msg = new Message();
//		msg.setType(1);
//		msg.setAcidrain(acidrain);
//		
//		try{
//			oos.writeObject(msg);
//			System.out.println("MSG sent well!");
//			
//			msg = (Message) ois.readObject();
//			
//			ArrayList<AcidRain> alist = msg.getList();
//			
//			System.out.println("list size: " + alist.size());
//			
//			for(int i = 0; i < alist.size(); i++){
//				list.add(i, alist.get(i).getWord());
//			}
//			
//			//워드를 받았으면 
//		}catch(IOException e){
//			System.out.println("MSG sent error: " + e);
//		}catch(ClassNotFoundException e){
//			System.out.println("MSG receive error(select): " + e);
//		}
//		
//
//		
//		System.out.println("일단 여까지 왔어 4");
//		return list;
//	}
	
	public ArrayList<String> selectWords(Message msg){
		System.out.println("readerTHread의 selectWords프로토콜타고까지 왔다 1");
		
		Message msgTemp = new Message();
		ArrayList<AcidRain> aList = null;
		ArrayList<String> tempWordList = null;
		
		//
		msgTemp = msg;
		
		aList = msgTemp.getList();
		tempWordList = new ArrayList<String>();
		
		System.out.println("list size: " + aList.size());
		
		for(int i = 0; i < aList.size(); i++){
			tempWordList.add(aList.get(i).getWord()); //템프 string리스트 만들고 값 받는다
		}
		
		
		
		return tempWordList;
	}
	
	
	//내 단계에서 서버랑 주고받고 할란다!
	public void run(){
		
		Message msg = null;
		ArrayList<String> wordList = null;
		int msgType = 0;
		try{
			while(true){
				msg = (Message) ois.readObject();
				
				msgType = msg.getType();
				System.out.println("읽었으면 msgType: " + msgType);
				// 5 유저리스트 refresh, 6: userScore refresh
				switch(msgType){
				case 101:
					System.out.println("protocol 101 received!");
					wordList = selectWords(msg);
					//무사히 왔으면 클라 리스트에 넣어줘라
					System.out.println("단어들을 wList에 집어넣습니다");
					client.assignWordList(wordList);
					System.out.println("단어들을 wList에 집어넣었나봅니다");
					break;
				case 11:
					String[] nameList = msg.getuListString().split(",");
					System.out.println(nameList);
					client.showUserList(nameList);
					
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
