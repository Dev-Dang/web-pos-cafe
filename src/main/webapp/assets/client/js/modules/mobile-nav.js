/**
 * ------------------------------------------------------------
 * Module: Mobile Navigation
 * ------------------------------------------------------------
 * @description
 * Handles bottom mobile navigation and cart drawer open/close,
 * including cart count badge synced from localStorage.
 *
 * @example
 * import MobileNavigation from './mobile-nav.js';
 * new MobileNavigation();
 *
 * @version 1.0.1
 * @since 1.0.0
 * @lastModified 12/26/2025
 * @module mobile-nav
 * @author Dang Van Trung
 */
// Mobile Bottom Navigation & Cart Drawer Handler

class MobileNavigation {
  /** Create a new mobile navigation controller and auto-init. */
  constructor() {
    this.cartDrawer = document.querySelector("[data-mobile-cart]");
    this.cartToggle = document.querySelector("[data-cart-toggle]");
    this.cartCloses = document.querySelectorAll("[data-cart-close]");
    this.cartCount = document.querySelector("[data-cart-count]");

    this.init();
  }

  /**
   * Initialize event bindings for cart drawer and badge updates.
   */
  init() {
    if (!this.cartDrawer || !this.cartToggle) return;

    // Toggle cart drawer
    this.cartToggle.addEventListener("click", (e) => {
      e.preventDefault();
      this.openCart();
    });

    // Close cart drawer
    this.cartCloses.forEach((closeBtn) => {
      closeBtn.addEventListener("click", () => {
        this.closeCart();
      });
    });

    // Close on backdrop click
    const backdrop = this.cartDrawer.querySelector(
      ".mobile-cart-drawer__backdrop"
    );
    if (backdrop) {
      backdrop.addEventListener("click", () => {
        this.closeCart();
      });
    }

    // Update cart count from localStorage
    this.updateCartCount();

    // Listen for cart changes
    window.addEventListener("cartUpdated", () => {
      this.updateCartCount();
    });
  }

  /** Open the mobile cart drawer and prevent body scroll. */
  openCart() {
    this.cartDrawer.classList.add("active");
    document.body.style.overflow = "hidden";
  }

  /** Close the mobile cart drawer and restore body scroll. */
  closeCart() {
    this.cartDrawer.classList.remove("active");
    document.body.style.overflow = "";
  }

  /**
   * Read cart from localStorage, sum quantities, and update badge.
   */
  updateCartCount() {
    try {
      const cart = JSON.parse(localStorage.getItem("cart") || "[]");
      const totalItems = cart.reduce(
        (sum, item) => sum + (item.quantity || 1),
        0
      );

      if (this.cartCount) {
        this.cartCount.textContent = totalItems;
        this.cartCount.style.display = totalItems > 0 ? "flex" : "none";
      }
    } catch (error) {
      console.error("Error updating cart count:", error);
    }
  }
}

// Initialize when DOM is ready
if (document.readyState === "loading") {
  document.addEventListener("DOMContentLoaded", () => {
    new MobileNavigation();
  });
} else {
  new MobileNavigation();
}

export default MobileNavigation;
