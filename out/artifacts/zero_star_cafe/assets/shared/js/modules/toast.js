/**
 * ------------------------------------------------------------
 * Module: Simple Toast Notification System
 * ------------------------------------------------------------
 * @description
 * A lightweight, dependency-free toast notification system.
 * Each toast appears dynamically in a configurable position,
 * auto-hides after a lifetime, and supports hover-to-pause.
 *
 * Designed for modular projects using ES Modules.
 *
 * @example
 * Toast.success("Profile updated successfully!");
 * Toast.error("Failed to load data.");
 *
 * @version 1.0.0
 * @since 1.0.0
 * @lastModified 25/10/2025
 * @module toast
 * @author Dang Van Trung
 */

import {getOrCreateContainer} from './utils.js';

/** Default configuration values */
const CONFIG = {
    LIFETIME: 5000,
    MAX: 3,
    GAP: 16,
    CONTAINER_SELECTOR: '.toast-container',
    DEFAULT_POSITION: 'top-center',
};

/** Icon mapping per toast type */
const ICONS = {
    success: 'fi-rr-check-circle',
    error: 'fi-rr-exclamation',
    warning: 'fi-rr-warning',
    info: 'fi-rr-bell',
    normal: 'fi-rr-megaphone',
    default: 'fi-rr-info',
};

/**
 * Returns a human-readable time difference (e.g., "10s", "3m").
 * @param {number} ms - The timestamp in milliseconds.
 * @returns {string}
 */
function timeAgo(ms) {
    const seconds = Math.floor((Date.now() - ms) / 1000);
    const units = [
        {t: 60, s: 's'},
        {t: 60, s: 'm'},
        {t: 24, s: 'h'},
        {t: Infinity, s: 'd'},
    ];

    let value = seconds;
    for (const {t, s} of units) {
        if (value < t) return `${value}${s}`;
        value = Math.floor(value / t);
    }
}

/**
 * Creates and displays a toast message.
 * @param {string} type - Toast type ("success", "error", etc.)
 * @param {string} title - Optional toast title.
 * @param {string} desc - Toast description text.
 * @param {string} [position=CONFIG.DEFAULT_POSITION] - Position class (e.g., "top-center").
 */
export function createToast(type, title, desc, position = CONFIG.DEFAULT_POSITION) {
    const selector = `${CONFIG.CONTAINER_SELECTOR}.${position}`;
    const containerEl = getOrCreateContainer(selector);
    containerEl.classList.add(position);

    // --- Build Toast Element ---
    const toastEl = document.createElement('div');
    toastEl.className = `toast toast-${type}`;
    toastEl.setAttribute('role', 'alert');
    toastEl.setAttribute('aria-live', 'assertive');
    toastEl.setAttribute('aria-atomic', 'true');

    // Body
    const body = document.createElement('div');
    body.className = 'toast-body d-flex align-items-center justify-content-between';

    // Left (icon + text)
    const icon = document.createElement('i');
    icon.className = `icon-base me-3 toast-icon ${ICONS[type] || ICONS.default}`;

    const text = document.createElement('div');
    text.className = 'toast-text w-100';
    if (title) {
        const titleEl = Object.assign(document.createElement('p'), {
            className: 'toast-title mb-1 fw-bolder',
            textContent: title,
        });
        text.appendChild(titleEl);
    }

    const descEl = Object.assign(document.createElement('p'), {
        className: 'toast-desc mb-0 text-break',
        textContent: desc,
    });
    text.appendChild(descEl);

    const left = document.createElement('div');
    left.className = 'toast-left d-flex align-items-center';
    left.append(icon, text);

    // Right (time)
    const timeEl = document.createElement('span');
    timeEl.className = 'toast-time text-muted small ms-3';
    const createdAt = Date.now();
    timeEl.textContent = timeAgo(createdAt);

    // Auto-update "time ago"
    const interval = setInterval(() => {
        if (!document.body.contains(toastEl)) clearInterval(interval);
        else timeEl.textContent = timeAgo(createdAt);
    }, 10000);

    body.append(left, timeEl);
    toastEl.appendChild(body);

    render(toastEl, containerEl, position);
}

/**
 * Renders and animates the toast in the DOM.
 * @private
 */
function render(toastEl, containerEl, position) {
    if (!containerEl._queue) containerEl._queue = [];
    const queue = containerEl._queue;

    // Insert toast based on position (top vs bottom)
    const isBottom = position.startsWith('bottom');
    isBottom ? containerEl.append(toastEl) : containerEl.prepend(toastEl);

    // Animate and manage queue
    requestAnimationFrame(() => {
        const height = toastEl.offsetHeight;

        queue.forEach(t => {
            const offset = parseFloat(t.style.getPropertyValue('--offset') || '0');
            t.style.setProperty('--offset', `${offset + height + CONFIG.GAP}px`);
        });

        toastEl.style.setProperty('--offset', '0px');
        queue.unshift(toastEl);

        if (queue.length > CONFIG.MAX) hide(queue.pop(), queue);
    });

    // Lifetime & hover-pause
    let remaining = CONFIG.LIFETIME;
    let start = Date.now();
    let timeout = setTimeout(() => hide(toastEl, queue), remaining);

    toastEl.addEventListener('mouseenter', () => {
        clearTimeout(timeout);
        remaining -= Date.now() - start;
    });
    toastEl.addEventListener('mouseleave', () => {
        start = Date.now();
        timeout = setTimeout(() => hide(toastEl, queue), remaining);
    });
}

/**
 * Hides and removes a toast with transition.
 * @private
 */
function hide(toastEl, queue) {
    if (toastEl.classList.contains('toast-hide')) return;
    toastEl.classList.add('toast-hide');

    toastEl.addEventListener(
        'transitionend',
        () => {
            const i = queue.indexOf(toastEl);
            if (i !== -1) queue.splice(i, 1);
            toastEl.remove();
        },
        {once: true}
    );
}

/** Public Toast API */
export const Toast = {
    normal(title = 'Information', desc = '') {
        createToast('normal', title, desc);
    },
    success(desc = 'Success') {
        createToast('success', '', desc);
    },
    error(desc = 'Error') {
        createToast('error', '', desc);
    },
    warn(desc = 'Warning') {
        createToast('warning', '', desc, 'bottom-right');
    },
    info(desc = 'Information') {
        createToast('info', '', desc, 'bottom-right');
    },
};
