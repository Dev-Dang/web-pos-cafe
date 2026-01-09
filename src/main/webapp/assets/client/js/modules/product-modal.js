const parsePrice = (value) => {
    const numeric = (value || "").toString().replace(/[^\d]/g, "");
    return parseInt(numeric, 10) || 0;
};

const formatPrice = (value) => {
    return new Intl.NumberFormat('vi-VN').format(value) + 'đ';
};

/**
 * Handles option selection (single/multi)
 */
function initOptionHandlers(modalEl) {
    const items = modalEl.querySelectorAll("[data-option-item]");

    items.forEach((item) => {
        item.addEventListener("click", () => {
            const group = item.dataset.optionGroup;
            const type = item.dataset.optionType;
            const section = item.closest(".product-modal__section");

            if (type === "single") {
                // Deselect all in group
                modalEl.querySelectorAll(`[data-option-group="${group}"]`)
                    .forEach(el => {
                        el.classList.remove("is-active");
                        el.querySelector(".option-row__check")?.classList.remove("is-active");
                    });
                // Select clicked
                item.classList.add("is-active");
                item.querySelector(".option-row__check")?.classList.add("is-active");
            } else if (type === "multi") {
                // Toggle multi-select with max check
                // Try to get max from data attribute first, then fallback to text parsing
                const maxSelect = parseInt(section.dataset.maxSelect) ||
                    parseInt(section.querySelector(".product-modal__section-sub")
                        ?.textContent.match(/\d+/)?.[0]) || 999;
                const groupItems = modalEl.querySelectorAll(`[data-option-group="${group}"]`);
                const currentActive = Array.from(groupItems).filter(el => el.classList.contains("is-active")).length;

                // Clicking on already selected item (deselect)
                if (item.classList.contains("is-active")) {
                    item.classList.remove("is-active");
                    item.classList.remove("is-disabled");
                    item.querySelector(".option-row__check")?.classList.remove("is-active");

                    // Re-enable other disabled items
                    groupItems.forEach(el => {
                        if (!el.classList.contains("is-active")) {
                            el.classList.remove("is-disabled");
                        }
                    });
                }
                // Clicking on unselected item
                else {
                    if (currentActive >= maxSelect) {
                        flashHighlight(section);
                        return; // Max reached, do nothing
                    }

                    item.classList.add("is-active");
                    item.querySelector(".option-row__check")?.classList.add("is-active");

                    // Check if max reached after this selection
                    const newActive = currentActive + 1;
                    if (newActive >= maxSelect) {
                        // Disable unselected items
                        groupItems.forEach(el => {
                            if (!el.classList.contains("is-active")) {
                                el.classList.add("is-disabled");
                            }
                        });
                    }
                }
            }

            updateTotals(modalEl);
            syncFormInputs(modalEl);
        });
    });
}

/**
 * Handles quantity +/- buttons
 */
function initQuantityStepper(modalEl) {
    const minus = modalEl.querySelector("[data-modal-minus]");
    const plus = modalEl.querySelector("[data-modal-plus]");
    const qtyEl = modalEl.querySelector("[data-modal-qty]");

    if (!qtyEl) return;

    const updateQty = (delta) => {
        const current = parseInt(qtyEl.textContent) || 1;
        const newQty = Math.max(1, Math.min(99, current + delta));
        qtyEl.textContent = newQty;
        updateTotals(modalEl);
        syncFormInputs(modalEl);
    };

    minus?.addEventListener("click", () => updateQty(-1));
    plus?.addEventListener("click", () => updateQty(1));
}

/**
 * Handles note input synchronization
 */
function initNoteHandler(modalEl) {
    const noteTextarea = modalEl.querySelector("[data-modal-note]");
    noteTextarea?.addEventListener("input", () => syncFormInputs(modalEl));
}

/**
 * Validates required option sections
 */
function validateRequired(modalEl) {
    const sections = modalEl.querySelectorAll(".product-modal__section");

    for (const section of sections) {
        // Check data attribute first, then fallback to text
        const isRequired = section.dataset.required === "true" ||
            section.querySelector(".product-modal__section-sub")?.textContent.toLowerCase().includes("bắt buộc") ||
            section.querySelector(".product-modal__section-sub")?.textContent.toLowerCase().includes("required");

        if (!isRequired) continue;

        const firstItem = section.querySelector("[data-option-item]");
        const group = firstItem?.dataset.optionGroup;

        // Both single and multi use "is-active" class
        const selected = section.querySelectorAll(`[data-option-group="${group}"].is-active`).length;

        if (selected === 0) {
            const headerText = section.querySelector(".product-modal__section-header span")?.textContent;
            return {valid: false, missing: headerText || "tùy chọn", section};
        }
    }

    return {valid: true};
}

/**
 * Gets base price from modal
 */
function getBasePrice(modalEl) {
    const priceData = modalEl.querySelector("[data-modal-price]");
    return parsePrice(priceData?.textContent || "0");
}

/**
 * Gets selected options and calculates total option price
 */
