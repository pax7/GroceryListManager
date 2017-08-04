package edu.gatech.seclass.glm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

/**
 * A view of selecting an item type for a new grocery list item.
 */
public class ItemTypeView extends AppCompatActivity
{

    private static ItemTypeView _instance;

    /**
     * Called on creation.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _instance=this;
        setContentView(R.layout.activity_item_type_view);

        TextView itemName = (TextView) findViewById(R.id._item_name);
        itemName.setText(ItemCatalogue.getLastSearch());

        GridView _itemTypeView = (GridView)findViewById(R.id._itemTypeView);
        _itemTypeView.setAdapter(new ItemTypeAdapter(this));
    }

    @Override
    protected void onResume()
    {
        _instance=this;
        super.onResume();
    }

    static void close()
    {
        _instance.finish();
    }


    @SuppressWarnings("UnusedParameters")
    public void _back_onClick(View view) {

        this.finish();
    }
}