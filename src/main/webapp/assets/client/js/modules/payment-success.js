/**
 * Payment success modal auto-close handler.
 */
var paymentCountdownInterval = null;
var paymentCloseTimer = null;
var isClosing = false;

export function initPaymentSuccessAutoClose() {
    if (typeof up !== 'undefined') {
        up.compiler('[data-payment-success]', function(modal) {
            startCountdown(modal);
            bindManualClose(modal);
        });
    }

    document.addEventListener('form:success:delayed', function() {
        var successModal = document.querySelector('[data-payment-success]');
        if (!successModal) {
            return;
        }
        closeAndReload();
    });
}

function bindManualClose(modal) {
    var closeButton = modal.querySelector('[data-payment-success-close]');
    if (!closeButton) {
        return;
    }

    closeButton.addEventListener('click', function(event) {
        event.preventDefault();
        closeAndReload();
    });
}

function startCountdown(modal) {
    clearTimers();

    var delayMs = window.formSuccessDelayMs || 5000;
    var remainingSeconds = Math.ceil(delayMs / 1000);
    var countdownElement = modal.querySelector('.payment-success__countdown');

    if (countdownElement) {
        countdownElement.textContent = remainingSeconds;
    }

    paymentCountdownInterval = setInterval(function() {
        remainingSeconds -= 1;
        if (countdownElement) {
            countdownElement.textContent = remainingSeconds;
        }

        if (remainingSeconds <= 0) {
            clearInterval(paymentCountdownInterval);
            paymentCountdownInterval = null;
        }
    }, 1000);

    paymentCloseTimer = setTimeout(function() {
        closeAndReload();
    }, delayMs);
}

function closeAndReload() {
    if (isClosing) {
        return;
    }
    isClosing = true;
    clearTimers();

    var reload = function() {
        window.location.href = '/zero_star_cafe/home';
    };

    if (typeof up !== 'undefined' && up.layer && up.layer.dismiss) {
        try {
            var result = up.layer.dismiss();
            if (result && typeof result.then === 'function') {
                result.then(reload);
            } else {
                window.setTimeout(reload, 50);
            }
            return;
        } catch (error) {
            reload();
            return;
        }
    }

    reload();
}

function clearTimers() {
    if (paymentCountdownInterval) {
        clearInterval(paymentCountdownInterval);
        paymentCountdownInterval = null;
    }

    if (paymentCloseTimer) {
        clearTimeout(paymentCloseTimer);
        paymentCloseTimer = null;
    }
}
