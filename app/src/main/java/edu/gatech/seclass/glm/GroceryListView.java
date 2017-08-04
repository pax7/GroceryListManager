package edu.gatech.seclass.glm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Space;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

/**
 * The view of an individual list.
 */
public class GroceryListView extends AppCompatActivity {


    @SuppressLint("StaticFieldLeak")
    private static ListView _listView;
    @SuppressLint("StaticFieldLeak")
    private static GroceryListView _instance;

    /**
     * Called when activity created.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        _instance =this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list_view);



        EditText _list_name =(EditText) findViewById(R.id._list_name);
        _listView =(ListView) findViewById(R.id._listItemView);
        Space space=new Space(this);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        space.setMinimumHeight(Math.max(metrics.widthPixels,metrics.heightPixels)/8);
        _listView.addFooterView(space);
        _list_name.setText(GroceryList.getCurrent().getName());
        _list_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(!s.toString().trim().isEmpty())
                {
                    GroceryList.getCurrent().setName(s.toString());
                }
                else
                {
                    GroceryList.getCurrent().setName("List");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        _listView.setAdapter(new ListItemAdapter(_instance));

        if(GroceryList.getCurrent().listItemList.size()==0) Toast.makeText(getApplicationContext(),"Click button to add item.",Toast.LENGTH_LONG).show();

        _toggleButtons();

    }

    /**
     * Used to make sure the buttons are in the right state.
     */
    public static void update() {_instance._toggleButtons();}

    private void _toggleButtons()
    {
        final Button _deselect = (Button) findViewById(R.id._deselect);
        final Button _delete = (Button) findViewById(R.id._delete);
        final ImageView _trash = (ImageView) findViewById(R.id._trash);
        boolean noChecked=true;
        for(ListItem li:GroceryList.getCurrent().getListItems())
        {
            if(li.isChecked())noChecked=false;
        }
        if(noChecked)
        {
            _deselect.setText(R.string.select_all);
            _delete.setEnabled(false);
            _trash.setColorFilter(0xFFD9D9D9, PorterDuff.Mode.SRC_ATOP);
        }
        else
        {
            _deselect.setText(R.string.deselect_all);
            _deselect.setEnabled(true);
            _delete.setEnabled(true);
            _trash.setColorFilter(0xFF000000);
        }
    }

    /**
     * Called when a new item button has been pressed.
     * @param view
     */
    public void _add_onClick(View view)
    {
        ItemCatalogue.setSearch("");
        try
        {
            ItemCatalogueView.update();
            ItemAdapter.refresh();
        } //it might not be created in which case no update required
        catch(Exception e){}

        Intent intent = new Intent(getApplicationContext(), ItemCatalogueView.class);
        startActivity(intent);
    }

    /**
     * Called when the back button gets pressed.
     * @param view
     */
    public void _back_onClick(View view) {

        this.finish();
    }

    /**
     * Called when the user select or deselcts all.
     * @param view
     */
    public void _deselect_onClick(View view)
    {
        GroceryList.getCurrent().toggleChecked();
        _toggleButtons();
    }

    /**
     * Called when the user clicks to delete all selected items.
     * @param view
     */
    public void _delete_onClick(View view)
    {
        GroceryList.getCurrent().removeChecked();
        _toggleButtons();
        refresh();

    }

    /**
     * Called when the user click share.
     * @param view
     */
    public void _share_onClick(View view)
    {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "";
        List<ListItem> list=GroceryList.getCurrent().listItemList;
        String lastType="";
        for(int i=0;i<list.size();i++)
        {
            ListItem li=list.get(i);
            String type=li.getItem().getItemType().getName();
            if(!type.equals(lastType))
            {
                shareBody+=String.format(Locale.US,"%S\n",type);
                lastType=type;
            }
            shareBody+=String.format(Locale.US,"%2d %s %s\n",li.getQuantity(),li.getItem().getName(),li.isChecked()?"(CHECKED)":"");
        }
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, GroceryList.getCurrent().getName());
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    /**
     * Resets the adapter forcing a complete rebuild of the listview which fixes issues encountered
     * SwipeLayouts getting corrupted when items are removed.
     */
    @SuppressWarnings("WeakerAccess")
    public static void refresh()
    {
        //This is a workaround for the swipeLayouts getting corrupted when items are removed from a listView
        _listView.setAdapter(new ListItemAdapter(_instance));
    }

    /**
     * Called on resume.
     */
    @Override
    protected  void onResume()
    {
        _instance=this;
        refresh();
        super.onResume();
    }

    /**
     * This is a hack to all clicks anywhere to get you out of an editText.
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