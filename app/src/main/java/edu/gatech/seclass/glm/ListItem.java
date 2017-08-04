package edu.gatech.seclass.glm;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

/**
 * Class for items in a list.  You can set quantity and check them off.
 */
class ListItem{

    /** DB table name */
    static final String TABLE_NAME = "ListItem";
    /** DB id */
    static final Pair<Integer, String> COLUMN_Id = new Pair<>(0, "Id");
    /** DB grocery list id */
    static final Pair<Integer, String> COLUMN_GroceryListId = new Pair<>(1, "GroceryListId");
    /** DB item id */
    static final Pair<Integer, String> COLUMN_ItemId = new Pair<>(2, "ItemId");
    /** DB quantity */
    static final Pair<Integer, String> COLUMN_Quantity = new Pair<>(3, "Quantity");
    /** DB checked */
    static final Pair<Integer, String> COLUMN_Checked = new Pair<>(4, "Checked");


    private int _quantity = 1;
    private long _id=0;
    private final Item _item;
    private boolean _checked = false;
    private boolean _new=false;

    /**
     * Constructor for a list item. Quantity is set to 1 and is left unchecked.
     * @param item item
     */
    ListItem(Item item)
    {
        if(item==null)throw new IllegalArgumentException();
        _item=item;
        _new=true;
        SQLiteDatabase dbW = DB.getWritableDB();
        ContentValues values = new ContentValues();
        long groceryListId = 0;
        if (GroceryList.getCurrent() != null) groceryListId = GroceryList.getCurrent().getId();
        values.put(COLUMN_GroceryListId.second, groceryListId);
        values.put(COLUMN_ItemId.second, item.getId());
        values.put(COLUMN_Quantity.second, 1);
        values.put(COLUMN_Checked.second, 0);
        _id = dbW.insert(TABLE_NAME, null, values);
        dbW.close();

    }

    /**
     * Needed only for DB repopulation
     * @param p_id
     * @param p_item_id
     * @param p_quantity
     * @param p_checked
     */
    ListItem(long p_id, long p_item_id, int p_quantity, boolean p_checked) {
        _item=ItemCatalogue.getItemById(p_item_id);
        if(_item == null || p_quantity < 0)
        {
            throw new IllegalArgumentException();
        }
        _id = p_id;
        _quantity = p_quantity;
        _checked = p_checked;
    }

    /**
     * Sets the quantity of the current list item.
     * @param quantity quantity of the item
     */
    void setQuantity(int quantity)
    {
        if(quantity < 1) {
            throw new IllegalArgumentException();
        }
        _quantity = quantity;
         SQLiteDatabase dbW = DB.getWritableDB();
        String where = COLUMN_Id.second + "=" + _id;
        ContentValues values = new ContentValues();
        values.put(COLUMN_Quantity.second, quantity);
        dbW.update(TABLE_NAME, values, where, null);
        try{ListItemAdapter.refresh();}
        catch(Exception e){}
        _new=false;
    }

    /**
     * Checks the current list item.
     * @param checked true to check
     */
    void check(boolean checked)
    {
        _checked=checked;
        SQLiteDatabase dbW = DB.getWritableDB();
        String where = COLUMN_Id.second+"=" + _id;
        ContentValues values = new ContentValues();
        values.put(COLUMN_Checked.second, checked);
        dbW.update(TABLE_NAME, values, where, null);
        try{ListItemAdapter.refresh();}
        catch(Exception e){}
        _new=false;



    }

    /**
     * Checks off an item.
     */
    @SuppressWarnings("unused")
    void check()
    {
        check(true);
    }


    /**
     * Gets id of item.
     * @return id
     */
    public long getId(){
        return _id;
    }

    /**
     * Gets the item.
     * @return item
     */
    public Item getItem(){return _item;}

    /**
     * Gets the quantity.
     * @return quantity
     */
    int getQuantity() { return _quantity; }

    /**
     * Checks if the list item is checked.
     * @return true if the list item is checked
     */
    boolean isChecked() {
        return _checked;
    }

    /**
     * Checks to see if the item has been newly added.
     * @return true if the item is new.
     */
    boolean isNew(){return _new;}

    /**
     * Removes the new status of the item.
     */
    void touch(){_new=false;}
}
