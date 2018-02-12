package com.nijus.alino.bfwcoopmanagement.events;

import java.util.ArrayList;
import java.util.List;


public class DataToDelete {
    private int position;
    private List<Integer> listsSelectedItem;

    public DataToDelete() {
    }

    public DataToDelete(int position, List<Integer> listsSelectedItem) {
        this.position = position;
        this.listsSelectedItem =  new ArrayList<>();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<Integer> getListsSelectedItem() {
        return listsSelectedItem;
    }

    public void setListsSelectedItem(List<Integer> listsSelectedItem) {
        this.listsSelectedItem = listsSelectedItem;
    }
}
