package src.com.tacademy.test;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;

import src.com.tacademy.data.AStudent;

public class ATest {
	FileWriter writer;
	
	public ATest(){
		//객체 직렬화, 객체 입출렬
		
		AStudent s1 = new AStudent("JOHN CENA", "U CANT C ME");
		s1.setKor(99);
		s1.setEng(48);
		s1.setMath(76);
		s1.findTotal();
		//s1.findAverage(); 	//l8er 안드서는 public하고 .찍고할수도잇서~
		s1.setPassword("1234");
		
		System.out.println(s1); //overriding안햇으믄...알지?
		
		// 이렇게 해놓으면 콤퓨타 끄면 다 날아가자너...
		// 난 얘네가 남아있길바래 ==> file에 보관하는 eu
		
		// 하나하나 말고 묶어서 보관하고싶어!
		//					=> FileOutputStream!
		
		
		//	I/O는 뭐다? try catch finally 써놓고 고민해^^7^^7^^7
		
		//FileOutputStream fos = null;
		ObjectOutputStream oos = null; 
		
		
		
		
		
		
		
		
		
		
		
		
		try{
			//fos = new FileOutputStream("a.ser"); // *.ser: 아예못읽어들이게	//객체입출력에는 serializable을 구현하는게 의무사항이기때문에 앞의 세글자를줄여서 .ser이라고많이쓴다.
				//fos는 객체쓰는거 없다..byte[] 밖에안된대(T-T)
				//	심지어 객체 출력하는기능없다
				// ---> 답은 바이트 배열로 만드는 것 뿐이다!
				// 혹은 객체출력 filter를 쓰는것도있다
			oos = new ObjectOutputStream(new FileOutputStream("a.ser"));
				//OutputStream의 자식이라 File Reader/Writer는안되다
				//anonymous object기법! 변수없이 생성자를 매개변수로 넣었죠?
			
			oos.writeObject(s1);//WoW!
				//얘는 심지어 DataOutputStream에있는것 primitive type write하는거 있긴한데 쓰지말고 쟤네 쓰려면 dataOutputStream써라 ㅡㅡ
				//얜 writeObject()만쓴다
			
				//어떤 인자 자료형이 Object면 얘 뿐만아니라 얘의 자식들까지도다받는다는뜻이죠?
			
			System.out.println("Write Success"); //야! 야야! 자꾸 찍어보면서 진행을해도록 해
			
			
		//NotSerealizableException //객체직렬화안댓대SSSsss	
			
		//안드에서는 Passarable인가 막 그런거있는데 복잡하대
		//implements Serializable한대 썜은ㅎ
			
		//String은 객첸데 WriteUTF해서 됐었잖아? 왜된줄앎?
		//String API가보면 얜 이미 Serializable 구현한거 ㅋㅋㅋ
		//Calendar도됨ㅋㅋ   Long 래퍼클라스나 Button클래스에도 없다
		
		//객체입출력의 대상이된다 ==> Serializable 클래스를 implements한다
		//data관리만하고 말거면 할필요는없겠지?
			System.out.println();//디버깅
			
		}catch(IOException e){ //try안에 암것도 안던지면 에라보여주긴하는데 이건 이클립스에서 봤을때 불필요한거 썼다고 경고떨구는거지 컴파일에라는아니다(패키지 이름 위에 쓰면 되는건데 이클립스에서는 만드는것처럼 보여주자너 ㅎㅎ 암튼 이클립스 자체에서 넘나 중요하다구 에라띄우는거)
			System.out.println("error:" + e); //디버깅용이죠 이제 sysout은?
		}finally{
			if(oos != null){
				try{
					oos.close();
				}catch(IOException e){}
			}
		}
		
		
		
		
		
		
		
		
	}
	
	public static void main(String[] args){
		new ATest();
	}
}
