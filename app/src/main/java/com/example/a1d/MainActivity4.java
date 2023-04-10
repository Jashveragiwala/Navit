package com.example.a1d;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.a1d.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationBarView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// MainActivity4 -> Add Locations
public class MainActivity4 extends AppCompatActivity {
    Button ButtonA;
    MaterialButton AddLoc;

    String numberOfDays = "";
    String startLocation = "";
    String locationsString = "";
    ArrayList<String> locations = new ArrayList<>();
    String Location;
    TextView ErrorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requetsWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main4);
        ErrorView = findViewById(R.id.error);
        ErrorView.setVisibility(View.GONE);
        ButtonA = (Button) findViewById(R.id.doneadding);

        String TAG = "mainActivity4";
        Log.i(TAG, "onCreate: I am inside activity4");

        numberOfDays = getIntent().getStringExtra("NUMBER_OF_DAYS");
        startLocation = getIntent().getStringExtra("START_LOCATION");

        System.out.println(numberOfDays);
        ButtonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // uncomment the following code to call the clustering + tsp to get the path
                // TODO Make runnable and executable for this

                for (String s : locations) {
                    locationsString += s + "%7C%";
                }

                int noOfDays = Integer.parseInt(numberOfDays);

                getPath(noOfDays, startLocation, locationsString);


                // call getClusters from Clustering.java
                Intent intent = new Intent(MainActivity4.this, MainActivity5.class);
                intent.putExtra("NUMBER_OF_DAYS", numberOfDays);
                startActivity(intent);
            }
        });

        MaterialButton AddLoc = findViewById(R.id.moreinput);
        AddLoc.setOnClickListener(new View.OnClickListener() {

            int count = 1;

            @Override
            public void onClick(View view) {
                EditText editText = new EditText(MainActivity4.this);
                editText.setId(View.generateViewId());

                // System.out.println(editText.getId());
                // gets the id for each component
                // System.out.println(editText.getText());

                // gets the text for each component
                // this text sent to Clustering code

                // locationsString += editText.getText().toString() + "%7C%";

                locations.add(editText.getText().toString());


                LinearLayoutCompat.LayoutParams layoutParams = new LinearLayoutCompat.LayoutParams(1220, 100);
                layoutParams.setMargins(0, 10, 10, 10); // set the top margin to 20 pixels
                editText.setLayoutParams(layoutParams);
                editText.setHint("Location");
                count++;
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                editText.setPadding(100, 2, 100, 2);
                editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                editText.setTextColor(Color.parseColor("#1F1F1F"));
                editText.setHintTextColor(Color.parseColor("#1F1F1F"));
                editText.setTextSize(20);
                editText.getGravity();
                editText.setBackgroundColor(Color.parseColor("#FFFFFF"));

                // delete feature

                Drawable deleteIcon = ContextCompat.getDrawable(MainActivity4.this, R.drawable.baseline_remove_24);
                deleteIcon.setBounds(0, 0, 100, 50);
                Drawable AddIcon = ContextCompat.getDrawable(MainActivity4.this, R.drawable.baseline_add_task_24);
                AddIcon.setBounds(0, 0, 90, 50);
                editText.setCompoundDrawables(deleteIcon, null, AddIcon, null);

                editText.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int viewWidth = v.getWidth();
                        int iconWidth = deleteIcon.getIntrinsicWidth();
                        int iconLeft = editText.getPaddingLeft();
                        int iconRight = iconLeft + iconWidth;
                        System.out.println(viewWidth);
                        int addiconRight = viewWidth-editText.getPaddingRight();
                        int addiconWidth = AddIcon.getIntrinsicWidth();
                        int addiconleft =  viewWidth - editText.getPaddingRight() - addiconWidth;
                        System.out.println(addiconleft);
                        System.out.println(addiconRight);
                        System.out.println(addiconWidth);

                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            if (event.getX() >= iconLeft && event.getX() <= iconRight) {
                                // Remove the EditText view from its parent layout

                                ((ViewGroup)editText.getParent()).removeView(editText);
                                return true;
                            }
                            if (event.getX() >= addiconleft && event.getX() <= addiconRight) {
                                // Remove the EditText view from its parent layout
                                Location = editText.getText().toString();
                                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
                                mapFragment.getMapAsync(new OnMapReadyCallback() {
                                    @Override
                                    public void onMapReady(GoogleMap googleMap) {
                                        // Map is ready to use
                                        // Set the map type to be normal (default is hybrid)
                                        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                                        // Convert location name to latitude and longitude
                                        Geocoder geocoder = new Geocoder(MainActivity4.this);
                                        try {
                                            List<Address> addresses = geocoder.getFromLocationName(Location, 1);
                                            if (addresses.size() == 0) {
                                                ErrorView.setVisibility(View.VISIBLE);
                                                System.out.println("ERROR No Location Found");
                                            }
                                            if (addresses.size() > 0) {
                                                Address address = addresses.get(0);
                                                LatLng location = new LatLng(address.getLatitude(), address.getLongitude());

                                                // Move the camera to the desired location and zoom level
                                                float zoomLevel = 12f;
                                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel));

                                                // Add a marker at the desired location
                                                MarkerOptions markerOptions = new MarkerOptions()
                                                        .position(location)
                                                        .title(address.getLocality())
                                                        .snippet(address.getAdminArea());
                                                googleMap.addMarker(markerOptions);
                                                ErrorView.setVisibility(View.GONE);
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                            ErrorView.setVisibility(View.VISIBLE);
                                            System.out.println("ERROR No Location Found");
                                        }
                                    }
                                });
                                return true;
                            }
                        }
                        return false;
                    }
                });

                // Add the EditText view to a parent view, e.g. a LinearLayout
                LinearLayoutCompat linearLayout = (LinearLayoutCompat) findViewById(R.id.search_bar);
                linearLayout.setGravity(Gravity.CENTER);
                linearLayout.addView(editText);
            }
        });

        //TODO 4: Check if the user inserted a valid location using google autocomplete
        //TODO 5: Add that location to the map view fragment
        //TODO 6: Give the user the option to remove the location
        //TODO 7: Store these locations into a JSON for the backend to receive


        NavigationBarView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_home:
                        // Handle click on "Home" button
                        Intent intent = new Intent(MainActivity4.this, HomePageActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.nav_journeys:
                        // Handle click on "Journeys" button
                        Intent intent_journeys = new Intent((MainActivity4.this), MainActivity5.class);
                        intent_journeys.putExtra("NUMBER_OF_DAYS", numberOfDays);
                        startActivity(intent_journeys);
                        return true;
                    default:
                        return false;
                }
            }
        });

    }

    void getPath(int numberOfDays, String startLocation, String locationsString) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        final Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {

            @Override
            public void run() {
                int numberOfDays = 2;
                String startLocation = "SUTD";
                String locationsString = "HomeTeamNS Bukit Batok%7C%SUTD%7C%NUS%7C%NTU%7C%Singapore Management University%7C%Singapore Institute of Technology%7C%Simei MRT%7C%51 Changi Village Rd%7C%Toppers Education Centre%7C%Waterway Point%7C%";

                Clustering c = new Clustering();
                ArrayList<String> clusters = null;

                ArrayList<String> finalPath = new ArrayList<>();

                try {
                    clusters = c.getClusters(numberOfDays, startLocation, locationsString);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (String s : clusters) {
                    DistanceMatrixExample DM = new DistanceMatrixExample();

                    double[][] distanceMatrix;

                    try {
                        distanceMatrix = DM.getDistances(s);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    HashMap<Integer, String> indexes = DM.getIndexes();

                    TravellingSalesman ts = new TravellingSalesman(distanceMatrix);

                    int[] path = ts.solve(distanceMatrix, 0);

                    for (int i : path) {
                        finalPath.add(indexes.get(i));
                    }

                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(finalPath);
                    }
                });
            }
        });
    }

    private void requetsWindowFeature ( int featureNoTitle){

    }
}

//                Clustering c = new Clustering();
//                ArrayList<String> clusters = null;
//                try {
//                    clusters = c.getClusters(numberOfDays, startLocation, locationsString);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                for (String s : clusters) {
//                    double[][] distanceMatrix = new double[0][];
//                    DistanceMatrixExample DM = new DistanceMatrixExample();
//                    HashMap<Integer, String> indexes = DM.indexes;
//                    Integer origin = null;
//
//                    for (Map.Entry<Integer, String> entry : indexes.entrySet()) {
//                        if (entry.getValue().equals(startLocation)) {
//                            origin = entry.getKey();
//                            break;
//                        }
//                    }
//
//                    try {
//                        distanceMatrix = DM.getDistances(s);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    int[] path = TravellingSalesman.solve(distanceMatrix, origin);
//
//                    System.out.println(path);
//                }