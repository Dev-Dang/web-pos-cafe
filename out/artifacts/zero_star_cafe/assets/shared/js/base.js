import {Modal} from './modules/modal.js';
import {getFlashMessages, getReopenModal, setupLogging} from './modules/utils.js';

// ==== Config logging system for client side ====
setupLogging();

// ==== Flash from SSR ====
getFlashMessages();

// ==== Modal System ====
await Modal.init();

// ==== Reopen modal if SSR required ====
const reopen = getReopenModal();
if (reopen) Modal.open(reopen);