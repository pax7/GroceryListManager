package edu.gatech.seclass.glm;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Adapter used to drive the listview on the catalogue page.
 */
class ItemAdapter extends ArrayAdapter<Item> {

    private List<Item> toAdd=new ArrayList<>();
    private static ItemAdapter _instance;
    private final HashSet<SwipeLayout> _itemManager=new HashSet<>();

    /**
     * Item Adapter constructor.
     * @param context the context of the containing activity
     */
    ItemAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1, ItemCatalogue.itemList);
        _instance=this;


    }

    /**
     * Updates the list view
     */
    public static void refresh()
    {
        _instance.notifyDataSetChanged();
    }

    /**
     * Called to add checked items to the current grocery list.
     */
    static void addChecked()
    {
        for(Item i:_instance.toAdd)
            GroceryList.getCurrent().addListItem(i);
        clearChecked();
    }

    /**
     * Clears the check marks.
     */
    static void clearChecked()
    {
        _instance.toAdd=new ArrayList<>();
    }

    /**
     * Sees if anything is checked
     * @return true if something is checked
     */
    static boolean canAdd()
    {
        return _instance.toAdd.size()>0;
    }


    /**
     * Called on each element in the list so you can add listeners and format them.
     * @param p_position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    @NonNull
    public View getView(int p_position,  View convertView, @NonNull ViewGroup parent) {

        final Item item = ItemCatalogue.itemList.get(p_position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_item, parent, false);
        }

        final CheckBox name = (CheckBox) convertView.findViewById(R.id._name);

        final SwipeLayout swipe = (SwipeLayout) convertView.findViewById(R.id._swipe);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.isEnabled()&&name.isChecked())toAdd.add(item);
                else if(name.isEnabled())toAdd.remove(item);
                ItemCatalogueView.update();
                //Toast toast = Toast.makeText(v.getContext(), check.isChecked()+"", Toast.LENGTH_SHORT);
                //toast.show();
            }
        });


        name.setText(item.getName());

        final TextView _item_type_divider=(TextView) convertView.findViewById(R.id._item_type_divider);
        _item_type_divider.setText(item.getItemType().getName());
        long type=item.getItemType().getId();
        if(p_position==0 || ItemCatalogue.itemList.get(p_position-1).getItemType().getId()!=type)
        {
            boolean found=false;
            for(Item i:ItemCatalogue.search()) if(type==i.getItemType().getId()) {found=true; break;}
            if(found)_item_type_divider.setVisibility(View.VISIBLE);
            else _item_type_divider.setVisibility(View.GONE);
        }
        else _item_type_divider.setVisibility(View.GONE);

        //Toast t=Toast.makeText(convertView.getContext(),"search",Toast.LENGTH_SHORT);
        //t.show();
        if(ItemCatalogue.getLastSearch().equals("") ||
                item.getName().trim().toLowerCase().contains(ItemCatalogue.getLastSearch().toLowerCase()))
        {
            swipe.setVisibility(View.VISIBLE);

        }
        else
        {
            swipe.setVisibility(View.GONE);
        }


        if(GroceryList.getCurrent().hasItem(item))
        {
            name.setEnabled(false);
            name.setChecked(true);
        }
        else if(toAdd.contains(item))
        {
            name.setChecked(true);
            name.setEnabled(true);
        }
        else
        {
            name.setEnabled(true);
            name.setChecked(false);
        }

        final View delete = convertView.findViewById(R.id._delete);
        _itemManager.add(swipe);
        swipe.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                for(SwipeLayout s: _itemManager)
                    if(!s.equals(layout))s.close();
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onClose(SwipeLayout layout) {

            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipe.close(false);
                boolean deletable=true;
                for(GroceryList gl:GroceryListManager.getGroceryLists())
                {
                    for (ListItem li : gl.listItemList)
                    {
                        if (li.getItem().equals(item))
                        {
                            deletable = false;
                            break;
                        }

                    }
                    if(!deletable)break;
                }
                if(!deletable) Toast.makeText(view.getContext(),"Item is in a list.  Remove it from all lists first.",Toast.LENGTH_LONG).show();
                else
                {
                    ItemCatalogue.removeItem(item);
                }
                ItemCatalogueView.refresh();
                ItemCatalogueView.update();

            }
        });

        //Toast.makeText(convertView.getContext(),""+_addable_count,Toast.LENGTH_SHORT).show();

        return convertView;
    }
}