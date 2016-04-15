package com.example.amir.koolak;

import java.util.ArrayList;

/**
 * Created by amir on 4/14/2016.
 */
public class Item {

    private String pName;
    private int id;
    public boolean clicked;
    private ArrayList<SubCategory> mSubCategoryList;

    public Item(String pName, int id, ArrayList<SubCategory> mSubCategoryList) {
        super();
        this.pName = pName;
        this.id = id;
        this.clicked = false;
        this.mSubCategoryList = mSubCategoryList;
    }

    public boolean hasChild() {
        return mSubCategoryList.size() > 0;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public ArrayList<SubCategory> getmSubCategoryList() {
        return mSubCategoryList;
    }

    public void setmSubCategoryList(ArrayList<SubCategory> mSubCategoryList) {
        this.mSubCategoryList = mSubCategoryList;
    }

    public int getId() {
        return id;
    }

    /**
     * second level item
     */

    public static class SubCategory {

        private String pSubCatName;
        private int id;
        public boolean clicked;
        private ArrayList<ItemList> mItemListArray;

        public SubCategory(String pSubCatName, int id,
                           ArrayList<ItemList> mItemListArray) {
            super();
            this.pSubCatName = pSubCatName;
            this.id = id;
            this.clicked = false;
            this.mItemListArray = mItemListArray;
        }

        public boolean hasChild() {
            return mItemListArray.size() > 0;
        }

        public String getpSubCatName() {
            return pSubCatName;
        }

        public void setpSubCatName(String pSubCatName) {
            this.pSubCatName = pSubCatName;
        }

        public ArrayList<ItemList> getmItemListArray() {
            return mItemListArray;
        }

        public void setmItemListArray(ArrayList<ItemList> mItemListArray) {
            this.mItemListArray = mItemListArray;
        }

        public int getId() {
            return id;
        }

        /**
         * third level item
         */
        public static class ItemList {

            private String itemName;
            private int id;

            public ItemList(String itemName, int id) {
                super();
                this.itemName = itemName;
                this.id = id;
            }

            public String getItemName() {
                return itemName;
            }

            public void setItemName(String itemName) {
                this.itemName = itemName;
            }

            public int getId() {
                return id;
            }

        }

    }

}

