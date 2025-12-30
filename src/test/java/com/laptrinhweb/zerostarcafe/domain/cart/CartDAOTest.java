package com.laptrinhweb.zerostarcafe.domain.cart;

import com.laptrinhweb.zerostarcafe.domain.cart.dao.CartDAO;
import com.laptrinhweb.zerostarcafe.domain.cart.dao.CartDAOImpl;
import com.laptrinhweb.zerostarcafe.domain.cart.model.Cart;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItem;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItemOption;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for CartDAO.
 * 
 * NOTE: These tests require a direct JDBC connection since JNDI is not
 * available in test environment. Configure DB_URL, DB_USER, DB_PASSWORD
 * before running.
 * 
 * To run: Set environment variables or modify the constants below.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CartDAOTest {

    // Configure these for your local database
    private static final String DB_URL = "jdbc:mysql://localhost:3306/zerostar_cf?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private static Connection conn;
    private static CartDAO cartDAO;

    // Test data
    private static final long TEST_USER_ID = 9L;  // Existing user from DB
    private static final long TEST_STORE_ID = 1L; // Existing store from DB
    private static final long TEST_MENU_ITEM_ID = 9L; // A-Mê Classic

    private static long createdCartId;
    private static long createdItemId;

    @BeforeAll
    static void setUp() throws SQLException {
        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        conn.setAutoCommit(false); // Use transaction for test isolation
        cartDAO = new CartDAOImpl(conn);

        // Clean up any existing test cart
        Cart existing = cartDAO.findByUserIdAndStoreId(TEST_USER_ID, TEST_STORE_ID);
        if (existing != null) {
            cartDAO.deleteById(existing.getId());
        }
    }

    @AfterAll
    static void tearDown() throws SQLException {
        if (conn != null) {
            conn.rollback(); // Rollback all test changes
            conn.close();
        }
    }

    @Test
    @Order(1)
    @DisplayName("save - should create new cart for user")
    void save_createsNewCart() throws SQLException {
        Cart cart = new Cart();
        cart.setUserId(TEST_USER_ID);
        cart.setStoreId(TEST_STORE_ID);
        
        Cart saved = cartDAO.save(cart);

        assertNotNull(saved);
        assertTrue(saved.getId() > 0);
        assertEquals(TEST_USER_ID, saved.getUserId());
        assertEquals(TEST_STORE_ID, saved.getStoreId());

        createdCartId = saved.getId();
    }

    @Test
    @Order(2)
    @DisplayName("findByUserIdAndStoreId - should find existing cart")
    void findByUserIdAndStoreId_findsCart() throws SQLException {
        Cart cart = cartDAO.findByUserIdAndStoreId(TEST_USER_ID, TEST_STORE_ID);

        assertNotNull(cart);
        assertEquals(createdCartId, cart.getId());
        assertNotNull(cart.getItems());
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    @Order(3)
    @DisplayName("findByUserIdAndStoreId - should return null for non-existent cart")
    void findByUserIdAndStoreId_returnsNullForNonExistent() throws SQLException {
        Cart cart = cartDAO.findByUserIdAndStoreId(99999L, TEST_STORE_ID);

        assertNull(cart);
    }

    @Test
    @Order(4)
    @DisplayName("saveItem - should insert cart item")
    void saveItem_insertsItem() throws SQLException {
        CartItem item = new CartItem();
        item.setCartId(createdCartId);
        item.setMenuItemId(TEST_MENU_ITEM_ID);
        item.setQty(2);
        item.setUnitPriceSnapshot(39000);
        item.setOptionsPriceSnapshot(6000);
        item.setNote("Ít đường");
        item.setItemHash("test_hash_12345");
        item.setItemNameSnapshot("A-Mê Classic");

        CartItem inserted = cartDAO.saveItem(item);

        assertNotNull(inserted);
        assertTrue(inserted.getId() > 0);
        assertEquals(2, inserted.getQty());
        assertEquals(39000, inserted.getUnitPriceSnapshot());

        createdItemId = inserted.getId();
    }

    @Test
    @Order(5)
    @DisplayName("saveOption - should insert cart item option")
    void saveOption_insertsOption() throws SQLException {
        CartItemOption option = new CartItemOption();
        option.setCartItemId(createdItemId);
        option.setOptionValueId(16L); // Size L
        option.setOptionGroupNameSnapshot("Size");
        option.setOptionValueNameSnapshot("L");
        option.setPriceDeltaSnapshot(6000);

        CartItemOption inserted = cartDAO.saveOption(option);

        assertNotNull(inserted);
        assertTrue(inserted.getId() > 0);
        assertEquals("Size", inserted.getOptionGroupNameSnapshot());
    }

    @Test
    @Order(6)
    @DisplayName("findItemsByCartId - should return items with options")
    void findItemsByCartId_returnsItemsWithOptions() throws SQLException {
        List<CartItem> items = cartDAO.findItemsByCartId(createdCartId);

        assertNotNull(items);
        assertEquals(1, items.size());

        CartItem item = items.get(0);
        assertEquals(createdItemId, item.getId());
        assertEquals("A-Mê Classic", item.getItemNameSnapshot());

        // Verify options loaded
        assertNotNull(item.getOptions());
        assertEquals(1, item.getOptions().size());
        assertEquals("Size", item.getOptions().get(0).getOptionGroupNameSnapshot());
    }

    @Test
    @Order(7)
    @DisplayName("findItemByCartIdAndItemHash - should find item by hash")
    void findItemByCartIdAndItemHash_findsItem() throws SQLException {
        CartItem item = cartDAO.findItemByCartIdAndItemHash(createdCartId, "test_hash_12345");

        assertNotNull(item);
        assertEquals(createdItemId, item.getId());
    }

    @Test
    @Order(8)
    @DisplayName("findItemByCartIdAndItemHash - should return null for wrong hash")
    void findItemByCartIdAndItemHash_returnsNullForWrongHash() throws SQLException {
        CartItem item = cartDAO.findItemByCartIdAndItemHash(createdCartId, "wrong_hash");

        assertNull(item);
    }

    @Test
    @Order(9)
    @DisplayName("updateItemQty - should update quantity")
    void updateItemQty_updatesQuantity() throws SQLException {
        cartDAO.updateItemQty(createdItemId, 5);

        CartItem item = cartDAO.findItemById(createdItemId);
        assertNotNull(item);
        assertEquals(5, item.getQty());
    }

    @Test
    @Order(10)
    @DisplayName("Cart.getTotalPrice - should calculate correct total")
    void cart_getTotalPrice_calculatesCorrectly() throws SQLException {
        Cart cart = cartDAO.findByUserIdAndStoreId(TEST_USER_ID, TEST_STORE_ID);
        assertNotNull(cart);

        // (39000 + 6000) * 5 = 225000
        assertEquals(225000, cart.getTotalPrice());
    }

    @Test
    @Order(11)
    @DisplayName("deleteItemById - should delete item and cascade options")
    void deleteItemById_deletesItemAndOptions() throws SQLException {
        cartDAO.deleteItemById(createdItemId);

        CartItem item = cartDAO.findItemById(createdItemId);
        assertNull(item);

        // Verify options also deleted (cascade)
        List<CartItemOption> options = cartDAO.findOptionsByCartItemId(createdItemId);
        assertTrue(options.isEmpty());
    }

    @Test
    @Order(12)
    @DisplayName("deleteAllItemsByCartId - should clear all items")
    void deleteAllItemsByCartId_clearsAllItems() throws SQLException {
        // Add an item first
        CartItem item = new CartItem();
        item.setCartId(createdCartId);
        item.setMenuItemId(TEST_MENU_ITEM_ID);
        item.setQty(1);
        item.setUnitPriceSnapshot(39000);
        item.setOptionsPriceSnapshot(0);
        item.setItemHash("clear_test_hash");
        item.setItemNameSnapshot("Test Item");
        cartDAO.saveItem(item);

        // Clear cart
        cartDAO.deleteAllItemsByCartId(createdCartId);

        List<CartItem> items = cartDAO.findItemsByCartId(createdCartId);
        assertTrue(items.isEmpty());
    }

    @Test
    @Order(13)
    @DisplayName("deleteById - should delete cart and cascade items")
    void deleteById_deletesCartAndItems() throws SQLException {
        // Add an item first
        CartItem item = new CartItem();
        item.setCartId(createdCartId);
        item.setMenuItemId(TEST_MENU_ITEM_ID);
        item.setQty(1);
        item.setUnitPriceSnapshot(39000);
        item.setOptionsPriceSnapshot(0);
        item.setItemHash("delete_cart_test");
        item.setItemNameSnapshot("Test Item");
        cartDAO.saveItem(item);

        // Delete cart
        cartDAO.deleteById(createdCartId);

        Cart cart = cartDAO.findByUserIdAndStoreId(TEST_USER_ID, TEST_STORE_ID);
        assertNull(cart);
    }
}
