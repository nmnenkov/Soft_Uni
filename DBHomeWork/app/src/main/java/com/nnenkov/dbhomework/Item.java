package com.nnenkov.dbhomework;

/**
 * Created by nik on 30.09.16.
 */

public class Item {
    int id;
    String itemName;

    public Item(int id, String itemName) {
        this.id = id;
        this.itemName = itemName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
