package com.example.ryan.simpledigitalsolutionmarketplace;

import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * Created by Ryan on 3/25/2015.
 */

public class ZFlash extends ActionBarActivity implements OnClickListener
{
    public android.hardware.Camera camera;
    boolean flash;
    boolean off;
    boolean on;
    boolean stop;
    boolean strobe;
    Strobe s;

    public boolean onCreateOptionsMenu(android.view.Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_flash_re_main , menu);

        return true;
    }

    public boolean onOptionsItemSelected(android.view.MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zflash);

        View on = findViewById(R.id.on_button);
        on.setOnClickListener(this);

        View off = findViewById(R.id.off_button);
        off.setOnClickListener(this);

        View strobe = findViewById(R.id.strobe_button);
        strobe.setOnClickListener(this);

        View stop = findViewById(R.id.stop_button);
        stop.setOnClickListener(this);

        View exit = findViewById(R.id.exit_button);
        exit.setOnClickListener(this);
    }

    public ZFlash() {
        on = true;
        strobe = true;
        off = false;
        flash = true;
        stop = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.on_button:
                if (on) {
                    on();
                    on = false;
                    off = true;
                    strobe = true;
                    return;
                }
            case R.id.off_button:
                if (off) {
                    off();
                    on = true;
                    off = false;
                    strobe = true;
                    return;
                }
            case R.id.strobe_button:
                if (strobe) {
                    strobe();
                    stop = true;
                    strobe = false;
                    return;
                }
            case R.id.stop_button:
                if (stop) {
                    s.flag = false;
                    return;
                }
                try {
                    camera.release();
                }
                catch (RuntimeException runtimeexception)
                {
                    Toast.makeText(this, "The exit method crashed", Toast.LENGTH_SHORT).show();
                }
            case R.id.exit_button:
                try{
                    camera.release();
                }
                catch(RuntimeException runtimeexception)
                {
                    {
                        Toast.makeText(this, "cammera off", Toast.LENGTH_SHORT).show();
                    }
                }
                finish();
                //return;
        }

    }

    public void on()
    {
        camera = android.hardware.Camera.open();
        android.hardware.Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode("torch");
        camera.setParameters(parameters);
        camera.startPreview();
    }

    public void off()
    {
        camera.stopPreview();
        camera.release();
    }

    public void onBackPressed()
    {
        finish();
    }

    public void strobe()
    {
        s = new Strobe(this);
        s.flag = true;
        s.start();
    }

    class Strobe extends Thread
    {

        public boolean flag;
        private ZFlash stop;

        public Strobe(ZFlash flashmainactivity)
        {
            flag = true;
            stop = flashmainactivity;
        }

        public void run()
        {
            int i = 0;
            do
            {
                if (!flag || i >= 10)
                {
                    flag = false;
                    return;
                }
                stop.on();
                stop.off();
                i++;
            } while (true);
        }
    }
}