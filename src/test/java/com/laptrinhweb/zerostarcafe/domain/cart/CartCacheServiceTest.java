package com.laptrinhweb.zerostarcafe.domain.cart;

import com.laptrinhweb.zerostarcafe.domain.cart.model.Cart;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItem;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItemOption;
import com.laptrinhweb.zerostarcafe.domain.cart.service.CartCacheService;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CartCacheService.
 * 
 * NOTE: These tests run against the cache layer.
 * For DB integration tests, use CartDAOTest.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Disabled("Requires JNDI DataSource - run manually in server context")
class CartCacheServiceTest {

    private static CartCacheService cacheService;
    
    // Test data
    private static final long TEST_USER_ID = 12L;
    private static final long TEST_STORE_ID = 1L;
    private static final long TEST_MENU_ITEM_ID = 10L;

    @BeforeAll
    static void setUp() {
        cacheService = CartCacheService.getInstance();
    }

    @AfterAll
    static void tearDown() {
        if (cacheService != null) {
            cacheService.evictFromCache(TEST_USER_ID, TEST_STORE_ID);
        }
    }

    @Test
    @Order(1)
    @DisplayName("getCart - should return empty cart for new user")
    void getCart_returnsEmptyCart() {
        Cart cart = cacheService.getCart(TEST_USER_ID, TEST_STORE_ID);
        
        assertNotNull(cart);
        assertEquals(TEST_USER_ID, cart.getUserId());
        assertEquals(TEST_STORE_ID, cart.getStoreId());
    }

    @Test
    @Order(2)
    @DisplayName("addItem - should add item to cart")
    void addItem_addsItem() {
        CartItem item = createTestItem(TEST_MENU_ITEM_ID, 2, 49000, "Test Item");
        
        Cart cart = cacheService.addItem(TEST_USER_ID, TEST_STORE_ID, item);
        
        assertNotNull(cart);
        assertEquals(1, cart.getItems().size());
        assertEquals(2, cart.getItems().get(0).getQty());
    }

    @Test
    @Order(3)
    @DisplayName("addItem - should not modify original item object")
    void addItem_doesNotModifyOriginalItem() {
        CartItem item = createTestItem(20L, 1, 35000, "Original Item");
        long originalId = item.getId();
        
        cacheService.addItem(TEST_USER_ID, TEST_STORE_ID, item);
        
        // Original item should not be modified
        assertEquals(originalId, item.getId());
    }

    @Test
    @Order(4)
    @DisplayName("addItem - should merge same item by hash")
    void addItem_mergesByHash() {
        // Clear and add fresh
        cacheService.clearCart(TEST_USER_ID, TEST_STORE_ID);
        
        CartItem item1 = createTestItem(TEST_MENU_ITEM_ID, 2, 49000, "Test Item");
        cacheService.addItem(TEST_USER_ID, TEST_STORE_ID, item1);
        
        // Add same item again (same menuItemId, options, note)
        CartItem item2 = createTestItem(TEST_MENU_ITEM_ID, 3, 49000, "Test Item");
        Cart cart = cacheService.addItem(TEST_USER_ID, TEST_STORE_ID, item2);
        
        assertNotNull(cart);
        assertEquals(1, cart.getItems().size());
        assertEquals(5, cart.getItems().get(0).getQty()); // 2 + 3 = 5
    }

    @Test
    @Order(5)
    @DisplayName("addItem - different options create separate items")
    void addItem_differentOptionsCreatesSeparateItem() {
        CartItem item = createTestItemWithDifferentOption(TEST_MENU_ITEM_ID, 1, 49000, "Test Item 2");
        
        Cart cart = cacheService.addItem(TEST_USER_ID, TEST_STORE_ID, item);
        
        assertNotNull(cart);
        assertEquals(2, cart.getItems().size());
    }

    @Test
    @Order(6)
    @DisplayName("getCart - should return copy, not live reference")
    void getCart_returnsCopy() {
        Cart cart1 = cacheService.getCart(TEST_USER_ID, TEST_STORE_ID);
        Cart cart2 = cacheService.getCart(TEST_USER_ID, TEST_STORE_ID);
        
        // Should be different objects
        assertNotSame(cart1, cart2);
        assertNotSame(cart1.getItems(), cart2.getItems());
    }

    @Test
    @Order(7)
    @DisplayName("updateItemQty - should update quantity")
    void updateItemQty_updatesQuantity() {
        Cart cart = cacheService.getCart(TEST_USER_ID, TEST_STORE_ID);
        long itemId = cart.getItems().get(0).getId();
        
        cart = cacheService.updateItemQty(TEST_USER_ID, TEST_STORE_ID, itemId, 10);
        
        assertNotNull(cart);
        assertEquals(10, cart.getItems().get(0).getQty());
    }

