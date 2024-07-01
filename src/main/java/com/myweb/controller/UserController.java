package com.myweb.controller;

import java.io.IOException;

import com.myweb.user.service.UserService;
import com.myweb.user.service.UserServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@WebServlet("*.user") // 확장자 패턴

public class UserController extends HttpServlet {
	
	public UserController() {
		// TODO Auto-generated constructor stub
	}
	
	// doAction으로 연결하기
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAction(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAction(request, response);
	}
	
	protected void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		
		String uri = request.getRequestURI(); 
		String path = request.getContextPath();
		String command = uri.substring(path.length());
		
		System.out.println(command);
		
		UserService service; // 공통으로 쓸 거라 바깥으로 뺌 2
		
		
		// *** 기본 이동방식 forward
		// *** MVC2 방식에서 sendRedirect는 다시 컨트롤러를 태울 때 사용함
		
		
		// 회원 가입 화면
		if(command.equals("/user/join.user")) {			  					// 회원 가입 화면
//			response.sendRedirect("join.jsp");
			request.getRequestDispatcher("join.jsp").forward(request, response);
		} else if(command.equals("/user/joinForm.user")) {					 // 회원 가입 기능
			/*UserService service 공통으로 쓸 거라 바깥으로 뺌 1*/ service = new UserServiceImpl(); // 부모에게 저장시킴
			service.join(request, response);
		} else if(command.equals("/user/login.user")) {						 // 로그인 화면
//			response.sendRedirect("login.jsp");
			request.getRequestDispatcher("login.jsp").forward(request, response);
		} else if(command.equals("/user/loginForm.user")) { 				// 로그인 요청
			service = new UserServiceImpl();
			service.login(request, response);
		} else if(command.equals("/user/mypage.user")) { // 회원 페이지
			request.getRequestDispatcher("mypage.jsp").forward(request, response);
		} else if(command.equals("/user/modify.user")) { // 정보 페이지
			service = new UserServiceImpl();
			service.getInfo(request, response);
		} else if(command.equals("/user/logout.user")) {
			HttpSession session = request.getSession();
			session.invalidate();
			response.sendRedirect(request.getContextPath()+"/index.jsp"); // 메인 화면으로
		} else if(command.equals("/user/update.user")) { // 정보 수정 페이지
			service = new UserServiceImpl();
			service.update(request, response);
		} else if(command.equals("/user/delete.user")) { // 삭제 페이지
			//mvc2는 기본 이동이 forward
			request.getRequestDispatcher("delete.jsp").forward(request, response);
		} else if(command.equals("/user/deleteForm.user")) { // 탈퇴 요청 페이지
			service = new UserServiceImpl();
			service.delete(request, response);
		}
		
	}
}
