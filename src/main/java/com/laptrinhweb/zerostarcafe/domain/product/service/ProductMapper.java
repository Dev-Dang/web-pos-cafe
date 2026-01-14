package com.laptrinhweb.zerostarcafe.domain.product.service;

import com.laptrinhweb.zerostarcafe.core.i18n.I18nUtils;
import com.laptrinhweb.zerostarcafe.domain.product.dto.ProductCardDTO;
import com.laptrinhweb.zerostarcafe.domain.product.dto.ProductDetailDTO;
import com.laptrinhweb.zerostarcafe.domain.product.dto.ProductOptionDTO;
import com.laptrinhweb.zerostarcafe.domain.product.dto.ProductOptionValueDTO;
import com.laptrinhweb.zerostarcafe.domain.product.model.Product;
import com.laptrinhweb.zerostarcafe.domain.product.model.ProductConstants;
import com.laptrinhweb.zerostarcafe.domain.product.model.ProductOption;
import com.laptrinhweb.zerostarcafe.domain.product.model.ProductOptionValue;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <h2>Description:</h2>
 * <p>
 * Utility class for mapping Product entities to DTOs with localization.
 * Handles extraction of multilingual content and proper DTO construction.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 06/01/2026
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProductMapper {

    /**
     * Converts a Product entity to a ProductCardDTO for UI cards.
     *
     * @param product the product entity
     * @return ProductCardDTO with localized content
     */
    public static ProductCardDTO toProductCard(Product product) {
        if (product == null) {
            return null;
        }

        ProductCardDTO card = new ProductCardDTO();
        card.setId(product.getId());
        card.setName(I18nUtils.extract(product.getNameJson()));
        card.setSlug(product.getSlug());
        card.setImageUrl(product.getImageUrl() != null ? 
            product.getImageUrl() : ProductConstants.Defaults.DEFAULT_IMAGE_URL);
        int currentPrice = product.getCurrentPrice() != null ? 
            product.getCurrentPrice() : product.getBasePrice();
        card.setBasePrice(product.getBasePrice());
        card.setCurrentPrice(currentPrice);
        card.setDisplayPrice(currentPrice);
        card.setUnit(product.getUnit() != null ? 
            product.getUnit() : ProductConstants.Defaults.DEFAULT_UNIT);
        card.setHasPromotion(product.isHasPromotion());
        
        // Determine availability
        boolean isAvailable = product.isActive() && 
            (product.getAvailabilityStatus() == null || 
             product.getAvailabilityStatus().isAvailable());
        card.setAvailable(isAvailable);
        card.setAvailabilityStatus(product.getAvailabilityStatus() != null ? 
            product.getAvailabilityStatus().name().toLowerCase() : null);

        return card;
    }

    /**
     * Converts a Product entity to a ProductDetailDTO for detail pages.
     *
     * @param product the product entity with options
     * @return ProductDetailDTO with localized content and options
     */
    public static ProductDetailDTO toProductDetail(Product product) {
        if (product == null) {
            return null;
        }

        ProductDetailDTO detail = new ProductDetailDTO();
        detail.setId(product.getId());
        detail.setCategoryId(product.getCategoryId());
        detail.setName(I18nUtils.extract(product.getNameJson()));
        detail.setSlug(product.getSlug());
        detail.setImageUrl(product.getImageUrl() != null ? 
            product.getImageUrl() : ProductConstants.Defaults.DEFAULT_IMAGE_URL);
        detail.setDescription(I18nUtils.extract(product.getDescriptionJson()));
        detail.setBasePrice(product.getBasePrice());
        detail.setCurrentPrice(product.getCurrentPrice() != null ? 
            product.getCurrentPrice() : product.getBasePrice());
        detail.setUnit(product.getUnit() != null ? 
            product.getUnit() : ProductConstants.Defaults.DEFAULT_UNIT);
        detail.setHasPromotion(product.isHasPromotion());
        detail.setPromotionValidFrom(product.getPromotionValidFrom());
        detail.setPromotionValidTo(product.getPromotionValidTo());
        
        // Availability
        boolean isAvailable = product.isActive() && 
            (product.getAvailabilityStatus() == null || 
             product.getAvailabilityStatus().isAvailable());
        detail.setAvailable(isAvailable);
        detail.setAvailabilityStatus(product.getAvailabilityStatus() != null ? 
            product.getAvailabilityStatus().name().toLowerCase() : null);
        detail.setSoldOutUntil(product.getSoldOutUntil());
        detail.setSoldOutNote(product.getSoldOutNote());

        // Options
        if (product.getOptions() != null) {
            detail.setOptions(product.getOptions().stream()
                .map(ProductMapper::toProductOptionDTO)
                .collect(Collectors.toList()));
        }

        return detail;
    }

    /**
     * Converts a list of Products to ProductCardDTOs.
     */
    public static List<ProductCardDTO> toProductCards(List<Product> products) {
        if (products == null) {
            return List.of();
        }

        return products.stream()
            .map(ProductMapper::toProductCard)
            .collect(Collectors.toList());
    }

    /**
     * Converts a ProductOption to ProductOptionDTO with localized content.
     */
    private static ProductOptionDTO toProductOptionDTO(ProductOption option) {
        if (option == null) {
            return null;
        }

        ProductOptionDTO dto = new ProductOptionDTO();
        dto.setId(option.getId());
        dto.setName(I18nUtils.extract(option.getNameJson()));
        dto.setType(option.getType());
        dto.setRequired(option.isRequired());
        dto.setMinSelect(option.getMinSelect());
        dto.setMaxSelect(option.getMaxSelect());

        if (option.getValues() != null) {
            dto.setValues(option.getValues().stream()
                .filter(value -> value.isActive() && value.isAvailableInStore())
                .map(ProductMapper::toProductOptionValueDTO)
                .collect(Collectors.toList()));
        }

        return dto;
    }

    /**
     * Converts a ProductOptionValue to ProductOptionValueDTO with localized content.
     */
    private static ProductOptionValueDTO toProductOptionValueDTO(ProductOptionValue value) {
        if (value == null) {
            return null;
        }

        ProductOptionValueDTO dto = new ProductOptionValueDTO();
        dto.setId(value.getId());
        dto.setName(I18nUtils.extract(value.getNameJson()));
        dto.setPriceDelta(value.getPriceDelta());
        dto.setAvailable(ProductConstants.AvailabilityStatus.AVAILABLE
            .equals(value.getStoreAvailabilityStatus()));
        dto.setNote(I18nUtils.extract(value.getStoreNote()));

        return dto;
    }
}
