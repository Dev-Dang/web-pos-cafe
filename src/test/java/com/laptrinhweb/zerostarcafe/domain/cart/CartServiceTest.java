package com.laptrinhweb.zerostarcafe.domain.cart;

import com.laptrinhweb.zerostarcafe.domain.cart.model.Cart;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItem;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItemOption;
import com.laptrinhweb.zerostarcafe.domain.cart.service.CartService;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for CartService.
 * 
 * NOTE: These tests require the application's JNDI DataSource to be available.
 * Run these tests from within the application server context (e.g., via servlet)
 * or configure a test DataSource.
 * 
 * Alternative: Use CartDAOTest with direct JDBC for database testing.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Disabled("Requires JNDI DataSource - run manually in server context")
class CartServiceTest {

    private static CartService cartService;
    
    // Test data
    private static final long TEST_USER_ID = 11L;  // Another existing user
    private static final long TEST_STORE_ID = 1L;  // Existing store
    private static final long TEST_MENU_ITEM_ID = 10L; // A-Mê Đào
    
    private static long createdItemId;

    @BeforeAll
    static void setUp() {
        cartService = new CartService();
        
        // Clear any existing cart for test user
        Cart existing = cartService.getCart(TEST_USER_ID, TEST_STORE_ID);
        if (existing != null) {
            cartService.clearCart(TEST_USER_ID, TEST_STORE_ID);
        }
    }

    @AfterAll
    static void tearDown() {
        // Clean up test data
        if (cartService != null) {
            cartService.clearCart(TEST_USER_ID, TEST_STORE_ID);
        }
    }

    @Test
    @Order(1)
    @DisplayName("getOrCreateCart - should create new cart")
    void getOrCreateCart_createsNewCart() {
        Cart cart = cartService.getOrCreateCart(TEST_USER_ID, TEST_STORE_ID);
        
        assertNotNull(cart);
        assertTrue(cart.getId() > 0);
        assertEquals(TEST_USER_ID, cart.getUserId());
        assertEquals(TEST_STORE_ID, cart.getStoreId());
    }

    @Test
    @Order(2)
    @DisplayName("getCart - should return existing cart")
    void getCart_returnsExisting() {
        Cart cart = cartService.getCart(TEST_USER_ID, TEST_STORE_ID);
        
        assertNotNull(cart);
        assertEquals(TEST_USER_ID, cart.getUserId());
    }

    @Test
    @Order(3)
    @DisplayName("addItem - should add item to cart")
    void addItem_addsItem() {
        CartItem item = createTestItem();
        
        Cart cart = cartService.addItem(TEST_USER_ID, TEST_STORE_ID, item);
        
        assertNotNull(cart);
        assertEquals(1, cart.getItems().size());
        
        CartItem addedItem = cart.getItems().get(0);
        assertEquals(TEST_MENU_ITEM_ID, addedItem.getMenuItemId());
        assertEquals(2, addedItem.getQty());
        assertEquals("A-Mê Đào", addedItem.getItemNameSnapshot());
        assertNotNull(addedItem.getItemHash());
        assertFalse(addedItem.getItemHash().isEmpty());
        
        createdItemId = addedItem.getId();
    }

    @Test
    @Order(4)
    @DisplayName("addItem - should merge duplicate items by hash")
    void addItem_mergesDuplicates() {
        // Add same item again (same menuItemId, options, note)
        CartItem item = createTestItem();
        
        Cart cart = cartService.addItem(TEST_USER_ID, TEST_STORE_ID, item);
        
        assertNotNull(cart);
        // Should still be 1 item, but qty increased to 4
        assertEquals(1, cart.getItems().size());
        assertEquals(4, cart.getItems().get(0).getQty());
    }

    @Test
    @Order(5)
    @DisplayName("addItem - should create separate item for different options")
    void addItem_separateItemForDifferentOptions() {
        CartItem item = new CartItem();
        item.setMenuItemId(TEST_MENU_ITEM_ID);
        item.setQty(1);
        item.setUnitPriceSnapshot(49000);
        item.setItemNameSnapshot("A-Mê Đào");
        item.setNote("Ít đường"); // Same note
        
        // Different option (Size XL instead of L)
        List<CartItemOption> options = new ArrayList<>();
        CartItemOption option = new CartItemOption();
        option.setOptionValueId(17L); // Size XL
        option.setOptionGroupNameSnapshot("Size");
        option.setOptionValueNameSnapshot("XL");
        option.setPriceDeltaSnapshot(10000);
        options.add(option);
        item.setOptions(options);
        
        Cart cart = cartService.addItem(TEST_USER_ID, TEST_STORE_ID, item);
        
        assertNotNull(cart);
        // Now should have 2 items
        assertEquals(2, cart.getItems().size());
    }

    @Test
    @Order(6)
    @DisplayName("updateItemQty - should update quantity")
    void updateItemQty_updatesQuantity() {
        Cart cart = cartService.updateItemQty(TEST_USER_ID, TEST_STORE_ID, createdItemId, 10);
        
        assertNotNull(cart);
        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId() == createdItemId)
                .findFirst()
                .orElse(null);
        
