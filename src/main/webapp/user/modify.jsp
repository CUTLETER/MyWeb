<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ include file="../include/header.jsp" %>

<section>
	
	
	<!--
	
		input 태그에 많이 사용되는 주요 속성
		
		required는 공백을 허용하지 않음
		readonly는 값을 수정하지 못하고 읽기만 가능함
		disabled는 태그를 사용하지 않음
		checked는 checkbox 안에서 미리 체크되어 있음
		selected는 select 태그에서 미리 선택되어 있음
	
		기타 등등 더 있음
	
	 -->


	<div align="center">
		<h3>회원 정보 관리</h3>
		<p>정보를 수정하려면 수정 버튼틀 누르세요.</p>
		
		<hr/>
		
		<form action="####" method="post">
			<table>
				<tr>							
					<td>아이디</td>										<!-- 아이디 못 바꾸게 설정함 -->
					<td> <input type="text" name="id" placeholder="4글자 이상" readonly="readonly"> </td>
				</tr>
				<tr>
					<td>비밀번호</td>
					<td><input type="password" name="pw" placeholder="4자 이상" required="required" pattern="[0-9A-Za-z]{4,}"></td>
				</tr>
				<tr>
					<td>이름</td>
					<td><input type="text" name="name" placeholder="이름" required="required"></td>
				</tr>
				<tr>
					<td>이메일</td>
					<td><input type="email" name="email"></td>
				</tr>
				<tr>
					<td>성별</td>
					<td>										<!-- checked 해당 값에 미리 체크되어 있음 -->
						<input type="radio" name="gender" value="M" checked="checked">남성
						<input type="radio" name="gender" value="W">여성
					</td>
				</tr>
			</table>
			
			${msg } <!-- 값이 있으면 msg, 없으면 공백 -->
			
			
			<br/> <!-- 버튼에 페이지 이동 기능 심어주기 onclick="location.href='경로';" -->
			<input type="submit" value="수정">
			<input type="button" value="취소" onclick="location.href='mypage.user';">
		</form>
		
	</div>
</section>

<%@ include file="../include/footer.jsp" %>    