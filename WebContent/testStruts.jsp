<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>Insert title here</title>

<script type="text/javascript" src="jquery-1.4.4.js"></script>
<script type="text/javascript">
	$(function() {
		$('#demo').click(function() {
			
			$.ajax({
				contentType : 'application/json',
				url : 'test/testStruts.action',
				type : 'POST',
				data : JSON.stringify({
				 ajaxData : {
					sname : $('#name').val(),
					sage : $('#age').val(),
					sheight : $('#height').val(),
					sweight : $('#weight').val()
				}}),
				success : function(data, st) {
					$('#demo2').html(data + ' ** ' + st);
				}
			});
		});
	});
</script>
</head>
<body>
<input type="text" id="name" value="name"/><br/>
<input type="text" id="age" value="age"/><br/>
<input type="text" id="height" value="height"/><br/>
<input type="text" id="weight" value="weight"/><br/>
<input type="button" onclick="">
<p id="demo">save sharon info</p>
<p id="demo2"></p>

</body>
</html>