/**
 * Global delay for post-success actions on form submissions.
 */
const FORM_SUCCESS_DELAY_MS = 5000;

export function initFormSuccessDelay() {
    window.formSuccessDelayMs = FORM_SUCCESS_DELAY_MS;

    if (typeof up === 'undefined') {
        return;
    }

    up.on('up:request:loaded', function(event) {
        var request = event.request;
        var method = request && request.method ? request.method.toUpperCase() : '';
        if (method !== 'POST') {
            return;
        }

        var response = event.response;
        if (!response) {
            return;
        }

        var status = response.status;
        if (status < 200 || status >= 400) {
            return;
        }

        window.setTimeout(function() {
            var delayedEvent = new CustomEvent('form:success:delayed', {
                detail: {
                    request: request,
                    response: response
                }
            });
            document.dispatchEvent(delayedEvent);
        }, FORM_SUCCESS_DELAY_MS);
    });
}
