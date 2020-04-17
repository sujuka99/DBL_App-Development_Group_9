package com.example.bq;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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

import com.example.bq.datamanager.DataManager;
import com.example.bq.datamanager.firebase.FirebaseFunction;
import com.example.bq.datamanager.firebase.FirebaseObserver;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    FirebaseUser user;

    public static boolean isAdmin = false;

    LocationManager locationManager;
    LocationListener locationListener;

    public static Location userLocation;

    @Override
    protected void onStart() {
        super.onStart();
        user = FirebaseAuth.getInstance().getCurrentUser();

        //check if the user is still logged in
        if (user == null) {
            // If not, return the user to the Login Screen
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        } else {
            // If the user is still logged in, check if the account has not been banned since
            // the user opened the app last
            user.reload().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "You have been signed out as your account got banned!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            });
        }

        // Check if the user is an administrator
        DataManager.getInstance().isAdmin(user.getUid(), new FirebaseObserver() {
            @Override
            public void notifyOfCallback(HashMap<String, Object> callback) {
                if (callback.get("action").equals(FirebaseFunction.FUNCTION_IS_ADMIN)) {
                    HashMap<String, Object> response = (HashMap<String, Object>) callback.get("response");
                    if ((boolean) response.get("success")) {
                        isAdmin = (boolean) response.get("admin");
                    } else {
                        Toast.makeText(getApplicationContext(), (String) response.get("error"), LENGTH_SHORT).show();
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

        // Ensure we have access to the user location, if not ask permission
        // Currently, the user cannot decline, if they do the app crashes
        CheckPermission();

        // Create a new location listener to request location updates
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

        // Get the user last known location asap
        // This is because the getLocation() method only responds to changes, thus if the user
        // does not move their phone opens the app and then opens the book page, their location is
        // still null resulting in a crash
        // This fixes it by setting their location to their last known location
        try {
            userLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        // Default fab, not used in the app
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.hide();

        // Create the Navigation menu
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
        menu.findItem(R.id.action_admin).setVisible(isAdmin);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        // Allow the property to change, as isAdmin is by default false and can change to true after
        // the server has processed the request, so update it every time the menu is opened
        menu.findItem(R.id.action_admin).setVisible(isAdmin);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                return true;
            case R.id.action_admin:
                openAdminWindow(getWindow().getDecorView().getRootView());
                return true;
        }
        return false;
    }

    /**
     * Open the administrator window which allows admins to ban other users via email in the supplied
     * view
     *
     * @param view View in which the admin window must be opened
     */
    public void openAdminWindow(View view) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.popup_admin, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });

        popupView.findViewById(R.id.adminCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupView.findViewById(R.id.adminBan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText email = popupView.findViewById(R.id.adminUser);
                if (TextUtils.isEmpty(email.getText())) {
                    email.setError("Fill in the email of the user to ban!");
                    return;
                }

                // Tell the server to attempt to ban the specified user
                DataManager.getInstance().banUser(email.getText().toString(), new FirebaseObserver() {
                    @Override
                    public void notifyOfCallback(HashMap<String, Object> callback) {
                        if (callback.get("action").equals(FirebaseFunction.FUNCTION_BAN_USER)) {
                            HashMap<String, Object> response = (HashMap<String, Object>) callback.get("response");
                            if ((boolean) response.get("success")) {
                                Toast.makeText(getApplicationContext(), "Banned the user", LENGTH_SHORT).show();
                                popupWindow.dismiss();
                            } else {
                                Toast.makeText(getApplicationContext(), (String) response.get("error"), LENGTH_SHORT).show();
                            }

                        }
                    }
                });
            }
        });
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

    /**
     * Request location updates for the main activity
     */
    public void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 0, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if we have location permissions, if not request them
     */
    public void CheckPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
    }
}
