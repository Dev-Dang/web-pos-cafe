# Dev Guidelines (Client vs Shared)

## Shared (assets/shared)

Purpose:

- Shared base styles, layout primitives, and shared JS utilities/modules.

Rules:

- Do not modify Bootstrap source files in `assets/shared/bootstrap/**`. These are vendor files and must remain
  untouched.
- Do not edit compiled CSS directly (e.g., `assets/shared/styles/base.css`). Always update the SCSS source.
- Shared layout components live in `assets/shared/styles/` (header, footer, button, scrollbar, toast). Keep structure
  consistent across pages.
- Shared JS entry is `assets/shared/js/base.js`. It initializes shared utilities (modal system, logging, flash, etc).
- Reuse shared JS modules (e.g., `assets/shared/js/modules/modal.js`, `assets/shared/js/modules/toast.js`) instead of
  duplicating logic.

Files that must NOT be mutated:

- `assets/shared/bootstrap/**`
- `assets/shared/styles/base.css` (generated)

## Client (assets/client)

Purpose:

- Page-specific styling, UI components, and interactive features.

Rules:

- Only write styles in SCSS. Do not edit `assets/client/styles/app.css` directly (generated).
- Page styles live in `assets/client/styles/pages/`.
- Component styles live in `assets/client/styles/components/`.
- Avoid class name prefixes like `pos-` (use clean, shared naming).
- Icons use Flaticon UI via CDN, with the `icon-base` class as the baseline style.
- Keep JS modular under `assets/client/js/modules/`, and import/initialize from `assets/client/js/main.js`.
- Use `bootstrap.bundle.min.js` for dropdowns/modals/etc. Do not re-import shared modules in multiple places if
  `assets/shared/js/base.js` already initializes them.
- Use `data-*` hooks for JS bindings (e.g., `data-product-card`, `data-category-item`).
- Product modal should use the shared modal system (`Modal.open()`).
- Save cart state to localStorage (current key: `pos_cart`) for testing.

Files generated (do not edit directly):

- `assets/client/styles/app.css`

## Cross-cutting rules

- All changes should be made in SCSS/JS/HTML sources, then rebuild SCSS to update generated CSS.
- Keep structure responsive and consistent with design spec.
- Avoid destructive git operations and do not revert unrelated changes.
- Please make sure all number if posibile use all from `assets/shared/styles/base/_global.scss` and
  `assets/shared/styles/base/_variables.scss` for avoid magic number and keep consistent across all page for spacing,
  font size, padding, ... etc in a unifier system design (already description at `homepage-designsystem.md`)

## Server-Side Rendering Strategy (Servlet + JSP)

### Core Philosophy

We use **Hybrid SSR**: Server renders HTML (full pages or fragments), JavaScript swaps HTML without page reload.

### Rule 1: Initial Page Load → Full Server Rendering

```java
// Servlet returns complete HTML page via JSP
@WebServlet("/products")
public class ProductServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        req.setAttribute("products", productService.getAll());
        req.getRequestDispatcher("/WEB-INF/views/products.jsp").forward(req, resp);
    }
}
```

**Use for:** Homepage, product listings, checkout, any page needing SEO.

**Benefits:** Fast load, SEO-friendly, works without JavaScript.

### Rule 2: User Interactions → AJAX + HTML Fragments

```java
// Servlet returns HTML fragment for AJAX
@WebServlet("/cart/add")
public class CartServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        CartItem item = cartService.addItem(...);

        boolean isAjax = "XMLHttpRequest".equals(req.getHeader("X-Requested-With"));

        if (isAjax) {
            // Return fragment
            req.setAttribute("item", item);
            req.getRequestDispatcher("/WEB-INF/views/fragments/_cart-item.jsp")
                    .forward(req, resp);
        } else {
            // Fallback: full page redirect
            resp.sendRedirect("/cart");
        }
    }
}
```

```javascript
// JavaScript swaps HTML from server
async function addToCart(productId) {
    showSpinner();
    
    const response = await fetch('/cart/add', {
        method: 'POST',
        headers: { 'X-Requested-With': 'XMLHttpRequest' },
        body: formData
    });
    
    const html = await response.text(); // Server returns HTML
    document.querySelector('[data-cart-list]').insertAdjacentHTML('beforeend', html);
    
    hideSpinner();
}
```

**Use for:** Add to cart, update quantity, remove item, apply coupon, filters.

**Benefits:** No page reload, smooth UX, server controls rendering.

### Rule 4: Always Server-Side

**Translation & Formatting** must happen on server (never in JavaScript):

```jsp
<!-- Translation -->
<fmt:message key="cart.total" />

<!-- Price formatting -->
<fmt:formatNumber value="${product.price}" type="currency" />

<!-- Date formatting -->
<fmt:formatDate value="${order.date}" pattern="dd/MM/yyyy" />
```

### What NOT to Do

❌ Don't return JSON (creates duplicate rendering logic):

```java
// ❌ AVOID
resp.getWriter().write(new Gson().toJson(item));
```

❌ Don't fetch initial products via JavaScript:

```javascript
// ❌ AVOID
fetch('/api/products').then(r => r.json())...
```

✅ **DO:** Server renders HTML → JavaScript swaps HTML → No page reload.

### Summary

- **Initial load:** Server renders full page (fast, SEO-friendly)
- **Interactions:** Server renders fragments, JavaScript swaps HTML (smooth UX)
- **No JSON APIs:** Server always returns HTML (simple, maintainable)
- **Translation/Formatting:** Always server-side (single source of truth)