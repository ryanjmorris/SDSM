package com.example.ryan.simpledigitalsolutionmarketplace;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
/**
 * Created by Ryan on 2/17/2015.
 */

/* Recorded - February 20th, 2015.*/

/* Altered file on March 18th, added in the graphical outlook for the application. */


public class Utilities extends ActionBarActivity implements OnClickListener
{
    MainActivitySDSM mainActivitySDSM = new MainActivitySDSM();
    Boolean x = mainActivitySDSM.loggedIn;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_utilities);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        View Zflash = findViewById(R.id.zachFlash);
        Zflash.setOnClickListener(this);

        View Zvibrate = findViewById(R.id.zachZVibrator);
        Zvibrate.setOnClickListener(this);

        View Flashlight = findViewById(R.id.flashLight);
        Flashlight.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity_sdsm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (mainActivitySDSM.isLoggedInTrue(x) == false)
        {
            switch (item.getItemId())
            {
                case R.id.action_settings:
                    startActivity(new Intent(this, Login.class));
                    return true;
                case R.id.SDSM_Copyright:
                    startActivity(new Intent(this, Copyright.class));
                    return true;
            }
        }
        else
        {
            switch (item.getItemId())
            {
                case R.id.action_settings:
                    startActivity(new Intent(this, Profile.class));
                    mainActivitySDSM.finish();
                    return true;
                case R.id.SDSM_Copyright:
                    startActivity(new Intent(this, Copyright.class));
                    return true;
                case R.id.SDSM_SignOut:
                    mainActivitySDSM.LoggedOut();
                    mainActivitySDSM.TheToaster();
                    this.finish();
                    return true;
            }
        }

        return false;
    }

    public void onClick(View v)
    {
        switch(v.getId())
        {
            case (R.id.zachFlash):
                Intent a = new Intent(this, ZFlash.class);
                startActivity(a);
                break;
            case (R.id.zachZVibrator):
                Intent b = new Intent(this, ZVibrator.class);
                startActivity(b);
                break;
            case (R.id.flashLight):
                Intent c = new Intent(this, FlashlightSplash.class);
                startActivity(c);
                break;
        }
    }

}
