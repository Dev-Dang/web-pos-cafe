/**
 * ------------------------------------------------------------
 * Module: Cart Service
 * ------------------------------------------------------------
 * @description
 * Manages cart operations for both guest and logged-in users.
 * - Guest users: LocalStorage as source of truth
 * - Logged-in users: Server as source of truth with SSR fragments
 *
 * @version 2.0.0
 * @since 1.0.0
 * @lastModified 30/12/2025
 * @module cart
 * @author Dang Van Trung
 */

import {CartWebConstants} from './web-constants.js';
import * as Cookie from '../../../shared/js/modules/cookie.js';

// ==========================================================
// CONSTANTS
// ==========================================================

const GUEST_CART_PREFIX = CartWebConstants.LocalStorage.GUEST_CART_PREFIX;

// ==========================================================
// STATE
// ==========================================================

/**
 * Check if user is logged in by looking for auth indicator in DOM.
 * The server sets a data attribute on body when user is authenticated.
 */
function isLoggedIn() {
    return document.body.hasAttribute('data-user-authenticated');
}

/**
 * Get current store ID from cookie or data attribute.
 */
function getStoreId() {
    const storeId = Cookie.get('store_id');
    if (storeId) {
        return parseInt(storeId, 10);
    }
    const bodyStoreId = document.body.getAttribute('data-store-id');
    if (bodyStoreId) {
        return parseInt(bodyStoreId, 10);
    }
    return null;
}

/**
 * Build the localStorage key for guest cart.
 */
function getGuestCartKey() {
    const storeId = getStoreId();
    return storeId ? GUEST_CART_PREFIX + storeId : 'cart_default';
}

// ==========================================================
// UTILITY FUNCTIONS
// ==========================================================

/**
 * Validates and extracts item identifier for cart operations.
 * @param {HTMLElement} element - Button element with data attributes
 * @param {boolean} isLoggedInUser - Whether user is logged in
 * @returns {Object} {success: boolean, value: number, type: 'id'|'index'}
 */
function getCartItemIdentifier(element, isLoggedInUser) {
    if (isLoggedInUser) {
        const itemId = element.getAttribute('data-item-id');

        if (!itemId || itemId === 'null' || itemId === 'undefined' || itemId.trim() === '') {
            console.error('[Cart] Missing item ID:', {
                itemId: itemId,
                element: element,
                allAttributes: Array.from(element.attributes).map(attr => `${attr.name}="${attr.value}"`).join(', ')
            });
            return {success: false, error: `Missing item ID. Found: "${itemId}"`};
        }

        const numericId = parseInt(itemId.trim(), 10);

        if (isNaN(numericId) || numericId <= 0) {
            console.error('[Cart] Invalid item ID:', {
                original: itemId,
                trimmed: itemId.trim(),
                parsed: numericId,
                isNaN: isNaN(numericId),
                isPositive: numericId > 0
            });
            return {success: false, error: `Invalid item ID: "${itemId}" -> ${numericId}`};
        }

        return {success: true, value: numericId, type: 'id'};
    } else {
        const itemIndex = element.getAttribute('data-item-index');
        const numericIndex = parseInt(itemIndex, 10);
        if (isNaN(numericIndex) || numericIndex < 0) {
            return {success: false, error: `Invalid item index: ${itemIndex}`};
        }

        return {success: true, value: numericIndex, type: 'index'};
    }
}

/**
 * Show toast message using the shared Toast module
 */
function showToast(message, type = 'info') {
    // Use window.Toast if available (loaded by main layout), otherwise fallback to console
    if (typeof window.Toast !== 'undefined') {
        switch (type) {
            case 'success':
                window.Toast.success(message);
                break;
            case 'error':
                window.Toast.error(message);
                break;
            case 'warning':
                window.Toast.warn(message);
                break;
            case 'info':
            default:
                window.Toast.info(message);
                break;
        }
    } else {
        // Fallback to console and simple DOM toast
        const toast = document.createElement('div');
        toast.className = `cart-toast cart-toast--${type}`;
        toast.style.cssText = `
            position: fixed;
            top: 20px;
            right: 20px;
            background: ${type === 'success' ? '#22c55e' : type === 'error' ? '#ef4444' : type === 'warning' ? '#f59e0b' : '#3b82f6'};
            color: white;
            padding: 12px 24px;
            border-radius: 8px;
            z-index: 10000;
            box-shadow: 0 4px 12px rgba(0,0,0,0.15);
            font-weight: 500;
            max-width: 300px;
        `;
        toast.textContent = message;
        document.body.appendChild(toast);

        setTimeout(() => {
            if (toast.parentNode) {
                toast.parentNode.removeChild(toast);
            }
        }, 5000);
    }
}

