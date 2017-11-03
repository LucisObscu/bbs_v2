<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>회원가입</title>
<script src="https://code.jquery.com/jquery-3.2.0.min.js"></script>
<script>
$.ajaxSetup({
	type : "POST",
	async : true,
	dataType : "json",
	error : function(xhr){
		alert("error html = " + xhr.statusText);
	}
});

 $(function(){
	 $("#id").on("blur", function(){		
		 $.ajax({			 
			url:"/bbs/joinIdCheck.bbs",					
			data:{				
				inputId:$("#id").val()				
			},
			success : function(data) {				
				let html;
				alert(data);
				if(data==1){					
					html="<b>멋진 아이디입니다</b>";
					$("#idCheckStatus").html(html).css("color","red");;
				}else{
					html="<b>안좋은 아이디입니다</b>";
					$("#idCheckStatus").html(html).css("color","green")
				} 				
			}					
		}); 		 
	 })
 });
 

</script>
</head>
<body>
	<form action="/bbs/joinMember.bbs" method="post">	
		<fieldset>
			<legend>회원가입</legend>	
          <label for="id"> I  D : <input type="text" name="id" id="id"></label><br /><br />
          <div id="idCheckStatus"></div>
          <label for="pass">PASS : <input type="text" name="pass" id="pass"></label>
          <input type="submit" value="확인">
          <input type="reset" value="취소"> 
       </fieldset>                  
   </form>
</body>
</html>