/**
 * ------------------------------------------------------------
 * Module: Product Modal
 * ------------------------------------------------------------
 * @description
 * Manages product detail modal with options, quantity, and add-to-cart.
 * Uses shared Modal system for loading and display.
 *
 * @version 2.0.0
 * @since 1.0.0
 * @lastModified 28/12/2025
 * @module product-modal
 * @author Dang Van Trung
 */

import { ProductWebConstants } from "./web-constants.js";
import { addToCart, isLoggedIn, getGuestCart, formatPrice } from "./cart.js";

const parsePrice = (value) => {
  const numeric = (value || "").toString().replace(/[^\d]/g, "");
  return parseInt(numeric, 10) || 0;
};

/**
 * Custom URL generator for product modal (SEO-friendly path param)
 */
const productModalUrl = (productSlug) => {
  return `${ProductWebConstants.Endpoint.PRODUCT_MODAL}/${productSlug}`;
};

/**
 * Initializes product cards to open modal on click
 */
export function initProductModal() {
  const cards = document.querySelectorAll("[data-product-card]");

  cards.forEach((card) => {
    card.addEventListener("click", async () => {
      const productSlug = card.dataset.productSlug;
      if (!productSlug) return;

      // Silently ignore sold-out products (no toast)
      if (card.dataset.soldOut === "true") {
        return; // Do nothing - gray overlay already visible
      }

      try {
        // Fetch modal HTML from server
        const response = await fetch(productModalUrl(productSlug), {
          headers: { "Accept": "text/html" }
        });

        if (!response.ok) {
          throw new Error(`HTTP ${response.status}`);
        }

        const modalHTML = await response.text();
        
        // Inject modal into container
        const container = document.querySelector("#modal-container");
        if (!container) {
          console.error("Modal container not found");
          return;
        }

        container.innerHTML = modalHTML;
        const modalEl = container.querySelector(".modal");
        if (!modalEl) {
          console.error("Modal element not found in response");
          return;
        }

        // Get base price
        const priceEl = modalEl.querySelector("[data-modal-price]");
        const basePrice = parsePrice(priceEl?.textContent || "0");

        // Setup modal interactions
        setupModalHandlers(modalEl, basePrice);

        // Show modal
        const bsModal = bootstrap.Modal.getOrCreateInstance(modalEl);
        bsModal.show();

        // Cleanup on hide
        modalEl.addEventListener("hidden.bs.modal", () => {
          resetModalState(modalEl);
          modalEl.remove();
        }, { once: true });

      } catch (error) {
        console.error("Failed to load product modal:", error);
        showError("Không thể tải thông tin sản phẩm");
      }
    });
  });
}

/**
 * Sets up all modal event handlers
 */
function setupModalHandlers(modalEl, basePrice) {
  initOptionHandlers(modalEl);
  initQuantityStepper(modalEl);
  initAddToCartButton(modalEl, basePrice);
  updateTotals(modalEl, basePrice);
}

/**
 * Handles option selection (single/multi)
 */
function initOptionHandlers(modalEl) {
  const items = modalEl.querySelectorAll("[data-option-item]");

  items.forEach((item) => {
    item.addEventListener("click", () => {
      const group = item.dataset.optionGroup;
      const type = item.dataset.optionType;
      const section = item.closest(".product-modal__section");

      if (type === "single") {
        // Deselect all in group
        modalEl.querySelectorAll(`[data-option-group="${group}"]`)
          .forEach(el => {
            el.classList.remove("is-active");
            el.querySelector(".option-row__check")?.classList.remove("is-active");
          });
        // Select clicked
        item.classList.add("is-active");
      } else if (type === "multi") {
        // Toggle multi-select with max check
        const maxSelect = parseInt(section.querySelector(".product-modal__section-sub")
          ?.textContent.match(/\d+/)?.[0] || "999");
        const groupItems = modalEl.querySelectorAll(`[data-option-group="${group}"]`);
        const currentActive = Array.from(groupItems).filter(el => el.classList.contains("is-active")).length;
        
        // Clicking on already selected item (deselect)
        if (item.classList.contains("is-active")) {
          item.classList.remove("is-active");
          item.classList.remove("is-disabled");
          
          // Re-enable other disabled items
          groupItems.forEach(el => {
            if (!el.classList.contains("is-active")) {
              el.classList.remove("is-disabled");
            }
          });
        } 
        // Clicking on unselected item
        else {
          if (currentActive >= maxSelect) {
            return; // Max reached, do nothing
          }
          
          item.classList.add("is-active");
          
          // Check if max reached after this selection
          const newActive = currentActive + 1;
          if (newActive >= maxSelect) {
            // Disable unselected items
            groupItems.forEach(el => {
              if (!el.classList.contains("is-active")) {
                el.classList.add("is-disabled");
              }
            });
          }
        }
      }

      // Update check icon
      const check = item.querySelector(".option-row__check");
      if (check) {
        check.classList.toggle("is-active", item.classList.contains("is-active"));
      }

      updateSectionBadge(section);
      updateTotals(modalEl, getBasePrice(modalEl));
    });
  });
}

