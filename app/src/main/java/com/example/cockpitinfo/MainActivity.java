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
    private Sensor magnetometer;

    private float[] gravity = new float[3];
    private float[] mfield  = new float[3];

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

        // Magnetometer
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magnetometer , SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;

        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            gravity[0] = sensorEvent.values[0];
            gravity[1] = sensorEvent.values[1];
            gravity[2] = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - acc_lasttime) > 100) {
                float acc = (float)Math.sqrt(gravity[0]*gravity[0]+
                                             gravity[1]*gravity[1]+
                                             gravity[2]*gravity[2]);
                float g = acc/9.81f;
                acc_out.setText(String.format("%.2f", acc)+" m/s "+String.format("%.2f", g)+" g");
                acc_lasttime = curTime;
            }
        }

        if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            mfield[0] = sensorEvent.values[0];
            mfield[1] = sensorEvent.values[1];
            mfield[2] = sensorEvent.values[2];

            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, gravity, mfield);

            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                float heading = (float) Math.toDegrees(orientation[0]);
                float pitch = (float) Math.toDegrees(orientation[1]);
                float roll = (float) Math.toDegrees(orientation[2]);

                hed_out.setText(heading+"\n"+pitch+"\n"+roll);
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
