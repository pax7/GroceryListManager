package edu.gatech.seclass.glm;


import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

/**
 * Utility class to provide a central place to get the context.
 */
public class App extends Application
{
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    /**
     * Called on app start.
     */
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        DB.loadTestDataBase();
    }

    /**
     * Gets the context.
     * @return context
     */
    public static Context getContext() {
        return context;
    }

    /**
     * Sets the context.
     * @param c context
     */
    public static void setContext(Context c)
    {
        App.context=c;
    }
}