/**
 * Handles quantity +/- buttons
 */
function initQuantityStepper(modalEl) {
  const minus = modalEl.querySelector("[data-modal-minus]");
  const plus = modalEl.querySelector("[data-modal-plus]");
  const qtyEl = modalEl.querySelector("[data-modal-qty]");

  if (!qtyEl) return;

  const updateQty = (delta) => {
    const current = parseInt(qtyEl.textContent) || 1;
    const newQty = Math.max(1, Math.min(99, current + delta));
    qtyEl.textContent = newQty;
    updateTotals(modalEl, getBasePrice(modalEl));
  };

  minus?.addEventListener("click", () => updateQty(-1));
  plus?.addEventListener("click", () => updateQty(1));
}

/**
 * Handles add to cart button click
 */
function initAddToCartButton(modalEl, basePrice) {
  const actionBtn = modalEl.querySelector("[data-modal-action]");
  if (!actionBtn) return;

  actionBtn.addEventListener("click", async () => {
    // Validate required options
    const validation = validateRequired(modalEl);
    if (!validation.valid) {
      showError(`Vui lòng chọn ${validation.missing}`);
      highlightSection(modalEl, validation.missing);
      return;
    }

    // Build cart item
    const item = buildCartItem(modalEl, basePrice);
    
    // Save to cart
    await saveToCart(item);

    // Close modal
    bootstrap.Modal.getInstance(modalEl)?.hide();
  });
}

/**
 * Validates required option sections
 */
function validateRequired(modalEl) {
  const sections = modalEl.querySelectorAll(".product-modal__section");
  
  for (const section of sections) {
    const subEl = section.querySelector(".product-modal__section-sub");
    const isRequired = subEl?.textContent.includes("Bắt buộc") || subEl?.textContent.includes("Required");
    
    if (!isRequired) continue;

    const firstItem = section.querySelector("[data-option-item]");
    const group = firstItem?.dataset.optionGroup;
    const selected = section.querySelectorAll(`[data-option-group="${group}"].is-active`).length;

    if (selected === 0) {
      const headerText = section.querySelector(".product-modal__section-header span")?.textContent;
      return { valid: false, missing: headerText || "tùy chọn" };
    }
  }

  return { valid: true };
}

/**
 * Builds cart item object from modal state
 */
function buildCartItem(modalEl, basePrice) {
  const menuItemId = modalEl.dataset.menuItemId;
  const productSlug = modalEl.dataset.productSlug;
  const nameEl = modalEl.querySelector("[data-modal-name]");
  const imageEl = modalEl.querySelector("[data-modal-image]");
  const qtyEl = modalEl.querySelector("[data-modal-qty]");
  const noteEl = modalEl.querySelector("[data-modal-note]");

  const qty = parseInt(qtyEl?.textContent || "1");
  const optionsData = getSelectedOptions(modalEl);
  const optionsPrice = sumOptionPrices(optionsData.options);
  const total = (basePrice + optionsPrice) * qty;

  return {
    menuItemId: parseInt(menuItemId, 10) || 0,
    slug: productSlug || "",
    name: nameEl?.textContent?.trim() || "",
    image: imageEl?.src || "",
    qty,
    note: noteEl?.value?.trim() || "",
    basePrice,
    optionsPrice,
    total,
    options: optionsData.options,
    optionValueIds: optionsData.optionValueIds,
    optionLabels: optionsData.optionLabels
  };
}

/**
 * Gets selected options from modal
 * Returns options object, optionValueIds array, and optionLabels map
 */
function getSelectedOptions(modalEl) {
  const options = { single: {}, multi: {} };
  const optionValueIds = [];
  const optionLabels = {};
  const items = modalEl.querySelectorAll("[data-option-item].is-active");

  items.forEach((item) => {
    const group = item.dataset.optionGroup;
    const type = item.dataset.optionType;
    const value = item.dataset.optionValue;
    const valueId = item.dataset.optionValueId;
    const price = parsePrice(item.dataset.optionPrice);

    // Collect option value ID for server
    if (valueId) {
      optionValueIds.push(parseInt(valueId, 10));
    }

    // Collect labels for display
    if (!optionLabels[group]) {
      optionLabels[group] = [];
    }
    optionLabels[group].push(value);

    // Build options structure for display
    if (type === "single") {
      options.single[group] = { label: value, price };
    } else if (type === "multi") {
      if (!options.multi[group]) options.multi[group] = [];
      options.multi[group].push({ label: value, price });
    }
  });

  return { options, optionValueIds, optionLabels };
}

