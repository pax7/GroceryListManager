package edu.gatech.seclass.glm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;


import java.io.IOException;

/**
 * View of optional functionality.
 */
public class MenuView extends AppCompatActivity {

    //private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE=456;


    /**
     * Called on creation.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_view);
    }

    /**
     * Called when the user clicks back.
     * @param view
     */
    @SuppressWarnings("UnusedParameters")
    public void _back_onClick(View view) {

        this.finish();
    }

    /**
     * Called when the user want to delete all lists.
     * @param view
     */
    @SuppressWarnings("UnusedParameters")
    public void _delete_all_onClick(View view)
    {
        for(GroceryList gl:GroceryListManager.getGroceryLists())
        {
            GroceryListManager.removeGroceryList(gl);
        }
        GroceryListAdapter.refresh();
        Toast.makeText(getApplicationContext(),"All lists have been removed.",Toast.LENGTH_LONG).show();
    }

    /**
     * Called when the user wants to look at the DB.
     * @param view
     */
    @SuppressWarnings("UnusedParameters")
    public void _inspect_database_onClick(View view)
    {
        Intent intent = new Intent(getApplicationContext(), AndroidDatabaseManager.class);
        startActivity(intent);
    }

    /**
     * Called when the user wants to clear the DB.
     * @param view
     */
    @SuppressWarnings("UnusedParameters")
    public void _reset_database_onClick(View view)
    {
        DB.resetDataBase();
        Intent mStartActivity = new Intent(getApplicationContext(), GroceryListManagerView.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(getApplicationContext(), mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        finishAffinity();
        System.exit(0);
    }

    /**
     * Called when the user wants to load test data.
     * @param view
     */
    @SuppressWarnings("UnusedParameters")
    public void _load_test_db_onClick(View view)
    {
        DB.loadTestDataBase();
        Intent mStartActivity = new Intent(getApplicationContext(), GroceryListManagerView.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(getApplicationContext(), mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        finishAffinity();
        System.exit(0);
    }

    /**
     * Called when the user wants a compress string representing the DB sent to their email.
     * @param view
     * @throws IOException
     */
    @SuppressWarnings("UnusedParameters")
    public void _save_database_onClick(View view) throws IOException
    {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody =DB.getDump()+"\n\nDECOMPRESS FUNCTION\n\n";
        shareBody+="public static void decompress(String data) throws Exception\n" +
                "{\n" +
                "    Base64.Decoder decoder = Base64.getDecoder();\n" +
                "    byte[] decodedByteArray = decoder.decode(data);\n" +
                "    Inflater inflater = new Inflater();\n" +
                "    inflater.setInput(decodedByteArray);\n" +
                "    FileOutputStream fos = new FileOutputStream (\"glm.db\");\n" +
                "    byte[] buffer = new byte[1024];\n" +
                "    while (!inflater.finished())" +
                "    {\n" +
                "        int count = inflater.inflate(buffer);\n" +
                "        fos.write(buffer, 0, count);\n" +
                "    }\n" +
                "    fos.close();\n" +
                "}\n";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Compressed Base64 SQLite DB");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }


}