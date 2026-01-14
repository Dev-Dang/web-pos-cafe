const PRODUCT_GRID_SELECTOR = '.product-grid';
const PRODUCT_CARD_SELECTOR = '.product-card';
const LOAD_TRIGGER_SELECTOR = '[data-load-trigger]';

export function initInfiniteScroll(trigger) {
    if (!trigger) return;
    if (!window.up) {
        console.warn('[InfiniteScroll] up not found, abort');
        return;
    }

    // Tránh bind 2 lần trên cùng 1 trigger (Unpoly có thể compile lại)
    if (trigger._infiniteBound) {
        return;
    }
    trigger._infiniteBound = true;

    let url =
        trigger.dataset.paginationUrl ||
        trigger.getAttribute('data-pagination-url');

    if (!url) {
        console.warn('[InfiniteScroll] trigger missing data-pagination-url', trigger);
        return;
    }

    console.log('[InfiniteScroll] bind trigger for url:', url);

    const observer = new IntersectionObserver(async (entries) => {
        for (const entry of entries) {
            if (!entry.isIntersecting) continue;

            // Ngắt + xoá sentinel cũ để tránh loop
            observer.unobserve(trigger);
            trigger.remove();

            console.log('[InfiniteScroll] loading more:', url);

            try {
                // 1) Gọi server bằng Unpoly, KHÔNG swap DOM
                const response = await up.request(url, {target: ':none'});
                const html = response.text;

                const temp = document.createElement('div');
                temp.innerHTML = html;

                // 2) Lấy product card mới từ response (A)
                const newCards = Array.from(temp.querySelectorAll(PRODUCT_CARD_SELECTOR));
                console.log('[InfiniteScroll] new cards:', newCards.length);

                // 3) Lấy product card hiện tại trong grid (B)
                const grid = document.querySelector(PRODUCT_GRID_SELECTOR);
                if (!grid) {
                    console.warn('[InfiniteScroll] .product-grid not found');
                    return;
                }

                const existingCards = Array.from(
                    grid.querySelectorAll(PRODUCT_CARD_SELECTOR),
                );

                // 4) B + A -> HTML mới
                let newHtml = '';
                existingCards.forEach((card) => {
                    newHtml += card.outerHTML;
                });
                newCards.forEach((card) => {
                    newHtml += card.outerHTML;
                });

                // 5) Sentinel mới (nếu còn trang)
                const nextTrigger = temp.querySelector(LOAD_TRIGGER_SELECTOR);
                if (nextTrigger) {
                    let nextUrl =
                        nextTrigger.dataset.paginationUrl ||
                        nextTrigger.getAttribute('data-pagination-url') ||
                        nextTrigger.getAttribute('up-href') ||
                        nextTrigger.getAttribute('href');

                    if (nextUrl) {
                        nextTrigger.setAttribute('data-load-trigger', '');
                        nextTrigger.setAttribute('data-pagination-url', nextUrl);
                        newHtml += nextTrigger.outerHTML;
                        console.log('[InfiniteScroll] next page url:', nextUrl);
                    } else {
                        console.warn('[InfiniteScroll] new trigger has no url');
                    }
                } else {
                    console.log('[InfiniteScroll] no more trigger in response');
                }

                // 6) Swap lại product-grid
                grid.innerHTML = newHtml;

                // 7) Cho Unpoly “hello” lại fragment mới
                up.hello(grid);

                console.log('[InfiniteScroll] grid updated & re-hello');
            } catch (e) {
                console.error('[InfiniteScroll] request failed', e);
            }
        }
    }, {
        rootMargin: '0px 0px 150px 0px', // preload sớm
    });

    observer.observe(trigger);
}
