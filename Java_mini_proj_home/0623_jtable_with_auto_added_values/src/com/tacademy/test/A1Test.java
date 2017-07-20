package src.com.tacademy.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import src.com.tacademy.data.AStudent;

public class A1Test {
	ObjectInputStream ois;	
	
	public A1Test(){
		//filter계열은 노드가 꼭 잇서야대!
		ois = null;
		AStudent s = null;
		
		//하나의 객체단위로 입출력하고싶다: implements Serializable
		
		try{
			ois = new ObjectInputStream(new FileInputStream("a.ser"));
			Object obj = ois.readObject(); //객체가 있다고 생각하고 readObject로 읽어오겠다
			if(obj instanceof AStudent){ //AStudent일때만해겠다(final uid설정해두면 변경이되더라도 같은클래스로 인식)
				s = (AStudent)obj; //달라지면 내부 serial version uid바뀌니까 그거 비교하면서 맞는지안맞는지 보고, 다르면 캐스팅이 안된다
				System.out.println(s);
			}
			 
		}catch(IOException e){
		}catch(ClassNotFoundException e) {
		}finally{
			if(ois != null){
				try {
					ois.close();
				} catch (IOException e) {}
			}
		}
	}
	
	
	
	public static void main(String[] args){
		new A1Test();
	}

}