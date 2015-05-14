package com.example.ryan.simpledigitalsolutionmarketplace;

import android.os.StrictMode;
import android.util.Log;

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
 * Created by Ryan on 3/26/2015.
 */
public class GrabUserData {

    //These strings are for the GetUserData (holds users information)
    public static String useName, useFName, useLName, useEmail, useState, usePassword;

    //This string keeps track if they paid for Cholors or Flashlight
    public static String cholorsPaid, flashlightPaid;

    //This int(s) keeps Cholors Personal High Score and World High Score.
    public static int cholorsPersonalHighScore, cholorsWorldHighScore;
    public static String cholorsWorldHighScoreState, cholorsWorldHighScoreUsername;

    //This sets the amount of clicks to Power, if they are a Paid Account, the Cost of the app,
    //and the username of the individual who has used the application.
    public static int flashlightPowerClicks, flashlightPaidAccount;
    public static double flashlightCost;
    public static String flashlightUsername;

    //These booleans are off/on switches to show what the user is buying.
    public static boolean purchaseCholors, purchaseFlashlight;

    /* GetUserData will be setup on Login or SignUp class so we can use their
       personal information at will in any of the applications.

       REMEMBER, just call getUsername/getFName/etc. to pull those values.
     */
    public String GetUserData(String username, String password, String firstname, String lastname, String theemail, String thestate)
    {
        JSONObject pObject = null;
        JSONArray jParse = null;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Log.d("Tag1", "row 1");

        try
        {
            URL url = new URL("http://cmscpgmg.info/SDSM/Service1.svc/json?username="+username+"&password="+password+"");
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

            for (int i=0; i < jParse.length(); i++)
            {
                JSONObject child = jParse.getJSONObject(i);
                firstname = child.optString("FIRSTNAME").toString();
                lastname = child.optString("LASTNAME").toString();
                username = child.optString("USERNA").toString();
                theemail = child.optString("THEEMAIL").toString();
                thestate = child.optString("THESTATE").toString();
                password = child.optString("THEPASSWORD").toString();
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        useName = username; useFName = firstname; useLName = lastname; useEmail = theemail; useState = thestate; usePassword = password;
        return username+";"+firstname+";"+lastname+";"+theemail+";"+thestate+";"+password;
    }

    public String getUsername()
    {
        return useName;
    }

    public String getUserFName()
    {
        return useFName;
    }

    public String getUserLName()
    {
        return useLName;
    }

    public String getUserEmail()
    {
        return useEmail;
    }

    public String getUserState()
    {
        return useState;
    }

    public String getUserPassword()
    {
        return usePassword;
    }

    /* This is the end of the GetUserData piece. */


    /* This is where we obtain the High Score from a user from
       the game Cholors.
     */

    public String setCholorsHighScore(String theuserName,String thehighScore, String isPaidAccount, String theCost)
    {
        try {
            //this is the string that is sent to the Service1.svc on GoDaddy.
            String url = "http://cmscpgmg.info/SDSM/Service1.svc/jsoncholorsinhiscore?username=" + theuserName + "&highscore=" + thehighScore + "&ispaidaccount=" + isPaidAccount+"&cost="+theCost+"";
            sendPOSTCholorsHighScore(url, theuserName, thehighScore, isPaidAccount, theCost);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String sendPOSTCholorsHighScore(String url, String theuserName, String thehighScore, String isPaidAccount, String theCost)
    {
        InputStream inputStream = null;
        String result = "";

        try
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            String json = "";

            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("username", theuserName.toString());
            jsonObject.accumulate("highscore", thehighScore.toString());
            jsonObject.accumulate("ispaidaccount", isPaidAccount.toString());
            jsonObject.accumulate("cost", theCost.toString());

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

    /*End of obtaining High Scores */


        /*This is getting the information on SDSMCholors to see if the user
    is a premium account or basic account.
     */

    public String isPremiumAccount(String userName)
    {
        JSONObject pObject = null;
        JSONArray jParse = null;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Log.d("Tag1", "row 1");
        try
        {
            URL url = new URL("http://cmscpgmg.info/SDSM/Service1.svc/jsonverified?username="+userName+"");
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

        jParse = pObject.optJSONArray("CheckIfPaidAccountResult");

        try
        {

            for (int i=0; i < jParse.length(); i++)
            {
                JSONObject child = jParse.getJSONObject(i);
                userName = child.optString("PAIDACCOUNT").toString();
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        cholorsPaid = userName;
        return userName;
    }

    public String getCholorsPaidCode()
    {
        return cholorsPaid;
    }

    /* End of checking to see if it's a premium account. */


    /* This is grabbing Cholors Personal High Score and enabling
    it to be shown in the Cholors application.
     */

    public String cholorsPersonalHighScore(String userName)
    {
        JSONObject pObject = null;
        JSONArray jParse = null;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Log.d("Tag1", "row 1");
        try
        {
            URL url = new URL("http://cmscpgmg.info/SDSM/Service1.svc/jsoncholorpersonalhiscore?username="+userName+"");
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

        jParse = pObject.optJSONArray("CholorsPersonalHighScoreResult");

        try
        {

            for (int i=0; i < jParse.length(); i++)
            {
                JSONObject child = jParse.getJSONObject(i);
                userName = child.optString("HIGHSCORE").toString();
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        if (userName.equals(getUsername()))
        {
            cholorsPersonalHighScore = 0;
        }
        else
        {
            cholorsPersonalHighScore = Integer.parseInt(userName);
        }

        return userName;
    }

    public int getMyPersonalHighScore()
    {
        return cholorsPersonalHighScore;
    }

    /* End of grabbing Cholors personal high score */


    /*This is grabbing the Cholors World High Scores and enabling it to be
    shown in the Cholors application.
     */

    public String cholorsWorldHighScore(String userName)
    {
        JSONObject pObject = null;
        JSONArray jParse = null;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Log.d("Tag1", "row 1");
        try
        {
            URL url = new URL("http://cmscpgmg.info/SDSM/Service1.svc/jsoncholorworldhiscore?username="+userName+"");
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

        jParse = pObject.optJSONArray("CholorsWorldHighScoreResult");

        try
        {

            for (int i=0; i < jParse.length(); i++)
            {
                JSONObject child = jParse.getJSONObject(i);
                userName = child.optString("HIGHSCORE").toString();
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        if (userName.equals(getUsername()))
        {
            cholorsWorldHighScore = 0;
        }
        else
        {
            cholorsWorldHighScore = Integer.parseInt(userName);
        }

        return userName;
    }

    public int getCholorsWorldHighScore()
    {
        return cholorsWorldHighScore;
    }

    public String getCholorsWorldHighScoreState() { return cholorsWorldHighScoreState; }

    public String getCholorsWorldHighScoreUsername() { return cholorsWorldHighScoreUsername; }

    /* End of grabbing Cholors World High Scores */


    /* This is setting the amount of clicks the current user has done in their session. */

    public String setFlashlightPowerButton(String theuserName, int theClick, int thepaidAccount, double theCost)
    {
        try {
            //this is the string that is sent to the Service1.svc on GoDaddy.
            String url = "http://cmscpgmg.info/SDSM/Service1.svc/jsonclickedpower?username=" + theuserName + "&clicked=" + theClick + "&paidaccount=" + thepaidAccount+"&thecost="+theCost+"";
            sendPOSTFlashlightPowerClick(url, theuserName, theClick, thepaidAccount, theCost);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String sendPOSTFlashlightPowerClick(String url, String theuserName, int theClick, int thepaidAccount, double theCost)
    {
        InputStream inputStream = null;
        String result = "";

        try
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            String json = "";

            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("username", theuserName.toString());
            jsonObject.accumulate("clicked", theClick);
            jsonObject.accumulate("paidaccount", thepaidAccount);
            jsonObject.accumulate("thecost", theCost);

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

    /* End of setting the amount of clicks the user has done in the session. */


    /* This is grabbing the amount of clicks that the user has done in Flashlight. */

    public String flashlightPowerButtonClicks(String userName)
    {
        JSONObject pObject = null;
        JSONArray jParse = null;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Log.d("Tag1", "row 1");
        try
        {
            URL url = new URL("http://cmscpgmg.info/SDSM/Service1.svc/jsongetamountclicked?username="+userName+"");
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

        jParse = pObject.optJSONArray("AmountClickedResult");

        try
        {

            for (int i=0; i < jParse.length(); i++)
            {
                JSONObject child = jParse.getJSONObject(i);
                flashlightPowerClicks = Integer.parseInt(child.optString("CLICK"));
                flashlightCost = 1.99;
                flashlightPaidAccount = Integer.parseInt(child.optString("ISPAIDACCOUNT"));
                flashlightUsername = child.optString("USERNAME").toString();
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        return userName;
    }

    public int getPowerButtonClicks() { return flashlightPowerClicks; }
    public double getflashlightCost() { return flashlightCost; }
    public int getflashlightIsPaidAccount() { return flashlightPaidAccount; }
    public String getFlashlightUsername() { return flashlightUsername; }

    /* Ends of grabbing Flashlight amount of clicks. */


    /* This is purchasing the Flashlight application. */

    public String setUserFlashlightPurchaseInfo(String theuserName, String thecreditCard, String thecardType, String theExpiration, String thesecurityCode)
    {
        try {
            //this is the string that is sent to the Service1.svc on GoDaddy.
            String url = "http://cmscpgmg.info/SDSM/Service1.svc/jsonpurchasedFlashlight?username=" + theuserName + "&creditcard=" + thecreditCard + "&cardtype=" + thecardType + "&expiration=" +theExpiration +"&securitycode="+thesecurityCode;
            sendPOSTurl(url, theuserName, thecreditCard, thecardType, theExpiration, thesecurityCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String isFlashlightPremiumAccount(String userName)
    {
        JSONObject pObject = null;
        JSONArray jParse = null;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Log.d("Tag1", "row 1");
        try
        {
            URL url = new URL("http://cmscpgmg.info/SDSM/Service1.svc/jsonflashlightverified?username="+userName+"");
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

        jParse = pObject.optJSONArray("CheckIfPaidFlashlightAccountResult");

        try
        {

            for (int i=0; i < jParse.length(); i++)
            {
                JSONObject child = jParse.getJSONObject(i);
                userName = child.optString("ISPAIDACCOUNT");
                flashlightPaidAccount = Integer.parseInt(userName);
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        return userName;
    }

    public int getFlashlightPaidCode() { return flashlightPaidAccount; }

    /* End of the Flashlight purchasing piece. */


    /* This method(s) just is a off/on switch to show what a user is purchasing.*/

    public static Boolean buyingCholors(Boolean x)
    {
        if (x.equals(true))
        {
            purchaseCholors = true;
        }
        else
        {
            purchaseCholors = false;
        }

        return purchaseCholors;
    }

    public static Boolean isBuyingCholors()
    {
        return purchaseCholors;
    }

    public static Boolean buyingFlashligh(Boolean x)
    {
        if (x.equals(true))
        {
            purchaseFlashlight = true;
        }
        else
        {
            purchaseFlashlight = false;
        }

        return purchaseFlashlight;
    }

    public static Boolean isBuyingFlashligh()
    {
        return purchaseFlashlight;
    }
    /* End of off/on switch showing what user is purchasing */


    /* This is for the Purchasing piece, where they send their information
       over to the Web Service -> SQL Server DB.
     */
    public String setUserPurchaseInfo(String theuserName, String thecreditCard, String thecardType, String theExpiration, String thesecurityCode)
    {
        try {
            //this is the string that is sent to the Service1.svc on GoDaddy.
            String url = "http://cmscpgmg.info/SDSM/Service1.svc/jsonpurchased?username=" + theuserName + "&creditcard=" + thecreditCard + "&cardtype=" + thecardType + "&expiration=" +theExpiration +"&securitycode="+thesecurityCode;
            sendPOSTurl(url, theuserName, thecreditCard, thecardType, theExpiration, thesecurityCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String sendPOSTurl(String url, String theuserName, String thecreditCard, String thecardType, String theExpiration, String thesecurityCode) {
        InputStream inputStream = null;
        String result = "";

        try
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            String json = "";

            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("username", theuserName.toString());
            jsonObject.accumulate("creditcard", thecreditCard.toString());
            jsonObject.accumulate("cardtype", thecardType.toString());
            jsonObject.accumulate("expiration", theExpiration.toString());
            jsonObject.accumulate("securitycode", thesecurityCode.toString());

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

    /* End of the purchasing piece sending info over to Web Service. */


}
