package com.laptrinhweb.zerostarcafe.domain.invoice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * DTO representing a single line item in an invoice.
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
public class InvoiceItemDTO {
    private String productName;
    private int quantity;
    private int unitPrice;
    private int totalPrice;
    private String options;
    private List<String> optionLines = new ArrayList<>();
    private String note;
}
