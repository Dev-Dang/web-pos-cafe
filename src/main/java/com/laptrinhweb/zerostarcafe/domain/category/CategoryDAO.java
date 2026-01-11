package com.laptrinhweb.zerostarcafe.domain.category;

import java.sql.SQLException;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 *
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * ... code here
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 02/12/2025
 * @since 1.0.0
 */
public interface CategoryDAO {

    /**
     * Returns all active categories ordered by order_index ASC.
     */
    List<Category> findAllByIsActiveTrueOrderByOrderIndexAsc() throws SQLException;

}