<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>  
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">  
<title>godeyecase</title>  
</head>  
<body>
<!--
JDBC 驱动名及数据库 URL 数据库的用户名与密码
useUnicode=true&characterEncoding=utf-8 防止中文乱码 -->
<sql:setDataSource var="snapshot" driver="com.mysql.jdbc.Driver"
     url="jdbc:mysql://localhost:3306/RUNOOB?useUnicode=true&characterEncoding=utf-8"
     user="root"  password="123456"/>
 
<sql:query dataSource="${snapshot}" var="result">
SELECT * from websites;
</sql:query>
<!-- 显示校验错误信息 -->
<c:if test="${allErrors!=null }">
	<c:forEach items="${allErrors}" var="error">
		${ error.defaultMessage}<br/>
	</c:forEach>
</c:if>
<form id="itemForm" action="${pageContext.request.contextPath }/items/editItemsSubmit.action?" method="post" enctype="multipart/form-data">
<input type="hidden" name="id" value="${items.id }"/>
修改商品信息：
<table width="100%" border=1>
<tr>
    <td>商品名称</td>
    <td><input type="text" name="name" value="${items.name }"/></td>
</tr>
<tr>
    <td>商品价格</td>
    <td><input type="text" name="price" value="${items.price }"/></td>
</tr>
<tr>
    <td>商品生产日期</td>
    <td><input type="text" name="createtime" value="<fmt:formatDate value="${items.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/></td>
</tr>
<tr>
    <td>商品图片</td>
    <td>
        <c:if test="${items.pic !=null}">
            <img src="/pic/${itemsCustom.pic}" width=100 height=100/>
            <br/>
        </c:if>
        <input type="file"  name="items_pic"/> 
    </td>
</tr>
<tr>
    <td>商品简介</td>
    <td>
    <textarea rows="3" cols="30" name="detail">${itemsCustom.detail }</textarea>
    </td>
</tr>
<tr>
<%-- <td><a href="${pageContext.request.contextPath}/items/editItemsSubmit.action">提交</a></td>   --%>
<td colspan="2" align="center"><input type="submit" value="提交"/></td>
</tr>
</table>
</form>
</body>  
</html>  