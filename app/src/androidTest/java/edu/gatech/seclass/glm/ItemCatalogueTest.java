package edu.gatech.seclass.glm;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;


@RunWith(AndroidJUnit4.class)
public class ItemCatalogueTest {

    @Test
    public void searchItemAverage()
    {


        Item i=ItemCatalogue.search("ang")[0];
        //The first letter gets capitalized
        Assert.assertEquals(i.getName().toLowerCase(),"orange");
    }

    @Test
    public void searchItemBlank() {
        Assert.assertEquals(ItemCatalogue.search("").length,21);
    }

    @Test(expected = IllegalArgumentException.class)
    public void searchItemNull() {
        ItemCatalogue.search(null);
    }

    @Test
    public void searchItemNoMatches() {
        Assert.assertEquals(ItemCatalogue.search("adsf").length,0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addItemNull() {
        ItemCatalogue.addItem(null,ItemType.getItemType(0));
    }

    @Test
    public void addItemAverage()
    {
        Assert.assertEquals(ItemCatalogue.hasItem("kiwi"),false);
        ItemCatalogue.addItem("kiwi",ItemType.getItemType(2));
        Assert.assertEquals(ItemCatalogue.hasItem("kiwi"),true);

    }

    @Test
    public void addItemAlreadyExists() {
        ItemCatalogue.clearAll();
        ItemCatalogue.addItem("kiwi",ItemType.getItemType(2));
        ItemCatalogue.addItem("kiwi",ItemType.getItemType(2));
        Assert.assertEquals(ItemCatalogue.itemList.size(), 1);
    }

    @Test
    public void getItemById() {
        Long a = Long.valueOf(1);

        Assert.assertEquals("apple", ItemCatalogue.getItemById(a).getName());
    }
}