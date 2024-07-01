package com.myweb.user.service;

import java.io.IOException;
import java.io.PrintWriter;

import com.myweb.user.model.UserDAO;
import com.myweb.user.model.UserDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class UserServiceImpl implements UserService {

	@Override
	public void join(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 값을 대신 받을 수 있음
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String gender = request.getParameter("gender");
		
		// 중복되는 회원이 있는지 확인하기
		// 중복이 없는 경우 회원가입 처리하기
		
		UserDAO dao = UserDAO.getInstance();
		int cnt = dao.findUser(id);
		
		if(cnt==1) { // 아이디 중복
			
			request.setAttribute("msg", "이미 존재하는 회원입니다.");
			request.getRequestDispatcher("join.jsp").forward(request, response);
			
		} else { // 중복 없으면 회원가입
			dao.insertUser(id, pw, name, email, gender); // 반환 없으니 받아올 데이터도 없음
			response.sendRedirect("login.user"); // *** MVC2 방식에서 sendRedirect는 다시 컨트롤러를 태울 때 사용함
		}
	}

	
	
	
	@Override
	public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		
		// 로그인 시도
		UserDAO dao = UserDAO.getInstance();
		UserDTO dto = dao.login(id, pw);
		
		if (dto == null) { // 아이디 or 비밀번호가 틀렸음
			request.setAttribute("msg", "아이디 또는 비밀번호가 틀렸습니다.");
			request.getRequestDispatcher("login.jsp").forward(request, response);
		} else { // 로그인 성공
			// 세션에 로그인 성공에 대한 내용을 저장 시킴
			HttpSession session = request.getSession(); // 리퀘스트에서 현재 세션을 얻음
			session.setAttribute("user_id", dto.getId());
			session.setAttribute("user_name", dto.getName());
			
			response.sendRedirect("mypage.user"); // 다시 컨트롤러 태워서 나감
		}





		// 아이디 기반으로 회원 정보를 조회해서 데이터를 가지고 수정 페이지로 이동시키기
		//1. 아이디는 세션이 있음
		//2. 아이디 기반으로 회원 정보를 조회하는 getInfo() DAO에 생성함
		//3. 서비스에서는 getInfo() 호출하고, 조회한 데이터를 request에 저장함
		//4. forward를 이용해서 modify.jsp로 이동함
		//5. 회원 정보를 input 태그에 미리 출력함
	}




	@Override
	public void getInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("user_id");
		
		UserDAO dao = UserDAO.getInstance();
		UserDTO dto = dao.getInfo(id);
		
		if (dto != null) {
			request.setAttribute("dto", dto);
			request.getRequestDispatcher("modify.jsp").forward(request, response);
		} else {
			request.setAttribute("msg", "회원 정보를 가져오는 데 실패했습니다.");
			response.sendRedirect("mypage.user");
		}
		
	}




	@Override
	public void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 클라이언트의 값을 받기
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String gender = request.getParameter("gender");
		
		// update 메소드의 매개변수가 dto 타입이라 먼저 파라미터를 dto에 저장시키기
		UserDTO dto = new UserDTO(id, pw, name, email, gender, null);
		
		// dao 생성
		UserDAO dao = UserDAO.getInstance();
		int result = dao.update(dto); // update 메소드의 반환은 정수형이라
		
		if(result == 1) { // 업데이트 성공
			
			// 세션의 name을 수정하기 (수정 직후의 닉네임으로 XX님의 회원 정보를 관리합니다에 바로 반영하기
			HttpSession session = request.getSession();
			session.setAttribute("user_name", name);
			
			
			// 알림창을 java에서 화면에 보내는 방법
			// out 객체 : 클라이언트로 출력
			response.setContentType("text/html; charset=UTF-8"); // 문서에 대한 타입, jsp 파일 맨위에 적혀 있음
			PrintWriter out = response.getWriter(); // printwriter에 담아내면 콘솔 말고 화면(웹 브라우저)에 출력해줌
			out.println("<script>");
			out.println("alert('회원 정보가 수정되었습니다.');"); // 경고창 띄우기
			out.println("location.href='mypage.user';"); // 경고창 타고 갈 이동경로
			out.println("</script>");
			
			
		} else { // 업데이트 실패
			response.sendRedirect("mypage.user"); // 보내줄 데이터가 없어서 redirect로! mvc2에선 컨트롤러의 경로를 적음
		}
		
		
	}




	@Override
	public void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 화면에서 넘어오는 pw 값을 받기, (id는 세션에 있음)
		 * 탈퇴는 비밀번호가 일치하는지 확인하고 진행하기
		 * 로그인 메소드 재활용하기
		 * 로그인 메소드가 DTO를 반환하면 DAO에 delete() 메소드 만들고 회원 삭제를 진행하기
		 * 탈퇴 성공 시엔 세션을 전부 삭제하고 "홈화면"으로 redirect 시키기 
		 * 비밀번호가 틀렸을 땐 delete.jsp에 "비밀번호를 확인하세요." 메세지 띄우기
		 */
		
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("user_id");
		
		String pw = request.getParameter("pw");
		
		UserDAO dao = UserDAO.getInstance();
//		UserDTO dto = dao.delete(id, pw); 이건 DAO에서 로그인 메소드와 중복되는 메소드 쓸 때
		UserDTO dto = dao.login(id, pw);
		
		if (dto != null) {
			dao.delete(id);
			session.invalidate();
			response.sendRedirect("../index.jsp");
		} else {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('비밀번호를 확인하세요.');");
			out.println("location.href='delete.jsp';");
			out.println("</script>");
			
		}
		
	}

}
