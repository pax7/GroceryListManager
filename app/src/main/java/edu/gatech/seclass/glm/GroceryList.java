package edu.gatech.seclass.glm;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A class for managing items in a grocery list.  You can add, remove, and change quantities of
 * these items
 */
class GroceryList
{

    /** DB table name */
    static final String TABLE_NAME = "GroceryList";
    /** DB ID column */
    static final Pair<Integer, String> COLUMN_Id = new Pair<>(0, "Id");
    /** DB name column */
    static final Pair<Integer, String> COLUMN_Name = new Pair<>(1, "Name");

    private static GroceryList __current;

    /**
     * Returns the active GroceryList.
     *
     * @return the active GroceryList
     */
    static GroceryList getCurrent() {return __current;}

    /**
     * Sets this GroceryList to active and imports its items from the DB.
     */
    void setCurrent()
    {
        __current = this;
    }


    private final long _id;
    private String _name;

    /** The grocery lists. */
    final List<ListItem> listItemList;

    /**
     * GroceryList constructor. This should normally only be used for populating from DB.
     * @param id DB id
     * @param name name of the GroceryList
     */
    GroceryList(long id, String name) throws IllegalArgumentException
    {
        _id = id;
        _name = name;
        listItemList=new ArrayList<>();

        String whereClause = ListItem.COLUMN_GroceryListId.second+"= ?";
        String[] whereArgs = new String[] {""+_id};
        SQLiteDatabase dbR = DB.getReadableDB();
        Cursor cursor=dbR.query (ListItem.TABLE_NAME,null,whereClause,whereArgs,null,null,null);
        if(cursor.moveToFirst())
        {
            while (!cursor.isAfterLast())
            {
                long listItem_id = cursor.getLong(ListItem.COLUMN_Id.first);
                long item_id = cursor.getLong(ListItem.COLUMN_ItemId.first);
                int quantity = cursor.getInt(ListItem.COLUMN_Quantity.first);
                boolean checked = cursor.getLong(ListItem.COLUMN_Checked.first)==1;
                listItemList.add(new ListItem(listItem_id, item_id, quantity, checked));
                cursor.moveToNext();
            }
        }
        cursor.close();
        dbR.close();
        orderListItems();
    }

    /**
     * The main constructor for GroceryList.
     * @param name name of list
     */
    GroceryList(String name) throws IllegalArgumentException
    {
        if(name==null) {
            throw new IllegalArgumentException();
        }
        if(name.equals("")) {name = "List";}

        SQLiteDatabase dbW = DB.getWritableDB();
        ContentValues values = new ContentValues();
        values.put(GroceryList.COLUMN_Name.second, name);
        _id = dbW.insert(GroceryList.TABLE_NAME, null, values);
        _name = name;
        dbW.close();
        listItemList=new ArrayList<>();
    }


    /**
     * Gets DB id.
     * @return DB id
     */
    long getId() {return _id;}

    /**
     * Sets the name of the GroceryList.
     * @param name name
     */
    void setName(String name) throws IllegalArgumentException
    {
        if(name==null) {
            throw new IllegalArgumentException();
        }
        if(name.equals("")) { name = "List";}

        SQLiteDatabase dbW = DB.getWritableDB();
        String where = COLUMN_Id.second+"=" + _id;
        ContentValues values = new ContentValues();
        values.put(COLUMN_Name.second, name);
        dbW.update(TABLE_NAME, values, where, null);
        dbW.close();

        _name = name;
        try{ListItemAdapter.refresh();}
        catch(Exception e){}
    }

    /**
     * Gets the name of the GroceryList.
     * @return name
     */
    String getName()
    {
        return _name;
    }

    private void orderListItems()
    {
        Collections.sort(listItemList, new Comparator<ListItem>()
        {
            @Override
            public int compare(ListItem o1, ListItem o2)
            {
                int d = o1.getItem().getItemType().getName().compareTo(o2.getItem().getItemType().getName());
                if (d != 0) return d;
                else return o1.getItem().getName().compareTo(o2.getItem().getName());
            }
        });
    }

    /**
     * Adds an item to the list with default quantity of 1
     * @param item item to be added
     */
    void addListItem(Item item)
    {
        setCurrent();
        ListItem li=new ListItem(item);
        listItemList.add(li);
        orderListItems();
        try{ListItemAdapter.refresh();}
        catch (Exception e){}
    }

    /**
     * Deletes the current list item.
     */
    void deleteListItem(ListItem listItem) throws IllegalArgumentException
    {
        if(listItem==null) {throw new IllegalArgumentException();}
        SQLiteDatabase dbW = DB.getWritableDB();
        dbW.delete(ListItem.TABLE_NAME, ListItem.COLUMN_Id.second + "=" + listItem.getId(), null);
        dbW.close();
        listItemList.remove(listItem);
        try{ListItemAdapter.refresh();}
        catch (Exception e){}
    }



    /**
     * If any list items are checked then uncheck all.  If no list items are check then check all.
     */
    void toggleChecked()
    {
        boolean anyChecked=false;
        for (ListItem listItem : listItemList)
            if(listItem.isChecked())
            {
                anyChecked=true;
                break;
            }
        for (ListItem listItem : listItemList)
        {
            listItem.check(!anyChecked);
        }
    }

    /**
     * Remove any list items that are checked.
     */
    void removeChecked()
    {
        ListItem[] listItemArray = listItemList.toArray(new ListItem[listItemList.size()]);
        for (ListItem listItem : listItemArray)
        {
            if (listItem.isChecked())
            {
                deleteListItem(listItem);
            }
        }
    }



    /**
     * Gets the list items
     * @return list items
     */
    ListItem[] getListItems()
    {
        if (listItemList == null || listItemList.size() == 0) return new ListItem[0];
        return listItemList.toArray(new ListItem[listItemList.size()]);
    }

    /**
     * Checks the list for an item.
     * @param item item to check for
     * @return true if the item is in the list
     */
    boolean hasItem(Item item)
    {
        for (ListItem li : listItemList)
        {
            if (li.getItem().getId() == item.getId())
            {
                return true;
            }
        }
        return false;
    }


}