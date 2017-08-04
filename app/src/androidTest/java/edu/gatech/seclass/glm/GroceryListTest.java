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

import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class GroceryListTest {

    @Before
    public void before() {
        App.setContext(InstrumentationRegistry.getTargetContext());
        DB.resetDataBase();
        ItemCatalogue.addItem("apple",ItemType.getItemType(0));
        ItemCatalogue.addItem("orange",ItemType.getItemType(1));
        ItemCatalogue.addItem("banana",ItemType.getItemType(2));


    }

    @After
    public void after() {
        DB.resetDataBase();
        App.setContext(InstrumentationRegistry.getContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNull() {
        GroceryList gl = new GroceryList(null);
    }

    @Test
    public void createAverage() {
        GroceryList gl = new GroceryList("List Name");
    }

    @Test
    public void createBlank() {
        GroceryList gl = new GroceryList("");
        Assert.assertEquals("List", gl.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void renameNull() {
        GroceryList gl = new GroceryList("List Name");
        gl.setName(null);
    }

    @Test
    public void renameAverage() {
        GroceryList gl = new GroceryList("List Name");
        gl.setName("Renamed");
        Assert.assertEquals("Renamed", gl.getName());
    }

    @Test
    public void renameEmpty() {
        GroceryList gl = new GroceryList("List Name");
        gl.setName("");
        Assert.assertEquals("List", gl.getName());
    }

    @Test
    public void getName() {
        GroceryList gl = new GroceryList("List Name");
        Assert.assertEquals("List Name", gl.getName());
    }

    @Test
    public void getListItems() {
        GroceryList gl = new GroceryList("List Name");
        Item i = new Item(1, "Test", ItemType.getItemType(1));
        gl.addListItem(i);
        ListItem[] items = gl.getListItems();
        Assert.assertEquals(1, items.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addNullListItem() {
        GroceryList gl = new GroceryList("List Name");
        Item i = null;
        gl.addListItem(i);
    }

    @Test
    public void deleteListItem() {
        GroceryList gl = new GroceryList("List Name");
        Item i = new Item(1, "Test", ItemType.getItemType(1));
        gl.addListItem(i);
        ListItem li = gl.getListItems()[0];
        gl.deleteListItem(li);
        Assert.assertEquals(gl.getListItems().length,0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteListItemNull() {
        GroceryList gl = new GroceryList("List Name");
        gl.deleteListItem(null);
    }

    @Test
    public void deleteListItemInvalid() {
        GroceryList gl = new GroceryList("List Name");
        Item i = new Item(1, "Test", ItemType.getItemType(1));
        gl.addListItem(i);
        ListItem li = new ListItem(1, 1, 1, true);
        gl.deleteListItem(li);
        Assert.assertEquals(gl.getListItems().length,1);
    }

    @Test
    public void resetChecked() {
        GroceryList gl = new GroceryList("List Name");
        Item a = new Item(1, "Test", ItemType.getItemType(1));
        Item b = new Item(2, "Test2", ItemType.getItemType(2));
        gl.addListItem(a);
        gl.addListItem(b);
        ListItem[] items = gl.getListItems();
        items[0].check();
        items[1].check();
        gl.toggleChecked();

        Assert.assertEquals(items[0].isChecked(),false);
        Assert.assertEquals(items[1].isChecked(), false);
    }

    @Test
    public void removeChecked() {
        GroceryList gl = new GroceryList("List Name");
        Item a = new Item(1, "Test", ItemType.getItemType(1));
        Item b = new Item(2, "Test2", ItemType.getItemType(2));
        gl.addListItem(a);
        gl.addListItem(b);
        ListItem[] items = gl.getListItems();
        items[0].check();
        items[1].check();
        gl.removeChecked();

        Assert.assertEquals(gl.getListItems().length, 0);
    }


}
