/**
 * ------------------------------------------------------------
 * Module: Modal Manager
 * ------------------------------------------------------------
 * @description
 * Simple dynamic modal management system built on top of
 * **Bootstrap Modal API**. It allows you to load, cache, and display modal
 * partials (JSP/HTML fragments) asynchronously from the server.
 *
 * @example
 * import { Modal } from './modules/modal.js';
 *
 * // Initialize the modal system
 * Modal.init({
 *   CONTAINER_SELECTOR: '#modal-container',
 *   TRIGGER_SELECTOR: '.btn-open-modal',
 *   preloadList: ['login', 'register'],
 * });
 *
 * // Open a modal programmatically
 * Modal.open('login');
 *
 * @version 1.0.2
 * @since 1.0.0
 * @lastModified 28/12/2025
 * @module modal
 * @author Dang Van Trung
 */

console.log('Modal.js module loading...');

import { getOrCreateContainer } from "./utils.js";

const CONFIG = {
  CONTAINER_SELECTOR: "#modal-container",
  TRIGGER_SELECTOR: ".btn-open-modal",
};

const MODAL_SELECTOR = ".modal";
const VISIBLE_MODAL_SELECTOR = ".modal.show";
const CACHE = {};
const CONTAINER_EL = getOrCreateContainer(CONFIG.CONTAINER_SELECTOR);

/**
 * Builds the fetch URL for a modal file based on its name.
 * @param {string} name - The short name of the modal (e.g., '_login').
 * @returns {string} The encoded URL to the modal resource.
 */
function modalUrl(name) {
  // Remove underscore prefix if present and map to auth endpoints
  const cleanName = name.startsWith('_') ? name.substring(1) : name;
  
  // Auth modals use servlet endpoints
  const authModals = ['login', 'register', 'forgot-password', 'reset-password'];
  if (authModals.includes(cleanName)) {
    return `./auth/${cleanName}`;
  }
  
  // Fallback to legacy modals endpoint
  return `./modals?name=${encodeURIComponent(name)}`;
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

  clone.querySelectorAll("input, textarea, select").forEach((el) => {
    el.classList.remove("is-invalid", "is-valid");
    el.value = "";
    el.removeAttribute("value");
    if (el.type === "checkbox" || el.type === "radio") {
      el.checked = false;
      el.removeAttribute("checked");
    }
  });

  clone
    .querySelectorAll(".invalid-feedback")
    .forEach((el) => (el.textContent = ""));

  return clone.outerHTML;
}

/**
 * Injects modal HTML into the container and displays it using Bootstrap.
 * @param {string} modalHTML - The HTML content of the modal.
 * @param {string} name - The name of the modal (e.g., 'login').
 * @returns {void}
 */
function injectAndShow(modalHTML, name) {
  console.log('injectAndShow called for:', name);
  CONTAINER_EL.innerHTML = modalHTML;
  const modalEl = CONTAINER_EL.querySelector(MODAL_SELECTOR);
  if (!modalEl)
    return console.error(`ModalManager - No ${MODAL_SELECTOR} found`);

  // Debug: Check if Unpoly exists
  console.log('Unpoly exists:', typeof up !== 'undefined');
  
  // Tell Unpoly to observe the new modal content for up-* attributes
  if (typeof up !== 'undefined' && up.hello) {
    console.log('Calling up.hello on modal element');
    up.hello(modalEl);
    
    // Debug: Check if form has up-submit
    const form = modalEl.querySelector('form[up-submit]');
    console.log('Form with up-submit found:', !!form);
    if (form) {
      console.log('Form action:', form.action);
      console.log('Form up-target:', form.getAttribute('up-target'));
    }
  }

  modalEl.addEventListener(
    "hidden.bs.modal",
    () => {
      const cleanHTML = sanitizeModal(modalEl);
      CACHE[name] = cleanHTML;
      modalEl.remove();
    },
    { once: true }
  );

  bootstrap.Modal.getOrCreateInstance(modalEl).show();
  console.log('Modal shown');
}

/**
 * Binds event listeners to reset form fields and validation states.
 * @param {HTMLElement} modalEl - The modal element to bind listeners to.
 * @param {string} modalName - The name of the modal (e.g., 'login').
 * @returns {void}
 */
function bindModalReset(modalEl, modalName) {
  modalEl.addEventListener("hidden.bs.modal", () => {
    const form = modalEl.querySelector("form");
    if (!form) return;
    form.reset();
    form
      .querySelectorAll(".is-invalid, .is-valid")
      .forEach((el) => el.classList.remove("is-invalid", "is-valid"));
    form
      .querySelectorAll(".invalid-feedback")
      .forEach((el) => (el.textContent = ""));
    CACHE[modalName] = modalEl;
  });
}

