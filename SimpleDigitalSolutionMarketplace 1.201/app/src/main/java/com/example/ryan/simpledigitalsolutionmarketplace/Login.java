package com.example.ryan.simpledigitalsolutionmarketplace;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Ryan on 2/16/2015.
 */

/* Recorded - February 16th, 2015.*/
/* Changes made (Record needed) - March 11th, 2015 */

 public class Login extends ActionBarActivity implements OnClickListener {
    //These line(s) are based off of setting the SignUpUserData.
    private static String theuser="", thepassword="", thefname="", thelname="", theemail = "", thestate = "";
    SignUpUserData Setuser = new SignUpUserData();

    @SuppressLint("NewApi")

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //The onclick listener in our case here, will be the
        //submit button.
        View logIn = findViewById(R.id.btnLogIn);
        logIn.setOnClickListener(this);
        View signUp = findViewById(R.id.signUp);
        signUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        //I'm declaring all of these EditTexts to create objects to the EditText field.
        //Why?
        //I'll be able to determine if they entered anything in the field, have
        //correct information in the field, and validate it on the button click.
        //There will be a nice little error field that pops up as well to
        //tell the user that they have to fill that field in.
        EditText userName = (EditText)findViewById(R.id.logUsername);
        EditText password = (EditText)findViewById(R.id.logPassword);

        switch(v.getId())
        {
            case R.id.btnLogIn:
            {
                //Next 4 lines are based off of the login piece.
                String user = userName.getText().toString();
                String pword = password.getText().toString();

                JSONObject pObject = null;
                JSONArray jParse = null;

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                Log.d("Tag1", "row 1");

                try
                {
                    URL url = new URL("http://cmscpgmg.info/SDSM/Service1.svc/json?username="+user+"&password="+pword+"");
                    URLConnection urlc = url.openConnection();
                    BufferedReader bfr = new BufferedReader(
                            new InputStreamReader(urlc.getInputStream()));

                    String line;

                    while ((line=bfr.readLine()) != null)
                    {
                        pObject = new JSONObject(line);
                    }

                }
                catch (Exception e)
                {
                    System.out.println(e.getMessage());
                }

                jParse = pObject.optJSONArray("GetDataResult");

                try
                {

                    for (int x=0; x < jParse.length(); x++)
                    {
                        JSONObject child = jParse.getJSONObject(x);
                        thefname = child.optString("FIRSTNAME").toString();
                        thelname = child.optString("LASTNAME").toString();
                        theuser = child.optString("USERNA").toString();
                        theemail = child.optString("THEEMAIL").toString();
                        thestate = child.optString("THESTATE").toString();
                        thepassword = child.optString("THEPASSWORD").toString();
                    }
                }
                catch (Exception e)
                {
                    System.out.println(e.getMessage());
                }

                //These if-statements are validating to make sure they
                //entered something in.
                if (userName.getText().toString().length() == 0 && password.getText().toString().length() == 0)
                {
                    userName.setError("Enter your username.");
                    password.setError("Enter your password.");
                }
                else if (userName.getText().toString().length() == 0)
                {
                    userName.setError("Enter your username.");
                }
                else if (password.getText().toString().length() == 0)
                {
                    password.setError("Enter your password.");
                }
                else if (userName.getText().toString().equals(theuser) && password.getText().toString().equals(thepassword))
                {
                    //Sets the users data into another class for later use.
                    Setuser.setFirstname(thefname);
                    Setuser.setLastname(thelname);
                    Setuser.setUsername(theuser);
                    Setuser.setEmail(theemail);
                    Setuser.setState(thestate);
                    Setuser.setPassword(thepassword);
                    GrabUserData example = new GrabUserData();
                    example.GetUserData(theuser, thepassword, thefname, thelname, theemail, thestate);

                    //Sets the loggedIn variable to true. Will send
                    //the user back to the Main page.
                    MainActivitySDSM loggedIn = new MainActivitySDSM();
                    loggedIn.LoggedIn();
                    loggedIn.finish();
                    Intent a = new Intent(this, MainActivitySDSM.class);
                    startActivity(a);

                }
                else
                {
                    userName.setError("Incorrect username or password.");
                    password.setError("Incorrect username or password.");
                }

                break;
            }
            case R.id.signUp:
            {
                Intent b = new Intent(this, SignUp.class);
                startActivity(b);
                break;
            }
        }
    }

}
