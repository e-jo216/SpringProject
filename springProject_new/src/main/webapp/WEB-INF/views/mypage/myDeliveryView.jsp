<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/mypage.css" type="text/css">
<link rel="stylesheet" href="https://pro.fontawesome.com/releases/v5.10.0/css/all.css" integrity="sha384-AYmEC3Yw5cVb3ZcuHtOA93w35dYTsvhLPVnYs9eStHfGJvOvKxVfELGroGkvsg+p" crossorigin="anonymous"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery-3.6.0.min.js"></script>
<script type="text/javascript">
$(function(){
	//추가 버튼 클릭
	$('#del_addbtn').click(function(){
		$('#del_content').show();
		$(this).hide();
		$('#del_list').hide();
		$('#delPageNum').hide();
		$('#del_reset').show();
		$('.noContent').hide();
	});
	//추가 취소 버튼 클릭
	$('#del_reset').click(function(){
		$('#del_content').hide();
		$('#del_addbtn').show();
		$('#del_list').show();
		$('#delPageNum').show();
		$('.noContent').show();
		$(this).hide();
	});
	//추가 한경우
	$('#delsubmit').click(function(){
		alert('배송지가 추가되었습니다.');
	});
	//수정 완료 한 경우
	$('#delUpdateSubmit').click(function(){
		alert('수정되었습니다.');
	});
	//삭제 한경우
	$('#delete_btn').click(function(){
		alert('삭제했습니다.');
	});
});
</script>
<!-- 배송지 추가 -->
<!-- iOS에서는 position:fixed 버그가 있음, 적용하는 사이트에 맞게 position:absolute 등을 이용하여 top,left값 조정 필요 -->
<div id="layer" style="display:none;position:fixed;overflow:hidden;z-index:1;-webkit-overflow-scrolling:touch;">
<img src="//t1.daumcdn.net/postcode/resource/images/close.png" id="btnCloseLayer" style="cursor:pointer;position:absolute;right:-3px;top:-3px;z-index:1" onclick="closeDaumPostcode()" alt="닫기 버튼">
</div>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
    // 우편번호 찾기 화면을 넣을 element
    var element_layer = document.getElementById('layer');

    function closeDaumPostcode() {
        // iframe을 넣은 element를 안보이게 한다.
        element_layer.style.display = 'none';
    }

    function sample2_execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                // 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var addr = ''; // 주소 변수
                var extraAddr = ''; // 참고항목 변수

                //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                    addr = data.roadAddress;
                } else { // 사용자가 지번 주소를 선택했을 경우(J)
                    addr = data.jibunAddress;
                }

                // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
                if(data.userSelectedType === 'R'){
                    // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                    // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                    if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                        extraAddr += data.bname;
                    }
                    // 건물명이 있고, 공동주택일 경우 추가한다.
                    if(data.buildingName !== '' && data.apartment === 'Y'){
                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                    // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                    if(extraAddr !== ''){
                        extraAddr = ' (' + extraAddr + ')';
                    }
                    // 조합된 참고항목을 해당 필드에 넣는다.(나머지주소)
                    document.getElementById("address_detail").value = extraAddr;
                
                } else {
                    document.getElementById("address_detail").value = '';
                }

                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                document.getElementById('zipcode').value = data.zonecode;
                document.getElementById("address").value = addr;
                // 커서를 상세주소 필드로 이동한다.
                document.getElementById("address_detail").focus();

                // iframe을 넣은 element를 안보이게 한다.
                // (autoClose:false 기능을 이용한다면, 아래 코드를 제거해야 화면에서 사라지지 않는다.)
                element_layer.style.display = 'none';
            },
            width : '100%',
            height : '100%',
            maxSuggestItems : 5
        }).embed(element_layer);

        // iframe을 넣은 element를 보이게 한다.
        element_layer.style.display = 'block';

        // iframe을 넣은 element의 위치를 화면의 가운데로 이동시킨다.
        initLayerPosition();
    }

    // 브라우저의 크기 변경에 따라 레이어를 가운데로 이동시키고자 하실때에는
    // resize이벤트나, orientationchange이벤트를 이용하여 값이 변경될때마다 아래 함수를 실행 시켜 주시거나,
    // 직접 element_layer의 top,left값을 수정해 주시면 됩니다.
    function initLayerPosition(){
        var width = 300; //우편번호서비스가 들어갈 element의 width
        var height = 400; //우편번호서비스가 들어갈 element의 height
        var borderWidth = 5; //샘플에서 사용하는 border의 두께

        // 위에서 선언한 값들을 실제 element에 넣는다.
        element_layer.style.width = width + 'px';
        element_layer.style.height = height + 'px';
        element_layer.style.border = borderWidth + 'px solid';
        // 실행되는 순간의 화면 너비와 높이 값을 가져와서 중앙에 뜰 수 있도록 위치를 계산한다.
        element_layer.style.left = (((window.innerWidth || document.documentElement.clientWidth) - width)/2 - borderWidth) + 'px';
        element_layer.style.top = (((window.innerHeight || document.documentElement.clientHeight) - height)/2 - borderWidth) + 'px';
    }
