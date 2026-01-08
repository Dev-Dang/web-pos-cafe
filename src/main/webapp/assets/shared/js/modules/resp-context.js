import {getFlashMessages} from "./utils.js";

/**
 * Process the response context to handle instructions like flash messages.
 * This function is designed to work with the #response-context wrapper.
 *
 * @param {Element} context - The #response-context element (can be from DOM or parsed HTML)
 */
export function handleRespContext(context) {
    if (!context) return;

    // Handle reopen modal
    const modal = context.querySelector('[data-reopen-modal]')
    if (modal) {
        console.log("Reopen Modal Calling ...");
        reOpenModal(modal);
    }

    // Handle Flash Messages
    const flashes = context.querySelector('[up-flashes]');
    if (flashes) {
        console.log("Flash Messages Calling ...");
        getFlashMessages(flashes);
    }
}

function reOpenModal(modal) {
    const targetSelector = modal.getAttribute('up-target');
    const modalEl = modal.querySelector(targetSelector);
    if (!modalEl) return;

    // up-layer="new modal" -> layer="new", mode="modal"
    const layerAttr = (modal.getAttribute('up-layer') || 'new modal').trim().split(/\s+/);
    const layer = layerAttr[0] || 'new';
    const mode = layerAttr[1] || 'modal';

    const options = {
        fragment: modalEl,
        layer,
        mode,
        origin: modal,
    };

    const size = modal.getAttribute('up-size');
    if (size) options.size = size;

    const animation = modal.getAttribute('up-animation');
    if (animation) options.animation = animation;

    const history = modal.getAttribute('up-history');
    if (history === 'true') options.history = true;
    else if (history === 'false') options.history = false;

    up.layer.open(options);
}