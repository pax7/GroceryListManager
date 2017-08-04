package edu.gatech.seclass.glm;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The landing page for the app.  This shows the names of all the lists. You can click and go to
 * a list.  You can delete.  You can add new lists.
 */
public class GroceryListManagerView extends AppCompatActivity {


    @SuppressLint("StaticFieldLeak")
    private static ListView _listView;

    private static GroceryListManagerView _instance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list_manager_view);
        _instance=this;


        _listView = (ListView)findViewById(R.id._groceryListView);
        _listView.setAdapter(new GroceryListAdapter(_instance));
        if(GroceryListManager.groceryListList.size()==0) Toast.makeText(getApplicationContext(),"Click button to create list.",Toast.LENGTH_LONG).show();
    }

    @Override
    protected  void onResume()
    {
        _instance=this;
        super.onResume();
    }

    @Override
    protected  void onPostResume()
    {
        update();
        super.onPostResume();
    }

    static void update()
    {
        //This is a workaround for the swipeLayouts getting corrupted when items are removed from a listView
        _listView.setAdapter(new GroceryListAdapter(_instance));
    }



    @SuppressWarnings("UnusedParameters")
    public void _add_onClick(View view)
    {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.list_naming_alert_dialog, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(
                this);
        builder.setView(promptsView);

        builder.setTitle("Name New Grocery List");
        final EditText input = (EditText)promptsView.findViewById(R.id._new_list_name);
        input.setSingleLine();
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if(!input.getText().toString().trim().isEmpty())
                {
                    GroceryListManager.addGroceryList(input.getText().toString());
                    update();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Couldn't create a list without a name.", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog d=builder.create();
        d.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        input.setOnEditorActionListener(new EditText.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if(actionId == EditorInfo.IME_ACTION_DONE)
                {
                    d.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
                    return true;
                }
                return false;
            }
        });
        d.show();



    }
    @SuppressWarnings("UnusedParameters")
    public void _menu_onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), MenuView.class);
        startActivity(intent);
    }


}