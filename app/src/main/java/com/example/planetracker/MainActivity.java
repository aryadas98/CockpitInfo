package com.example.planetracker;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView acc_out;
    private TextView loc_out;
    private TextView alt_out;
    private TextView hed_out;

    private boolean acc_log = false;
    private boolean loc_log = false;
    private boolean alt_log = false;
    private boolean hed_log = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
