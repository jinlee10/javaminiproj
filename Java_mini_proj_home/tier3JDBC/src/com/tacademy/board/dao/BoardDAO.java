package com.tacademy.board.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tacademy.board.vo.Board;
import com.tacademy.board.vo.Message;

public class BoardDAO {
	
	String insertSQL = "insert into board(brdid, brdtitle, brdtext, ip) "
			+ "values(?, ?, ?, ?);";	// single quotation도 다 없에야한다
	
	String selectSQL = "select brdtext from board where brdid = ?";
	
	String updateSQL = "update board set brdtitle = ? where brdid = ?";
	
	String deleteSQL = "delete from board where brdid = ?";
	
	public void insertBoard(Board board){	//자체를 넣어버려
						//  Board vo: 보드는 데이터니까 이렇게쓰는사람도많다
		
		Connection conn = null; //import할때 java.sql.Connection해야대!!!!
		PreparedStatement stmt = null;
		
		//jdbc하면 반드시 finally로 닫아줘라!
		
		try{
			conn = JDBCUtil.getConnection(); //유틸이갖고있는 커넥션 갖고와
			stmt = conn.prepareStatement(insertSQL);	//인자로 insert구문을줘서 stmt얻기
			stmt.setString(1, board.getBrdid());
			stmt.setString(2, board.getBrdtitle());
			stmt.setString(3, board.getBrdtext());
			stmt.setString(4, board.getIp());
			
			// execute : create, alter, drp같은 ddl --> 이건잘안함. 걍 db에할것..구조짜기 등
			// exe query : select하나만	
			// exe update: insert, delete, update
			
			int cnt = stmt.executeUpdate();	//숫자하나나온다->영향받은 레코드의 갯수
			
			System.out.println("insert" + (cnt == 1 ? "성공" : "실패"));
			
		}catch(SQLException e){
			System.out.println("board insert error: " + e); //디버깅용
		}finally{
			JDBCUtil.close(stmt, conn); //이걸루다가 하면 되네!SSSsss	//메소드 참말로 좋다야!
			// insert도 열닫, delete도 여닫, update도 여닫!
		}
		
		
		
		
		//delete update select는 우리가 만들자
		
		
		// 자꾸만 소스가 숨어들지? 메소드단위로 블럭돼있어서..
		
		
		
		
		
	}
	
	public Message selectBoard(Board board){

		Connection conn = null; //import할때 java.sql.Connection해야대!!!!
		PreparedStatement stmt = null;

		ArrayList<Board> list = null;	// 야!!!!!!! 만들어야ㅣㅈ1`!!!!!!
		Board brd = null;
		
		//message에 담아서 보내자 얘두!
		
		Message msg = null;

		try{
			conn = JDBCUtil.getConnection(); //유틸이갖고있는 커넥션 갖고와
			stmt = conn.prepareStatement(selectSQL);	//인자로 insert구문을줘서 stmt얻기
			stmt.setString(1, board.getBrdid());
			
			// exe query : select하나만	
			// exe update: insert, delete, update
			
			ResultSet rst = stmt.executeQuery();	//숫자하나나온다->영향받은 레코드의 갯수
			
			System.out.println("rst = " + (rst == null  ? "null" : "not null"));
			msg = new Message();
			list = new ArrayList<Board>();
			
			while(rst.next()){
				brd = new Board();
				brd.setBrbtext(rst.getString("brdtext"));
				list.add(brd);
			}
			
			msg.setList(list);
			
		}catch(SQLException e){
			System.out.println("board select error: " + e); //디버깅용
		}finally{
			JDBCUtil.close(stmt, conn); //이걸루다가 하면 되네!SSSsss	//메소드 참말로 좋다야!
			// insert도 열닫, delete도 여닫, update도 여닫!
		}
		
		return msg;
	}

	
	public void updateBoard(Board board){

		Connection conn = null; //import할때 java.sql.Connection해야대!!!!
		PreparedStatement stmt = null;
		

		try{
			conn = JDBCUtil.getConnection(); //유틸이갖고있는 커넥션 갖고와
			stmt = conn.prepareStatement(updateSQL);	//인자로 insert구문을줘서 stmt얻기
			stmt.setString(1, board.getBrdtitle());
			stmt.setString(2, board.getBrdid());
			
			// exe query : select하나만	
			// exe update: insert, delete, update
			
			int cnt = stmt.executeUpdate();	//숫자하나나온다->영향받은 레코드의 갯수
			
			System.out.println("update" + (cnt == 0 ? "실패" : "성공"));
			
		}catch(SQLException e){
			System.out.println("board update error: " + e); //디버깅용
		}finally{
			JDBCUtil.close(stmt, conn); //이걸루다가 하면 되네!SSSsss	//메소드 참말로 좋다야!
			// insert도 열닫, delete도 여닫, update도 여닫!
		}
	}
	
	public void deleteBoard(Board board){

		Connection conn = null; //import할때 java.sql.Connection해야대!!!!
		PreparedStatement stmt = null;


		try{
			conn = JDBCUtil.getConnection(); //유틸이갖고있는 커넥션 갖고와
			stmt = conn.prepareStatement(deleteSQL);	//인자로 insert구문을줘서 stmt얻기
			stmt.setString(1, board.getBrdid());
			
			// exe query : select하나만	
			// exe update: insert, delete, update
			
			int cnt = stmt.executeUpdate();	//숫자하나나온다->영향받은 레코드의 갯수
			
			System.out.println("delete" + (cnt == 0 ? "실패" : "성공"));
			
		}catch(SQLException e){
			System.out.println("board delete error: " + e); //디버깅용
		}finally{
			JDBCUtil.close(stmt, conn); //이걸루다가 하면 되네!SSSsss	//메소드 참말로 좋다야!
			// insert도 열닫, delete도 여닫, update도 여닫!
		}
	}
}
