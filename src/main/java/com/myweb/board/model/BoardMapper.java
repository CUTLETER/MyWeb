package com.myweb.board.model;

import java.util.ArrayList;

public interface BoardMapper {
	
	// 마이바티스는 인터페이스를 호출시키면, 연결될 수 있는 mapper.xml 파일이 실행됨
	
	public String now(); // 연습용 메소드
	
	public ArrayList<BoardDTO> getList(); // 글 목록 조회
	
	public int regist(BoardDTO dto); // 실행되고 끝이 아니라 실행 결과까지 돌려 받으려면 반환 타입 지정하기, 매개변수는 당장은 꼭 '1개'여야 함!
	
	public BoardDTO getContent(String bno); // 글 상세 내용
	
	public int update(BoardDTO dto); // 글 수정 작업
	
	public void delete(String bno); // 삭제
	
	public void increaseHit(String bno); // 조회수
	
}
