package edu.gatech.seclass.glm;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import java.util.ArrayList;

/**
 * A type for grocery list items.
 */
class ItemType {

    private static final String TABLE_NAME = "ItemType";
    private static final Pair<Integer, String> COLUMN_Id = new Pair<>(0, "Id");
    private static final Pair<Integer, String> COLUMN_Name = new Pair<>(1, "Name");

    /**
     * The list of all types.
     */
    static final ArrayList<ItemType> itemTypeList= new ArrayList<>();

    static {init();}

    private final long _id;
    private final String _name;

    private ItemType(long p_id, String p_name)
    {
        _id = p_id;
        _name = p_name;
    }

    private static void init()
    {

        SQLiteDatabase dbR = DB.getReadableDB();
        Cursor cursor = dbR.rawQuery("SELECT  * FROM " + ItemType.TABLE_NAME, null);
        if (cursor.moveToFirst())
        {
            while (!cursor.isAfterLast())
            {
                long _id = cursor.getLong(ItemType.COLUMN_Id.first);
                String _name = cursor.getString(ItemType.COLUMN_Name.first);
                itemTypeList.add(new ItemType(_id, _name));
                cursor.moveToNext();
            }
        }
        cursor.close();
        dbR.close();

    }

    /**
     * Gets the DB id.
     * @return the DB id.
     */
    long getId(){return _id;}

    /**
     * Gets the name of the type.
     * @return name
     */
    String getName(){return _name;}

    /**
     * Gets an itemtype by its DB id.
     * @param id DB id
     * @return type
     */
    static ItemType getItemType(long id) { return itemTypeList.get((int)id);}

}
