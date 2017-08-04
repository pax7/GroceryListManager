package edu.gatech.seclass.glm;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


class ItemCatalogue {


    private static String _lastSearch="";

    static List<Item> itemList=new ArrayList<>();

    static{init();}

    static void init()
    {
        itemList=new ArrayList<>();
        SQLiteDatabase dbR = DB.getReadableDB();
        Cursor cursor=dbR.rawQuery("SELECT  * FROM " + Item.TABLE_NAME, null);
        if(cursor.moveToFirst())
        {
            while (!cursor.isAfterLast())
            {
                long _id = cursor.getLong(Item.COLUMN_Id.first);
                String _name = cursor.getString(Item.COLUMN_Name.first);
                ItemType _type = ItemType.getItemType(cursor.getLong(Item.COLUMN_ItemTypeId.first));
                itemList.add(new Item(_id, _name,_type));
                cursor.moveToNext();
            }
        }
        cursor.close();
        dbR.close();
        orderListItems();
    }

    static void clearAll()
    {
        SQLiteDatabase dbW = DB.getWritableDB();
        dbW.execSQL("delete from "+ Item.TABLE_NAME);
        dbW.close();
        itemList.clear();
    }


    static void addItem(String name, ItemType itemType)
    {
        if(name==null || itemType==null)throw new IllegalArgumentException();
        if(!hasItem(name)) {
            itemList.add(new Item(name, itemType)); //Item creation takes care of DB insert
            orderListItems();
            try {
                ItemAdapter.refresh();
            } catch (Exception e) {
            }
        }
    }

    static void removeItem(Item item)
    {
        SQLiteDatabase dbW = DB.getWritableDB();
        dbW.delete(Item.TABLE_NAME, Item.COLUMN_Id.second + "=" + item.getId(), null);
        dbW.close();
        itemList.remove(item);
    }

    private static void orderListItems()
    {
        Collections.sort(itemList, new Comparator<Item>()
        {
            @Override
            public int compare(Item o1, Item o2)
            {
                int d = o1.getItemType().getName().compareTo(o2.getItemType().getName());
                if (d != 0) return d;
                else return o1.getName().compareTo(o2.getName());
            }
        });
    }


    static Item[] search(String query)
    {
        if(query==null)throw new IllegalArgumentException();
        setSearch(query);
        List<Item> results=new ArrayList<>();
        for(Item i:itemList)
            if(clean(i.getName()).contains(clean(_lastSearch)))
                results.add(i);
        return results.toArray(new Item[results.size()]);

    }

    @SuppressWarnings("unused")
    static Item[] search()
    {
        return search(_lastSearch);

    }

    private static String clean(String str)
    {
        return str.trim().toLowerCase();
    }

    static void setSearch(String str)
    {
        str=str.trim();
        if(str.length()<=0)
        {
            _lastSearch="";
            return;
        }
        str=str.substring(0,1).toUpperCase()+str.substring(1);
        _lastSearch=str;
    }

    static boolean hasItem(String str)
    {
        for(Item i:itemList)
            if(clean(i.getName()).equals(clean(str))) return true;
        return false;
    }

    static Item getItemById(Long id)
    {
        for(Item i:itemList)
            if(i.getId()==id)return i;
        return null;
    }

    static String getLastSearch()
    {
        return _lastSearch;
    }
}