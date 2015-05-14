package com.example.ryan.simpledigitalsolutionmarketplace;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
/* Recorded - February 16th, 2015.*/


public class MainActivitySDSM extends ActionBarActivity implements OnClickListener {
    public static Boolean loggedIn = false;
    GrabUserData grabUserData = new GrabUserData();
    static Random rand = new Random();
    static int chooseGame = rand.nextInt(1);
    static int chooseUtility = rand.nextInt(3);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_sdsm);

        ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setIcon(R.drawable.sdsm);
        menu.setDisplayUseLogoEnabled(true);

        String[] randomGameName = new String[] { "Cholors" };
        String[] randomUtilityName = new String[] { "ZFlash", "ZVibrator", "Flashlight" };

        Button utilityofWeekArr = (Button) findViewById(R.id.utilityofWeek);
        utilityofWeekArr.setText(randomUtilityName[chooseUtility]);
        Button gameofWeekArr = (Button) findViewById(R.id.gameofWeek);
        gameofWeekArr.setText(randomGameName[chooseGame]);

        View games = findViewById(R.id.button);
        games.setOnClickListener(this);
        View utility = findViewById(R.id.button2);
        utility.setOnClickListener(this);
        View gameofWeek = findViewById(R.id.gameofWeek);
        gameofWeek.setOnClickListener(this);
        View utilityofWeek = findViewById(R.id.utilityofWeek);
        utilityofWeek.setOnClickListener(this);
        View randomGame = findViewById(R.id.randomizerGame);
        randomGame.setOnClickListener(this);
        View randomUtility = findViewById(R.id.randomizerUtility);
        randomUtility.setOnClickListener(this);
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

            if (loggedIn == false)
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
                        LoggedOut();
                        TheToaster();
                        return true;
                }
            }

        return false;
        }

    public void onClick(View v)
    {
        if (loggedIn == false) {
            switch (v.getId()) {
                case R.id.button: {
                    Intent a = new Intent(this, Login.class);
                    startActivity(a);
                    break;
                }
                case R.id.button2: {
                    Intent b = new Intent(this, Login.class);
                    startActivity(b);
                    break;
                }
                case R.id.gameofWeek:
                    Intent c = new Intent(this, Login.class);
                    startActivity(c);
                    break;
                case R.id.utilityofWeek:
                    Intent d = new Intent(this, Login.class);
                    startActivity(d);
                    break;
                case R.id.randomizerGame:
                    Intent e = new Intent(this, Login.class);
                    startActivity(e);
                    break;
                case R.id.randomizerUtility:
                    Intent f = new Intent(this, Login.class);
                    startActivity(f);
                    break;
            }
        }
        else
        {
            switch (v.getId())
            {
                case R.id.button: {
                    Intent a = new Intent(this, Games.class);
                    startActivity(a);
                    break;
                }
                case R.id.button2: {
                    Intent b = new Intent(this, Utilities.class);
                    startActivity(b);
                    break;
                }
                case R.id.gameofWeek:
                    gameofWeek();
                    break;
                case R.id.utilityofWeek:
                    utilityofWeek();
                    break;
                case R.id.randomizerGame:
                    randomizeUtility();
                    break;
                case R.id.randomizerUtility:
                    randomizeGame();
                    break;
            }
        }
    }

    //Toasty
    public void TheToaster()
    {
        try
        {
            if (!loggedIn && !grabUserData.getUsername().equals(""))
            {
                Toast.makeText(getApplicationContext(), grabUserData.getUserFName()+", you have Logged Out.", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e) { }
    }

    //Methods that grab random games/utilites to fulfill the Randomizer.
    public void randomizeUtility()
    {
        int chooseUtilityRan = rand.nextInt(3);

        Intent[] randomUtility = new Intent[] { (new Intent(this, ZFlash.class)), (new Intent(this, ZVibrator.class)),
                (new Intent(this, FlashlightSplash.class)) };

        startActivity(randomUtility[chooseUtilityRan]);
    }

    public void randomizeGame()
    {
        int chooseGameRan = rand.nextInt(1);

        Intent[] randomGame = new Intent[] { (new Intent(this, CholorSplash.class)) };

        startActivity(randomGame[chooseGameRan]);
    }

    public void gameofWeek()
    {
        Intent[] randomGame = new Intent[] { (new Intent(this, CholorSplash.class)) };

        startActivity(randomGame[chooseGame]);
    }

    public void utilityofWeek()
    {
        Intent[] randomUtility = new Intent[] { (new Intent(this, ZFlash.class)), (new Intent(this, ZVibrator.class)),
                (new Intent(this, FlashlightSplash.class)) };

        startActivity(randomUtility[chooseUtility]);
    }

    //Changing the value of loggedIn to true.
    public static Boolean LoggedIn()
    {
        loggedIn = true;
        return loggedIn;
    }
    public static Boolean isLoggedInTrue(Boolean x) { return loggedIn; }

    //This method isn't going to be used, but if we implement
    //a log out feature, this will be necessary.
    public static Boolean LoggedOut()
    {
        loggedIn = false;
        return loggedIn;
    }
    public static Boolean isLoggedInFalse(Boolean x) {return loggedIn; }

    //A test to see if this would remove the ability to press the back button
    @Override
    public void onBackPressed()
    {
        this.finish();
    }

}
