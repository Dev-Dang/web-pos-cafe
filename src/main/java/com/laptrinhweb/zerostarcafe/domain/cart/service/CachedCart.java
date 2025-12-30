package com.laptrinhweb.zerostarcafe.domain.cart.service;

import com.laptrinhweb.zerostarcafe.domain.cart.model.Cart;

import java.util.concurrent.ScheduledFuture;

/**
 * <h2>Description:</h2>
 * <p>
 * Wrapper holding cart data and metadata for cache management.
 * Tracks modification times, dirty state, and scheduled persist handles.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 30/12/2025
 * @since 1.0.0
 */
public class CachedCart {

    /** The cart data (mutable, modified in-place) */
    private Cart cart;

    /** Timestamp when cart was last modified */
    private long lastModified;

    /** Timestamp when cart was last accessed (read or write) */
    private long lastAccessed;

    /** Timestamp when cart was last persisted to DB */
    private long lastPersisted;

    /** Whether cart has unsaved changes */
    private boolean dirty;

    /** Version number, incremented on each persist */
    private int version;

    /** Handle to the scheduled idle-persist task (for cancellation) */
    private ScheduledFuture<?> scheduledPersistTask;

    // ==========================================================
    // CONSTRUCTOR
    // ==========================================================

    public CachedCart(Cart cart) {
        long now = System.currentTimeMillis();
        this.cart = cart;
        this.lastModified = now;
        this.lastAccessed = now;
        this.lastPersisted = now;
        this.dirty = false;
        this.version = 0;
        this.scheduledPersistTask = null;
    }

    // ==========================================================
    // METHODS
    // ==========================================================

    /**
     * Marks the cart as dirty (has unsaved changes).
     */
    public void markDirty() {
        long now = System.currentTimeMillis();
        this.dirty = true;
        this.lastModified = now;
        this.lastAccessed = now;
    }

    /**
     * Marks the cart as clean (just persisted).
     */
    public void markClean() {
        this.dirty = false;
        this.lastPersisted = System.currentTimeMillis();
        this.version++;
    }

    /**
     * Updates the last accessed time (for eviction tracking).
     */
    public void touch() {
        this.lastAccessed = System.currentTimeMillis();
    }

    /**
     * Cancels any pending scheduled persist task.
     */
    public void cancelScheduledPersist() {
        if (scheduledPersistTask != null) {
            scheduledPersistTask.cancel(false);
            scheduledPersistTask = null;
        }
    }

    /**
     * Checks if cart has been idle long enough for persist.
     *
     * @param idleThresholdMs the idle time threshold in milliseconds
     * @return true if idle time exceeds threshold
     */
    public boolean isIdleForPersist(long idleThresholdMs) {
        long idleTime = System.currentTimeMillis() - lastModified;
        return idleTime >= idleThresholdMs;
    }

    /**
     * Checks if cart has been dirty for too long.
     *
     * @param maxDirtyDurationMs the maximum dirty duration in milliseconds
     * @return true if dirty duration exceeds threshold
     */
    public boolean isDirtyTooLong(long maxDirtyDurationMs) {
        if (!dirty) {
            return false;
        }
        long dirtyDuration = System.currentTimeMillis() - lastPersisted;
        return dirtyDuration > maxDirtyDurationMs;
    }

    /**
     * Checks if cart has been inactive for too long (for eviction).
     *
     * @param inactiveThresholdMs the inactive time threshold in milliseconds
     * @return true if inactive time exceeds threshold
     */
    public boolean isInactiveForEviction(long inactiveThresholdMs) {
        long inactiveTime = System.currentTimeMillis() - lastAccessed;
        return inactiveTime > inactiveThresholdMs;
    }

    // ==========================================================
    // GETTERS AND SETTERS
    // ==========================================================

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public long getLastModified() {
        return lastModified;
    }

    public long getLastAccessed() {
        return lastAccessed;
    }

    public long getLastPersisted() {
        return lastPersisted;
    }

    public boolean isDirty() {
        return dirty;
    }

    public int getVersion() {
        return version;
    }

    public ScheduledFuture<?> getScheduledPersistTask() {
        return scheduledPersistTask;
    }

    public void setScheduledPersistTask(ScheduledFuture<?> scheduledPersistTask) {
        this.scheduledPersistTask = scheduledPersistTask;
    }
}
