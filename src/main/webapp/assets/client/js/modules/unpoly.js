/**
 * Unpoly-specific functionality and event handling
 * Handles modal auto-opening and other Unpoly integrations
 * 
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 07/01/2026
 */

/**
 * Initialize Unpoly-specific functionality
 */
export function initUnpoly() {
    if (typeof up === 'undefined') {
        return; // Unpoly not loaded
    }

    // Auto-open product modal when triggered by response context
    initAutoModalOpening();
}

/**
 * Handle auto-opening of product modals for non-partial requests
 */
function initAutoModalOpening() {
    // Listen for DOM insertion events to detect response context changes
    up.on('up:fragment:inserted', function(event) {
        const responseContext = document.getElementById('response-context');
        
        if (responseContext) {
            const autoOpenProduct = responseContext.dataset.autoOpenProduct;
            
            if (autoOpenProduct) {
                // Auto-open product modal
                up.layer.open({
                    url: `/products/${autoOpenProduct}`,
                    layer: 'modal',
                    size: 'large',
                    animation: 'fade-in'
                }).then(() => {
                    // Clear the flag to prevent re-opening
                    delete responseContext.dataset.autoOpenProduct;
                }).catch((error) => {
                    console.warn('Failed to auto-open product modal:', error);
                });
            }
        }
    });
}