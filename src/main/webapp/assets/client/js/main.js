import {initStoreDetection} from "./modules/store-detect.js";
import {initCategorySlider} from "./modules/swiper.js";
import {initProductSearch} from "./modules/search.js";
import {initUnpoly} from "./modules/unpoly.js";
import {initInfiniteScroll} from "./modules/infinite-scroll.js";
import {initFormSuccessDelay} from "./modules/form-success-delay.js";
import {initOrderHistory} from "./modules/order-history.js";
import {initPaymentSuccessAutoClose} from "./modules/payment-success.js";
import "./modules/product-modal.js";

// Initialize core modules
initStoreDetection();
initUnpoly();
initProductSearch();
initFormSuccessDelay();
initOrderHistory();
initPaymentSuccessAutoClose();

// Initialize Unpoly-aware modules
if (typeof up !== 'undefined') {
    // Category slider initialization for Unpoly
    up.compiler('[data-category-slider]', (slider) => {
        initCategorySlider(slider);
    });

    // Modal close handler for Unpoly modals
    up.compiler('.product-modal__close', (button) => {
        button.addEventListener('click', (e) => {
            e.preventDefault();
            up.layer.dismiss();
        });
    });

    // Mobile back button handler
    up.compiler('.product-modal__mobile-header__back', (button) => {
        button.addEventListener('click', (e) => {
            e.preventDefault();
            up.layer.dismiss();
        });
    });

    // Infinite scroll
    up.compiler('[data-load-trigger]', (trigger) => {
        initInfiniteScroll(trigger);
    });
}

