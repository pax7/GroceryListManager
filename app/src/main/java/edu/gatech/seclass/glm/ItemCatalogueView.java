package edu.gatech.seclass.glm;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

/**
 * A view for the catalogue.
 */
public class ItemCatalogueView extends AppCompatActivity {

    private static ItemCatalogueView _instance;

    /**
     * Called on view creation.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_catalogue_view);
        _instance =this;



        final SearchView _searchView=(SearchView) findViewById(R.id._search);
        _searchView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(v.getId()==R.id._search)
                    _searchView.onActionViewExpanded();
            }
        });
        //_searchView.setQuery(ItemCatalogue.getLastSearch(),false);
        _searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                if(!ItemCatalogue.hasItem(query) && !query.trim().isEmpty())
                {
                    Intent intent = new Intent(_instance, ItemTypeView.class);
                    startActivity(intent);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ItemCatalogue.search(newText);
                ItemAdapter.refresh();
                update();
                return false;
            }
        });

        ListView _itemView = (ListView)findViewById(R.id._itemView);

        _itemView.setAdapter(new ItemAdapter(this));
        if(ItemCatalogue.itemList.size()==0) Toast.makeText(getApplicationContext(),"Type an item name and click \"ADD\" to add an item to the catalogue.",Toast.LENGTH_LONG).show();
    }


    /**
     *  Called on resume of activity.
     */
    @Override
    protected void onResume()
    {
        _instance=this;
        SearchView _searchView=(SearchView) findViewById(R.id._search);
        _searchView.setQuery(ItemCatalogue.getLastSearch(),false);
        _searchView.clearFocus();
        update();
        super.onResume();
    }

    /**
     * Resets the adapter to deal with corruption when SwipeLayout items are deleted.
     */
    public static void refresh()
    {
        ListView lv=(ListView) _instance.findViewById(R.id._itemView);
        lv.setAdapter(new ItemAdapter(_instance));

    }

    /**
     * Makes sure buttons are in the right state.
     */
    public static void update()
    {
        Button create= (Button) _instance.findViewById(R.id._create);
        if(!ItemCatalogue.hasItem(ItemCatalogue.getLastSearch())&& !ItemCatalogue.getLastSearch().trim().isEmpty())
            create.setEnabled(true);
        else create.setEnabled(false);

        FloatingActionButton add= (FloatingActionButton) _instance.findViewById(R.id._add);
        if(ItemAdapter.canAdd()) add.setVisibility(View.VISIBLE);
        else add.setVisibility(View.GONE);
    }

    /**
     * Called when user clicks to create a new item
     * @param view
     */
    @SuppressWarnings("UnusedParameters")
    public void _create_onClick(View view) {
        //Toast.makeText(view.getContext(),"create",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ItemTypeView.class);
        startActivity(intent);
    }

    /**
     * Called when the user clicks the search bar.
     * @param view
     */
    @SuppressWarnings("UnusedParameters")
    public void _search_onClick(View view) {
        SearchView _searchView=(SearchView) findViewById(R.id._search);
        InputMethodManager im = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
        im.showSoftInput(_searchView, 0);
    }

    /**
     * Called when the user clicks to go back.
     * @param view
     */
    @SuppressWarnings("UnusedParameters")
    public void _back_onClick(View view) {
        ItemCatalogue.setSearch("");
        ItemAdapter.clearChecked();
        finish();
    }

    /**
     * Called when the user clicks to add selected items to the grocery list.
     * @param view
     */
    @SuppressWarnings("UnusedParameters")
    public void _add_onClick(View view) {
        ItemAdapter.addChecked();
        ItemAdapter.clearChecked();
        finish();
    }

    /**
     * Hack to all a click off the search bar otherwise the keyboard stays open.
     * @param event
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
}