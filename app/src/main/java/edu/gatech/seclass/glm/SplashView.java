package edu.gatech.seclass.glm;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.util.Locale;

/**
 * The sweet splash screen view.
 */
public class SplashView extends AppCompatActivity {

    /**
     * Called on creation.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_view);

        AnimationDrawable animation = new AnimationDrawable();
        animation.setOneShot(true);
        Resources res = this.getResources();

        for(int i=1;i<=30;i++)
        {
            String name = String.format(Locale.US,"bag_animation%04d", i);
            int frame = res.getIdentifier(name, "drawable",
                    this.getPackageName());
            animation.addFrame(res.getDrawable(frame), 30);
        }
        ImageView iv=(ImageView)findViewById(R.id._logo);
        iv.setImageDrawable(animation);
        animation.start();


        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent intent = new Intent(getApplicationContext(), GroceryListManagerView.class);
                startActivity(intent);
                finish();
            }
        }, 1200);

    }

}