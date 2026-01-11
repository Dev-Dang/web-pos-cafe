<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<base href="${pageContext.request.contextPath}/">

<%-- ========= MAIN ========= --%>
<main class="bg-body py-10">
    <div class="container">
        <div class="row g-5">
            <%-- Left: Menu & Order --%>
            <div class="col-lg-8">
                <%-- Bàn & Thời gian --%>
                <div class="d-flex align-items-center mb-4">
                    <%-- <di%>
                              class="bg-primary d-flex flex-column align-items-center rounded-4 px-3 py-2 me-3 text-white fw-bold"
                           >
                              <span>Còn lại</span>
                              <span>04:39:01</span>
                           </div>
                           <div class="d-flex flex-column gap-1">
                              <span class="fw-semibold">Bàn Số 8</span>
                              <span class="text-muted">
                                 Cố lên bạn ơi, sắp xong rồi!
                              </span>
                           </div> -->

                    <%-- Search box --%>
                    <div class="ms-auto w-50 d-flex align-items-center gap-3">
                        <input
                                type="text"
                                class="form-control"
                                placeholder="${i18n.trans("general.searchBar")}"
                                name="search-box"/>
                        <button class="btn btn-primary">
                            <i class="icon-base fi fi-rr-search"></i>
                        </button>
                    </div>
                </div>

                <%-- Banner --%>
                <div
                        id="bannerCarousel"
                        class="carousel slide mb-4"
                        data-bs-ride="carousel"
                        data-bs-touch="true">
                    <div class="carousel-inner">
                        <div class="carousel-item active">
                            <img src="assets/client/img/category/banner.png" class="d-block w-100" alt="..."/>
                        </div>
                        <div class="carousel-item">
                            <img src="assets/client/img/category/banner-2.png" class="d-block w-100" alt="..."/>
                        </div>
                        <div class="carousel-item">
                            <img src="assets/client/img/category/banner-3.png" class="d-block w-100" alt="..."/>
                        </div>
                    </div>

                    <button
                            class="carousel-control-prev"
                            type="button"
                            data-bs-target="#bannerCarousel"
                            data-bs-slide="prev">
                        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Previous</span>
                    </button>
                    <button
                            class="carousel-control-next"
                            type="button"
                            data-bs-target="#bannerCarousel"
                            data-bs-slide="next">
                        <span class="carousel-control-next-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Next</span>
                    </button>
                </div>

                <%-- Categories --%>
                <div class="category-wrapper position-relative">
                    <button class="category-nav-btn prev" id="prevCategory">
                        <i class="icon-sm fi fi-rr-angle-left"></i>
                    </button>

                    <div class="category-container">
                        <ul class="category-list">
                            <c:forEach items="${categories}" var="category">
                                <li class="category-chip">
                                    <img src="${category.iconUrl}"
                                         alt="${category.name}"
                                         class="category-image"/>
                                    <span class="category-name">
                                            ${category.name}
                                    </span>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>

                    <button class="category-nav-btn next" id="nextCategory">
                        <i class="icon-sm fi fi-rr-angle-right"></i>
                    </button>
                </div>

                <%-- Coffee Menu --%>
                <h4 class="fw-semibold mt-8 mb-4"> ${i18n.trans("general.productList")} </h4>
                <div class="row row-cols-3 g-4">
                    <div class="col">
                        <%-- Product card --%>
                        <div class="card" data-bs-toggle="modal" data-bs-target="#productDetailModal">
                            <img
                                    src="assets/client/img/product/soda-dao.png"
                                    class="card-img-top"
                                    alt="Soda Đào"/>
                            <div class="card-body">
                                <h5 class="card-title mb-1">Soda Đào</h5>
                                <p class="card-text small text-muted mb-2">
                                    Aquafina soda kèm với mứt đào ngon ngọt, cảm giác mát lạnh
                                </p>
                                <div>
                                    <span class="fw-bold fs-3 text-primary">18,000đ</span>
                                    <span class="text-decoration-line-through fs-5 fw-semibold text-muted ms-2">
                                        23,000đ
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <%-- Right: Cart --%>
            <div class="col-lg-4">
                <div class="bg-white rounded-5 shadow-sm p-4">
                    <h4 class="fw-bold mb-4"> ${i18n.trans("general.cart")} </h4>
                </div>
            </div>
        </div>
    </div>
</main>

<%-- ========= MODAL ========= --%>
<div id="modal-template" hidden>
    <!-- Preload template -->
    <jsp:include page="../modals/register.jsp"/>
    <jsp:include page="../modals/login.jsp"/>

    <!-- Open modal if needed -->
    <c:if test="${not empty requestScope.openModal}">
        <p data-open-modal="${openModal}"></p>
    </c:if>
</div>