package com.example.a1d;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import com.example.a1d.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// MainActivity4 -> Add Locations
public class MainActivity4 extends AppCompatActivity {
    Button ButtonA;
    MaterialButton AddLoc;
    String startLocation = "";
    String locationsString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requetsWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main4);
        ButtonA = (Button) findViewById(R.id.doneadding);

        String TAG = "mainActivity4";
        Log.i(TAG, "onCreate: I am inside activity4");
        String numberOfDays = getIntent().getStringExtra("NUMBER_OF_DAYS");
        System.out.println(numberOfDays);
        ButtonA.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // uncomment the following code to call the clustering + tsp to get the path
                // TODO Make runnable and executable for this

//                Clustering c = new Clustering();
//                ArrayList<String> clusters = null;
//                try {
//                    clusters = c.getClusters(Integer.parseInt(numberOfDays), startLocation, locationsString);
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
//                }

                // call getClusters from Clustering.java
                Intent intent = new Intent(MainActivity4.this,MainActivity5.class);
                intent.putExtra("NUMBER_OF_DAYS", numberOfDays);
                startActivity(intent);
            }}
        );

        MaterialButton AddLoc = findViewById(R.id.moreinput);
        AddLoc.setOnClickListener(new View.OnClickListener() {

            int count = 1;
            @Override
            public void onClick(View view) {
                EditText editText = new EditText(MainActivity4.this);
                editText.setId(View.generateViewId());

                System.out.println(editText.getId());
                // gets the id for each component
                System.out.println(editText.getText());
                // gets the text for each component
                // this text sent to Clustering code
                locationsString += editText.getText() + "%7C%";


                LinearLayoutCompat.LayoutParams layoutParams = new LinearLayoutCompat.LayoutParams(1220, 100);
                layoutParams.setMargins(0, 10, 10, 10); // set the top margin to 20 pixels
                editText.setLayoutParams(layoutParams);
                editText.setHint("Location");
                count++;
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                editText.setPadding(120, 2, 0, 2);
                editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                editText.setTextColor(Color.parseColor("#1F1F1F"));
                editText.setHintTextColor(Color.parseColor("#1F1F1F"));
                editText.setTextSize(20);
                editText.getGravity();
                editText.setBackgroundColor(Color.parseColor("#FFFFFF"));

                // delete feature

                Drawable deleteIcon = ContextCompat.getDrawable(MainActivity4.this, R.drawable.cross);
                deleteIcon.setBounds(0, 0, 50, 50);
                editText.setCompoundDrawables(deleteIcon, null, null, null);

                editText.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            if (event.getX() >= 120 && event.getX() <= 170) {
                                // Remove the EditText view from its parent layout
                                ((ViewGroup)editText.getParent()).removeView(editText);
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



        NavigationBarView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id){
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




    private void requetsWindowFeature(int featureNoTitle) {
    }
}