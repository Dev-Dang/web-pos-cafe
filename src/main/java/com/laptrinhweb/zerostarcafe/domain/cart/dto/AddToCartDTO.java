package com.laptrinhweb.zerostarcafe.domain.cart.dto;

import com.laptrinhweb.zerostarcafe.domain.cart.model.CartConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * DTO for adding a product to cart with selected options and note.
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
public class AddToCartDTO {
    private long menuItemId;
    private int qty;
    private String note;
    private List<Long> optionValueIds = new ArrayList<>();

    /**
     * Validate DTO data.
     *
     * @return true if valid
     */
    public boolean isValid() {
        if (menuItemId <= 0) {
            return false;
        }
        if (qty < CartConstants.Validation.MIN_QUANTITY) {
            return false;
        }
        if (qty > CartConstants.Validation.MAX_QUANTITY) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AddToCartDTO{" +
                "menuItemId=" + menuItemId +
                ", qty=" + qty +
                ", note='" + note + '\'' +
                ", optionValueIds=" + optionValueIds +
                '}';
    }
}