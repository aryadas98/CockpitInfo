package com.example.cockpitinfo;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView acc_out;
    private TextView loc_out;
    private TextView alt_out;
    private TextView hed_out;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private long acc_lasttime = 0;
    private long loc_lasttime = 0;
    private long hed_lasttime = 0;

    private boolean acc_log = false;
    private boolean loc_log = false;
    private boolean alt_log = false;
    private boolean hed_log = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TextViews
        acc_out = (TextView) findViewById(R.id.acc_out);
        loc_out = (TextView) findViewById(R.id.loc_out);
        alt_out = (TextView) findViewById(R.id.alt_out);
        hed_out = (TextView) findViewById(R.id.hed_out);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // Accelerometer
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer , SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;

        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - acc_lasttime) > 500) {
                float acc = (float)Math.sqrt(x*x + y*y + z*z);
                float g = acc/9.81f;
                acc_out.setText(String.format("%.2f", acc)+" m/s "+String.format("%.2f", g)+" g");
                acc_lasttime = curTime;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void onCheckClick(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        ((CheckBox)view).setText(checked ? "Logging" : "Not Logging");

        switch(view.getId()) {
            case R.id.acc_log: acc_log = checked; break;
            case R.id.loc_log: loc_log = checked; break;
            case R.id.alt_log: alt_log = checked; break;
            case R.id.hed_log: hed_log = checked; break;
        }
    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
