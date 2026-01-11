package com.laptrinhweb.zerostarcafe.domain.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <h2>Description:</h2>
 * <p>
 * Domain model representing a physical store location.
 * Maps directly to the {@code stores} table.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 02/12/2025
 * @since 1.0.0
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Store {
    private long id;
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private StoreStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}