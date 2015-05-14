package com.example.ryan.simpledigitalsolutionmarketplace;

import android.os.Bundle;
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

public class Games extends ActionBarActivity implements OnClickListener
{
    MainActivitySDSM mainActivitySDSM = new MainActivitySDSM();
    Boolean x = mainActivitySDSM.loggedIn;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_games);

        View startCholors = findViewById(R.id.cholors);
        startCholors.setOnClickListener(this);
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
        switch (v.getId())
        {
            case R.id.cholors:
            {
                Intent a = new Intent(this, CholorSplash.class);
                startActivity(a);
                break;
            }
        }
    }
}
