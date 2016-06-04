package com.example.anita.acceleration;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;


public class AniitaAnita extends AppCompatActivity implements SensorEventListener{


    private TextView xTxt,yTxt,zTxt;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //here we create sensormanager
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        //accelerometer sensor
        Sensor s = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //here we register sensorlistener
        sm.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL);
        //here we assign TextviewyTxt
        xTxt=(TextView)findViewById(R.id.xTxt);

        button=(Button)findViewById(R.id.Blubutton);
       button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               Bluetooth();
            }
        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
 public void onAccuracyChanged(Sensor S,int accuracy){

 }

    public void onSensorChanged(SensorEvent event){
        xTxt.setText("x " + event.values[2]);
        Log.d("Man", " man");



        if(event.values[2]>=1 ){
            Bluetooth.instruction("m");
            Log.d("yo", " yo");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;


        }

        return super.onOptionsItemSelected(item);
    }
public void Bluetooth() { Bluetooth bluetooth = new Bluetooth();
bluetooth.findBT();
    try {
        bluetooth.openBT();
    } catch (IOException e) {
        e.printStackTrace();
    }


}}