</script>
<div>
	<div class="miniTitle_left">
	<b class="miniTitle_content">배송지 관리</b>
	</div>
	<hr noshade="noshade" class="line">
</div>
<div class="container-title">
	<span>배송지</span>
</div>
<input type="button" id="del_addbtn" value="+ 추가" class="btnCss">
<input type="button" id="del_reset" value="취소" style="display: none;" class="btnCss">
<div class="cssdelete"></div>
<div id="del_content" style="display: none;">
	<form id="register_form" action="${pageContext.request.contextPath}/mypage/myDeliverysubmit.do">
		<p>수령인</p>
		<input type="text" placeholder="받는 분 성함을 입력해주세요." name="name" size=40>
		<p>주소</p>
		<input type="text" name="zipcode" id="zipcode" maxlength="5">
		<input type="button" onclick="sample2_execDaumPostcode()" value="우편번호"><br>
		<input type="text" name="address" id="address" maxlength="30" size=40><br>
		<input type="text" name="address_detail" id="address_detail" maxlength="30" size=40><br>
		<p>받는 사람 휴대폰 번호</p>
		<input type="tel" placeholder="받는 분 휴대폰 번호를 입력해주세요." name="phone" size=40><br>
		<input type="checkbox" name="address_check" value="1" >기본 배송지로 등록<br><br>
		<input type="submit" value="등록 완료" id="delsubmit">
	</form>
</div>
<c:if test="${count == 0}">
	<div class="noContent">
		등록된 배송지가 없습니다.
	</div>
</c:if>
<c:if test="${count > 0}">
	<div class="del_container" id="del_list">
	<c:forEach var="del" items="${list}">
		<div class="del_item">	
			<div id="del_hide_content">
				<ul class="del_item_css delChoice_btn">
					<li>
					<input type="button" value="수정" class="btnCss update_dle_btn" onclick="location.href='${pageContext.request.contextPath}/mypage/myDeliveryUpdateForm.do?num=${del.num}&m_num=${del.m_num}'">
					<input type="button" value="삭제" class="btnCss" id="delete_btn" onclick="location.href='${pageContext.request.contextPath}/mypage/myDeliveryDelete.do?num=${del.num}'">
					</li>
				</ul>
				<ul class="del_item_css delContent">
					<li><input type="hidden" value="${del.num}" id="num" name="num"></li>
					<li>${del.name}<c:if test="${del.address_check==1}">&nbsp;&nbsp;<span id="mainDel">기본</span></c:if></li>
					<li>[${del.zipcode}] ${del.address}</li>
					<li>${del.phone}</li>
				</ul>
			</div>
		</div><!-- 아이템 -->
	</c:forEach>
	</div><!-- 컨테이너 -->
</c:if>
<div style="text-align: center;" id="delPageNum">${pagingHtml}</div>