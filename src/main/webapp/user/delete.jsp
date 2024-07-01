<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../include/header.jsp" %>    
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<section>
	<div align="center">
		<h3>회원 탈퇴</h3>
		<p>비밀번호를 입력하세요.</p>
		
		<form action="deleteForm.user" method="post">
			<input type="password" name="pw">
			<input type="submit" value="확인">
		</form>
	</div>
</section>

</body>
</html>