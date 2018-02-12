package com.nijus.alino.bfwcoopmanagement.vendors.ui.fragment.dummy;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContents {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<>();
    public static final List<DummyItemContract> ITEM_CONTRACTS = new ArrayList<>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(), createDummyItemContract());
        }
    }

    private static void addItem(DummyItem item, DummyItemContract contract) {
        ITEMS.add(item);
        ITEM_CONTRACTS.add(contract);
    }

    private static DummyItem createDummyItem() {
        return new DummyItem("John Peter", "+250786525059", "1ha");
    }

    private static DummyItemContract createDummyItemContract() {
        return new DummyItemContract("2017A", "RGCC");
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

    public static class DummyItemContract {
        public final String season;
        public final String customer;

        public DummyItemContract(String content, String details) {
            this.season = content;
            this.customer = details;
        }

        @Override
        public String toString() {
            return season;
        }
    }
}
