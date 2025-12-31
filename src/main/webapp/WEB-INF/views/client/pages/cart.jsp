<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- 
    Full cart page for non-AJAX access.
    Uses main layout template.
--%>

<c:set var="pageTitle" value="Giỏ hàng" scope="request"/>
<c:set var="pageContent" value="/WEB-INF/views/client/pages/cart-content.jsp" scope="request"/>
<jsp:include page="/WEB-INF/views/client/layouts/_main-layout.jsp"/>
