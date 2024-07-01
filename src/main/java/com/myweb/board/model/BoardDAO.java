package com.myweb.board.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.myweb.util.JdbcUtil;

public class BoardDAO {

	//DAO는 불필요하게 여러개 만들 필요가 없기 때문에 객체가 한개만 생성되도록 
	//singleton형식으로 설계
	
	//1. 나자신의 객체를 1개 생성하고, private을 붙임
	private static BoardDAO instance = new BoardDAO();
	
	//2. 직접 객체를 생성할 수 없도록 생성자에도 private을 붙임
	private BoardDAO() {
		
		//커넥션풀에 사용할 객체를 얻어옴
		try {
			InitialContext init = new InitialContext(); //시작설정 객체
			ds = (DataSource)init.lookup("java:comp/env/jdbc/oracle");
			
		} catch (Exception e) {
			System.out.println("커넥션 풀 에러");
		}
	}
	
	//3. 객체 생성을 요구할때 getter메서드를 사용해서 1번의 객체를 반환
	public static BoardDAO getInstance() {
		return instance;
	}

	///////////////////////////////////////
	//커넥션 풀 객체정보
	private DataSource ds;
	
	
	// 글 등록
	public void regist(String writer,
					   String title,
					   String content) {
		String sql = "INSERT INTO BOARD(BNO, WRITER, TITLE, CONTENT) VALUES (BOARD_SEQ.NEXTVAL, ?, ?, ?)";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			
			// sql 물음표 개수 만큼
			pstmt.setString(1, writer);
			pstmt.setString(2, title);
			pstmt.setString(3, content);
			
			pstmt.executeUpdate(); // 반환이 없으면 여기서 끝
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn, pstmt, null);
		}
		
		
	}

	// 글 목록 - 1행 UserDTO, 여러 행 List<UserDTO>
	public ArrayList<BoardDTO> getList() { // 1행 말고 여러 행일 땐 List에 DTO 타입 담기
		ArrayList<BoardDTO> list = new ArrayList<>();
		
		String sql = "SELECT * FROM BOARD ORDER BY BNO DESC";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) { // 여러 행이라 while, sql 결과는 rs에 담겨 있고 그 안에서 필요한 컬럼만 DTO에 담음
				// 1행에 대한 처리
				// 컬럼을 DTO에 담고, DTO를 List에 추가한다
				
				// 필요한 컬럼만 담는 작업
				int bno = rs.getInt("bno");
				String writer = rs.getString("writer");
				String title = rs.getString("title");
				String content = rs.getString("content");
				Timestamp regdate = rs.getTimestamp("regdate");
				int hit = rs.getInt("hit");
				
				// dto에 행 데이터 담기
				BoardDTO dto = new BoardDTO(bno, writer, title, content, regdate, hit);
				
				// 리스트에 dto 담기
				list.add(dto);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn, pstmt, rs);
		}
		
		return list;		
	}
}
