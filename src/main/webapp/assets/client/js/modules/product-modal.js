const ProductModal = (() => {
    const SELECTORS = {
        modal: '[data-product-modal-form]',
        optionItem: '[data-option-item]',
        optionSingle: '[data-option-type="single"]',
        optionMulti: '[data-option-type="multi"]',
        qtyDisplay: '[data-modal-qty]',
        qtyInput: '[data-form-quantity]',
        noteTextarea: '[data-modal-note]',
        noteInput: '[data-form-note]',
        totalDisplay: '[data-modal-total]',
        minusBtn: '[data-modal-minus]',
        plusBtn: '[data-modal-plus]',
        submitBtn: '[data-modal-action]'
    };

    const CLASSES = {
        active: 'is-active',
        selected: 'is-selected',
        disabled: 'is-disabled'
    };

    class ModalState {
        constructor(modalElement) {
            this.modal = modalElement;
            this.form = modalElement.querySelector('form');
            this.data = up.data(modalElement);
            this.quantity = this.data.initialQty || 1;
            this.note = '';
            this.selectedOptions = new Map(); // groupId -> Set of valueIds
            this.optionPrices = new Map(); // valueId -> price delta
            this.optionGroups = new Map(); // groupId -> {required, maxSelect, selectedCount}

            this.initializeOptions();
            this.syncFormInputs();
        }

        initializeOptions() {
            const optionItems = this.modal.querySelectorAll(SELECTORS.optionItem);

            optionItems.forEach(item => {
                const groupId = item.dataset.optionGroup;
                const valueId = item.dataset.optionValueId;
                const price = parseFloat(item.dataset.optionPrice) || 0;

                // Store price delta
                this.optionPrices.set(valueId, price);

                // Initialize group tracking
                if (!this.selectedOptions.has(groupId)) {
                    this.selectedOptions.set(groupId, new Set());
                }

                // Get group metadata from section header
                const section = item.closest('.product-modal__section');
                if (section && !this.optionGroups.has(groupId)) {
                    const header = section.querySelector('.product-modal__section-header');
                    const isRequired = header?.textContent.includes('required') || false;
                    const maxSelectMatch = header?.textContent.match(/\(.*?(\d+).*?\)/);
                    const maxSelect = maxSelectMatch ? parseInt(maxSelectMatch[1]) : 1;

                    this.optionGroups.set(groupId, {
                        required: isRequired,
                        maxSelect: maxSelect,
                        selectedCount: 0
                    });
                }
            });
        }

        toggleOption(item) {
            const groupId = item.dataset.optionGroup;
            const valueId = item.dataset.optionValueId;
            const type = item.dataset.optionType;
            const group = this.optionGroups.get(groupId);

            if (type === 'single') {
                // Deselect all in group
                const groupItems = this.modal.querySelectorAll(`[data-option-group="${groupId}"]`);
                groupItems.forEach(i => {
                    i.classList.remove(CLASSES.active);
                    const vid = i.dataset.optionValueId;
                    this.selectedOptions.get(groupId).delete(vid);
                });

                // Select this one
                item.classList.add(CLASSES.active);
                this.selectedOptions.get(groupId).add(valueId);
                group.selectedCount = 1;
            } else {
                // Multi-select
                const isSelected = item.classList.contains(CLASSES.selected);
                const checkbox = item.querySelector('.option-row__check');

                if (isSelected) {
                    // Deselect
                    item.classList.remove(CLASSES.selected);
                    checkbox?.classList.remove(CLASSES.active);
                    this.selectedOptions.get(groupId).delete(valueId);
                    group.selectedCount--;
                } else {
                    // Check if max reached
                    if (group.selectedCount >= group.maxSelect) {
                        this.flashWarning(item.closest('.product-modal__section'));
                        return;
                    }

                    // Select
                    item.classList.add(CLASSES.selected);
                    checkbox?.classList.add(CLASSES.active);
                    this.selectedOptions.get(groupId).add(valueId);
                    group.selectedCount++;
                }

                // Update disabled state for other items
                this.updateMultiSelectState(groupId);
            }

            this.updateTotal();
            this.syncFormInputs();
        }

        updateMultiSelectState(groupId) {
            const group = this.optionGroups.get(groupId);
            const groupItems = this.modal.querySelectorAll(`[data-option-group="${groupId}"][data-option-type="multi"]`);

            groupItems.forEach(item => {
                const isSelected = item.classList.contains(CLASSES.selected);
                if (!isSelected && group.selectedCount >= group.maxSelect) {
                    item.classList.add(CLASSES.disabled);
                } else {
                    item.classList.remove(CLASSES.disabled);
                }
            });
        }

        flashWarning(section) {
            section?.classList.add('flash-highlight');
            setTimeout(() => section?.classList.remove('flash-highlight'), 1800);
        }

        incrementQty() {
            this.quantity++;
            this.updateQtyDisplay();
            this.updateTotal();
            this.syncFormInputs();
        }

        decrementQty() {
            if (this.quantity > 1) {
                this.quantity--;
                this.updateQtyDisplay();
                this.updateTotal();
                this.syncFormInputs();
            }
        }

        updateQtyDisplay() {
            const display = this.modal.querySelector(SELECTORS.qtyDisplay);
            if (display) display.textContent = this.quantity;
        }

        updateNote(value) {
            this.note = value;
            this.syncFormInputs();
        }

        calculateTotal() {
            let total = this.data.currentPrice;

            // Add option prices
            this.selectedOptions.forEach(valueSet => {
                valueSet.forEach(valueId => {
                    total += this.optionPrices.get(valueId) || 0;
                });
            });

            // Multiply by quantity
            return total * this.quantity;
        }

        updateTotal() {
            const total = this.calculateTotal();
            const display = this.modal.querySelector(SELECTORS.totalDisplay);
            if (display) {
                display.textContent = new Intl.NumberFormat('vi-VN').format(total) + 'đ';
            }
        }

        syncFormInputs() {
            // Update quantity
            const qtyInput = this.form.querySelector(SELECTORS.qtyInput);
            if (qtyInput) qtyInput.value = this.quantity;

            // Update note
            const noteInput = this.form.querySelector(SELECTORS.noteInput);
            if (noteInput) noteInput.value = this.note;

            // Clear existing option inputs
            this.form.querySelectorAll('input[name="optionValueIds"]').forEach(input => input.remove());

            // Add selected option inputs
            const allSelectedIds = [];
            this.selectedOptions.forEach(valueSet => {
                valueSet.forEach(valueId => allSelectedIds.push(valueId));
            });

            allSelectedIds.forEach(valueId => {
                const input = document.createElement('input');
                input.type = 'hidden';
                input.name = 'optionValueIds';
                input.value = valueId;
                this.form.appendChild(input);
            });
        }

        validateForm() {
            // Check required options
            for (const [groupId, group] of this.optionGroups) {
                if (group.required && group.selectedCount === 0) {
                    return false;
                }
            }
            return true;
        }
    }

    function bindEvents(modal, state) {
        // Option selection
        modal.querySelectorAll(SELECTORS.optionItem).forEach(item => {
            item.addEventListener('click', () => state.toggleOption(item));
        });

        // Quantity buttons
        const minusBtn = modal.querySelector(SELECTORS.minusBtn);
        const plusBtn = modal.querySelector(SELECTORS.plusBtn);

        minusBtn?.addEventListener('click', () => state.decrementQty());
        plusBtn?.addEventListener('click', () => state.incrementQty());

        // Note input
        const noteTextarea = modal.querySelector(SELECTORS.noteTextarea);
        noteTextarea?.addEventListener('input', (e) => state.updateNote(e.target.value));

        // Form submission validation
        const form = modal.querySelector('form');
        form?.addEventListener('submit', (e) => {
            if (!state.validateForm()) {
                e.preventDefault();
                alert('Vui lòng chọn đầy đủ các tùy chọn bắt buộc');
            }
        });
    }

    function init(modalElement) {
        if (!modalElement) return;

        const state = new ModalState(modalElement);
        bindEvents(modalElement, state);

        // Select first option for single-choice required groups
        state.optionGroups.forEach((group, groupId) => {
            if (group.required && group.maxSelect === 1 && group.selectedCount === 0) {
                const firstOption = modalElement.querySelector(`[data-option-group="${groupId}"][data-option-type="single"]`);
                if (firstOption) {
                    state.toggleOption(firstOption);
                }
            }
        });

        return state;
    }

    // Unpoly compiler - automatically runs when modal is loaded
    up.compiler('#product-detail-modal', (element) => {
        init(element);
    });

    return {init};
})();

// Export for external use if needed
window.ProductModal = ProductModal;
