/**
 * Order history modal behaviors.
 */
export function initOrderHistory() {
    initOrderHistoryPrint();
}

function initOrderHistoryPrint() {
    if (typeof up === 'undefined') {
        return;
    }

    up.compiler('[data-order-history-print]', function(button) {
        button.addEventListener('click', function(event) {
            event.preventDefault();
            document.body.classList.add('is-printing-order-history');
            window.print();
            window.setTimeout(function() {
                document.body.classList.remove('is-printing-order-history');
            }, 1000);
        });
    });

    window.addEventListener('afterprint', function() {
        document.body.classList.remove('is-printing-order-history');
    });
}
