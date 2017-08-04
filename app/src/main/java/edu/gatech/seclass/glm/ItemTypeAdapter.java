package edu.gatech.seclass.glm;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Toast;

/**
 * Adapter to drive the gridview containing item types.
 */
class ItemTypeAdapter extends ArrayAdapter<ItemType> {

    private static boolean hasExplained=false;

    /**
     * The constructor that need to be called with the containing context.
     * @param context
     */
    ItemTypeAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1, ItemType.itemTypeList);
    }

    /**
     * Called when each element in the gridview is populated.
     * @param p_position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    @NonNull
    public View getView(int p_position, View convertView, @NonNull ViewGroup parent)
    {

        String itemTypeName = ItemType.getItemType(p_position).getName();
        final int itemTypeId=p_position;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_type_item, parent, false);
        }

        RadioButton typeButton = (RadioButton) convertView.findViewById(R.id._name);

        typeButton.setText(itemTypeName);
        typeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ItemCatalogue.addItem(ItemCatalogue.getLastSearch(),ItemType.getItemType(itemTypeId));
                }
                catch(Exception e) {
                    Toast.makeText(v.getContext().getApplicationContext(),"There was an error adding the item.",Toast.LENGTH_LONG).show();
                    hasExplained=true;
                }
                ItemTypeView.close();
                ItemCatalogueView.update();
                if(!hasExplained)
                {
                    Toast.makeText(v.getContext().getApplicationContext(),"Now you can add your item by checking it and tapping add.",Toast.LENGTH_LONG).show();
                    hasExplained=true;
                }
            }
        });

        return convertView;
    }
}