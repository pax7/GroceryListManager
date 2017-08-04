package edu.gatech.seclass.glm;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;

/**
 * GroceryListAdapter used to populate the ListView containing the names of the GroceryLists.
 */
class GroceryListAdapter extends ArrayAdapter<GroceryList> {

    private final List<GroceryList> _groceryListList;
    private final HashSet<SwipeLayout> _itemManager=new HashSet<>();
    private static BaseAdapter _instance;

    /**
     * Sets up an adapter that is bound to the items in a GroceryListManager
     * @param context context
     */
    GroceryListAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1, GroceryListManager.groceryListList);
        _instance=this;
        _groceryListList = GroceryListManager.groceryListList;
    }

    /** Triggers a refresh of the list. */
    public static void refresh()
    {
        _instance.notifyDataSetChanged();
    }




    /**
     * Gets a View for one item on the ListView in the GroceryListManagerView.
     * @param position positions of the item
     * @param convertView view
     * @param parent parent
     * @return view
     */
    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        final GroceryList groceryList = _groceryListList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grocery_list_item, parent, false);
        }

        final TextView name = (TextView) convertView.findViewById(R.id._name);
        final View delete = convertView.findViewById(R.id._delete);
        final SwipeLayout swipe= (SwipeLayout) convertView.findViewById(R.id._swipe);
        _itemManager.add(swipe);
        //delete.setVisibility(View.VISIBLE);
        //swipe.setVisibility(View.VISIBLE);

        swipe.addSwipeListener(new SwipeLayout.SwipeListener(){
            @Override
            public void onStartOpen(SwipeLayout layout) {
                closeSwipeLayouts(layout,true);
            }
            @Override
            public void onOpen(SwipeLayout layout) {}
            @Override
            public void onStartClose(SwipeLayout layout) {}
            @Override
            public void onClose(SwipeLayout layout) {}
            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {}
            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {}
        });

        //if you don't setCurrent then the list may not be populated.
        groceryList.setCurrent();
        int count=groceryList.getListItems().length;
        name.setText(String.format(Locale.US,"%s (%d %s)", groceryList.getName(),count,count==1?"item":"items"));

        name.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                swipe.open();
                return true;
            }
        });

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(ListItem li:groceryList.listItemList)li.touch();
                groceryList.setCurrent();
                Intent intent = new Intent(App.getContext(), GroceryListView.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                App.getContext().startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeSwipeLayouts(null,false);
                GroceryListManager.removeGroceryList(groceryList);
                GroceryListManagerView.update();
            }
        });

        return convertView;
    }

    private void closeSwipeLayouts(SwipeLayout current,boolean smooth)
    {
        for(SwipeLayout s: _itemManager)
        {
            if(!s.equals(current))s.close(smooth);
        }
    }
}