/**
 * Fetches a modal HTML file from the server and caches it.
 * @async
 * @param {string} name - The name of the modal to fetch.
 * @returns {Promise<string|null>} The modal HTML string, or `null` if fetch fails.
 */
async function fetchModal(name) {
  try {
    const res = await fetch(modalUrl(name));
    if (!res.ok) throw new Error(`Failed to fetch modal ${name}`);
    const modalHTML = await res.text();
    CACHE[name] = modalHTML;
    return modalHTML;
  } catch (error) {
    console.error(error);
    return null;
  }
}

/**
 * Preloads and caches multiple modals in parallel.
 * @async
 * @param {string[]} [names=[]] - List of modal names to preload.
 * @returns {Promise<void>}
 */
async function preload(names = []) {
  await Promise.all(names.map(async (item) => CACHE[item] || fetchModal(item)));
}

/**
 * Opens a modal by name. Hides the current visible modal if necessary.
 * @param {string} name - The modal identifier to open.
 * @returns {void}
 */
async function open(name) {
  console.log('Modal.open called with:', name);
  if (!name) return;
  const current = document.querySelector(VISIBLE_MODAL_SELECTOR);

  async function loadAndShow() {
    console.log('loadAndShow called for:', name);
    const modalHTML = CACHE[name] || (await fetchModal(name));
    console.log('modalHTML loaded:', !!modalHTML);
    if (modalHTML) injectAndShow(modalHTML, name);
  }

  if (!current) return loadAndShow();
  const instance = bootstrap.Modal.getInstance(current);
  current.addEventListener("hidden.bs.modal", loadAndShow, { once: true });

  document.activeElement?.blur();
  instance.hide();
}

/**
 * Opens a modal with custom fetch URL (for dynamic content).
 * Useful for modals that need server-side data (e.g., product details).
 * @param {string|Function} urlOrFunction - URL string or function that returns URL
 * @param {Function} [setupCallback] - Optional callback to setup handlers after modal loads
 * @returns {Promise<HTMLElement|null>} The modal element or null if failed
 */
async function openDynamic(urlOrFunction, setupCallback) {
  const url = typeof urlOrFunction === "function" ? urlOrFunction() : urlOrFunction;
  
  try {
    const response = await fetch(url, { headers: { "Accept": "text/html" } });
    if (!response.ok) throw new Error(`HTTP ${response.status}`);
    
    const modalHTML = await response.text();
    CONTAINER_EL.innerHTML = modalHTML;
    
    const modalEl = CONTAINER_EL.querySelector(MODAL_SELECTOR);
    if (!modalEl) return null;
    
    // Setup custom handlers if provided
    if (typeof setupCallback === "function") {
      setupCallback(modalEl);
    }
    
    // Show modal
    bootstrap.Modal.getOrCreateInstance(modalEl).show();
    
    // Cleanup on hide
    modalEl.addEventListener("hidden.bs.modal", () => {
      modalEl.remove();
    }, { once: true });
    
    return modalEl;
  } catch (error) {
    console.error("Failed to load dynamic modal:", error);
    return null;
  }
}

/**
 * Initializes the modal system.
 * Sets container, triggers selectors, and optionally preloads modals.
 *
 * @async
 * @param {Object} [options={}] - Configuration options.
 * @param {string} [options.CONTAINER_SELECTOR] - Custom selector for modal container.
 * @param {string} [options.TRIGGER_SELECTOR] - Selector for modal trigger elements.
 * @param {string[]} [options.preloadList=[]] - List of modal names to preload.
 * @param onReady - Callback function to execute when preloading is complete.
 * @returns {Promise<void>}
 */
async function init({
  CONTAINER_SELECTOR,
  TRIGGER_SELECTOR,
  preloadList = [],
  onReady,
} = {}) {
  console.log('Modal.init called');
  if (CONTAINER_SELECTOR) CONFIG.CONTAINER_SELECTOR = CONTAINER_SELECTOR;
  if (TRIGGER_SELECTOR) CONFIG.TRIGGER_SELECTOR = TRIGGER_SELECTOR;

  if (preloadList.length) await preload(preloadList);

  if (typeof onReady === "function") {
    onReady();
  }

  document.addEventListener("click", (e) => {
    const btn = e.target.closest(CONFIG.TRIGGER_SELECTOR);
    if (!btn) return;
    console.log('Modal trigger clicked:', btn.dataset.type);
    e.preventDefault();
    const name = btn.dataset.type;
    if (name) open(name);
  });
  
  console.log('Modal.init complete, listening for clicks on:', CONFIG.TRIGGER_SELECTOR);
}

export const Modal = {
  init,
  preload,
  open,
  openDynamic,
};
