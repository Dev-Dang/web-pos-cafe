/**
 * ------------------------------------------------------------
 * Module: Client App Bootstrap
 * ------------------------------------------------------------
 * @description
 * Entry point for client-side features. Wires up UI modules
 * on DOMContentLoaded using simple, predictable initialization.
 *
 * @example
 * // Executed automatically after DOM is ready
 *
 * @version 1.0.3
 * @since 1.0.0
 * @lastModified 01/01/2026
 * @module app-main
 * @author Dang Van Trung
 */
import { initStoreDetection } from "./modules/store-detect.js";
import { initCategorySlider } from "./modules/swiper.js";
import { initCart } from "./modules/cart.js";
import { initProductModal } from "./modules/product-modal.js";
import { initProductCards } from "./modules/product-card.js";
import { initCatalogSwitcher } from "./modules/catalog.js";
import { initProductSearch } from "./modules/search.js";

initStoreDetection();

document.addEventListener("DOMContentLoaded", () => {
  initCategorySlider();
  initCart();
  initProductModal();
  initProductCards();
  initCatalogSwitcher();
  initProductSearch();
});
