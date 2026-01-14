window.__APP_MODE__ = "development";

// Enable Unpoly logging
if (typeof up !== 'undefined') {
    up.log.enable();
}

import {initUnpoly} from "./modules/unpoly.js";
import {initPasswordToggle} from "./modules/form.js";

// ==== Global Unpoly workflow (includes form validation) ====
initUnpoly();

// ==== Form utilities ====
initPasswordToggle();