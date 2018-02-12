package com.nijus.alino.bfwcoopmanagement.farmers.ui.fragment.dummy;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyCont {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<>();

    public static final List<DummyPurchase> ITEMS_PURCHASE = new ArrayList<>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(), createDummyPurchase());
        }
    }

    private static void addItem(DummyItem item, DummyPurchase purchase) {
        ITEMS.add(item);
        ITEMS_PURCHASE.add(purchase);
    }

    private static DummyItem createDummyItem() {
        return new DummyItem("John Peter", "+250786525059", "1ha");
    }

    private static DummyPurchase createDummyPurchase() {
        return new DummyPurchase();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final String content;
        public final String details;

        public DummyItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }

    public static class DummyPurchase {

    }
}
