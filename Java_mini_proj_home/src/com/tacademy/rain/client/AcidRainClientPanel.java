package com.tacademy.rain.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;

public class AcidRainClientPanel extends JPanel{
	
	private ArrayList<String> typenameList;	//타입이름 저장용 리스트
	private ArrayList<String> list;	//단어 저장용 리스트
	private ArrayList<DrawWord> wList;	//단어 + x, y coord 저장용 리스트
	
	
	private int xCoord, yCoord = -15;	//초기값 
	private int level = 1; //레벨 (1~3까지있는걸로 하자 or not exist)
	
	private Random random = new Random(); //랜덤한 자리선정을위한..
	
	Font font = new Font("batang", Font.PLAIN, 22);
	
	//애니메이션 이너쓰레드 돌리기용
	private boolean onAir = false;
	private AniThread at;
	private DrawWord word;
	private int deltaY;
	private Color c = Color.white;
	
	//쓰레드 종료용
	private int wordCnt;	//DrawWord 갯수 카운트용
	private AcidRainClient client;
	
	
	//생성자
	public AcidRainClientPanel(AcidRainClient client){
		 this.client = client;
	}
	
	public void setList(ArrayList<String> list){
		wList = new ArrayList<DrawWord>();
		
		System.out.println("list: " + (list == null ? "null" : "not null"));
		for(int i = 0; i < list.size(); i++){
			System.out.println(list.get(i));
		}
		
		this.list = list;
		
		testAssignWList();
	}
	
	
	//애니메이션 쓰레드
	public void startAniThread(){
		if(!onAir){
			onAir = true;
			at = new AniThread();
			
			at.start();
		}else{
			onAir = !onAir;
		}
	}
	
	//list 비교용
	public void matchWord(String s){
		for(int i = 0; i < wList.size(); i++){
			if(s.equals(wList.get(i).getText())){
				wList.remove(i);
				System.out.println(s + " 입력!");
			}
			checkEmptyList();
		}
	}
	
	//바닥에 닿으면?
	public void wordTouchedHeight(){
		for(int i = 0; i < wList.size(); i++){
			if(wList.get(i).getY() >= getHeight()){
				System.out.println(wList.get(i).getText()+ " 땅에 닿음!");
				wList.remove(i);
			}
			checkEmptyList();
		}
	}
	
	
	
	//list empty 테스트용
	public void checkEmptyList(){
		// 리스트 없을시 리턴
		if(wList.isEmpty()){
			System.out.println("텅빔");
			repaint();
			client.gameIsOver();
			onAir = !onAir;
		}
	}
	
	public void testAssignWList(){
		for(int i = 0; i < this.list.size(); i++){
			xCoord = random.nextInt(500);
			yCoord = random.nextInt(600) - 600; //맞나?
			deltaY = random.nextInt(50) + 20;
			
			//alignment : 정렬
			
			word = new DrawWord(xCoord, yCoord, this.list.get(i), deltaY);
			wList.add(word);
			
			System.out.println(wList.get(i).getText());
		}			
	}
	
	class AniThread extends Thread{
		
		int dx;
		int dy = 20;
		
		public AniThread(){
		}
		
		public void run(){	//여기서는 내려주는거랑 계속 그려주기만 하면됨
			while(onAir){
				drawWords(20);

				//리스트 수량 확인
				wordTouchedHeight();
				
				repaint();
				
				try{
					sleep(500);
				} catch(InterruptedException e){}
				
			}
		}
		
	}
	
	public void drawWords(int dy){
		for(int i = 0; i < wList.size(); i++){
			DrawWord temp = wList.get(i);
			temp.yAxisMover();
			wList.set(i, temp);
		}
	}
	
	void setGraphicsSetting(Graphics g){
		g.setColor(c);
		g.fillRect(0, 0, getWidth(), getHeight());
	}
	
	// =============   페  인  트  ================
	@Override
	public void paint(Graphics g) {
		setGraphicsSetting(g);
		
		g.setFont(font);
		g.setColor(new Color(200, 170, 220));
		if(wList != null){
			for(int i = 0; i < wList.size(); i++){
				g.drawString(wList.get(i).getText(), 
						wList.get(i).getX(), wList.get(i).getY());
			}
		}
		
		//super.paint(g);
	}
	
	@Override
	public void update(Graphics g) {
		
		paint(g);	//내가 오버라이딩 하면 호출해야 그림이 그려진다
	}
	
	// ------------------------------------------
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public ArrayList<String> getList() {
		return list;
	}

	
	
	
	
}
