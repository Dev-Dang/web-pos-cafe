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
export function initCategorySlider() {
  const track = document.querySelector('[data-category-track]');
  const prevButton = document.querySelector('[data-category-prev]');
  const nextButton = document.querySelector('[data-category-next]');
  const items = track ? Array.from(track.querySelectorAll('[data-category-item]')) : [];
  const title = document.querySelector('[data-category-title]');
  const meta = document.querySelector('[data-category-meta]');

  if (!track || !prevButton || !nextButton) return;

  const getScrollAmount = () => {
    const firstItem = track.querySelector('[data-category-item]');
    if (!firstItem) return 160;
    return firstItem.getBoundingClientRect().width + 16;
  };

  prevButton.addEventListener('click', () => {
    track.scrollBy({left: -getScrollAmount(), behavior: 'smooth'});
  });

  nextButton.addEventListener('click', () => {
    track.scrollBy({left: getScrollAmount(), behavior: 'smooth'});
  });

  items.forEach((item) => {
    item.addEventListener('click', () => {
      items.forEach((node) => node.classList.remove('is-active'));
      item.classList.add('is-active');
      const name = item.dataset.categoryName || item.textContent.trim();

      if (title) title.textContent = name;
      if (meta) meta.textContent = `Dang xem: ${name}`;

    });
  });
}
