package com.example.ryan.simpledigitalsolutionmarketplace;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Ryan on 2/16/2015.
 */

/* Needs to be recorded - March 19th, 2015.*/

 public class SignUp extends ActionBarActivity implements OnClickListener {
    private static String JSONUser, JSONUserCheck;
    SignUpUserData user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_signup);

        //Removes the actionBar, it isn't necessary in the signup process.
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        View signUp = findViewById(R.id.submitBtn);
        signUp.setOnClickListener(this);
    }

    public void onClick(View v) {
        //I'm declaring all of these EditTexts to create objects to the EditText field.
        //Why?
        //I'll be able to determine if they entered anything in the field, have
        //correct information in the field, and validate it on the button click.
        //There will be a nice little error field that pops up as well to
        //tell the user that they have to fill that field in.
        EditText firstName = (EditText) findViewById(R.id.editFName);
        EditText lastName = (EditText) findViewById(R.id.editLName);
        EditText userName = (EditText) findViewById(R.id.editUsername);
        EditText email = (EditText) findViewById(R.id.editEmail);
        EditText state = (EditText) findViewById(R.id.editState);
        EditText password = (EditText) findViewById(R.id.editPassword);
        EditText rePassword = (EditText) findViewById(R.id.editRePassword);

        switch (v.getId()) {
            case R.id.submitBtn: {
                //These if-statements generate the correct amount of errors if
                //the fields are not entered in correctly.
                if (firstName.getText().toString().length() == 0 && lastName.getText().toString().length() == 0 && userName.getText().toString().length() == 0 && email.getText().toString().length() == 0 && state.getText().toString().length() == 0 && password.getText().toString().length() == 0 && rePassword.getText().toString().length() == 0) {
                    firstName.setError("First name is required!");
                    lastName.setError("Last name is required!");
                    userName.setError("You must choose a username!");
                    email.setError("You must enter an email address!");
                    state.setError("State is required!");
                    password.setError("You must decide on a password!");
                    rePassword.setError("You must make sure this is the same as password!");
                } else if (lastName.getText().toString().length() == 0 && userName.getText().toString().length() == 0
                        && email.getText().toString().length() == 0 && state.getText().toString().length() == 0 && password.getText().toString().length() == 0
                        && rePassword.getText().toString().length() == 0) {
                    lastName.setError("Last name is required!");
                    userName.setError("You must choose a username!");
                    email.setError("You must enter an email address!");
                    state.setError("State is required!");
                    password.setError("You must decide on a password!");
                    rePassword.setError("You must make sure this is the same as password!");
                } else if (userName.getText().toString().length() == 0
                        && email.getText().toString().length() == 0 && state.getText().toString().length() == 0 && password.getText().toString().length() == 0
                        && rePassword.getText().toString().length() == 0) {
                    userName.setError("You must choose a username!");
                    email.setError("You must enter an email address!");
                    state.setError("State is required!");
                    password.setError("You must decide on a password!");
                    rePassword.setError("You must make sure this is the same as password!");
                } else if (email.getText().toString().length() == 0 && state.getText().toString().length() == 0 && password.getText().toString().length() == 0
                        && rePassword.getText().toString().length() == 0) {
                    email.setError("You must enter an email address!");
                    state.setError("State is required!");
                    password.setError("You must decide on a password!");
                    rePassword.setError("You must make sure this is the same as password!");
                } else if (state.getText().toString().length() == 0 && password.getText().toString().length() == 0
                        && rePassword.getText().toString().length() == 0) {
                    state.setError("State is required!");
                    password.setError("You must decide on a password!");
                    rePassword.setError("You must make sure this is the same as password!");
                } else if (password.getText().toString().length() == 0
                        && rePassword.getText().toString().length() == 0) {
                    password.setError("You must decide on a password!");
                    rePassword.setError("You must make sure this is the same as password!");
                } else if (firstName.getText().toString().length() == 0) {
                    firstName.setError("First name is required!");
                } else if (lastName.getText().toString().length() == 0) {
                    lastName.setError("Last name is required!");
                } else if (userName.getText().toString().length() == 0) {
                    userName.setError("You must choose a username!");
                } else if (email.getText().toString().length() == 0) {
                    email.setError("You must enter an email address!");
                } else if (state.getText().toString().length() == 0) {
                    state.setError("State is required!");
                } else if (password.getText().toString().length() == 0) {
                    password.setError("You must decide on a password!");
                } else if (rePassword.getText().toString().length() == 0) {
                    rePassword.setError("You must make sure this is the same as password!");
                } else {

                    //These next couple of lines will determine if the state/email entered is correct.
                    //It will also check to see if the first and last names are correct as well.
                    String JSONCheck;
                    String theState = state.getText().toString();
                    String theEmail = email.getText().toString();
                    String theFName = firstName.getText().toString();
                    String theLName = lastName.getText().toString();
                    JSONUser = userName.getText().toString();
                    JSONUserCheck = userName.getText().toString();
                    theState = validStates(theState);
                    theEmail = isValidEmailAddress(theEmail);
                    theFName = isValidFirstName(theFName);
                    theLName = isValidLastName(theLName);
                    JSONCheck = isAvailableUsername(JSONUser);

                    if (firstName.getText().toString().length() < 2)
                    {
                        firstName.setError("Your first name must be more than one character.");
                    } else if (lastName.getText().toString().length() < 2) {
                        lastName.setError("Your last name must be more than one character.");
                    } else if (!firstName.getText().toString().equals(isValidFirstName(theFName))) {
                        firstName.setError("No special characters allowed.");
                    } else if (!lastName.getText().toString().equals(isValidLastName(theLName))) {
                        lastName.setError("No special characters allowed.");
                    } else if (!password.getText().toString().equals(rePassword.getText().toString()) || password.getText().toString().length() == 0) {
                        password.setError("Passwords must match!");
                        rePassword.setError("Passwords must match!");
                    } else if (password.getText().toString().length() < 8) {
                        password.setError("Password must be greater than 7 characters.");
                    } else if (!email.getText().toString().equals(theEmail)) {
                        email.setError("Incorrect email format.");
                    }else if (!state.getText().toString().equals(theState)) {
                        state.setError("Incorrect state. Abbreviations only.");
                    } else if (JSONCheck.equals(""))
                    {
                        userName.setError("Username already taken, choose another.");
                    } else {
                            //If the program gets to this point, it means validation worked.

                            //This will set up a class to hold the new users information.
                            //This class will be helpful for cool later additions in the app
                            //if we want to pull records on our Games/Utilities.
                            user = new SignUpUserData();
                            user.setFirstname(firstName.getText().toString());
                            user.setLastname(lastName.getText().toString());
                            user.setUsername(userName.getText().toString());
                            user.setEmail(email.getText().toString());
                            user.setState(state.getText().toString().toUpperCase());
                            user.setPassword(password.getText().toString());

                            try {
                                //this is the string that is sent to the Service1.svc on GoDaddy.
                                String url = "http://cmscpgmg.info/SDSM/Service1.svc/jsonset?firstname=" + user.getFirstname() + "&lastname=" + user.getLastname() + "&username=" + user.getUsername() + "&state=" + user.getState() + "&email=" + user.getEmail() + "&password=" + user.getPassword();
                                sendPOSTurl(url, user);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Intent a = new Intent(this, Login.class);
                            startActivity(a);
                            break;
                        }
                    }
                }

                break;
            }
        }

    public static String isAvailableUsername(String x)
    {
        JSONObject pObject = null;
        JSONArray jParse = null;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Log.d("Tag1", "row 1");

        try
        {
            URL url = new URL("http://cmscpgmg.info/SDSM/Service1.svc/jsonuser?username="+x+"");
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

        jParse = pObject.optJSONArray("GetUserResult");

        try
        {

            for (int i=0; i < jParse.length(); i++)
            {
                JSONObject child = jParse.getJSONObject(i);
                x = child.optString("USERNA").toString();
                if (x.equals(JSONUserCheck))
                {
                    return x = "";
                }
                else
                {
                    return x;
                }

            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return x;
    }

    //Checks to see if the user entered a valid first name.
    public static String isValidFirstName(String x)
    {
        String ePattern = "^^[a-z A-Z\\.\\s]+$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(x);
        if (m.matches())
        {
            return x;
        }
        return x = "";
    }

    //Checks to see if the user entered a valid last name.
    public static String isValidLastName(String x)
    {
        String ePattern = "^^[a-z A-Z\\.\\s]+$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(x);
        if (m.matches())
        {
            return x;
        }
        return x = "";
    }

    //Checks to see if the user entered an
    //email or not.
    public static String isValidEmailAddress(String x)
    {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(x);
        if (m.matches())
        {
            return x;
        }
        return x = "";
    }

    //Validates the states that the user enters
    //Only abbreviations.
    public static String validStates(String x)
    {
        String[] states = new String[] {"AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "DC", "FL", "GA", "HI", "ID", "IL", "IN", "IA"
                , "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND",
                "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"};

        for (int i = 0; i < states.length; i++)
        {
            if (states[i].toUpperCase().toString().equals(x.toUpperCase().toString()))
            {
                return x.toLowerCase();
            }
            else
            {

            }
        }
        return x="";
    }

    public static String sendPOSTurl(String url, SignUpUserData user) {
        InputStream inputStream = null;
        String result = "";

        try
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            String json = "";

            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("firstname", user.getFirstname());
            jsonObject.accumulate("lastname", user.getLastname());
            jsonObject.accumulate("username", user.getUsername());
            jsonObject.accumulate("email", user.getEmail());
            jsonObject.accumulate("state", user.getState());
            jsonObject.accumulate("password", user.getPassword());

            json = jsonObject.toString();

            StringEntity se = new StringEntity(json);

            httppost.setEntity(se);
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httppost);

            inputStream = httpResponse.getEntity().getContent();

            if (inputStream != null)
            {
                result = convertStreamToString(inputStream);
            }
            else
            {
                result = "Did not work!";
            }

        } catch (Exception e)
        {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        String line = "";
        String result = "";
        try {
            while ((line = reader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }
    }

}
