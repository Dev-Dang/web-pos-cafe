package com.laptrinhweb.zerostarcafe.domain.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * <h2>Description:</h2>
 * <p>
 * Simple request DTO carrying information extracted from a QR code
 * that is needed to resolve the current store context.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 02/12/2025
 * @since 1.0.0
 */

@AllArgsConstructor
@Getter
@Setter
public final class StoreContext {
    private Long storeId;
    private Long tableId;
}