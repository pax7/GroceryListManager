package edu.gatech.seclass.glm;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;



class Item {
    static final String TABLE_NAME = "Item";
    static final Pair<Integer, String> COLUMN_Id = new Pair<>(0, "Id");
    static final Pair<Integer, String> COLUMN_Name = new Pair<>(1, "Name");
    static final Pair<Integer, String> COLUMN_ItemTypeId = new Pair<>(2, "ItemTypeId");


    private long _id;
    private final String _name;
    private final ItemType _itemType;

    Item(long p_id, String p_name, ItemType p_itemType){
		if(p_name == null || p_itemType == null)
            throw new IllegalArgumentException();
        _id = p_id;
        _name = p_name;
        _itemType = p_itemType;
    }

    Item(String name, ItemType itemType)
    {

        SQLiteDatabase dbW = DB.getWritableDB();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ItemTypeId.second, itemType.getId());
        values.put(COLUMN_Name.second, name);
        _id = dbW.insert(TABLE_NAME, null, values);
        dbW.close();


        _itemType=itemType;
        _name=name;
    }

    long getId(){
        return _id;
    }
    ItemType getItemType(){
        return _itemType;
    }

    String getName(){
        return _name;
    }
    /*
    void setName(String name)
    {
        SQLiteDatabase dbW = DB.getWritableDB();
        String where = COLUMN_Id.second+"=" + _id;
        ContentValues values = new ContentValues();
        values.put(COLUMN_Name.second, name);
        dbW.update(TABLE_NAME, values, where, null);
        dbW.close();

        _name = name;
        ItemAdapter.refresh();
    }*/
}
