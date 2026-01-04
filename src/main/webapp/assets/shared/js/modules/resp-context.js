import {getFlashMessages} from "./utils.js";

/**
 * Process the response context to handle instructions like flash messages.
 * This function is designed to work with the #response-context wrapper.
 *
 * @param {Element} context - The #response-context element (can be from DOM or parsed HTML)
 */
export function handleRespContext(context) {
    if (!context) return;

    // Handle Flash Messages
    const flashes = context.querySelector('[up-flashes]');
    if (flashes) {
        console.log("Flash Messages Calling ...");
        getFlashMessages(flashes);
    }
}
