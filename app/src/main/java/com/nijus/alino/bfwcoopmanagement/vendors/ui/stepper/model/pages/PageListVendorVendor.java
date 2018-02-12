package com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages;

import java.util.ArrayList;

/**
 * Represents a list of wizard pages.
 */
public class PageListVendorVendor extends ArrayList<PageVendorVendor> implements PageTreeNodeVendor {

    public PageListVendorVendor() {

    }

    public PageListVendorVendor(PageVendorVendor... pageVendors) {
        for (PageVendorVendor pageVendor : pageVendors) {
            add(pageVendor);
        }
    }

    @Override
    public PageVendorVendor findByKey(String key) {
        for (PageVendorVendor childPageVendor : this) {
            PageVendorVendor found = childPageVendor.findByKey(key);
            if (found != null) {
                return found;
            }
        }

        return null;
    }

    @Override
    public void flattenCurrentPageSequence(ArrayList<PageVendorVendor> dest) {
        for (PageVendorVendor childPageVendor : this) {
            childPageVendor.flattenCurrentPageSequence(dest);
        }
    }
}