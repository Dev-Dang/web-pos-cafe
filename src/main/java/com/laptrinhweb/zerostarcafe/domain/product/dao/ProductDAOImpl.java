package com.laptrinhweb.zerostarcafe.domain.product.dao;

import com.laptrinhweb.zerostarcafe.domain.product.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <h2>Description:</h2>
 * <p>
 * JDBC implementation of {@link ProductDAO} that interacts with product-related tables.
 * Handles complex queries joining multiple tables for catalog items, pricing, availability,
 * and options.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 26/12/2025
 * @since 1.0.0
 */
public class ProductDAOImpl implements ProductDAO {

    private final Connection conn;

    public ProductDAOImpl(Connection conn) {
        this.conn = conn;
    }

    // ==========================================================
    // CATALOG ITEM RETRIEVAL
    // ==========================================================

    @Override
    public Optional<CatalogItem> findCatalogItemBySlugAndStoreId(String productSlug, long storeId) throws SQLException {
        String sql = """
                SELECT 
                    mi.id, mi.category_id, mi.name, mi.slug, mi.image_url, mi.description,
                    mi.base_price, mi.unit, mi.is_active,
                    c.slug AS category_slug,
                    smi.in_menu, smi.availability_status, smi.sold_out_until, smi.sold_out_note,
                    COALESCE(sips.price, mi.base_price) AS resolved_price,
                    (SELECT COUNT(*) FROM item_option_groups iog WHERE iog.menu_item_id = mi.id) AS has_options
                FROM menu_items mi
                INNER JOIN categories c ON mi.category_id = c.id
                INNER JOIN store_menu_items smi ON mi.id = smi.menu_item_id
                LEFT JOIN store_item_price_schedules sips 
                    ON smi.store_id = sips.store_id 
                    AND smi.menu_item_id = sips.menu_item_id
                    AND NOW() BETWEEN sips.valid_from AND sips.valid_to
                WHERE mi.slug = ?
                  AND smi.store_id = ?
                  AND mi.is_active = 1
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, productSlug);
            ps.setLong(2, storeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapCatalogItem(rs));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public List<CatalogItem> findCatalogItemsByStoreId(long storeId) throws SQLException {
        String sql = """
                SELECT 
                    mi.id, mi.category_id, mi.name, mi.slug, mi.image_url, mi.description,
                    mi.base_price, mi.unit, mi.is_active,
                    c.slug AS category_slug,
                    smi.in_menu, smi.availability_status, smi.sold_out_until, smi.sold_out_note,
                    COALESCE(sips.price, mi.base_price) AS resolved_price,
                    (SELECT COUNT(*) FROM item_option_groups iog WHERE iog.menu_item_id = mi.id) AS has_options
                FROM menu_items mi
                INNER JOIN categories c ON mi.category_id = c.id
                INNER JOIN store_menu_items smi ON mi.id = smi.menu_item_id
                LEFT JOIN store_item_price_schedules sips 
                    ON smi.store_id = sips.store_id 
                    AND smi.menu_item_id = sips.menu_item_id
                    AND NOW() BETWEEN sips.valid_from AND sips.valid_to
                WHERE smi.store_id = ?
                  AND mi.is_active = 1
                  AND smi.in_menu = 1
                ORDER BY mi.category_id, mi.name
                """;

        List<CatalogItem> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, storeId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapCatalogItem(rs));
                }
            }
        }

        return list;
    }

    @Override
    public List<CatalogItem> findCatalogItemsByStoreIdAndCategoryId(long storeId, long categoryId) throws SQLException {
        String sql = """
                SELECT 
                    mi.id, mi.category_id, mi.name, mi.slug, mi.image_url, mi.description,
                    mi.base_price, mi.unit, mi.is_active,
                    c.slug AS category_slug,
                    smi.in_menu, smi.availability_status, smi.sold_out_until, smi.sold_out_note,
                    COALESCE(sips.price, mi.base_price) AS resolved_price,
                    (SELECT COUNT(*) FROM item_option_groups iog WHERE iog.menu_item_id = mi.id) AS has_options
                FROM menu_items mi
                INNER JOIN categories c ON mi.category_id = c.id
                INNER JOIN store_menu_items smi ON mi.id = smi.menu_item_id
                LEFT JOIN store_item_price_schedules sips 
                    ON smi.store_id = sips.store_id 
                    AND smi.menu_item_id = sips.menu_item_id
                    AND NOW() BETWEEN sips.valid_from AND sips.valid_to
                WHERE smi.store_id = ?
                  AND mi.category_id = ?
                  AND mi.is_active = 1
                  AND smi.in_menu = 1
                ORDER BY mi.name
                """;

        List<CatalogItem> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, storeId);
            ps.setLong(2, categoryId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapCatalogItem(rs));
                }
            }
        }

        return list;
    }

    // ==========================================================
    // PRODUCT DETAIL RETRIEVAL
    // ==========================================================

    @Override
    public Optional<ProductDetail> findProductDetailBySlugAndStoreId(String productSlug, long storeId) throws SQLException {
        Optional<CatalogItem> itemOpt = findCatalogItemBySlugAndStoreId(productSlug, storeId);
        if (itemOpt.isEmpty()) {
            return Optional.empty();
        }

        CatalogItem item = itemOpt.get();
        List<OptionGroup> optionGroups = findOptionGroupsByProductIdAndStoreId(item.getId(), storeId);

        ProductDetail detail = new ProductDetail();
        detail.setItem(item);
        detail.setOptionGroups(optionGroups);

        return Optional.of(detail);
    }

    // ==========================================================
    // OPTION GROUPS RETRIEVAL
    // ==========================================================

    @Override
    public List<OptionGroup> findOptionGroupsByProductIdAndStoreId(long productId, long storeId) throws SQLException {
        String sql = """
                SELECT DISTINCT
                    og.id, og.name, og.type, og.is_required, og.min_select, og.max_select
                FROM option_groups og
                INNER JOIN item_option_groups iog ON og.id = iog.option_group_id
                WHERE iog.menu_item_id = ?
                ORDER BY og.id
                """;

        List<OptionGroup> groups = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, productId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OptionGroup group = mapOptionGroup(rs);
                    group.setValues(findOptionValuesByGroupIdAndStoreId(group.getId(), storeId));
                    groups.add(group);
                }
            }
        }

        return groups;
    }

    private List<OptionValue> findOptionValuesByGroupIdAndStoreId(long groupId, long storeId) throws SQLException {
        String sql = """
                SELECT 
                    ov.id, ov.option_group_id, ov.name, ov.price_delta, ov.is_active,
                    sov.availability_status, sov.sold_out_until, sov.note
                FROM option_values ov
                LEFT JOIN store_option_values sov 
                    ON ov.id = sov.option_value_id 
                    AND sov.store_id = ?
                WHERE ov.option_group_id = ?
                  AND ov.is_active = 1
                ORDER BY ov.id
                """;

        List<OptionValue> values = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, storeId);
            ps.setLong(2, groupId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    values.add(mapOptionValue(rs));
                }
            }
        }

        return values;
    }

    // ==========================================================
    // SEARCH
    // ==========================================================

    @Override
    public List<CatalogItem> searchCatalogItemsByNameAndStoreId(long storeId, String searchTerm) throws SQLException {
        String sql = """
                SELECT 
                    mi.id, mi.category_id, mi.name, mi.slug, mi.image_url, mi.description,
                    mi.base_price, mi.unit, mi.is_active,
                    c.slug AS category_slug,
                    smi.in_menu, smi.availability_status, smi.sold_out_until, smi.sold_out_note,
                    COALESCE(sips.price, mi.base_price) AS resolved_price,
                    (SELECT COUNT(*) FROM item_option_groups iog WHERE iog.menu_item_id = mi.id) AS has_options
                FROM menu_items mi
                INNER JOIN categories c ON mi.category_id = c.id
                INNER JOIN store_menu_items smi ON mi.id = smi.menu_item_id
                LEFT JOIN store_item_price_schedules sips 
                    ON smi.store_id = sips.store_id 
                    AND smi.menu_item_id = sips.menu_item_id
                    AND NOW() BETWEEN sips.valid_from AND sips.valid_to
                WHERE smi.store_id = ?
                  AND mi.is_active = 1
                  AND smi.in_menu = 1
                  AND mi.name LIKE ?
                ORDER BY mi.name
                """;

        List<CatalogItem> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, storeId);
            ps.setString(2, "%" + searchTerm + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapCatalogItem(rs));
                }
            }
        }

        return list;
    }

    // ==========================================================
    // SINGLE ITEM LOOKUPS (FOR CART VALIDATION)
    // ==========================================================

    @Override
    public Optional<CatalogItem> findCatalogItemByIdAndStoreId(long productId, long storeId) throws SQLException {
        String sql = """
                SELECT 
                    mi.id, mi.category_id, mi.name, mi.slug, mi.image_url, mi.description,
                    mi.base_price, mi.unit, mi.is_active,
                    c.slug AS category_slug,
                    smi.in_menu, smi.availability_status, smi.sold_out_until, smi.sold_out_note,
                    COALESCE(sips.price, mi.base_price) AS resolved_price,
                    (SELECT COUNT(*) FROM item_option_groups iog WHERE iog.menu_item_id = mi.id) AS has_options
                FROM menu_items mi
                INNER JOIN categories c ON mi.category_id = c.id
                INNER JOIN store_menu_items smi ON mi.id = smi.menu_item_id
                LEFT JOIN store_item_price_schedules sips 
                    ON smi.store_id = sips.store_id 
                    AND smi.menu_item_id = sips.menu_item_id
                    AND NOW() BETWEEN sips.valid_from AND sips.valid_to
                WHERE mi.id = ?
                  AND smi.store_id = ?
                  AND mi.is_active = 1
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, productId);
            ps.setLong(2, storeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapCatalogItem(rs));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<OptionValue> findOptionValueById(long optionValueId) throws SQLException {
        String sql = """
                SELECT 
                    ov.id, ov.option_group_id, ov.name, ov.price_delta, ov.is_active,
                    NULL AS availability_status, NULL AS sold_out_until, NULL AS note
                FROM option_values ov
                WHERE ov.id = ?
                  AND ov.is_active = 1
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, optionValueId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapOptionValue(rs));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<OptionGroup> findOptionGroupByOptionValueId(long optionValueId) throws SQLException {
        String sql = """
                SELECT 
                    og.id, og.name, og.type, og.is_required, og.min_select, og.max_select
                FROM option_groups og
                INNER JOIN option_values ov ON og.id = ov.option_group_id
                WHERE ov.id = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, optionValueId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapOptionGroup(rs));
                }
            }
        }

        return Optional.empty();
    }

    // ==========================================================
    // MAPPING UTILITIES
    // ==========================================================

    private CatalogItem mapCatalogItem(ResultSet rs) throws SQLException {
        CatalogItem item = new CatalogItem();
        item.setId(rs.getLong("id"));
        item.setCategoryId(rs.getLong("category_id"));
        item.setCategorySlug(rs.getString("category_slug"));
        item.setName(rs.getString("name"));
        item.setSlug(rs.getString("slug"));
        item.setImageUrl(rs.getString("image_url"));
        item.setDescription(rs.getString("description"));
        item.setBasePrice(rs.getInt("base_price"));
        item.setUnit(rs.getString("unit"));
        item.setActive(rs.getBoolean("is_active"));
        item.setResolvedPrice(rs.getInt("resolved_price"));
        item.setHasOptions(rs.getInt("has_options") > 0);

        // Map availability
        Availability availability = new Availability();
        availability.setInMenu(rs.getBoolean("in_menu"));

        String statusStr = rs.getString("availability_status");
        availability.setStatus(statusStr != null
                ? AvailabilityStatus.valueOf(statusStr.toUpperCase())
                : AvailabilityStatus.AVAILABLE);

        Timestamp soldOutUntil = rs.getTimestamp("sold_out_until");
        if (soldOutUntil != null) {
            availability.setSoldOutUntil(soldOutUntil.toLocalDateTime());
        }
        availability.setSoldOutNote(rs.getString("sold_out_note"));

        item.setAvailability(availability);

        return item;
    }

    private OptionGroup mapOptionGroup(ResultSet rs) throws SQLException {
        OptionGroup group = new OptionGroup();
        group.setId(rs.getLong("id"));
        group.setName(rs.getString("name"));
        group.setType(rs.getString("type"));
        group.setRequired(rs.getBoolean("is_required"));
        group.setMinSelect(rs.getInt("min_select"));
        group.setMaxSelect(rs.getInt("max_select"));
        return group;
    }

    private OptionValue mapOptionValue(ResultSet rs) throws SQLException {
        OptionValue value = new OptionValue();
        value.setId(rs.getLong("id"));
        value.setGroupId(rs.getLong("option_group_id"));
        value.setName(rs.getString("name"));
        value.setPriceDelta(rs.getInt("price_delta"));
        value.setActive(rs.getBoolean("is_active"));

        // Map store-specific availability
        String storeStatus = rs.getString("availability_status");
        if (storeStatus != null) {
            OptionAvailability storeAvail = new OptionAvailability();
            storeAvail.setStatus(AvailabilityStatus.valueOf(storeStatus.toUpperCase()));

            Timestamp soldOutUntil = rs.getTimestamp("sold_out_until");
            if (soldOutUntil != null) {
                storeAvail.setSoldOutUntil(soldOutUntil.toLocalDateTime());
            }
            storeAvail.setNote(rs.getString("note"));

            value.setStoreAvailability(storeAvail);
        }

        return value;
    }
}

