package com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages;

import java.util.ArrayList;

/**
 * Represents a node in the page tree. Can either be a single page, or a page container.
 */
public interface PageTreeNodeVendor {
    PageVendorVendor findByKey(String key);

    void flattenCurrentPageSequence(ArrayList<PageVendorVendor> dest);
}
