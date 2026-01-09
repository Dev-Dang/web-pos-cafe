package com.laptrinhweb.zerostarcafe.domain.product.dao;

import com.laptrinhweb.zerostarcafe.core.context.DBContext;
import com.laptrinhweb.zerostarcafe.domain.product.model.AvailabilityStatus;
import com.laptrinhweb.zerostarcafe.domain.product.model.Product;
import com.laptrinhweb.zerostarcafe.domain.product.model.ProductOption;
import com.laptrinhweb.zerostarcafe.domain.product.model.ProductOptionValue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * <h2>Description:</h2>
 * <p>
 * JDBC implementation of {@link ProductDAO} that interacts with
 * the menu items, options, and pricing tables for product data.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 06/01/2026
 * @since 1.0.0
 */
public class ProductDAOImpl implements ProductDAO {

    // ==========================================================
    // RETRIEVAL
    // ==========================================================

    @Override
    public List<Product> findByCategoryId(long categoryId, long storeId) throws SQLException {
        String sql = """
                SELECT 
                    mi.id, mi.category_id, mi.name, mi.slug, mi.image_url, 
                    mi.description, mi.base_price, mi.unit, mi.is_active, mi.created_at,
                    smi.in_menu, smi.availability_status, smi.sold_out_until, smi.sold_out_note,
                    sips.price as promo_price, sips.valid_from, sips.valid_to
                FROM menu_items mi
                LEFT JOIN store_menu_items smi ON mi.id = smi.menu_item_id AND smi.store_id = ?
                LEFT JOIN store_item_price_schedules sips ON mi.id = sips.menu_item_id 
                    AND sips.store_id = ? AND sips.valid_from <= NOW() AND sips.valid_to >= NOW()
                WHERE mi.category_id = ? AND mi.is_active = 1 
                    AND (smi.in_menu IS NULL OR smi.in_menu = 1)
                ORDER BY mi.created_at DESC
                """;

        Connection conn = DBContext.getOrCreate();
        List<Product> products = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, storeId);
            ps.setLong(2, storeId);
            ps.setLong(3, categoryId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapToProduct(rs));
                }
            }
        }

        return products;
    }

    @Override
    public List<Product> findByCategoryId(long categoryId, long storeId, int limit, int offset) throws SQLException {
        String sql = """
                SELECT 
                    mi.id, mi.category_id, mi.name, mi.slug, mi.image_url, 
                    mi.description, mi.base_price, mi.unit, mi.is_active, mi.created_at,
                    smi.in_menu, smi.availability_status, smi.sold_out_until, smi.sold_out_note,
                    sips.price as promo_price, sips.valid_from, sips.valid_to
                FROM menu_items mi
                LEFT JOIN store_menu_items smi ON mi.id = smi.menu_item_id AND smi.store_id = ?
                LEFT JOIN store_item_price_schedules sips ON mi.id = sips.menu_item_id 
                    AND sips.store_id = ? AND sips.valid_from <= NOW() AND sips.valid_to >= NOW()
                WHERE mi.category_id = ? AND mi.is_active = 1 
                    AND (smi.in_menu IS NULL OR smi.in_menu = 1)
                ORDER BY mi.created_at DESC
                LIMIT ? OFFSET ?
                """;

        Connection conn = DBContext.getOrCreate();
        List<Product> products = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, storeId);
            ps.setLong(2, storeId);
            ps.setLong(3, categoryId);
            ps.setInt(4, limit);
            ps.setInt(5, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapToProduct(rs));
                }
            }
        }

        return products;
    }

    @Override
    public Optional<Product> findById(long productId, long storeId) throws SQLException {
        String sql = """
                SELECT 
                    mi.id, mi.category_id, mi.name, mi.slug, mi.image_url, 
                    mi.description, mi.base_price, mi.unit, mi.is_active, mi.created_at,
                    smi.in_menu, smi.availability_status, smi.sold_out_until, smi.sold_out_note,
                    sips.price as promo_price, sips.valid_from, sips.valid_to
                FROM menu_items mi
                LEFT JOIN store_menu_items smi ON mi.id = smi.menu_item_id AND smi.store_id = ?
                LEFT JOIN store_item_price_schedules sips ON mi.id = sips.menu_item_id 
                    AND sips.store_id = ? AND sips.valid_from <= NOW() AND sips.valid_to >= NOW()
                WHERE mi.id = ? AND mi.is_active = 1
                """;

        Connection conn = DBContext.getOrCreate();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, storeId);
            ps.setLong(2, storeId);
            ps.setLong(3, productId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Product product = mapToProduct(rs);
                    product.setOptions(loadProductOptions(productId, storeId));
                    return Optional.of(product);
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<Product> findBySlug(String slug, long storeId) throws SQLException {
        String sql = """
                SELECT 
                    mi.id, mi.category_id, mi.name, mi.slug, mi.image_url, 
                    mi.description, mi.base_price, mi.unit, mi.is_active, mi.created_at,
                    smi.in_menu, smi.availability_status, smi.sold_out_until, smi.sold_out_note,
                    sips.price as promo_price, sips.valid_from, sips.valid_to
                FROM menu_items mi
                LEFT JOIN store_menu_items smi ON mi.id = smi.menu_item_id AND smi.store_id = ?
                LEFT JOIN store_item_price_schedules sips ON mi.id = sips.menu_item_id 
                    AND sips.store_id = ? AND sips.valid_from <= NOW() AND sips.valid_to >= NOW()
                WHERE mi.slug = ? AND mi.is_active = 1
                """;

        Connection conn = DBContext.getOrCreate();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, storeId);
            ps.setLong(2, storeId);
            ps.setString(3, slug);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Product product = mapToProduct(rs);
                    product.setOptions(loadProductOptions(product.getId(), storeId));
                    return Optional.of(product);
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<Product> findActiveProductById(long productId, long storeId) throws SQLException {
        String sql = """
                SELECT 
                    mi.id, mi.category_id, mi.name, mi.slug, mi.image_url, 
                    mi.description, mi.base_price, mi.unit, mi.is_active, mi.created_at,
                    smi.in_menu, smi.availability_status, smi.sold_out_until, smi.sold_out_note,
                    sips.price as promo_price, sips.valid_from, sips.valid_to
                FROM menu_items mi
                LEFT JOIN store_menu_items smi ON mi.id = smi.menu_item_id AND smi.store_id = ?
                LEFT JOIN store_item_price_schedules sips ON mi.id = sips.menu_item_id 
                    AND sips.store_id = ? AND sips.valid_from <= NOW() AND sips.valid_to >= NOW()
                WHERE mi.id = ? AND mi.is_active = 1 
                    AND (smi.availability_status IS NULL OR smi.availability_status = 'available')
                """;

        Connection conn = DBContext.getOrCreate();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, storeId);
            ps.setLong(2, storeId);
            ps.setLong(3, productId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Product product = mapToProduct(rs);
                    product.setOptions(loadProductOptions(productId, storeId));
                    return Optional.of(product);
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Product> findAvailableInStore(long storeId) throws SQLException {
        String sql = """
                SELECT 
                    mi.id, mi.category_id, mi.name, mi.slug, mi.image_url, 
                    mi.description, mi.base_price, mi.unit, mi.is_active, mi.created_at,
                    smi.in_menu, smi.availability_status, smi.sold_out_until, smi.sold_out_note,
                    sips.price as promo_price, sips.valid_from, sips.valid_to
                FROM menu_items mi
                JOIN store_menu_items smi ON mi.id = smi.menu_item_id AND smi.store_id = ?
                LEFT JOIN store_item_price_schedules sips ON mi.id = sips.menu_item_id 
                    AND sips.store_id = ? AND sips.valid_from <= NOW() AND sips.valid_to >= NOW()
                WHERE mi.is_active = 1 AND smi.in_menu = 1 
                    AND smi.availability_status = 'available'
                ORDER BY mi.created_at DESC
                """;

        Connection conn = DBContext.getOrCreate();
        List<Product> products = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, storeId);
            ps.setLong(2, storeId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapToProduct(rs));
                }
            }
        }

        return products;
    }

    // ==========================================================
    // PRIVATE HELPER METHODS
    // ==========================================================

    /**
     * Maps a ResultSet row to a Product object with localized fields.
     */
    private Product mapToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();

        // Basic fields
        product.setId(rs.getLong("id"));
        product.setCategoryId(rs.getLong("category_id"));
        product.setNameJson(rs.getString("name"));
        product.setSlug(rs.getString("slug"));
        product.setImageUrl(rs.getString("image_url"));
        product.setDescriptionJson(rs.getString("description"));
        product.setBasePrice(rs.getInt("base_price"));
        product.setUnit(rs.getString("unit"));
        product.setActive(rs.getBoolean("is_active"));
        product.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);

        // Store-specific fields
        product.setInMenu(rs.getBoolean("in_menu"));
        product.setAvailabilityStatus(AvailabilityStatus
                .fromString(rs.getString("availability_status")));
        product.setSoldOutUntil(rs.getTimestamp("sold_out_until") != null ?
                rs.getTimestamp("sold_out_until").toLocalDateTime() : null);
        product.setSoldOutNote(rs.getString("sold_out_note"));

        // Pricing logic
        Integer promoPrice = rs.getObject("promo_price", Integer.class);
        if (promoPrice != null) {
            product.setCurrentPrice(promoPrice);
            product.setHasPromotion(true);
            product.setPromotionValidFrom(rs.getTimestamp("valid_from") != null ?
                    rs.getTimestamp("valid_from").toLocalDateTime() : null);
            product.setPromotionValidTo(rs.getTimestamp("valid_to") != null ?
                    rs.getTimestamp("valid_to").toLocalDateTime() : null);
        } else {
            product.setCurrentPrice(product.getBasePrice());
            product.setHasPromotion(false);
        }

        return product;
    }

    /**
     * Loads all option groups and their values for a specific product.
     */
    private List<ProductOption> loadProductOptions(long productId, long storeId) throws SQLException {
        String sql = """
                SELECT 
                    og.id as group_id, og.name as group_name, og.type, 
                    og.is_required, og.min_select, og.max_select,
                    ov.id as value_id, ov.name as value_name, ov.price_delta,
                    ov.is_active as value_active,
                    COALESCE(sov.is_active, 1) as store_value_active,
                    COALESCE(sov.availability_status, 'available') as store_availability_status,
                    sov.note as store_note
                FROM item_option_groups iog
                JOIN option_groups og ON iog.option_group_id = og.id
                JOIN option_values ov ON og.id = ov.option_group_id
                LEFT JOIN store_option_values sov ON ov.id = sov.option_value_id AND sov.store_id = ?
                WHERE iog.menu_item_id = ? AND ov.is_active = 1
                ORDER BY og.id, ov.price_delta ASC
                """;

        Connection conn = DBContext.getOrCreate();
        Map<Long, ProductOption> optionMap = new HashMap<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, storeId);
            ps.setLong(2, productId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    long groupId = rs.getLong("group_id");

                    // Get or create option group
                    ProductOption option = optionMap.computeIfAbsent(groupId, k -> {
                        ProductOption opt = new ProductOption();
                        try {
                            opt.setId(groupId);
                            opt.setNameJson(rs.getString("group_name"));
                            opt.setType(rs.getString("type"));
                            opt.setRequired(rs.getBoolean("is_required"));
                            opt.setMinSelect(rs.getInt("min_select"));
                            opt.setMaxSelect(rs.getInt("max_select"));
                            opt.setValues(new ArrayList<>());
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        return opt;
                    });

                    // Add option value
                    ProductOptionValue value = new ProductOptionValue();
                    value.setId(rs.getLong("value_id"));
                    value.setOptionGroupId(groupId);
                    value.setNameJson(rs.getString("value_name"));
                    value.setPriceDelta(rs.getInt("price_delta"));
                    value.setActive(rs.getBoolean("value_active"));
                    value.setAvailableInStore(rs.getBoolean("store_value_active"));
                    value.setStoreAvailabilityStatus(rs.getString("store_availability_status"));
                    value.setStoreNote(rs.getString("store_note"));

                    option.getValues().add(value);
                }
            }
        }

        return new ArrayList<>(optionMap.values());
    }

    @Override
    public List<Product> searchProducts(String query, long storeId) throws SQLException {
        String sql = """
                SELECT 
                    mi.id, mi.category_id, mi.name, mi.slug, mi.image_url, 
                    mi.description, mi.base_price, mi.unit, mi.is_active, mi.created_at,
                    smi.in_menu, smi.availability_status, smi.sold_out_until, smi.sold_out_note,
                    sips.price as promo_price, sips.valid_from, sips.valid_to
                FROM menu_items mi
                LEFT JOIN store_menu_items smi ON mi.id = smi.menu_item_id AND smi.store_id = ?
                LEFT JOIN store_item_price_schedules sips ON mi.id = sips.menu_item_id 
                    AND sips.store_id = ? AND sips.valid_from <= NOW() AND sips.valid_to >= NOW()
                WHERE mi.is_active = 1 
                    AND (smi.in_menu IS NULL OR smi.in_menu = 1)
                    AND (mi.name LIKE ? OR mi.description LIKE ?)
                ORDER BY 
                    CASE 
                        WHEN mi.name LIKE ? THEN 1 
                        WHEN mi.description LIKE ? THEN 2 
                        ELSE 3 
                    END,
                    mi.name
                """;

        Connection conn = DBContext.getOrCreate();
        List<Product> products = new ArrayList<>();

        // Prepare search patterns
        String searchPattern = "%" + query.toLowerCase() + "%";
        String exactPattern = query.toLowerCase() + "%";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, storeId);
            ps.setLong(2, storeId);
            ps.setString(3, searchPattern);
            ps.setString(4, searchPattern);
            ps.setString(5, exactPattern);  // For exact match priority
            ps.setString(6, exactPattern);  // For exact match priority

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapToProduct(rs));
                }
            }
        }

        return products;
    }

    @Override
    public List<Product> searchProducts(String query, long storeId, int limit, int offset) throws SQLException {
        String sql = """
                SELECT 
                    mi.id, mi.category_id, mi.name, mi.slug, mi.image_url, 
                    mi.description, mi.base_price, mi.unit, mi.is_active, mi.created_at,
                    smi.in_menu, smi.availability_status, smi.sold_out_until, smi.sold_out_note,
                    sips.price as promo_price, sips.valid_from, sips.valid_to
                FROM menu_items mi
                LEFT JOIN store_menu_items smi ON mi.id = smi.menu_item_id AND smi.store_id = ?
                LEFT JOIN store_item_price_schedules sips ON mi.id = sips.menu_item_id 
                    AND sips.store_id = ? AND sips.valid_from <= NOW() AND sips.valid_to >= NOW()
                WHERE mi.is_active = 1 
                    AND (smi.in_menu IS NULL OR smi.in_menu = 1)
                    AND (mi.name LIKE ? OR mi.description LIKE ?)
                ORDER BY 
                    CASE 
                        WHEN mi.name LIKE ? THEN 1 
                        WHEN mi.description LIKE ? THEN 2 
                        ELSE 3 
                    END,
                    mi.name
                LIMIT ? OFFSET ?
                """;

        Connection conn = DBContext.getOrCreate();
        List<Product> products = new ArrayList<>();

        // Prepare search patterns
        String searchPattern = "%" + query.toLowerCase() + "%";
        String exactPattern = query.toLowerCase() + "%";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, storeId);
            ps.setLong(2, storeId);
            ps.setString(3, searchPattern);
            ps.setString(4, searchPattern);
            ps.setString(5, exactPattern);  // For exact match priority
            ps.setString(6, exactPattern);  // For exact match priority
            ps.setInt(7, limit);
            ps.setInt(8, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapToProduct(rs));
                }
            }
        }

        return products;
    }

    @Override
    public int countSearchResults(String query, long storeId) throws SQLException {
        String sql = """
                SELECT COUNT(*) as total
                FROM menu_items mi
                LEFT JOIN store_menu_items smi ON mi.id = smi.menu_item_id AND smi.store_id = ?
                WHERE mi.is_active = 1 
                    AND (smi.in_menu IS NULL OR smi.in_menu = 1)
                    AND (mi.name LIKE ? OR mi.description LIKE ?)
                """;

        Connection conn = DBContext.getOrCreate();
        String searchPattern = "%" + query.toLowerCase() + "%";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, storeId);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        }

        return 0;
    }

    @Override
    public List<Product> findByCategorySlug(String categorySlug, long storeId, int limit, int offset) throws SQLException {
        String sql = """
                SELECT 
                    mi.id, mi.category_id, mi.name, mi.slug, mi.image_url, 
                    mi.description, mi.base_price, mi.unit, mi.is_active, mi.created_at,
                    smi.in_menu, smi.availability_status, smi.sold_out_until, smi.sold_out_note,
                    sips.price as promo_price, sips.valid_from, sips.valid_to
                FROM menu_items mi
                LEFT JOIN categories c ON mi.category_id = c.id
                LEFT JOIN store_menu_items smi ON mi.id = smi.menu_item_id AND smi.store_id = ?
                LEFT JOIN store_item_price_schedules sips ON mi.id = sips.menu_item_id 
                    AND sips.store_id = ? AND sips.valid_from <= NOW() AND sips.valid_to >= NOW()
                WHERE c.slug = ? AND mi.is_active = 1 AND c.is_active = 1
                    AND (smi.in_menu IS NULL OR smi.in_menu = 1)
                ORDER BY mi.created_at DESC
                LIMIT ? OFFSET ?
                """;

        Connection conn = DBContext.getOrCreate();
        List<Product> products = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, storeId);
            ps.setLong(2, storeId);
            ps.setString(3, categorySlug);
            ps.setInt(4, limit);
            ps.setInt(5, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapToProduct(rs));
                }
            }
        }

        return products;
    }

    @Override
    public List<Product> findByCategorySlug(String categorySlug, long storeId) throws SQLException {
        String sql = """
                SELECT 
                    mi.id, mi.category_id, mi.name, mi.slug, mi.image_url, 
                    mi.description, mi.base_price, mi.unit, mi.is_active, mi.created_at,
                    smi.in_menu, smi.availability_status, smi.sold_out_until, smi.sold_out_note,
                    sips.price as promo_price, sips.valid_from, sips.valid_to
                FROM menu_items mi
                LEFT JOIN categories c ON mi.category_id = c.id
                LEFT JOIN store_menu_items smi ON mi.id = smi.menu_item_id AND smi.store_id = ?
                LEFT JOIN store_item_price_schedules sips ON mi.id = sips.menu_item_id 
                    AND sips.store_id = ? AND sips.valid_from <= NOW() AND sips.valid_to >= NOW()
                WHERE c.slug = ? AND mi.is_active = 1 AND c.is_active = 1
                    AND (smi.in_menu IS NULL OR smi.in_menu = 1)
                ORDER BY mi.created_at DESC
                """;

        Connection conn = DBContext.getOrCreate();
        List<Product> products = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, storeId);
            ps.setLong(2, storeId);
            ps.setString(3, categorySlug);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapToProduct(rs));
                }
            }
        }

        return products;
    }
}