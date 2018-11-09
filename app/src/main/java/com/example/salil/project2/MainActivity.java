package com.example.salil.project2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button normal = (Button) findViewById(R.id.Normal);
        Button brighness = (Button) findViewById(R.id.Brightness);
        Button keypoints = (Button) findViewById(R.id.Keypoints);
        Button canny = (Button) findViewById(R.id.Canny);
        Button activity3 = (Button) findViewById(R.id.Activity3);

        // Capture button clicks
        normal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent = new Intent(MainActivity.this,
                        Main2Activity.class);
                myIntent.putExtra("Choice", 1);
                startActivity(myIntent);
            }
        });

        brighness.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent = new Intent(MainActivity.this,
                        Main2Activity.class);
                myIntent.putExtra("Choice", 2);
                startActivity(myIntent);
            }
        });

        keypoints.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent = new Intent(MainActivity.this,
                        Main2Activity.class);
                myIntent.putExtra("Choice", 3);
                startActivity(myIntent);
            }
        });

        canny.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent = new Intent(MainActivity.this,
                        Main2Activity.class);
                myIntent.putExtra("Choice", 4);
                startActivity(myIntent);
            }
        });

        activity3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent = new Intent(MainActivity.this,
                        Main3Activity.class);
                myIntent.putExtra("Choice", 5);
                startActivity(myIntent);
            }
        });

    }
}
