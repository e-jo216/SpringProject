<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 중앙 내용 시작 -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/projectList.css">
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.13.0/css/all.min.css" rel="stylesheet">
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery-3.6.0.min.js"></script>
<script>
	$(document).ready(function(){
		/*
		//검색 유효성 체크
		$('#search_form').submit(function(){
			if($('#keyword').val().trim() == ''){
				alert('검색어를 입력하세요!');
				$('#keyword').val('').focus();
				return false;
			}
		});
		*/

	});
</script>
<div class="container">
	<div class="container-title"><span>프로젝트</span></div>
	<div class="category_wrap">
		<ul class="category">
			<li><a href="list.do">전체</a></li>
			<li>l</li>
			<li><a href="list.do?category=0">온라인</a></li>
			<li>l</li>
			<li><a href="list.do?category=1">모바일</a></li>
			<li>l</li>
			<li><a href="list.do?category=2">보드</a></li>
			<li>l</li>
			<li><a href="list.do?category=3">카드</a></li>
		</ul>
	</div>
	<c:if test="${count == 0}">
		<div class="result-display">
			등록된 프로젝트가 없습니다.
		</div>
	</c:if>
	<c:if test="${count > 0}">
		<div class="filter-bar">
			<div class="filter_wrap">
				<form id="filter_form" action="list.do" method="get">
					<ul class="filter">
						<li>
							<select name="state" id="state">
								<option value="" >전체</option>
								<option value="1">진행</option>
								<option value="2">마감</option>
								<option value="3">공개예정</option>
							</select>
						</li>
						<li>
							<select name="order" id="order">
								<option value="1">최신순</option>
								<option value="2">인기순</option>
								<option value="3">마감임박순</option>
							</select>
						</li>
						<li>
							<button type="submit" class="filterButton">검색</button>
							
						</li>
					</ul>
				</form>
			</div>
			<div class="form_wrap">
				<form id="search_form" action="list.do" method="get">
					<ul class="search">
						<li>
							<div class="search_wrap">
								<input type="search" name="keyword" id="keyword">
						    	<button type="submit" class="searchButton"><i class="fas fa-search"></i></button>
							</div>
						</li>
					</ul>
				</form>
			</div>
		</div>
		<div class="content-wrap">
		<c:forEach var="project" items="${list}">
			<div class="content">
				<c:if test="${project.photo == 'default_team.jpg'}">
					<a href="detail.do?num=${project.num}">
						<img class="photo" src="${pageContext.request.contextPath}/resources/images/default_team.jpg">
					</a>
				</c:if>
				<c:if test="${project.photo != 'default_team.jpg'}">
					<a href="detail.do?num=${project.num}">
						<img class="photo" src="${pageContext.request.contextPath}/upload/${project.photo}">
					</a>
				</c:if>
				<div class="name"><p><a href="detail.do?num=${project.num}">${project.name}</a></p></div>
				<div class="info-wrap">
					<div class="info">
						<c:if test="${project.category == 0}">온라인</c:if>
						<c:if test="${project.category == 1}">모바일</c:if>
						<c:if test="${project.category == 2}">보드</c:if>
						<c:if test="${project.category == 3}">카드</c:if>
					</div>
					<div class="info">l</div>
					<div class="info">${project.nickname}</div>
				</div>
				<div class="summary"><p>${project.summary}</p></div>
				<progress value="${project.progress}" max="100"></progress>
				<div class="progress-wrap">
					<div class="progress-right">
						<div class="progressper">${project.progress}% &nbsp;</div>
						<div class="amount">${project.amount}원</div>
					</div>
					<c:if test="${project.deadline >= 0}">
						<div class="deadline">${project.deadline}일 남음</div>
					</c:if>
					<c:if test="${project.deadline < 0}">
						<div class="deadline">마감</div>
					</c:if>
				</div>
			</div>
		</c:forEach>
		</div>
		<div class="align-center">${pagingHtml}</div> 
	</c:if>
</div>
<!-- 중앙 내용 끝 -->
