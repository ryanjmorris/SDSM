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
 * Created by Ryan on 5/1/2015.
 */
public class FlashlightSplash extends ActionBarActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_flashlightsplash);

        //Removes the actionBar, it isn't necessary for a splash screen.
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        LoadingScreen loadingScreen = new LoadingScreen();
        loadingScreen.execute();
    }

    class LoadingScreen extends AsyncTask<Void, Integer, Void>
    {
        int loadTimer = 0, backgroundTimer = 255, toneLighter = 0;
        LinearLayout flashSplashBackground = (LinearLayout) findViewById(R.id.flashSplashBackground);
        TextView loadingFlash = (TextView) findViewById(R.id.loadingFlash);

        public void changingText()
        {
            if (loadTimer == 0)
            {
                loadingFlash.setText("Loading");
                loadTimer++;
            }
            else if (loadTimer == 40)
            {
                loadingFlash.setText("Loading.");
                loadTimer++;
            }
            else if (loadTimer == 80)
            {
                loadingFlash.setText("Loading..");
                loadTimer++;
            }
            else if (loadTimer == 120)
            {
                loadingFlash.setText("Loading...");
                loadTimer = 0;
            }
            else if (loadTimer == 150)
            {
                loadTimer = 0;
            }
            else
            {
                loadTimer++;
            }
        }

        public void changingBackground()
        {
            flashSplashBackground.setBackgroundColor(Color.argb(backgroundTimer, 251, 255, 251));
            loadingFlash.setTextColor(Color.argb(backgroundTimer, 0, 0, 0));
        }

        @Override
        protected void onPreExecute()
        {
            loadingFlash.setText("Loading...");
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (int a = 0; a <= 510; a++)
            {
                publishProgress(a);
                try
                {
                    toneLighter++;
                    if (toneLighter == 2)
                    {
                        backgroundTimer--;
                        toneLighter = 0;
                    }
                    Thread.sleep(10);
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
        startActivity(new Intent(this, Flashlight.class));
        this.finish();
    }

    public void onBackPressed()
    {

    }
}
