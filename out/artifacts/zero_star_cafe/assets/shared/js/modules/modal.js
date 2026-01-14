/**
 * ------------------------------------------------------------
 * Module: Modal Manager
 * ------------------------------------------------------------
 * @description
 * Lightweight modal manager built on top of Bootstrap Modal API.
 * Designed for SSR setups where modal templates are preloaded
 * as <script type="text/template"> in the DOM.
 *
 * Supports:
 * - Opening modals from pre-rendered templates.
 * - Caching cleaned versions after close.
 * - Automatic transition handling when switching modals.
 *
 * @example
 * Modal.init({ TRIGGER_SELECTOR: '.btn-open-modal' });
 * Modal.open('register');
 *
 * @version 1.1.0
 * @since 1.0.0
 * @lastModified 10/11/2025
 * @module modal
 * @author Dang Van Trung
 */

import {getOrCreateContainer} from './utils.js'

const CONFIG = {
    CONTAINER_SELECTOR: '#modal-container',
    TEMPLATE_SELECTOR: '#modal-template',
    TRIGGER_SELECTOR: '.btn-open-modal',
};

const MODAL_SELECTOR = '.modal';
const VISIBLE_MODAL_SELECTOR = '.modal.show';
const CACHE = {};
const CONTAINER_EL = getOrCreateContainer(CONFIG.CONTAINER_SELECTOR);
const TEMPLATE_EL = document.querySelector(CONFIG.TEMPLATE_SELECTOR);

function getTemplate(name) {
    const tpl = TEMPLATE_EL.querySelector(`#tpl-modal-${name}`);
    return tpl ? tpl.innerHTML.trim() : null;
}

/**
 * Produce sanitized HTML from modal element
 * - remove validation classes
 * - clear invalid feedback
 * - clear input values
 * @param {HTMLElement} modalEl
 * @returns {string}
 */
function sanitizeModal(modalEl) {
    const clone = modalEl.cloneNode(true);

    clone.querySelectorAll('input, textarea, select')
        .forEach(el => {
            el.classList.remove('is-invalid', 'is-valid');
            el.value = '';
            el.removeAttribute('value');
            if (el.type === 'checkbox' || el.type === 'radio') {
                el.checked = false;
                el.removeAttribute('checked');
            }
        });

    clone.querySelectorAll('.invalid-feedback')
        .forEach(el => el.textContent = '');

    return clone.outerHTML;
}

/**
 * Injects modal HTML into the container and displays it using Bootstrap.
 * @param {string} modalHTML - The HTML content of the modal.
 * @param {string} name - The name of the modal (e.g., 'login').
 * @returns {void}
 */
function injectAndShow(modalHTML, name) {
    CONTAINER_EL.innerHTML = modalHTML;
    const modalEl = CONTAINER_EL.querySelector(MODAL_SELECTOR);
    if (!modalEl) return console.error(`ModalManager - No ${MODAL_SELECTOR} found`);

    modalEl.addEventListener('hidden.bs.modal', () => {
        const cleanHTML = sanitizeModal(modalEl);
        CACHE[name] = cleanHTML;
        modalEl.remove();
    }, {once: true});

    bootstrap.Modal.getOrCreateInstance(modalEl).show();
}

/**
 * Opens a modal by name. Hides the current visible modal if necessary.
 * @param {string} name - The modal identifier to open.
 * @returns {void}
 */
async function open(name) {
    if (!name) return;
    const current = document.querySelector(VISIBLE_MODAL_SELECTOR);

    async function loadAndShow() {
        const modalHTML = CACHE[name] || getTemplate(name);
        if (modalHTML) injectAndShow(modalHTML, name);
    }

    if (!current) return loadAndShow();
    const instance = bootstrap.Modal.getInstance(current);
    current.addEventListener('hidden.bs.modal', loadAndShow, {once: true});

    document.activeElement?.blur();
    instance.hide();
}

/**
 * Initializes the modal system.
 * Sets container, triggers selectors, and optionally preloads modals.
 *
 * @async
 * @param {Object} [options={}] - Configuration options.
 * @param {string} [options.CONTAINER_SELECTOR] - Custom selector for modal container.
 * @param {string} [options.TRIGGER_SELECTOR] - Selector for modal trigger elements.
 * @returns {Promise<void>}
 */
async function init({CONTAINER_SELECTOR, TRIGGER_SELECTOR} = {}) {
    if (CONTAINER_SELECTOR) CONFIG.CONTAINER_SELECTOR = CONTAINER_SELECTOR;
    if (TRIGGER_SELECTOR) CONFIG.TRIGGER_SELECTOR = TRIGGER_SELECTOR;

    document.addEventListener('click', e => {
        const btn = e.target.closest(CONFIG.TRIGGER_SELECTOR);
        if (!btn) return;
        e.preventDefault();
        const name = btn.dataset.type;
        if (name) open(name);
    });
}

export const Modal = {
    init, open
}