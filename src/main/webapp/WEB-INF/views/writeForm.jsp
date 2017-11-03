<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta charset="utf-8">
<title>글쓰기</title>
</head>
<body>
<%System.out.println("writeForm.jsp");%>
<form action="/bbs/write.bbs" method="post">
	<table border="2" width="200">  
		<tr>
 			 <td>글쓴이 :</td><td>${id}</td>
 		</tr>
 		<tr>	 
		 <td>제목 : </td><td><input type="text" name="title"></td>			 
		</tr>
		<tr>
		  <td colspan="2"> <textarea cols="50" rows="20" name="content" ></textarea></td>
	    </tr> 	    
	    <tr>
	      <td>첨부 : </td><td><input type="file" name="fname"></td>
	    </tr>
	    <tr>
	      <td><input type="submit" value="글쓰기"></td>
	      <td><input type="reset" value="글쓰기취소"></td>	      	 
	    </tr>		
	</table>	
</form>
</body>
</html>