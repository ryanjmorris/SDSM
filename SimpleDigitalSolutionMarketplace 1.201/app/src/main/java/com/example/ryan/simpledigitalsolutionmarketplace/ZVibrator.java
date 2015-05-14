package com.example.ryan.simpledigitalsolutionmarketplace;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
/**
 * Created by Ryan on 4/6/2015.
 */
public class ZVibrator extends ActionBarActivity implements OnTouchListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_vibrator);

        View on = findViewById(R.id.on_button);
        on.setOnTouchListener(this);

        View off = findViewById(R.id.off_button);
        off.setOnTouchListener(this);

        View exit = findViewById(R.id.exit_button);
        exit.setOnTouchListener(this);
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.on_button:

                Vibrator buzz = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    buzz.vibrate(500000);
                }else if(event.getAction()==MotionEvent.ACTION_UP)
                {
                    buzz.cancel();
                }

                // v.vibrate(0,500,110,500,110,450,110,200,110,170,40,450,110,200,110,170,40,500); - star wars
                // v.vibrate(0,300,500,100,100,100,100,300,50,50,250,50,50,50);-Zelda

                break;

            case R.id.exit_button:
                finish();
                break;
        }
        return true;
    }


}