    @Test
    @Order(8)
    @DisplayName("removeItem - should remove item")
    void removeItem_removesItem() {
        Cart cart = cacheService.getCart(TEST_USER_ID, TEST_STORE_ID);
        int initialCount = cart.getItems().size();
        long itemId = cart.getItems().get(0).getId();
        
        cart = cacheService.removeItem(TEST_USER_ID, TEST_STORE_ID, itemId);
        
        assertNotNull(cart);
        assertEquals(initialCount - 1, cart.getItems().size());
    }

    @Test
    @Order(9)
    @DisplayName("clearCart - clears all items")
    void clearCart_clearsAllItems() {
        // Add some items first
        CartItem item = createTestItem(TEST_MENU_ITEM_ID, 1, 49000, "Item to clear");
        cacheService.addItem(TEST_USER_ID, TEST_STORE_ID, item);
        
        Cart cart = cacheService.clearCart(TEST_USER_ID, TEST_STORE_ID);
        
        assertNotNull(cart);
        assertTrue(cart.getItems().isEmpty());
        assertEquals(0, cart.getTotalPrice());
    }

    @Test
    @Order(10)
    @DisplayName("mergeGuestCart - merges guest items without modifying originals")
    void mergeGuestCart_mergesItems() {
        // Add an item to server cart
        cacheService.clearCart(TEST_USER_ID, TEST_STORE_ID);
        CartItem serverItem = createTestItem(TEST_MENU_ITEM_ID, 1, 49000, "Server Item");
        cacheService.addItem(TEST_USER_ID, TEST_STORE_ID, serverItem);
        
        // Create guest items
        List<CartItem> guestItems = new ArrayList<>();
        
        // Same as server (should merge)
        CartItem guestItem1 = createTestItem(TEST_MENU_ITEM_ID, 2, 49000, "Server Item");
        guestItems.add(guestItem1);
        
        // Different (should add)
        CartItem guestItem2 = createTestItem(11L, 1, 39000, "Guest Only");
        guestItems.add(guestItem2);
        
        Cart cart = cacheService.mergeGuestCart(TEST_USER_ID, TEST_STORE_ID, guestItems);
        
        assertNotNull(cart);
        assertEquals(2, cart.getItems().size());
        
        // First item should have qty 1 + 2 = 3
        int totalQty = cart.getTotalQty();
        assertEquals(4, totalQty); // 3 + 1
        
        // Original guest items should not be modified
        assertEquals(0, guestItem1.getCartId());
        assertEquals(0, guestItem2.getCartId());
    }

    @Test
    @Order(11)
    @DisplayName("persistImmediately - persists to DB")
    void persistImmediately_persistsToDb() {
        cacheService.persistImmediately(TEST_USER_ID, TEST_STORE_ID);
        
        // No exception means success
        // Actual verification would need DB check
    }

    @Test
    @Order(12)
    @DisplayName("evictFromCache - removes cart from cache")
    void evictFromCache_removesFromCache() {
        // Add item to ensure cart is in cache
        CartItem item = createTestItem(TEST_MENU_ITEM_ID, 1, 49000, "Item before evict");
        cacheService.addItem(TEST_USER_ID, TEST_STORE_ID, item);
        
        // Evict
        cacheService.evictFromCache(TEST_USER_ID, TEST_STORE_ID);
        
        // Getting cart again should reload from DB (empty or with persisted data)
        Cart cart = cacheService.getCart(TEST_USER_ID, TEST_STORE_ID);
        assertNotNull(cart);
    }

    // ==========================================================
    // HELPERS
    // ==========================================================

    private CartItem createTestItem(long menuItemId, int qty, int price, String name) {
        CartItem item = new CartItem();
        item.setMenuItemId(menuItemId);
        item.setQty(qty);
        item.setUnitPriceSnapshot(price);
        item.setItemNameSnapshot(name);
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

    private CartItem createTestItemWithDifferentOption(long menuItemId, int qty, int price, String name) {
        CartItem item = new CartItem();
        item.setMenuItemId(menuItemId);
        item.setQty(qty);
        item.setUnitPriceSnapshot(price);
        item.setItemNameSnapshot(name);
        item.setNote("Ít đường");
        
        List<CartItemOption> options = new ArrayList<>();
        CartItemOption option = new CartItemOption();
        option.setOptionValueId(17L); // Size XL (different!)
        option.setOptionGroupNameSnapshot("Size");
        option.setOptionValueNameSnapshot("XL");
        option.setPriceDeltaSnapshot(10000);
        options.add(option);
        item.setOptions(options);
        
        return item;
    }
}
