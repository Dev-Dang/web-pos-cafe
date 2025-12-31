/**
 * ------------------------------------------------------------
 * Module: Utility Functions
 * ------------------------------------------------------------
 * @description
 * Provides helper utilities for DOM manipulation and logging.
 * Includes:
 * - `getOrCreateContainer()` — find or create a DOM container.
 * - `setupLogging()` — enhanced console logger with dev toasts.
 * - `getFlashMessages()` — reads SSR-rendered flash messages and shows them as toasts.
 * - `getReopenModal()` — reads SSR marker for modal reopening.
 *
 * @example
 * import {getFlashMessages, getReopenModal} from './utils.js';
 * getFlashMessages();
 * const modalName = getReopenModal();
 *
 * @version 1.1.0
 * @since 1.0.0
 * @lastModified 10/11/2025
 * @module utils
 * @author Dang Van Trung
 */

import {Toast} from './toast.js';

/**
 * Finds an existing DOM element by selector or creates it if not found.
 * Automatically sets the element’s ID and class names based on the selector.
 *
 * @param {string} selector - CSS selector (e.g., '#container', '.toast-container').
 * @returns {HTMLElement} The found or newly created DOM element.
 */
export function getOrCreateContainer(selector) {
    let el = document.querySelector(selector);
    if (el) return el;

    el = document.createElement('div');
    const idMatch = selector.match(/#([\w-]+)/);
    if (idMatch && idMatch[1]) {
        el.id = idMatch[1];
    }
    const classMatches = selector.match(/\.([\w-]+)/g);
    if (classMatches) {
        const classes = classMatches.map(c => c.substring(1));
        el.classList.add(...classes);
    }
    document.body.appendChild(el);
    return el;
}

/**
 * Sets up enhanced console logging based on the environment mode.
 * In development mode, logs are colorized and displayed as toast messages.
 * In production mode, all console outputs are disabled for cleaner UX.
 */
export function setupLogging() {
    const MODE = window.__APP_MODE__ || 'production';
    const isDev = MODE === 'development';

    // Copy original console methods to prevent recursion.
    const original = Object.assign({}, console);

    if (!isDev) {
        // Production mode → disable all console outputs.
        console.log = console.info = console.warn = console.debug = () => {
        };
        console.error = () => {
        };
        return;
    }

    /** Prints messages with colored prefix in the browser console. */
    function printWithStyle(type, color, ...args) {
        const prefix = `[${type.toUpperCase()}]`;
        const style = `color:${color};font-weight:bold`;
        original[type](`%c${prefix}`, style, ...args);
    }

    /** Converts any log arguments into a readable multiline string. */
    function buildMessage(args) {
        return args
            .map(a => (a instanceof Error ? a.message : String(a)))
            .join('\n');
    }

    // Override console methods in development mode.
    console.log = (...args) => {
        printWithStyle('log', '#00bcd4', ...args);
        Toast.normal('', buildMessage(args));
    };

    console.info = (...args) => {
        printWithStyle('info', '#4caf50', ...args);
        Toast.info(buildMessage(args));
    };

    console.warn = (...args) => {
        printWithStyle('warn', '#ff9800', ...args);
        Toast.warn(buildMessage(args));
    };

    console.error = (...args) => {
        const error = args.find(a => a instanceof Error) || args[0];
        printWithStyle('error', '#f44336', error);
        Toast.error(buildMessage(args));
    };
}

/**
 * Reads SSR-rendered flash messages from the DOM and displays them as toasts.
 * - If the message type is supported by the Toast system, it is displayed.
 * - Otherwise, a generic "normal" toast is shown.
 *
 * @returns {void}
 */
export function getFlashMessages() {
    const flashData = document.querySelector('#flash-data');
    if (!flashData) return;

    flashData.querySelectorAll('p[data-type][data-message]').forEach(p => {
        const type = p.dataset.type;
        const msg = p.dataset.message;
        if (Toast[type]) Toast[type](msg);
        else Toast.normal('', msg);
        p.remove();
    });
}

/**
 * Stores modal state globally for reopening after server responses.
 * This allows server-side form responses to trigger modal reopening.
 *
 * @type {string|null}
 * @private
 */
let pendingModalToOpen = null;

/**
 * Sets the modal that should be reopened from server response.
 * Called by response handlers when .set("openModal", "modalName") is used.
 *
 * @param {string|null} modalName - The modal name to open, or null to clear.
 * @returns {void}
 */
export function setPendingModal(modalName) {
    pendingModalToOpen = modalName;
}

/**
 * Gets the pending modal name and clears the stored value.
 * Returns which modal (if any) should be reopened automatically.
 * Checks two sources:
 * 1. Runtime variable (from form response handler)
 * 2. DOM data attribute (from server-side flash on page load)
 *
 * @returns {string|null} The modal name to reopen, or null if none pending.
 */
export function getReopenModal() {
    // First priority: runtime pending modal (from form response)
    if (pendingModalToOpen) {
        const modalToOpen = pendingModalToOpen;
        pendingModalToOpen = null;
        return modalToOpen;
    }

    // Second priority: DOM flash data (from server-side SSR on page load)
    const flashData = document.querySelector('#flash-data');
    if (flashData && flashData.dataset.openModal) {
        return flashData.dataset.openModal;
    }

    return null;
}

/**
 * Refills form fields with values from server flash data.
 * Used to restore form state when modal reopens after failed submission.
 * Should be called AFTER modal HTML is rendered in the DOM.
 *
 * @returns {void}
 */
export function refillFormData() {
    const flashData = document.querySelector('#flash-data');
    if (!flashData) {
        console.debug('No #flash-data found');
        return;
    }

    // Get all hidden inputs with form data
    const inputs = flashData.querySelectorAll('input[type="hidden"][name]');
    console.debug(`Found ${inputs.length} form data inputs to refill`);

    inputs.forEach(input => {
        const fieldName = input.name;
        const fieldValue = input.value;

        console.debug(`Refilling field: ${fieldName} = ${fieldValue}`);

        // Find form field in all forms and set value
        // Try by: name attribute, id, or constructed id pattern
        const selectors = [
            `input[name="${fieldName}"]`,
            `textarea[name="${fieldName}"]`,
            `select[name="${fieldName}"]`,
            `#${fieldName}`,
        ];

        let found = false;
        selectors.forEach(selector => {
            const field = document.querySelector(selector);
            if (field && !field.closest('#flash-data')) {
                field.value = fieldValue;
                found = true;
                console.debug(`  ✓ Set via selector: ${selector}`);
            }
        });

        if (!found) {
            console.debug(`  ✗ Field "${fieldName}" not found in DOM`);
        }
    });
}

/**
 * Registers the global response handler.
 * Processes server response data including modal reopening.
 * Call this once during app initialization.
 *
 * @returns {void}
 */
export function initResponseHandler() {
    window.__handleFormResponse__ = function (responseData) {
        if (!responseData || typeof responseData !== 'object') return;
        if (responseData.openModal) {
            setPendingModal(responseData.openModal);
        }
    };
}