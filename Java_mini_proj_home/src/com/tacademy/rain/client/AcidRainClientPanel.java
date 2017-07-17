package com.tacademy.rain.client;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

public class AcidRainClientPanel extends JPanel{
	
	ArrayList<String> list;
	
	int xCord, yCord = -15;	//초기값 
	
	Random random = new Random(); //랜덤한 자리선정을위한..
	
	boolean onDrawMode = false;
	
	public AcidRainClientPanel(ThreadTest tt){
		
	}
	
}
