package com.example.bq;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.bq.datatypes.QuestionResponseData;
import com.example.bq.profiletest.DataManager;
import com.example.bq.profiletest.FirebaseObserver;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    FirebaseUser user;

    public static boolean isAdmin;

    LocationManager locationManager;
    LocationListener locationListener;

    public static Location userLocation;

    public static String major;

    @Override
    protected void onStart() {
        super.onStart();
        user = FirebaseAuth.getInstance().getCurrentUser();

        //check if the user is still logged in
        if (user == null) {
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        } else {
            user.reload().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "You have been signed out as your account got banned!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    finish();
                }
            });
        }

        DataManager.getInstance().isAdmin(user.getUid(), new FirebaseObserver() {
            @Override
            public void notifyOfCallback(Object obj) {
                if (obj instanceof HashMap) {
                    HashMap<String, Object> result = (HashMap<String, Object>) obj;
                    if (result.get("action") == "isAdmin") {
                        if ((boolean) result.get("result")) {
                            isAdmin = true;
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CheckPermission();
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                userLocation = location;
                Log.d("Location", "Updated!");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Toast.makeText(MainActivity.this, "Please Enable GPS and Internet", LENGTH_SHORT).show();
            }
        };

        getLocation();

        try {
            userLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } catch (SecurityException e) {
            e.printStackTrace();
        }


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.hide();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_myprofile, R.id.nav_help,
                R.id.nav_settings, R.id.nav_messages)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        Log.d("CreateOptionsMenu", "Executed before isAdmin becomes true " + isAdmin);
        if (isAdmin) {
            menu.findItem(R.id.action_admin).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu){
        if (isAdmin) {
            menu.findItem(R.id.action_admin).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    // This code below makes the user logout when you press the settings button
    // I still dont know how to link it to the actual logout button
    // so I used the settings button since it does nothing jet
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
                return true;
            case R.id.action_admin:
                DataManager.getInstance().respondToQuestion(new QuestionResponseData(), new FirebaseObserver() {
                    @Override
                    public void notifyOfCallback(Object obj) {
                        Log.d("This works!", "KEKW");
                    }
                });
                return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        getLocation();
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener); //or MainActivity.this
    }

    public void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 0, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void CheckPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
    }
}
