package com.laptrinhweb.zerostarcafe.domain.order.model;

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
 * Domain model representing an order with full details including all items and options.
 * Maps to the {@code orders} table and includes related order items via join.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 09/01/2026
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Order {
    private long id;
    private long storeId;
    private Long tableId;
    private Long userId;
    private Long bookingId;
    private String status;     // 'open', 'served', 'partial_paid', 'paid', 'void'
    private LocalDateTime openedAt;
    private LocalDateTime closedAt;
    private String source;     // 'qr', 'staff_pos', 'kiosk', 'web'

    private List<OrderItem> items = new ArrayList<>();
}
