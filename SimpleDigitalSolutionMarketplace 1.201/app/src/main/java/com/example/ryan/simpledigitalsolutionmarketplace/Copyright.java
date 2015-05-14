package com.example.ryan.simpledigitalsolutionmarketplace;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Created by Ryan on 4/30/2015.
 */
public class Copyright extends ActionBarActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_copyright);

        //Removes the actionBar, it isn't necessary for looking at copyright notice.
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

    }

    public void onBackPressed()
    {
        this.finish();
    }
}
