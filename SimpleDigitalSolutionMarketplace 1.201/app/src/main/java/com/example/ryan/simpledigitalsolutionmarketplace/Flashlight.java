package com.example.ryan.simpledigitalsolutionmarketplace;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by Ryan on 4/9/2015.
 */
public class Flashlight extends ActionBarActivity implements OnClickListener
{
    GrabUserData grabUserData = new GrabUserData();
    ScrollView backChange;
    LinearLayout backgroundChange;
    TextView purchaseFlashlight;
    Button powerButton, sosButton;
    Camera camera;
    Camera.Parameters Flash;
    int toggled = 0, accumulate = 0, count = 0;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_flashlight);

        //Removes the actionBar, it isn't necessary in this utility.
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        grabUserData.flashlightPowerButtonClicks(grabUserData.getUsername());
        grabUserData.isFlashlightPremiumAccount(grabUserData.getUsername());

        purchaseFlashlight = (TextView) findViewById(R.id.purchaseFlashlight);
        purchaseFlashlight.setOnClickListener(this);

        powerButton = (Button) findViewById(R.id.powerButton);
        powerButton.setOnClickListener(this);

        sosButton = (Button) findViewById(R.id.sosButton);
        sosButton.setOnClickListener(this);

        if (grabUserData.getFlashlightPaidCode() == 1)
        {
            sosButton.setVisibility(View.VISIBLE);

            backgroundChange = (LinearLayout) findViewById(R.id.backgroundChange);
            backgroundChange.setOnClickListener(this);

            backChange = (ScrollView) findViewById(R.id.background);
        }
    }

    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.powerButton:
                flashlightOn();
                break;
            case R.id.sosButton:
                try
                {
                    if (Flash.getFlashMode().equals("torch"))
                    {
                        camera.release();
                        camera = null;
                    }
                }
                catch (Exception e) {}
                sosSignal();
                break;
            case R.id.purchaseFlashlight:
                try
                {
                    if (Flash.getFlashMode().equals("torch"))
                    {
                        camera.release();
                        camera = null;
                    }
                }
                catch (Exception e) {}

                grabUserData.buyingFlashligh(true);
                grabUserData.setFlashlightPowerButton(grabUserData.getUsername(), accumulate, 0, 1.99);
                Intent a = new Intent(this, Purchase.class);
                this.finish();
                startActivity(a);
                break;
            case R.id.backgroundChange:
                if (grabUserData.getFlashlightPaidCode() == 1)
                {
                    try
                    {
                        if (Flash.getFlashMode().equals("torch"))
                        {
                            camera.release();
                            camera = null;
                        }
                    }
                    catch (Exception e) {}

                    if (count == 0)
                    {
                        backChange.setBackgroundColor(Color.WHITE);
                        powerButton.setVisibility(View.GONE);
                        sosButton.setVisibility(View.GONE);
                        count++;
                    }
                    else
                    {
                        backChange.setBackgroundColor(Color.BLACK);
                        powerButton.setVisibility(View.VISIBLE);
                        sosButton.setVisibility(View.VISIBLE);
                        count--;
                    }
                    System.out.println(count);
                }
                else
                {

                }
                System.out.println("Background has been clicked.");
        }
    }

    public void flashlightOn()
    {
        if (toggled == 0)
        {
            camera = Camera.open();
            try
            {
                Flash = camera.getParameters();

                Flash.setFlashMode("torch");
                Camera.Parameters p = camera.getParameters();
                camera.setParameters(Flash);
                camera.startPreview();
            }
            catch (Exception e) {}
            accumulate++;
            toggled++;
        }
        else
        {
            camera.stopPreview();
            camera.release();
            camera = null;

            toggled--;
        }

        try {
            if (grabUserData.getFlashlightPaidCode() == 1)
            {
                disablePurchase();
            }
            else
            {
                if (grabUserData.getPowerButtonClicks() > 0) {
                    showPurchase();
                }
                else if (accumulate > 0)
                {
                    showPurchase();
                }
                else {}
            }
        }
        catch (Exception e)
        {

        }
    }

    //These methods just are on/off switches to display if they want to purchase the app.
    public void showPurchase()
    {
        purchaseFlashlight.setVisibility(View.VISIBLE);
    }
    public void disablePurchase() { purchaseFlashlight.setVisibility(View.GONE);}

    public void sosSignal()
    {
        try
        {
            if (Flash.getFlashMode().equals("torch"))
            {
                camera.release();
                camera = null;
            }
        }
        catch (Exception e) {}

        for (int x = 0; x <= 8; x++) {
            camera = Camera.open();
            try {
                Flash = camera.getParameters();

                Flash.setFlashMode("torch");
                Camera.Parameters p = camera.getParameters();
                camera.setParameters(Flash);
                camera.startPreview();
            } catch (Exception e) { }

            if (x >= 3 && x <= 5)
            {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {

                }
            }
            else
            {
                try {
                    Thread.sleep(50);
                } catch (Exception e) {

                }
            }

            try {
                camera.stopPreview();
                camera.release();
                camera = null;
            } catch (Exception e) {

            }

            if (toggled == 1)
            {
                toggled = 0;
            }
        }
    }

    public void onBackPressed()
    {
        try
        {
            if (Flash.getFlashMode().equals("torch"))
            {
                camera.release();
                camera = null;
            }
        }
        catch (Exception e) {}

        grabUserData.setFlashlightPowerButton(grabUserData.getUsername(), accumulate, 0, 1.99);
        this.finish();
    }

}