        assertNotNull(item);
        assertEquals(10, item.getQty());
    }

    @Test
    @Order(7)
    @DisplayName("Cart.getTotalPrice - should calculate correct total")
    void cart_getTotalPrice_calculatesCorrectly() {
        Cart cart = cartService.getCart(TEST_USER_ID, TEST_STORE_ID);
        
        assertNotNull(cart);
        // Item 1: (49000 + 6000) * 10 = 550000
        // Item 2: (49000 + 10000) * 1 = 59000
        // Total: 609000
        assertEquals(609000, cart.getTotalPrice());
    }

    @Test
    @Order(8)
    @DisplayName("Cart.getTotalQty - should calculate total quantity")
    void cart_getTotalQty_calculatesCorrectly() {
        Cart cart = cartService.getCart(TEST_USER_ID, TEST_STORE_ID);
        
        assertNotNull(cart);
        assertEquals(11, cart.getTotalQty()); // 10 + 1
    }

    @Test
    @Order(9)
    @DisplayName("removeItem - should remove item")
    void removeItem_removesItem() {
        Cart cart = cartService.removeItem(TEST_USER_ID, TEST_STORE_ID, createdItemId);
        
        assertNotNull(cart);
        // Should have 1 item left
        assertEquals(1, cart.getItems().size());
        
        // The removed item should not exist
        boolean itemExists = cart.getItems().stream()
                .anyMatch(i -> i.getId() == createdItemId);
        assertFalse(itemExists);
    }

    @Test
    @Order(10)
    @DisplayName("clearCart - should clear all items")
    void clearCart_clearsAllItems() {
        Cart cart = cartService.clearCart(TEST_USER_ID, TEST_STORE_ID);
        
        assertNotNull(cart);
        assertTrue(cart.getItems().isEmpty());
        assertEquals(0, cart.getTotalPrice());
        assertEquals(0, cart.getTotalQty());
    }

    @Test
    @Order(11)
    @DisplayName("mergeGuestCart - should merge guest items")
    void mergeGuestCart_mergesItems() {
        // Add an item to the server cart first
        CartItem serverItem = createTestItem();
        serverItem.setQty(1);
        cartService.addItem(TEST_USER_ID, TEST_STORE_ID, serverItem);
        
        // Create guest items
        List<CartItem> guestItems = new ArrayList<>();
        
        // Same item as server (should merge quantities)
        CartItem guestItem1 = createTestItem();
        guestItem1.setQty(3);
        guestItems.add(guestItem1);
        
        // Different item (should be added)
        CartItem guestItem2 = new CartItem();
        guestItem2.setMenuItemId(11L); // Different product
        guestItem2.setQty(2);
        guestItem2.setUnitPriceSnapshot(49000);
        guestItem2.setItemNameSnapshot("A-Mê Mơ");
        guestItem2.setOptions(new ArrayList<>());
        guestItems.add(guestItem2);
        
        Cart cart = cartService.mergeGuestCart(TEST_USER_ID, TEST_STORE_ID, guestItems);
        
        assertNotNull(cart);
        assertEquals(2, cart.getItems().size());
        
        // First item qty should be 1 + 3 = 4
        CartItem mergedItem = cart.getItems().stream()
                .filter(i -> i.getMenuItemId() == TEST_MENU_ITEM_ID)
                .findFirst()
                .orElse(null);
        assertNotNull(mergedItem);
        assertEquals(4, mergedItem.getQty());
    }

    @Test
    @Order(12)
    @DisplayName("generateItemHash - should generate consistent hash")
    void generateItemHash_isConsistent() {
        List<CartItemOption> options = new ArrayList<>();
        CartItemOption opt1 = new CartItemOption();
        opt1.setOptionValueId(16L);
        options.add(opt1);
        
        CartItemOption opt2 = new CartItemOption();
        opt2.setOptionValueId(20L);
        options.add(opt2);
        
        String hash1 = cartService.generateItemHash(10L, options, "test note");
        String hash2 = cartService.generateItemHash(10L, options, "test note");
        
        assertEquals(hash1, hash2);
        assertEquals(64, hash1.length()); // SHA-256 = 64 hex chars
    }

    @Test
    @Order(13)
    @DisplayName("generateItemHash - should differ for different inputs")
    void generateItemHash_differsForDifferentInputs() {
        List<CartItemOption> options = new ArrayList<>();
        CartItemOption opt = new CartItemOption();
        opt.setOptionValueId(16L);
        options.add(opt);
        
        String hash1 = cartService.generateItemHash(10L, options, "note1");
        String hash2 = cartService.generateItemHash(10L, options, "note2");
        String hash3 = cartService.generateItemHash(11L, options, "note1");
        
        assertNotEquals(hash1, hash2);
        assertNotEquals(hash1, hash3);
    }

    // Helper to create a test item
    private CartItem createTestItem() {
        CartItem item = new CartItem();
        item.setMenuItemId(TEST_MENU_ITEM_ID);
        item.setQty(2);
        item.setUnitPriceSnapshot(49000);
        item.setItemNameSnapshot("A-Mê Đào");
        item.setNote("Ít đường");
        
        List<CartItemOption> options = new ArrayList<>();
        CartItemOption option = new CartItemOption();
        option.setOptionValueId(16L); // Size L
        option.setOptionGroupNameSnapshot("Size");
        option.setOptionValueNameSnapshot("L");
        option.setPriceDeltaSnapshot(6000);
        options.add(option);
        item.setOptions(options);
        
        return item;
    }
}
