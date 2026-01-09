import {handleRespContext} from "./resp-context.js";
import {enableLiveValidation, validateFormBeforeSubmit} from "./form.js";

export function initUnpoly() {
    if (typeof up === 'undefined') return;

    // Configure CSRF protection
    up.protocol.config.csrfHeader = 'X-CSRF-Token';
    up.protocol.config.csrfToken = () => {
        return document.querySelector('meta[name="csrf-token"]')?.content;
    }

    // Page loader
    up.compiler('body', function () {
        const loader = document.querySelector('[data-page-loader]');

        up.on('up:network:late', function () {
            loader.classList.add('is-loading');
        });

        up.on('up:network:recover', function () {
            loader.classList.remove('is-loading');
        });
    });

    // Handle #response-context updates
    up.on('up:fragment:inserted', '#response-context', function (event, fragment) {
        console.info("Handling response context update ...");
        handleRespContext(fragment);
    })

    up.compiler('#response-context', function (fragment) {
        console.info("Handling response context ...");
        handleRespContext(fragment);
    })

    // Compile auth forms to add validation before Unpoly submission  
    up.compiler('form#loginForm, form#registerForm, form#forgotPasswordForm, form#resetPasswordForm', function (form) {
        enableLiveValidation(form);

        form.addEventListener('submit', function (event) {
            const isValid = validateFormBeforeSubmit(form);
            if (!isValid) {
                event.preventDefault();
                event.stopImmediatePropagation();
                return false;
            }
        }, true);
    });

    // Intercept requests to check responses for X-Up-Location header
    up.on('up:request:loaded', function (event) {
        const response = event.response;
        const upLocation = response?.xhr?.getResponseHeader('X-Up-Location');

        // Handle 303 redirects with X-Up-Location header
        if (response.status === 303 && upLocation) {

            // Handle [response-context] elements inside the response body
            const html = response.text || "";
            if (html) {
                up.render({
                        target: '#response-context',
                        document: html,
                        fail: false,
                        revalidate: false,
                        layer: 'any'
                    }
                );
            }

            // Close all layers/modals
            if (up.layer) {
                up.layer.dismissOverlays(':all', {animation: false});
            }

            // Navigate to the target page
            setTimeout(() => {
                window.location.href = upLocation;
            }, 500);
        }

        // Csrf protection logging
        if (event.request.method !== 'GET') {
            const hasToken = event.request.headers['X-CSRF-Token'] ? '✓' : '✗';
            console.log(`[CSRF] ${event.request.method} ${event.request.url} - Token: ${hasToken}`);
        }
    });
}