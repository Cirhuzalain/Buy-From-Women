package com.nijus.alino.bfwcoopmanagement.loans.ui.activities;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains a list of items selected  passed from BROADCASTRECEIVER  to the appbar menu.
 * This class allow us to add the "DELETE ICON" in the appbar when an item from recycleview is onLongCliked.
 * ()
 */

public class SelectedItems {
    private List<Long> listsSelectedItem = new ArrayList<>();
    private String message;
    boolean isSuccess;
    Long selected_item;


    public SelectedItems() {
    }

    public SelectedItems(Long selected_item) {
        this.selected_item = selected_item;
    }

    public SelectedItems(List<Long> listsSelectedItem) {
        this.listsSelectedItem = listsSelectedItem;
    }

    public SelectedItems(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public SelectedItems(List<Long> listsSelectedItem, String message) {
        this.listsSelectedItem = listsSelectedItem;
        this.message = message;
    }

    public SelectedItems(List<Long> listsSelectedItem, String message, boolean isSuccess) {
        this.listsSelectedItem = listsSelectedItem;
        this.message = message;
        this.isSuccess = isSuccess;
    }

    public List<Long> getListsSelectedItem() {
        return listsSelectedItem;
    }

    public void setListsSelectedItem(List<Long> listsSelectedItem) {
        this.listsSelectedItem = listsSelectedItem;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public Long getSelected_item() {
        return selected_item;
    }

    public void setSelected_item(Long selected_item) {
        this.selected_item = selected_item;
    }

    public int getCountSelctedItem()
    {
        int count = 0;
        for(long s : listsSelectedItem )
        {
            count++;
        }
        return count;
    }

}
