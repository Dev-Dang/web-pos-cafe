/**
 * ------------------------------------------------------------
 * Module: Category Slider
 * ------------------------------------------------------------
 * @description
 * Horizontal category track with prev/next scrolling and active
 * selection. Updates the page title and meta with the selected
 * category name.
 *
 * @example
 * import { initCategorySlider } from './swiper.js';
 * initCategorySlider();
 *
 * @version 1.0.1
 * @since 1.0.0
 * @lastModified 12/26/2025
 * @module swiper
 * @author Dang Van Trung
 */
export function initCategorySlider(root = document) {
    const scope = root && typeof root.querySelector === 'function' ? root : document;
    const track = scope.querySelector('[data-category-track]');
    const prevButton = scope.querySelector('[data-category-prev]');
    const nextButton = scope.querySelector('[data-category-next]');
    const items = track ? Array.from(track.querySelectorAll('[data-category-item]')) : [];
    const title = scope.querySelector('[data-category-title]');
    const meta = scope.querySelector('[data-category-meta]');

    if (!track || !prevButton || !nextButton) return;
    if (track.dataset.categorySliderInitialized === 'true') return;
    track.dataset.categorySliderInitialized = 'true';

    const getScrollAmount = () => {
        const firstItem = track.querySelector('[data-category-item], .category-item');
        if (!firstItem) return 160;
        return firstItem.getBoundingClientRect().width + 16;
    };

    const getItemSpacing = () => {
        const firstItem = track.querySelector('[data-category-item], .category-item');
        const secondItem = firstItem?.nextElementSibling;
        if (!firstItem || !secondItem) return 16;
        return Math.max(0, secondItem.offsetLeft - firstItem.offsetLeft - firstItem.offsetWidth);
    };

    const scrollToItemStart = (item, behavior = 'smooth') => {
        if (!item) return;
        const trackStyles = window.getComputedStyle(track);
        const paddingLeft = parseFloat(trackStyles.paddingLeft) || 0;
        const itemWidth = item.getBoundingClientRect().width;
        const spacing = getItemSpacing();
        const offset = paddingLeft + itemWidth + spacing;
        const targetLeft = Math.max(0, item.offsetLeft - offset);
        track.scrollTo({left: targetLeft, behavior});
    };

    prevButton.addEventListener('click', () => {
        track.scrollBy({left: -getScrollAmount(), behavior: 'smooth'});
    });

    nextButton.addEventListener('click', () => {
        track.scrollBy({left: getScrollAmount(), behavior: 'smooth'});
    });

    track.addEventListener('click', (event) => {
        const item = event.target.closest('.category-item');
        if (!item || !track.contains(item)) return;
        const current = track.querySelector('.category-item.is-active');
        if (current && current !== item) {
            current.classList.remove('is-active');
        }
        item.classList.add('is-active');
        scrollToItemStart(item);
    });

    const activeItem = track.querySelector('.category-item.is-active');
    scrollToItemStart(activeItem, 'auto');
}
