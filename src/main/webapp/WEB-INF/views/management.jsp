<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<script src="https://code.jquery.com/jquery-3.4.1.js" integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU=" crossorigin="anonymous"></script>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>

<style>
	.space{
		position:absolute;
		top: 25%; left:30%;
        width:50%; height:200px;
	}
	table {
	  border-collapse: separate;
	  border-spacing: 10px 10px;
	}
</style>
</head>
<body>
	<jsp:include page="sellerTop.jsp" />
	<div class="space">
		<form action="management" method="POST">
			<table>
				<tr>
					<th>NAME</th>
					<th>STATUS</th>
					<th>STOCK</th>
					<th>DELETE</th>
				</tr>
				<c:forEach var="item" items="${managementList}" >
					<tr class="tr${item.no}">
						<td>${item.name}</td>
						<td class="${item.no}">
							<input type="radio" class="on" name="${item.no}" value=1>ON
							<input type="radio" class="off" name="${item.no}" value=0>OFF
						</td>
						<td>
							<input type="text" name="stock${item.no}" value=${item.stock}>
						</td>
						<td><button type="button" class="del${item.no}">DELETE</button></td>
					</tr>
				</c:forEach>
				<tr>
					<td><input type="submit" value="SAVE"></td>
				</tr>
			</table>
		</form>	
	</div>
</body>
</html>

<script>
	<c:forEach var="item" items="${managementList}">
		var item = ${item.getStatus()}
		
		if(item == 0){
			$('.${item.getNo()} > .off').prop("checked", true);
		}else{
			$('.${item.getNo()} > .on').prop("checked", true);
		}
		
		var data = ${item.no};
		$('.del${item.no}').click(function(){
			$.ajax({
				url: "/shop/management/del/${item.no}",
				type: "GET"
			})
			.done(function(result){
				$('.tr${item.no}').remove();
			})
		})
	</c:forEach>
</script>