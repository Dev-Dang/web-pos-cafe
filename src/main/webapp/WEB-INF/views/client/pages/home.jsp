<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags" %>
<base href="${pageContext.request.contextPath}/">

<%-- ========= MAIN ========= --%>
<main class="page-main">
    <div class="page-container">
        <div class="row g-5">
            <div class="col-lg-8 col-12">
                <section class="product-section">
                    <%-- Product section header --%>
                    <div class="d-flex align-items-center justify-content-end gap-4 flex-wrap">
                        <%-- Search box --%>
                        <jsp:include page="../components/search/_search-bar.jsp"/>
                    </div>

                    <%-- Product banner carousel --%>
                    <jsp:include page="../components/carousels/_home-carousel.jsp"/>

                    <%-- Product section --%>
                    <jsp:include page="../components/product/_product-section.jsp"/>
                </section>
            </div>

            <!-- Sidebar Cart -->
            <div class="col-lg-4 col-12 d-none d-lg-block">
                <jsp:include page="../components/cart/_cart-panel.jsp"/>
            </div>
        </div>
    </div>
</main>

<!-- Mobile Bottom Navigation -->
<jsp:include page="../components/navbar/_mobile-nav.jsp"/>

<!-- Mobile Cart Drawer -->
<jsp:include page="../components/cart/_mobile-cart.jsp"/>