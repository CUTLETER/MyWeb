package com.myweb.controller;

import java.io.IOException;

import com.myweb.board.service.BoardService;
import com.myweb.board.service.BoardServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("*.board") // .board로 끝나는 모든 요청은 서블릿으로 연결함

public class BoardController extends HttpServlet{


	private static final long serialVersionUID = 1L;

	public BoardController() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doAction(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doAction(req, resp);
	}
	
	protected void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		
		String uri = request.getRequestURI(); 
		String path = request.getContextPath();
		String command = uri.substring(path.length());
		
		System.out.println(command);
		
//		boardservice 선언하기
		BoardService service;
		
		
		if(command.equals("/board/list.board")) { // 목록 페이지
			//mvc2 기본 이동 방식은 forward
//	XXXXX	request.getRequestDispatcher("board_list.jsp").forward(request, response);
			// 서비스 영역을 거쳐서 목록 data를 가지고 가야 함
			service = new BoardServiceImpl();
			service.getList(request, response);
			
		} else if(command.equals("/board/write.board")) { // 글 작성
//			request.getRequestDispatcher("board_write.jsp").forward(request, response);
			HttpSession session = request.getSession();
			String user_id = (String)session.getAttribute("user_id");
			
			if(user_id == null) {
				response.sendRedirect("/MyWeb/index.jsp");
					return;
			}
			
		} else if(command.equals("/board/registForm.board")) { // 글 등록
			service = new BoardServiceImpl();
			service.regist(request, response);
		} else if(command.equals("/board/getContent.board")) { // 글 상세 내용
			service = new BoardServiceImpl();
			service.getContent(request, response);
		} else if(command.equals("/board/modify.board")) { // 글 수정 페이지
			service = new BoardServiceImpl();
			service.modify(request, response);
		} else if(command.equals("/board/updateForm.board")) { //글 수정 작업
			service = new BoardServiceImpl();
			service.update(request, response);
		} else if(command.equals("/board/delete.board")) { // 삭제 작업
			service = new BoardServiceImpl();
			service.delete(request, response);
		}
	}
	
}
