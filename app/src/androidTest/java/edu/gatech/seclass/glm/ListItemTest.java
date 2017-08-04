package edu.gatech.seclass.glm;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;


@RunWith(AndroidJUnit4.class)
@SmallTest
public class ListItemTest
{
    @Test(expected = IllegalArgumentException.class)
    public void nullInputs() {
        new ListItem(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNegativeQuantityCreation() {
        Item i = new Item("Item", ItemType.getItemType(0));
        ListItem l = new ListItem(i);
        l.setQuantity(-5);
    }


    @Test
    public void averageCreation() {
        Item i = new Item("Item", ItemType.getItemType(0));
        new ListItem(i);
    }

    @Test
    public void setQuantityAverage() {
        ItemType t = ItemType.getItemType(0);
        Item i = new Item("Item", t);
        ListItem l = new ListItem(i);
        l.setQuantity(5);
        assertEquals(5, l.getQuantity());
    }

    @Test
    public void isChecked() {
        ItemType t = ItemType.getItemType(0);
        Item i = new Item("Item", t);
        ListItem l = new ListItem(i);
        assertEquals(false,l.isChecked());
    }

    @Test
    public void isCheckedTrueOption() {
        ItemType t = ItemType.getItemType(0);
        Item i = new Item("Item", t);
        ListItem l = new ListItem(i);
        l.check(true);
        assertEquals(true,l.isChecked());
    }

    @Test
    public void getItemType() {
        ItemType t = ItemType.getItemType(0);
        Item i = new Item("Item", t);
        ListItem l = new ListItem(i);
        l.check(true);
        assertEquals(t,l.getItem().getItemType());
    }
}