// ==========================================================
// GUEST CART (LocalStorage)
// ==========================================================

/**
 * Get guest cart from localStorage.
 * @returns {Array} Cart items array
 */
function getGuestCart() {
    const key = getGuestCartKey();
    try {
        const data = localStorage.getItem(key);
        return data ? JSON.parse(data) : [];
    } catch (e) {
        console.error('Failed to parse guest cart:', e);
        return [];
    }
}

/**
 * Save guest cart to localStorage.
 * @param {Array} cart - Cart items array
 */
function saveGuestCart(cart) {
    const key = getGuestCartKey();
    try {
        localStorage.setItem(key, JSON.stringify(cart));
    } catch (e) {
        console.error('Failed to save guest cart:', e);
    }
}

/**
 * Clear guest cart from localStorage.
 */
function clearGuestCart() {
    const key = getGuestCartKey();
    localStorage.removeItem(key);
}

/**
 * Generate a hash for a guest cart item for deduplication.
 * Simple client-side hash (server will regenerate proper hash).
 */
function generateGuestItemKey(menuItemId, optionValueIds, note) {
    const optionsPart = optionValueIds ? optionValueIds.slice().sort().join(',') : '';
    const notePart = note ? note.trim().toLowerCase() : '';
    return `${menuItemId}|${optionsPart}|${notePart}`;
}

/**
 * Add item to guest cart.
 */
function addToGuestCart(item) {
    const cart = getGuestCart();
    const itemKey = generateGuestItemKey(
        item.menuItemId,
        item.optionValueIds,
        item.note
    );

    // Find existing item with same key
    let existingIndex = -1;
    for (let i = 0; i < cart.length; i++) {
        const existingKey = generateGuestItemKey(
            cart[i].menuItemId,
            cart[i].optionValueIds,
            cart[i].note
        );
        if (existingKey === itemKey) {
            existingIndex = i;
            break;
        }
    }

    if (existingIndex >= 0) {
        // Increase quantity
        cart[existingIndex].qty = (cart[existingIndex].qty || 1) + (item.qty || 1);
    } else {
        // Add new item
        cart.push({
            menuItemId: item.menuItemId,
            name: item.name,
            image: item.image,
            qty: item.qty || 1,
            basePrice: item.basePrice,
            optionsPrice: item.optionsPrice || 0,
            optionValueIds: item.optionValueIds || [],
            optionLabels: item.optionLabels || {},
            note: item.note || null
        });
    }

    saveGuestCart(cart);
    return cart;
}

/**
 * Update item quantity in guest cart.
 */
function updateGuestCartItem(index, newQty) {
    const cart = getGuestCart();

    // Validate inputs
    if (isNaN(index) || index < 0 || index >= cart.length) {
        console.error('[Cart] Invalid index for guest cart update:', index, 'cart length:', cart.length);
        return cart;
    }

    if (isNaN(newQty) || newQty < 0) {
        console.error('[Cart] Invalid quantity for guest cart update:', newQty);
        return cart;
    }

    if (newQty === 0) {
        // Remove item when quantity is 0
        cart.splice(index, 1);
    } else {
        // Update quantity
        cart[index].qty = newQty;
    }

    saveGuestCart(cart);
    return cart;
}

/**
 * Remove item from guest cart.
 */
function removeFromGuestCart(index) {
    const cart = getGuestCart();

    // Validate input
    if (isNaN(index) || index < 0 || index >= cart.length) {
        console.error('[Cart] Invalid index for guest cart remove:', index, 'cart length:', cart.length);
        return cart;
    }

    cart.splice(index, 1);
    saveGuestCart(cart);
    return cart;
}

// ==========================================================
// SERVER CART (Logged-in users)
// ==========================================================

/**
 * Make an AJAX POST request to cart endpoint.
 * Returns the HTML fragment response.
 * Uses URL-encoded form data (not multipart) for simplicity.
 */
