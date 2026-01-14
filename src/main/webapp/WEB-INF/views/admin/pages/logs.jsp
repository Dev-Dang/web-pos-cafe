<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div id="log" class="page-content">
    <header>
        <h1>Nhật Ký Hệ Thống</h1>
        <input type="text" class="search-bar" id="log-search-input"
               placeholder="Tìm kiếm hành động, người dùng...">
    </header>

    <div class="data-table-container">
        <c:forEach var="log" items="${logsList}">
            <div class="log-entry" data-search="${log.userName} ${log.description} ${log.action}">

                <c:choose>
                    <c:when test="${log.level == 'ERROR'}">
                        <div class="log-icon log-error">
                            <i class="fas fa-bug"></i>
                        </div>
                    </c:when>
                    <c:when test="${log.level == 'WARNING'}">
                        <div class="log-icon log-warning">
                            <i class="fas fa-exclamation-triangle"></i>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="log-icon log-info">
                            <i class="fas fa-info-circle"></i>
                        </div>
                    </c:otherwise>
                </c:choose>

                <div class="log-message">
                    <strong>${log.userName}</strong>
                    <span style="color: #666; font-size: 0.9em;">[${log.action}]</span>:
                        ${log.description}
                    <c:if test="${not empty log.ipAddress}">
                        <span style="font-size: 0.8em; color: #999;">(IP: ${log.ipAddress})</span>
                    </c:if>
                </div>

                <div class="log-timestamp" data-time="${log.createdAt}">
                    <fmt:formatDate value="${log.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                </div>
            </div>
        </c:forEach>

        <c:if test="${empty logsList}">
            <div style="padding: 20px; text-align: center; color: #888;">
                Chưa có nhật ký hoạt động nào.
            </div>
        </c:if>
    </div>
</div>

<script>
    $(document).ready(function() {
        // 1. Chuyển đổi thời gian sang dạng "X phút trước"
        // Kiểm tra xem hàm calculateTimeAgo (từ dashboard.js) có tồn tại không

</script>