package com.myweb.board.service;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface BoardService {
	
	// 등록
	void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	
	// 목록
	void getList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	
	// 내용 확인
	void getContent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	
	// 글 수정 페이지
	void modify(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

	// 글 수정 작업
	void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	
	// 글 삭제 작업
	void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	
	
}
