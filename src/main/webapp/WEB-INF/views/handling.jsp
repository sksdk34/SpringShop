<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
<meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=device-width"/>
<script src="//developers.kakao.com/sdk/js/kakao.min.js"></script>
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
		<table>
			<tr>
				<th><label>PURCHASE-NO</label></th>
				<th><label>NO</label></th>
				<th><label>ID</label></th>
				<th><label>ADDRESS</label></th>
				<th><label>PHONE</label></th>
				<th><label>HANDLING</label></th>
			</tr>
			<c:forEach var="item" items="${list}">
				<tr>
					<td>${item.purchaseno}</td>
					<td>${item.no}</td>
					<td>${item.id}</td>
					<td>${item.address}</td>
					<td>${item.phone}</td>
					<td><button class="${item.purchaseno}">HANDLING</button></td>
				</tr>
			</c:forEach>
		</table>
	</div>
	<!-- 
	<a id="kakao-link-btn" href="javascript:sendLink()">
		<img src="//developers.kakao.com/assets/img/about/logos/kakaolink/kakaolink_btn_medium.png"/>
	</a>
	 -->
</body>
</html>

<script>
	<c:forEach var="item" items="${list}">
		$('.' + ${item.purchaseno}).click(function(){
			location.href = ${item.purchaseno} + "/handlingProcessing";
		})
	</c:forEach>
		
	//<![CDATA[
    // // 사용할 앱의 JavaScript 키를 설정해 주세요.
    Kakao.init('0b6dfc31f981d4c71cb940e568f94267');
    // // 카카오링크 버튼을 생성합니다. 처음 한번만 호출하면 됩니다.
    function sendLink() {
      Kakao.Link.sendDefault({
        objectType: 'feed',
        content: {
          title: '딸기 치즈 케익',
          description: '#케익 #딸기 #삼평동 #카페 #분위기 #소개팅',
          imageUrl: 'http://mud-kage.kakao.co.kr/dn/Q2iNx/btqgeRgV54P/VLdBs9cvyn8BJXB3o7N8UK/kakaolink40_original.png',
          link: {
            mobileWebUrl: 'https://developers.kakao.com',
            webUrl: 'https://developers.kakao.com'
          }
        },
        social: {
          likeCount: 286,
          commentCount: 45,
          sharedCount: 845
        },
        buttons: [
          {
            title: '웹으로 보기',
            link: {
              mobileWebUrl: 'https://developers.kakao.com',
              webUrl: 'https://developers.kakao.com'
            }
          },
          {
            title: '앱으로 보기',
            link: {
              mobileWebUrl: 'https://developers.kakao.com',
              webUrl: 'https://developers.kakao.com'
            }
          }
        ]
      });
    }
  //]]>
</script>