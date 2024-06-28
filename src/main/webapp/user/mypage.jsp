<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ include file="../include/header.jsp" %>


<section>
	<div align="center">
		<h3>${sessionScope.user_name } 회원(${sessionScope.user_id })님의 회원정보를 관리합니다.</h3>
		
		<a href="modify.user">회원 정보 관리</a> <!-- 컨트롤러 태움 -->
		<a>회원 탈퇴</a>
		
	</div>
</section>


<%@ include file="../include/footer.jsp" %>