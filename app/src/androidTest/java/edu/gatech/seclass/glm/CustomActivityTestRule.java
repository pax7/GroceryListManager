package edu.gatech.seclass.glm;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

class CustomActivityTestRule<E> extends ActivityTestRule
{
    public CustomActivityTestRule(Class<E> c)
    {
        super(c);
    }

    @Override
    protected void beforeActivityLaunched() {
        super.beforeActivityLaunched();
        App.setContext(InstrumentationRegistry.getTargetContext());
        DB.loadTestDataBase();
        ItemCatalogue.init();
        GroceryListManager.init();
    }
}