/**
 * Sums all option prices
 * @param {Object} options - The options object with single and multi properties
 */
function sumOptionPrices(options) {
  let total = 0;
  
  // Sum single-choice option prices
  for (const key in options.single) {
    const opt = options.single[key];
    total += opt.price || 0;
  }
  
  // Sum multi-choice option prices
  for (const key in options.multi) {
    const arr = options.multi[key];
    for (let i = 0; i < arr.length; i++) {
      total += arr[i].price || 0;
    }
  }
  
  return total;
}

/**
 * Updates total price display
 */
function updateTotals(modalEl, basePrice) {
  const qtyEl = modalEl.querySelector("[data-modal-qty]");
  const totalEl = modalEl.querySelector("[data-modal-total]");

  const qty = parseInt(qtyEl?.textContent || "1");
  const options = getSelectedOptions(modalEl);
  const total = (basePrice + sumOptionPrices(options)) * qty;

  if (totalEl) {
    totalEl.textContent = formatPrice(total);
  }
}

/**
 * Updates section badge state (valid/invalid)
 */
function updateSectionBadge(section) {
  const subEl = section.querySelector(".product-modal__section-sub");
  if (!subEl) return;

  const firstItem = section.querySelector("[data-option-item]");
  const type = firstItem?.dataset.optionType;
  const group = firstItem?.dataset.optionGroup;
  const selected = section.querySelectorAll(`[data-option-group="${group}"].is-active`).length;

  if (selected > 0) {
    subEl.classList.add("is-dirty", "is-valid");
    subEl.classList.remove("is-invalid");
  } else {
    subEl.classList.remove("is-dirty", "is-valid", "is-invalid");
  }
}

/**
 * Gets base price from modal
 */
function getBasePrice(modalEl) {
  const priceEl = modalEl.querySelector("[data-modal-price]");
  return parsePrice(priceEl?.textContent || "0");
}

/**
 * Resets modal to default state
 */
function resetModalState(modalEl) {
  const qtyEl = modalEl.querySelector("[data-modal-qty]");
  const noteEl = modalEl.querySelector("[data-modal-note]");

  if (qtyEl) qtyEl.textContent = "1";
  if (noteEl) noteEl.value = "";

  modalEl.querySelectorAll("[data-option-item]").forEach(item => {
    item.classList.remove("is-active");
    item.querySelector(".option-row__check")?.classList.remove("is-active");
  });
}

/**
 * Highlights a section (for validation feedback)
 */
function highlightSection(modalEl, sectionName) {
  const sections = modalEl.querySelectorAll(".product-modal__section");
  for (const section of sections) {
    const headerText = section.querySelector(".product-modal__section-header span")?.textContent;
    if (headerText?.includes(sectionName)) {
      section.scrollIntoView({ behavior: "smooth", block: "center" });
      section.classList.add("flash-highlight");
      setTimeout(() => section.classList.remove("flash-highlight"), 1800);
      break;
    }
  }
}

/**
 * Saves item to cart (guest localStorage or server for logged-in users)
 */
async function saveToCart(item) {
  try {
    // Use unified cart API from cart-new.js
    const result = await addToCart(item);
    
    if (result) {
      // Show success toast
      const { Toast } = await import("../../../shared/js/modules/toast.js");
      Toast.success("Đã thêm sản phẩm vào giỏ hàng");
    } else {
      throw new Error("Failed to add item to cart");
    }
  } catch (e) {
    console.error("Error saving to cart:", e);
    showError("Không thể thêm sản phẩm vào giỏ hàng");
  }
}

/**
 * Shows error toast
 */
async function showError(message) {
  try {
    const { Toast } = await import("../../../shared/js/modules/toast.js");
    Toast.error(message);
  } catch (e) {
    alert(message);
  }
}

/**
 * Opens product modal for editing an existing cart item
 * @param {Object} cartItem - The cart item to edit
 * @param {number} cartIndex - The index of the item in cart array
 */
