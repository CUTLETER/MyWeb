package com.myweb.board.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.myweb.board.model.BoardDAO;
import com.myweb.board.model.BoardDTO;
import com.myweb.board.model.BoardMapper;
import com.myweb.util.mybatis.MybatisUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class BoardServiceImpl implements BoardService {
	
	// 멤버 변수에 세션 팩토리 하나 선언 (----------mybatis---------)
	private SqlSessionFactory sqlSessionFactory = MybatisUtil.getSqlSessionFactory();
	

	@Override
	public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// TODO Auto-generated method stub
		// 사용자가 입력한 값을 받음
		String writer = request.getParameter("writer");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		
		BoardDTO dto = new BoardDTO(0, writer, title, content, null, 0);
//		BoardMapper board = sqlSessionFactory.openSession().getMapper(BoardMapper.class);
		SqlSession sql = sqlSessionFactory.openSession(true); // true를 넣어야 실행마다 DB에 커밋이 반영됨
		// Class<?> ??? = BoardMapper.class; 이걸 아래 괄호 안에다 집어 넣음
		BoardMapper board = sql.getMapper(BoardMapper.class); // 호출 시킬 인터페이스명 작성
//		board.regist(dto); 반환 타입 없을 때는 이렇게!
		int result = board.regist(dto);
		sql.close(); // mybatis 세션 종료
		
		System.out.println("성공 여부 : " + result);
		
		// 목록 화면을 redirect로 태워서 나감
		response.sendRedirect("list.board");
		
		/* mybatis가 생성돼서 DAO는 사라짐
		 * 
		 * BoardDAO dao = BoardDAO.getInstance(); dao.regist(writer, title, content);
		 * 
		 * // 목록 화면으로 이동시키기 //
		 * request.getRequestDispatcher("board_list.jsp").forward(request, response); 글
		 * 목록은 이렇게 말고 아래처럼
		 * 
		 * // 목록 화면으로 이동시키기 response.sendRedirect("list.board");
		 */
	}

	
	@Override
	public void getList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/* mybatis가 생성돼서 DAO는 사라짐
		 * 
		 * // TODO Auto-generated method stub BoardDAO dao = BoardDAO.getInstance();
		 * ArrayList<BoardDTO> list = dao.getList();
		 * 
		 * request.setAttribute("list", list);
		 * request.getRequestDispatcher("board_list.jsp").forward(request, response);
		 * 
		 * 
		 */
	
		
		// (-------------mybatis------------)
		SqlSession sql = sqlSessionFactory.openSession(); // true를 넣어야 실행마다 DB에 커밋이 반영됨
		// Class<?> ??? = BoardMapper.class; 이걸 아래 괄호 안에다 집어 넣음
		BoardMapper board = sql.getMapper(BoardMapper.class); // 호출 시킬 인터페이스명 작성
		
//		String now = board.now();
//		System.out.println("sql 실행 성공 " + now);
		
		ArrayList<BoardDTO> list = board.getList();
		sql.close(); // mybatis 세션 종료
		// request forward
		request.setAttribute("list", list);
		request.getRequestDispatcher("board_list.jsp").forward(request, response);
		
	}


	@Override
	public void getContent(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// a href 로 넘어온 bno 값 받음 <a href="getContent.board?***이 bno***=${dto.bno }"
		String bno = request.getParameter("bno");
		
		// mybatis 실행
		SqlSession sql = sqlSessionFactory.openSession(true);
		BoardMapper mapper = sql.getMapper(BoardMapper.class);
		BoardDTO dto = mapper.getContent(bno); // 결과 반환
		
		// 조회수
		mapper.increaseHit(bno);
		
		
		sql.close(); // mybatis 세션 종료
		
		//dto를 request에 담고 forward 화면으로 이동
		request.setAttribute("dto", dto);
		request.getRequestDispatcher("board_content.jsp").forward(request, response);
	}


	@Override
	public void modify(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String bno = request.getParameter("bno");
		
		SqlSession sql = sqlSessionFactory.openSession(true);
		BoardMapper mapper = sql.getMapper(BoardMapper.class);
		BoardDTO dto = mapper.getContent(bno);
		
		sql.close(); // mybatis 세션 종료
		
		request.setAttribute("dto", dto);
		request.getRequestDispatcher("board_modify.jsp").forward(request, response);
	}


	@Override
	public void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String bno = request.getParameter("bno");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		
		BoardDTO dto = new BoardDTO();
		dto.setBno(Integer.parseInt(bno));
		dto.setTitle(title);
		dto.setContent(content);
		
		
		// mybatis 실행
		SqlSession sql = sqlSessionFactory.openSession(true);
		BoardMapper mapper = sql.getMapper(BoardMapper.class);
		
		int result = mapper.update(dto);
		
		sql.close();
		
		if(result == 1) {
			
			response.setContentType("text/html; charset=UTF-8;");
			PrintWriter out = response.getWriter();
			
			out.println("<script>");
			out.println("alert('정상 처리 완료');");
			out.println("location.href='list.board';");
			out.println("</script>");
			
		} else {
			response.sendRedirect("getContent.board?bno=" + bno); // getContent 메소드는 bno를 받기 때문에(첫줄 참고) 건네줘야 함
		}
	}


	@Override
	public void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		/* 1. 화면에서 전달되는 bno값을 얻음
		 * 2. mapper에 <delete> 태그를 추가함
		 * 		- insert, update의 적용 방식과 똑같음
		 * 3. 서비스 영역에서는 bno를 이용해서 삭제를 진행하고, 삭제 후에는 목록 페이지로 이동시킴
		 */
		String bno = request.getParameter("bno");

		SqlSession sql = sqlSessionFactory.openSession(true);
		BoardMapper mapper = sql.getMapper(BoardMapper.class);
		
		mapper.delete(bno);
		sql.close();
	
		response.sendRedirect("list.board");
	}
	

}
