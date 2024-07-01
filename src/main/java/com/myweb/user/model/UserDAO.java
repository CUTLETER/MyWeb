package com.myweb.user.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Period;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.myweb.util.JdbcUtil;

import oracle.jdbc.proxy.annotation.Pre;

public class UserDAO {
	
	private static UserDAO instance = new UserDAO();
	
	private UserDAO() {
		
		// 커넥션 풀에 사용할 객체를 얻어오기
		try {
			InitialContext init = new InitialContext(); // 시작 설정 객체
			ds = (DataSource)init.lookup("java:comp/env/jdbc/oracle");
			
		} catch (Exception e) {
			System.out.println("커넥션 풀 에러");
		}
		
		
	}
	
	// 3. 객체 생성을 요구할 때 getter 메소드를 사용해서 1번의 객체를 반환시키기
	public static UserDAO getInstance() {
		return instance;
	}
	
	
	///////////////////////////////////////////////////////////////////
	//커넥션 풀 객체 정보
	private DataSource ds;
	
	
	//아이디 중복 검사하는 메소드
	public int findUser(String id) {
		int result = 0;
		
		String sql = "SELECT * FROM USERS WHERE ID = ?";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			
			if (rs.next()) { // 다음 행이 있다? 유저가 있다
				result = 1; // 유저 있단 걸 반환되는 변수에 담아냄
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}	finally {
			JdbcUtil.close(conn, pstmt, rs); // 코드 쓰기 편하고자 객체 닫는 메소드 따로 만들어놓음
		}
		
		
		return result;
	}
	
	
	// 회원가입 메소드
	public void insertUser(String id, 
						   String pw, 
						   String name, 
						   String email, 
						   String gender) {
		String sql = "INSERT INTO USERS(ID, PW, NAME, EMAIL, GENDER) VALUES (?, ?, ?, ?, ?)";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		//ResultSet은 필요 없음 SELECT 구문이 아니라서
		
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			
			// 물음표에 순서대로 들어갈 값
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			pstmt.setString(3, name);
			pstmt.setString(4, email);
			pstmt.setString(5, gender);
			
			pstmt.executeUpdate(); // insert, update, delete 구문은 executeUpdate() 이거로!
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn, pstmt, null);
		}
	
	}
	

	// 로그인 메소드
	public UserDTO login (String id, String pw) {

		
		UserDTO dto = null;
		
		String sql = "SELECT * FROM USERS WHERE ID = ? AND PW = ?";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
			String name = rs.getString("name");
			String gender = rs.getString("gender");
			String email = rs.getString("email");
			// 비밀번호는 쓸모가 없음
			
			dto = new UserDTO(id, null, name, email, gender, null);
			
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn, pstmt, rs);
		}
		
		return dto;
	}
	
	// 정보 조회 메소드
	public UserDTO getInfo (String id) {
		UserDTO dto = null;
		
		String sql = "SELECT * FROM USERS WHERE ID = ?";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id); // select문의 첫번째 물음표 값에 id 대입
			rs = pstmt.executeQuery(); // SELECT한 값을 resultset 객체 타입에 저장시킴
			
			// resultset에 저장된 값을 자바 변수에다 저장시킴
			if(rs.next()) {
				String pw = rs.getString("pw");
				String name = rs.getString("name");
				String email = rs.getString("email");
				String gender = rs.getString("gender");
				
				dto = new UserDTO(id, pw, name, email, gender, null);
				
//				UserDTO dto = new UserDTO();
//				dto.setId(id);
//				dto.setPw(pw);
//				dto.setName(name);
//				dto.setEmail(email);
//				dto.setGender(gender);
				
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn, pstmt, rs);
		}
		
		return dto;
	}

	
	// 정보 수정 메소드  - 성공 시 1 반환 , 실패 시 0 반환
	public int update(UserDTO dto) { // 매개변수 dto
		int result = 0; // 실패 시 0 대입
		
		String sql = "UPDATE USERS SET PW = ?, NAME = ?, EMAIL = ?, GENDER = ? WHERE ID = ?";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getPw());
			pstmt.setString(2, dto.getName());
			pstmt.setString(3, dto.getEmail());
			pstmt.setString(4, dto.getGender());
			pstmt.setString(5, dto.getId()); // sql문의 물음표 값 순서대로
			
			result = pstmt.executeUpdate(); // insert, update, delete 한정
//			pstmt.executeUpdate()는 성공하면 정수로 행의 개수 반환, 실패하면 0 반환	
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn, pstmt, null);
		}
		
		return result;
	}
	
	
	/*
	 * public UserDTO delete (String id, String pw) { // 로그인 메소드와 일부 중복됨 -> 좋지 못함 (DAO는 재활용이 장점)
	 * 
	 * UserDTO dto = null;
	 * 
	 * String sql1 = "SELECT * FROM USERS WHERE ID = ? AND PW = ?"; String sql2 =
	 * "DELETE FROM USERS WHERE ID = ? AND PW = ?";
	 * 
	 * Connection conn = null; PreparedStatement pstmt = null; ResultSet rs = null;
	 * 
	 * try { conn = ds.getConnection(); pstmt = conn.prepareStatement(sql1);
	 * pstmt.setString(1, id); pstmt.setString(2, pw); rs = pstmt.executeQuery();
	 * 
	 * if (rs.next()) { String name = rs.getString("name"); String email =
	 * rs.getString("email"); String gender = rs.getString("gender");
	 * 
	 * dto = new UserDTO(id, pw, name, email, gender, null); }
	 * 
	 * if (dto != null) { pstmt = conn.prepareStatement(sql2);
	 * 
	 * pstmt.setString(1, id); pstmt.setString(2, pw);
	 * 
	 * pstmt.executeUpdate();
	 * 
	 * }
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } finally { JdbcUtil.close(conn,
	 * pstmt, rs); }
	 * 
	 * 
	 * return dto; }
	 */
	
	public void delete (String id) { // 로그인 메소드와 중복되는 부분 XXXXX
		String sql = "DELETE FROM USERS WHERE ID = ? AND PW = ?";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
	
			pstmt.executeUpdate();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(conn, pstmt, null);
		}
		
	}
	
}

