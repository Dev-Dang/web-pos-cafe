package com.laptrinhweb.zerostarcafe.domain.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * DTO for cart view with localized fields and calculated totals.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 08/01/2026
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartDTO {
    private long id;
    private long userId;
    private long storeId;
    private int totalQty;
    private int subtotal;
    private int total;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CartItemDTO> items = new ArrayList<>();
}