function getSelectedOptionsData(modalEl) {
    const items = modalEl.querySelectorAll("[data-option-item].is-active");
    const optionValueIds = [];
    let optionsPrice = 0;

    items.forEach((item) => {
        const valueId = item.dataset.optionValueId;
        const price = parsePrice(item.dataset.optionPrice);

        if (valueId) {
            optionValueIds.push(valueId);
        }
        optionsPrice += price;
    });

    return {optionValueIds, optionsPrice};
}

/**
 * Updates total price display
 */
function updateTotals(modalEl) {
    const qtyEl = modalEl.querySelector("[data-modal-qty]");
    const totalEl = modalEl.querySelector("[data-modal-total]");

    const qty = parseInt(qtyEl?.textContent || "1");
    const basePrice = getBasePrice(modalEl);
    const {optionsPrice} = getSelectedOptionsData(modalEl);
    const total = (basePrice + optionsPrice) * qty;

    if (totalEl) {
        totalEl.textContent = formatPrice(total);
    }
}

/**
 * Syncs visible UI state to hidden form inputs
 */
function syncFormInputs(modalEl) {
    const form = modalEl.querySelector("form");
    if (!form) return;

    // Update quantity
    const qtyInput = form.querySelector("[data-form-quantity]");
    const qtyDisplay = modalEl.querySelector("[data-modal-qty]");
    if (qtyInput && qtyDisplay) {
        qtyInput.value = qtyDisplay.textContent;
    }

    // Update note
    const noteInput = form.querySelector("[data-form-note]");
    const noteTextarea = modalEl.querySelector("[data-modal-note]");
    if (noteInput && noteTextarea) {
        noteInput.value = noteTextarea.value;
    }

    // Clear existing option inputs
    form.querySelectorAll('input[name="optionValueIds"]').forEach(input => input.remove());

    // Add selected option value IDs
    const {optionValueIds} = getSelectedOptionsData(modalEl);
    optionValueIds.forEach(valueId => {
        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'optionValueIds';
        input.value = valueId;
        form.appendChild(input);
    });
}

/**
 * Flashes highlight animation on section
 */
function flashHighlight(section) {
    if (!section) return;
    section.classList.add('flash-highlight');
    setTimeout(() => section.classList.remove('flash-highlight'), 1800);
}

/**
 * Highlights and scrolls to missing required section
 */
function highlightMissingSection(section) {
    if (!section) return;
    section.scrollIntoView({behavior: "smooth", block: "center"});
    flashHighlight(section);
}

/**
 * Form submission validation handler
 */
function initFormValidation(modalEl) {
    const form = modalEl.querySelector("form");
    if (!form) return;

    form.addEventListener("submit", (e) => {
        const validation = validateRequired(modalEl);
        if (!validation.valid) {
            e.preventDefault();
            highlightMissingSection(validation.section);

            // Show error message
            showError(`Vui lòng chọn ${validation.missing}`);
        }
    });
}

/**
 * Shows error message (using toast if available, otherwise alert)
 */
async function showError(message) {
    try {
        const {Toast} = await import("../../../shared/js/modules/toast.js");
        Toast.error(message);
    } catch (e) {
        alert(message);
    }
}

/**
 * Auto-selects first option for required single-choice groups
 */
function autoSelectRequiredSingleOptions(modalEl) {
    const sections = modalEl.querySelectorAll(".product-modal__section");

    sections.forEach(section => {
        // Check data attribute first, then fallback to text
        const isRequired = section.dataset.required === "true" ||
            section.querySelector(".product-modal__section-sub")?.textContent.toLowerCase().includes("bắt buộc") ||
            section.querySelector(".product-modal__section-sub")?.textContent.toLowerCase().includes("required");

        if (!isRequired) return;

        const firstItem = section.querySelector("[data-option-item]");
        const type = firstItem?.dataset.optionType;

        // Only auto-select for single-choice
        if (type === "single") {
            const group = firstItem?.dataset.optionGroup;
            const alreadySelected = section.querySelector(`[data-option-group="${group}"].is-active`);

            if (!alreadySelected && firstItem) {
                firstItem.classList.add("is-active");
                firstItem.querySelector(".option-row__check")?.classList.add("is-active");
            }
        }
    });

    // Update totals and sync after auto-selection
    updateTotals(modalEl);
    syncFormInputs(modalEl);
}

/**
 * Main initialization function
 */
function initProductModal(modalEl) {
    if (!modalEl) return;

    // Initialize all handlers
    initOptionHandlers(modalEl);
    initQuantityStepper(modalEl);
    initNoteHandler(modalEl);
    initFormValidation(modalEl);

    // Auto-select required single options
    autoSelectRequiredSingleOptions(modalEl);

    // Initial sync
    syncFormInputs(modalEl);
}

/**
 * Unpoly compiler - automatically runs when modal is loaded
 */
up.compiler('#product-detail-modal', (element) => {
    initProductModal(element);
});

// Export for external use if needed
export {initProductModal};