export async function openProductModalForEdit(cartItem, cartIndex) {
  if (!cartItem || !cartItem.name) {
    console.error("Invalid cart item for edit");
    return;
  }

  try {
    // Build slug from product name (simplified - ideally store slug in cart item)
    const productSlug = cartItem.slug || cartItem.name.toLowerCase()
      .replace(/\s+/g, "-")
      .replace(/[^a-z0-9-]/g, "");

    // Fetch modal HTML from server
    const response = await fetch(productModalUrl(productSlug), {
      headers: { "Accept": "text/html" }
    });

    if (!response.ok) {
      throw new Error(`HTTP ${response.status}`);
    }

    const modalHTML = await response.text();

    // Inject modal into container
    const container = document.querySelector("#modal-container");
    if (!container) {
      console.error("Modal container not found");
      return;
    }

    container.innerHTML = modalHTML;
    const modalEl = container.querySelector(".modal");
    if (!modalEl) {
      console.error("Modal element not found in response");
      return;
    }

    // Get base price
    const priceEl = modalEl.querySelector("[data-modal-price]");
    const basePrice = parsePrice(priceEl?.textContent || "0");

    // Setup modal handlers
    setupModalHandlers(modalEl, basePrice);

    // Restore cart item state to modal
    restoreCartItemToModal(modalEl, cartItem);

    // Change action button to "Update" mode
    const actionBtn = modalEl.querySelector("[data-modal-action]");
    if (actionBtn) {
      actionBtn.textContent = "Cập nhật";
      
      // Remove existing click handler and add update handler
      const newActionBtn = actionBtn.cloneNode(true);
      actionBtn.parentNode.replaceChild(newActionBtn, actionBtn);
      
      newActionBtn.addEventListener("click", async () => {
        // Validate required options
        const validation = validateRequired(modalEl);
        if (!validation.valid) {
          showError(`Vui lòng chọn ${validation.missing}`);
          highlightSection(modalEl, validation.missing);
          return;
        }

        // Build updated cart item
        const updatedItem = buildCartItem(modalEl, basePrice);
        // Preserve the slug for future edits
        updatedItem.slug = cartItem.slug || productSlug;

        // For guest users, update localStorage directly
        // For logged-in users, this will need server-side update
        if (!isLoggedIn()) {
          const guestCart = getGuestCart();
          if (cartIndex >= 0 && cartIndex < guestCart.length) {
            guestCart[cartIndex] = updatedItem;
            // Re-save to localStorage via cart-new.js
            const { addToGuestCart } = await import("./cart-new.js");
            // Remove old item and add updated one
            guestCart.splice(cartIndex, 1, updatedItem);
            localStorage.setItem("cart_" + (document.body.getAttribute("data-store-id") || "default"), JSON.stringify(guestCart));
          }
        }

        // Close modal
        bootstrap.Modal.getInstance(modalEl)?.hide();

        // Reload cart display - trigger custom event
        window.dispatchEvent(new CustomEvent("cart:updated"));

        // Show success toast
        const { Toast } = await import("../../../shared/js/modules/toast.js");
        Toast.success("Đã cập nhật sản phẩm");
      });
    }

    // Update totals with restored values
    updateTotals(modalEl, basePrice);

    // Show modal
    const bsModal = bootstrap.Modal.getOrCreateInstance(modalEl);
    bsModal.show();

    // Cleanup on hide
    modalEl.addEventListener("hidden.bs.modal", () => {
      resetModalState(modalEl);
      modalEl.remove();
    }, { once: true });

  } catch (error) {
    console.error("Failed to open product modal for edit:", error);
    showError("Không thể tải thông tin sản phẩm để chỉnh sửa");
  }
}

/**
 * Restores cart item state to modal (quantity, options, note)
 */
function restoreCartItemToModal(modalEl, cartItem) {
  // Restore quantity
  const qtyEl = modalEl.querySelector("[data-modal-qty]");
  if (qtyEl && cartItem.qty) {
    qtyEl.textContent = cartItem.qty.toString();
  }

  // Restore note
  const noteEl = modalEl.querySelector("[data-modal-note]");
  if (noteEl && cartItem.note) {
    noteEl.value = cartItem.note;
  }

  // Restore options
  if (cartItem.options) {
    // Restore single-choice options
    if (cartItem.options.single) {
      for (const group in cartItem.options.single) {
        const optionData = cartItem.options.single[group];
        const label = typeof optionData === "object" ? optionData.label : optionData;
        
        const items = modalEl.querySelectorAll(`[data-option-group="${group}"]`);
        items.forEach(item => {
          if (item.dataset.optionValue === label) {
            item.classList.add("is-active");
            const check = item.querySelector(".option-row__check");
            if (check) check.classList.add("is-active");
          }
        });
      }
    }

    // Restore multi-choice options
    if (cartItem.options.multi) {
      for (const group in cartItem.options.multi) {
        const values = cartItem.options.multi[group];
        if (!Array.isArray(values)) continue;

        const items = modalEl.querySelectorAll(`[data-option-group="${group}"]`);
        items.forEach(item => {
          const matchValue = values.some(v => {
            const label = typeof v === "object" ? v.label : v;
            return item.dataset.optionValue === label;
          });
          
          if (matchValue) {
            item.classList.add("is-active");
            const check = item.querySelector(".option-row__check");
            if (check) check.classList.add("is-active");
          }
        });
      }
    }
  }

  // Update section badges for restored options
  const sections = modalEl.querySelectorAll(".product-modal__section");
  sections.forEach(section => updateSectionBadge(section));
}
