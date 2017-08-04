package edu.gatech.seclass.glm;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/** A list of grocery lists. */
class GroceryListManager {

    /** the list of grocery lists */
    static ArrayList<GroceryList> groceryListList= new ArrayList<>();
    static{ init(); }

    static void init()
    {
        groceryListList= new ArrayList<>();
        SQLiteDatabase dbR = DB.getReadableDB();
        Cursor cursor=dbR.rawQuery("SELECT  * FROM " + GroceryList.TABLE_NAME, null);
        if(cursor.moveToFirst())
        {
            while (!cursor.isAfterLast())
            {
                long _id = cursor.getLong(GroceryList.COLUMN_Id.first);
                String _name = cursor.getString(GroceryList.COLUMN_Name.first);
                groceryListList.add(new GroceryList(_id, _name));
                cursor.moveToNext();
            }
        }
        cursor.close();
        dbR.close();
    }

    /**
     * Add a new list.
     * @param name name of the list
     */
    static void addGroceryList(String name) {

        GroceryList gl=new GroceryList(name);
        groceryListList.add(gl);
    }

    /**
     * Removes a grocery list.
     * @param groceryList grocery list to remove
     */
    static void removeGroceryList(GroceryList groceryList) throws IllegalArgumentException{
        if(groceryList==null) {throw new IllegalArgumentException();}
        SQLiteDatabase dbW = DB.getWritableDB();
        dbW.delete(GroceryList.TABLE_NAME, GroceryList.COLUMN_Id.second + "=" + groceryList.getId(), null);
        dbW.close();
        groceryListList.remove(groceryList);
        try{GroceryListAdapter.refresh();}
        catch(Exception e){}
    }

    /**
     * Gets an array of all the lists.
     * @return list of lists
     */
    static GroceryList[] getGroceryLists()
    {
        return groceryListList.toArray(new GroceryList[groceryListList.size()]);
    }

}