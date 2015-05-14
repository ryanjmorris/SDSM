package com.example.ryan.simpledigitalsolutionmarketplace;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Ryan on 3/27/2015.
 */

//This class will show the purchasing piece, and send the values over to a Web Service
//to be entered into the SQL Server Database.

public class Purchase extends ActionBarActivity implements OnClickListener
{
    GrabUserData userData = new GrabUserData();

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_purchase);

        //Removes the actionBar, it isn't necessary in the purchasing piece.
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        if (userData.isBuyingCholors().equals(true))
        {
            TextView purchaseApp = (TextView) findViewById(R.id.purchaseApp);

            purchaseApp.setText("Purchase Cholors");
        }
        else if (userData.isBuyingFlashligh().equals(true))
        {
            TextView purchaseApp = (TextView) findViewById(R.id.purchaseApp);

            purchaseApp.setText("Purchase Flashlight");
        }
        else { } //If there is anymore purchasing pieces, continue to do what I have up top
        //there to display that specific apps name to be displayed.

        //The onclick listener in our case here, will be the
        //submit button.
        View purchase = findViewById(R.id.btnSubmit);
        purchase.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        EditText creditCard = (EditText) findViewById(R.id.creditCard);
        EditText cardType = (EditText) findViewById(R.id.cardType);
        EditText expiration = (EditText) findViewById(R.id.expiration);
        EditText securityCode = (EditText) findViewById(R.id.securityCode);

        switch(v.getId())
        {
            case R.id.btnSubmit:
                if (creditCard.getText().toString().length() == 0 && cardType.getText().toString().length() == 0 &&
                        expiration.getText().toString().length() == 0 && securityCode.getText().toString().length() == 0)
                {
                    creditCard.setError("Credit Card needed.");
                    cardType.setError("Card type needed.");
                    expiration.setError("Expiration needed.");
                    securityCode.setError("Security code needed.");
                }
                else if (cardType.getText().toString().length() == 0 &&
                        expiration.getText().toString().length() == 0 && securityCode.getText().toString().length() == 0)
                {
                    cardType.setError("Card type needed.");
                    expiration.setError("Expiration needed.");
                    securityCode.setError("Security code needed.");
                }
                else if (expiration.getText().toString().length() == 0 && securityCode.getText().toString().length() == 0)
                {
                    expiration.setError("Expiration needed.");
                    securityCode.setError("Security code needed.");
                }
                else if (creditCard.getText().toString().length() == 0)
                {
                    creditCard.setError("Credit Card needed.");
                }
                else if (cardType.getText().toString().length() == 0)
                {
                    cardType.setError("Card type needed.");
                }
                else if (expiration.getText().toString().length() == 0)
                {
                    expiration.setError("Expiration needed.");
                }
                else if (securityCode.getText().toString().length() == 0)
                {
                    securityCode.setError("Security code needed.");
                }
                else { }

                String checkCreditCard = creditCard.getText().toString(), checkCardType = cardType.getText().toString(),
                        checkExpiration = expiration.getText().toString(), checkSecurityCode = securityCode.getText().toString();

                checkCreditCard = isValidCreditCard(checkCreditCard);
                checkCardType = isValidCardType(checkCardType);
                checkExpiration = isValidExpiriation(checkExpiration);
                checkSecurityCode = isValidSecurityCode(checkSecurityCode);

                if (checkCreditCard.equals(""))
                {
                    creditCard.setError("Invalid Credit Card.");
                }
                else if (checkCardType.equals(""))
                {
                    cardType.setError("Invalid Card Type.");
                }
                else if (checkExpiration.equals(""))
                {
                    expiration.setError("Invalid format. MM/YYYY.");
                }
                else if (checkSecurityCode.equals(""))
                {
                    securityCode.setError("Three numbers only.");
                }
                else
                {
                    GrabUserData setData = new GrabUserData();
                    String checkUsername = setData.getUsername();

                    if (userData.isBuyingCholors().equals(true))
                    {
                        setData.setUserPurchaseInfo(checkUsername, checkCreditCard, checkCardType, checkExpiration, checkSecurityCode);
                    }
                    else if (userData.isBuyingFlashligh().equals(true))
                    {
                        setData.setUserFlashlightPurchaseInfo(checkUsername, checkCreditCard, checkCardType, checkExpiration, checkSecurityCode);
                    } else { }

                    userData.buyingFlashligh(false);
                    userData.buyingCholors(false);
                    Intent a = new Intent(this, Flashlight.class);
                    this.finish();
                    startActivity(a);
                }
        }
    }

    public String isValidCreditCard(String x)
    {
        String ePattern = "^(?:4[0-9]{12}(?:[0-9]{3})?|5[1-5][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\\d{3})\\d{11})$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(x);
        if (m.matches())
        {
            return x;
        }

        return x = "";
    }

    public String isValidCardType(String x)
    {
        String[] validCards = new String[] { "Visa", "MasterCard", "American Express", "Diners Club", "Discover", "JCB", "AmericanExpress", "DinersClub" };

        for (int i=0; i < validCards.length; i++)
        {
            if (validCards[i].toString().equalsIgnoreCase(x.toString()))
            {
                return x;
            }
            else { }
        }
        return x = "";
    }

    public String isValidExpiriation(String x)
    {
        String ePattern = "([0-9]{2})/([0-9]{4})";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(x);
        if (m.matches())
        {
            return x;
        }
        return x = "";
    }

    public String isValidSecurityCode(String x)
    {
        String ePattern = "([0-9]{3})";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(x);
        if (m.matches())
        {
            return x;
        }
        return x = "";
    }

    public void onBackPressed()
    {
        userData.buyingFlashligh(false);
        userData.buyingCholors(false);
        this.finish();
    }

}
