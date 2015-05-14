package com.example.ryan.simpledigitalsolutionmarketplace;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 * Created by Ryan on 3/24/2015.
 */
public class Cholors extends ActionBarActivity implements OnClickListener {
    private GrabUserData userData = new GrabUserData();
    private cholorCounter timer;
    private scrambleButtons scrambleButtons;
    private SendToTwitter sendToTwitter;
    private Random randTimer = new Random();
    private Random randClickColor = new Random();
    private static int totalScore = 0, count = 0, twitterScore = 0, countTwitter = 0;
    private TextView theTimer, cholorsChangeToScore, cholorsSetClickColor;
    private TextView twitterText;
    private ImageView twitterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_cholors);

        //Removes the actionBar, it isn't necessary in a game.
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        TextView theUsername = (TextView) findViewById(R.id.theUser);
        TextView purchase = (TextView) findViewById(R.id.pointView);
        Button start = (Button) findViewById(R.id.startButton);

        theUsername.setText("Welcome, "+userData.getUsername());
        myPersonalHighScore();
        theWorldHighScore();

        twitterButton = (ImageView) findViewById(R.id.twitterButton);
        twitterButton.setOnClickListener(this);
        purchase.setOnClickListener(this);
        start.setOnClickListener(this);
    }

    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.pointView:
                timer.endOfGame();
                timer.cancel(true);
                userData.buyingCholors(true);
                Intent a = new Intent(this, Purchase.class);
                startActivity(a);
                break;
            case R.id.startButton:
                startGame();
                break;
            case R.id.twitterButton:
                //Checks to see if the button has been hit or not.
                if (countTwitter == 0) {
                    //Executes the Thread.
                    sendToTwitter.execute();
                    //Just gives information based on if it went to Twitter or not.
                    Toast.makeText(getApplicationContext(), "Your score has been posted to Twitter!", Toast.LENGTH_SHORT).show();
                    //Keeps counting to give different error messages.
                    countTwitter++;
                } else if (countTwitter == 1) {
                    Toast.makeText(getApplicationContext(), "Remember, you already posted your score.", Toast.LENGTH_SHORT).show();
                    countTwitter++;
                }
                else if (countTwitter == 2)
                {
                    Toast.makeText(getApplicationContext(), "Seriously, it's there, we promise.", Toast.LENGTH_SHORT).show();
                    countTwitter++;
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "If you hit me one more time....", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public void startGame()
    {
        cholorsChangeToScore = (TextView) findViewById(R.id.cholorsToScore);
        cholorsSetClickColor = (TextView) findViewById(R.id.cholorsSetClickColor);
        Button start = (Button) findViewById(R.id.startButton);
        twitterText = (TextView) findViewById(R.id.twitterText);
        twitterButton = (ImageView) findViewById(R.id.twitterButton);
        countTwitter = 0;

        twitterText.setVisibility(twitterText.GONE);
        twitterButton.setVisibility(twitterButton.GONE);
        start.setVisibility(start.GONE);
        isPaidAccount();

        //Starts the timer & Random color they must click.
        timer = new cholorCounter();
        timer.execute();

        //Starts the scrambling of buttons.
        scrambleButtons = new scrambleButtons();
        scrambleButtons.execute();
    }

    public void isPaidAccount()
    {
        TextView cholorsPurchase = (TextView) findViewById(R.id.pointView);
        String theUser = userData.getUsername();
        userData.isPremiumAccount(theUser);

        if (userData.getCholorsPaidCode().equals("0"))
        {
            if (count == 0)
            {
                cholorsPurchase.setBackgroundColor(Color.rgb(0,5,255));
                cholorsPurchase.setTextColor(Color.rgb(163,140,38));
                cholorsPurchase.setText("0.99 for more points, tap here!");
                count++;
            }
            else
            {
                cholorsPurchase.setBackgroundColor(Color.rgb(98,104,104));
                cholorsPurchase.setTextColor(Color.rgb(230,233,230));
                cholorsPurchase.setText("0.99 for more points, tap here!");
                count--;
            }
        }
        else
        {
            cholorsPurchase.setText("");
        }
    }

    public void theWorldHighScore()
    {
        TextView worlHighScore = (TextView) findViewById(R.id.worldScore);

        String thepersUser = userData.getUsername();
        try
        {
            userData.cholorsWorldHighScore(thepersUser);
            worlHighScore.setText("World Score: "+userData.getCholorsWorldHighScore());
        }
        catch (Exception e)
        {

        }
    }

    public void myPersonalHighScore()
    {
        TextView persHighScore = (TextView) findViewById(R.id.highScore);

        String thepersUser = userData.getUsername();
        try
        {
            userData.cholorsPersonalHighScore(thepersUser);
            persHighScore.setText("High Score: "+userData.getMyPersonalHighScore());
        }
        catch (Exception e)
        {

        }
    }

    public void correctClick()
    {
        if (userData.getCholorsPaidCode().equals("0"))
        {
            totalScore = totalScore + 1000;
        }
        else
        {
            totalScore = totalScore + 2000;
        }
    }

    @Override
    public void onBackPressed()
    {
        this.finish();
    }

    class cholorCounter extends AsyncTask<Void, Integer, Void>
    {
        //Randomly selects how much time the user has to hit the right button.
        int theRandTimer = randTimer.nextInt(5);
        //Second keeps the second value, millisecond keeps the milliseconds going.
        private int second, millisecond;

        @Override
        protected void onPreExecute()
        {
            //Setting up a new random time.
            theTimer = (TextView) findViewById(R.id.theUser);
            theTimer.setText("Time remaining: 0"+theRandTimer+":00");
            cholorsChangeToScore.setTextSize(26);
            cholorsChangeToScore.setText("Score: " + totalScore);
        }

        @Override
        protected Void doInBackground(Void... params) {

            for (int a = theRandTimer; a >= 0; a--)
            {
                //b is 100, because 100*10 = 1000 (1 second).
                //more importantly, this is here because it resets.
                int b = 100;

                if(isCancelled())
                {
                    break;
                }
                else
                {
                    //Second takes the form of a, which is the second from randTimer.
                    second = a;
                    //Sends it to the method to start up the counting.
                    publishProgress(a);
                    try
                    {
                        while (b > 0)
                        {
                            //b - 1, until it is 0, then resets (has to do with milliseconds).
                            b--;
                            millisecond = b;
                            publishProgress(b);
                            //The thread sleeps 100 times, 100 * 10 = 1000 (1 second).
                            Thread.sleep(10);
                        }
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        protected void onProgressUpdate(Integer... values)
        {
            if (isCancelled())
            {

            }
            else
            {
                //Displays the time remaining. seconds and milliseconds generated from while
                //loop in the doInBackground.
                theTimer.setText("Time remaining: 0"+second+":"+millisecond);
            }
        }

        protected void onPostExecute(Void result)
        {
            if (isCancelled())
            {

            }
            else
            {
                turnOffListeners();
                theTimer.setText("You ran out of time!");

                //The button is regenerated again.
                Button start = (Button) findViewById(R.id.startButton);
                start.setVisibility(start.VISIBLE);

                //Twitter for a score of 0? Don't be silly.
                if (totalScore > 0)
                {
                    twitterText.setVisibility(twitterText.VISIBLE);
                    twitterButton.setVisibility(twitterButton.VISIBLE);
                    sendToTwitter = new SendToTwitter();
                    sendToTwitter.twitterScore(totalScore);
                }

                cholorsChangeToScore.setTextSize(26);
                cholorsChangeToScore.setText("Final Score: " + totalScore);
                userData.setCholorsHighScore(userData.getUsername(), String.valueOf(totalScore), "0", "0.99");
                myPersonalHighScore();
                theWorldHighScore();
                totalScore = 0;
                cholorsSetClickColor.setText("");
            }
        }

        //This method turns off the button listeners
        //if they failed the game.
        protected void turnOffListeners()
        {
            Button green = (Button) findViewById(R.id.cholorOne);
            Button red = (Button) findViewById(R.id.cholorTwo);
            Button blue = (Button) findViewById(R.id.cholorThree);
            Button teal = (Button) findViewById(R.id.cholorFour);
            Button yellow = (Button) findViewById(R.id.cholorFive);
            Button purple = (Button) findViewById(R.id.cholorSix);
            Button black = (Button) findViewById(R.id.cholorSeven);
            Button white = (Button) findViewById(R.id.cholorEight);

            green.setClickable(false);
            red.setClickable(false);
            blue.setClickable(false);
            teal.setClickable(false);
            yellow.setClickable(false);
            purple.setClickable(false);
            black.setClickable(false);
            white.setClickable(false);
        }

        //If they hit the wrong button, this is the
        //end of the game version they get.
        protected void endOfGame()
        {
            turnOffListeners();
            theTimer.setText("You hit the wrong button!");
            Button start = (Button) findViewById(R.id.startButton);
            start.setVisibility(start.VISIBLE);

            //Twitter for a score of 0? Don't be silly.
            if (totalScore > 0 )
            {
                twitterText.setVisibility(twitterText.VISIBLE);
                twitterButton.setVisibility(twitterButton.VISIBLE);
                sendToTwitter = new SendToTwitter();
                sendToTwitter.twitterScore(totalScore);
            }
            cholorsChangeToScore.setTextSize(26);
            cholorsChangeToScore.setText("Final Score: " + totalScore);
            userData.setCholorsHighScore(userData.getUsername(), String.valueOf(totalScore), "0", "0.99");
            myPersonalHighScore();
            theWorldHighScore();
            totalScore = 0;
            cholorsSetClickColor.setText("");
        }
    }

    class scrambleButtons extends AsyncTask<Void, Integer, Void>
    {
        Button green = (Button) findViewById(R.id.cholorOne);
        Button red = (Button) findViewById(R.id.cholorTwo);
        Button blue = (Button) findViewById(R.id.cholorThree);
        Button teal = (Button) findViewById(R.id.cholorFour);
        Button yellow = (Button) findViewById(R.id.cholorFive);
        Button purple = (Button) findViewById(R.id.cholorSix);
        Button black = (Button) findViewById(R.id.cholorSeven);
        Button white = (Button) findViewById(R.id.cholorEight);
        Button[] btnColorArr;
        int theColorGenerator;

        protected void randomizeColors()
        {
            int theButtonGenerator;
            Boolean x = true;
            int i = 0, g = 9, r = 9, b = 9, t = 9, y = 9, p = 9, bl = 9, w = 9,
                    b0 = 9, b1 = 9, b2 = 9, b3 = 9, b4 = 9, b5 = 9, b6 = 9, b7 =9;

            //Sets all of the button according to listeners.
            green = (Button) findViewById(R.id.cholorOne);
            red = (Button) findViewById(R.id.cholorTwo);
            blue = (Button) findViewById(R.id.cholorThree);
            teal = (Button) findViewById(R.id.cholorFour);
            yellow = (Button) findViewById(R.id.cholorFive);
            purple = (Button) findViewById(R.id.cholorSix);
            black = (Button) findViewById(R.id.cholorSeven);
            white = (Button) findViewById(R.id.cholorEight);

            //int array holding the color values.
            int[] randomColGen = new int[] { Color.rgb(49, 174, 37), Color.rgb(249, 76, 76),
                                    Color.rgb(67, 136, 231), Color.rgb(59, 235, 240),  Color.rgb(240, 234, 88),
                                    Color.rgb(229, 61, 222), Color.rgb(0, 0, 0), Color.rgb(252, 252, 252) };

            //Creates an array of values so a user know what color to click.
            String[] setClColorArr = { "Green", "Red", "Blue", "Teal", "Yellow", "Purple", "Black", "White"};

            //Button array holding the colors.
            btnColorArr = new Button[]{green, red, blue, teal, yellow, purple, black, white};

            while (x.equals(true)) {

                theColorGenerator = randClickColor.nextInt(8);
                theButtonGenerator = randClickColor.nextInt(8);
                g = theColorGenerator; //Ex 1
                b0 = theButtonGenerator;
                btnColorArr[theButtonGenerator].setBackgroundColor(randomColGen[theColorGenerator]);
                System.out.println("THE G Random button = " + theButtonGenerator + " Random color = " + theColorGenerator);

                theColorGenerator = randClickColor.nextInt(8);
                theButtonGenerator = randClickColor.nextInt(8);
                r = theColorGenerator;
                b1 = theButtonGenerator;
                //btnColorArr[theButtonGenerator].setBackgroundColor(randomColGen[theColorGenerator]);
                /*if ((r == g) || (b1 == b0))
                {
                    btnColorArr[theButtonGenerator].setBackgroundColor(randomColGen[theColorGenerator]);
                    //System.out.println("I am unique = "+btnColorArr[theButtonGenerator].toString());
                }*/
                System.out.println("THE R Random button = " + theButtonGenerator + " Random color = " + theColorGenerator);
                while ((r == g) || (b1 == b0))
                {
                    theColorGenerator = randClickColor.nextInt(8);
                    theButtonGenerator = randClickColor.nextInt(8);
                    r = theColorGenerator;
                    b1 = theButtonGenerator;
                    if ((r == g) || (b1 == b0))
                    {
                        btnColorArr[theButtonGenerator].setBackgroundColor(randomColGen[theColorGenerator]);
                        //System.out.println("I am unique = "+btnColorArr[theButtonGenerator].toString());
                    }
                    System.out.println("R WHILE LOOP Random button = " + theButtonGenerator + " Random color = " + theColorGenerator);
                }

                theColorGenerator = randClickColor.nextInt(8);
                theButtonGenerator = randClickColor.nextInt(8);
                b = theColorGenerator;
                b2 = theButtonGenerator;
                //btnColorArr[theButtonGenerator].setBackgroundColor(randomColGen[theColorGenerator]);
                /*if (((b == r) || (b == g)) || ((b2 == b1) || (b2 == b0)))
                {
                    btnColorArr[theButtonGenerator].setBackgroundColor(randomColGen[theColorGenerator]);
                    //System.out.println("I am unique = "+btnColorArr[theButtonGenerator]);
                }*/
                System.out.println("THE B Random button = " + theButtonGenerator + " Random color = " + theColorGenerator);
                while (((b == r) || (b == g)) || ((b2 == b1) || (b2 == b0)))
                {
                    theColorGenerator = randClickColor.nextInt(8);
                    theButtonGenerator = randClickColor.nextInt(8);
                    b = theColorGenerator;
                    b2 = theButtonGenerator;
                    if (((b == r) || (b == g)) || ((b2 == b1) || (b2 == b0)))
                    {
                        btnColorArr[theButtonGenerator].setBackgroundColor(randomColGen[theColorGenerator]);
                        //System.out.println("I am unique = "+btnColorArr[theButtonGenerator]);
                    }
                    System.out.println("B WHILE LOOP Random button = " + theButtonGenerator + " Random color = " + theColorGenerator);
                }

                theColorGenerator = randClickColor.nextInt(8);
                theButtonGenerator = randClickColor.nextInt(8);
                t = theColorGenerator;
                b3 = theButtonGenerator;
                //btnColorArr[theButtonGenerator].setBackgroundColor(randomColGen[theColorGenerator]);
                /*if (((t == b) || (t == r) || (t == g)) || ((b3 == b2) || (b3 == b1) || (b3 == b0)))
                {
                    btnColorArr[theButtonGenerator].setBackgroundColor(randomColGen[theColorGenerator]);
                    //System.out.println("I am unique = "+btnColorArr[theButtonGenerator]);
                }*/
                System.out.println("THE T Random button = " + theButtonGenerator + " Random color = " + theColorGenerator);
                while (((t == b) || (t == r) || (t == g)) || ((b3 == b2) || (b3 == b1) || (b3 == b0)))
                {
                    theColorGenerator = randClickColor.nextInt(8);
                    theButtonGenerator = randClickColor.nextInt(8);
                    t = theColorGenerator;
                    b3 = theButtonGenerator;
                    if (((t == b) || (t == r) || (t == g)) || ((b3 == b2) || (b3 == b1) || (b3 == b0)))
                    {
                        btnColorArr[theButtonGenerator].setBackgroundColor(randomColGen[theColorGenerator]);
                        //System.out.println("I am unique = "+btnColorArr[theButtonGenerator]);
                    }

                    System.out.println("T WHILE LOOP Random button = " + theButtonGenerator + " Random color = " + theColorGenerator);
                }

                theColorGenerator = randClickColor.nextInt(8);
                theButtonGenerator = randClickColor.nextInt(8);
                y = theColorGenerator;
                b4 = theButtonGenerator;
                //btnColorArr[theButtonGenerator].setBackgroundColor(randomColGen[theColorGenerator]);
              /*  if (((y == t) || (y == b) || (y == r) || (y == g)) || ((b4 == b3) || (b4 == b2) || (b4 == b1) || (b4 == b0)))
                {
                    btnColorArr[theButtonGenerator].setBackgroundColor(randomColGen[theColorGenerator]);
                    //System.out.println("I am unique = "+btnColorArr[theButtonGenerator]);
                }*/
                System.out.println("THE Y Random button = " + theButtonGenerator + " Random color = " + theColorGenerator);
                while (((y == t) || (y == b) || (y == r) || (y == g)) || ((b4 == b3) || (b4 == b2) || (b4 == b1) || (b4 == b0)))
                {
                    theColorGenerator = randClickColor.nextInt(8);
                    theButtonGenerator = randClickColor.nextInt(8);
                    y = theColorGenerator;
                    b4 = theButtonGenerator;
                    if (((y == t) || (y == b) || (y == r) || (y == g)) || ((b4 == b3) || (b4 == b2) || (b4 == b1) || (b4 == b0)))
                    {
                        btnColorArr[theButtonGenerator].setBackgroundColor(randomColGen[theColorGenerator]);
                        //System.out.println("I am unique = "+btnColorArr[theButtonGenerator]);
                    }

                    System.out.println("Y WHILE LOOP Random button = " + theButtonGenerator + " Random color = " + theColorGenerator);
                }

                theColorGenerator = randClickColor.nextInt(8);
                theButtonGenerator = randClickColor.nextInt(8);
                p = theColorGenerator;
                b5 = theButtonGenerator;
                //btnColorArr[theButtonGenerator].setBackgroundColor(randomColGen[theColorGenerator]);
             /*   if (((p == y) || (p == t) || (p == b) || (p == r) || (p == g))
                        || ((b5 == b4) || (b5 == b3) || (b5 == b2) || (b5 == b1) || (b5 == b0)))
                {
                    btnColorArr[theButtonGenerator].setBackgroundColor(randomColGen[theColorGenerator]);
                    //System.out.println("I am unique = "+btnColorArr[theButtonGenerator]);
                }*/
                System.out.println("THE P Random button = " + theButtonGenerator + " Random color = " + theColorGenerator);
                while (((p == y) || (p == t) || (p == b) || (p == r) || (p == g))
                        || ((b5 == b4) || (b5 == b3) || (b5 == b2) || (b5 == b1) || (b5 == b0)))
                {
                    theColorGenerator = randClickColor.nextInt(8);
                    theButtonGenerator = randClickColor.nextInt(8);
                    p = theColorGenerator;
                    b5 = theButtonGenerator;
                    if (((p == y) || (p == t) || (p == b) || (p == r) || (p == g))
                            || ((b5 == b4) || (b5 == b3) || (b5 == b2) || (b5 == b1) || (b5 == b0)))
                    {
                        btnColorArr[theButtonGenerator].setBackgroundColor(randomColGen[theColorGenerator]);
                        //System.out.println("I am unique = "+btnColorArr[theButtonGenerator]);
                    }

                    System.out.println("P WHILE LOOP Random button = " + theButtonGenerator + " Random color = " + theColorGenerator);
                }

                theColorGenerator = randClickColor.nextInt(8);
                theButtonGenerator = randClickColor.nextInt(8);
                bl = theColorGenerator;
                b6 = theButtonGenerator;
                //btnColorArr[theButtonGenerator].setBackgroundColor(randomColGen[theColorGenerator]);
              /*  if (((bl == p) || (bl == y) || (bl == t) || (bl == b) || (bl == r) || (bl == g))
                        || ((b6 == b5) || (b6 == b4) || (b6 == b3) || (b6 == b2) || (b6 == b1) || (b6 == b0)))
                {
                    btnColorArr[theButtonGenerator].setBackgroundColor(randomColGen[theColorGenerator]);
                    //System.out.println("I am unique = "+btnColorArr[theButtonGenerator]);
                }*/
                System.out.println("THE BL Random button = " + theButtonGenerator + " Random color = " + theColorGenerator);
                while (((bl == p) || (bl == y) || (bl == t) || (bl == b) || (bl == r) || (bl == g))
                        || ((b6 == b5) || (b6 == b4) || (b6 == b3) || (b6 == b2) || (b6 == b1) || (b6 == b0)))
                {
                    theColorGenerator = randClickColor.nextInt(8);
                    theButtonGenerator = randClickColor.nextInt(8);
                    bl = theColorGenerator;
                    b6 = theButtonGenerator;
                    if (((bl == p) || (bl == y) || (bl == t) || (bl == b) || (bl == r) || (bl == g))
                            || ((b6 == b5) || (b6 == b4) || (b6 == b3) || (b6 == b2) || (b6 == b1) || (b6 == b0)))
                    {
                        btnColorArr[theButtonGenerator].setBackgroundColor(randomColGen[theColorGenerator]);
                        //System.out.println("I am unique = "+btnColorArr[theButtonGenerator]);
                    }

                    System.out.println("BL WHILE LOOP Random button = " + theButtonGenerator + " Random color = " + theColorGenerator);
                }

                theColorGenerator = randClickColor.nextInt(8);
                theButtonGenerator = randClickColor.nextInt(8);
                w = theColorGenerator;
                b7 = theButtonGenerator;
                //btnColorArr[theButtonGenerator].setBackgroundColor(randomColGen[theColorGenerator]);
               /* if (((w == bl || w == p || w == y || w == t || w == b || w == r || w == g)
                        || ((b7 == b6) || (b7 == b5) || (b7 == b4) || (b7 == b3) || (b7 == b2) || (b7 == b1) || (b7 == b0))))
                {
                    btnColorArr[theButtonGenerator].setBackgroundColor(randomColGen[theColorGenerator]);
                    //System.out.println("I am unique = "+btnColorArr[theButtonGenerator]);
                }*/
                System.out.println("THE W Random button = " + theButtonGenerator + " Random color = " + theColorGenerator);
                while (((w == bl || w == p || w == y || w == t || w == b || w == r || w == g)
                        || ((b7 == b6) || (b7 == b5) || (b7 == b4) || (b7 == b3) || (b7 == b2) || (b7 == b1) || (b7 == b0))))
                {
                    theColorGenerator = randClickColor.nextInt(8);
                    theButtonGenerator = randClickColor.nextInt(8);
                    w = theColorGenerator;
                    b7 = theButtonGenerator;
                    if (((w == bl || w == p || w == y || w == t || w == b || w == r || w == g)
                            || ((b7 == b6) || (b7 == b5) || (b7 == b4) || (b7 == b3) || (b7 == b2) || (b7 == b1) || (b7 == b0))))
                    {
                        btnColorArr[theButtonGenerator].setBackgroundColor(randomColGen[theColorGenerator]);
                        //System.out.println("I am unique = "+btnColorArr[theButtonGenerator]);
                    }

                    System.out.println("W WHILE LOOP Random button = " + theButtonGenerator + " Random color = " + theColorGenerator);
                }

                //Decides what color they should click on.
                cholorsSetClickColor.setText(setClColorArr[theColorGenerator]);
                x = false;
            }
        }

        //This sets all of the button listeners to "on"
        //although, if they hit the wrong button it will
        //end the game for them.
        protected void setOnClickListeners()
        {
                green.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.cholorOne:
                                timer.endOfGame();
                                timer.cancel(true);
                                break;
                        }
                    }
                });
                red.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.cholorTwo:
                                timer.endOfGame();
                                timer.cancel(true);
                                break;
                        }
                    }
                });
                blue.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.cholorThree:
                                timer.endOfGame();
                                timer.cancel(true);
                                break;
                        }
                    }
                });
                teal.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.cholorFour:
                                timer.endOfGame();
                                timer.cancel(true);
                                break;
                        }
                    }
                });
                yellow.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.cholorFive:
                                timer.endOfGame();
                                timer.cancel(true);
                                break;
                        }
                    }
                });
                purple.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.cholorSix:
                                timer.endOfGame();
                                timer.cancel(true);
                                break;
                        }
                    }
                });
                black.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.cholorSeven:
                                timer.endOfGame();
                                timer.cancel(true);
                                break;
                        }
                    }
                });
                white.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.cholorEight:
                                timer.endOfGame();
                                timer.cancel(true);
                                break;
                        }
                    }
                });
        }

        //Reruns the PreExecution to get a new random colors.
        protected void reRunExecute()
        {
            onPreExecute();
        }

        protected void onPreExecute()
        {
            //Randomizes the colors so that every correct button
            //click will change where the colors are - to mess the user up :-).
            //randomizeColors();

            //Turns on all of the buttons, so the user can click them.
            setOnClickListeners();

            //Creates an array of values, sets a random generator, displays what the user should click.
            String[] setClColorArr = { "Green", "Red", "Blue", "Teal", "Yellow", "Purple", "Black", "White"};
            theColorGenerator = randClickColor.nextInt(7);
            cholorsSetClickColor.setText(setClColorArr[theColorGenerator]);

            //Button array holding the colors.
            btnColorArr = new Button[]{green, red, blue, teal, yellow, purple, black, white};

            btnColorArr[theColorGenerator].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (btnColorArr[theColorGenerator].equals(green)) {
                        switch (v.getId()) {
                            case R.id.cholorOne:
                                timer.cancel(true);
                                correctClick();
                                timer = new cholorCounter();
                                timer.execute();
                                reRunExecute();
                                break;
                        }
                    }
                    if (btnColorArr[theColorGenerator].equals(red))
                    {
                        switch (v.getId()) {
                            case R.id.cholorTwo:
                                timer.cancel(true);
                                correctClick();
                                timer = new cholorCounter();
                                timer.execute();
                                reRunExecute();
                                break;
                        }
                    }
                    if (btnColorArr[theColorGenerator].equals(blue))
                    {
                        switch (v.getId()) {
                            case R.id.cholorThree:
                                timer.cancel(true);
                                correctClick();
                                timer = new cholorCounter();
                                timer.execute();
                                reRunExecute();
                                break;
                        }
                    }
                        if (btnColorArr[theColorGenerator].equals(teal))
                        {
                            switch (v.getId()) {
                                case R.id.cholorFour:
                                    timer.cancel(true);
                                    correctClick();
                                    timer = new cholorCounter();
                                    timer.execute();
                                    reRunExecute();
                                    break;
                            }
                        }
                        if (btnColorArr[theColorGenerator].equals(yellow))
                        {
                            switch (v.getId()) {
                                case R.id.cholorFive:
                                    timer.cancel(true);
                                    correctClick();
                                    timer = new cholorCounter();
                                    timer.execute();
                                    reRunExecute();
                                    break;
                            }
                        }
                        if (btnColorArr[theColorGenerator].equals(purple))
                        {
                            switch (v.getId()) {
                                case R.id.cholorSix:
                                    timer.cancel(true);
                                    correctClick();
                                    timer = new cholorCounter();
                                    timer.execute();
                                    reRunExecute();
                                    break;
                            }
                        }
                        if (btnColorArr[theColorGenerator].equals(black))
                        {
                            switch (v.getId()) {
                                case R.id.cholorSeven:
                                    timer.cancel(true);
                                    correctClick();
                                    timer = new cholorCounter();
                                    timer.execute();
                                    reRunExecute();
                                    break;
                            }
                        }
                        if (btnColorArr[theColorGenerator].equals(white))
                        {
                            switch (v.getId()) {
                                case R.id.cholorEight:
                                    timer.cancel(true);
                                    correctClick();
                                    timer = new cholorCounter();
                                    timer.execute();
                                    reRunExecute();
                                    break;
                            }
                        }
                    }
            });

        }

        protected Void doInBackground(Void... params)
        {
            return null;
        }

        protected void onProgressUpdate(Integer... values)
        {
            if (isCancelled())
            {

            }
            else
            {

            }
        }

        protected void onPostExecute(Void result)
        {
            if (isCancelled())
            {

            }
            else
            {

            }
        }
    }

    class SendToTwitter extends AsyncTask<Void, Integer, Void>
    {

        public int twitterScore(int x)
        {
            twitterScore = x;
            return x;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String consumerKeyStr = "mR4AoKfPVbQXf6NBiTVTF3h4M",
                        consumerSecretStr = "LCkheK1hUVuhbcWCsC6TImiXp7DdzaYb1ZN9b5bjPPGxspqA3y",
                        accessTokenStr = "3177115975-0NMh007tQt1XTulhe6E6YaN9l0e2CHh0FliZk2D",
                        accessTokenSecretStr = "aC2FO4NsfykIX4zZs0LQCeFSU5W471b5lLEB7obGvdNx1";

                Twitter twitter = new TwitterFactory().getInstance();

                twitter.setOAuthConsumer(consumerKeyStr, consumerSecretStr);
                AccessToken accessToken = new AccessToken(accessTokenStr, accessTokenSecretStr);

                twitter.setOAuthAccessToken(accessToken);


                twitter.updateStatus("From my app, #Cholors, "+userData.getUsername()+" shared their score of "+
                twitterScore+"! #androidDev #Twittergration #SystemAnalysi");
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

