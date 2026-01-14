<%--
  Description: Search bar component with Unpoly autosubmit integration
  Author: Dang Van Trung
  Date: 07/01/2026
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="ctxPath" value="${pageContext.request.contextPath}"/>

<div class="search-field search-field--wide" style="position: relative;">
    <form method="GET" action="${ctxPath}/search"
          up-target=".product-grid, .section-header"
          up-transition="cross-fade"
          up-cache="false"
          up-history="true"
          data-search-form>
        <input class="search-field__input"
               type="search"
               name="keyword"
               value="${searchQuery}"
               placeholder="${i18n.trans("general.search.placeholder")}"
               data-search-input
               autocomplete="off"/>
        <span class="search-field__icon icon-base">
            <i class="fi fi-rr-search"></i>
        </span>
    </form>

    <!-- Search History Dropdown -->
    <div class="search-history" data-search-history>
        <div class="search-history__header">
            <span class="search-history__title">
                <i class="fi fi-rr-time-past"></i>
                ${i18n.trans("general.search.recentHistory")}
            </span>
            <button type="button" class="search-history__clear" data-history-clear>
                <i class="fi fi-rr-trash"></i>
            </button>
        </div>
        <ul class="search-history__list" data-history-list>
            <!-- History items will be inserted here -->
        </ul>
    </div>
</div>