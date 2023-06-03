package com.example.finalproject.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.finalproject.fragments.CalendarFragment;
import com.example.finalproject.fragments.CheckListFragment;
import com.example.finalproject.fragments.NotesFragment;
import com.example.finalproject.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class HomePage extends AppCompatActivity implements SensorEventListener {
    MaterialToolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView calendarIv , todolistIv , notesIv , logoutIv;
     SensorManager sensorManager;
     Sensor mySensor;
     long lastUpdate=0, actualTime=0;
    AlertDialog.Builder builder , alert;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        toolbar=findViewById(R.id.topAppbar);
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.navigation_view);

        calendarIv=findViewById(R.id.calendarIv);
        todolistIv=findViewById(R.id.todolistIv);
        notesIv=findViewById(R.id.notesIv);
        logoutIv=findViewById(R.id.logoutIv);

        alert = new AlertDialog.Builder(this);

        lastUpdate = System.currentTimeMillis();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mySensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (mySensor == null) {
            Toast.makeText(this, "No accelerometer detected in this device", Toast.LENGTH_LONG).show();
        }
        else {
            sensorManager.registerListener(this, mySensor , SensorManager.SENSOR_DELAY_NORMAL);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Intent take = getIntent();
                int id = item.getItemId();
                item.setChecked(true);
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (id)
                {

                    case R.id.nav_schedule:
                        replaceFragment(new NotesFragment());
                        break;

                    case R.id.nav_calendar:
                        replaceFragment(new CalendarFragment());
                        break;

                    case R.id.nav_checklist:
                        replaceFragment(new CheckListFragment());
                        break;

                    case R.id.nav_login:
                        alert.setTitle("Logout").setMessage("Are you sure you want to close the app?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        alert.create().show();
                        break;
                    default:
                        return true;
                }
                return true;

            }
        });


    }
    private void replaceFragment (Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) { // בודק לפי הערכים של החיישן האם המכשיר זז
        if (sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            float [] values = sensorEvent.values;
            float x = values[0];
            float y = values[1];
            float z = values[2];

            float EG = SensorManager.GRAVITY_EARTH;
            float devAccel = (x*x+y*y+z*z)/(EG*EG);

            if (devAccel>=1.5){
                actualTime=System.currentTimeMillis();
                if ((actualTime-lastUpdate)>1000){
                    lastUpdate=actualTime;
                    builder = new AlertDialog.Builder(this);
                    builder.setTitle("Logout").setMessage("Are you sure you want to close the app?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.create().show();
                }
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
