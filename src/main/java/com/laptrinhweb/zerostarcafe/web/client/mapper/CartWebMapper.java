package com.laptrinhweb.zerostarcafe.web.client.mapper;

import com.laptrinhweb.zerostarcafe.domain.cart.dto.AddToCartDTO;
import com.laptrinhweb.zerostarcafe.web.common.WebConstants;
import com.laptrinhweb.zerostarcafe.web.common.utils.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * Mapper for converting HTTP form requests to Cart DTOs.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 08/01/2026
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CartWebMapper {

    /**
     * Parse form parameters to AddToCartDTO.
     *
     * @param req HTTP request
     * @return AddToCartDTO or null if parsing fails
     */
    public static AddToCartDTO toAddToCartDTO(HttpServletRequest req) {
        try {
            AddToCartDTO dto = new AddToCartDTO();

            // Get menuItemId
            Long menuItemId = RequestUtils.getLongParam(req, WebConstants.Cart.MENU_ITEM_ID);
            if (menuItemId == null) {
                return null;
            }
            dto.setMenuItemId(menuItemId);

            // Get qty
            Integer qty = RequestUtils.getIntParam(req, WebConstants.Cart.QUANTITY);
            dto.setQty(qty != null ? qty : 1);

            // Get note
            String note = RequestUtils.getStringParam(req, WebConstants.Cart.NOTE);
            dto.setNote(note);

            // Get option value IDs (array parameter)
            String[] optionIds = req.getParameterValues(WebConstants.Cart.OPTION_VALUE_IDS);
            if (optionIds != null && optionIds.length > 0) {
                List<Long> optionValueIds = new ArrayList<>();
                for (String id : optionIds) {
                    try {
                        optionValueIds.add(Long.parseLong(id));
                    } catch (NumberFormatException ignore) {
                        // Skip invalid IDs
                    }
                }
                dto.setOptionValueIds(optionValueIds);
            }

            return dto;

        } catch (Exception e) {
            return null;
        }
    }
}