async function postToCart(endpoint, params) {
    try {
        // Convert params object to URL-encoded string
        const body = new URLSearchParams(params).toString();

        const response = await fetch(endpoint, {
            method: 'POST',
            headers: {
                'X-Requested-With': 'XMLHttpRequest',
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: body
        });

        if (response.status === 401) {
            // Session expired, reload page to redirect to login
            window.location.reload();
            return null;
        }

        if (!response.ok) {
            const errorText = await response.text();
            console.error('[Cart] API Error:', {
                status: response.status,
                statusText: response.statusText,
                endpoint: endpoint,
                params: params,
                error: errorText
            });
            return null;
        }

        // Get cart count/total from headers
        const cartCount = response.headers.get('X-Cart-Count');
        const cartTotal = response.headers.get('X-Cart-Total');

        // Get HTML fragment
        const html = await response.text();

        const result = {
            html: html,
            cartCount: cartCount ? parseInt(cartCount, 10) : 0,
            cartTotal: cartTotal ? parseInt(cartTotal, 10) : 0
        };

        return result;
    } catch (e) {
        console.error('[Cart] Network Error:', {
            endpoint: endpoint,
            params: params,
            error: e.message
        });
        return null;
    }
}

/**
 * Add item to server cart.
 */
async function addToServerCart(item) {
    const params = {
        menuItemId: item.menuItemId,
        qty: item.qty || 1
    };

    if (item.note) {
        params.note = item.note;
    }

    if (item.optionValueIds && item.optionValueIds.length > 0) {
        params.optionValueIds = item.optionValueIds.join(',');
    }

    return await postToCart(CartWebConstants.Endpoint.CART_ADD, params);
}

/**
 * Update item quantity on server.
 */
async function updateServerCartItem(itemId, newQty) {
    // Validate inputs
    if (!itemId || isNaN(itemId) || itemId <= 0) {
        console.error('[Cart] Invalid itemId for server update:', itemId);
        return null;
    }

    if (!newQty || isNaN(newQty) || newQty <= 0) {
        console.error('[Cart] Invalid quantity for server update:', newQty);
        return null;
    }

    const params = {
        itemId: itemId,
        qty: newQty
    };

    return await postToCart(CartWebConstants.Endpoint.CART_UPDATE, params);
}

/**
 * Remove item from server cart.
 */
async function removeFromServerCart(itemId) {
    // Validate input
    if (!itemId || isNaN(itemId) || itemId <= 0) {
        console.error('[Cart] Invalid itemId for server remove:', itemId);
        return null;
    }

    const params = {
        itemId: itemId
    };

    return await postToCart(CartWebConstants.Endpoint.CART_REMOVE, params);
}

/**
 * Clear all items from server cart.
 */
async function clearServerCart() {
    return await postToCart(CartWebConstants.Endpoint.CART_CLEAR, {});
}

/**
 * Merge guest cart into server cart on login.
 */
async function mergeGuestCartToServer() {
    const guestCart = getGuestCart();
    if (guestCart.length === 0) {
        return null;
    }

    const params = {
        guestCart: JSON.stringify(guestCart)
    };

    const result = await postToCart(CartWebConstants.Endpoint.CART_MERGE, params);

    if (result) {
        // Clear guest cart after successful merge
        clearGuestCart();
    }

    return result;
}

// ==========================================================
// UNIFIED CART API
// ==========================================================

/**
 * Add item to cart (guest or server based on auth state).
 * @param {Object} item - Cart item to add
 * @returns {Promise<Object|Array>} Updated cart or HTML response
 */
export async function addToCart(item) {
    if (isLoggedIn()) {
        const result = await addToServerCart(item);
        if (result) {
            updateCartUI(result);
        }
        return result;
    } else {
        const cart = addToGuestCart(item);
        renderGuestCart(cart);
        return cart;
    }
}

/**
 * Update item quantity in cart.
 * @param {number|string} itemIdOrIndex - Item ID (server) or index (guest)
 * @param {number} newQty - New quantity
 */
export async function updateCartItem(itemIdOrIndex, newQty) {
    try {
        if (isLoggedIn()) {
            const result = await updateServerCartItem(itemIdOrIndex, newQty);
            if (result) {
                updateCartUI(result);
            } else {
                console.error('[Cart] Failed to update server cart item');
                showToast('Không thể cập nhật sản phẩm', 'error');
            }
            return result;
        } else {
            const cart = updateGuestCartItem(itemIdOrIndex, newQty);
            renderGuestCart(cart);
            return cart;
        }
    } catch (error) {
        console.error('[Cart] Error updating cart item:', error);
        showToast('Có lỗi xảy ra khi cập nhật giỏ hàng', 'error');
        return null;
    }
}

/**
 * Remove item from cart.
 * @param {number|string} itemIdOrIndex - Item ID (server) or index (guest)
 */
export async function removeCartItem(itemIdOrIndex) {
    try {
        if (isLoggedIn()) {
            const result = await removeFromServerCart(itemIdOrIndex);
            if (result) {
                updateCartUI(result);
            } else {
                console.error('[Cart] Failed to remove server cart item');
                showToast('Không thể xóa sản phẩm', 'error');
            }
            return result;
        } else {
            const cart = removeFromGuestCart(itemIdOrIndex);
            renderGuestCart(cart);
            return cart;
        }
    } catch (error) {
        console.error('[Cart] Error removing cart item:', error);
        showToast('Có lỗi xảy ra khi xóa sản phẩm', 'error');
        return null;
    }
}

/**
 * Clear entire cart.
 */
export async function clearCart() {
    if (isLoggedIn()) {
        const result = await clearServerCart();
        if (result) {
            updateCartUI(result);
        }
        return result;
    } else {
        clearGuestCart();
        renderGuestCart([]);
        return [];
    }
}

// ==========================================================
// UI RENDERING
// ==========================================================

/**
 * Format price as Vietnamese currency.
 */
function formatPrice(value) {
    if (typeof value === 'number') {
        return value.toLocaleString('vi-VN') + 'đ';
    }
    return value;
}

/**
 * Update cart UI with server response (HTML fragment).
 * The server returns cart-sidebar.jsp which contains [data-cart-sidebar] wrapper.
 * home.jsp has a different structure with [data-cart-list] for items.
 */
function updateCartUI(response) {
    if (!response) {
        return;
    }

    // Parse the response HTML
    var temp = document.createElement('div');
    temp.innerHTML = response.html;

    // Try to find the items container or empty state
    var serverItems = temp.querySelector('[data-cart-items]');
    var serverBody = temp.querySelector('.cart-sidebar__body');

    // Get the content to inject into cart list containers
    var contentHtml = '';
    if (serverItems) {
        // Has items - use the items innerHTML
        contentHtml = serverItems.innerHTML;
    } else if (serverBody) {
        // Empty state - get the empty div or body content
        contentHtml = serverBody.innerHTML;
    } else {
        // Fallback - use raw HTML
        contentHtml = response.html;
    }

    // Update all cart list containers (home.jsp uses data-cart-list)
    var cartLists = document.querySelectorAll('[data-cart-list]');
    for (var i = 0; i < cartLists.length; i++) {
        cartLists[i].innerHTML = contentHtml;
    }

    // Update data-cart-sidebar if it exists (full fragment replacement for cart page)
    var cartSidebar = document.querySelector('[data-cart-sidebar]');
    var serverSidebar = temp.querySelector('[data-cart-sidebar]');
    if (cartSidebar && serverSidebar) {
        cartSidebar.outerHTML = serverSidebar.outerHTML;
    }

    // Update cart badge/count
    updateCartBadge(response.cartCount);

    // Update cart total displays
    var totalEls = document.querySelectorAll('[data-cart-total]');
    for (var k = 0; k < totalEls.length; k++) {
        totalEls[k].textContent = formatPrice(response.cartTotal);
    }
}

/**
 * Update cart count badge.
 */
function updateCartBadge(count) {
    var badges = document.querySelectorAll('[data-cart-badge]');
    for (var i = 0; i < badges.length; i++) {
        var badge = badges[i];
        if (count > 0) {
            badge.textContent = count > 99 ? '99+' : count.toString();
            badge.style.display = 'flex';
        } else {
            badge.style.display = 'none';
        }
    }

    var countElements = document.querySelectorAll('[data-cart-count]');
    for (var j = 0; j < countElements.length; j++) {
        countElements[j].textContent = count.toString();
    }
}

/**
 * Render guest cart from localStorage.
 * Updates all cart list containers (desktop sidebar + mobile drawer).
 */
function renderGuestCart(cart) {
    // Find all cart list containers
    var cartLists = document.querySelectorAll('[data-cart-list]');
    if (cartLists.length === 0) {
        // Fallback to data-cart-items
        cartLists = document.querySelectorAll('[data-cart-items]');
    }
    if (cartLists.length === 0) return;

    var isEmpty = !cart || cart.length === 0;
    var html = '';
    var totalQty = 0;
    var totalPrice = 0;

    if (isEmpty) {
        html = '<div class="cart-empty">' +
            '<div class="cart-empty__icon"><i class="fi fi-rr-shopping-cart icon-base"></i></div>' +
            '<p class="cart-empty__text">Giỏ hàng trống</p>' +
            '</div>';
    } else {
        for (var i = 0; i < cart.length; i++) {
            var item = cart[i];
            var lineTotal = ((item.basePrice || 0) + (item.optionsPrice || 0)) * (item.qty || 1);
            totalQty += item.qty || 1;
            totalPrice += lineTotal;

            // Build options display
            var optionsHtml = '';
            if (item.optionLabels) {
                var labels = [];
                for (var key in item.optionLabels) {
                    if (item.optionLabels.hasOwnProperty(key)) {
                        var labelVal = item.optionLabels[key];
                        if (Array.isArray(labelVal)) {
                            labels.push(labelVal.join(', '));
                        } else {
                            labels.push(labelVal);
                        }
                    }
                }
                if (labels.length > 0) {
                    optionsHtml = '<div class="cart-item__options">' + labels.join(', ') + '</div>';
                }
            }

            var noteHtml = '';
            if (item.note) {
                noteHtml = '<div class="cart-item__note"><i class="fi fi-rr-comment-alt icon-base"></i><span>' + item.note + '</span></div>';
            }

            html += '<div class="cart-item" data-cart-item data-item-index="' + i + '">' +
                '<div class="cart-item__info">' +
                '<div class="cart-item__name">' + (item.name || '') + '</div>' +
                optionsHtml +
                noteHtml +
                '<div class="cart-item__price">' + formatPrice((item.basePrice || 0) + (item.optionsPrice || 0)) + '</div>' +
                '</div>' +
                '<div class="cart-item__controls">' +
                '<div class="cart-item__qty-controls">' +
                '<button type="button" class="cart-item__qty-btn" data-qty-action="decrease" data-item-index="' + i + '">' +
                '<i class="fi fi-rr-minus icon-base"></i>' +
                '</button>' +
                '<span class="cart-item__qty" data-item-qty>' + (item.qty || 1) + '</span>' +
                '<button type="button" class="cart-item__qty-btn" data-qty-action="increase" data-item-index="' + i + '">' +
                '<i class="fi fi-rr-plus icon-base"></i>' +
                '</button>' +
                '</div>' +
                '<div class="cart-item__line-total">' + formatPrice(lineTotal) + '</div>' +
                '<button type="button" class="cart-item__remove" data-remove-item data-item-index="' + i + '" title="Xóa">' +
                '<i class="fi fi-rr-trash icon-base"></i>' +
                '</button>' +
                '</div>' +
                '</div>';
        }
    }

    // Update all cart list containers
    for (var j = 0; j < cartLists.length; j++) {
        cartLists[j].innerHTML = html;
    }

    // Update summary for all total elements
    var totalEls = document.querySelectorAll('[data-cart-total]');
    for (var k = 0; k < totalEls.length; k++) {
        totalEls[k].textContent = formatPrice(totalPrice);
    }

    updateCartBadge(totalQty);
}

// ==========================================================
// EVENT HANDLERS
// ==========================================================

/**
 * Handle quantity button clicks.
 */
async function handleQtyButtonClick(e) {
    const btn = e.target.closest('[data-qty-action]');
    if (!btn) return;

    e.preventDefault();
    e.stopPropagation();

    // Get current quantity
    const qtyEl = btn.closest('.cart-item__qty-controls').querySelector('[data-item-qty]');
    const currentQty = parseInt(qtyEl.textContent, 10) || 1;

    // Calculate new quantity
    const action = btn.getAttribute('data-qty-action');
    let newQty = action === 'increase' ? currentQty + 1 : currentQty - 1;

    // Validate quantity
    if (newQty < 0) {
        return;
    }

    // Get item identifier
    const identifier = getCartItemIdentifier(btn, isLoggedIn());

    if (!identifier.success) {
        console.error('[Cart] Identifier validation failed:', identifier.error);
        showToast('Debug: ' + identifier.error, 'error');
        return;
    }

    // Perform action
    if (newQty === 0) {
        await removeCartItem(identifier.value);
    } else {
        await updateCartItem(identifier.value, newQty);
    }
}

/**
 * Handle remove button clicks.
 */
async function handleRemoveButtonClick(e) {
    const btn = e.target.closest('[data-remove-item]');
    if (!btn) return;

    e.preventDefault();
    e.stopPropagation();

    // Get item identifier
    const identifier = getCartItemIdentifier(btn, isLoggedIn());
    if (!identifier.success) {
        console.error('[Cart]', identifier.error);
        showToast('Có lỗi với dữ liệu sản phẩm', 'error');
        return;
    }

    await removeCartItem(identifier.value);
}

/**
 * Handle clear cart button clicks.
 */
async function handleClearCartClick(e) {
    const btn = e.target.closest('[data-cart-clear]');
    if (!btn) return;

    e.preventDefault();
    e.stopPropagation();

    if (confirm('Bạn có chắc muốn xóa tất cả sản phẩm trong giỏ hàng?')) {
        await clearCart();
    }
}

/**
 * Handle checkout button clicks.
 */
async function handleCheckoutClick(e) {
    const btn = e.target.closest('[data-checkout-btn]');
    if (!btn) return;

    e.preventDefault();
    e.stopPropagation();

    // Disable button during processing
    btn.disabled = true;
    btn.innerHTML = '<i class="fi fi-rr-spinner icon-base"></i> Đang xử lý...';

    try {
        if (isLoggedIn()) {
            const response = await fetch(CartWebConstants.Endpoint.CHECKOUT, {
                method: 'POST',
                headers: {
                    'X-Requested-With': 'XMLHttpRequest',
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            });

            if (response.ok) {
                const result = await response.json();
                if (result.success) {
                    // Show success toast
                    showToast('Đặt hàng thành công! Hóa đơn đang được tải xuống...', 'success');

                    // Open invoice in new tab
                    window.open(`${CartWebConstants.Endpoint.INVOICE}?orderId=${result.orderId}`, '_blank');

                    // Refresh cart UI
                    updateCartBadge(0);
                    var cartLists = document.querySelectorAll('[data-cart-list]');
                    for (var i = 0; i < cartLists.length; i++) {
                        cartLists[i].innerHTML = '<div class="cart-empty">' +
                            '<div class="cart-empty__icon"><i class="fi fi-rr-shopping-cart icon-base"></i></div>' +
                            '<p class="cart-empty__text">Giỏ hàng trống</p>' +
                            '</div>';
                    }
                } else {
                    showToast('Có lỗi xảy ra khi đặt hàng', 'error');
                }
            } else {
                showToast('Có lỗi xảy ra khi đặt hàng', 'error');
            }
        } else {
            showToast('Vui lòng đăng nhập để tiếp tục', 'warning');
        }
    } catch (error) {
        console.error('Checkout error:', error);
        showToast('Có lỗi xảy ra khi đặt hàng', 'error');
    } finally {
        // Re-enable button
        btn.disabled = false;
        btn.innerHTML = 'Thanh toán';
    }
}

// ==========================================================
// INITIALIZATION
// ==========================================================

/**
 * Initialize cart module.
 * - Loads cart from localStorage for guests
 * - Triggers merge if user just logged in
 */
export async function initCart() {
    // Debug: Check existing cart items in DOM
    const existingCartItems = document.querySelectorAll('[data-cart-item]');
    existingCartItems.forEach((item, index) => {
    });

    // Check if merge is needed (flag set by server after login)
    const needsMerge = document.body.hasAttribute('data-needs-cart-merge');

    if (isLoggedIn()) {
        if (needsMerge) {
            // Merge guest cart to server
            const result = await mergeGuestCartToServer();
            if (result) {
                updateCartUI(result);
            }
            // Remove the flag
            document.body.removeAttribute('data-needs-cart-merge');
        }
        // Logged-in users get cart from server via SSR, no need to render here
    } else {
        // Render guest cart from localStorage
        const cart = getGuestCart();
        renderGuestCart(cart);
    }

    // Bind event handlers using event delegation
    document.addEventListener('click', async (e) => {
        await handleQtyButtonClick(e);
        await handleRemoveButtonClick(e);
        await handleClearCartClick(e);
        await handleCheckoutClick(e);
    });

    // Listen for cart:updated event to refresh cart display
    window.addEventListener('cart:updated', () => {
        if (!isLoggedIn()) {
            const cart = getGuestCart();
            renderGuestCart(cart);
        }
    });
}

// Export for use in other modules (e.g., product-modal.js)
export {
    isLoggedIn,
    getGuestCart,
    addToGuestCart,
    formatPrice
};

