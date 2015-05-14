package com.example.ryan.simpledigitalsolutionmarketplace;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Ryan on 4/30/2015.
 */
public class CholorSplash extends ActionBarActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_cholorsplash);

        //Removes the actionBar, it isn't necessary for a splash screen.
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        LoadingScreen loadingScreen = new LoadingScreen();
        loadingScreen.execute();
    }

    class LoadingScreen extends AsyncTask<Void, Integer, Void>
    {
        int timer = 8, pleaseTimer = 0, backgroundTimer = 0;
        LinearLayout splashBackground = (LinearLayout) findViewById(R.id.splashBackground);
        TextView pleaseWait = (TextView) findViewById(R.id.pleaseWait);

        public void changingText()
        {
            if (pleaseTimer == 0)
            {
                pleaseWait.setText("Please Wait.");
                pleaseTimer++;
            }
            else if (pleaseTimer == 1)
            {
                pleaseWait.setText("Please Wait..");
                pleaseTimer++;
            }
            else
            {
                pleaseWait.setText("Please Wait...");
                pleaseTimer = 0;
            }
        }

        public void changingBackground()
        {
            if (backgroundTimer == 1)
            {
                splashBackground.setBackgroundColor(Color.rgb(65, 118, 234));
            }
            else if (backgroundTimer == 2)
            {
                splashBackground.setBackgroundColor(Color.rgb(234, 227, 77));
            }
            else if (backgroundTimer == 3)
            {
                splashBackground.setBackgroundColor(Color.rgb(231, 234, 205));
            }
            else if (backgroundTimer == 4)
            {
                splashBackground.setBackgroundColor(Color.rgb(42, 43, 41));
            }
            else if (backgroundTimer == 5)
            {
                splashBackground.setBackgroundColor(Color.rgb(247, 108, 232));
            }
            else if (backgroundTimer == 6)
            {
                splashBackground.setBackgroundColor(Color.rgb(115, 247, 74));
            }
            else if (backgroundTimer == 7)
            {
                splashBackground.setBackgroundColor(Color.rgb(8, 113, 45));
            }

        }

        @Override
        protected void onPreExecute()
        {
            pleaseWait.setText("Please Wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (int a = 0; a < 8 ; a++)
            {
                publishProgress(a);
                try
                {
                    backgroundTimer++;
                    Thread.sleep(1000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            return null;
        }

        protected void onProgressUpdate(Integer... values)
        {
            changingText();
            changingBackground();
        }

        protected void onPostExecute(Void result)
        {
            transition();
        }
    }

    public void transition()
    {
        startActivity(new Intent(this, Cholors.class));
        this.finish();
    }

    public void onBackPressed()
    {

    }
}
