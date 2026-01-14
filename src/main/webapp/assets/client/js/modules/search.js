/**
 * ------------------------------------------------------------
 * Module: Product Search with Auto-submit & History
 * ------------------------------------------------------------
 * @description
 * Manages product search with auto-submit delay and search history.
 * Infinite scroll is handled by the separate infinite-scroll module.
 *
 * @example
 * import { initProductSearch } from './search.js';
 * initProductSearch();
 *
 * @version 4.0.0
 * @since 1.0.0
 * @lastModified 07/01/2026
 * @module search
 * @author Dang Van Trung
 */

const SEARCH_INPUT = '[data-search-input]';
const SEARCH_HISTORY = '[data-search-history]';
const HISTORY_LIST = '[data-history-list]';
const HISTORY_CLEAR = '[data-history-clear]';
const MAX_HISTORY_ITEMS = 100; // Store up to 100 items
const MAX_DISPLAY_HISTORY = 5; // Show max 5 items in dropdown
const STORAGE_KEY = 'product_search_history';
const SEARCH_DELAY = 500; // 500ms delay for auto-submit

let searchTimeout = null;

/**
 * Initializes product search with auto-submit and history.
 */
export function initProductSearch() {
    const searchInput = document.querySelector(SEARCH_INPUT);
    if (!searchInput) return;

    const historyDropdown = document.querySelector(SEARCH_HISTORY);
    const historyList = document.querySelector(HISTORY_LIST);
    const historyClear = document.querySelector(HISTORY_CLEAR);
    const searchForm = searchInput.closest('form');

    console.log('Product search initialized');

    // Prevent form submission for empty queries only
    if (searchForm) {
        searchForm.addEventListener('submit', (e) => {
            const query = searchInput.value.trim();
            if (query.length === 0) { // Only prevent empty submissions
                e.preventDefault();
                return false;
            }
        });
    }

    // Handle Enter key for immediate search submission
    searchInput.addEventListener('keydown', (e) => {
        if (e.key === 'Enter') {
            e.preventDefault();
            clearTimeout(searchTimeout);
            performSearch(searchInput, searchForm, historyDropdown);
        }
    });

    // Auto-submit search with delay after typing stops
    searchInput.addEventListener('input', (e) => {
        const searchTerm = e.target.value.trim();
        
        // Clear any existing timeout
        clearTimeout(searchTimeout);
        
        // Show filtered search history based on current input
        if (historyDropdown) {
            showSearchHistory(historyDropdown, historyList, searchInput, searchTerm);
        }

        // Set timeout for auto-submit (only if not empty)
        if (searchTerm.length > 0) {
            searchTimeout = setTimeout(() => {
                performSearch(searchInput, searchForm, historyDropdown);
            }, SEARCH_DELAY);
        }
    });

    // Show search history when input is focused
    searchInput.addEventListener('focus', () => {
        const currentValue = searchInput.value.trim();
        showSearchHistory(historyDropdown, historyList, searchInput, currentValue);
    });

    // Show search history when input is clicked
    searchInput.addEventListener('click', () => {
        const currentValue = searchInput.value.trim();
        showSearchHistory(historyDropdown, historyList, searchInput, currentValue);
    });

    // Hide history dropdown when input loses focus
    searchInput.addEventListener('blur', () => {
        // Delay to allow clicking on history items
        setTimeout(() => {
            if (historyDropdown) {
                historyDropdown.classList.remove('is-visible');
            }
        }, 200);
    });

    // Clear all history
    if (historyClear) {
        historyClear.addEventListener('mousedown', (e) => {
            e.preventDefault();
        });

        historyClear.addEventListener('click', (e) => {
            e.stopPropagation();
            clearSearchHistory();
            const currentValue = searchInput.value.trim();
            showSearchHistory(historyDropdown, historyList, searchInput, currentValue);
        });
    }

    // Hide history when clicking anywhere outside search field
    document.addEventListener('click', (e) => {
        const searchField = searchInput.closest('.search-field');
        if (searchField && !searchField.contains(e.target)) {
            if (historyDropdown) {
                historyDropdown.classList.remove('is-visible');
            }
        }
    });

    // Handle Unpoly events
    setupUnpolyEvents();
}

/**
 * Performs search submission
 */
