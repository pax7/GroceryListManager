package edu.gatech.seclass.glm;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class GroceryListManagerTest {

    @Test
    public void addGroceryList() {
        GroceryListManager.addGroceryList("Name");
        GroceryList[] lists = GroceryListManager.getGroceryLists();
        Assert.assertEquals(lists[lists.length-1].getName(),"Name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void addGroceryListNull() {
        GroceryListManager.addGroceryList(null);
    }

    @Test
    public void removeGroceryList() {
        GroceryListManager.addGroceryList("Name");
        GroceryList[] lists = GroceryListManager.getGroceryLists();
        GroceryListManager.removeGroceryList(lists[0]);
        GroceryListManager.removeGroceryList(lists[1]);
        Assert.assertEquals(GroceryListManager.getGroceryLists().length, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeGroceryListNull() {
        GroceryListManager.removeGroceryList(null);
    }

    @Test
    public void removeGroceryListInvalid() {
        GroceryList gl = new GroceryList("Name");
        GroceryListManager.removeGroceryList(gl);
        Assert.assertEquals(GroceryListManager.getGroceryLists().length, 2);
    }

    @Test
    public void getGroceryLists() {
        GroceryListManager.addGroceryList("Test");
        GroceryListManager.addGroceryList("Test2");
        Assert.assertEquals(GroceryListManager.getGroceryLists().length, 3);
    }
}
