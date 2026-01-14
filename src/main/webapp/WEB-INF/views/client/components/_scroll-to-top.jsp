<%--
  Description: Scroll to top button component
  Author: Dang Van Trung
  Date: 07/01/2026
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<button class="scroll-to-top" 
        id="scrollToTopBtn" 
        title="Cuộn lên đầu trang"
        aria-label="Cuộn lên đầu trang">
    <i class="fi fi-rr-angle-up"></i>
</button>

<style>
.scroll-to-top {
    position: fixed;
    bottom: 2rem;
    right: 2rem;
    z-index: 1000;
    width: 5rem;
    height: 5rem;
    border: none;
    border-radius: 50%;
    background: var(--color-primary);
    color: white;
    font-size: 2rem;
    cursor: pointer;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    transition: all 0.3s ease;
    opacity: 0;
    visibility: hidden;
    transform: translateY(20px);
}

.scroll-to-top.visible {
    opacity: 1;
    visibility: visible;
    transform: translateY(0);
}

.scroll-to-top:hover {
    background: var(--color-primary-dark);
    transform: translateY(-2px);
    box-shadow: 0 6px 16px rgba(0, 0, 0, 0.2);
}

.scroll-to-top:active {
    transform: translateY(0);
}

/* Mobile responsive */
@media (max-width: 768px) {
    .scroll-to-top {
        width: 4.5rem;
        height: 4.5rem;
        font-size: 1.8rem;
        bottom: 1.5rem;
        right: 1.5rem;
    }
}

/* Ensure it's above other elements */
.scroll-to-top {
    z-index: 9999;
}
</style>

<script>
document.addEventListener('DOMContentLoaded', function() {
    const scrollToTopBtn = document.getElementById('scrollToTopBtn');
    
    if (scrollToTopBtn) {
        // Show/hide button based on scroll position
        function toggleScrollButton() {
            const scrollTop = window.pageYOffset || document.documentElement.scrollTop;
            
            if (scrollTop > 300) {
                scrollToTopBtn.classList.add('visible');
            } else {
                scrollToTopBtn.classList.remove('visible');
            }
        }
        
        // Smooth scroll to top
        function scrollToTop() {
            window.scrollTo({
                top: 0,
                behavior: 'smooth'
            });
        }
        
        // Event listeners
        window.addEventListener('scroll', toggleScrollButton, { passive: true });
        scrollToTopBtn.addEventListener('click', scrollToTop);
        
        // Initial check
        toggleScrollButton();
    }
});
</script>