function performSearch(searchInput, searchForm, historyDropdown) {
    const query = searchInput.value.trim();
    
    if (query.length === 0) { // Only prevent empty searches
        return;
    }
    
    // Save to history before search
    saveSearchHistory(query);
    
    // Submit the form with Unpoly (page loader will show automatically)
    if (searchForm) {
        up.submit(searchForm);
    }
    
    // Hide history dropdown
    if (historyDropdown) {
        historyDropdown.classList.remove('is-visible');
    }
}

/**
 * Setup Unpoly event handlers
 */
function setupUnpolyEvents() {
    // Handle search form submission
    up.on('up:form:submit', 'form', (event) => {
        const form = event.target;
        const searchInputInForm = form.querySelector(SEARCH_INPUT);
        if (searchInputInForm) {
            const searchTerm = searchInputInForm.value.trim();
            if (searchTerm.length > 0) { // Only save non-empty searches
                saveSearchHistory(searchTerm);
            }
        }
    });

    // Clear search input only on page reload
    window.addEventListener('beforeunload', () => {
        const searchInput = document.querySelector(SEARCH_INPUT);
        if (searchInput) {
            searchInput.value = '';
        }
    });
}

/**
 * Shows search history dropdown with filtering based on current input.
 * @param {HTMLElement} dropdown - History dropdown element
 * @param {HTMLElement} list - History list element
 * @param {HTMLElement} input - Search input element
 * @param {string} currentInput - Current input value to filter by
 */
function showSearchHistory(dropdown, list, input, currentInput = '') {
    const history = getSearchHistory();
    
    if (!dropdown || !list) return;

    // Filter history based on current input
    const filteredHistory = currentInput 
        ? history.filter(term => term.toLowerCase().includes(currentInput.toLowerCase()))
        : history;
    
    // Limit to max display items
    const displayHistory = filteredHistory.slice(0, MAX_DISPLAY_HISTORY);

    // Clear existing items
    list.innerHTML = '';

    if (displayHistory.length === 0) {
        list.innerHTML = `
            <li class="search-history__empty">
                <i class="fi fi-rr-time-past"></i>
                <span>${currentInput ? 'Không tìm thấy lịch sử phù hợp' : 'Không có lịch sử tìm kiếm'}</span>
            </li>
        `;
        dropdown.classList.add('is-visible');
        return;
    }

    // Render history items
    displayHistory.forEach(term => {
        const li = document.createElement('li');
        li.className = 'search-history__item';
        
        // Highlight matching text
        let displayText = term;
        if (currentInput && currentInput.length > 0) {
            const regex = new RegExp(`(${escapeRegex(currentInput)})`, 'gi');
            displayText = term.replace(regex, '<mark>$1</mark>');
        }
        
        li.innerHTML = `
            <i class="fi fi-rr-search"></i>
            <span class="search-history__text">${displayText}</span>
        `;
        
        li.addEventListener('mousedown', (e) => {
            e.preventDefault(); // Prevent blur
            input.value = term;
            dropdown.classList.remove('is-visible');
            
            // Trigger search immediately when clicking history item
            const searchForm = input.closest('form');
            if (searchForm) {
                performSearch(input, searchForm, dropdown);
            }
        });
        
        list.appendChild(li);
    });

    dropdown.classList.add('is-visible');
}

/**
 * Saves search term to localStorage history.
 * @param {string} term - Search term to save
 */
function saveSearchHistory(term) {
    if (!term || term.length === 0) return;

    let history = getSearchHistory();
    
    // Remove existing occurrence of the term
    history = history.filter(item => item !== term);
    // Add to the beginning
    history.unshift(term);
    // Limit to max items
    history = history.slice(0, MAX_HISTORY_ITEMS);
    
    localStorage.setItem(STORAGE_KEY, JSON.stringify(history));
}

/**
 * Gets search history from localStorage.
 * @returns {string[]} Array of search terms
 */
function getSearchHistory() {
    try {
        const history = localStorage.getItem(STORAGE_KEY);
        return history ? JSON.parse(history) : [];
    } catch (e) {
        console.error('Failed to load search history:', e);
        return [];
    }
}

/**
 * Clears all search history.
 */
function clearSearchHistory() {
    localStorage.removeItem(STORAGE_KEY);
}

/**
 * Escapes HTML to prevent XSS.
 * @param {string} str - String to escape
 * @returns {string} Escaped string
 */
function escapeHtml(str) {
    const div = document.createElement('div');
    div.textContent = str;
    return div.innerHTML;
}

/**
 * Escapes regex special characters.
 * @param {string} str - String to escape
 * @returns {string} Escaped string
 */
function escapeRegex(str) {
    return str.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
}