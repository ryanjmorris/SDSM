package com.example.ryan.simpledigitalsolutionmarketplace;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by Ryan on 4/12/2015.
 */
public class Profile extends ActionBarActivity implements OnClickListener
{
    int counter = 0;
    GrabUserData grabUserData = new GrabUserData();
    TextView profileUsername, profileEmail, profileFirstname, profileLastname, profileState;
    ScrollView houdini;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_profile);

        ActionBar menu = getSupportActionBar();
        menu.hide();

        profileUsername = (TextView) findViewById(R.id.profileUsername);
        profileEmail = (TextView) findViewById(R.id.profileEmail);
        profileFirstname = (TextView) findViewById(R.id.profileFirstname);
        profileLastname = (TextView) findViewById(R.id.profileLastname);
        profileState = (TextView) findViewById(R.id.profileState);
        houdini = (ScrollView) findViewById(R.id.houdiniMirror);

        //The background of the Profile is clickable.
        View houdiniTrick = findViewById(R.id.houdiniMirror);
        houdiniTrick.setOnClickListener(this);

        //Sets all of the text up depending on the user that has clicked 'Profile'
        displayInformation();
    }

    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.houdiniMirror:
                counter++;
                //If counter gets to 10 clicks, it will prompt a secret administrator form.
                break;
        }
    }

    public void displayInformation()
    {
        profileUsername.setText(grabUserData.getUsername());
        profileEmail.setText(grabUserData.getUserEmail());
        profileState.setText(grabUserData.getUserState());
        profileFirstname.setText(grabUserData.getUserFName());
        profileLastname.setText(grabUserData.getUserLName());
    }

    @Override
    public void onBackPressed()
    {
        this.finish();
    }

}
